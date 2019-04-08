package at.tugraz.ist.swe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.ColorInt;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextPaint;
import android.util.DisplayMetrics;

import org.zakariya.flyoutmenu.FlyoutMenuView;

public class FlyoutToolbar {



    static String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public static class MenuItemImage extends FlyoutMenuView.MenuItem {

        private int scale;

        private Drawable icon;
        private int id;

        public MenuItemImage(int id, int vector, Context context)
        {
            super(id);
            this.id = id;
            icon = AppCompatResources.getDrawable(context, vector);
            DisplayMetrics display;
            Application app = (Application) context;
            display = app.getResources().getDisplayMetrics();
            scale = (int)(15 * display.density);
        }

        @Override
        public void onDraw(Canvas canvas, RectF bounds, float degreeSelected)
        {
            Rect new_bounds = new Rect();
            new_bounds.top = (int)bounds.top + this.scale;
            new_bounds.left = (int)bounds.left + this.scale;
            new_bounds.bottom = (int)bounds.bottom - this.scale;
            new_bounds.right = (int)bounds.right - this.scale;

            icon.setBounds(new_bounds);
            icon.draw(canvas);
        }

        public int getID()
        {
            return id;
        }
    }

    public static class ButtonRenderer extends FlyoutMenuView.ButtonRenderer {

        private int scale;

        Drawable icon;

        public ButtonRenderer(int vector, Context context) {
            super();
            icon = AppCompatResources.getDrawable(context, vector);
            DisplayMetrics display;
            Application app = (Application) context;
            display = app.getResources().getDisplayMetrics();
            scale = (int)(8 * display.density);
        }

        @Override
        public void onDrawButtonContent(Canvas canvas, RectF bounds, @ColorInt int buttonColor, float alpha) {
            Rect new_bounds = new Rect();
            new_bounds.top = (int)bounds.top + this.scale;
            new_bounds.left = (int)bounds.left + this.scale;
            new_bounds.bottom = (int)bounds.bottom - this.scale;
            new_bounds.right = (int)bounds.right - this.scale;
            icon.setBounds(new_bounds);
            icon.draw(canvas);
        }
    }
}