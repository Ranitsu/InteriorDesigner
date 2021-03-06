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
import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.Project;
import com.interiordesigner.Classes.RoomPlan;
import com.interiordesigner.Views.PreviewRoomPlanView;

import java.util.List;

public class Project3DPreview extends AppCompatActivity {
    int projectId;
    SceneView sceneView;
    DatabaseHelper databaseHelper;
    Project project;
    RoomPlan roomPlan;

    Color wallColor = new Color(android.graphics.Color.parseColor("#ffa71c"));
    Color cornerColor = new Color(android.graphics.Color.parseColor("#0000FF"));
    Color floorColor = new Color(android.graphics.Color.parseColor("#FFFFFF"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectId = (Integer) getIntent().getExtras().get(MainActivity.EXTRA_PROJECT_ID);
        databaseHelper = new DatabaseHelper(this);

        project = databaseHelper.GetProject(projectId);
        roomPlan = project.GetRoomPlan();

        setContentView(R.layout.activity_project_3d_preview);
        sceneView = findViewById(R.id.project3DPreview);

        double cameraHeight = CalculateCameraHeight();
        Quaternion cameraRotation1 = Quaternion.axisAngle(Vector3.up(), 45f);
        Quaternion cameraRotation2 = Quaternion.axisAngle(Vector3.right(), -60.0f);


        Camera camera = sceneView.getScene().getCamera();
        //camera.setLocalPosition(new Vector3(0f, 3f, 3f));
        camera.setVerticalFovDegrees(60);
        camera.setLocalPosition(new Vector3((float) 1000, 1200f, 0));
        camera.setLocalRotation(Quaternion.axisAngle(Vector3.right(), -45.0f));
        camera.setFarClipPlane(10000);
        camera.setLocalPosition(new Vector3(5f, 10, 30));
        camera.setLocalPosition(new Vector3(1100f, 1000, 1100));
        camera.setWorldRotation(Quaternion.multiply(cameraRotation1, cameraRotation2));
        //camera.setWorldRotation(Quaternion.axisAngle(Vector3.right(), -30.0f));

        CreateFloor();
        //CreateLine();
        CreateWalls();
    }

    private double CalculateCameraHeight() {
        List<Point> points = roomPlan.getPoints();
        Point p0 = points.get(0);
        Point p2 = points.get(2);

        double length = Math.sqrt((p2.getY() - p0.getY()) * (p2.getY() - p0.getY()) + (p2.getX() - p0.getX()) * (p2.getX() - p0.getX()));
        double height = length / Math.sqrt(3);

        return height;
    }

    private float GetDistanceBetweenVectors(Vector3 vec1, Vector3 vec2) {
        return (float) Math.sqrt(Math.pow(vec2.x - vec1.x ,2) + Math.pow(vec2.y - vec1.y ,2) + Math.pow(vec2.z - vec1.z ,2));
    }

    void CreateFloor() {
        MaterialFactory.makeOpaqueWithColor(this, floorColor)
                .thenAccept(material -> {
                    ModelRenderable model = ShapeFactory.makeCube(new Vector3(2000, 0f, 2000), new Vector3(0,0f,0) , material);
                    model.setShadowReceiver(false);
                    model.setShadowCaster(false);

                    Node node = new Node();
                    node.setRenderable(model);
                    node.setLocalPosition(new Vector3(1000,0,1000));

                    sceneView.getScene().addChild(node);
                });
    }

    void CreateWalls() {
        List<Point> points = roomPlan.getPoints();

        for (int i = 0; i < points.size() - 1; i++) {
            CreateWall(points.get(i), points.get(i+1));
            CreateCorner(points.get(i));
        }
        CreateWall(points.get(points.size()-1), points.get(0));
        CreateCorner(points.get(points.size()-1));
    }

    void CreateCorner(Point point) {
        Vector3 vector = new Vector3(point.getX(), 5, point.getY());

        MaterialFactory.makeOpaqueWithColor(this, cornerColor)
                .thenAccept(material -> {
                    ModelRenderable model = ShapeFactory.makeSphere(10f, new Vector3(0,0,0) , material);
                    model.setShadowReceiver(false);
                    model.setShadowCaster(false);

                    Node node = new Node();
                    node.setRenderable(model);
                    node.setLocalPosition(vector);

                    sceneView.getScene().addChild(node);
                });

    }

    void CreateWall(Point p1, Point p2) {
        Vector3 vector1 = new Vector3( p1.getX(), 0, p1.getY());
        Vector3 vector2 = new Vector3( p2.getX(), 0, p2.getY());
        Vector3 midpoint =  new Vector3((vector1.x + vector2.x)/2, 20, (vector1.z + vector2.z)/2);
        float wallLength = GetDistanceBetweenVectors(vector1, vector2);

        MaterialFactory.makeOpaqueWithColor(this, wallColor)
                .thenAccept(material -> {
                    ModelRenderable model = ShapeFactory.makeCylinder(10f, wallLength, new Vector3(0, wallLength / 2, 0), material);
                    model.setShadowReceiver(false);
                    model.setShadowCaster(false);

                    Node node = new Node();
                    node.setRenderable(model);
                    node.setLocalPosition(vector1);

                    Vector3 difference = Vector3.subtract(vector1, vector2);
                    Vector3 directionFromTopToBottom = difference.normalized();
                    Quaternion rotationFromAToB =
                            Quaternion.lookRotation(directionFromTopToBottom, Vector3.right());
                    node.setWorldRotation(Quaternion.multiply(rotationFromAToB,
                            Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90)));

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