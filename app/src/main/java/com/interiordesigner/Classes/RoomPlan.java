package com.interiordesigner.Classes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class RoomPlan {
    int id;
    int projectId;
    List<Point> points;
    List<FurnitureOnPlan> furnitures;
    boolean isComplete;

    public RoomPlan(int projId, List<Point> points, boolean isComplete) {
        this.projectId = projId;
        this.points = points;
        this.isComplete = isComplete;
        this.furnitures = new ArrayList<>();
    }

    public RoomPlan(int id, int projectId, List<Point> points, boolean isComplete) {
        this.id = id;
        this.projectId = projectId;
        this.points = points;
        this.isComplete = isComplete;
        this.furnitures = new ArrayList<>();
    }

    public int getId() { return id; }
    public int getProjectId() { return projectId; }
    public List<Point> getPoints() { return points; }
    public List<FurnitureOnPlan> getFurnitures() { return furnitures; }
    public boolean IsComplete() { return isComplete; }

    public String getPlanJson() {
        String json = new Gson().toJson(points);
        return json;
    }

    static public List<Point> getPointsFromJson(String json) {
        List<Point> points = new Gson().fromJson(json, new TypeToken<ArrayList<Point>>(){}.getType());

        return points;
    }


    public void SetId(int id) { this.id = id; }
    public void SetComplete(boolean value) {
        isComplete = value;
    }

    public void addFurniture(FurnitureOnPlan furniture) {
        furnitures.add(furniture);
    }
}
