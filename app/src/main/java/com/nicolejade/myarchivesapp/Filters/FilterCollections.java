package com.nicolejade.myarchivesapp.Filters;

import android.widget.Filter;

import com.nicolejade.myarchivesapp.Adapters.AdapterCollections;
import com.nicolejade.myarchivesapp.Models.ModelCollections;

import java.util.ArrayList;

//alieya started
//----------CODE ATTRIBUTION----------
//Author: Atif Pervaiz
//Title: Book App Firebase | 04 Show Book Categories | Android Studio | Java
//Publish Date: 23 May 2021
//URL: https://youtu.be/j6GrP2MdFos
//This code allows to filter the categories in the recyclerview
public class FilterCollections extends Filter {

    //arraylist we want to filter from
    ArrayList<ModelCollections> filterList;

    //adapter filter will be implemented
    AdapterCollections adapterCollections;

    //constructor
    public FilterCollections(ArrayList<ModelCollections> filterList, AdapterCollections adapterCollections) {
        this.filterList = filterList;
        this.adapterCollections = adapterCollections;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        //value should not be null or empty
        if(charSequence != null && charSequence.length() > 0){
            //Change to uppercase or lowercase
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ModelCollections> filterModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                //validate
                if(filterList.get(i).getCategory().toUpperCase().contains(charSequence)){
                    //add to filter array
                    filterModels.add(filterList.get(i));
                }
            }
            results.count = filterModels.size();
            results.values = filterModels;
        }
        else{
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        //apply filter category
        adapterCollections.modelCollectionsArrayList = (ArrayList<ModelCollections>)filterResults.values;

        //notify changes
        adapterCollections.notifyDataSetChanged();
    }
}
//----------CODE ATTRIBUTION ENDS----------
//alieya ends
