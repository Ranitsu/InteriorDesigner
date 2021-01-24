package com.interiordesigner.Classes;

import android.content.res.Resources;

import com.interiordesigner.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private String description;
    private LocalDate createDate;
    private LocalDate editDate;
    private RoomPlan roomPlan;
    private int thumbnailId;

    public Project () { }

    public Project (String name) {
        this.name = name;
        this.thumbnailId = R.drawable.thumbnail;
        this.createDate = java.time.LocalDate.now();
    }

    public Project (String name, String description) {
        this.name = name;
        this.description = description;
        this.thumbnailId = R.drawable.thumbnail;
        this.createDate = java.time.LocalDate.now();
    }

    public Project (int id, String name, int thumbnailId) {
        this.id = id;
        this.name = name;
        if (thumbnailId == 0)
            this.thumbnailId = R.drawable.thumbnail;
        else {
            this.thumbnailId = thumbnailId;
        }
    }

    public Project(int id, String name, String description, String createDate, int thumbnailId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createDate = LocalDate.parse(createDate);
        if (thumbnailId == 0)
            this.thumbnailId = R.drawable.thumbnail;
        else {
            this.thumbnailId = thumbnailId;
        }
    }

    public Project(int id, String name, String description, String createDate, int thumbnailId, RoomPlan roomPlan) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createDate = LocalDate.parse(createDate);
        if (thumbnailId == 0)
            this.thumbnailId = R.drawable.thumbnail;
        else {
            this.thumbnailId = thumbnailId;
        }
        this.roomPlan = roomPlan;
    }

    public int GetId() { return this.id; }
    public String GetName() { return this.name; }
    public String GetDescription() { return this.description; }
    public int GetThumbnailId(){
        return this.thumbnailId;
    }
    public RoomPlan GetRoomPlan() { return this.roomPlan; }

    public LocalDate GetCreateDate() { return this.createDate; }
    public LocalDate GetEditDate() { return this.editDate; }



    public static List<Project> getProjects() {
        List<Project> projects = new ArrayList<Project>();
        projects.add(new Project("Test 1"));
        projects.add(new Project("Test 2"));
        projects.add(new Project("Test 3"));

        return projects;
    }
}
