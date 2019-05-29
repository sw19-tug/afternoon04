package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class Circle extends PaintingTool {

    private Point position;

    public Circle(int color, int size)
    {
        Paint realColor = new Paint();
        realColor.setColor(color);
        realColor.setStyle(Paint.Style.FILL);
        this.color = realColor;
        this.size = size;
        this.position = null;
    }

    @Override
    public void drawTool(Canvas canvas)
    {
        if(position != null)
            canvas.drawCircle(this.position.x, this.position.y, this.size, this.color);
    }

    @Override
    public void handleEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            this.position = new Point((int)x, (int)y);
        }
    }

    @Override
    public void cleanUp() {}

    @Override
    public int getId() {
        return R.drawable.ic_si_glyph_circle;
    }
}
