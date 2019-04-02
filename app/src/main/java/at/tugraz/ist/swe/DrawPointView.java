package at.tugraz.ist.swe;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawPointView extends View {

    Paint paint;
    private List<Point> point_list = new ArrayList<>();

    public DrawPointView(Context context) {
        super(context);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        for(Point p : point_list)
            canvas.drawCircle(p.x, p.y, 10, paint);

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            Point input_point = new Point((int)x, (int)y);
            point_list.add(input_point);

            invalidate();
        }
        return false;
    }
}