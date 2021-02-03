package com.interiordesigner.Classes;

public class Dimensions3D extends Dimensions2D {
    protected double height;

    public double getHeight() { return height; }

    public Dimensions3D (double width, double length, double height) {
        super(width, length);
        this.height = height;
    }
}
