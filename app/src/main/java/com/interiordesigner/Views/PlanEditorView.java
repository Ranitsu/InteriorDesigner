package com.interiordesigner.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;

public class PlanEditorView extends View {
    private static final int SQUARE_SIZE = 100;
    private int bitmapHeight = 1000;
    private int bitmapWidth = 1000;

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

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d("DEBUG_TAG", "Action was DOWN");
                touchX = event.getX();
                touchY = event.getY();

                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d("DEBUG_TAG", "Action was MOVE");
                Log.d("DEBUG_TAG", event.toString());
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d("DEBUG_TAG", "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d("DEBUG_TAG", "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d("DEBUG_TAG", "Action was OUTSIDE");
                return true;
            default:
                return super.onTouchEvent(event);
        }
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

}
