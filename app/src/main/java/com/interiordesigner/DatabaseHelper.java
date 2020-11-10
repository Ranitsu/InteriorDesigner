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
    private static final int DB_VERSION = 3;

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
        }
    }

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

    private void CreateCategoryTable(SQLiteDatabase db) {
        String query = "CREATE TABLE Category (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT," +
                "ParentId INTEGER" +
                ")";

        db.execSQL(query);
    }

    private void CreateFurnitureTable(SQLiteDatabase db) {
        String query = "CREATE TABLE Furniture (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT," +
                "CategoryId INTEGER," +
                "PhotoId INTEGER" +
                ")";

        db.execSQL(query);
    }

    public void AddProject(SQLiteDatabase db, Project project) {
        ContentValues projectValues = new ContentValues();
        projectValues.put("Name", project.GetName());
        projectValues.put("Description", project.GetDescription());
        projectValues.put("CreateDate", project.GetCreateDate().toString());

        db.insert("Project", null, projectValues);
    }

    public void AddCategory(SQLiteDatabase db, Category category) {
        ContentValues categoryValues = new ContentValues();
        categoryValues.put("Name", category.GetName());
        categoryValues.put("ParentId", category.GetParentId());

        db.insert("Category", null, categoryValues);
    }

    public void AddFurniture(SQLiteDatabase db, Furniture furniture) {
        ContentValues furnitureValues = new ContentValues();
        furnitureValues.put("Name", furniture.GetName());
        furnitureValues.put("PhotoId", furniture.GetPhotoId());
        furnitureValues.put("CategoryId", furniture.GetCategoryId());
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
        db.close();

        return project;
    }
}
