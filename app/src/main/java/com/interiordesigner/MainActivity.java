package com.interiordesigner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.Project;
import com.interiordesigner.Classes.RoomPlan;
import com.interiordesigner.Views.PreviewRoomPlanView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_PROJECT_ID = "projectId";

    int projectId;

    DatabaseHelper databaseHelper;

    Project project;
    RoomPlan roomPlan;

    Button btnRoomPlan;
    PreviewRoomPlanView imgRoomPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectId = (Integer) getIntent().getExtras().get(EXTRA_PROJECT_ID);
        databaseHelper = new DatabaseHelper(this);

        project = databaseHelper.GetProject(projectId);

        TextView projectName = findViewById(R.id.projectNameTxt);
        TextView projectDescription = findViewById(R.id.descriptionTxt);

        projectName.setText(project.GetName());
        projectDescription.setText(project.GetDescription());

    }

    @Override
    protected void onResume() {
        super.onResume();

        roomPlan = databaseHelper.GetRoomPlanByProjectId(projectId);
        btnRoomPlan = findViewById(R.id.btnRoomPlan);
        imgRoomPlan = findViewById(R.id.imgRoomPlan);

        if (roomPlan == null) {
            btnRoomPlan.setText(getResources().getString(R.string.btnNewRoomPlan));
        } else {
            btnRoomPlan.setText(getResources().getString(R.string.btnEditRoomPlan));
            imgRoomPlan.roomPlan = roomPlan;
            imgRoomPlan.postInvalidate();
        }
    }

    public void onClickBackToMenu(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void onClickCreateEditPlan(View view) {
        Intent intent = new Intent(this, PlanActivity.class);
        intent.putExtra(MainActivity.EXTRA_PROJECT_ID, projectId);
        startActivity(intent);
    }

}
