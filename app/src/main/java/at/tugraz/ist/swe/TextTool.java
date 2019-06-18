package at.tugraz.ist.swe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class TextTool extends PaintingTool {

    private String text;
    private Point position;
    private int scale = 2;
    private Context context;

    public TextTool(Context context, int color, int size, String text) {
        this.size = size;
        this.text = text;
        Paint newColor = new Paint();
        newColor.setColor(color);
        newColor.setTextAlign(Paint.Align.CENTER);
        this.color = newColor;
        this.context = context;
    }

    @Override
    public void drawTool(Canvas canvas) {
        if(this.position != null)
        {
            this.color.setTextSize(size * context.getResources().getDisplayMetrics().density);
            canvas.drawText(text, this.position.x, this.position.y, color);
        }
    }

    @Override
    public void handleEvent(MotionEvent event) {
            this.position = new Point((int)event.getX(), (int)event.getY());
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public int getId() {
        return R.drawable.ic_baseline_text_fields_24px;
    }
}
