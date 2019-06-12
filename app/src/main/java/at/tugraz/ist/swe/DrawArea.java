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
    private boolean prepareBitmap;

    public DrawArea(Context context)
    {
        super(context);
        this.paintingTool = new Circle(Color.BLACK, 10);
        this.setId(R.id.draw_point_view);
        this.setBackgroundColor(Color.WHITE);
        this.handleEvents = true;
        this.drawCurrentTool = true;
        this.prepareBitmap = true;
    }

    protected void onDraw(Canvas canvas) {
        oldBitmap = BitmapCache.mMemoryCache.get("oldBitmap");
        if (BitmapCache.mMemoryCache.get("oldBitmap") != null)
        {
            canvas.drawBitmap(oldBitmap, 0, 0, null);
        }
        else if(prepareBitmap)
        {
            prepareBitmap = false;
            oldBitmap = this.createBitmap();
            BitmapCache.mMemoryCache.put("oldBitmap", oldBitmap);
        }
        if(drawCurrentTool)
            paintingTool.drawTool(canvas);
    }

    public void setHandleToucheEvents(boolean bool)
    {
        handleEvents = bool;
    }

    public boolean onTouchEvent(MotionEvent event) {

        if(handleEvents) {
            this.drawCurrentTool = true;
            paintingTool.handleEvent(event);
            invalidate();
            if (event.getAction() != MotionEvent.ACTION_MOVE) {
                oldBitmap = createBitmap();
                paintingTool.cleanUp();
            }
            if(event.getAction() == MotionEvent.ACTION_UP)
                BitmapCache.mMemoryCache.put("oldBitmap", oldBitmap);
        }
        return true;
    }
    private Bitmap createBitmap()
    {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap current = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return current;
    }

    public Bitmap getBitmap()
    {
        return this.oldBitmap;
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
