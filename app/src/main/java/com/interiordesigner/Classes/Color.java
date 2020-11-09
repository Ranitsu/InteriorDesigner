package com.interiordesigner.Classes;


import android.content.res.Resources;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.interiordesigner.R;

import java.util.ArrayList;
import java.util.List;

public class Color {
    private int id;
    private String name;
    private int miniatureColor;
    private int miniatureBorderColor;

    public Color(String name) {
        this.name = name;
        this.miniatureColor = R.color.blue;
    }

    public Color(String name, int miniatureColor) {
        this.name = name;
        this.miniatureColor = miniatureColor;
        this.miniatureBorderColor = miniatureColor;
    }

    public Color(int id, String name, int miniatureColor) {
        this.id = id;
        this.name = name;
        this.miniatureColor = miniatureColor;
        this.miniatureBorderColor = miniatureColor;
    }

    public Color(int id, String name, int miniatureColor, int miniatureBorderColor) {
        this.id = id;
        this.name = name;
        this.miniatureColor = miniatureColor;
        this.miniatureBorderColor = miniatureBorderColor;
    }

    public int GetId() { return this.id; }
    public String GetName() { return this.name; }
    public int GetMiniatureColor() {
        return this.miniatureColor;
    }
    public int GetMiniatureBorderColor() {return this.miniatureBorderColor; }


    public static Color[] Colors = new Color[] {
        new Color(1, "Blue", R.color.blue),
        new Color(2, "Green", R.color.green),
        new Color(3, "Green", R.color.red),
        new Color(4, "Original", R.color.gray),
        new Color(5, "Red-Black", R.color.red, R.color.black),
    };

    public static Color GetById(int id) {
        if (id > Colors.length)
            return null;
        return Colors[id-1];
    }

}
