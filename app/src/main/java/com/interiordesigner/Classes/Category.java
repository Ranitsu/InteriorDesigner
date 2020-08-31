package com.interiordesigner.Classes;

import java.util.ArrayList;
import java.util.List;

public class Category {
    int id;
    String name;
    int parentId;

    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name, int parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public Category(int id, String name, int parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public int GetId() { return this.id; }
    public String GetName() { return this.name; }
    public int GetParentId() { return this.parentId; }

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Sofas and armchairs"));
        categories.add(new Category("Wardrobes and chests of drawers"));
        categories.add(new Category("Beds"));

        return categories;
    }

    public static List<Category> getCategoriesLv2() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Sofas", 1));
        categories.add(new Category("Armchairs", 1));
        categories.add(new Category("Footrests", 1));

        categories.add(new Category("Wardrobes", 2));
        categories.add(new Category("Chests of drawers", 2));
        categories.add(new Category("Racks", 2));

        return categories;
    }
}
