package com.interiordesigner.Classes;

import com.interiordesigner.R;

public class Furniture {
    private int id;
    private String name;
    private int categoryId;
    private int photoId;
    private int[] colorsIds;
    private String modelName;
    private float modelRadius;

    public Furniture(int id, String name, int categoryId, int[] colorsIds) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
        this.colorsIds = colorsIds;
        this.modelName = "andy.sfb";
        this.modelRadius = 0.5f;
    }

    public Furniture(int id, String name, int categoryId, int[] colorsIds, String modelName) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
        this.colorsIds = colorsIds;
        this.modelName = modelName;
        this.modelRadius = 0.5f;
    }

    public Furniture(int id, String name, int categoryId, int[] colorsIds, String modelName, float modelRadius) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
        this.colorsIds = colorsIds;
        this.modelName = modelName;
        this.modelRadius = modelRadius;
    }

    public int GetId() { return id; }
    public String GetName() { return name; }
    public int GetCategoryId() { return categoryId; }
    public int GetPhotoId() { return photoId; }
    public int[] GetColorsIds() { return colorsIds; }
    public String GetModelName() { return modelName; }
    public float GetModelRadius() { return modelRadius; }

    public static Furniture[] furnitures = new Furniture[] {
        new Furniture(1, "Angsta", 4, new int[] {1, 2, 3}),
        new Furniture(2, "Asarum", 4, new int[] {1, 3}, ""),
        new Furniture(3, "Nyhamn", 4, new int[] {1}, "andy.sfb", 0.3f)
    };

    public static Furniture GetFurniture(int id) {
        Furniture selectedFurniture = null;

        for (Furniture furniture : furnitures) {
            if (furniture.id == id)
                selectedFurniture = furniture;
        }

        return selectedFurniture;
    }

}
