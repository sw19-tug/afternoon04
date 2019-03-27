package at.tugraz.ist.swe;

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

import org.zakariya.flyoutmenu.FlyoutMenuView;

public class FlyoutToolbar {

    static String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public static class MenuItem extends FlyoutMenuView.MenuItem {

        int emojiCode;
        String emojiString;
        TextPaint textPaint;

        public MenuItem(int id, int emojiCode, float size, @ColorInt int color) {
            super(id);
            this.emojiCode = emojiCode;
            this.emojiString = getEmojiByUnicode(emojiCode);

            textPaint = new TextPaint();
            textPaint.setTextSize(size);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(color);
        }

        public int getEmojiCode() {
            return emojiCode;
        }

        @Override
        public void onDraw(Canvas canvas, RectF bounds, float degreeSelected) {
            canvas.drawText(emojiString, bounds.centerX(), bounds.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        }
    }

    public static class MenuItemImage extends FlyoutMenuView.MenuItem
    {
        Drawable icon;

        public MenuItemImage(int id, int vector, Context context)
        {
            super(id);
            icon = AppCompatResources.getDrawable(context, vector);
        }

        @Override
        public void onDraw(Canvas canvas, RectF bounds, float degreeSelected)
        {
            icon.setBounds(canvas.getClipBounds());
            icon.draw(canvas);
        }
    }

    public static class ButtonRenderer extends FlyoutMenuView.ButtonRenderer {

        int emojiCode;
        String emojiString;
        Paint paint;
        TextPaint textPaint;

        public ButtonRenderer(int emojiCode, float size, @ColorInt int color) {
            super();

            this.setEmojiCode(emojiCode);

            paint = new Paint();
            paint.setAntiAlias(true);

            textPaint = new TextPaint();
            textPaint.setTextSize(size);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(color);
        }

        public int getEmojiCode() {
            return emojiCode;
        }

        public void setEmojiCode(int emojiCode) {
            this.emojiCode = emojiCode;
            this.emojiString = getEmojiByUnicode(this.emojiCode);
        }

        @Override
        public void onDrawButtonContent(Canvas canvas, RectF buttonBounds, @ColorInt int buttonColor, float alpha) {
            textPaint.setAlpha((int) (alpha * 255f));
            canvas.drawText(emojiString, buttonBounds.centerX(), buttonBounds.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        }
    }
}