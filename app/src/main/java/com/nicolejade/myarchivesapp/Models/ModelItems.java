package com.nicolejade.myarchivesapp.Models;

//nicole started
//----------CODE ATTRIBUTION----------
//Author: Alifio Tutorials
//Title: Using Recyclerview to show data from Firebase Database
//Published: 31 July 2018
//URL: https://www.youtube.com/watch?v=vpObpZ5MYSE
public class ModelItems {

    String name, description, creators, price, date, categoryId, image;

    //empty constructor for firebase
    public ModelItems() {
    }

    //constructor with parameters
    public ModelItems(String name, String description, String creators, String price,
                      String date, String categoryId, String image) {
        this.name = name;
        this.description = description;
        this.creators = creators;
        this.price = price;
        this.date = date;
        this.categoryId = categoryId;
        this.image = image;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreators() {
        return creators;
    }

    public void setCreators(String creators) {
        this.creators = creators;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
//----------CODE ATTRIBUTION ENDS----------
//nicole ended
