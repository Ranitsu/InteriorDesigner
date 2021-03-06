package com.interiordesigner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.interiordesigner.Classes.Category;
import com.interiordesigner.Classes.Furniture;
import com.interiordesigner.Classes.FurnitureOnPlan;
import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.Project;
import com.interiordesigner.Classes.RoomPlan;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "InteriorDesigner";
    private static final int DB_VERSION = 1;

    private static final String PROJECT = "Project";
    private static final String ROOM_PLAN = "RoomPlan";

    private Context context;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CreateOrUpgradeDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CreateOrUpgradeDatabase(db, oldVersion, newVersion);
    }

    private void CreateOrUpgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            CreateProjectTable(db);
            for (Project project : Project.getProjects()) {
                AddProject(db, project);
            }
            CreateRoomPlanTable(db);
        }
    }

    // Project
    private void CreateProjectTable(SQLiteDatabase db) {
        String query = "CREATE TABLE Project (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT," +
                "Description TEXT," +
                "CreateDate TEXT," +
                "EditDate TEXT," +
                "ThumbnailId INTEGER" +
                ")";

        db.execSQL(query);
    }

    public void AddProject(SQLiteDatabase db, Project project) {
        ContentValues projectValues = new ContentValues();
        projectValues.put("Name", project.GetName());
        projectValues.put("Description", project.GetDescription());
        projectValues.put("CreateDate", project.GetCreateDate().toString());

        db.insert(PROJECT, null, projectValues);
    }

    public List<Project> GetProjects() {
        List<com.interiordesigner.Classes.Project> projects = new ArrayList<>();

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + PROJECT + " LEFT JOIN " + ROOM_PLAN + " ON " + PROJECT + "._id = " + ROOM_PLAN + ".ProjectId", new String[]{});

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Project project = ParseProject(cursor);
                    projects.add(project);

                    cursor.moveToNext();
                }
            }
        } catch (SQLiteException ex) {
            Toast toast = Toast.makeText(context, R.string.DB_notAvailable, Toast.LENGTH_SHORT);
            toast.show();
        }

        return projects;
    }

    public Project GetProject(int projectId) {
        Project project = new Project();

        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query("Project",
//            new String[]{"_id", "Name", "Description", "CreateDate", "ThumbnailId"},
//            "_id = ?",
//            new String[]{Integer.toString(projectId)},
//            null, null, null);

        Cursor cursor = db.rawQuery("SELECT * FROM " + PROJECT + " LEFT JOIN " + ROOM_PLAN + " ON " + PROJECT + "._id = " + ROOM_PLAN + ".ProjectId WHERE " + PROJECT + "._id = ? ", new String[]{Integer.toString(projectId)});

        if (cursor.moveToFirst()) {
            project = ParseProject(cursor);
        }

        cursor.close();
        return project;
    }

    private Project ParseProject(Cursor cursor) {
        Project project;
        RoomPlan roomPlan = null;

        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String description = cursor.getString(2);
        String createDate = cursor.getString(3);
        String editDate = cursor.getString(4);
        int thumbnailId = cursor.getInt(5);

        int roomPlanId = cursor.getInt(6);
        String roomPlanJson = cursor.getString(8);
        String roomFurnituresJson = cursor.getString(9);
        int roomPlanIsComplete = cursor.getInt(10);

        if (roomPlanId != 0) {
            List<Point> points = RoomPlan.getPointsFromJson(roomPlanJson);
            List<FurnitureOnPlan> furnitures = RoomPlan.getFurnituresFromJson(roomFurnituresJson);
            roomPlan = new RoomPlan(roomPlanId, id, points, furnitures, roomPlanIsComplete == 1);
        }

        project = new Project(id, name, description, createDate, thumbnailId, roomPlan);

        return project;
    }


    // RoomPlan
    private void CreateRoomPlanTable(SQLiteDatabase db) {
        String query = "CREATE TABLE RoomPlan (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ProjectId INTEGER," +
                "PlanJson TEXT," +
                "FurnituresJson TEXT," +
                "IsComplete INTEGER" +
                ")";

        db.execSQL(query);
    }

    public void AddRoomPlan(SQLiteDatabase db, RoomPlan roomPlan) {
        ContentValues roomPlanValues = new ContentValues();
        roomPlanValues.put("ProjectId", roomPlan.getProjectId());
        roomPlanValues.put("PlanJson", roomPlan.getPlanJson());
        roomPlanValues.put("FurnituresJson", roomPlan.getFurnituresJson());
        roomPlanValues.put("IsComplete", roomPlan.IsComplete());

        db.insert(ROOM_PLAN, null, roomPlanValues);
    }

    public void UpdateRoomPlan(SQLiteDatabase db, RoomPlan roomPlan) {
        ContentValues roomPlanValues = new ContentValues();
        roomPlanValues.put("ProjectId", roomPlan.getId());
        roomPlanValues.put("PlanJson", roomPlan.getPlanJson());
        roomPlanValues.put("FurnituresJson", roomPlan.getFurnituresJson());
        roomPlanValues.put("IsComplete", roomPlan.IsComplete());

        db.update(ROOM_PLAN, roomPlanValues, "_id = ?", new String[] { String.valueOf(roomPlan.getId()) } );
    }

    public RoomPlan GetRoomPlanByProjectId(int projectId) {
        RoomPlan roomPlan;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(ROOM_PLAN,
                new String[]{"_id", "ProjectId", "PlanJson", "FurnituresJson", "IsComplete"},
                "ProjectId = ?",
                new String[] {Integer.toString(projectId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int projId = cursor.getInt(1);
            String planJson = cursor.getString(2);
            String furnituresJson = cursor.getString(3);
            int complete = cursor.getInt(4);

            List<Point> points = RoomPlan.getPointsFromJson(planJson);
            List<FurnitureOnPlan> furnitures = RoomPlan.getFurnituresFromJson(furnituresJson);
            boolean isComplete = (complete == 1);

            roomPlan = new RoomPlan(id, projId, points, furnitures, isComplete);
        } else {
            roomPlan = null;
        }

        cursor.close();

        return roomPlan;
    }

}
