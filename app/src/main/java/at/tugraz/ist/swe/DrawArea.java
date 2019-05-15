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

    public DrawArea(Context context)
    {
        super(context);
        this.paintingTool = new Circle(Color.BLACK, 10);
        this.setId(R.id.draw_point_view);
    }

    protected void onDraw(Canvas canvas) {
        if (oldBitmap != null)
        {
            canvas.drawBitmap(oldBitmap, 0, 0, null);
        }
        paintingTool.drawTool(canvas);
    }
    public boolean onTouchEvent(MotionEvent event) {

        paintingTool.handleEvent(event);
        invalidate();
        if(event.getAction() != MotionEvent.ACTION_MOVE)
        {
            oldBitmap = createBitmap();
            paintingTool.cleanUp();
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
}
