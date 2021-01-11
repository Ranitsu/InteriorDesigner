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

public class PlanEditorView extends View {
    private static final int SQUARE_SIZE = 100;

    private int bitmapHeight = 1000;
    private int bitmapWidth = 1000;

    private GestureDetectorCompat detector;

    private Bitmap bitmap;
    private Canvas myCanvas;


    int scrollPosX = 0;
    int scrollPosY = 0;

    float touchX, touchY;

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
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas(bitmap);

        PlanGestureListener gestureListener = new PlanGestureListener();
        detector = new GestureDetectorCompat( getContext(), gestureListener);
        detector.setOnDoubleTapListener(gestureListener);
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

//                float newTouchX = event.getX();
//                float newTouchY = event.getY();
//
//                scrollPosX += (int) (touchX - newTouchX);
//                scrollPosY += (int) (touchY - newTouchY);
//
//                touchX = newTouchX;
//                touchY = newTouchY;
//                postInvalidate();
//                return true;
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        Rect rect = new Rect();
        rect.left = 10;
        rect.top = 10;
        rect.right = rect.left + SQUARE_SIZE;
        rect.bottom = rect.top + SQUARE_SIZE;

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        //canvas = new Canvas(bitmap);
        myCanvas.drawRect(rect, paint);

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
            DrawCircle(myCanvas, event.getX(), event.getY());

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
