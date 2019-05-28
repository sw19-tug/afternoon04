package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;


public class ImageImportTool extends PaintingTool {

    private Bitmap image;
    private PointF position;

    public ImageImportTool(Bitmap file) {
        this.image = file;
        this.position = null;
    }

    @Override
    public void drawTool(Canvas canvas) {
        
        if (image != null && position != null) {
            canvas.drawBitmap(image, position.x - image.getWidth()/2.f, position.y - image.getHeight()/2.f, null);

        }

    }

    @Override
    public void handleEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            position = new PointF(x, y);

        }
    }

    @Override
    public void cleanUp() {

    }


}
