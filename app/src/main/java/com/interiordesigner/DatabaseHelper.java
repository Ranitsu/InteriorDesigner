package com.interiordesigner;

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

import com.interiordesigner.Classes.Category;
import com.interiordesigner.Classes.Furniture;
import com.interiordesigner.Classes.Project;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "InteriorDesigner";
    private static final int DB_VERSION = 1;

    private static final String PROJECT = "Project";
    private static final String ROOM_PLAN = "RoomPlan";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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

    public Project GetProject(int projectId) {
        Project project = new Project();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Project",
            new String[]{"_id", "Name", "Description", "CreateDate", "ThumbnailId"},
            "_id = ?",
            new String[]{Integer.toString(projectId)},
            null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String createDate = cursor.getString(3);
            int thumbnailId = cursor.getInt(4);

            project = new Project(id, name, description, createDate, thumbnailId);
        }

        cursor.close();

        return project;
    }


    // RoomPlan
    private void CreateRoomPlanTable(SQLiteDatabase db) {
        String query = "CREATE TABLE RoomPlan (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ProjectId INTEGER," +
                "PlanJson TEXT" +
                ")";

        db.execSQL(query);
    }

    public void AddRoomPlan(SQLiteDatabase db, int projectId, String json) {
        ContentValues roomPlanValues = new ContentValues();
        roomPlanValues.put("ProjectId", projectId);
        roomPlanValues.put("PlanJson", json);

        db.insert(ROOM_PLAN, null, roomPlanValues);
    }

    public String GetPlanJson(int projectId) {
        String planJson = "";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("RoomPlan",
                new String[]{"PlanJson"},
                "ProjectId = ?",
                new String[] {Integer.toString(projectId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            planJson = cursor.getString(0);
        }

        cursor.close();

        return planJson;
    }
}
