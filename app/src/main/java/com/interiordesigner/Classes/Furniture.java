package com.interiordesigner.Classes;

import com.interiordesigner.R;

public class Furniture {
    int id;
    String name;
    int categoryId;
    int photoId;

    public Furniture(String name) {
        this.name = name;
        this.photoId = R.drawable.thumbnail;
    }

    public Furniture(int id, String name) {
        this.id = id;
        this.name = name;
        this.photoId = R.drawable.thumbnail;
    }

    public Furniture(int id, String name, int categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
    }

    public int GetId() { return id; }
    public String GetName() { return name; }
    public int GetCategoryId() { return categoryId; }
    public int GetPhotoId() { return photoId; }
}
