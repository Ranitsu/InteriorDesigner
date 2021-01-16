package com.interiordesigner.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.interiordesigner.Classes.Point;

import java.util.List;

public class PreviewRoomPlanView extends View {
    public List<Point> points;


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

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);

        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(bitmap);

        canvas.drawLine(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(), points.get(1).getY(), paint);
        canvas.drawLine(points.get(1).getX(), points.get(1).getY(), points.get(2).getX(), points.get(2).getY(), paint);
    }
}
