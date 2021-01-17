package com.interiordesigner.Classes;

import com.google.gson.Gson;

import java.util.List;

public class RoomPlan {
    int id;
    int projectId;
    List<Point> points;
    boolean isComplete;

    public RoomPlan(int projId, List<Point> points, boolean isComplete) {
        this.projectId = projId;
        this.points = points;
        this.isComplete = isComplete;
    }

    public RoomPlan(int id, int projId, List<Point> points, boolean isComplete) {
        this.id = id;
        this.projectId = projId;
        this.points = points;
        this.isComplete = isComplete;
    }

    public int getId() { return id; }
    public int getProjectId() { return projectId; }
    public List<Point> getPoints() { return points; }
    public boolean IsComplete() { return isComplete; }

    public String getPlanJson() {
        String json = new Gson().toJson(points);
        return json;
    }

    public void SetId(int id) { this.id = id; }
    public void SetComplete(boolean value) {
        isComplete = value;
    }
}
