package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;



public class ImageImportTool extends PaintingTool {

    private Bitmap image;
    private PointF position;
    private boolean overwrite_full_canvas;

    public ImageImportTool(Bitmap file) {
        this.image = file;
        this.position = null;
        this.overwrite_full_canvas = false;
    }

    @Override
    public void drawTool(Canvas canvas) {
        float pos_x = 0.0f;
        float pos_y = 0.0f;
        int c_width = canvas.getWidth();
        int c_height = canvas.getHeight();

        if (image != null && position != null) {

            if (image.getHeight() > c_height || image.getWidth() > c_width) {
                image = scaleDownBitmap(image, c_height, c_width);
                overwrite_full_canvas = true;
            }

            if (!overwrite_full_canvas) {
                pos_x = position.x - image.getWidth() / 2.f;
                pos_y = position.y - image.getHeight() / 2.f;
            }
            canvas.drawBitmap(image, pos_x, pos_y, null);
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

    @Override
    public int getId() {
        return 0;
    }

    private Bitmap scaleDownBitmap(Bitmap bm, int max_height, int max_width) {

        int new_height = bm.getHeight();
        int new_width = bm.getWidth();

        float aspect_ratio = new_width/ (float) new_height;

        if (new_width > max_width) {
            new_width = max_width;
            new_height = Math.round(new_width / aspect_ratio);
        }

        if (new_height > max_height) {
            new_height = max_height;
            new_width = Math.round(new_height * aspect_ratio);
        }

        return Bitmap.createScaledBitmap(
                bm, new_width, new_height, false);

    }



}
