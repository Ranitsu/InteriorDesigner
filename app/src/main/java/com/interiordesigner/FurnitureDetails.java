package com.interiordesigner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.NodeParent;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.interiordesigner.Classes.Furniture;
import com.interiordesigner.Nodes.MyTransformableNode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.CompletableFuture;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FurnitureDetails extends AppCompatActivity {
    public static final String EXTRA_FURNITURE_ID = "furniture_id";

    private Furniture furniture;
    private SceneView sceneView;
    private ModelRenderable modelRenderable;
    private MyTransformableNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int furnitureId = (Integer) getIntent().getExtras().get(EXTRA_FURNITURE_ID);
        furniture = Furniture.GetFurniture(furnitureId);

        setContentView(R.layout.activity_furniture_details);
        sceneView = (SceneView) findViewById(R.id.objectPreview);

        TextView titleText = findViewById(R.id.textTitle);
        titleText.setText(furniture.GetName());

        Button btnReset = findViewById(R.id.btnReset);
        Button btnAR = findViewById(R.id.btnAR);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                node.rotationController.resetInitialState();
            }
        });
        btnAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FurnitureDetails.this, FurnitureARPreview.class);
                intent.putExtra(FurnitureARPreview.EXTRA_FURNITURE_ID, furniture.GetId());
                startActivity(intent);
            }
        });

        Camera camera = sceneView.getScene().getCamera();
        camera.setLocalPosition(new Vector3(0f, furniture.GetModelRadius(), furniture.GetModelRadius()));
        camera.setLocalRotation(Quaternion.axisAngle(Vector3.right(), -30.0f));
        ModelRenderable.builder()
                .setSource(this, Uri.parse(furniture.GetModelName()))
                .setRegistryId(modelRenderable)
                .build()
                .thenAccept(renderable -> addNodeToScene(renderable))
                .exceptionally(
                throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load furniture model", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
    }

    private TransformationSystem makeTransformationSystem() {
        FootprintSelectionVisualizer footpringSelectionVisualizer = new FootprintSelectionVisualizer();
        return new TransformationSystem(this.getResources().getDisplayMetrics(), footpringSelectionVisualizer);
    }

    private void addNodeToScene(ModelRenderable model) {
        if (sceneView != null) {
            TransformationSystem ts = makeTransformationSystem();
            node = new MyTransformableNode(furniture.GetModelRadius(), ts);
            node.setRenderable(model);
            node.setLocalPosition(new Vector3(0F, 0F, 0F));
            sceneView.getScene().addChild(node);
            node.select();
            sceneView.getScene().addOnPeekTouchListener(
                    (HitTestResult hitTestResult, MotionEvent motionEvent) -> {
                        ts.onTouch(hitTestResult, motionEvent);
                    });

        }
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



