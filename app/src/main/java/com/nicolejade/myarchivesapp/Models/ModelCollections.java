package com.nicolejade.myarchivesapp.Models;

//nicole started
//**********CODE ATTRIBUTION**********
//Author: Atif Pervaiz
//Title: Book App Firebase | 04 Show Book Categories | Android Studio | Java
//Published: 23 May 2021
//URL: https://youtu.be/j6GrP2MdFos
public class ModelCollections {

    String id, category, goalItems, totalItems;

    //empty constructor for firebase
    public ModelCollections() {
    }

    //constructor with parameters
    public ModelCollections(String id, String category, String goalItems, String totalItems) {
        this.category = category;
        this.goalItems = goalItems;
        this.totalItems = totalItems;
    }

    //Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGoalItems() {
        return goalItems;
    }

    public void setGoalItems(String goalItems) {
        this.goalItems = goalItems;
    }

    public String getTotalItems() { return totalItems; }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }
}
//----------CODE ATTRIBUTION----------
//nicole ended
