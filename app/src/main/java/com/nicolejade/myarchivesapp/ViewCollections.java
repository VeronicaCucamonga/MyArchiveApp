package com.nicolejade.myarchivesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.nicolejade.myarchivesapp.R;
import com.nicolejade.myarchivesapp.databinding.ActivityViewCollectionsBinding;
import com.nicolejade.myarchivesapp.Adapters.AdapterCollections;
import com.nicolejade.myarchivesapp.Models.ModelCollections;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//nicole started
//----------CODE ATTRIBUTION----------
//Author: Atif Pervaiz
//Title: Book App Firebase | 04 Show Book Categories | Android Studio | Java
//Publish Date: 23 May 2021
//URL: https://youtu.be/j6GrP2MdFos
//This code allows users to view their various collections via an adapter and model class
public class ViewCollections extends AppCompatActivity {

    //view binding
    private ActivityViewCollectionsBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //arraylist to store categories
    private ArrayList<ModelCollections> collectionsArrayList;

    //adapter
    private AdapterCollections adapterCollections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityViewCollectionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadCategories();
        //nicole ends

        //alieya started
        //text change listener on search bar
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //called when user types each letter
                try{
                    adapterCollections.getFilter().filter(charSequence);
                }
                catch (Exception e){
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //alieya ends

        //nicole starts
        //handle add new category screen
        binding.addCategoryBtnVc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewCollections.this, AddCollections.class));
            }
        });

        //nicole started
        //----------CODE ATTRIBUTION----------
        //Author: Stevdza-San
        //Title: Swipe to Refresh Function | Android Studio
        //Publish Date: 26 May 2019
        //URL: https://youtu.be/rGTy8sjcuG8
        //This code allows users to refresh their categories if categories, number of goals or items is not displayed
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCategories();

                binding.refreshLayout.setRefreshing(false);
            }
        });
        //----------CODE ATTRIBUTION ENDS----------

        //----------CODE ATTRIBUTION----------
        //Author: Lemon Soft
        //Title: How to Implement Bottom Navigation With Activities in Android Studio | BottomNav | Lemon Soft
        //Publish Date: 16 May 2022
        //URL: https://youtu.be/y4arA4hsok8
        //This code allows uses to make use of a navigation bar and change activities
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_nav:
                        startActivity(new Intent(getApplicationContext(), ViewCollections.class));
                        break;

                    case R.id.profile_nav:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;

                    case R.id.progress_nav:
                        startActivity(new Intent(getApplicationContext(), BarChartActivity.class));
                        break;

                    case R.id.other_nav:
                        startActivity(new Intent(getApplicationContext(), ChromeGame.class));
                        break;

                    default:
                }

                return true;
            }
        });
        //----------CODE ATTRIBUTION ENDS----------
    }

    //method loads all categories from the firebase database
    private void loadCategories() {
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
                adapterCollections = new AdapterCollections(ViewCollections.this, collectionsArrayList);
                //setup adapter to recyclerview
                binding.collectionsRv.setAdapter(adapterCollections);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //method checks if user is logged in or not
    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseAuth == null){
            //go to login screen if not logged in
            startActivity(new Intent(ViewCollections.this, LoginActivity.class));
            finish();
        }
        else{
            //logged in
            String email = firebaseUser.getEmail();
            //set in textview
            binding.subTitle.setText(email);
        }
    }
}
//----------CODE ATTRIBUTION ENDS----------
//nicole ends