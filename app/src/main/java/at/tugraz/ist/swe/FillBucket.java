package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class FillBucket extends PaintingTool {

    private Point position;

    public FillBucket(int color)
    {
        Paint realColor = new Paint();
        realColor.setColor(color);
        realColor.setStyle(Paint.Style.FILL);
        this.color = realColor;
        this.position = null;
    }

    @Override
    public void drawTool(Canvas canvas)
    {
        if(position != null) {
            Bitmap bitmap = BitmapCache.mMemoryCache.get("oldBitmap");
            int alpha_color = Color.alpha(bitmap.getPixel(position.x,position.y));
            int red_color = Color.red(bitmap.getPixel(position.x,position.y));
            int green_color = Color.green(bitmap.getPixel(position.x,position.y));
            int blue_color = Color.blue(bitmap.getPixel(position.x,position.y));

            QueueLinearFloodFiller filler = new QueueLinearFloodFiller(bitmap,Color.argb(alpha_color,red_color,green_color,blue_color), color.getColor(),canvas);
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

    @Override
    public void cleanUp() {

    }


}
