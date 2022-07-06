package com.nicolejade.myarchivesapp.Filters;

import android.widget.Filter;

import com.nicolejade.myarchivesapp.Adapters.AdapterItems;
import com.nicolejade.myarchivesapp.Models.ModelItems;

import java.util.ArrayList;

//alieya started
//----------CODE ATTRIBUTION----------
//Author: Atif Pervaiz
//Title: Book App Firebase | 06 Show Books Admin | Android Studio | Java
//Publish Date: 6 June 2021
//URL: https://youtu.be/vgWihyzAv-U
//This code allows to filter the items in the recyclerview
public class FilterItems extends Filter {

    //arraylist we want to filter from
    ArrayList<ModelItems> filterList;

    //adapter filter will be implemented
    AdapterItems adapterItems;

    //constructor
    public FilterItems(ArrayList<ModelItems> filterList, AdapterItems adapterItems) {
        this.filterList = filterList;
        this.adapterItems = adapterItems;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        //value should not be null or empty
        if(charSequence != null && charSequence.length() > 0){
            //Change to uppercase or lowercase
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ModelItems> filterModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                //validate
                if(filterList.get(i).getName().toUpperCase().contains(charSequence)){
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
        adapterItems.itemsArrayList = (ArrayList<ModelItems>)filterResults.values;

        //notify changes
        adapterItems.notifyDataSetChanged();
    }
}
//----------CODE ATTRIBUTION ENDS----------
//alieya ends
