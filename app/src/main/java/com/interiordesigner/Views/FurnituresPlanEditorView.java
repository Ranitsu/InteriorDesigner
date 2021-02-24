package com.interiordesigner.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.interiordesigner.Classes.Furniture;
import com.interiordesigner.Classes.FurnitureOnPlan;
import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.RoomPlan;

import java.io.IOException;
import java.util.List;

public class FurnituresPlanEditorView extends PreviewRoomPlanView {

    public FurnituresPlanEditorView(Context context) {
        super(context);
    }
    public FurnituresPlanEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public FurnituresPlanEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public FurnituresPlanEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (roomPlan != null) {
            DrawPoints(canvas);

            if (roomPlan.getFurnitures().size() > 0) {
                DrawFurnitures(canvas);
            }
        }
    }

    private void DrawFurnitures(Canvas canvas) {
        List<FurnitureOnPlan> furnitures = roomPlan.getFurnitures();

        for (int i = 0; i < furnitures.size(); i++) {
            FurnitureOnPlan furniture = furnitures.get(i);
            String path = furniture.getPhotoPath();
            Drawable drawable = null;
            try {
                drawable = Drawable.createFromStream(getContext().getAssets().open(path), null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            drawable.setBounds(furniture.getPosition().getX(), furniture.getPosition().getY(), furniture.getPosition().getX() + 200 ,furniture.getPosition().getY() + 200 );
            drawable.draw(canvas);
        }
    }
}
