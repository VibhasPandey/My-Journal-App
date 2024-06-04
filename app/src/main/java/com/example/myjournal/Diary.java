package com.example.myjournal;

import java.io.Serializable;
import java.util.ArrayList;


public class Diary implements Serializable {
    String dInit;
    String diaryTitle;
    ArrayList<Page>  pages;

    public ArrayList<Page> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public String getdInit() {
        return dInit;
    }

    public void setdInit(String dInit) {
        this.dInit = dInit;
    }



    public String getDiaryTitle() {
        return diaryTitle;
    }

    public void setDiaryTitle(String diaryTitle) {
        this.diaryTitle = diaryTitle;
    }





}
