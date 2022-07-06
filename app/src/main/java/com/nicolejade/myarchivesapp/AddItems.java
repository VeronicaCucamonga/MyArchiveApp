package com.nicolejade.myarchivesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.nicolejade.myarchivesapp.databinding.ActivityAddItemsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

//nicole started
//----------CODE ATTRIBUTION----------
//Author: RT Developer
//Title: How to insert and retrieve data with image in RecyclerView with Firebase database || Easy Method
//Publish Date: 17 June 2020
//URL: https://www.youtube.com/watch?v=KuqLbN41Rag
//This code allows users to add their new items to firebase database
public class AddItems extends AppCompatActivity {

    //view binding
    private ActivityAddItemsBinding binding;

    //arraylist for category title and id
    private ArrayList<String> collectionsArrayList, collectionsIdArrayList;

    //date, month and year variables
    private int mDate, mMonth, mYear;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 113;

    //image variables
    private static final int Gallery_Code = 1;
    Uri imageUri = null;

    //firebase auth, database, reference and storage
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;

    //progress dialog
    ProgressDialog progressDialog;

    //get current timestamp
    long timestamp = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityAddItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Items");
        mStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        loadCategories();

        //handle back button clicked
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //----------CODE ATTRIBUTION----------
        //Author: AllCodingTutorials
        //Title: How to Add Date Picker Dialog in Android Studio | DatePicker Dialog in Android App
        //Publish Date: 13 June 2020
        //URL: https://www.youtube.com/watch?v=aJ3PXhp6WFk
        //This code handles picking a date
        binding.dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);
                mYear = cal.get(Calendar.YEAR);
                DatePickerDialog dpd = new DatePickerDialog(AddItems.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        binding.dateTxt.setText(date+"/"+month+"/"+year);
                    }
                }, mYear, mMonth, mDate);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                dpd.show();
            }
        });
        //----------CODE ATTRIBUTION ENDS----------

        //----------CODE ATTRIBUTION----------
        //Author: Atif Pervaiz
        //Title: Book App Firebase | 05 Upload Pdf | Android Studio | Java
        //Published: 29 May 2021
        //URL: https://www.youtube.com/watch?v=b_oSKEBv0fk&list=PLs1bCj3TvmWmtQbEzNewkf-UhBJ2pFr5n&index=5
        //handle choosing category for item
        binding.chooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPickDialog();
            }
        });
        //----------CODE ATTRIBUTION ENDS----------

        binding.uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            }
        });

        //handles add new item button
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    //loads categories from firebase
    private void loadCategories() {
        collectionsArrayList = new ArrayList<>();
        collectionsIdArrayList = new ArrayList<>();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.orderByChild("uid").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collectionsArrayList.clear();
                collectionsArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                    //get id and title of category
                    String categoryId = ""+ds.child("id").getValue();
                    String categoryTitle = ""+ds.child("category").getValue();

                    //add to respective arraylist
                    collectionsArrayList.add(categoryTitle);
                    collectionsIdArrayList.add(categoryId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //displays categories in dialog
    //selected category and id
    private String selectedCategoryTitle, selectedCategoryId;
    private void categoryPickDialog() {
        //get arraylist of categories
        String[] categoryArray = new String[collectionsArrayList.size()];
        for (int i = 0; i<collectionsArrayList.size(); i++){
            categoryArray[i] = collectionsArrayList.get(i);
        }

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a Category")
                .setItems(categoryArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get clicked item
                        selectedCategoryTitle = collectionsArrayList.get(i);
                        selectedCategoryId = collectionsIdArrayList.get(i);
                        binding.chooseCategory.setText(selectedCategoryTitle);
                    }
                }).show();
    }

    //nicole started
    //----------CODE ATTRIBUTION----------
    //Author: Code With Fenil
    //Title: How to Request a Run Time Permission in Android Studio Java || Android Permission Tutorial
    //Publish Date: 13 June 2021
    //URL: https://www.youtube.com/watch?v=q1OLKyilp8M
    //this code allows to verify permission to storage method
    private void verifyPermissions(String permission, int requestCode) {
        //check if permission is granted
        if(ContextCompat.checkSelfPermission(AddItems.this, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(AddItems.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Code);
            }
        }
    }
    //----------CODE ATTRIBUTION ENDS----------
    //nicole ends

    //alieya started
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK){
            imageUri = data.getData();
            Toast.makeText(this, "Image Inserted", Toast.LENGTH_SHORT).show();;
        }
    }
    //alieya ends

    //nicole started
    //validate user input data before uploading to firebase
    String name = "", description = "", creators = "", price = "", date = "null";
    private void validateData() {
        name = binding.itemNameEdt.getText().toString().trim();
        description = binding.itemDescEdt.getText().toString().trim();
        creators = binding.itemPublishEdt.getText().toString().trim();
        price = binding.itemPriceEdt.getText().toString().trim();
        date = binding.dateTxt.getText().toString().trim();

        //validate data
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(creators)){
            Toast.makeText(this, "Enter Creator", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "Enter Price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(selectedCategoryTitle)){
            Toast.makeText(this, "Pick Category", Toast.LENGTH_SHORT).show();
        }
        else if(imageUri == null){
            Toast.makeText(this, "Choose an Image", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadToFirebase();
        }
    }
    //nicole ended

    //alieya started
    //**********CODE ATTRIBUTION**********
    //Author: RT Developer
    //Title: How to insert and retrieve data with image in RecyclerView with Firebase database || Easy Method
    //Published: 17 June 2020
    //URL: https://www.youtube.com/watch?v=KuqLbN41Rag
    //calls method to upload data to firebase if it correct
    private void uploadToFirebase() {
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference filePath = mStorage.getReference().child("imagePost").child(imageUri.getLastPathSegment());
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String t = task.getResult().toString();

                        String uid = firebaseAuth.getUid();

                        DatabaseReference newPost = mRef.push();

                        newPost.child("uid").setValue(uid);
                        newPost.child("id").setValue(timestamp);
                        newPost.child("name").setValue(name);
                        newPost.child("description").setValue(description);
                        newPost.child("creators").setValue(creators);
                        newPost.child("price").setValue(price);
                        newPost.child("date").setValue(date);
                        newPost.child("categoryId").setValue(selectedCategoryId);
                        newPost.child("image").setValue(task.getResult().toString());
                        progressDialog.dismiss();
                    }
                });
            }
        });
        Toast.makeText(this, "Item Uploaded Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddItems.this, ViewCollections.class));
    }
    //----------CODE ATTRIBUTION ENDS----------
    //alieya ends
}
//----------CODE ATTRIBUTION ENDS----------
//nicole ends