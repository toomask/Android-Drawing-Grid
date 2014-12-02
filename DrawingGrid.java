package com.x_mega.drawablegridexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by toomas on 2.12.2014.
 */
public class DrawingGrid extends View {

    public DrawingGrid(Context context) {
        super(context);
        init();
    }

    public DrawingGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    int gridWidth = 10;
    int gridHeight = 10;

    int gridLineWidth = 5;

    int backgroundColor = Color.parseColor("#ff0000");
    int gridLineColor = Color.parseColor("#00ff00");
    int drawingColor = Color.parseColor("#0000ff");

    Bitmap bitmap;


    private void init() {
        resetBitmap();
    }

    private void resetBitmap() {
        bitmap = Bitmap.createBitmap(gridWidth, gridHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(backgroundColor);
    }

    public void setGridLineWidth(int gridLineWidth) {
        this.gridLineWidth = gridLineWidth;
        invalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        resetBitmap();
        invalidate();
    }

    public void setGridLineColor(int gridLineColor) {
        this.gridLineColor = gridLineColor;
        invalidate();
    }

    public void setDrawingColor(int drawingColor) {
        this.drawingColor = drawingColor;
    }

    public void setGridSize(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        resetBitmap();
        invalidate();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private Rect calculateElementBounds(int x, int y) {
        int elementWidth = calculateElementWidth();
        int elementHeight = calculateElementHeigth();

        int left = x * ( gridLineWidth + elementWidth );
        int top = y * ( gridLineWidth + elementHeight );

        return new Rect(
                left, top, left + elementWidth, top + elementWidth );
    }

    private int calculateElementHeigth() {
        return ( getHeight() - ( gridLineWidth * ( gridHeight - 1 ) ) ) / gridHeight;
    }

    private int calculateElementWidth() {
        return ( getWidth() - ( gridLineWidth * ( gridWidth - 1 ) ) ) / gridWidth;
    }

    private Point findElementPosition(int viewX, int viewY) {
        for (int x = 0; x < gridWidth; x++ ) {
            for (int y = 0; y < gridHeight; y++) {
                if ( calculateElementBounds(x, y).contains(viewX, viewY) ) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point elementPosition = findElementPosition(Math.round(event.getX()), Math.round(event.getY()));
        if (elementPosition != null) {
            bitmap.setPixel(elementPosition.x, elementPosition.y, drawingColor);
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int x = 0; x < gridWidth; x++ ) {
            for (int y = 0; y < gridHeight; y++) {
                Rect elementBounds = calculateElementBounds(x, y);
                Paint paint = new Paint();
                paint.setColor(bitmap.getPixel(x, y));
                canvas.drawRect(elementBounds, paint);
            }
        }

        for (int x = 0; x < gridWidth - 1; x++) {
            int left = (1 + x) * calculateElementWidth() + x * gridLineWidth;
            Paint paint = new Paint();
            paint.setColor(gridLineColor);
            canvas.drawRect(
                    left, 0, left + gridLineWidth, getHeight(), paint);
        }

        for (int y = 0; y < gridHeight - 1; y++) {
            int top = (1 + y) * calculateElementHeigth() + y * gridLineWidth;
            Paint paint = new Paint();
            paint.setColor(gridLineColor);
            canvas.drawRect(
                    0, top, getWidth(), top + gridLineWidth, paint );
        }
    }
}
