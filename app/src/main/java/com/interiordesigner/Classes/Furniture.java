package com.interiordesigner.Classes;

import com.interiordesigner.Interfaces.Card;
import com.interiordesigner.R;

public class Furniture implements Card {
    private int id;
    private String name;
    private int categoryId;
    private int photoId;
    private int[] colorsIds;
    private String photoPath;
    private String modelPath;
    private String[] modelsPaths;
    private float modelRadius;
    private Dimensions3D dimensions;

    public Furniture(int id, String name, int categoryId, int[] colorsIds, String[] modelsPaths, float modelRadius, String imagePath, Dimensions3D dimensions) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.photoId = R.drawable.thumbnail;
        this.colorsIds = colorsIds;
        this.modelsPaths = modelsPaths;
        this.modelRadius = modelRadius;
        this.photoPath = imagePath;
        this.dimensions = dimensions;
    }

    @Override
    public CardType getType() {
        return CardType.Furniture;
    }

    public int getId() { return id; }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public String getText() {
        return this.name;
    }

    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public int getPhotoId() { return photoId; }
    public int[] getColorsIds() { return colorsIds; }
    public String getModelPath() { return modelPath; }
    public String[] getModelsPaths() { return modelsPaths; }
    public float getModelRadius() { return modelRadius; }
    public String getPhotoPath() { return photoPath; }
    public Dimensions2D getDimensions2D() { return dimensions; }
    public Dimensions3D getDimensions3D() { return dimensions; }

    public static Furniture[] Furnitures = new Furniture[] {
        new Furniture( 1, "Simple Table", 2, new int[] {4, 5},
                new String[]{
                        "models/simple_table/simple_table.glb",
                        "models/simple_table/simple_table_v2.glb"
                },
                3, "images/simple_table.png",
                 new Dimensions3D(0.5, 0.5, 0.5)
            )
    };

    public static Furniture getById(int id) {
        Furniture selectedFurniture = null;

        for (Furniture furniture : Furnitures) {
            if (furniture.id == id)
                selectedFurniture = furniture;
        }

        return selectedFurniture;
    }

    public static Furniture[] getByCategoryId(int categoryId) {
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
