package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;



public class ShapeTool extends PaintingTool {
    public static final String TYPE_RECT = "rect";
    public static final String TYPE_OVAL = "oval";
    private boolean in_use;



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

            if (second.y > first.y) {
                top = first.y;
                bottom = second.y;
            }
            else
            {
                bottom = first.y;
                top = second.y;
            }

            if (second.x > first.x) {
                right = second.x;
                left = first.x;
            }
            else
            {
                right = first.x;
                left = second.x;
            }

            if (shape_type.equals(TYPE_RECT))
                canvas.drawRect(left, top, right, bottom, color);
            if (shape_type.equals(TYPE_OVAL))
            {
                RectF rect = new RectF(left, top, right, bottom);
                canvas.drawOval(rect, color);
            }


        }


    }

    @Override
    public void handleEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            this.first = new PointF(x, y);
            this.in_use = true;

        }

        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            this.second =  new PointF(x, y);
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            this.second = new PointF(x, y);
            this.in_use = false;

        }

    }

    @Override
    public void cleanUp()
    {
        if(!in_use)
        {
            this.second = null;
        }

    }
}
