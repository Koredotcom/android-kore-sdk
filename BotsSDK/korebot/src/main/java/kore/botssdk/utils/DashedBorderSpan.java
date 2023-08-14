package kore.botssdk.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;

public class DashedBorderSpan extends ReplacementSpan {

    private final Drawable mDrawable;
    private final int mPadding;


    public DashedBorderSpan(Drawable drawable, int padding) {
        super();

        mDrawable = drawable;
        mPadding = padding;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        RectF rect = new RectF(x - mPadding, top - mPadding, x + measureText(paint, text, start, end) + mPadding, bottom + mPadding);

        mDrawable.setBounds((int) rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom);


        canvas.drawText(text, start, end, x, y, paint);
        mDrawable.draw(canvas);
    }



    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}