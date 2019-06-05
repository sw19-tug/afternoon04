package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class PathTool extends PaintingTool {
    private Path path;
    private float oldX;
    private float oldY;
    private static final int TOUCH_TOLERANCE = 5;
    private boolean in_use;
    
    public PathTool(int color, int size)
    {
        Paint realColor = new Paint();
        realColor.setColor(color);
        realColor.setStyle(Paint.Style.STROKE);
        this.size = size;
        realColor.setStrokeWidth(size);
        this.color = realColor;
        this.in_use = false;
    }

    @Override
    public void drawTool(Canvas canvas) {
        if(path != null)
        {
            this.color.setStrokeWidth(this.size);
            canvas.drawPath(this.path, this.color);
        }
    }


    @Override
    public void handleEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if(event.getAction()==  MotionEvent.ACTION_DOWN)
        {
            in_use = true;
            path = new Path();
            path.reset();
            path.moveTo(x, y);
            oldX = x;
            oldY = y;

        }
        if(event.getAction()==  MotionEvent.ACTION_MOVE)
        {
            float delta_x = Math.abs(x - oldX);
            float delta_y = Math.abs(y - oldY);

            if (delta_x > TOUCH_TOLERANCE || delta_y > TOUCH_TOLERANCE) {
                path.quadTo(oldX, oldY, (x + oldX) / 2, (y + oldY) / 2);
                oldX = x;
                oldY = y;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            path.lineTo(oldX, oldY);
            in_use = false;
        }

    }

    @Override
    public void cleanUp() {
        if(!in_use)
            path = null;
    }

    @Override
    public int getId() {
        return R.drawable.ic_outline_brush_24px;
    }
}
