package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class Line extends PaintingTool {

    private Point start_position;
    private Point end_position;
    private boolean in_use;

    public Line(int color, int size) {
        Paint realColor = new Paint();
        realColor.setColor(color);
        realColor.setStyle(Paint.Style.STROKE);
        this.size = size;
        this.color = realColor;
        this.start_position = null;
        this.end_position = null;
    }

    @Override
    public void drawTool(Canvas canvas) {
        if (start_position != null && end_position != null) {
            this.color.setStrokeWidth(this.size);
            canvas.drawLine(this.start_position.x, this.start_position.y, this.end_position.x, this.end_position.y, this.color);
        }
    }

    @Override
    public void handleEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            this.start_position = new Point((int)x, (int)y);
            this.in_use = true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            this.end_position = new Point((int) x, (int) y);
        }
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            this.in_use = false;
        }
    }

    @Override
    public void cleanUp()
    {
        if(!in_use)
        {
            this.end_position = null;
        }
    }

    @Override
    public int getId() {
        return R.drawable.ic_si_glyph_line_two_angle_point;
    }
}
