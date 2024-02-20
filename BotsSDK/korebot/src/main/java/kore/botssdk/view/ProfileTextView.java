package kore.botssdk.view;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

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

   /* public ProfileTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }*/
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
            Path path = RoundedRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft()+getMeasuredWidth() , getPaddingTop()+getMeasuredHeight() , 8 * dp1, 8 * dp1,
                    true, true,true,true);
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
            float left, float top, float right, float bottom, float rx, float ry,
            boolean tl, boolean tr, boolean br, boolean bl
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
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else{
            path.rLineTo(0, -ry);
            path.rLineTo(-rx,0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else{
            path.rLineTo(-rx, 0);
            path.rLineTo(0,ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else{
            path.rLineTo(0, ry);
            path.rLineTo(rx,0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else{
            path.rLineTo(rx,0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }
}
