package com.example.myjournal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class InsideDiaryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewRecyclerViewAdapter newRecyclerViewAdapter;
    ImageView writeNew;
    ImageView backButton;
    Page newPage;
    Diary currentDiary;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_diary);
        writeNew =findViewById(R.id.imageView1);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        database=FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        writeNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPage=new Page();
                createDate();
                addNewPage();

            }

        });
        backButton=findViewById(R.id.imageView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void createDate() {
        Date currentDate = new Date();
        newPage.setDateCreated(currentDate);


    }


    private void addNewPage() {
        currentDiary=((app) getApplication()).getDiary();
        currentDiary.pages=new ArrayList<>();//add to constructor of diary
        currentDiary.pages.add(newPage);
        int index=currentDiary.pages.indexOf(newPage);
        String title=getIntent().getStringExtra("title");
        //set heading
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String id=user.getUid();
            DatabaseReference databaseReference=database.getReference().child("user").child(id).child("Diaries").child(title).child("pages").child("page"+index);
//            databaseReference.setValue(currentDiary.pages.get(index)).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        updateNewRecyclerView();
//                        Intent intent=new Intent(InsideDiaryActivity.this,EditPageActivity.class);
//                        intent.putExtra("pgno","page"+index);
//                        intent.putExtra("title",title);
//                        startActivity(intent);
//
//                    }
//
//                }
//            });
            Log.d("PRASOON", "Initial Log");
            Task<Void> taskSetValue = databaseReference.setValue(currentDiary.pages.get(index));
            taskSetValue.addOnCompleteListener((OnCompleteListener<Void>) task -> {
                Log.d("PRASOON", "Inside OnCompleteListene callback");
                if(task.isSuccessful()){
                    updateNewRecyclerView();
                    Intent intent=new Intent(InsideDiaryActivity.this,EditPageActivity.class);
                    intent.putExtra("pgno","page"+index);
                    intent.putExtra("title",title);
                    startActivity(intent);

                } else {
                    Log.d("PRASOON", "Task is unsuccessful");
                }
            });

            taskSetValue.addOnFailureListener(e -> Log.d("PRASOON LOG---", "Task to set value of page has FAILED"));
            taskSetValue.addOnCanceledListener(() -> Log.d("PRASOON LOG---", "Task to set value of page has been CANCELLED"));
        }

    }

    private void updateNewRecyclerView() {
        if (currentDiary.pages != null) {
            newRecyclerViewAdapter = new NewRecyclerViewAdapter(InsideDiaryActivity.this, currentDiary.pages);
            recyclerView.setAdapter(newRecyclerViewAdapter);
        }
    }
}