package com.nicolejade.myarchivesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.nicolejade.myarchivesapp.databinding.ActivityRegisterUserBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//alieya started
//----------CODE ATTRIBUTION----------
//Author: Atif Pervaiz
//Title: Book App Firebase | 02 Login SignUp | Android Studio | Java
//Publish Date: 9 May 2021
//URL: https://youtu.be/SbUtFAu9O7k
//This code allows users to register themselves into the application
public class RegisterUser extends AppCompatActivity{

    //view binding
    private ActivityRegisterUserBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //set up progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);


        //handles registering user button
        binding.registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    //method validates data before registering user
    private String name="", age = "", email = "", password = "";
    private void validateData() {
        //get data
        name = binding.fullName.getText().toString().trim();
        age = binding.age.getText().toString().trim();
        email = binding.email.getText().toString().trim();
        password = binding.password.getText().toString().trim();

        //validate data
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(age)){
            Toast.makeText(this, "Please enter your age", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < 6){
            Toast.makeText(this, "Password should be more than 6 characters", Toast.LENGTH_SHORT).show();
        }
        else{
            createUserAccount();
        }
    }

    //method creates user after validating their data
    private void createUserAccount() {
        //show progress dialog
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        //create user in firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //account created successfully
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to create account
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //method updates user information in firebase database
    private void updateUserInfo() {
        progressDialog.setTitle("Saving User Information");

        //timestamp
        long timestamp = System.currentTimeMillis();

        //get current user uid
        String uid = firebaseAuth.getUid();

        //setup data to db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("name", name);
        hashMap.put("email", email);
        hashMap.put("age", age);
        hashMap.put("profileImage", "");
        hashMap.put("timestamp", timestamp);

        //set data for db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //data added to db successfully
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUser.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterUser.this, ViewCollections.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to add data to db
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
//----------CODE ATTRIBUTION ENDS----------
//alieya ends