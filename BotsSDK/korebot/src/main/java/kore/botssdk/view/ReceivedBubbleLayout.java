package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.view.viewUtils.BubbleViewUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ReceivedBubbleLayout extends BaseBubbleLayout {

    CircularProfileView cpvSenderImage;
    int carouselViewHeight;

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

    private void init() {
        textMediaLayoutGravity = TextMediaLayout.GRAVITY_LEFT;
        carouselViewHeight = (int) getResources().getDimension(R.dimen.carousel_layout_height);
        super.setLeftSide(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        cpvSenderImage = (CircularProfileView) findViewById(R.id.cpvSenderImage);
    }

    @Override
    void initializeBubbleBorderPass1() {

        BUBBLE_LEFT_PROFILE_PIC = cpvSenderImage.getMeasuredWidth();
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? (int) (10 * dp1) : 0;
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? 0 : 0;
        if (isContinuousMessage && isSeparatedClosely) {
            BUBBLE_TOP_BORDER = (int) dp1;
        } else {
            BUBBLE_TOP_BORDER = headerLayout.getMeasuredHeight();//int) (dp14 + dp1);
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
                + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + Collections.max(Arrays.asList(textMediaDimen[0], botCarouselView.getMeasuredWidth(), botButtonView.getMeasuredWidth(),  botListTemplateView.getMeasuredWidth())) + BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;

        headerLayoutDimen[1] = headerLayout.getMeasuredHeight();
        maxBubbleDimen[0] = Collections.max(Arrays.asList(maxContentDimen[0], headerLayoutDimen[0]));

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                headerLayoutDimen[1] + textMediaDimen[1] + botCarouselView.getMeasuredHeight() + botButtonView.getMeasuredHeight() + botListTemplateView.getMeasuredHeight() + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN + headerLayoutDimen[1] + textMediaDimen[1] + botCarouselView.getMeasuredHeight() + botButtonView.getMeasuredHeight() + botListTemplateView.getMeasuredHeight() + BUBBLE_CONTENT_BOTTOM_MARGIN;
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


    protected void populateForTemplates(int position, BaseBotMessage baseBotMessage) {

        // Default out everything
        botButtonView.populateButtonList(null);
        botListTemplateView.populateListTemplateView(null, null);

        if (!((BotResponse) baseBotMessage).getMessage().isEmpty()) {

            ComponentModel compModel = ((BotResponse) baseBotMessage).getMessage().get(0).getComponent();
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
                        botButtonView.populateButtonList(payInner.getButtons());
                        bubbleTextMediaLayout.populateText(payInner.getText());
                    } else if (BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type())) {
                        bubbleTextMediaLayout.populateText(payInner.getText());
                    } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(payInner.getTemplate_type())) {
                        botCarouselView.setVisibility(View.VISIBLE);
//                        botCarouselView.populateCarouselView(payInner.getElements());
                        botCarouselView.populateCarouselView(null);
                        setDoDrawBubbleBackground(false);
                    } else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(payInner.getTemplate_type())) {
                        botListTemplateView.setVisibility(View.VISIBLE);
                        botListTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN - dp1 + BubbleViewUtil.getBubbleContentWidth() - dp1 + BUBBLE_CONTENT_RIGHT_MARGIN);
                        botListTemplateView.populateListTemplateView(payInner.getElements(), payInner.getButtons());
                    }
                } else if (compType.equals(BotResponse.COMPONENT_TYPE_TEXT)) {
                    botListTemplateView.setVisibility(View.GONE);
                }
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

        /*
        *//*
         * For OptionsList
         *//*
        childWidthSpec = MeasureSpec.makeMeasureSpec((int)(BUBBLE_CONTENT_LEFT_BORDER + BubbleViewUtil.getBubbleContentWidth() + BUBBLE_CONTENT_RIGHT_BORDER), MeasureSpec.EXACTLY);
        MeasureUtils.measure(botCustomListView, childWidthSpec, wrapSpec);*/

        /*
         * For CarouselView
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (carouselViewHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(botCarouselView, childWidthSpec, childHeightSpec);

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
         * For Carousel View
         */
        left = 0;
        top = bubbleTextMediaLayout.getBottom();
        LayoutUtils.layoutChild(botCarouselView, left, top);

         /*
         * For re-adjusting CPV
         */
        if (cpvSenderImage.getVisibility() != GONE) {
            int cpvLeft = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT;
            int cpvTop = Collections.max(Arrays.asList(bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN,
                    botButtonView.getBottom() + (int)dp1,
                    botCarouselView.getBottom() - BUBBLE_CAROUSEL_BOTTOM_SHADE_MARGIN,
                    botListTemplateView.getBottom() + (int)dp1)) - cpvSenderImage.getMeasuredHeight();
            LayoutUtils.layoutChild(cpvSenderImage, cpvLeft, cpvTop);
        }

//        botCarouselView.bringToFront();

        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...

    }
}
