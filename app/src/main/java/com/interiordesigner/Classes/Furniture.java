package com.interiordesigner.Classes;

import com.interiordesigner.R;

public class Furniture {
    private int id;
    private String name;
    private int categoryId;
    private int photoId;
    private int[] colorsIds;
    private String modelPath;
    private String[] modelsPaths;
    private float modelRadius;

    public Furniture(int id, String name, int categoryId, int[] colorsIds) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
        this.colorsIds = colorsIds;
        this.modelPath = "models/cube.glb";
        this.modelRadius = 0.5f;
    }

    public Furniture(int id, String name, int categoryId, int[] colorsIds, String modelPath) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
        this.colorsIds = colorsIds;
        this.modelPath = modelPath;
        this.modelRadius = 0.5f;
    }

    public Furniture(int id, String name, int categoryId, int[] colorsIds, String modelPath, float modelRadius) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
        this.colorsIds = colorsIds;
        this.modelPath = modelPath;
        this.modelRadius = modelRadius;
    }

    public Furniture(int id, String name, int categoryId, int[] colorsIds, String[] modelsPaths, float modelRadius) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
        this.colorsIds = colorsIds;
        this.modelsPaths = modelsPaths;
        this.modelRadius = modelRadius;
    }

    public int GetId() { return id; }
    public String GetName() { return name; }
    public int GetCategoryId() { return categoryId; }
    public int GetPhotoId() { return photoId; }
    public int[] GetColorsIds() { return colorsIds; }
    public String GetModelPath() { return modelPath; }
    public String[] GetModelsPaths() { return modelsPaths; }
    public float GetModelRadius() { return modelRadius; }

    public static Furniture[] Furnitures = new Furniture[] {
        new Furniture(1, "Simple Table", 2, new int[] {4, 5}, new String[]{"models/simple_table/simple_table.glb", "models/simple_table/simple_table_v2.glb"}, 3)
    };

    public static Furniture GetById(int id) {
        Furniture selectedFurniture = null;

        for (Furniture furniture : Furnitures) {
            if (furniture.id == id)
                selectedFurniture = furniture;
        }

        return selectedFurniture;
    }

    public static Furniture[] GetByCategoryId(int categoryId) {
        Furniture[] furnitures;
        int counter = 0;

        for (Furniture furniture: Furnitures) {
            if (furniture.categoryId == categoryId)
                counter++;
        }

        int index = 0;
        furnitures = new Furniture[counter];
        for (Furniture furniture: Furnitures) {
            if (furniture.categoryId == categoryId) {
                furnitures[index] = furniture;
                index++;
            }

            if (index == counter)
                break;
        }

        return furnitures;
    }

}
