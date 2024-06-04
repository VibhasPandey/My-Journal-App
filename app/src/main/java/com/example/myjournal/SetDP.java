package com.example.myjournal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetDP extends AppCompatActivity {

    CircleImageView dPic;
    Button setDP,skip;
    FirebaseStorage storage;
    Uri imageUri;
    String dpUri;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dp);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        dPic=findViewById(R.id.profilerg);
        setDP=findViewById(R.id.setdp);
        skip=findViewById(R.id.skip);
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();


        // creating the option to choose image
        dPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select picture"),7);
            }
        });

        setDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Get the current user
                if (user != null) {
                    String id = user.getUid(); // Get the user's UID
                    StorageReference storageReference = storage.getReference().child("Upload").child(id);
                    if (imageUri != null) {
                        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            dpUri=uri.toString();
                                            UserBase ub=new UserBase(dpUri);
                                            DatabaseReference reference=database.getReference().child("user").child(id).child("dp");
                                            reference.setValue(ub).addOnCompleteListener(new OnCompleteListener<Void>() { //setting user base contents to the cloud base
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Intent intent=new Intent(SetDP.this,MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }else{
                                                        // Handle upload failure
                                                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                                                        Toast.makeText(SetDP.this, "Error uploading image: " + errorMessage, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });



                                        }
                                    });

                                }else{
                                    // Handle upload failure
                                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                                    Toast.makeText(SetDP.this, "Error uploading image: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(SetDP.this,"DP not found",Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(SetDP.this,"User not found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String id = user.getUid();

                    dpUri = "https://firebasestorage.googleapis.com/v0/b/myjournal-5c937.appspot.com/o/Vector.png?alt=media&token=c95c2718-aff8-422d-984c-7716e5006183";
                    UserBase ub = new UserBase(dpUri);


                    DatabaseReference reference = database.getReference().child("user").child(id);
                    reference.setValue(ub).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(SetDP.this,MainActivity.class);
                                startActivity(intent);
                                finish();

                            }else{
                                Toast.makeText(SetDP.this,"Error creating user",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }else{
                    Toast.makeText(SetDP.this,"Error creating user",Toast.LENGTH_SHORT).show();
                }
            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==7){
            if(data!=null){
                imageUri=data.getData();
                dPic.setImageURI(imageUri);
            }
        }
    }
}