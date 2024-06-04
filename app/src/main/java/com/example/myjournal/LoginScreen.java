package com.example.myjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginScreen extends AppCompatActivity {
    EditText emailText;
    EditText passText;
    Button loginBut;
    TextView signUpButton;
    TextView forgotPass;
    FirebaseAuth auth;
    String mailPattern="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        emailText=findViewById(R.id.editText);
        passText=findViewById(R.id.editText2);
        loginBut=findViewById(R.id.button);
        signUpButton=findViewById(R.id.signUpText);
        forgotPass=findViewById(R.id.forgotP);



        auth=FirebaseAuth.getInstance();


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginScreen.this,SignUp.class);
                startActivity(intent);
                finish();

            }
        });

        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailText.getText().toString();
                String pass=passText.getText().toString();


                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginScreen.this,"Enter email!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginScreen.this,"enter password!",Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<7){
                    passText.setError("password length should be more than 6");
                }
                else if(!email.matches(mailPattern)){
                    emailText.setError("enter correct email");}
                else{
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                try{
                                    Intent intent=new Intent(LoginScreen.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }catch (Exception e){
                                    Toast.makeText(LoginScreen.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginScreen.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });




    }
}