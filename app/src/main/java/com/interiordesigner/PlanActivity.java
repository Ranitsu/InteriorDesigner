package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.RoomPlan;
import com.interiordesigner.Views.PlanEditorView;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity {
    private PlanEditorView planEditor;

    int projectId;
    RoomPlan roomPlan;
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

        roomPlan = databaseHelper.GetRoomPlanByProjectId(projectId);

        if (roomPlan != null) {
            planEditor.roomPlan = roomPlan;
        } else {
            List<Point> points = new ArrayList<Point>();
            points.add(new Point(150, 150, PlanEditorView.CIRCLE_RADIUS));
            points.add(new Point(500, 150, PlanEditorView.CIRCLE_RADIUS));

            roomPlan = new RoomPlan(projectId, points, false);
            planEditor.roomPlan = roomPlan;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDelegate().onStart();
    }

    public void onClickDelete(View view) {
        Point point = planEditor.selectedPoint;
        List<Point> points = roomPlan.getPoints();

        points.remove(point);
        planEditor.selectedPoint = null;
        planEditor.postInvalidate();
    }

    public void onClickSave(View view) {
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

        String toastText;
        try {
            if (roomPlan.getId() > 0) {
                databaseHelper.UpdateRoomPlan(db, roomPlan);
                toastText = "Room plan updated";

            } else {
                databaseHelper.AddRoomPlan(db, roomPlan);
                RoomPlan savedRoomPlan = databaseHelper.GetRoomPlanByProjectId(projectId);
                roomPlan.SetId(savedRoomPlan.getId());
                toastText = "Room plan saved";
            }
        } catch(Exception exception) {
            toastText = "Error";
        }

        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_LONG);
        toast.show();
    }


}