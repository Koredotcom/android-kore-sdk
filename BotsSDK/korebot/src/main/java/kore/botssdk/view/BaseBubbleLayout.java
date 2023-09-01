package kore.botssdk.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BubbleConstants;
import kore.botssdk.view.viewUtils.BubbleViewUtil;
import kore.botssdk.view.viewUtils.DimensionUtil;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public abstract class BaseBubbleLayout extends ViewGroup {

    final Context context;
    Activity activityContext;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    protected float dp1, dp2, dp4, dp10, dp14, dp283, dp81, dp91, dp100, dp6, dp13, dp15, dp21,
            dp28, dp33, dp44, dp50, dp106, dp160, dp253, dp226;
    protected float screenWidth;
    protected int senderImageRadius, bubbleCornerRadius;
    private boolean leftSide;
    protected boolean isContinuousMessage = false;
    protected boolean isSeparatedClosely = false;
    protected boolean doDrawBubbleBackground = true;
    protected boolean isGroupMessage = true;
    private Point triangleCoordA, triangleCoordB, triangleCoordC;
    private Point lineStart, lineEnd;
    private int linkTextColor;

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
    protected int BUBBLE_CAROUSEL_BOTTOM_SHADE_MARGIN = 0;
    protected int BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = 0;
    protected int BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = 0;
    protected int BUBBLE_TOP_BORDER = 0;
    protected int BUBBLE_DOWN_BORDER = 0;
    protected int BUBBLE_LEFT_BORDER = 0;
    protected int BUBBLE_RIGHT_BORDER = 0;
    protected final int BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT = 0;
    protected int BUBBLE_CONTENT_LEFT_BORDER = 0; //TODO remove this...
    protected int BUBBLE_CONTENT_RIGHT_BORDER = 0; //TODO remove this...
    protected int BUBBLE_LEFT_PROFILE_PIC = 0;
    protected final int BUBBLE_SEPARATION_DISTANCE = 0;
    protected int BUBBLE_GROUPING_TIMELINE = 0;
    protected int BUBBLE_READ_RECEIPT = 0;

    Paint paint;

    protected final int WHITE_COLOR = 0xffffffff;
//    public static String NON_KORE_COLOR = "#AEBFC4";

    protected TextMediaLayout bubbleTextMediaLayout;
    protected TextView botContentTextView;
    protected HeaderLayout headerLayout;
    protected BotListTemplateView botListTemplateView;
    protected BotButtonView botButtonView;
    protected BotCarouselView botCarouselView;
    protected PieChartView botPieChartView;
//    protected CustomTableView tableView;
    protected LineChartView lineChartView;
    protected BarChartView barChartView;
    protected StackedBarChatView stackedBarChatView;
    protected KoraCarouselView miniTableView;


    protected int position;
    protected int[] dimens;
    protected int textColor;
    protected int textMediaLayoutGravity = BubbleConstants.GRAVITY_LEFT;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
            dp1 = DimensionUtil.dp1;
            dp4 = 4 * dp1;
            dp2 = 2 * dp1;
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
            screenWidth = AppControl.getInstance().getDimensionUtil().screenWidth;
        }
    }

    protected void setPaintColor(Paint paint) {

        if (isLeftSide()) {
            if (!isDoDrawBubbleBackground()) {
                paint.setColor(Color.parseColor(SDKConfiguration.BubbleColors.rightBubbleTextColor));
            } else if (isSelected()) {
                paint.setColor(Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleSelected));
            } else {
                paint.setColor(Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleUnSelected));
            }
        } else if (!isLeftSide()) {
            if (isSelected()) {
                paint.setColor(Color.parseColor(SDKConfiguration.BubbleColors.rightBubbleSelected));
            } else {
                paint.setColor(Color.parseColor(SDKConfiguration.BubbleColors.rightBubbleUnSelected));
            }
        }
    }


    abstract int getLinkTextColor();
    private void viewAddition() {
        ownLayoutInflater = LayoutInflater.from(context);

        // Bubble Text Media
        bubbleTextMediaLayout = new TextMediaLayout(context,getLinkTextColor());
        bubbleTextMediaLayout.setId(BubbleConstants.TEXT_MEDIA_LAYOUT_ID);
        bubbleTextMediaLayout.setRestrictedLayoutWidth(BubbleViewUtil.getBubbleContentWidth());
     //   bubbleTextMediaLayout.setRestrictedLayoutHeight(BubbleViewUtil.getBubbleContentHeight());
        bubbleTextMediaLayout.widthStyle = BubbleConstants.WRAP_CONTENT;
      /*  bubbleTextMediaLayout.setLinkTextColor(getLinkTextColor());*/
        addView(bubbleTextMediaLayout);

        botListTemplateView = new BotListTemplateView(getContext());
        botListTemplateView.setId(BubbleConstants.LIST_ID);
        botListTemplateView.setVisibility(View.GONE);
        addView(botListTemplateView);

        botButtonView = new BotButtonView(getContext());
        botButtonView.setId(BubbleConstants.BUTTON_VIEW_ID);
        botButtonView.setVisibility(View.GONE);
        addView(botButtonView);


        botCarouselView = new BotCarouselView(getContext());
        botCarouselView.setComposeFooterInterface(composeFooterInterface);
        botCarouselView.setVisibility(View.GONE);
        botCarouselView.setId(BubbleConstants.CAROUSEL_VIEW_ID);
        addView(botCarouselView);

        botPieChartView = new PieChartView(getContext());
        botPieChartView.setVisibility(View.GONE);
        botPieChartView.setId(BubbleConstants.PIECHART_VIEW_ID);
        addView(botPieChartView);

        /*tableView = new CustomTableView(getContext());
        tableView.setVisibility(View.GONE);
        tableView.setId(TextMediaLayout.TABLE_VIEW_ID);
        addView(tableView);*/

        lineChartView = new LineChartView(getContext());
        lineChartView.setVisibility(GONE);
        lineChartView.setId(BubbleConstants.LINECHART_VIEW_ID);
        addView(lineChartView);

        barChartView = new BarChartView(getContext());
        barChartView.setVisibility(GONE);
        barChartView.setId(BubbleConstants.BARCHART_VIEW_ID);
        addView(barChartView);

        stackedBarChatView = new StackedBarChatView(getContext());
        stackedBarChatView.setVisibility(GONE);
        stackedBarChatView.setId(BubbleConstants.STACK_BARCHAT_VIEW_ID);
        addView(stackedBarChatView);

        miniTableView = new KoraCarouselView(getContext());
        miniTableView.setVisibility(GONE);
        miniTableView.setId(BubbleConstants.MINI_TABLE_VIEW_ID);
        addView(miniTableView);

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
        int y = 0;
        if (botButtonView.getMeasuredHeight() > 0) {
            y = botButtonView.getBottom();
        } else if (botListTemplateView.getMeasuredHeight() > 0) {
            y = (int) (botListTemplateView.getBottom() + dp1);
        } else {
            y = bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        }
        y += 2 - senderImageRadius;

        if (isLeftSide()) {
            x = (int) (bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN - 6 * dp1 - senderImageRadius + dp1 / 3);
        } else {
            x = bubbleTextMediaLayout.getRight() + BUBBLE_CONTENT_RIGHT_MARGIN - senderImageRadius;
        }

        canvas.drawBitmap(curveBitmap, x, y, paint);
    }

    private final Paint curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
        if (isLeftSide()) {
            curveCanvas.drawCircle(BUBBLE_LEFT_BORDER + 4 * dp1 - dp1 / 3, 0, channelImageRadius, curvePaint);
        } else {
            int circularCurveX = (int) (channelImageRadius + bubbleRadius + 3 * dp1);
            curveCanvas.drawCircle(circularCurveX, 0, channelImageRadius, curvePaint);
        }

        return curveBitmap;
    }

    protected void drawBubbleBackground(Canvas canvas) {
        final RectF rect = new RectF();

        int[] dimen = textMediaDimen;
        int rectLeft = bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN;
        int rectTop = bubbleTextMediaLayout.getTop() - (BUBBLE_CONTENT_TOP_MARGIN);// + BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT);
        int rectBottom = 0;
        if (botButtonView.getMeasuredHeight() > 0) {
            rectBottom = botButtonView.getBottom();
        } else if (botListTemplateView.getMeasuredHeight() > 0) {
            rectBottom = (int) (botListTemplateView.getBottom() + dp1);
        } else {
            rectBottom = bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        }
        int rectRight = Collections.max(Arrays.asList(bubbleTextMediaLayout.getRight() + BUBBLE_CONTENT_RIGHT_MARGIN,
                botButtonView.getRight() + (int) dp1,
                botListTemplateView.getRight() + (int) dp1));

        rect.set(rectLeft, rectTop, rectRight, rectBottom);
        canvas.drawRoundRect(rect, (float) (1.5 * dp10), (float) (1.5 * dp10), paint);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headerLayout = findViewById(R.id.headerLayout);
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

        ComponentModel componentModel = getComponentModel(baseBotMessage);
        // Bubble Text Media
        populateBubbleTextMedia(position, baseBotMessage, componentModel, constrictLayout, dimens);
        // Bubble Templates
        populateForTemplates(position, componentModel);

        // Header Layout
        populateHeaderLayout(position, baseBotMessage);

        // 70% of UI-alignments happens here...
        cosmeticChanges(baseBotMessage, position);

    }

    private ComponentModel getComponentModel(BaseBotMessage baseBotMessage) {
        ComponentModel compModel = null;
        if (baseBotMessage instanceof BotResponse && !((BotResponse) baseBotMessage).getMessage().isEmpty()) {
            compModel = ((BotResponse) baseBotMessage).getMessage().get(0).getComponent();
        }
        return compModel;
    }

    protected void preCosmeticChanges() {
        setDoDrawBubbleBackground(true);
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
                textColor = context.getResources().getColor(R.color.left_bubble_text_color);
            } else {
                textColor = context.getResources().getColor(R.color.left_bubble_text_color);
            }
        } else {
            if (isLeftSide()) {
                textColor = context.getResources().getColor(R.color.left_bubble_text_color);
            } else if (!isLeftSide()) {
                textColor = context.getResources().getColor(R.color.left_bubble_text_color);
            }
        }
    }

    protected void populateForTemplates(int position, ComponentModel componentModel) {
    }

    protected void populateBubbleTextMedia(int position, BaseBotMessage baseBotMessage, ComponentModel componentModel, boolean constrictLayout, int... dimens) {

        String message = null;
        String textColor = "#000000";
        if (baseBotMessage.isSend()) {
            message = ((BotRequest) baseBotMessage).getMessage().getBody();
            bubbleTextMediaLayout.populateTextSenders(message);
        } else {
            BotResponseMessage msg = ((BotResponse) baseBotMessage).getTempMessage();
            if (componentModel != null) {
                String compType = componentModel.getType();
                PayloadOuter payOuter = componentModel.getPayload();
                if (BotResponse.COMPONENT_TYPE_TEXT.equalsIgnoreCase(compType)) {
                    message = payOuter.getText();
                } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType())) {
                    message = payOuter.getPayload().getText();
                    textColor = payOuter.getPayload().getColor();
                    if (botContentTextView != null) {
                        try {
                            botContentTextView.setTextColor(Color.parseColor(textColor));
                            botContentTextView.setText(message);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            bubbleTextMediaLayout.populateText(message);
        }


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
        textMediaDimen = new int[]{bubbleTextMediaLayout.getMeasuredWidth(), bubbleTextMediaLayout.getMeasuredHeight()};

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

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
        if (botCarouselView != null) {
            botCarouselView.setComposeFooterInterface(composeFooterInterface);
        }
        if (botButtonView != null) {
            botButtonView.setComposeFooterInterface(composeFooterInterface);
        }
        if (botListTemplateView != null) {
            botListTemplateView.setComposeFooterInterface(composeFooterInterface);
        }
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        if (botCarouselView != null) {
            botCarouselView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botButtonView != null) {
            botButtonView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botListTemplateView != null) {
            botListTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
        if (botCarouselView != null) {
            botCarouselView.setActivityContext(activityContext);
        }
    }

    public boolean isDoDrawBubbleBackground() {
        return doDrawBubbleBackground;
    }

    public void setDoDrawBubbleBackground(boolean doDrawBubbleBackground) {
        this.doDrawBubbleBackground = doDrawBubbleBackground;
    }


}
