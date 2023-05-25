package com.kore.ai.widgetsdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

import static com.kore.ai.widgetsdk.utils.DimensionUtil.dp1;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Shiva Krishna on 2/22/2018.
 */

public class ProfileTextView extends AppCompatTextView {
    private Paint paint;
    private Paint tPaint;
    private int color;

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    boolean isCircle = true;

    public boolean isForToken() {
        return isForToken;
    }

    public void setForToken(boolean forToken) {
        isForToken = forToken;
    }

    boolean isForToken = false;

    public ProfileTextView(Context context) {
        super(context);
        init();
    }

    public ProfileTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public final void init(){
        paint = new Paint();
        tPaint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        tPaint.setStyle(Paint.Style.STROKE);
        tPaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(color);
        if(isForToken){
            int  h = this.getHeight();
            int  w = this.getWidth();

            int diameter = ((h > w) ? h : w);
            int radius = diameter/2;

            this.setHeight(diameter);
            this.setWidth(diameter);

            canvas.drawCircle(diameter / 2 , diameter / 2, radius-2, paint);
            tPaint.setColor(Color.parseColor("#485260"));
            canvas.drawCircle(diameter / 2 , diameter / 2, radius-1, tPaint);
        }else if (isCircle) {
            int  h = this.getHeight();
            int  w = this.getWidth();

            int diameter = ((h > w) ? h : w);
            int radius = diameter/2;

            this.setHeight(diameter);
            this.setWidth(diameter);

            canvas.drawCircle(diameter / 2 , diameter / 2, radius, paint);

        }else{
            Path path = RoundedRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft()+getMeasuredWidth() , getPaddingTop()+getMeasuredHeight() , (float) (8 * dp1),(float) (8 * dp1));
            canvas.drawPath(path, paint);
        }
        super.onDraw(canvas);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public static Path RoundedRect(
            float left, float top, float right, float bottom, float rx, float ry
    ){
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);
        path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        path.rLineTo(widthMinusCorners, 0);
        path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        path.rLineTo(0, -heightMinusCorners);
        path.close();//Given close, last lineto can be removed.

        return path;
    }
}
