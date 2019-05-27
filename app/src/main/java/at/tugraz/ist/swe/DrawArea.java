package at.tugraz.ist.swe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class DrawArea extends View {


    public PaintingTool getPaintingTool() {
        return paintingTool;
    }

    private PaintingTool paintingTool;
    private Bitmap oldBitmap;
    private boolean handleEvents;
    private boolean drawCurrentTool;

    public DrawArea(Context context)
    {
        super(context);
        this.paintingTool = new Circle(Color.BLACK, 10);
        this.setId(R.id.draw_point_view);
        this.handleEvents = true;
        this.drawCurrentTool = true;
    }

    protected void onDraw(Canvas canvas) {
        oldBitmap = BitmapCache.mMemoryCache.get("oldBitmap");
        if (BitmapCache.mMemoryCache.get("oldBitmap") != null)
        {
            canvas.drawBitmap(oldBitmap, 0, 0, null);
        }
        if(drawCurrentTool)
            paintingTool.drawTool(canvas);
    }

    public void setHandleToucheEvents(boolean bool)
    {
        handleEvents = bool;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(handleEvents)
        {
            this.drawCurrentTool = true;
            paintingTool.handleEvent(event);
            invalidate();
            BitmapCache.mMemoryCache.put("oldBitmap", getBitmap());
        }
        return false;
    }

    public Bitmap getBitmap()
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

    public void setSize(int size)
    {
        this.paintingTool.setSize(size);
        this.drawCurrentTool = false;
    }
}
