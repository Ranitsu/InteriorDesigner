package com.interiordesigner.Classes;

public class FurnitureOnPlan extends Furniture {
    private Point position;
    private float angle;

    public FurnitureOnPlan(Furniture furniture) {
        super(furniture.getId(), furniture.getName(), furniture.getCategoryId(),
            furniture.getColorsIds(), furniture.getModelsPaths(), furniture.getModelRadius(),
            furniture.getPhotoPath(), furniture.getDimensions3D());
    }

    public FurnitureOnPlan(int id, String name, int categoryId, int[] colorsIds, String[] modelsPaths, float modelRadius, String imagePath, Dimensions3D dimensions) {
        super(id, name, categoryId, colorsIds, modelsPaths, modelRadius, imagePath, dimensions);
    }

    public Point getPosition() { return position; }
    public float getAngle() { return angle; }

    public void setPosition(Point newPosition) {
        position = newPosition;
    }

    public void setAngle(float newAngle) {
        angle = newAngle;
    }
}
