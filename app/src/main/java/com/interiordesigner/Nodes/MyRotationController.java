package com.interiordesigner.Nodes;

import android.os.Handler;

import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.BaseTransformableNode;
import com.google.ar.sceneform.ux.BaseTransformationController;
import com.google.ar.sceneform.ux.DragGesture;
import com.google.ar.sceneform.ux.DragGestureRecognizer;
import com.google.ar.sceneform.ux.TransformationSystem;

public class MyRotationController extends BaseTransformationController<DragGesture> {
    private MyTransformableNode node;
    DragGestureRecognizer gestureRecognizer;

    private float rotationRateDegrees = 0.5f;

    private double initVerticalAngle = 0d;
    private double initHorizontalAngle = 30.0d;

    double verticalAngle = initVerticalAngle;
    double horizontalAngle = initHorizontalAngle;

    public MyRotationController(MyTransformableNode node, DragGestureRecognizer gestureRecognizer) {
        super(node, gestureRecognizer);
        this.node = (MyTransformableNode) super.getTransformableNode();
    }

    @Override
    public void onActivated(Node node) {
        super.onActivated(node);
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        //transformCamera(verticalAngle, horizontalAngle);
                        transformNode(verticalAngle, horizontalAngle);
                    }
                }, 0
        );
    }

    @Override
    protected boolean canStartTransformation(DragGesture gesture) {
        return node.isSelected();
    }

    @Override
    protected void onContinueTransformation(DragGesture gesture) {
        float rotationAmountY = gesture.getDelta().y * rotationRateDegrees;
        float rotationAmountX = gesture.getDelta().x * rotationRateDegrees;
        double deltaAngleY = rotationAmountY;
        double deltaAngleX = rotationAmountX;

        horizontalAngle -= deltaAngleX;
        verticalAngle += deltaAngleY;

        verticalAngle = Math.max(Math.min(verticalAngle, 90.0), 0.0);

        //transformCamera(horizontalAngle, verticalAngle);
        transformNode(horizontalAngle, verticalAngle);
    }

    private void transformCamera(double hAngle, double vAngle) {
        Camera camera = node.getScene().getCamera();

        Quaternion rot = Quaternion.eulerAngles(new Vector3(0F, 0F, 0F));
        Vector3 pos = new Vector3(getX(hAngle, vAngle), getY(vAngle), getZ(hAngle, vAngle));
        rot = Quaternion.multiply(rot, new Quaternion(Vector3.up(), (float) hAngle));
        rot = Quaternion.multiply(rot, new Quaternion(Vector3.right(), (float) -vAngle));
        camera.setLocalRotation(rot);
        camera.setLocalPosition(pos);
    }

    private void transformNode(double hAngle, double vAngle) {
        Quaternion rot = Quaternion.eulerAngles(new Vector3(0F, 0F, 0F));
        rot = Quaternion.multiply(rot, new Quaternion(Vector3.up(), (float) hAngle));
        node.setLocalRotation(rot);
    }

    private float getX(double hAngle, double vAngle) {
        return (float) (node.radius * Math.cos(Math.toRadians(vAngle)) * Math.sin(Math.toRadians(hAngle)));
    }

    private float getY(double vAngle) {
        return (float) ((node.radius) * Math.sin(Math.toRadians(vAngle)));
    }

    private float getZ(double hAngle, double vAngle) {
        return (float) (node.radius * Math.cos(Math.toRadians(vAngle)) * Math.cos(Math.toRadians(hAngle)));
    }

    public void resetInitialState() {
        //transformCamera(initVerticalAngle, initHorizontalAngle);
        transformNode(initVerticalAngle, initHorizontalAngle);
    }

    @Override
    protected void onEndTransformation(DragGesture gesture) {}
}
