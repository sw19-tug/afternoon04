package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

public class ShapeTool extends PaintingTool {

    private PointF first;
    private PointF second;
    private String shape_type;

    public ShapeTool(int color, String shape_type) {
        Paint realColor = new Paint();
        realColor.setColor(color);
        realColor.setStyle(Paint.Style.FILL);
        this.color = realColor;
        this.first = null;
        this.second = null;
        this.shape_type = shape_type;

    }

    @Override
    public void drawTool(Canvas canvas) {
        float left;
        float right;
        float top;
        float bottom;

        if (first != null && second != null) {

            bottom = first.y;
            top = second.y;
            if (bottom < top) {
                top = first.y;
                bottom = second.y;
            }

            left = first.x;
            right = second.x;

            if (left < right) {
                left = second.x;
                right = first.x;
            }

            if (shape_type == "rect")
                canvas.drawRect(left, top, right, bottom, color);
            if (shape_type == "oval")
                canvas.drawOval(left, top, right, bottom, color);

        }


    }

    @Override
    public void handleEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            this.first = new PointF(x, y);
            this.second = new PointF(x, y);


        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            this.second = new PointF(x, y);

        }

    }

    @Override
    public void cleanUp()
    {


    }
}
