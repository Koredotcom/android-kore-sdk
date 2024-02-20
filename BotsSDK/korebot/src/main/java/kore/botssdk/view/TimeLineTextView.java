package kore.botssdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep on 18-Oct-15.
 */
public class TimeLineTextView extends ViewGroup {

//    private static final String LOG_TAG = TimeLineTextView.class.getSimpleName();

    private TextView unreadTimeLineTextView;

    int dp1;
    Paint paint;
    int DARK_TIMELINE_COLOR, TIMELINE_DIVIDER_COLOR;
    Drawable UNREAD_TIMELINE_DOWN_ARROW;
    String UNREAD_TIMELINE_TEXT;
    int[] colorLeft;
    int[] colorRight;
    final float[] positionLeft = new float[]{0f, 0f};
    final float[] positionRight = new float[]{0f, 0f};

    public TimeLineTextView(Context context) {
        this(context, null);
    }

    public TimeLineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        createView(context);
    }

    private void createView(Context context) {

        if (!isInEditMode()) {
            dp1 = (int) DimensionUtil.dp1;
            DARK_TIMELINE_COLOR = getResources().getColor(R.color.color_B0B0B0);
            TIMELINE_DIVIDER_COLOR = getResources().getColor(R.color.color_B0B0B0);
           // UNREAD_TIMELINE_TEXT = getResources().getString(R.string.unread_timeline_text);
            //UNREAD_TIMELINE_DOWN_ARROW = getResources().getDrawable(R.drawable.down_arrow_small);

            colorLeft = new int[]{0x00c4d2d7, TIMELINE_DIVIDER_COLOR};
            colorRight = new int[]{TIMELINE_DIVIDER_COLOR, 0x00c4d2d7};
        }

        setWillNotDraw(false);
        paint = new Paint();
        customisePaint();

        //messageDateRow1
        unreadTimeLineTextView = new TextView(context);
        LayoutParams lparam2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        unreadTimeLineTextView.setLayoutParams(lparam2);
        unreadTimeLineTextView.setGravity(Gravity.CENTER);
        unreadTimeLineTextView.setText(UNREAD_TIMELINE_TEXT);
        unreadTimeLineTextView.setTextColor(DARK_TIMELINE_COLOR);
        unreadTimeLineTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        unreadTimeLineTextView.setTag(KaFontUtils.ROBOTO_MEDIUM);
        unreadTimeLineTextView.setCompoundDrawablePadding(5 * dp1);
      //  unreadTimeLineTextView.setCompoundDrawablesWithIntrinsicBounds(UNREAD_TIMELINE_DOWN_ARROW, null, UNREAD_TIMELINE_DOWN_ARROW, null);
        unreadTimeLineTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        unreadTimeLineTextView.setPadding(5 * dp1, 5 * dp1, 5 * dp1, 5 * dp1);
        KaFontUtils.applyCustomFont(getContext(),unreadTimeLineTextView);
        //Now add all view
        addView(unreadTimeLineTextView);

        if(!isInEditMode()) {
            KaFontUtils.applyCustomFont(context, this);
        }
    }

    public void setText(String text){
        unreadTimeLineTextView.setVisibility(StringUtils.isNullOrEmpty(text) ? GONE :VISIBLE);
        unreadTimeLineTextView.setText(text);
    }

    /**
     * START OF : UI change functions
     */

    /**
     * END OF : UI change functions
     */

    /**
     * START OF : Getter - Setter
     */

    /**
     * END OF : Getter - Setter
     *
     */

    private void customisePaint() {
        paint.setColor(0xff000000);
        paint.setStrokeWidth(dp1);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int left = getLeft() + getPaddingLeft();
        int right = getRight() - getPaddingRight();
        int top = getTop() + getPaddingTop();
        int down = getBottom() - getPaddingBottom();

        int parentWidth = right - left;
        int centerYCord = (down - top) /2;

        /*
         * DRAW THE TIMELINE ....
         * 1) Construct the path
         * 2) Define the respective colors and the positions for the gradient
         * 3) Create the Gradients...
         * 4) Draw both the lines...
         *
         * Following code,.. cannot seem to be optimised.... Try later... Fresh coordinates are required, always.. thus can't ignore 'em...
         */
        int leftSectionRightCord;
        leftSectionRightCord = unreadTimeLineTextView.getLeft();
        int rightSectionLeftCord;
        rightSectionLeftCord = unreadTimeLineTextView.getRight();

        positionLeft[0] = 0f;
        positionLeft[1] = (float)leftSectionRightCord/parentWidth;

        positionRight[0] = (float)rightSectionLeftCord/parentWidth;
        positionRight[1] = 1f;

        LinearGradient linearGradientLeft = new LinearGradient(left, centerYCord, leftSectionRightCord, centerYCord, colorLeft, positionLeft, Shader.TileMode.MIRROR);
        LinearGradient linearGradientRight = new LinearGradient(rightSectionLeftCord, centerYCord, right, centerYCord, colorRight, positionRight, Shader.TileMode.MIRROR);

        paint.setShader(linearGradientLeft);
        try {
            canvas.drawLine(left, centerYCord, leftSectionRightCord, centerYCord, paint);
        }catch (Exception e){
//            e.printStackTrace();
        }

        paint.setShader(linearGradientRight);
        try {
            canvas.drawLine(rightSectionLeftCord, centerYCord, right, centerYCord, paint);
        }catch (Exception e){
//            e.printStackTrace();
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getVisibility() == VISIBLE || getVisibility() == INVISIBLE) {
            int totalHeight = getPaddingTop();

            /*
             * For Row 1 TextView
             */
            MeasureUtils.measure(unreadTimeLineTextView);
            totalHeight += unreadTimeLineTextView.getMeasuredHeight();

            totalHeight += getPaddingBottom();

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec((int)AppControl.getInstance().getDimensionUtil().screenWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(unreadTimeLineTextView == null) return;
        //Consider the paddings and manupulate them it first..
        l += getPaddingLeft();
        t += getPaddingTop();
        r -= getPaddingRight();
        b -= getPaddingBottom();
        int parentWidth = getMeasuredWidth();

        /*
         * For Row 1 TextView
         */
        int top = getPaddingTop();
        int left = 0;
        LayoutUtils.layoutChildByCenteringItHorizontally(unreadTimeLineTextView, left, top, parentWidth);

    }

}
