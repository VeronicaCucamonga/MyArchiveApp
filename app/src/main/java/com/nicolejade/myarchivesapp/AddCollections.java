package com.nicolejade.myarchivesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.nicolejade.myarchivesapp.databinding.ActivityAddCollectionsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//nicole started
//----------CODE ATTRIBUTION----------
//Author: Atif Pervaiz
//Title: Book App Firebase | 03 Add Book Category | Android Studio | Java
//Publish Date: 15 May 2021
//URL: https://youtu.be/TkBos_Flc4k
//This code allows users to add their new collections to firebase database
public class AddCollections extends AppCompatActivity {

    //view binding
    private ActivityAddCollectionsBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityAddCollectionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle click of back button
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //handle click when adding category
        binding.addCategoryBtnAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    //method that validates data before adding category
    private String category = "", goalItems = "";
    private void validateData() {
        //get data
        category = binding.categoryEdit.getText().toString().trim();
        goalItems = binding.goalEdit.getText().toString().trim();

        //validate data
        if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(goalItems)){
            Toast.makeText(this, "Please enter target number of items", Toast.LENGTH_SHORT).show();
        }
        else{
            //call upload to firebase method
            uploadToFirebase();
        }
    }

    //method uploads users collection data to firebase database after validation
    private void uploadToFirebase() {

        progressDialog.setMessage("Adding category...");
        progressDialog.show();

        //timestamp
        long timestamp = System.currentTimeMillis();

        //set up info to add in firebase
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", ""+timestamp);
        hashMap.put("category", ""+category);
        hashMap.put("goalItems", ""+goalItems);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("uid", ""+firebaseAuth.getUid());

        //add to firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void unused){
                        //success
                        progressDialog.dismiss();
                        Toast.makeText(AddCollections.this, "Category Added Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCollections.this, ViewCollections.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        //failure
                        progressDialog.dismiss();
                        Toast.makeText(AddCollections.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
//----------CODE ATTRIBUTION ENDS----------
//nicole ends