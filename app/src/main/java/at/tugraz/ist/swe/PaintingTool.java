package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public abstract class PaintingTool
{
    protected Paint color;
    protected int size;

    public abstract void drawTool(Canvas canvas);
    public abstract void handleEvent(MotionEvent event);
    public abstract void cleanUp();
    public void setColor(int color)
    {
        this.color.setColor(color);
    }
    public void setSize(int size)
    {
        this.size = size;
    }
}
