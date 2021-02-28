package com.interiordesigner.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.interiordesigner.Classes.FurnitureOnPlan;
import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.RoomPlan;

import java.io.IOException;
import java.util.List;

public class PreviewRoomPlanView extends View {
    public RoomPlan roomPlan;

    public PreviewRoomPlanView(Context context) {
        super(context);
        init();
    }
    public PreviewRoomPlanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PreviewRoomPlanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public PreviewRoomPlanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {

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

    protected void DrawPoints(Canvas canvas)
    {
        List<Point> points = roomPlan.getPoints();
        int pointsSize = points.size();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        for (int i = 1; i < pointsSize; i++) {
            canvas.drawLine(points.get(i-1).getX(),
                            points.get(i-1).getY(),
                            points.get(i).getX(),
                            points.get(i).getY(),
                            paint);
        }

        if (roomPlan.IsComplete()) {
            canvas.drawLine(points.get(0).getX(),
                    points.get(0).getY(),
                    points.get(pointsSize-1).getX(),
                    points.get(pointsSize-1).getY(),
                    paint);
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
