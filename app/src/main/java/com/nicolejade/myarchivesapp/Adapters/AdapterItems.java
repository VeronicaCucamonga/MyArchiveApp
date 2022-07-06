package com.nicolejade.myarchivesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicolejade.myarchivesapp.R;
import com.nicolejade.myarchivesapp.Filters.FilterItems;
import com.nicolejade.myarchivesapp.Models.ModelItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicolejade.myarchivesapp.databinding.RowItemLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//nicole started
//----------CODE ATTRIBUTION----------
//Author: Alifio Tutorials
//Title: Using Recyclerview to show data from Firebase Database
//Publish Date: 31 July 2018
//URL: https://www.youtube.com/watch?v=vpObpZ5MYSE
//This code allows creates an adapter that connects the layout and class for a recyclerview
public class AdapterItems extends RecyclerView.Adapter<AdapterItems.HolderItems> implements Filterable {

    //context and arraylist
    private Context context;
    public ArrayList<ModelItems> itemsArrayList, filterList;

    //view binding
    private RowItemLayoutBinding binding;

    //instance filter class
    private FilterItems filter;

    //constructor
    public AdapterItems(Context context, ArrayList<ModelItems> itemsArrayList) {
        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.filterList = itemsArrayList;
    }

    @NonNull
    @Override
    public HolderItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind the row layout
        binding = RowItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderItems(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItems holder, int position) {

        ModelItems modelItem = itemsArrayList.get(position);

        holder.name.setText(modelItem.getName());
        holder.desc.setText(modelItem.getDescription());
        holder.creator.setText(modelItem.getCreators());
        holder.date.setText(modelItem.getDate());
        holder.price.setText(modelItem.getPrice());
        Picasso.get().load(itemsArrayList.get(position).getImage()).into(holder.imageView);

        loadCategories(modelItem, holder);
    }

    //method that gets the category name of each item
    private void loadCategories(ModelItems modelItem, HolderItems holder) {
        //get category using categoryId
        String categoryId = modelItem.getCategoryId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(categoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get category
                        String category = ""+snapshot.child("category").getValue();

                        //set text to category
                        holder.category.setText(category);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    //alieya started
    //----------CODE ATTRIBUTION----------
    //Author: Atif Pervaiz
    //Title: Book App Firebase | 06 Show Books Admin | Android Studio | Java
    //Publish Date: 6 June 2021
    //URL: https://youtu.be/vgWihyzAv-U
    //filters list of categories
    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterItems(filterList, this);
        }
        return filter;
    }
    //----------CODE ATTRIBUTION ENDS----------
    //alieya ended

    //view holder class for row items
    class HolderItems extends RecyclerView.ViewHolder{

        //ui views of row items layout
        ImageView imageView;
        TextView name, desc, creator, date, category, price;

        public HolderItems(@NonNull View itemView) {
            super(itemView);

            //init views
            imageView = itemView.findViewById(R.id.item_image_ll);
            name = itemView.findViewById(R.id.item_title_tv_ll);
            desc = itemView.findViewById(R.id.item_desc_tv_ll);
            creator = itemView.findViewById(R.id.item_creators_tv_ll);
            date = itemView.findViewById(R.id.item_date_tv_ll);
            category = itemView.findViewById(R.id.item_category_tv_ll);
            price = itemView.findViewById(R.id.item_price_tv_ll);
        }
    }
}
//----------CODE ATTRIBUTION ENDS----------
//nicole ends
