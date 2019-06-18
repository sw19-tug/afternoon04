package at.tugraz.ist.swe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class DrawArea extends View {

    private StepsListener listener;

    public interface StepsListener
    {
        void onTouched(boolean undo, boolean redo);
    }

    public void setStepListener(StepsListener listener){
        this.listener = listener;
    }

    public PaintingTool getPaintingTool() {
        return paintingTool;
    }



    private PaintingTool paintingTool;
    private Bitmap oldBitmap = null;
    private boolean handleEvents;
    private boolean drawCurrentTool;
    private boolean prepareBitmap;
    private boolean undo = false;
    private boolean redo = false;
    private int pixelColor = 0;
    private boolean getPixel = false;

    public DrawArea(Context context)
    {
        super(context);
        this.listener = null;
        this.paintingTool = new Circle(Color.BLACK, 10);
        this.setId(R.id.draw_point_view);
        this.setBackgroundColor(Color.WHITE);
        this.handleEvents = true;
        this.drawCurrentTool = true;
        this.prepareBitmap = true;
    }

    protected void onDraw(Canvas canvas) {

        if(this.undo)
        {
            oldBitmap = BitmapCache.mMemoryCache.get("step" + Integer.toString(BitmapCache.oldBitmap));
        }

        else if(this.redo)
        {
            this.redo = false;
            BitmapCache.oldBitmap++;
            BitmapCache.nextBitmap++;
            if(BitmapCache.redo_overflow) {
                if (BitmapCache.oldBitmap >= BitmapCache.max_undo_steps)
                    BitmapCache.oldBitmap = 0;
                if (BitmapCache.nextBitmap >= BitmapCache.max_undo_steps)
                    BitmapCache.nextBitmap = 0;
            }
            oldBitmap = BitmapCache.mMemoryCache.get("step" + Integer.toString(BitmapCache.nextBitmap));
        }

        if (oldBitmap != null)
        {
            canvas.drawBitmap(oldBitmap, 0, 0, null);
        }
        else if(prepareBitmap)
        {
            prepareBitmap = false;
            if(BitmapCache.mMemoryCache.get("step0") == null)
            {
                oldBitmap = this.createBitmap();
                BitmapCache.mMemoryCache.put("step0", oldBitmap);
                BitmapCache.max_undo_steps = (int)(BitmapCache.cacheSize / (BitmapCache.mMemoryCache.get("step0").getByteCount() / 1024));
            }
            else
            {
                oldBitmap = BitmapCache.mMemoryCache.get("step" + Integer.toString(BitmapCache.nextBitmap));
                canvas.drawBitmap(oldBitmap, 0, 0, null);
            }
        }
        if(drawCurrentTool)
            paintingTool.drawTool(canvas);
        if(undo)
        {
            this.undo = false;
            BitmapCache.oldBitmap--;
            BitmapCache.nextBitmap--;
            if(BitmapCache.redo_overflow) {
                if (BitmapCache.oldBitmap < 0)
                    BitmapCache.oldBitmap = BitmapCache.max_undo_steps - 1;
                if (BitmapCache.nextBitmap < 0)
                    BitmapCache.nextBitmap = BitmapCache.max_undo_steps - 1;
            }
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
                listener.onTouched(false, false);
                BitmapCache.oldBitmap = BitmapCache.nextBitmap;
                if(BitmapCache.redo_overflow && BitmapCache.array_position != BitmapCache.nextBitmap && BitmapCache.dead_zone_start == -1)
                    BitmapCache.dead_zone_start = BitmapCache.array_position;
                BitmapCache.nextBitmap++;
                BitmapCache.array_position = BitmapCache.nextBitmap;
                if(BitmapCache.dead_zone_start != -1 && BitmapCache.dead_zone_start == BitmapCache.array_position)
                    BitmapCache.dead_zone_start = -1;
                if(BitmapCache.nextBitmap >= BitmapCache.max_undo_steps)
                {
                    BitmapCache.nextBitmap = 0;
                    BitmapCache.redo_overflow = true;
                }
                if(BitmapCache.array_position >= BitmapCache.max_undo_steps) {
                    BitmapCache.array_position = 0;
                }
            }

            if(event.getAction() == MotionEvent.ACTION_UP)
            {
                oldBitmap = createBitmap();
                paintingTool.cleanUp();
                BitmapCache.mMemoryCache.put("step"+Integer.toString(BitmapCache.nextBitmap), oldBitmap);
                invalidate();
                listener.onTouched(true, false);
            }
        }
        else if(!handleEvents && getPixel)
        {
            paintingTool.handleEvent(event);
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
    public void redoStep()
    {
        setDrawCurrentTool(false);
        this.redo = true;
        invalidate();
    }

    public void getPixelColor(boolean status)
    {
        getPixel = status;
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

    public void resetCanvas() {
        this.oldBitmap.eraseColor(Color.WHITE);
    }
}
