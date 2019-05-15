package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public abstract class PaintingTool
{
    protected Paint color;
    protected int size;
    protected Bitmap oldBitmap;

    public abstract void drawTool(Canvas canvas);
    public abstract void handleEvent(MotionEvent event);
    public void setColor(int color)
    {
        this.color.setColor(color);
    }
    public void setSize(int size)
    {
        this.size = size;
    }
}
