package com.interiordesigner.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.EdgeEffectCompat;

import com.interiordesigner.Classes.PlanPoint;

import java.util.ArrayList;
import java.util.List;

public class PlanEditorView extends View {
    private static final int SQUARE_SIZE = 100;
    private static final int CIRCLE_RADIUS = 50;

    private int bitmapHeight = 1000;
    private int bitmapWidth = 1000;

    private GestureDetectorCompat detector;

    private Bitmap bitmap;
    private Canvas myCanvas;


    int scrollPosX = 0;
    int scrollPosY = 0;

    float touchX, touchY;
    boolean planIsComplite;

    public List<PlanPoint> points;

    public PlanEditorView(Context context) {
        super(context);

        init(null);
    }
    public PlanEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }
    public PlanEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }
    public PlanEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        points = new ArrayList<PlanPoint>();
        planIsComplite = false;

        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas(bitmap);

        PlanGestureListener gestureListener = new PlanGestureListener();
        detector = new GestureDetectorCompat( getContext(), gestureListener);
        detector.setOnDoubleTapListener(gestureListener);

        points.add(new PlanPoint(150, 150, CIRCLE_RADIUS));
        points.add(new PlanPoint(500, 150, CIRCLE_RADIUS));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_UP) {
            touchX = scrollPosX;
            touchY = scrollPosY;
        }

        boolean result = this.detector.onTouchEvent(event);
        postInvalidate();
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas(bitmap);
        DrawPoints(myCanvas);

        canvas.drawBitmap(
                bitmap,
                new Rect(scrollPosX,
                        scrollPosY,
                        scrollPosX + width,
                        scrollPosY + height),
                new Rect(0, 0, width, height),
                null
        );
    }

    private void DrawPoints(Canvas canvas)
    {
        Paint paint = new Paint();

        for (int i = 0; i < points.size(); i++) {
            if (i == 0)
                paint.setColor(Color.GREEN);
            else if (i == points.size() -1 && !planIsComplite)
                paint.setColor(Color.RED);
            else
                paint.setColor(Color.BLACK);

            canvas.drawCircle(points.get(i).getX(),
                    points.get(i).getY(),
                    points.get(i).getRadius(),
                    paint);

            if (i != 0) {
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(10);
                canvas.drawLine(points.get(i-1).getX(),
                                points.get(i-1).getY(),
                                points.get(i).getX(),
                                points.get(i).getY(),
                                paint);
            }
        }

        if (planIsComplite) {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(10);
            canvas.drawLine(points.get(0).getX(),
                            points.get(0).getY(),
                            points.get(points.size()-1).getX(),
                            points.get(points.size()-1).getY(),
                            paint);
        }

    }


    void DrawCircle (Canvas canvas, float cx, float cy) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(cx + scrollPosX, cy + scrollPosY, 40, paint);

    }

    class PlanGestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener {
        private static final String DEBUG_TAG = "Gesture";

        @Override
        public boolean onDown(MotionEvent event) {
            touchX = event.getX();// - scrollPosX;
            touchY = event.getY();// - scrollPosY;

            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            //DrawCircle(myCanvas, event.getX(), event.getY());
            int x = (int) event.getX() + scrollPosX;
            int y = (int) event.getY() + scrollPosY;

            double dx = Math.pow(x - points.get(0).getX(), 2);
            double dy = Math.pow(y - points.get(0).getY(), 2);

            if (dx + dy < Math.pow(CIRCLE_RADIUS, 2) && points.size() >= 3) {
                planIsComplite = true;
            }
            else if (!planIsComplite) {
                points.add(new PlanPoint( x, y, CIRCLE_RADIUS));
            }

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
           // Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
           // Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2,
                             float distanceX, float distanceY) {
            Log.d("SCROLL", "X: " + scrollPosX + ", Y: " + scrollPosY);

            float newTouchX = event2.getX();
            float newTouchY = event2.getY();

            scrollPosX += (int) (touchX - newTouchX);
            scrollPosY += (int) (touchY - newTouchY);

            touchX = newTouchX;
            touchY = newTouchY;
            Log.d("TOUCH", "X: " + newTouchX + ", Y: " + newTouchY);
            Log.d("SCROLL", "X: " + scrollPosX + ", Y: " + scrollPosY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocitX, float velocityY) {
           // Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            return true;
        }

    }
}
