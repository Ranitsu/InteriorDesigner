package com.interiordesigner.Classes;

import android.graphics.Color;
import android.graphics.drawable.shapes.OvalShape;

public class PlanPoint {
    int x;
    int y;
    int radius;
    int color;
    OvalShape shape;

    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }

    public PlanPoint(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.radius = r;
    }

}
