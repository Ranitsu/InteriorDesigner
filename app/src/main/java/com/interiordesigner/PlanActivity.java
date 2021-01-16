package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.interiordesigner.Classes.Point;
import com.interiordesigner.Views.PlanEditorView;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity {
    private PlanEditorView planEditor;

    int projectId;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        projectId = (Integer) getIntent().getExtras().get(MainActivity.EXTRA_PROJECT_ID);
        planEditor = findViewById(R.id.planEditorView);

        String json = databaseHelper.GetPlanJson(projectId);
        List<Point> result = new Gson().fromJson(json, new TypeToken<ArrayList<Point>>() {}.getType());

        if (result != null)
            planEditor.points = result;
        else {
            planEditor.points = new ArrayList<Point>();
            planEditor.points.add(new Point(150, 150, PlanEditorView.CIRCLE_RADIUS));
            planEditor.points.add(new Point(500, 150, PlanEditorView.CIRCLE_RADIUS));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDelegate().onStart();
    }


    public void onClickDelete(View view) {
        Point point = planEditor.selectedPoint;
        List<Point> points = planEditor.points;

        points.remove(point);
        planEditor.selectedPoint = points.get(points.size()-1);
        planEditor.postInvalidate();
    }

    public void onClickSave(View view) {
        boolean isComplite = planEditor.planIsComplete;
        List<Point> points = planEditor.points;

//        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("Alert message to be shown");
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();



        String plantAsString = points.toString();
        String json = new Gson().toJson(points);

        databaseHelper.AddRoomPlan(db, projectId, json);
    }


}