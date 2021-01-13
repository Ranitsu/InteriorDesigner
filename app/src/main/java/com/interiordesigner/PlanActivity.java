package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.interiordesigner.Classes.PlanPoint;
import com.interiordesigner.Views.PlanEditorView;

import java.util.List;

public class PlanActivity extends AppCompatActivity {

    private PlanEditorView planEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        planEditor = findViewById(R.id.planEditorView);
    }

    public void onClickDelete(View view) {
        PlanPoint point = planEditor.selectedPoint;
        List<PlanPoint> points = planEditor.points;

        points.remove(point);
        planEditor.selectedPoint = points.get(points.size()-1);
        planEditor.postInvalidate();
    }

}