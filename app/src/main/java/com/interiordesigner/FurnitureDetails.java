package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Renderer;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.interiordesigner.Classes.Color;
import com.interiordesigner.Classes.Furniture;
import com.interiordesigner.Nodes.MyTransformableNode;

import java.util.concurrent.CompletableFuture;

public class FurnitureDetails extends AppCompatActivity {
    public static final String EXTRA_FURNITURE_ID = "furniture_id";

    private Furniture furniture;
    private SceneView sceneView;
    private String modelId = "ModelUID";
    private ModelRenderable modelRenderable;
    private MyTransformableNode node;
    private Color[] colors;

    private String modelPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colors = Color.Colors;

        int furnitureId = (Integer) getIntent().getExtras().get(EXTRA_FURNITURE_ID);
        furniture = Furniture.GetFurniture(furnitureId);

        setContentView(R.layout.activity_furniture_details);
        sceneView = (SceneView) findViewById(R.id.objectPreview);

        TextView titleText = findViewById(R.id.textTitle);
        titleText.setText(furniture.GetName());

        Button btnReset = findViewById(R.id.btnReset);
        Button btnAR = findViewById(R.id.btnAR);


        int[] furnitureColors = furniture.GetColorsIds();

        RadioGroup radioGroup = findViewById(R.id.colorsRadiosGroup);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        RadioButton[] colorsRadios = new RadioButton[furnitureColors.length];
        for (int i = 0; i < furnitureColors.length; i++) {
            colorsRadios[i] = new RadioButton(this);
            colorsRadios[i].setText(Color.GetById(furnitureColors[i]).GetName());
            colorsRadios[i].setId(i);
            radioGroup.addView(colorsRadios[i]);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                modelPath = furniture.GetModelsPaths()[checkedRadioButtonId];
                createModel(modelPath);
            }
        });

        colorsRadios[0].toggle();

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
        //createModel(modelPath);
    }

    private void createModel(String pathToModel) {
        ModelRenderable.builder()
                .setSource(this, Uri.parse(pathToModel))
                .setIsFilamentGltf(true)
                .setRegistryId(pathToModel)
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

    private void addNodeToScene(ModelRenderable model) {
        if (sceneView != null) {
            TransformationSystem ts = makeTransformationSystem();
            if (node != null)
                sceneView.getScene().removeChild(node);
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

    private TransformationSystem makeTransformationSystem() {
        FootprintSelectionVisualizer footpringSelectionVisualizer = new FootprintSelectionVisualizer();
        return new TransformationSystem(this.getResources().getDisplayMetrics(), footpringSelectionVisualizer);
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



