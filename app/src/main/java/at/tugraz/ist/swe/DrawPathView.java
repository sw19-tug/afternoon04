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

public class DrawPathView extends View {

    Paint paint;
    private List<Point> point_list = new ArrayList<>();

    public DrawPathView(Context context) {
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

    public List<Point> getPoint_list()
    {
        return point_list;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                Point input_point = new Point((int)x, (int)y);
                point_list.add(input_point);

                invalidate();

            case MotionEvent.ACTION_MOVE:
                float move_x = event.getX();
                float move_y = event.getY();
                Point move_point = new Point((int)move_x, (int)move_y);
                point_list.add(move_point);

                invalidate();
        }
        return false;
    }
}