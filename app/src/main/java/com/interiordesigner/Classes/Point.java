package com.interiordesigner.Classes;

import android.graphics.Color;
import android.graphics.drawable.shapes.OvalShape;

public class Point {
    int x;
    int y;

    public int getX() { return x; }
    public int getY() { return y; }

    public Point(int x, int y, int r) {
        this.x = x;
        this.y = y;
    }

    public void SetXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }

}
