package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.interiordesigner.Classes.Project;
import com.interiordesigner.Classes.RoomPlan;
import com.interiordesigner.Views.PreviewRoomPlanView;

public class Project3DPreview extends AppCompatActivity {
    int projectId;
    SceneView sceneView;
    DatabaseHelper databaseHelper;
    Project project;
    RoomPlan roomPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectId = (Integer) getIntent().getExtras().get(MainActivity.EXTRA_PROJECT_ID);
        databaseHelper = new DatabaseHelper(this);

        project = databaseHelper.GetProject(projectId);

        setContentView(R.layout.activity_project_3d_preview);
        sceneView = findViewById(R.id.project3DPreview);

        Camera camera = sceneView.getScene().getCamera();
        camera.setLocalPosition(new Vector3(0f, 3f, 3f));
        camera.setLocalRotation(Quaternion.axisAngle(Vector3.right(), -30.0f));

        CreateLine();
    }

    void CreateLine() {
        Color colorOrange = new Color(android.graphics.Color.parseColor("#ffa71c"));

        MaterialFactory.makeOpaqueWithColor(this, colorOrange)
                .thenAccept(material -> {
                    ModelRenderable model = ShapeFactory.makeCylinder(0.25f, 3f, new Vector3(0,0,0), material);
                    model.setShadowReceiver(false);
                    model.setShadowCaster(false);

                    Node node = new Node();
                    node.setRenderable(model);
                    node.setLocalPosition(new Vector3(0,0,0));

                    sceneView.getScene().addChild(node);
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        sceneView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            sceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            sceneView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}