package com.example.myjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    EditText uName;
    EditText emailText;
    EditText passText;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button register;
    TextView login;
    String mailPattern="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        uName=findViewById(R.id.editText);
        emailText=findViewById(R.id.editText2);
        passText=findViewById(R.id.editText3);
        register=findViewById(R.id.button);
        login=findViewById(R.id.login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailText.getText().toString();
                String name=uName.getText().toString();
                String password=passText.getText().toString();

                if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(name)|| TextUtils.isEmpty(password)){
                    Toast.makeText(SignUp.this,"Field can't be empty",Toast.LENGTH_SHORT).show();
                }
                else if(!email.matches(mailPattern)){
                    emailText.setError("invalid email");
                }
                else if(password.length()<6){
                    passText.setError("password length should be more than 6");
                }
                else{

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id=task.getResult().getUser().getUid();  //fetch the fresh uid of the newly created user
                                DatabaseReference reference=database.getReference().child("user").child(id);

                                UserBase ub=new UserBase(id, name, email,password);
                                MainActivity mainActivity=new MainActivity();

                                reference.setValue(ub).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent intent=new Intent(SignUp.this,SetDP.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("Firebase", "Error creating user", task.getException());
                                        }

                                    }
                                });


                            }else{
                                Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }




}
