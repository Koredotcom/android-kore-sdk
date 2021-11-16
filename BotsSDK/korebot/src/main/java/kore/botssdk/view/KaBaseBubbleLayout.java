package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BubbleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.ViewProvider;
import kore.botssdk.view.viewUtils.BubbleViewUtil;
import kore.botssdk.view.viewUtils.DimensionUtil;

import static kore.botssdk.net.SDKConfiguration.BubbleColors.BubbleUI;


/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public abstract class KaBaseBubbleLayout extends ViewGroup {
    Context context;
    Activity activityContext;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    protected float dp1, dp2, dp4, dp10,dp12, dp14, dp283, dp81, dp91, dp100, dp6, dp13, dp15, dp21,
            dp28, dp33, dp44, dp50, dp106, dp160, dp253, dp226;
    protected float screenWidth;
    protected int senderImageRadius, bubbleCornerRadius;
    private boolean leftSide;
    protected boolean isContinuousMessage = false;
    protected boolean doDrawBubbleBackground = false;
    protected boolean isGroupMessage = false;
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
    protected int BUBBLE_CONTENT_RIGHT_LIST_MARGIN = 50;

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




    protected int WHITE_COLOR = 0xffffffff;
//    public static String NON_KORE_COLOR = "#AEBFC4";

    protected TextMediaLayout bubbleTextMediaLayout;
    protected TextView botContentTextView;
    // protected HeaderLayout headerLayout;
    protected BotListTemplateView botListTemplateView;
    protected BotButtonView botButtonView;
    protected BotCarouselView botCarouselView;
    protected PieChartView botPieChartView;
    protected BotTableView tableView;
    protected BotResponsiveExpandTableView responsiveExpandTableView;
    protected BotResponsiveTableView responsiveTableView;
    protected LineChartView lineChartView;
    protected BarChartView barChartView;
    protected StackedBarChatView stackedBarChatView;
    protected ContactInfoView contactInfoView;
    protected WelcomeSummaryView welcomeSummaryView;
    protected UniversalSearchView universalSearchView;
    protected KoraSummaryHelpView koraSummaryHelpView;
    protected KoraCarouselView koraCarouselView;
    protected MeetingSlotsView meetingSlotsView;
    protected MultiSelectView multiSelectView;
    protected MeetingConfirmationView meetingConfirmationView;
    protected VerticalListView verticalListView;
    protected AttendeeSlotSelectionView attendeeSlotSelectionView;

    //Added by Sudheer
    protected HorizontalBarChartView horizontalBarChartView;
    protected BotFormTemplateView botFormTemplateView;
    protected BotListViewTemplateView botListViewTemplateView;
    protected BotTableListTemplateView botTableListTemplateView;
    protected BotQuickRepliesTemplateView botQuickRepliesTemplateView;
    protected AgentTransferTemplateView agentTransferTemplateView;
    protected FeedbackTemplateView feedbackTemplateView;
    protected ListWidgetView listWidgetView;
    protected BotListWidgetTemplateView botListWidgetTemplateView;
    protected BotDropDownTemplateView botDropDownTemplateView;
    protected ImageTemplateView imageTemplateView;
    protected BankingFeedbackTemplateView bankingFeedbackTemplateView;
    protected BotContactTemplateView botContactTemplateView;

    //    protected int[] dimens;
    protected int textColor;
    protected int textMediaLayoutGravity = BubbleConstants.GRAVITY_LEFT;
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
        if(isDoDrawBubbleBackground()) {
            setWillNotDraw(false);
            paint = new Paint();
            paint.setXfermode(null);
            setPaintColor(paint);
            paint.setAntiAlias(true);
        }
        viewAddition();
    }


    public void setTimeStampVisible(){
        timeStampsTextView.setVisibility(VISIBLE);
    }
    private void initiliazeCoordinates() {
        if (AppControl.getInstance() != null
                && AppControl.getInstance().getDimensionUtil() != null) {
            dp1 = DimensionUtil.dp1;
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
            BUBBLE_CONTENT_LEFT_MARGIN = (int) dp14;
            BUBBLE_CONTENT_TOP_MARGIN = 0;
            BUBBLE_CONTENT_RIGHT_MARGIN = (int) dp14;
            BUBBLE_CONTENT_BOTTOM_MARGIN = (int)(BubbleUI ?  (8 * dp1) : 21 * dp1);
            BUBBLE_CONTENT_RIGHT_LIST_MARGIN = (int) (dp1 * 40);
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
        //  bubbleTextMediaLayout.setRestrictedLayoutHeight(BubbleViewUtil.getBubbleContentHeight());
        bubbleTextMediaLayout.widthStyle = BubbleConstants.WRAP_CONTENT;
        addView(bubbleTextMediaLayout);

        botButtonView = ViewProvider.getBotButtonView(context,null);
        addView(botButtonView);

        meetingSlotsView = ViewProvider.getMeetingSlotsView(context);
        meetingSlotsView.setComposeFooterInterface(composeFooterInterface);
        //  meetingSlotsView.setRestrictedLayoutWidth(BubbleViewUtil.getSlotsContentWidth());
        addView(meetingSlotsView);

        botContactTemplateView = ViewProvider.getBotContactView(context,null);
        addView(botContactTemplateView);

        multiSelectView = ViewProvider.getMultiSelectView(context);
        multiSelectView.setComposeFooterInterface(composeFooterInterface);
        addView(multiSelectView);

        contactInfoView = ViewProvider.getContactInfoView(context);
        contactInfoView.setComposeFooterInterface(composeFooterInterface);
        contactInfoView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        addView(contactInfoView);

        welcomeSummaryView = ViewProvider.getWelcomeSummaryView(context);
        welcomeSummaryView.setComposeFooterInterface(composeFooterInterface);
        addView(welcomeSummaryView);


        universalSearchView = ViewProvider.getUniversalSearchView(context);
        universalSearchView.setComposeFooterInterface(composeFooterInterface);
        universalSearchView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        addView(universalSearchView);

        koraSummaryHelpView = ViewProvider.getKoraSummaryHelpView(context);
        koraSummaryHelpView.setComposeFooterInterface(composeFooterInterface);
        addView(koraSummaryHelpView);

        meetingConfirmationView = ViewProvider.getMeetingConfirmationView(context);
        meetingConfirmationView.setComposeFooterInterface(composeFooterInterface);
        //   meetingConfirmationView.setRestrictedLayoutWidth(BubbleViewUtil.getMeetingSlotConfirmationWidth());
        addView(meetingConfirmationView);

        attendeeSlotSelectionView = ViewProvider.getAttendeeSlotSelectionView(context);
        attendeeSlotSelectionView.setComposeFooterInterface(composeFooterInterface);
        addView(attendeeSlotSelectionView);

        botListTemplateView = ViewProvider.getBotListTempleteView(context);
        addView(botListTemplateView);

        botCarouselView = ViewProvider.getBotCarousalView(context);
        botCarouselView.setComposeFooterInterface(composeFooterInterface);
        addView(botCarouselView);

        botPieChartView = ViewProvider.getPieChartView(context);
        addView(botPieChartView);

        tableView = ViewProvider.getTableView(context);
        addView(tableView);

        responsiveExpandTableView = ViewProvider.getResponsiveExpandTableView(context);
        addView(responsiveExpandTableView);

        responsiveTableView = ViewProvider.getResponsiveTableView(context);
        addView(responsiveTableView);

        lineChartView = ViewProvider.getLineChartView(context);
        addView(lineChartView);

        barChartView = ViewProvider.getBarChartView(context);
        addView(barChartView);

        stackedBarChatView = ViewProvider.getStackedBarChartView(context);
        addView(stackedBarChatView);

        koraCarouselView = ViewProvider.getKoraCarouselView(context);
        addView(koraCarouselView);

        verticalListView = ViewProvider.getVerticalListView(context);
        addView(verticalListView);
        verticalListView.setComposeFooterInterface(composeFooterInterface);
        verticalListView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);

        timeStampsTextView = ViewProvider.getTimeStampTextView(context);
        timeStampsTextView.setPadding(0,(int)dp1*3  ,0,0);
        addView(timeStampsTextView);
        timeStampsTextView.setVisibility(SDKConfiguration.isTimeStampsRequired() ? VISIBLE : GONE);

        timeLineView = ViewProvider.getTimeLineView(context);
        addView(timeLineView);

        //Added by Sudheer
        horizontalBarChartView = ViewProvider.getHorizontalBarChartView(context);
        addView(horizontalBarChartView);

        botFormTemplateView = ViewProvider.getBotFormTemplateView(context);
        botFormTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(botFormTemplateView);

        botListViewTemplateView = ViewProvider.getBotListViewTempleteView(context);
        botListViewTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(botListViewTemplateView);

        botListWidgetTemplateView = ViewProvider.getBotListWidgetTempleteView(context);
        botListWidgetTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(botListWidgetTemplateView);

        botTableListTemplateView = ViewProvider.getBotTableListTempleteView(context);
        botTableListTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(botTableListTemplateView);

        botQuickRepliesTemplateView = ViewProvider.getBotQuickRepliesTemplateView((context));
        botQuickRepliesTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(botQuickRepliesTemplateView);

        agentTransferTemplateView = ViewProvider.getAgentTransferTemplateView(context);
        agentTransferTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(agentTransferTemplateView);

        feedbackTemplateView = ViewProvider.getFeedbackTemplateView(context);
        feedbackTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(feedbackTemplateView);

        listWidgetView = ViewProvider.getListWidgetTemplateView(context);
        listWidgetView.setComposeFooterInterface(composeFooterInterface);
        addView(listWidgetView);

        botDropDownTemplateView = ViewProvider.getDropDownTemplateView(context);
        botDropDownTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(botDropDownTemplateView);

        imageTemplateView = ViewProvider.getImageTemplateView(context);
        imageTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(imageTemplateView);

        bankingFeedbackTemplateView = ViewProvider.getBankingFeedbackTemplateView(context);
        bankingFeedbackTemplateView.setComposeFooterInterface(composeFooterInterface);
        addView(bankingFeedbackTemplateView);
    }



    abstract void initializeBubbleBorderPass1();

    abstract void initializeBubbleBorderPass2();

    @Override
    protected void onDraw(Canvas canvas) {
        if(isDoDrawBubbleBackground()) {
            //First clear everything
            clearCanvas(canvas);

            //Set the Paint
            setPaintStroke(paint, !isSelected());
            drawBubbleBackground(canvas);
        }
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
        if (botContactTemplateView != null) {
            botContactTemplateView.setComposeFooterInterface(composeFooterInterface);
        }
        if(verticalListView != null){
            verticalListView.setComposeFooterInterface(composeFooterInterface);
        }
        if(meetingSlotsView != null){
            meetingSlotsView.setComposeFooterInterface(composeFooterInterface);
        }
        if(multiSelectView != null){
            multiSelectView.setComposeFooterInterface(composeFooterInterface);
        }
        if(contactInfoView != null){
            contactInfoView.setComposeFooterInterface(composeFooterInterface);
        }
        if(welcomeSummaryView !=null){
            welcomeSummaryView.setComposeFooterInterface(composeFooterInterface);
        }

        if(universalSearchView !=null){
            universalSearchView.setComposeFooterInterface(composeFooterInterface);
        }
        if(koraSummaryHelpView !=null){
            koraSummaryHelpView.setComposeFooterInterface(composeFooterInterface);
        }
        if(meetingConfirmationView != null){
            meetingConfirmationView.setComposeFooterInterface(composeFooterInterface);
        }
        if(attendeeSlotSelectionView != null){
            attendeeSlotSelectionView.setComposeFooterInterface(composeFooterInterface);
        }

        if(botFormTemplateView != null){
            botFormTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if (botListViewTemplateView != null) {
            botListViewTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if (botListWidgetTemplateView != null) {
            botListWidgetTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if (botTableListTemplateView != null) {
            botTableListTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if (botQuickRepliesTemplateView != null) {
            botQuickRepliesTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if (agentTransferTemplateView != null) {
            agentTransferTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if(feedbackTemplateView != null){
            feedbackTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if(listWidgetView != null){
            listWidgetView.setComposeFooterInterface(composeFooterInterface);
        }

        if(botDropDownTemplateView != null) {
            botDropDownTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if(imageTemplateView != null) {
            imageTemplateView.setComposeFooterInterface(composeFooterInterface);
        }

        if(bankingFeedbackTemplateView != null) {
            bankingFeedbackTemplateView.setComposeFooterInterface(composeFooterInterface);
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
        if (verticalListView != null) {
            verticalListView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botContactTemplateView != null) {
            botContactTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if(contactInfoView != null){
            contactInfoView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if(universalSearchView != null){
            universalSearchView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if(botFormTemplateView != null){
            botFormTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botListViewTemplateView != null) {
            botListViewTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botListWidgetTemplateView != null) {
            botListWidgetTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botTableListTemplateView != null) {
            botTableListTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (botQuickRepliesTemplateView != null) {
            botQuickRepliesTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if (agentTransferTemplateView != null) {
            agentTransferTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if(feedbackTemplateView != null){
            feedbackTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
        if(listWidgetView != null){
            listWidgetView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }

        if(botDropDownTemplateView != null) {
            botDropDownTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }

        if(imageTemplateView != null) {
            imageTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }

        if(bankingFeedbackTemplateView != null) {
            bankingFeedbackTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
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
        } else {
            rectBottom = bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        }
        int rectRight = Collections.max(Arrays.asList(bubbleTextMediaLayout.getRight() + BUBBLE_CONTENT_RIGHT_MARGIN,
                botButtonView.getRight(),
                meetingSlotsView.getRight()+ (int)dp2,meetingConfirmationView.getRight()));

        GradientDrawable gradientDrawable = getGradientBubble();
        gradientDrawable.setBounds(new Rect(rectLeft,rectTop,rectRight,rectBottom));
        gradientDrawable.draw(canvas);

        // ViewProvider.drawRoundRect(canvas, new RectF(rectLeft,rectTop,rectRight,rectBottom), paint,(int)(dp12),(int)(dp12),(int)(!isLeftSide() ?  dp12 : dp1),(int)(isLeftSide() ?  dp12 : dp1));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void fillBubbleLayout(int position,boolean isLastItem, BaseBotMessage baseBotMessage) {

        //    bubbleTextMediaLayout.gravity = isLeftSide() ? Gravity.START : Gravity.END;
//        this.dimens = dimens;
        bubbleTextMediaLayout.gravity = textMediaLayoutGravity;
        // Customize BubbleSeparation
        // Customise BubbleTimeLineGrouping Height
        BUBBLE_GROUPING_TIMELINE = 0;

        preCosmeticChanges();

        ComponentModel componentModel = getComponentModel(baseBotMessage);
        // Bubble Text Media
        populateBubbleTextMedia(baseBotMessage, componentModel, isLastItem);
        timeStampsTextView.setText(DateUtils.getTimeInAmPm(baseBotMessage.getCreatedInMillis()));
        // Bubble Templates
        populateForTemplates(position,isLastItem,componentModel,baseBotMessage);

        // timeLineView.setGravity(Gravity.CENTER);
        timeStampsTextView.setGravity(isLeftSide() ? Gravity.START : Gravity.END);
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
        setDoDrawBubbleBackground(false);
        determineTextColor();
        textViewCosmeticChanges();
        timeStampsTextView.setVisibility(GONE);
    }

    /**
     * UI changes
     */
    protected void cosmeticChanges(BaseBotMessage baseBotMessage) {
        bubbleTextMediaLayout.setGravityAndTypeFace();
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

    protected void populateBubbleTextMedia(BaseBotMessage baseBotMessage, ComponentModel componentModel, boolean _isclickable) {

        String message = null;
        String textColor = "#000000";
        bubbleTextMediaLayout.setClicable(_isclickable);
        if (baseBotMessage.isSend() && baseBotMessage instanceof BotRequest) {
            if(((BotRequest) baseBotMessage).getMessage() != null)
                message = ((BotRequest) baseBotMessage).getMessage().getBody();
            bubbleTextMediaLayout.populateTextSenders(message);
        } else if (componentModel != null) {
            String compType = componentModel.getType();
            PayloadOuter payOuter = componentModel.getPayload();
            if(payOuter != null) message = payOuter.getText();
            else return;
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
            bubbleTextMediaLayout.populateText(message);
        }



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

    public void setGroupMessage(boolean groupMessage) {
        isGroupMessage = groupMessage;
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
