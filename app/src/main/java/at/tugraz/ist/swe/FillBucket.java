package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class FillBucket extends PaintingTool {

    private Point position;
    private Bitmap bitmap;

    public FillBucket(int color, int size,Bitmap bitmap)
    {
        Paint realColor = new Paint();
        realColor.setColor(color);
        realColor.setStyle(Paint.Style.FILL);
        this.color = realColor;
        this.size = size;
        this.position = null;
        this.bitmap = bitmap;
    }

    @Override
    public void drawTool(Canvas canvas)
    {
        if(position != null) {
            QueueLinearFloodFiller filler = new QueueLinearFloodFiller(bitmap,Color.BLACK, Color.RED);
            filler.floodFill(position.x, position.y);
        }

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
