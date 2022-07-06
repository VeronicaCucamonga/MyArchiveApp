package com.nicolejade.myarchivesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.nicolejade.myarchivesapp.databinding.ActivityBarChartBinding;
import com.nicolejade.myarchivesapp.Adapters.AdapterPieChart;
import com.nicolejade.myarchivesapp.Models.ModelCollections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//alieya started
//----------CODE ATTRIBUTION----------
//Author: Atif Pervaiz
//Title: Book App Firebase | 04 Show Book Categories | Android Studio | Java
//Publish Date: 23 May 2021
//URL: https://youtu.be/j6GrP2MdFos
public class BarChartActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //view binding
    private ActivityBarChartBinding binding;

    //arraylist to store categories
    private ArrayList<ModelCollections> collectionsArrayList;

    //adapter
    private AdapterPieChart adapterPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityBarChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //handle back button clicked
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loadChart();
    }

    //method which loads all the charts from adapter
    private void loadChart(){
        //init arraylist
        collectionsArrayList = new ArrayList<>();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        //get all categories from firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.orderByChild("uid").equalTo(uid).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear arraylist before adding data
                        collectionsArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            //get data
                            ModelCollections model = ds.getValue(ModelCollections.class);

                            //add to arraylist
                            collectionsArrayList.add(model);
                        }
                        //setup adapter
                        adapterPieChart = new AdapterPieChart(collectionsArrayList, BarChartActivity.this);
                        //setup adapter to recyclerview
                        binding.chartRv.setAdapter(adapterPieChart);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}
//----------CODE ATTRIBUTION ENDS----------
//alieya ends