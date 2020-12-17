package com.interiordesigner.Classes;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private int parentId;

    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, int parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public int GetId() { return this.id; }
    public String GetName() { return this.name; }
    public int GetParentId() { return this.parentId; }

    public static Category[] Categories = new Category[] {
            new Category(1, "Test objects"),
            new Category(2, "Table and chairs", 1),
            new Category(3, "Sofas and armchairs", 1),

//            new Category(1, "Sofas and armchairs"),
//            new Category(2, "Wardrobes and chests of drawers"),
//            new Category(3, "Beds"),
//
//            new Category(10, "Sofas", 1),
//            new Category(11, "Armchairs", 1),
//            new Category(12, "Footrests", 1),
//
//            new Category(20, "Wardrobes", 2),
//            new Category(21, "Chests of drawers", 2),
//            new Category(22, "Racks", 2)
    };

    public static Category GetById(int id) {
        for (Category category: Categories) {
            if (category.id == id)
                return category;
        }
        return null;
    }

    public static Category[] GetByParentId(int parentId) {
        Category[] categories;
        int counter = 0;

        for (Category category: Categories) {
            if (category.parentId == parentId)
                counter++;
        }

        int index = 0;
        categories = new Category[counter];
        for (Category category: Categories) {
            if (category.parentId == parentId) {
                categories[index] = category;
                index++;
            }

            if (index == counter)
                break;
        }

        return categories;
    }
}
