package at.tugraz.ist.swe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.Toast;

public class Pipette extends PaintingTool {

    private Context context;
    private ColorPicker colorPicker;
    private DrawArea drawArea;
    private Point position;


    public Pipette(@NonNull Context _context, ColorPicker _colorPicker, DrawArea _drawArea)
    {
       this.context = _context;
       this.colorPicker = _colorPicker;
       this.drawArea = _drawArea;
       this.position = null;
    }

    @Override
    public void drawTool(Canvas canvas) {}

    @Override
    public void handleEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int pixel = this.drawArea.getBitmap().getPixel((int) event.getX(),(int) event.getY());
            this.colorPicker.setColor(Color.alpha(pixel), Color.red(pixel),Color.green(pixel), Color.blue(pixel));
            Toast.makeText(this.context,"Set new Color: " + Integer.toString(pixel) , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cleanUp() {}

    @Override
    public int getId() {
        return R.drawable.ic_pipette;
    }
}
