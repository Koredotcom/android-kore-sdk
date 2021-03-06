package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.ViewProvider;
import kore.botssdk.view.viewUtils.BubbleViewUtil;


/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public abstract class KaBaseBubbleLayout extends ViewGroup {

    Context context;
    Activity activityContext;
    ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    protected float dp1, dp2, dp4, dp10,dp12, dp14, dp283, dp81, dp91, dp100, dp6, dp13, dp15, dp21,
            dp28, dp33, dp44, dp50, dp106, dp160, dp253, dp226;
    protected float screenWidth;
    protected int senderImageRadius, bubbleCornerRadius;
    private boolean leftSide;
    protected boolean isContinuousMessage = false;
    protected boolean isSeparatedClosely = false;
    protected boolean doDrawBubbleBackground = true;
    protected boolean isGroupMessage = false;
    private Point triangleCoordA, triangleCoordB, triangleCoordC;
    private Point lineStart, lineEnd;
    private int linkTextColor;

    protected int[] textMediaDimen;
    protected int[] maxBubbleDimen;
  //  protected int[] headerLayoutDimen;
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
    protected int BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT = 0;
    protected int BUBBLE_CONTENT_LEFT_BORDER = 0; //TODO remove this...
    protected int BUBBLE_CONTENT_RIGHT_BORDER = 0; //TODO remove this...
    protected int BUBBLE_LEFT_PROFILE_PIC = 0;
    protected int BUBBLE_SEPARATION_DISTANCE = 0;
    protected int BUBBLE_GROUPING_TIMELINE = 0;
    protected int BUBBLE_READ_RECEIPT = 0;

    Paint paint;

    protected int RIGHT_COLOR_SELECTED = Color.parseColor(SDKConfiguration.BubbleColors.rightBubbleSelected);
    protected int RIGHT_COLOR_UNSELECTED = Color.parseColor(SDKConfiguration.BubbleColors.rightBubbleUnSelected);
    protected int LEFT_COLOR_SELECTED = Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleSelected);
    protected int LEFT_COLOR_UNSELECTED =  Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleUnSelected);
    protected int BUBBLE_WHITE_COLOR =  Color.parseColor(SDKConfiguration.BubbleColors.whiteColor);
    protected int LEFT_BUBBLE_BORDER_COLOR = Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleBorderColor);
    protected int LEFT_BUBBLE_LINK_COLOR =  Color.parseColor(SDKConfiguration.BubbleColors.leftLinkColor);
    protected int RIGHT_BUBBLE_LINK_COLOR = Color.parseColor(SDKConfiguration.BubbleColors.rightLinkColor);

    protected int LEFT_TEXT_COLOR =  Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleTextColor);
    protected int RIGHT_TEXT_COLOR = Color.parseColor(SDKConfiguration.BubbleColors.rightBubbleTextColor);

    /*
    protected int LEFT_GRADIENT_START = getResources().getColor(R.color.gradient_left_start);
    protected int LEFT_GRADIENT_END = getResources().getColor(R.color.gradient_left_end);
    protected int LEFT_GRADIENT_MIDDLE = getResources().getColor(R.color.gradient_left_middle);

    protected int RIGHT_GRADIENT_START =getResources().getColor(R.color.gradient_right_start);
    protected int RIGHT_GRADIENT_END = getResources().getColor(R.color.gradient_right_end);
    protected int RIGHT_GRADIENT_MIDDLE = getResources().getColor(R.color.gradient_right_middle);

*/


    protected int WHITE_COLOR = 0xffffffff;
    public static String NON_KORE_COLOR = "#AEBFC4";

    protected TextMediaLayout bubbleTextMediaLayout;
    protected TextView botContentTextView;
   // protected HeaderLayout headerLayout;
    protected BotListTemplateView botListTemplateView;
    protected BotButtonView botButtonView;
    protected BotCarouselView botCarouselView;
    protected PieChartView botPieChartView;
    protected BotMainTableView tableView;
    protected LineChartView lineChartView;
    protected KoraCarouselView koraCarouselView;
    protected MeetingSlotsView meetingSlotsView;
    protected MeetingConfirmationView meetingConfirmationView;
    protected CalendarEventsTemplateView calendarEventsView;
    protected KoraFilesCarousalView filesCarousalView;
    protected AttendeeSlotSelectionView attendeeSlotSelectionView;




    protected int[] dimens;
    protected int textColor;
    protected int textMediaLayoutGravity = TextMediaLayout.GRAVITY_LEFT;
    protected GradientDrawable leftGradientDrawable,rightGradientDrawable;

    LayoutInflater ownLayoutInflater;
    protected TextView timeStampsTextView;
    protected TimeLineTextView timeLineView;


    public KaBaseBubbleLayout(Context context) {
        super(context);
        this.context = getContext();
        init();
    }

    public KaBaseBubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = getContext();
        init();
    }

    public KaBaseBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = getContext();
        init();
    }

    @SuppressLint("NewApi")
    public KaBaseBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            dp2 = 2 * dp1;
            dp6 = 6 * dp1;
            dp10 = 10 * dp1;
            dp12 = 12 * dp1;
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
            BUBBLE_CONTENT_LEFT_MARGIN = (int) dp12;
            BUBBLE_CONTENT_TOP_MARGIN = (int) (8 * dp1);
            BUBBLE_CONTENT_RIGHT_MARGIN = (int) dp12;
            BUBBLE_CONTENT_BOTTOM_MARGIN = (int) (8 * dp1);
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
                paint.setColor(BUBBLE_WHITE_COLOR);
            } else if (isSelected()) {
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


    private GradientDrawable getGradientBubble( ){
        if(leftGradientDrawable == null) {
            leftGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{LEFT_COLOR_UNSELECTED,LEFT_COLOR_UNSELECTED});
            leftGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            leftGradientDrawable.setGradientRadius((float) (Math.sqrt(2) * 60));
            leftGradientDrawable.setStroke((int)dp1,LEFT_BUBBLE_BORDER_COLOR);
            leftGradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            leftGradientDrawable.setCornerRadii(new float[]{dp12, dp12, dp12, dp12, isLeftSide() ? dp12 : dp1, isLeftSide() ? dp12 : dp1, !isLeftSide() ? dp12 : dp1, !isLeftSide() ? dp12 : dp1});
        }
        if(rightGradientDrawable == null) {
            rightGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{RIGHT_COLOR_UNSELECTED,RIGHT_COLOR_UNSELECTED});


            rightGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            rightGradientDrawable.setGradientRadius((float) (Math.sqrt(2) * 60));
            rightGradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            rightGradientDrawable.setCornerRadii(new float[]{dp12, dp12, dp12, dp12, isLeftSide() ? dp12 : dp1, isLeftSide() ? dp12 : dp1, !isLeftSide() ? dp12 : dp1, !isLeftSide() ? dp12 : dp1});
        }

        return  isLeftSide() ? leftGradientDrawable : rightGradientDrawable;
    }

    abstract int getLinkTextColor();
    private void viewAddition() {
        ownLayoutInflater = LayoutInflater.from(context);

        bubbleTextMediaLayout = ViewProvider.getTextMediaLayout(context,getLinkTextColor());
        bubbleTextMediaLayout.setRestrictedLayoutWidth(BubbleViewUtil.getBubbleContentWidth());
        bubbleTextMediaLayout.setRestrictedLayoutHeight(BubbleViewUtil.getBubbleContentHeight());
        bubbleTextMediaLayout.widthStyle = TextMediaLayout.WRAP_CONTENT;
        bubbleTextMediaLayout.gravity = textMediaLayoutGravity;
        addView(bubbleTextMediaLayout);

        botButtonView = ViewProvider.getBotButtonView(context,null,null);
        addView(botButtonView);

        meetingSlotsView = ViewProvider.getMeetingSlotsView(context);
        meetingSlotsView.setComposeFooterInterface(composeFooterInterface);
        meetingSlotsView.setRestrictedLayoutWidth(BubbleViewUtil.getSlotsContentWidth());
        addView(meetingSlotsView);

        meetingConfirmationView = ViewProvider.getMeetingConfirmationView(context);
        meetingConfirmationView.setComposeFooterInterface(composeFooterInterface);
        meetingConfirmationView.setRestrictedLayoutWidth(BubbleViewUtil.getSlotConfirmationWidth());
        addView(meetingConfirmationView);

        attendeeSlotSelectionView = ViewProvider.getAttendeeSlotSelectionView(context);
        attendeeSlotSelectionView.setComposeFooterInterface(composeFooterInterface);
        attendeeSlotSelectionView.setRestrictedLayoutWidth(BubbleViewUtil.getSlotConfirmationWidth());
        addView(attendeeSlotSelectionView);

        botListTemplateView = ViewProvider.getBotListTempleteView(context);
        addView(botListTemplateView);

        botCarouselView = ViewProvider.getBotCarousalView(context);
        botCarouselView.setComposeFooterInterface(composeFooterInterface);
        addView(botCarouselView);

        koraCarouselView =  ViewProvider.getKoraCarouselView(context);
        koraCarouselView.setComposeFooterInterface(composeFooterInterface);
        addView(koraCarouselView);



        botPieChartView = ViewProvider.getPieChartView(context);
        addView(botPieChartView);

        tableView = ViewProvider.getTableView(context);
        addView(tableView);

        lineChartView = ViewProvider.getLineChartView(context);
        addView(lineChartView);

        calendarEventsView = ViewProvider.getCalenderTemplateView(context);
        addView(calendarEventsView);

        filesCarousalView = ViewProvider.getKoraFilesCarouselView(context);
        addView(filesCarousalView);

        timeStampsTextView = ViewProvider.getTimeStampTextView(context);
        addView(timeStampsTextView);
        timeStampsTextView.setVisibility(SDKConfiguration.isTimeStampsRequired() ? VISIBLE : GONE);

        timeLineView = ViewProvider.getTimeLineView(context);
        addView(timeLineView);

    }



    abstract void initializeBubbleBorderPass1();

    abstract void initializeBubbleBorderPass2();

    @Override
    protected void onDraw(Canvas canvas) {

        //First clear everything
        clearCanvas(canvas);

        //Set the Paint
        setPaintStroke(paint, !isSelected());
/*

        //Draw Arrow
        if (isContinuousMessage() && isSeparatedClosely()) {
        } else {
            drawCurve(canvas);
        }
*/

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

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
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
        if(koraCarouselView != null){
            koraCarouselView.setComposeFooterInterface(composeFooterInterface);
        }
        if(meetingSlotsView != null){
            meetingSlotsView.setComposeFooterInterface(composeFooterInterface);
        }
        if(meetingConfirmationView != null){
            meetingConfirmationView.setComposeFooterInterface(composeFooterInterface);
        }
        if(attendeeSlotSelectionView != null){
            attendeeSlotSelectionView.setComposeFooterInterface(composeFooterInterface);
        }
    }
    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        if (botCarouselView != null) {
            botCarouselView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (koraCarouselView != null) {
            koraCarouselView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botButtonView != null) {
            botButtonView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botListTemplateView != null) {
            botListTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
    }

    protected void drawBubbleBackground(Canvas canvas) {
        int rectLeft = bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN;
        int rectTop = bubbleTextMediaLayout.getTop() - (BUBBLE_CONTENT_TOP_MARGIN);// + BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT);
        int rectBottom = 0;
        if (botButtonView.getMeasuredHeight() > 0) {
            rectBottom = (int)(botButtonView.getBottom()+ dp1);
        }else if(meetingSlotsView.getMeasuredHeight() > 0){
            rectBottom = (int) (meetingSlotsView.getBottom() + dp1);
        } else if(meetingConfirmationView.getMeasuredHeight() > 0){
            rectBottom = (meetingConfirmationView.getBottom());
        }else if(calendarEventsView.getMeasuredHeight() > 0 && calendarEventsView.getVisibility() != ViewGroup.GONE){
            rectBottom = (int) (calendarEventsView.getBottom() + dp1);
        }else if(attendeeSlotSelectionView.getMeasuredHeight() > 0){
            rectBottom = (int) (attendeeSlotSelectionView.getBottom() + dp1);
        }else {
            rectBottom = bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        }
        int rectRight = Collections.max(Arrays.asList(bubbleTextMediaLayout.getRight() + BUBBLE_CONTENT_RIGHT_MARGIN,
                botButtonView.getRight(),
                meetingSlotsView.getRight()+ (int)dp2,attendeeSlotSelectionView.getRight()+ (int)dp12,meetingConfirmationView.getRight(),calendarEventsView.getRight()));

        GradientDrawable gradientDrawable = getGradientBubble();
        gradientDrawable.setBounds(new Rect(rectLeft,rectTop,rectRight,rectBottom));
        gradientDrawable.draw(canvas);

       // ViewProvider.drawRoundRect(canvas, new RectF(rectLeft,rectTop,rectRight,rectBottom), paint,(int)(dp12),(int)(dp12),(int)(!isLeftSide() ?  dp12 : dp1),(int)(isLeftSide() ?  dp12 : dp1));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void fillBubbleLayout(int position,boolean isLastItem, BaseBotMessage baseBotMessage,
                                 boolean constrictLayout,int... dimens) {


        this.dimens = dimens;

        // Customize BubbleSeparation
        setSeparatedClosely(true);
        // Customise BubbleTimeLineGrouping Height
        BUBBLE_GROUPING_TIMELINE = 0;

        preCosmeticChanges();

        ComponentModel componentModel = getComponentModel(baseBotMessage);
        // Bubble Text Media
        populateBubbleTextMedia(baseBotMessage, componentModel, constrictLayout, dimens);
        timeStampsTextView.setText(DateUtils.getTimeInAmPm(baseBotMessage.getCreatedInMillis()));
        // Bubble Templates
        populateForTemplates(position,isLastItem,componentModel,baseBotMessage);

       // timeLineView.setGravity(Gravity.CENTER);
        timeStampsTextView.setGravity(isLeftSide() ? Gravity.LEFT : Gravity.RIGHT);
        // Header Layout
       /* populateHeaderLayout(position, baseBotMessage);*/

        // 70% of UI-alignments happens here...
        cosmeticChanges(baseBotMessage);

    }

    private ComponentModel getComponentModel(BaseBotMessage baseBotMessage) {
        ComponentModel compModel = null;
        if (baseBotMessage instanceof BotResponse && ((BotResponse) baseBotMessage).getMessage() != null && !((BotResponse) baseBotMessage).getMessage().isEmpty()) {
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
    protected void cosmeticChanges(BaseBotMessage baseBotMessage) {
    }

    protected void determineTextColor() {
        if (isLeftSide()) {
            if (isSelected()) {
                textColor = LEFT_TEXT_COLOR;
            } else {
                textColor = LEFT_TEXT_COLOR;
            }
        } else {
            if (isSelected()) {
                textColor = RIGHT_TEXT_COLOR;
            } else {
                textColor = RIGHT_TEXT_COLOR;
            }
        }
    }

    protected void populateForTemplates(int position,boolean isLastItem,ComponentModel componentModel,BaseBotMessage baseBotMessage) {
    }

    protected void populateBubbleTextMedia(BaseBotMessage baseBotMessage, ComponentModel componentModel, boolean constrictLayout, int... dimens) {

        String message = null;
        String textColor = "#000000";
        if (baseBotMessage.isSend()) {
            message = ((BotRequest) baseBotMessage).getMessage().getBody();
        } else {
            BotResponseMessage msg = ((BotResponse) baseBotMessage).getTempMessage();
            if (componentModel != null) {
                String compType = componentModel.getType();
                PayloadOuter payOuter = componentModel.getPayload();
                message = payOuter.getText();
                if (BotResponse.COMPONENT_TYPE_TEXT.equalsIgnoreCase(compType)) {
                    message = payOuter.getText();
                } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType())) {
                    message = payOuter.getPayload().getText();
                    textColor = payOuter.getPayload().getColor();
                    if (botContentTextView != null) {
                        try {
                            botContentTextView.setTextColor(Color.parseColor(textColor));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        bubbleTextMediaLayout.startup(message, dimens);

    }

  /*  abstract protected void populateHeaderLayout(int position, BaseBotMessage baseBotMessage);*/

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

        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN  + textMediaDimen[1]+BUBBLE_CONTENT_BOTTOM_MARGIN;
     //   headerLayoutDimen = new int[2];
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


    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
        if (botCarouselView != null) {
            botCarouselView.setActivityContext(activityContext);
        }
        if (koraCarouselView != null) {
            koraCarouselView.setActivityContext(activityContext);
        }
        if(filesCarousalView != null){
            filesCarousalView.setActivityContext(activityContext);
        }
    }

    public boolean isDoDrawBubbleBackground() {
        return doDrawBubbleBackground;
    }

    public void setDoDrawBubbleBackground(boolean doDrawBubbleBackground) {
        this.doDrawBubbleBackground = doDrawBubbleBackground;
    }


}
