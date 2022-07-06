package com.nicolejade.myarchivesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicolejade.myarchivesapp.databinding.PieChartLayoutBinding;
import com.nicolejade.myarchivesapp.Models.ModelCollections;
import com.github.lzyzsd.circleprogress.DonutProgress;
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
//This code creates an adapter that connects the layout and class for a recyclerview
public class AdapterPieChart extends RecyclerView.Adapter<AdapterPieChart.HolderCategory>  {

    //context and arraylist
    public ArrayList<ModelCollections> modelCollectionsArrayList;
    private Context context;

    private int countItem;
    private double percentage;
    private double goalItemss;

    //constructor
    public AdapterPieChart(ArrayList<ModelCollections> modelCollectionsArrayList, Context context) {
        this.modelCollectionsArrayList = modelCollectionsArrayList;
        this.context = context;
    }

    //view binding
    private PieChartLayoutBinding binding;

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //binding row collections layout
        binding = PieChartLayoutBinding.inflate(LayoutInflater.from(context), parent, false);

        return new AdapterPieChart.HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        //get data
        ModelCollections model = modelCollectionsArrayList.get(position);
        String category = model.getCategory();
        String goalItems = model.getGoalItems();
        goalItemss = Integer.parseInt(goalItems);

        //set data
        holder.categoryName.setText(category);

        loadItems(model, holder);
    }

    //method that loads the number of items in a category
    private void loadItems(ModelCollections model, AdapterPieChart.HolderCategory holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
        ref.orderByChild("categoryId").equalTo(model.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //count item is used to get the number of items for each category
                        countItem = (int)snapshot.getChildrenCount();

                        percentage = (countItem/goalItemss)*100;
                        holder.donutProgress.setProgress((float) percentage);

                        if(countItem>=goalItemss){
                            holder.donutProgress.setProgress(100);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return modelCollectionsArrayList.size();
    }

    class HolderCategory extends RecyclerView.ViewHolder {

        //ui views of row items layout
        TextView categoryName;
        DonutProgress donutProgress;

        public HolderCategory(@NonNull View itemView) {
            super(itemView);

            //init views
            categoryName = binding.categoryTv;
            donutProgress = binding.donutProgress;
        }
    }
}
//----------CODE ATTRIBUTION ENDS----------
//alieya ends
