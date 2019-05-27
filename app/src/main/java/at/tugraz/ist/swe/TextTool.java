package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class TextTool extends PaintingTool {

    private String text;
    private Point position;

    public TextTool(int color, int size, String text) {

        this.text = text;
        Paint newColor = new Paint();
        newColor.setColor(color);
        newColor.setTextSize(size);
    }

    @Override
    public void drawTool(Canvas canvas) {
        if(position != null)
        {
            this.color.setTextSize(size);
            canvas.drawText(text, position.x - (color.measureText(text) / 2), position.y, color);
        }
    }

    @Override
    public void handleEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
        {
            position = new Point((int)event.getX(), (int)event.getY());
        }
    }

    @Override
    public void cleanUp() {

    }
}
