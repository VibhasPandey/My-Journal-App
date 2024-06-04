package com.example.myjournal;

import java.util.ArrayList;

public class UserBase {
    String dp,uID,name,email,password;

    public UserBase(){}

    public UserBase( String uID, String name, String email, String password) {

        this.uID = uID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserBase(String dp) {
        this.dp = dp;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
