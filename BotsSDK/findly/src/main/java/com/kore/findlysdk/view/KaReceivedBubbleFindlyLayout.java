package com.kore.findlysdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.github.mikephil.charting.data.PieEntry;
import com.kore.findlysdk.R;
import com.kore.findlysdk.models.BaseBotMessage;
import com.kore.findlysdk.models.BotPieChartElementModel;
import com.kore.findlysdk.models.BotResponse;
import com.kore.findlysdk.models.ComponentModel;
import com.kore.findlysdk.models.PayloadInner;
import com.kore.findlysdk.models.PayloadOuter;
import com.kore.findlysdk.net.SDKConfiguration;
import com.kore.findlysdk.utils.BubbleConstants;
import com.kore.findlysdk.utils.DateUtils;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.view.viewUtils.BubbleViewUtil;
import com.kore.findlysdk.view.viewUtils.LayoutUtils;
import com.kore.findlysdk.view.viewUtils.MeasureUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.kore.findlysdk.net.SDKConfiguration.BubbleColors.BubbleUI;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class KaReceivedBubbleFindlyLayout extends KaBaseBubbleFindlyLayout {

    CircularProfileFindlyView cpvSenderImage;
    int carouselViewHeight, pieViewHeight, lineHeight, respTableViewHeight;
    ArrayList<Integer> arrayList = new ArrayList<>();

    public KaReceivedBubbleFindlyLayout(Context context) {
        super(context);
        init();
    }

    public KaReceivedBubbleFindlyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KaReceivedBubbleFindlyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public KaReceivedBubbleFindlyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public int getLinkTextColor() {
        return LEFT_BUBBLE_LINK_COLOR;
    }

    private void init() {
        textMediaLayoutGravity = BubbleConstants.GRAVITY_LEFT;
        carouselViewHeight = (int) getResources().getDimension(R.dimen.carousel_layout_height);
        pieViewHeight = (int) getResources().getDimension(R.dimen.pie_layout_height);
        respTableViewHeight = (int) getResources().getDimension(R.dimen.line_layout_height);
//        tableHeight = (int) getResources().getDimension(R.dimen.my_table_height);
        lineHeight = (int) getResources().getDimension(R.dimen.line_layout_height);
        super.setLeftSide(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        cpvSenderImage = (CircularProfileFindlyView) findViewById(R.id.cpvSenderImage);
    }

    @Override
    void initializeBubbleBorderPass1() {

        BUBBLE_LEFT_PROFILE_PIC = cpvSenderImage.getMeasuredWidth();
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? (int) (10 * dp1) : 0;
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? 0 : 0;
        if (isContinuousMessage) {
            BUBBLE_TOP_BORDER = 0;
        } else {
            BUBBLE_TOP_BORDER = (int) (10 * dp1);
        }
        BUBBLE_LEFT_BORDER = (int) ((!isGroupMessage) ? BubbleUI ? dp4 : 0 : dp1);
        BUBBLE_RIGHT_BORDER = (int) dp1;
        BUBBLE_DOWN_BORDER = (int) dp1;
        BUBBLE_ARROW_WIDTH = (int) ((isGroupMessage) ? dp1 : BubbleUI ? dp10 : 0);
        BUBBLE_LEFT_ARROW_WIDTH = (int) (BubbleUI ? BUBBLE_ARROW_WIDTH / 2 + 7 * dp1 : 0);
        BUBBLE_RIGHT_ARROW_WIDTH = 0;
        if (bubbleTextMediaLayout.getMeasuredHeight() != 0) {
            BUBBLE_CONTENT_TOP_MARGIN = 0;
            BUBBLE_CONTENT_BOTTOM_MARGIN = (int) (BubbleUI ? 8 * dp1 : 21 * dp1);
        } else {
            BUBBLE_CONTENT_TOP_MARGIN = 0;
            BUBBLE_CONTENT_BOTTOM_MARGIN = 0;
        }
        BUBBLE_CAROUSEL_BOTTOM_SHADE_MARGIN = (int) getResources().getDimension(R.dimen.carousel_view_cardCornerRadius);
    }


    @Override
    void initializeBubbleBorderPass2() {
        BUBBLE_CONTENT_RIGHT_BORDER = 0; //this is always 0...
        BUBBLE_CONTENT_LEFT_BORDER = 0; //this is always 0...

        //  invalidate();
    }

    @Override
    protected void initializeBubbleContentDimen() {
        super.initializeBubbleContentDimen();

        // headerLayoutDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + BUBBLE_LEFT_PROFILE_PIC + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH + headerLayout.getMeasuredWidth();
        maxContentDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + BUBBLE_LEFT_PROFILE_PIC + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT
                + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + Collections.max(Arrays.asList(textMediaDimen[0], botCarouselView.getMeasuredWidth(), timeStampsTextView.getMeasuredWidth(), timeLineView.getMeasuredWidth(),
                multiSelectView.getMeasuredWidth(), botButtonView.getMeasuredWidth(), tableView.getMeasuredWidth(),responsiveTableView.getMeasuredWidth(),responsiveExpandTableView.getMeasuredWidth(),
                lineChartView.getMeasuredWidth(),barChartView.getMeasuredWidth(),horizontalBarChartView.getMeasuredWidth(), stackedBarChatView.getMeasuredWidth(), botListViewTemplateView.getMeasuredWidth(),
                botListTemplateView.getMeasuredWidth(), botPieChartView.getMeasuredWidth(), botFormTemplateView.getMeasuredWidth(),botTableListTemplateView.getMeasuredWidth(),botQuickRepliesTemplateView.getMeasuredWidth(), cardTemplateView.getMeasuredWidth(),
                agentTransferTemplateView.getMeasuredWidth(),feedbackTemplateView.getMeasuredWidth(),listWidgetView.getMeasuredWidth(),resultsTemplateView.getMeasuredWidth())) + BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;


        // headerLayoutDimen[1] = headerLayout.getMeasuredHeight();
        maxBubbleDimen[0] = maxContentDimen[0];

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                textMediaDimen[1] + botCarouselView.getMeasuredHeight() + multiSelectView.getMeasuredHeight() + cpvSenderImage.getMeasuredHeight() + timeLineView.getMeasuredHeight() +
                botPieChartView.getMeasuredHeight() + tableView.getMeasuredHeight() + responsiveTableView.getMeasuredHeight() + responsiveExpandTableView.getMeasuredHeight() +
                lineChartView.getMeasuredHeight()+barChartView.getMeasuredHeight()+horizontalBarChartView.getMeasuredHeight()+stackedBarChatView.getMeasuredHeight()+ resultsTemplateView.getMeasuredHeight()
                + botListViewTemplateView.getMeasuredHeight()+botTableListTemplateView.getMeasuredHeight()+botQuickRepliesTemplateView.getMeasuredHeight()+agentTransferTemplateView.getMeasuredHeight() + feedbackTemplateView.getMeasuredHeight() + cardTemplateView.getMeasuredHeight()
                + botButtonView.getMeasuredHeight() + botListTemplateView.getMeasuredHeight() + listWidgetView.getMeasuredHeight() + botFormTemplateView.getMeasuredHeight() + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER + (int) (botButtonView.getMeasuredHeight() != 0 ? dp2 : 0);
        if (bubbleTextMediaLayout.getMeasuredHeight() != 0 && isTemplatePresent()) {
            maxBubbleDimen[1] = maxBubbleDimen[1] + (int) dp15;
        }
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN + textMediaDimen[1] + botCarouselView.getMeasuredHeight() + cpvSenderImage.getMeasuredHeight() + timeLineView.getMeasuredHeight() + botButtonView.getMeasuredHeight() + botListTemplateView.getMeasuredHeight() + botPieChartView.getMeasuredHeight() +
                tableView.getMeasuredHeight() + responsiveTableView.getMeasuredHeight() + responsiveExpandTableView.getMeasuredHeight() + lineChartView.getMeasuredHeight()
                + barChartView.getMeasuredHeight()+horizontalBarChartView.getMeasuredHeight()+stackedBarChatView.getMeasuredHeight()
                + agentTransferTemplateView.getMeasuredHeight()
                + multiSelectView.getMeasuredHeight() + botFormTemplateView.getMeasuredHeight()+botListViewTemplateView.getMeasuredHeight()
                + botTableListTemplateView.getMeasuredHeight()+botQuickRepliesTemplateView.getMeasuredHeight()+resultsTemplateView.getMeasuredHeight()+cardTemplateView.getMeasuredHeight()
                +feedbackTemplateView.getMeasuredHeight()+listWidgetView.getMeasuredHeight()+ BUBBLE_CONTENT_BOTTOM_MARGIN;
        if (bubbleTextMediaLayout.getMeasuredHeight() != 0 && isTemplatePresent()) {
            maxBubbleDimen[1] = maxBubbleDimen[1] + (int) dp15;
        }
    }

    private boolean isTemplatePresent() {
        return  botListTemplateView.getMeasuredHeight() > 0 || botButtonView.getMeasuredHeight() > 0
                || botCarouselView.getMeasuredHeight() > 0  || botPieChartView.getMeasuredHeight() > 0
                || botFormTemplateView.getMeasuredHeight() > 0 || botListViewTemplateView.getMeasuredHeight() > 0 || botTableListTemplateView.getMeasuredHeight() > 0 ||
                lineChartView.getMeasuredHeight() > 0 ||barChartView.getMeasuredHeight() > 0||horizontalBarChartView.getMeasuredHeight() > 0||stackedBarChatView.getMeasuredHeight() > 0 || listWidgetView.getMeasuredHeight() > 0 ||
                tableView.getMeasuredHeight() > 0 ||responsiveTableView.getMeasuredHeight() > 0 ||responsiveExpandTableView.getMeasuredHeight() > 0 || agentTransferTemplateView.getMeasuredHeight() > 0 || feedbackTemplateView.getMeasuredHeight() > 0 ||
                multiSelectView.getMeasuredHeight() > 0 || botQuickRepliesTemplateView.getMeasuredHeight() > 0 || resultsTemplateView.getMeasuredHeight() > 0 || cardTemplateView.getMeasuredHeight() > 0;
    }

    @Override
    protected void preCosmeticChanges() {
        super.preCosmeticChanges();
        resetAll();

    }

    private void resetAll() {
        arrayList.clear();
        botButtonView.setVisibility(View.GONE);
        botButtonView.populateButtonList(null,false);
        botCarouselView.populateCarouselView(null);
        botCarouselView.setVisibility(View.GONE);

        multiSelectView.populateData(null,false);
        multiSelectView.setVisibility(GONE);
        botListTemplateView.setVisibility(View.GONE);
        botListTemplateView.populateListTemplateView(null, null);
        botPieChartView.setVisibility(View.GONE);
        tableView.setData(null);
        tableView.setVisibility(View.GONE);
        tableView.setData(null);
        responsiveTableView.setData(null);
        responsiveTableView.setVisibility(View.GONE);
        responsiveTableView.setData(null);
        responsiveExpandTableView.setData(null);
        responsiveExpandTableView.setVisibility(View.GONE);
        responsiveExpandTableView.setData(null);
        lineChartView.setVisibility(GONE);
        barChartView.setVisibility(GONE);
        horizontalBarChartView.setVisibility(GONE);
        stackedBarChatView.setVisibility(GONE);
        timeLineView.setVisibility(GONE);
        timeLineView.setText("");

        botFormTemplateView.populateData(null,false);
        botFormTemplateView.setVisibility(GONE);
        botListViewTemplateView.setVisibility(View.GONE);
        botListViewTemplateView.populateListTemplateView(null, null,null, null, 0, null);
        botTableListTemplateView.setVisibility(View.GONE);
        botTableListTemplateView.populateListTemplateView(null);
        botQuickRepliesTemplateView.setVisibility(View.GONE);
        botQuickRepliesTemplateView.populateQuickReplyView(null);
        agentTransferTemplateView.setVisibility(View.GONE);
        agentTransferTemplateView.populateAgentTemplateView(null);
        feedbackTemplateView.setVisibility(View.GONE);
        feedbackTemplateView.populateData(null, false);
        listWidgetView.setVisibility(GONE);
        listWidgetView.populateListWidgetData(null);
        resultsTemplateView.setVisibility(GONE);
        resultsTemplateView.populateResultsTemplateView(null);
        cardTemplateView.setVisibility(GONE);
        cardTemplateView.populateCardTemplateView(null);
    }

    @Override
    protected void cosmeticChanges(BaseBotMessage baseBotMessage) {
        super.cosmeticChanges(baseBotMessage);
        cosmetiseForProfilePic(baseBotMessage);
    }


    protected void cosmetiseForProfilePic(BaseBotMessage baseBotMessage) {
/*        if (isGroupMessage) {
            String icon = ((BotResponse) baseBotMessage).getIcon();
            cpvSenderImage.setVisibility(VISIBLE);
            cpvSenderImage.populateLayout(" ", null, icon, null, SDKConfiguration.BubbleColors.getIcon(), 0, true, BUBBLE_LEFT_PROFILE_PIC, BUBBLE_LEFT_PROFILE_PIC);
        } else {
            cpvSenderImage.setVisibility(GONE);
        }*/
        String icon = ((BotResponse) baseBotMessage).getIcon();
        if(SDKConfiguration.BubbleColors.showIcon) {
            cpvSenderImage.populateLayout(" ", null, null, null, SDKConfiguration.BubbleColors.getIcon(), R.color.white, true, BUBBLE_LEFT_PROFILE_PIC, BUBBLE_LEFT_PROFILE_PIC);
            cpvSenderImage.setVisibility(StringUtils.isNullOrEmptyWithTrim(timeStampsTextView.getText()) ? GONE : VISIBLE);
        }else{
            cpvSenderImage.setVisibility(GONE);
        }
    }


    protected void populateForTemplates(int position, boolean isLastItem, ComponentModel compModel, BaseBotMessage baseBotMessage) {
        resetAll();
        if (compModel != null) {

            //composeFooterInterface.showMentionNarratorContainer(false, "","" ,null);

            PayloadOuter payOuter = compModel.getPayload();
            if(payOuter == null) return;
            PayloadInner payInner;
            if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
//                Gson gson = new Gson();
                payOuter.getText().replace("&quot;", "\"");
            }
            payInner = payOuter.getPayload();

            if (payInner != null)
                payInner.convertElementToAppropriate();

            if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(payOuter.getType()) && payInner != null) {
                checkBubbleVisibilityAndHideCpv(payInner);
                if (BotResponse.TEMPLATE_TYPE_BUTTON.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botButtonView.setVisibility(View.VISIBLE);
                    botButtonView.setRestrictedMaxWidth(screenWidth - 28 * dp1 );
                    botButtonView.populateButtonList(payInner.getButtons(),isLastItem);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TYPE_FORM_ACTIONS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.setClicable(isLastItem);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    if (StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
                        timeStampsTextView.setText("");
                    }
                } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TYPE_WELCOME_CAROUSEL.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botCarouselView.setVisibility(View.VISIBLE);
                    botCarouselView.populateCarouselView(payInner.getCarouselElements(), payInner.getTemplate_type());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botListTemplateView.setVisibility(View.VISIBLE);
                    botListTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() + BUBBLE_CONTENT_RIGHT_MARGIN);
                    botListTemplateView.populateListTemplateView(payInner.getListElements(), payInner.getButtons());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_PIECHART.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botPieChartView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    ArrayList<BotPieChartElementModel> elementModels = payInner.getPieChartElements();
                    if (elementModels != null && !elementModels.isEmpty()) {
                        ArrayList<String> xVal = new ArrayList<>(elementModels.size());
                        ArrayList<PieEntry> yVal = new ArrayList<>(elementModels.size());
                        ArrayList<String> arrLables = new ArrayList<>(elementModels.size());

                        for (int i = 0; i < elementModels.size(); i++) {
                            xVal.add(elementModels.get(i).getTitle());
                            yVal.add(new PieEntry((float) elementModels.get(i).getValue(), " "));
                            arrLables.add(elementModels.get(i).getTitle()+" "+elementModels.get(i).getValue());
                        }
                        botPieChartView.populatePieChart("", payInner.getPie_type(), xVal, yVal,arrLables);
                    }

                }/*else if (BotResponse.TEMPLATE_TYPE_TABLE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    tableView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    tableView.setData(payInner);

                }*/else if (BotResponse.TEMPLATE_TYPE_TABLE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if(payInner.getTableDesign() != null && (payInner.getTableDesign().equalsIgnoreCase(BotResponse.TABLE_VIEW_RESPONSIVE) ||
                            payInner.getTableDesign().equalsIgnoreCase("")))
                    {
                        responsiveExpandTableView.setVisibility(View.VISIBLE);
                        bubbleTextMediaLayout.populateText(payInner.getText());
                        responsiveExpandTableView.setData(payInner);
                    }
                    else
                    {
                        tableView.setVisibility(View.VISIBLE);
                        bubbleTextMediaLayout.populateText(payInner.getText());
                        tableView.setData(payInner);
                    }
                }
                else if(BotResponse.TEMPLATE_TYPE_MULTI_SELECT.equalsIgnoreCase(payInner.getTemplate_type())){
                    multiSelectView.setVisibility(View.VISIBLE);
                    multiSelectView.populateData(payInner, isLastItem);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }else if (BotResponse.TEMPLATE_TYPE_LINECHART.equalsIgnoreCase(payInner.getTemplate_type())) {
                    lineChartView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    lineChartView.setData(payInner);
                }
                else if(BotResponse.TEMPLATE_TYPE_BARCHART.equalsIgnoreCase(payInner.getTemplate_type())){
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    if(!payInner.isStacked())
                    {
                        if(!BotResponse.BAR_CHART_DIRECTION_VERTICAL.equalsIgnoreCase(payInner.getDirection()))
                        {
                            barChartView.setVisibility(View.VISIBLE);
                            barChartView.setData(payInner);
                        }
                        else
                        {
                            horizontalBarChartView.setVisibility(VISIBLE);
                            horizontalBarChartView.setData(payInner);
                        }
                    }else{
                        stackedBarChatView.setVisibility(View.VISIBLE);
                        stackedBarChatView.setData(payInner);
                    }
                }
                else if(BotResponse.TEMPLATE_TYPE_FORM.equalsIgnoreCase(payInner.getTemplate_type())){
                    botFormTemplateView.setVisibility(View.VISIBLE);
                    botFormTemplateView.populateData(payInner, isLastItem);
//                    bubbleTextMediaLayout.populateText(payInner.getHeading());
                }
                else if(BotResponse.TEMPLATE_TYPE_LIST_VIEW.equalsIgnoreCase(payInner.getTemplate_type())){
                    botListViewTemplateView.setVisibility(View.VISIBLE);
                    botListViewTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    botListViewTemplateView.populateListTemplateView(payInner.getText(), payInner.getMoreData(), payInner.getListElements(), payInner.getButtons(), payInner.getMoreCount(), payInner.getSeeMore());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }
                else if(BotResponse.TEMPLATE_TYPE_TABLE_LIST.equalsIgnoreCase(payInner.getTemplate_type())){
                    botTableListTemplateView.setVisibility(View.VISIBLE);
                    botTableListTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    botTableListTemplateView.populateListTemplateView(payInner.getTableListElements());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }
                else if(BotResponse.TEMPLATE_TYPE_WELCOME_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type())){
                    botQuickRepliesTemplateView.setVisibility(View.VISIBLE);
                    botQuickRepliesTemplateView.populateQuickReplyView(payInner.getQuick_replies());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }
                else if(BotResponse.TEMPLATE_TYPE_NOTIFICATIONS.equalsIgnoreCase(payInner.getTemplate_type())){
                    agentTransferTemplateView.setVisibility(View.VISIBLE);
                    agentTransferTemplateView.populateAgentTemplateView(payInner);
                }
                else if(BotResponse.TEMPLATE_TYPE_FEEDBACK.equalsIgnoreCase(payInner.getTemplate_type())){
                    feedbackTemplateView.setVisibility(View.VISIBLE);
                    feedbackTemplateView.populateData(payInner, true);
                }
                else if(BotResponse.TEMPLATE_TYPE_LIST_WIDGET.equalsIgnoreCase(payInner.getTemplate_type())){
                    listWidgetView.setVisibility(View.VISIBLE);
                    listWidgetView.populateListWidgetData(payInner);
                }
                else if(BotResponse.TEMPLATE_TYPE_RESULTS_LIST.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    if(payInner.getTemplate() != null && payInner.getTemplate().getResults() != null
                        && ((payInner.getTemplate().getResults().getFaq() != null && payInner.getTemplate().getResults().getFaq().getData() != null && payInner.getTemplate().getResults().getFaq().getData().size() > 0)
                            || (payInner.getTemplate().getResults().getWeb() != null && payInner.getTemplate().getResults().getWeb().getData() != null && payInner.getTemplate().getResults().getWeb().getData().size() > 0)
                            || (payInner.getTemplate().getResults().getTask() != null && payInner.getTemplate().getResults().getTask().getData() != null && payInner.getTemplate().getResults().getTask().getData().size() > 0)))
                    {
                        resultsTemplateView.setVisibility(VISIBLE);
                        resultsTemplateView.populateResultsTemplateView(payInner);
                    }

                    bubbleTextMediaLayout.populateText(payInner.getText());
                }
                else if(BotResponse.TEMPLATE_TYPE_CARDS.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    cardTemplateView.setVisibility(VISIBLE);
                    cardTemplateView.populateCardTemplateView(payInner);
                    bubbleTextMediaLayout.populateText("Choose the credit card to pay bill");
                }
                else if (BotResponse.TEMPLATE_TYPE_AUTO_FORMS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    if (StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
                        timeStampsTextView.setText("");
                    }
                }
                else if(BotResponse.TEMPLATE_TYPE_HIDDEN_DIALOG.equalsIgnoreCase(payInner.getTemplate_type())){
//                    hiddenDialog.setVisibility(View.VISIBLE);
                    timeStampsTextView.setText("");
                    bubbleTextMediaLayout.populateText("");
                }else if(BotResponse.NARRATOR_TEXT.equalsIgnoreCase(payInner.getTemplate_type())){
//                    bubbleTextMediaLayout.populateText(payInner.getText());
//                    ArrayList<NarratorTextModel> narratorModels = payInner.getNarratorTextModel();
//                    if (narratorModels != null){}

                }
                else if (BotResponse.TEMPLATE_TYPE_CONVERSATION_END.equalsIgnoreCase(payInner.getTemplate_type())) {
                    timeStampsTextView.setText("");
                    timeLineView.setVisibility(VISIBLE);
                    timeLineView.setText(String.format("%s %s", getContext().getString(R.string.conversation_end), DateUtils.getTimeInAmPm(baseBotMessage.getCreatedInMillis())));
                } else if (BotResponse.KA_SWITCH_SKILL.equalsIgnoreCase(payInner.getTemplate_type())) {
                    timeStampsTextView.setText("");
                    timeLineView.setVisibility(VISIBLE);
                    timeLineView.setText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_SHOW_PROGRESS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    timeStampsTextView.setText("");
                } else if (BotResponse.TEMPLATE_TYPE_SESSION_END.equalsIgnoreCase(payInner.getTemplate_type())) {
                    timeStampsTextView.setText("");
                }else if(BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payInner.getTemplate_type())){
                    bubbleTextMediaLayout.populateErrorText(payInner.getText(),payInner.getColor());
                } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (StringUtils.isNullOrEmptyWithTrim(payOuter.getText())) {
                    timeStampsTextView.setText("");
                }

            } else if (BotResponse.COMPONENT_TYPE_MESSAGE.equalsIgnoreCase(payOuter.getType()) && payInner != null) {
                bubbleTextMediaLayout.populateText(payInner.getText());
            } else if(BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType()) && payInner != null){
                bubbleTextMediaLayout.populateText(payInner.getText());
            }else{
                bubbleTextMediaLayout.populateText(payOuter.getText());

            }
            bubbleTextMediaLayout.setClicable(isLastItem);
        }
    }

    void checkBubbleVisibilityAndHideCpv(PayloadInner payloadInner) {
        //setDoDrawBubbleBackground(!(payloadInner.getText() == null || payloadInner.getText().isEmpty()));
        if (!isDoDrawBubbleBackground()) {
            cpvSenderImage.setVisibility(GONE);
        }
    }


    /**
     * onla
     * Layout Manipulation Section
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int childWidthSpec;
        int childHeightSpec;

        /*
         * For TextMedia Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.AT_MOST);
        int fullWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.EXACTLY);
        MeasureUtils.measure(bubbleTextMediaLayout, childWidthSpec, wrapSpec);
        // MeasureUtils.measure(meetingSlotsView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(timeStampsTextView, wrapSpec, wrapSpec);
        MeasureUtils.measure(timeLineView, fullWidthSpec, wrapSpec);

        /*
         * For Sender icon [CPV]
         */
        float cpvSenderImageDimen = dp1 * 21;
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) cpvSenderImageDimen, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) cpvSenderImageDimen, MeasureSpec.EXACTLY);
        cpvSenderImage.setDimens(cpvSenderImageDimen, cpvSenderImageDimen);
        MeasureUtils.measure(cpvSenderImage, childWidthSpec, childHeightSpec);

        /*
         * For List Templates
         */
        MeasureUtils.measure(botListTemplateView, wrapSpec, wrapSpec);

        /*
         * For List View Templates
         */
//        MeasureUtils.measure(botListViewTemplateView, wrapSpec, wrapSpec);

        /*For calendar events*/

        /**
         * For PieChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(botPieChartView, childWidthSpec, childHeightSpec);

        /*
         * For List View Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(botListViewTemplateView, childWidthSpec, wrapSpec);


        /**
         * For TableViev
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (tableHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(tableView, childWidthSpec,wrapSpec);

        /**
         * For Responsive TableViev
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (respTableViewHeight), MeasureSpec.EXACTLY);
//        MeasureUtils.measure(responsiveTableView, childWidthSpec,childHeightSpec);
        MeasureUtils.measure(responsiveTableView, childWidthSpec,wrapSpec);

        /**
         * For Responsive TableViev
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (respTableViewHeight), MeasureSpec.EXACTLY);
//        MeasureUtils.measure(responsiveTableView, childWidthSpec,childHeightSpec);
        MeasureUtils.measure(botFormTemplateView, childWidthSpec,wrapSpec);

        /**
         * For Bot Form TableViev
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (respTableViewHeight), MeasureSpec.EXACTLY);
//        MeasureUtils.measure(responsiveExpandTableView, childWidthSpec,childHeightSpec);
        MeasureUtils.measure(responsiveExpandTableView, childWidthSpec,wrapSpec);

        /**
         * for line chart
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) lineHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(lineChartView, childWidthSpec, childHeightSpec);

        /**
         * For BarChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (lineHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(barChartView, childWidthSpec, childHeightSpec);

        /**
         * For Horizontal BarChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (lineHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(horizontalBarChartView, childWidthSpec,childHeightSpec);

        /**
         * For stacked BarChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (lineHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(stackedBarChatView, childWidthSpec,childHeightSpec);

        /*
         * For CarouselView
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth, MeasureSpec.EXACTLY);
        //  childHeightSpec = MeasureSpec.makeMeasureSpec((int) (carouselViewHeight) + BUBBLE_CONTENT_BOTTOM_MARGIN , MeasureSpec.EXACTLY);
        /*
         * For Button Templates
         */

        /*
         * For Table List Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(botTableListTemplateView, childWidthSpec, wrapSpec);

        /*
         * For Agent Transfer Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(agentTransferTemplateView, childWidthSpec, wrapSpec);

        /*
         * For Table List Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(botQuickRepliesTemplateView, childWidthSpec, wrapSpec);

        /*
         * For Widget List Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(listWidgetView, childWidthSpec, wrapSpec);

        /*
         * For Results List Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 30 * (int) dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(resultsTemplateView, childWidthSpec, wrapSpec);

        /*
         * For Results List Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(cardTemplateView, childWidthSpec, wrapSpec);

        MeasureUtils.measure(multiSelectView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(botButtonView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(botCarouselView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(feedbackTemplateView, childWidthSpec, wrapSpec);
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
        int minimumTop = getPaddingTop() + BUBBLE_TOP_BORDER + BUBBLE_SEPARATION_DISTANCE;
        left = BUBBLE_LEFT_BORDER;
        top = minimumTop;


        int bubbleTextMediaLayouMarginLeft = BUBBLE_CONTENT_LEFT_MARGIN;
        int bubbleTextMediaLayouMarginTop = BUBBLE_CONTENT_TOP_MARGIN + BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT;

        /*
         * For TextMedia Layout
         */
        left += bubbleTextMediaLayouMarginLeft;
        top = top + bubbleTextMediaLayouMarginTop;
        LayoutUtils.layoutChild(bubbleTextMediaLayout, left, top);
        arrayList.add(bubbleTextMediaLayout.getBottom());

        top = bubbleTextMediaLayout.getMeasuredHeight() != 0 ? bubbleTextMediaLayout.getBottom() + (int) dp15 : minimumTop;
        left = bubbleTextMediaLayout.getLeft() - (BubbleUI ? BUBBLE_CONTENT_LEFT_MARGIN : 0);

        layoutView(botButtonView, top, left, arrayList);
        layoutView(multiSelectView, top, left, arrayList);
        layoutView(botCarouselView, top, left, arrayList);
        layoutView(botListTemplateView, top, left, arrayList);
        layoutView(botPieChartView, top, left, arrayList);
        layoutView(tableView, top, left, arrayList);
        layoutView(responsiveTableView, top, left, arrayList);
        layoutView(responsiveExpandTableView, top, left, arrayList);
        layoutView(lineChartView, top, left, arrayList);
        layoutView(barChartView, top, left, arrayList);
        layoutView(horizontalBarChartView, top, left, arrayList);
        layoutView(stackedBarChatView, top, left, arrayList);
        layoutView(botFormTemplateView, top, left, arrayList);
        layoutView(botListViewTemplateView, top, left, arrayList);
        layoutView(botTableListTemplateView, top, left, arrayList);
        layoutView(botQuickRepliesTemplateView, top, left, arrayList);
        layoutView(agentTransferTemplateView, top, left, arrayList);
        layoutView(feedbackTemplateView, top, left, arrayList);
        layoutView(listWidgetView, top, left, arrayList);
        layoutView(resultsTemplateView, top, left, arrayList);
        layoutView(cardTemplateView, top, left, arrayList);

        left = bubbleTextMediaLayout.getLeft();
        top = Collections.max(arrayList);
        LayoutUtils.layoutChild(cpvSenderImage, left, top);
        if (cpvSenderImage.getMeasuredWidth() > 0) {
            left = cpvSenderImage.getRight() + (int) (9 * dp1);
            top = top + (int) (1 * dp1);
        }


        LayoutUtils.layoutChild(timeStampsTextView, left, top);
        LayoutUtils.layoutChild(timeLineView, 0, top);

        botCarouselView.bringToFront();
        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...

    }

    private void layoutView(View view, int top, int left, ArrayList<Integer> arrayList) {
        if (view.getVisibility() == VISIBLE) {
            LayoutUtils.layoutChild(view, left, top);
            arrayList.add(view.getBottom());
        }
    }
}
