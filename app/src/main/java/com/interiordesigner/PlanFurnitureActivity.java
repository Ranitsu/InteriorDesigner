package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.RoomPlan;
import com.interiordesigner.Views.FurnituresPlanEditorView;
import com.interiordesigner.Views.PlanEditorView;

import java.util.ArrayList;
import java.util.List;

public class PlanFurnitureActivity extends AppCompatActivity {
    int projectId;
    RoomPlan roomPlan;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    FurnituresPlanEditorView editorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_furniture);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        projectId = (Integer) getIntent().getExtras().get(MainActivity.EXTRA_PROJECT_ID);
        roomPlan = databaseHelper.GetRoomPlanByProjectId(projectId);

        editorView = findViewById(R.id.viewFurniturePlanEditor);

        editorView.roomPlan = roomPlan;
    }
}