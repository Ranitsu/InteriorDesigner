package com.interiordesigner.Classes;

import com.interiordesigner.Interfaces.Card;

import java.util.ArrayList;
import java.util.List;

public class Category implements Card {
    private int id;
    private String name;
    private int parentId;
    private int level;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public Category(int id, String name, int parentId, int level) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
    }

    public int GetId() { return this.id; }
    public String GetName() { return this.name; }
    public int GetParentId() { return this.parentId; }
    public int GetLevel() { return this.level; }

    public static Category[] Categories = new Category[] {
            new Category(1, "Test objects", 1),
            new Category(10, "Test objects v2", 1),
            new Category(2, "Table and chairs", 1, 2),
            new Category(3, "Sofas and armchairs", 1, 2),

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

    @Override
    public CardType getType() {
        return CardType.Category;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public String getText() {
        return this.name;
    }
}
