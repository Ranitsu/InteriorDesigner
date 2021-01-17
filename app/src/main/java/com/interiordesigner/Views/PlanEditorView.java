package com.interiordesigner.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.RoomPlan;

import java.util.List;

public class PlanEditorView extends View {
    public static final int CIRCLE_RADIUS = 50;

    private int bitmapHeight = 1000;
    private int bitmapWidth = 1000;

    private GestureDetectorCompat detector;

    private Bitmap bitmap;
    private Canvas myCanvas;


    int scrollPosX = 0;
    int scrollPosY = 0;

    float touchX, touchY;
    boolean movePoint;

    public RoomPlan roomPlan;

    public boolean planIsComplete;
    public List<Point> points;
    public Point selectedPoint;

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

        //selectedPoint = points.get(1);

        movePoint = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_UP) {
            touchX = scrollPosX;
            touchY = scrollPosY;
            movePoint = false;
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

        for (int i = 0; i < roomPlan.getPoints().size(); i++) {
            if (i == 0)
                paint.setColor(Color.GREEN);
            else if (roomPlan.getPoints().get(i) == selectedPoint)
                paint.setColor(Color.RED);
            else
                paint.setColor(Color.BLACK);

            canvas.drawCircle(roomPlan.getPoints().get(i).getX(),
                    roomPlan.getPoints().get(i).getY(),
                    CIRCLE_RADIUS,
                    paint);

            if (i != 0) {
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(10);
                canvas.drawLine(roomPlan.getPoints().get(i-1).getX(),
                                roomPlan.getPoints().get(i-1).getY(),
                                roomPlan.getPoints().get(i).getX(),
                                roomPlan.getPoints().get(i).getY(),
                                paint);
            }
        }

        if (roomPlan.IsComplete()) {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(10);
            canvas.drawLine(roomPlan.getPoints().get(0).getX(),
                            roomPlan.getPoints().get(0).getY(),
                            roomPlan.getPoints().get(roomPlan.getPoints().size()-1).getX(),
                            roomPlan.getPoints().get(roomPlan.getPoints().size()-1).getY(),
                            paint);
        }

    }


    class PlanGestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener {
        private static final String DEBUG_TAG = "Gesture";

        @Override
        public boolean onDown(MotionEvent event) {
            touchX = event.getX();// - scrollPosX;
            touchY = event.getY();// - scrollPosY;

            if (IsPointTapped(event))
            {
                movePoint = true;
            }

            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            int x = (int) event.getX() + scrollPosX;
            int y = (int) event.getY() + scrollPosY;

            int selectedPointIndex = SelectPoint(event);

            if (selectedPointIndex <= 0 && selectedPoint == roomPlan.getPoints().get(roomPlan.getPoints().size()-1) ) {
                if (selectedPointIndex == 0 && roomPlan.getPoints().size() >= 3) {
                    roomPlan.SetComplete(true);
                } else if (!roomPlan.IsComplete()) {
                    Point point = new Point(x, y, CIRCLE_RADIUS);
                    roomPlan.getPoints().add(point);
                    selectedPoint = point;
                }
            }

            if (selectedPointIndex > -1)
                selectedPoint = roomPlan.getPoints().get(selectedPointIndex);

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


            if (movePoint)
            {
                int x = (int) event2.getX() + scrollPosX;;
                int y = (int) event2.getY() + scrollPosY;;
                selectedPoint.SetXY(x, y);
            }
            else
            {
                float newTouchX = event2.getX();
                float newTouchY = event2.getY();

                scrollPosX += (int) (touchX - newTouchX);
                scrollPosY += (int) (touchY - newTouchY);

                touchX = newTouchX;
                touchY = newTouchY;
            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocitX, float velocityY) {
           // Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            return true;
        }

        private int SelectPoint(MotionEvent event) {
            int result = -1;
            int x = (int) event.getX() + scrollPosX;
            int y = (int) event.getY() + scrollPosY;

            for (int i = 0; i < roomPlan.getPoints().size(); i++) {
                Point point = roomPlan.getPoints().get(i);
                double dx = Math.pow(x - point.getX(), 2);
                double dy = Math.pow(y - point.getY(), 2);

                if (dx + dy < Math.pow(CIRCLE_RADIUS, 2)) {
                    result = i;
                    break;
                }
            }

            return result;
        }

        private boolean IsPointTapped(MotionEvent event) {
            int selectedPointIndex = SelectPoint(event);

            if (selectedPointIndex >= 0)
                return true;
            else
                return false;
        }

    }
}
