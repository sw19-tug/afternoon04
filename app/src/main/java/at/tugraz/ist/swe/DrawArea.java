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
    private Paint color;
    private Paint oldColor;
    private int tool;
    private Point position;
    private Bitmap oldBitmap;
    private boolean generate;
    private boolean applied;

    public DrawArea(Context context)
    {
        super(context);
        this.size = 10;
        this.color = new Paint();
        this.color.setStyle(Paint.Style.FILL);
        this.tool = 0;
        generate = false;
        this.oldColor = new Paint();
        this.oldColor.setColor(Color.BLACK);
        this.oldColor.setStyle(Paint.Style.FILL);
        this.applied = false;
    }

    protected void onDraw(Canvas canvas) {
        switch (tool)
        {
            case R.drawable.ic_si_glyph_circle:
                if(position != null && oldBitmap != null) {
                    canvas.drawBitmap(oldBitmap, 0, 0, null);
                    if(generate)
                        canvas.drawCircle(position.x, position.y, size, oldColor);
                    else
                        canvas.drawCircle(position.x, position.y, size, color);
                }
                break;
            default:
                break;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(tool)
        {
            case R.drawable.ic_si_glyph_circle:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    oldBitmap = getBitmap();
                    this.applied = true;
                    this.position = new Point((int)x, (int)y);
                    invalidate();
                }
                break;
            default:
                break;
        }
        return false;
    }

    private Bitmap getBitmap()
    {
        generate = true;
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap current = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        generate = false;
        this.oldColor.setColor(this.color.getColor());
        return current;
    }

    public void setTool(int tool) {
        this.tool = tool;
    }

    public void setColor(int color) {
        if(applied) {
            this.oldColor.setColor(this.color.getColor());
            applied = false;
        }
        this.color.setColor(color);
    }
}
