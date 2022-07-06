package com.nicolejade.myarchivesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.nicolejade.myarchivesapp.databinding.ActivityViewItemsBinding;
import com.nicolejade.myarchivesapp.Adapters.AdapterItems;
import com.nicolejade.myarchivesapp.Models.ModelItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//nicole started
//----------CODE ATTRIBUTION----------
//Author: Alifio Tutorials
//Title: Using Recyclerview to show data from Firebase Database
//Publish Date: 31 July 2018
//URL: https://www.youtube.com/watch?v=vpObpZ5MYSE
//This code allows users to view their various items in a collection via an adapter and model class
public class ViewItems extends AppCompatActivity {

    //view binding
    private ActivityViewItemsBinding binding;

    //arraylist to hold list of data
    private ArrayList<ModelItems> itemsArrayList;
    //adapter
    private AdapterItems adapterItems;

    private String categoryTitle, categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityViewItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get data from intent
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryTitle = intent.getStringExtra("categoryTitle");

        //set category title
        binding.subTitleTv.setText(categoryTitle);

        loadItemsList();

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //nicole ended

        //alieya started
        //----------CODE ATTRIBUTION----------
        //Author: Atif Pervaiz
        //Title: Book App Firebase | 06 Show Books Admin | Android Studio | Java
        //Publish Date: 6 June 2021
        //URL: https://youtu.be/vgWihyzAv-U
        //text change listener on search bar
        binding.searchBarVi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //called when user types each letter
                try{
                    adapterItems.getFilter().filter(charSequence);
                }
                catch (Exception e){
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //----------CODE ATTRIBUTION ENDS----------
        //alieya ended

        //nicole started
        //handles add new item screen
        binding.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewItems.this, AddItems.class));
            }
        });
    }

    //method that loads all items in the category
    private void loadItemsList() {
        //init array list
        itemsArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        ref.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        itemsArrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ModelItems model = ds.getValue(ModelItems.class);

                            //add to list
                            itemsArrayList.add(model);
                        }
                        //setup adapter
                        adapterItems = new AdapterItems(ViewItems.this, itemsArrayList);
                        binding.itemsRv.setAdapter(adapterItems);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewItems.this, "Oops...something went wrong"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
//----------CODE ATTRIBUTION ENDS----------
//nicole ends