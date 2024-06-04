package com.example.myjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PopUpClass.PopupCallback {
   private RecyclerView recyclerView;
   private RecyclerViewAdapter recyclerViewAdapter;
   private ArrayList<Diary>  userDiaries;
   private ArrayList<String>  arrayAdapter;
   FirebaseDatabase database;
   Diary newDiary;


    ImageView addBut;
    private  String jTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        database=FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addBut=findViewById(R.id.addPop);
        final PopUpClass popUpClass = new PopUpClass(); // Create an instance of PopUpClass
        popUpClass.setCallback(this);
        addBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpClass.showPopUp(view);

            }
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        fetchUserDiaries();
        TextView textView=findViewById(R.id.textView3);
        textView.setVisibility(View.INVISIBLE);

    }

    private void fetchUserDiaries() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String id=user.getUid();
            DatabaseReference reference=database.getReference().child("user").child(id).child("Diaries");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userDiaries = new ArrayList<>();
                    //check if snapshot exists
                    if(dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Diary diary = snapshot.getValue(Diary.class);
                            if (diary != null) {
                                userDiaries.add(diary);
                            }else{
                                userDiaries=new ArrayList<>();// initializing the arraylist to not make it stay null
                            }
                        }
                    }

                    // Update RecyclerView with all user's diary entries
                    updateRecyclerView();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Error fetching user diaries", error.toException());
                }
            });
        }else {
            // Initialize userDiaries with an empty list if the user is null
            userDiaries = new ArrayList<>();
            // Update RecyclerView with the empty list
            updateRecyclerView();
        }
    }

    private void updateRecyclerView() {
        if (userDiaries != null) {
            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, userDiaries);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    @Override
    public void onTitleSet(String title) {
        jTitle = title;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String id = user.getUid();
            newDiary = new Diary();
            newDiary.setDiaryTitle(jTitle);
            DatabaseReference reference = database.getReference().child("user").child(id).child("Diaries").child(jTitle);

            reference.setValue(newDiary).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Logic goes here
//                        userDiaries = new ArrayList<>();
//                        userDiaries.add(newDiary);
//                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, userDiaries);
//                        recyclerView.setAdapter(recyc
//                       lerViewAdapter);
                        fetchUserDiaries();
                        ((app) getApplication()).setDiary(newDiary);
                        // Use code that helps in writing down pages
                    } else {
                        Log.e("Firebase", "Error creating diary", task.getException());
                    }
                }
            });
        }
    }

}