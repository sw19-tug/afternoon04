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
    private Bitmap oldBitmap = null;
    private boolean handleEvents;
    private boolean drawCurrentTool;
    private boolean prepareBitmap;
    private boolean undo = false;

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

        //oldBitmap = BitmapCache.mMemoryCache.get("step" + Integer.toString(BitmapCache.oldBitmap));

        if(this.undo)
        {
            oldBitmap = BitmapCache.mMemoryCache.get("step" + Integer.toString(BitmapCache.oldBitmap));
        }

        if (oldBitmap != null)
        {
            canvas.drawBitmap(oldBitmap, 0, 0, null);
        }
        else if(prepareBitmap)
        {
            prepareBitmap = false;
            oldBitmap = this.createBitmap();
            BitmapCache.mMemoryCache.put("step0", oldBitmap);
        }
        if(drawCurrentTool)
            paintingTool.drawTool(canvas);
        if(undo)
        {
            this.undo = false;
            BitmapCache.oldBitmap--;
            BitmapCache.current_step--;
            this.drawCurrentTool = true;
        }

    }

    public void setHandleToucheEvents(boolean bool)
    {
        handleEvents = bool;
    }

    public void setDrawCurrentTool(boolean bool)
    {
        drawCurrentTool = bool;
    }

    public boolean onTouchEvent(MotionEvent event) {

        if(handleEvents) {
            this.drawCurrentTool = true;
            paintingTool.handleEvent(event);
            invalidate();
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                BitmapCache.oldBitmap = BitmapCache.current_step;
                BitmapCache.current_step++;
                if(BitmapCache.current_step >= BitmapCache.max_undo_steps)
                {
                    BitmapCache.current_step = BitmapCache.current_step - BitmapCache.max_undo_steps;
                    BitmapCache.redo_overflow = true;
                }
                BitmapCache.current_undo = BitmapCache.current_step;
            }

            if(event.getAction() == MotionEvent.ACTION_UP)
            {
                oldBitmap = createBitmap();
                paintingTool.cleanUp();
                BitmapCache.mMemoryCache.put("step"+Integer.toString(BitmapCache.current_step), oldBitmap);
                invalidate();
            }
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

    public void undoStep()
    {
        setDrawCurrentTool(false);
        this.undo = true;
        invalidate();
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
