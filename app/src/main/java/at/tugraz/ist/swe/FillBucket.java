package at.tugraz.ist.swe;

import android.graphics.Canvas;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class FillBucket extends PaintingTool {

    private Point position;

    public FillBucket(int color, int size)
    {
        Paint realColor = new Paint();
        realColor.setColor(color);
        realColor.setStyle(Paint.Style.FILL);
        this.color = realColor;
        this.size = size;
        this.position = null;
    }

    @Override
    public void drawTool(Canvas canvas)
    {
        if(position != null)
            //View DrawArea = findViewById
            QueueLinearFloodFiller Filler = new QueueLinearFloodFiller(canvas.)
    }

    @Override
    public void handleEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            this.position = new Point((int)x, (int)y);
        }
    }
}
