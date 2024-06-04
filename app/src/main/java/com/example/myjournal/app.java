package com.example.myjournal;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class app extends Application {
    private Diary diary;

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
