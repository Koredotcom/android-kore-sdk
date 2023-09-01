package kore.botssdk.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.R;
import kore.botssdk.charts.data.PieEntry;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotPieChartElementModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.utils.BubbleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.BubbleViewUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ReceivedBubbleLayout extends BaseBubbleLayout {

    CircularProfileView cpvSenderImage;
    int carouselViewHeight, pieViewHeight,tableHeight,lineHeight,miniTableHeight;

    public ReceivedBubbleLayout(Context context) {
        super(context);
        init();
    }

    public ReceivedBubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReceivedBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ReceivedBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public int getLinkTextColor() {
        return getResources().getColor(R.color.mentionsAndHashTagColor);
    }

    private void init() {
        textMediaLayoutGravity = BubbleConstants.GRAVITY_LEFT;
        carouselViewHeight = (int) getResources().getDimension(R.dimen.carousel_layout_height);
        pieViewHeight = (int) getResources().getDimension(R.dimen.pie_layout_height);
        tableHeight = (int) getResources().getDimension(R.dimen.table_layout_height);
        lineHeight = (int) getResources().getDimension(R.dimen.line_layout_height);
        miniTableHeight = (int) getResources().getDimension(R.dimen.mini_table_layout_height);
        super.setLeftSide(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        cpvSenderImage = findViewById(R.id.cpvSenderImage);
    }

    @Override
    void initializeBubbleBorderPass1() {

        BUBBLE_LEFT_PROFILE_PIC = cpvSenderImage.getMeasuredWidth();
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? (int) (10 * dp1) : 0;
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = 0;
        if (isContinuousMessage && isSeparatedClosely) {
            BUBBLE_TOP_BORDER = (int) dp1;
        } else {
            BUBBLE_TOP_BORDER = (int) dp1 + headerLayout.getMeasuredHeight();//int) (dp14 + dp1);
        }
        BUBBLE_LEFT_BORDER = (int) ((!isGroupMessage) ? dp4 : dp1);
        BUBBLE_RIGHT_BORDER = (int) dp1;
        BUBBLE_DOWN_BORDER = (int) dp1;
        BUBBLE_ARROW_WIDTH = (int) ((isGroupMessage) ? dp1 : dp10);
        BUBBLE_LEFT_ARROW_WIDTH = (int) (BUBBLE_ARROW_WIDTH / 2 + 7 * dp1);
        BUBBLE_RIGHT_ARROW_WIDTH = 0;
        BUBBLE_CONTENT_TOP_MARGIN = (int) dp14;
        BUBBLE_CAROUSEL_BOTTOM_SHADE_MARGIN = (int) getResources().getDimension(R.dimen.carousel_view_cardCornerRadius);
    }


    @Override
    void initializeBubbleBorderPass2() {
        BUBBLE_CONTENT_RIGHT_BORDER = 0; //this is always 0...
        BUBBLE_CONTENT_LEFT_BORDER = 0; //this is always 0...

        invalidate();
    }

    @Override
    protected void initializeBubbleContentDimen() {
        super.initializeBubbleContentDimen();

        headerLayoutDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + BUBBLE_LEFT_PROFILE_PIC + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH + headerLayout.getMeasuredWidth();
        maxContentDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + BUBBLE_LEFT_PROFILE_PIC + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT
                + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + Collections.max(Arrays.asList(textMediaDimen[0], botCarouselView.getMeasuredWidth(), botButtonView.getMeasuredWidth(),  lineChartView.getMeasuredWidth(),
                barChartView.getMeasuredWidth(),miniTableView.getMeasuredWidth(),stackedBarChatView.getMeasuredWidth(),
                botListTemplateView.getMeasuredWidth(),botPieChartView.getMeasuredWidth())) + BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;

        headerLayoutDimen[1] = headerLayout.getMeasuredHeight();
        maxBubbleDimen[0] = Collections.max(Arrays.asList(maxContentDimen[0], headerLayoutDimen[0]));

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                headerLayoutDimen[1] + textMediaDimen[1] + botCarouselView.getMeasuredHeight() + botPieChartView.getMeasuredHeight() +  lineChartView.getMeasuredHeight()+ barChartView.getMeasuredHeight()+stackedBarChatView.getMeasuredHeight()
                +botButtonView.getMeasuredHeight() + botListTemplateView.getMeasuredHeight()+miniTableView.getMeasuredHeight() + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN + headerLayoutDimen[1] + textMediaDimen[1] + botCarouselView.getMeasuredHeight() + botButtonView.getMeasuredHeight() + botListTemplateView.getMeasuredHeight() + botPieChartView.getMeasuredHeight() +
                lineChartView.getMeasuredHeight()+barChartView.getMeasuredHeight()+stackedBarChatView.getMeasuredHeight()+miniTableView.getMeasuredHeight()+BUBBLE_CONTENT_BOTTOM_MARGIN;
    }

    @Override
    protected void populateHeaderLayout(int position, BaseBotMessage baseBotMessage) {
        try {
            headerLayout.populateHeader(DateUtils.getTimeStamp(baseBotMessage.getCreatedOn(), true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void preCosmeticChanges() {
        super.preCosmeticChanges();
        botButtonView.setVisibility(View.GONE);
        botCarouselView.setVisibility(View.GONE);
        botListTemplateView.setVisibility(View.GONE);
        botPieChartView.setVisibility(View.GONE);
        lineChartView.setVisibility(GONE);
        barChartView.setVisibility(GONE);
        stackedBarChatView.setVisibility(GONE);
        miniTableView.setVisibility(GONE);
    }

    @Override
    protected void cosmeticChanges(BaseBotMessage baseBotMessage, int position) {
        super.cosmeticChanges(baseBotMessage, position);
        cosmetiseForProfilePic(baseBotMessage);
    }


    protected void cosmetiseForProfilePic(BaseBotMessage baseBotMessage) {
        if (isGroupMessage) {
            String icon = ((BotResponse) baseBotMessage).getIcon();
            cpvSenderImage.setVisibility(VISIBLE);
            cpvSenderImage.populateLayout(" ", null, icon, null, -1, 0, true, BUBBLE_LEFT_PROFILE_PIC, BUBBLE_LEFT_PROFILE_PIC);
        } else {
            cpvSenderImage.setVisibility(GONE);
        }
    }

    protected void populateForTemplates(int position, ComponentModel compModel) {

        // Default out everything
        botButtonView.populateButtonList(null,false);
        botListTemplateView.populateListTemplateView(null, null);

        botListTemplateView.setVisibility(View.GONE);
        botCarouselView.populateCarouselView(null);
        botCarouselView.setVisibility(View.GONE);
        botButtonView.setVisibility(View.GONE);
        miniTableView.setVisibility(GONE);

        if (compModel != null) {
            String compType = compModel.getType();
            PayloadOuter payOuter = compModel.getPayload();
            PayloadInner payInner;
            if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                Gson gson = new Gson();
                payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
            }
            payInner = payOuter.getPayload();

            if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(payOuter.getType())) {

                if (BotResponse.TEMPLATE_TYPE_BUTTON.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botButtonView.setVisibility(View.VISIBLE);
                    botButtonView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN - dp1 + BubbleViewUtil.getBubbleContentWidth() - dp1 + BUBBLE_CONTENT_RIGHT_MARGIN);
                    botButtonView.populateButtonList(payInner.getButtons(),false);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    setDoDrawBubbleBackground(!(payInner.getText() == null || payInner.getText().isEmpty()));
                } else if (BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    setDoDrawBubbleBackground(!(payInner.getText() == null || payInner.getText().isEmpty()));
                } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TYPE_CAROUSEL_ADV.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botCarouselView.setVisibility(View.VISIBLE);
                    botCarouselView.populateCarouselView(payInner.getCarouselElements(),payInner.getTemplate_type());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    setDoDrawBubbleBackground(!(payInner.getText() == null || payInner.getText().isEmpty()));
                    if(!isDoDrawBubbleBackground()){
                        cpvSenderImage.setVisibility(GONE);
                    }
                } else if(BotResponse.TEMPLATE_TYPE_PIECHART.equalsIgnoreCase(payInner.getTemplate_type())){
                    botPieChartView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    ArrayList<BotPieChartElementModel> elementModels = payInner.getPieChartElements();
                    if(elementModels != null && !elementModels.isEmpty()) {
                        ArrayList<String > xVal = new ArrayList<>(elementModels.size());
                        ArrayList<PieEntry> yVal = new ArrayList<>(elementModels.size());
                        ArrayList<String> arrLables = new ArrayList<>(elementModels.size());

                        for(int i=0; i < elementModels.size(); i++){
                            xVal.add(elementModels.get(i).getTitle());
                            yVal.add(new PieEntry((float)elementModels.get(i).getValue(),i));
                            arrLables.add(elementModels.get(i).getTitle()+" "+elementModels.get(i).getValue());
                        }
                        botPieChartView.populatePieChart("",payInner.getPie_type(), xVal,yVal,arrLables);
                    }
                }else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botListTemplateView.setVisibility(View.VISIBLE);
                    botListTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN - dp1 + BubbleViewUtil.getBubbleContentWidth() - dp1 + BUBBLE_CONTENT_RIGHT_MARGIN);
                    botListTemplateView.populateListTemplateView(payInner.getListElements(), payInner.getButtons());
                }/*else if(BotResponse.TEMPLATE_TYPE_TABLE.equalsIgnoreCase(payInner.getTemplate_type())){
                    tableView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    tableView.populateTableView(payInner);
                }*/else if(BotResponse.TEMPLATE_TYPE_LINECHART.equalsIgnoreCase(payInner.getTemplate_type())){
                    lineChartView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    lineChartView.setData(payInner);
                }else if(BotResponse.TEMPLATE_TYPE_BARCHART.equalsIgnoreCase(payInner.getTemplate_type())){
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    if(!payInner.isStacked()) {
                        barChartView.setVisibility(View.VISIBLE);
                        barChartView.setData(payInner);
                    }else{
                        stackedBarChatView.setVisibility(View.VISIBLE);
                        stackedBarChatView.setData(payInner);
                    }
                }else if(BotResponse.TEMPLATE_TYPE_TABLE.equalsIgnoreCase(payInner.getTemplate_type())){
                    //TODO handle
//                    miniTableView.setVisibility(View.VISIBLE);
//                    bubbleTextMediaLayout.populateText(payInner.getText());
//                    miniTableView.setData(payInner.getTemplate_type(), payInner);
                }else if(BotResponse.TEMPLATE_TYPE_MINITABLE.equalsIgnoreCase(payInner.getTemplate_type())){
                    //TODO handle
                    /*miniTableView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
//                    for(BotMiniTableModel model:payInner.getMiniTableDataModels()) {
                        miniTableView.setData(payInner.getTemplate_type(), payInner);
//                    }*/
                }else if(!StringUtils.isNullOrEmptyWithTrim(payInner.getText())){
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }else if(BotResponse.COMPONENT_TYPE_MESSAGE.equalsIgnoreCase(payOuter.getType())){
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }else {
                    bubbleTextMediaLayout.populateText(payOuter.getText());
                }
            }else if(BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType())){
                String message = payOuter.getPayload().getText();
                String txtColor = payOuter.getPayload().getColor();
                if (botContentTextView != null) {
                    try {
                        botContentTextView.setTextColor(Color.parseColor(txtColor));
                        botContentTextView.setText(message);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                bubbleTextMediaLayout.populateText(payOuter.getText());
            }
        }

    }


    /**
     * Layout Manipulation Section
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int totalWidth = getPaddingLeft();

        int childWidthSpec;
        int childHeightSpec;
        int contentWidth = 0;

        /*
         * For TextMedia Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.AT_MOST);
        MeasureUtils.measure(bubbleTextMediaLayout, childWidthSpec, wrapSpec);
        contentWidth = bubbleTextMediaLayout.getWidth();

        /*
         * For Sender icon [CPV]
         */
        float cpvSenderImageDimen = dp1 * 35;
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) cpvSenderImageDimen, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) cpvSenderImageDimen, MeasureSpec.EXACTLY);
        cpvSenderImage.setDimens(cpvSenderImageDimen, cpvSenderImageDimen);
        MeasureUtils.measure(cpvSenderImage, childWidthSpec, childHeightSpec);

        int cpvMarginLeft, cpvMarginRight;
        if (cpvSenderImage.getMeasuredWidth() != 0) {
            cpvMarginLeft = cpvMarginRight = (int) dp4;
        } else {
            cpvMarginLeft = cpvMarginRight = 0;
        }


        /*
         * For Time Stamp
         */
        MeasureUtils.measure(headerLayout, wrapSpec, wrapSpec);

        /*
         * For Button Templates
         */
        MeasureUtils.measure(botButtonView, wrapSpec, wrapSpec);

        /*
         * For List Templates
         */
        MeasureUtils.measure(botListTemplateView, wrapSpec, wrapSpec);

        /**
         * For PieChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 50 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(pieViewHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(botPieChartView, childWidthSpec,childHeightSpec);

        /**
         * For TableView
         */
        /*childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 50 * (int)dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (tableHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(miniTableView, childWidthSpec,wrapSpec);*/

        /**
         * for line chart
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 50 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(lineHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(lineChartView, childWidthSpec,childHeightSpec);

        /**
         * For BarChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 50 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(pieViewHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(barChartView, childWidthSpec,childHeightSpec);

        /**
         * For stacked BarChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 50 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(pieViewHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(stackedBarChatView, childWidthSpec,childHeightSpec);

        /*
         * For CarouselView
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (carouselViewHeight) + BUBBLE_CONTENT_BOTTOM_MARGIN , MeasureSpec.EXACTLY);
        MeasureUtils.measure(botCarouselView, childWidthSpec, wrapSpec);

        //for mini table
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 50 * (int)dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (miniTableHeight), MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (carouselViewHeight) + BUBBLE_CONTENT_BOTTOM_MARGIN , MeasureSpec.EXACTLY);
        MeasureUtils.measure(miniTableView, childWidthSpec, wrapSpec);


        initializeBubbleDimensionalParametersPhase1(); //Initiliaze params

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(maxBubbleDimen[1], MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(maxBubbleDimen[0], MeasureSpec.EXACTLY);

        super.onMeasure(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //Consider the paddings and manupulate them it first..
        l += getPaddingLeft();
        t += getPaddingTop();
        r -= getPaddingRight();
        b -= getPaddingBottom();

        int top = 0, left = 0;

        left = BUBBLE_LEFT_BORDER;
        top = getPaddingTop() + BUBBLE_TOP_BORDER + BUBBLE_SEPARATION_DISTANCE;

        /*
         * For Time Stamp
         */
        left += BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + cpvSenderImage.getMeasuredWidth() + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH;
        LayoutUtils.layoutChild(headerLayout, left, top);
        top = headerLayout.getBottom();

        /*
         * For Sender icon [CPV]
         */
        if (cpvSenderImage.getVisibility() != GONE) {
            left = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT;
            LayoutUtils.layoutChild(cpvSenderImage, left, top);
            left = cpvSenderImage.getRight() + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH;
        } else {
            left = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_ARROW_WIDTH;
        }

        int bubbleTextMediaLayouMarginLeft = BUBBLE_CONTENT_LEFT_MARGIN;
        int bubbleTextMediaLayouMarginTop = BUBBLE_CONTENT_TOP_MARGIN + BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT;
        int bubbleTextMediaLayouMarginRight = BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;
        int bubbleTextMediaLayouMarginBottom = BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;

        /*
         * For TextMedia Layout
         */
        left += bubbleTextMediaLayouMarginLeft;
        top = headerLayout.getBottom() + bubbleTextMediaLayouMarginTop;
        LayoutUtils.layoutChild(bubbleTextMediaLayout, left, top);
        top = bubbleTextMediaLayout.getBottom();

        /*
         * For Button View
         */
        left = (int) (bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN + dp1);
        top += BUBBLE_CONTENT_BOTTOM_MARGIN;
        LayoutUtils.layoutChild(botButtonView, left, top);
        top = botButtonView.getBottom();

        /*
        * For OptionsList
        * */
        left = botButtonView.getLeft();
        top = bubbleTextMediaLayout.getBottom() - BUBBLE_CONTENT_TOP_MARGIN;
        LayoutUtils.layoutChild(botListTemplateView, left, top);




         /*
         * For re-adjusting CPV
         */
        if (cpvSenderImage.getVisibility() != GONE) {
            int cpvLeft = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT;
            int cpvTop = Collections.max(Arrays.asList(bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN,
                    botButtonView.getBottom() + (int)dp1,
                  /*  botCarouselView.getBottom() - BUBBLE_CAROUSEL_BOTTOM_SHADE_MARGIN,*/
                    botListTemplateView.getBottom() + (int) dp1)) - cpvSenderImage.getMeasuredHeight();
            LayoutUtils.layoutChild(cpvSenderImage, cpvLeft, cpvTop);
        }
        /*
         * For Carousel View
         */
        left = 0;
        top = cpvSenderImage.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        LayoutUtils.layoutChild(botCarouselView, left, top);

        /**
         * For PieChat view
         */
        left = cpvSenderImage.getRight() /2;
        top = cpvSenderImage.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        LayoutUtils.layoutChild(botPieChartView, left, top);

        /**
         * For Table view
         */
        /*left = cpvSenderImage.getRight() /2;
        top = cpvSenderImage.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN + 10;
        LayoutUtils.layoutChild(tableView, left, top);*/

        left = cpvSenderImage.getRight()/2;
        top = cpvSenderImage.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN + 10;
        LayoutUtils.layoutChild(lineChartView, left, top);

        left = cpvSenderImage.getRight()/2;
        top = cpvSenderImage.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN + 10;
        LayoutUtils.layoutChild(barChartView, left, top);

        left = cpvSenderImage.getRight()/2;
        top = cpvSenderImage.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN + 10;
        LayoutUtils.layoutChild(stackedBarChatView, left, top);

        left = cpvSenderImage.getRight()/2;
        top = cpvSenderImage.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN + 10;
        LayoutUtils.layoutChild(miniTableView, left, top);



        botCarouselView.bringToFront();

        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...

    }
}
