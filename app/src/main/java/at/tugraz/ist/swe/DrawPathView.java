package at.tugraz.ist.swe;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        paint.setStrokeWidth(12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        for(Point p : point_list) {
          canvas.drawLine(p.getPreviousX(), p.getPreviousY(), p.getX(), p.getY(),paint);
        }

    }

    public List<Point> getPoint_list()
    {
        return point_list;
    }

    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
          case MotionEvent.ACTION_DOWN: float x = event.getX();
                                        float y = event.getY();
                                        float previous_x = event.getX();
                                        float previous_y = event.getY();
                                        Point input_point = new Point((int) x, (int) y, (int) previous_x, (int) previous_y);
                                        point_list.add(input_point);
                                        invalidate();
                                        break;

           case MotionEvent.ACTION_MOVE :   float move_x = event.getX();
                                            float move_y = event.getY();
                                            event.getHistorySize();
                                            float move_previous_x = event.getHistoricalX((int) move_x);
                                            float move_previous_y = event.getHistoricalY((int) move_y);
                                            Point move_point = new Point((int) move_x, (int) move_y, (int) move_previous_x, (int) move_previous_y);
                                            point_list.add(move_point);
                                            invalidate();
                                            break;
            case MotionEvent.ACTION_UP : break;
        }

        return true;
    }
}