package com.interiordesigner.Nodes;

import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

public class MyTransformableNode extends TransformableNode {
    float radius;
    public MyRotationController rotationController;

    public MyTransformableNode(float radius, TransformationSystem transformationSystem) {
        super(transformationSystem);
        this.radius = radius;
        this.rotationController = new MyRotationController(this, transformationSystem.getDragRecognizer());
    }


}
