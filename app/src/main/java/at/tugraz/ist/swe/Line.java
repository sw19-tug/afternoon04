package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class Line extends PaintingTool {

    private Point start_position;
    private Point end_position;

    public Line(int color, int size) {
        Paint realColor = new Paint();
        realColor.setColor(color);
        realColor.setStyle(Paint.Style.FILL);
        this.color = realColor;
        this.size = size;
        this.start_position = null;
        this.end_position = null;
    }

    @Override
    public void drawTool(Canvas canvas) {
        if(start_position != null && end_position != null)
            canvas.drawLine(this.start_position.x, this.start_position.y, this.end_position.x, this.end_position.y, this.color);
    }

    @Override
    public void handleEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            this.start_position = new Point((int)x, (int)y);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            this.end_position = new Point((int) x, (int) y);
        }

    }
}
