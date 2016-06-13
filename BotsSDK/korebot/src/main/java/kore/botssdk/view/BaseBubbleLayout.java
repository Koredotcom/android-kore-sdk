package kore.botssdk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.view.viewUtils.BubbleViewUtil;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public abstract class BaseBubbleLayout extends ViewGroup {

    Context context;
    protected float dp1, dp4, dp10, dp14, dp283, dp81, dp91, dp100, dp6, dp13, dp15, dp21,
            dp28, dp33, dp44, dp50, dp106, dp160, dp253, dp226;
    protected int senderImageRadius, bubbleCornerRadius;
    private boolean leftSide;
    protected boolean isContinuousMessage = false;
    protected boolean isSeparatedClosely = false;
    protected boolean isGroupMessage = true;
    private Point triangleCoordA, triangleCoordB, triangleCoordC;
    private Point lineStart, lineEnd;

    protected int[] textMediaDimen;
    protected int[] maxBubbleDimen;
    protected int[] headerLayoutDimen;
    protected int[] maxContentDimen;

    protected int BUBBLE_ARROW_WIDTH = 0;
    protected int BUBBLE_RIGHT_ARROW_WIDTH = 0;
    protected int BUBBLE_LEFT_ARROW_WIDTH = 0;
    protected int BUBBLE_ARROW_TOP_Y = 0;
    protected int BUBBLE_ARROW_MIDDLE_Y = 0;
    protected int BUBBLE_ARROW_END_Y = 0;
    protected int BUBBLE_CONTENT_LEFT_MARGIN = 0;
    protected int BUBBLE_CONTENT_TOP_MARGIN = 0;
    protected int BUBBLE_CONTENT_RIGHT_MARGIN = 0;
    protected int BUBBLE_CONTENT_BOTTOM_MARGIN = 0;
    protected int BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = 0;
    protected int BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = 0;
    protected int BUBBLE_TOP_BORDER = 0;
    protected int BUBBLE_DOWN_BORDER = 0;
    protected int BUBBLE_LEFT_BORDER = 0;
    protected int BUBBLE_RIGHT_BORDER = 0;
    protected int BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT = 0;
    protected int BUBBLE_CONTENT_LEFT_BORDER = 0; //TODO remove this...
    protected int BUBBLE_CONTENT_RIGHT_BORDER = 0; //TODO remove this...
    protected int BUBBLE_LEFT_PROFILE_PIC = 0;
    protected int BUBBLE_SEPARATION_DISTANCE = 0;
    protected int BUBBLE_GROUPING_TIMELINE = 0;
    protected int BUBBLE_READ_RECEIPT = 0;

    Paint paint;
    protected int RIGHT_COLOR_SELECTED = getResources().getColor(R.color.right_bubble_selected);
    protected int RIGHT_COLOR_UNSELECTED = getResources().getColor(R.color.right_bubble_unselected);
    protected int LEFT_COLOR_SELECTED = getResources().getColor(R.color.left_bubble_selected);
    protected int LEFT_COLOR_UNSELECTED = getResources().getColor(R.color.left_bubble_unselected);
    protected int POLICY_BUBBLE_COLOR = getResources().getColor(R.color.policy_bubble_color);
    protected int WHITE_COLOR = 0xffffffff;
    public static String NON_KORE_COLOR = "#AEBFC4";

    protected TextMediaLayout bubbleTextMediaLayout;
    protected TextView botContentTextView;
    protected HeaderLayout headerLayout;
    protected int position;
    protected int[] dimens;
    protected int textColor;
    protected int textMediaLayoutGravity = TextMediaLayout.GRAVITY_LEFT;

    LayoutInflater ownLayoutInflater;

    public BaseBubbleLayout(Context context) {
        super(context);
        this.context = getContext();
        init();
    }

    public BaseBubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = getContext();
        init();
    }

    public BaseBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = getContext();
        init();
    }

    public BaseBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = getContext();
        init();
    }

    private void init() {
        initiliazeCoordinates();
        setWillNotDraw(false);
        paint = new Paint();
        paint.setXfermode(null);
        setPaintColor(paint);
        paint.setAntiAlias(true);
        viewAddition();
    }

    private void initiliazeCoordinates() {
        if (AppControl.getInstance() != null
                && AppControl.getInstance().getDimensionUtil() != null) {
            dp1 = AppControl.getInstance().getDimensionUtil().dp1;
            dp4 = 4 * dp1;
            dp6 = 6 * dp1;
            dp10 = 10 * dp1;
            dp13 = 13 * dp1;
            dp14 = 14 * dp1;
            dp15 = 15 * dp1;
            dp21 = 21 * dp1;
            dp28 = 28 * dp1;
            dp33 = 33 * dp1;
            dp44 = 44 * dp1;
            dp50 = 50 * dp1;
            dp81 = 81 * dp1;
            dp91 = 91 * dp1;
            dp100 = 100 * dp1;
            dp106 = 106 * dp1;
            dp160 = 160 * dp1;
            dp226 = 226 * dp1;
            dp253 = 253 * dp1;
            dp283 = 283 * dp1;
            BUBBLE_READ_RECEIPT = (int) (dp1 * 7);
            BUBBLE_ARROW_WIDTH = (int) dp1;
            BUBBLE_CONTENT_LEFT_MARGIN = (int) dp14;
            BUBBLE_CONTENT_TOP_MARGIN = (int) dp14;
            BUBBLE_CONTENT_RIGHT_MARGIN = (int) dp14;
            BUBBLE_CONTENT_BOTTOM_MARGIN = (int) dp14;
            senderImageRadius = (int) (dp1 * 17); // Change this value if sender image width and height is changed
            bubbleCornerRadius = (int) dp15;
            float arrow_factor = 1f;//(float) (1 / Math.sqrt(3));
            BUBBLE_ARROW_TOP_Y = 0;
            BUBBLE_ARROW_MIDDLE_Y = (int) (arrow_factor * BUBBLE_ARROW_WIDTH);
            BUBBLE_ARROW_END_Y = (int) (2 * arrow_factor * BUBBLE_ARROW_WIDTH);
        }
    }

    protected void setPaintColor(Paint paint) {

        if (isLeftSide()) {
            if (isSelected()) {
                paint.setColor(LEFT_COLOR_SELECTED);
            } else {
                paint.setColor(LEFT_COLOR_UNSELECTED);
            }
        } else if (!isLeftSide()) {
            if (isSelected()) {
                paint.setColor(RIGHT_COLOR_SELECTED);
            } else {
                paint.setColor(RIGHT_COLOR_UNSELECTED);
            }
        }
    }


    private void viewAddition() {
        ownLayoutInflater = LayoutInflater.from(context);

        // Bubble Text Media
        bubbleTextMediaLayout = new TextMediaLayout(context);

        bubbleTextMediaLayout.setId(TextMediaLayout.TEXT_MEDIA_LAYOUT_ID);
        bubbleTextMediaLayout.setRestrictedLayoutWidth(BubbleViewUtil.getBubbleContentWidth());
        bubbleTextMediaLayout.setRestrictedLayoutHeight(BubbleViewUtil.getBubbleContentHeight());
        bubbleTextMediaLayout.widthStyle = TextMediaLayout.WRAP_CONTENT;
        bubbleTextMediaLayout.gravity = textMediaLayoutGravity;
        addView(bubbleTextMediaLayout);

    }

    protected void setInivisiblePaintColor(Paint paint) {
        paint.setColor(0x00000000);
    }

    protected void initiliazePathCoordinates(boolean isLeft) {

        if (triangleCoordA == null || triangleCoordB == null || triangleCoordC == null) {
            triangleCoordA = new Point(0, 0);
            triangleCoordB = new Point(0, 0);
            triangleCoordC = new Point(0, 0);
        }

        if (isLeft) {
            triangleCoordA.x = bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN;
            triangleCoordA.y = bubbleTextMediaLayout.getTop() + BUBBLE_ARROW_TOP_Y;
            triangleCoordB.x = triangleCoordA.x - BUBBLE_LEFT_ARROW_WIDTH;
            triangleCoordB.y = triangleCoordA.y + BUBBLE_ARROW_MIDDLE_Y;
            triangleCoordC.x = triangleCoordA.x;
            triangleCoordC.y = triangleCoordA.y + BUBBLE_ARROW_END_Y;
        } else {
            triangleCoordA.x = bubbleTextMediaLayout.getRight() + BUBBLE_CONTENT_RIGHT_MARGIN;
            triangleCoordA.y = bubbleTextMediaLayout.getTop() + BUBBLE_ARROW_TOP_Y;
            triangleCoordB.x = triangleCoordA.x + BUBBLE_RIGHT_ARROW_WIDTH;
            triangleCoordB.y = triangleCoordA.y + BUBBLE_ARROW_MIDDLE_Y;
            triangleCoordC.x = triangleCoordA.x;
            triangleCoordC.y = triangleCoordA.y + BUBBLE_ARROW_END_Y;
        }
    }

    private void initializeLineCoordinates() {
        lineStart = new Point(triangleCoordA.x, (int) (triangleCoordA.y + dp1));
        lineEnd = new Point(triangleCoordC.x, (int) (triangleCoordC.y - dp1));
    }

    abstract void initializeBubbleBorderPass1();
    abstract void initializeBubbleBorderPass2();

    @Override
    protected void onDraw(Canvas canvas) {

        //First clear everything
        clearCanvas(canvas);

        //Set the Paint
        setPaintStroke(paint, !isSelected());

        //Draw Arrow
        if (isContinuousMessage() && isSeparatedClosely()) {
        } else {
            drawCurve(canvas);
        }

        //Draw Rectangle
        drawBubbleBackground(canvas);
    }

    private void clearCanvas(Canvas canvas) {
        paint.setColor(WHITE_COLOR);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        final RectF rect = new RectF();
        rect.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(rect, paint);
    }

    protected void setPaintStroke(Paint paint, boolean forBorder) {
        setPaintColor(paint);
        paint.setStrokeWidth(dp1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    protected void drawCurve(Canvas canvas) {
        Bitmap curveBitmap = formCurveBitmap(senderImageRadius, bubbleCornerRadius);

        int x = 0;
        int y = bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN + 2 - senderImageRadius;

        if(isLeftSide()) {
            x = (int) (bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN - 6 * dp1 - senderImageRadius + dp1/3);
        } else {
            x = bubbleTextMediaLayout.getRight() + BUBBLE_CONTENT_RIGHT_MARGIN - senderImageRadius;
        }

        canvas.drawBitmap(curveBitmap, x, y, paint);
    }

    private Paint curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap formCurveBitmap(int channelImageRadius, int bubbleRadius) {

        Bitmap curveBitmap = Bitmap.createBitmap((int) (channelImageRadius + bubbleRadius + 3 * dp1), channelImageRadius, Bitmap.Config.ARGB_8888);
        Canvas curveCanvas = new Canvas(curveBitmap);

        curvePaint.setXfermode(null);
        setPaintColor(curvePaint);
        curvePaint.setStrokeWidth(dp1);
        curvePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        curveCanvas.drawRect(BUBBLE_LEFT_BORDER, 0, BUBBLE_LEFT_BORDER + channelImageRadius + bubbleRadius + 3 * dp1, channelImageRadius, curvePaint);

        curvePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        curvePaint.setColor(Color.WHITE);
        if(isLeftSide()) {
            curveCanvas.drawCircle(BUBBLE_LEFT_BORDER + 4 * dp1 - dp1/3, 0, channelImageRadius, curvePaint);
        } else {
            int circularCurveX = (int) ((isGroupMessage) ? channelImageRadius + bubbleRadius + 3 * dp1 : channelImageRadius + bubbleRadius + 3*dp1);
            curveCanvas.drawCircle(circularCurveX, 0, channelImageRadius, curvePaint);
        }

        return curveBitmap;
    }

    protected void drawBubbleBackground(Canvas canvas) {
        final RectF rect = new RectF();

        int dimen[] = textMediaDimen;
        int rectLeft = bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN;
        int rectTop = bubbleTextMediaLayout.getTop() - (BUBBLE_CONTENT_TOP_MARGIN);// + BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT);
        int rectBottom = bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        int rectRight = bubbleTextMediaLayout.getRight() + BUBBLE_CONTENT_RIGHT_MARGIN;

        rect.set(rectLeft, rectTop, rectRight, rectBottom);
        canvas.drawRoundRect(rect, (float) (1.5 * dp10), (float) (1.5 * dp10), paint);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
    }

    public void fillBubbleLayout(int position, BaseBotMessage baseBotMessage,
                                 boolean constrictLayout, int... dimens) {

        this.position = position;
        this.dimens = dimens;

        // Customize BubbleSeparation
        setSeparatedClosely(true);
        // Customise BubbleTimeLineGrouping Height
        BUBBLE_GROUPING_TIMELINE = 0;

        preCosmeticChanges();

        // Bubble Text Media
        populateBubbleTextMedia(position, baseBotMessage, constrictLayout, dimens);

        // Header Layout
        populateHeaderLayout(position, baseBotMessage);

        // 70% of UI-alignments happens here...
        cosmeticChanges(baseBotMessage, position);

    }

    protected void preCosmeticChanges() {
        determineTextColor();
        textViewCosmeticChanges();
    }

    /**
     * UI changes
     */
    protected void cosmeticChanges(BaseBotMessage baseBotMessage, int position) {
    }

    protected void determineTextColor() {
        if (isLeftSide()) {
            if (isSelected()) {
                textColor = context.getResources().getColor(R.color.bubble_light_text_color);
            } else {
                textColor = context.getResources().getColor(R.color.bubble_dark_text_color);
            }
        } else {
            if (isLeftSide()) {
                textColor = context.getResources().getColor(R.color.left_bubble_text_color);
            } else if (!isLeftSide()) {
                textColor = context.getResources().getColor(R.color.right_bubble_text_color);
            }
        }
    }

    protected void populateBubbleTextMedia(int position, BaseBotMessage baseBotMessage, boolean constrictLayout, int... dimens) {

        String message;
        if (baseBotMessage.isSend()) {
            message = ((BotRequest) baseBotMessage).getMessage().getBody();
        } else {
            message = ((BotResponse) baseBotMessage).getTempMessage().getcInfo().getBody();
        }

        bubbleTextMediaLayout.startup(position, message, dimens);
    }

    abstract protected void populateHeaderLayout(int position, BaseBotMessage baseBotMessage);

    protected void textViewCosmeticChanges() {

        botContentTextView = bubbleTextMediaLayout.getBotContentTextView();
        if (botContentTextView != null) {
            botContentTextView.setTextColor(textColor);
        }
    }

    /**
     * Function to extract bubble dimens... basically textMediaLayout's and FooterLayout's
     */
    protected void initializeBubbleContentDimen() {
        //STEP 1: Retrieve TextMedia Layout dimensional value... and also FooterLayoutDimentionalValue
        textMediaDimen =  new int[]{bubbleTextMediaLayout.getMeasuredWidth(), bubbleTextMediaLayout.getMeasuredHeight()} ;//bubbleTextMediaLayout.getTextMediaLayoutDimens(bubbleMeta.getComponentMeta(), dimens);

        //STEP 2: Store additional informations required in further stage of UI rendering...
        maxBubbleDimen = new int[2];
        maxContentDimen = new int[2];
        headerLayoutDimen = new int[2];
    }

    /**
     * Initialises variables for Paint constructions.
     * This method should be called only after views before layed out
     */
    protected void initializeBubbleDimensionalParametersPhase1() {
        // Initialze Bubble Border
        initializeBubbleBorderPass1();
        // Initiliaze bubbleContent Dimens [ NOTE: MUST BE DONE AFTER VIEW POPULATION ]
        initializeBubbleContentDimen();
    }

    /**
     * Initialises variables for Paint constructions.
     * This method should be called only after views after layed out
     */
    protected void initializeBubbleDimensionalParametersPhase2() {
        // Initialze Bubble Border
        initializeBubbleBorderPass2();
    }

    /*
    * GETTER - SETTER
    */

    public void setLeftSide(boolean leftSide) {
        this.leftSide = leftSide;
    }

    public boolean isLeftSide() {
        return leftSide;
    }

    public boolean isContinuousMessage() {
        return isContinuousMessage;
    }

    public void setContinuousMessage(boolean isContinuousMessage) {
        this.isContinuousMessage = isContinuousMessage;
    }

    public boolean isSeparatedClosely() {
        return isSeparatedClosely;
    }

    public void setSeparatedClosely(boolean isSeparatedClosely) {
        this.isSeparatedClosely = isSeparatedClosely;
    }

    public void setGroupMessage(boolean groupMessage) {
        isGroupMessage = groupMessage;
    }
}
