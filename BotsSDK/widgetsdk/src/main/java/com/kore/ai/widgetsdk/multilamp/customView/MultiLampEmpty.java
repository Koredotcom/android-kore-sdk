package com.kore.ai.widgetsdk.multilamp.customView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.kore.ai.widgetsdk.multilamp.model.Target;

import java.util.ArrayList;

public class MultiLampEmpty extends RelativeLayout {

    private final int overlayColor = Color.parseColor("#66000000");
    private ArrayList<Target> list;
    private final Activity activity;
    public static final char LEFT = 'l';
    public static final char RIGHT = 'r';
    public static final char TOP = 't';
    public static final char BOTTOM = 'b';
    private final Context context;
    //    private float density;
    private Callback callback;

    public interface Callback {
        void onTutorialFinished();
    }

    public MultiLampEmpty(Activity activity) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.context = activity.getApplicationContext();

        init();
    }

    public MultiLampEmpty(Activity activity, @Nullable AttributeSet attrs) {
        super(activity.getApplicationContext(), attrs);
        this.activity = activity;
        this.context = activity.getApplicationContext();
        init();
    }

    public MultiLampEmpty(Activity activity, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(activity.getApplicationContext(), attrs, defStyleAttr);
        this.activity = activity;
        this.context = activity.getApplicationContext();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MultiLampEmpty(Activity activity, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(activity.getApplicationContext(), attrs, defStyleAttr, defStyleRes);
        this.activity = activity;
        this.context = activity.getApplicationContext();
        init();
    }

    private void init() {
//        density = context.getResources().getDisplayMetrics().density;
        list = new ArrayList<>();
        setLayerType(LAYER_TYPE_HARDWARE, null);
          }

    @SuppressLint("ClickableViewAccessibility")
    public void build(ArrayList<Target> list) {
        this.list = list;

//        bringToFront();
        setWillNotDraw(false);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        this.setClickable(false);
        if (null != this.getParent()) {
            ((ViewGroup) activity.getWindow().getDecorView()).removeView(this);
        }
        //todo need to work on it
        //        WindowManager.LayoutParams params5 = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSLUCENT);

        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);

        if (null != list && list.size() > 0) {
            invalidate();
        }
    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    //
   @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint;
        canvas.drawColor(overlayColor);
        if (null != list && list.size() > 0) {
            for (Target target : list) {
                View view = target.getView();
                paint = new Paint();
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                paint.setTextSize(60f);
                paint.setTypeface(Typeface.create("Arial", Typeface.NORMAL));
                int[] location = new int[2];
                view.getLocationInWindow(location);
                float x = view.getX() + view.getWidth() / 2f;
                float y = view.getY() + view.getHeight() / 2f;
                target.getShape().draw(canvas, context, new PointF(view.getX(), view.getY()), 0, target, paint);
            }
        }

//        canvas.drawCircle(mCx, mCy, 100f, mBackgroundPaint);
    }
//
//    @Override
//    public void onClick(View view) {
//        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
//        if (null != activity && null != this.getParent()) {
////            ((ViewGroup) ((Activity) activity).getWindow().getDecorView()).removeView(this);
//            this.setVisibility(View.GONE);
//        }
//    }

    public void dissmissView()
    {
        if (null != activity && null != this.getParent()) {
            ((ViewGroup) activity.getWindow().getDecorView()).removeView(this);

        }
    }
}
