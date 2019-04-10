package at.tugraz.ist.swe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class DrawArea extends View {

    private int size;
    private int color;
    private Paint oldColor;
    private int tool;
    private PaintingTool paintingTool;
    private Point position;
    private Bitmap oldBitmap;
    private boolean generate;
    private boolean applied;

    public DrawArea(Context context)
    {
        super(context);
        this.size = 10;
        this.tool = 0;
    }

    protected void onDraw(Canvas canvas) {
        if (oldBitmap != null)
        {
            canvas.drawBitmap(oldBitmap, 0, 0, null);
        }
        paintingTool.drawTool(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        oldBitmap = getBitmap();
        paintingTool.handleEvent(event);
        invalidate();
        return false;
    }

    private Bitmap getBitmap()
    {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap current = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return current;
    }

    public void setTool(PaintingTool tool) {
        this.paintingTool = tool;
    }
}
