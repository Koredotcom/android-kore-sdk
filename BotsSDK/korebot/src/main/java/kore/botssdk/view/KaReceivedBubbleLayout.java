package kore.botssdk.view;

import static kore.botssdk.net.SDKConfiguration.BubbleColors.BubbleUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.charts.data.PieEntry;
import kore.botssdk.models.AnnoucementResModel;
import kore.botssdk.models.AttendeeSlotTemplateModel;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotPieChartElementModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.ContactInfoModel;
import kore.botssdk.models.KaFileLookupModel;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.models.KoraSummaryHelpModel;
import kore.botssdk.models.MeetingConfirmationModel;
import kore.botssdk.models.MeetingTemplateModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.TaskTemplateResponse;
import kore.botssdk.models.WelcomeSummaryModel;
import kore.botssdk.net.SDKConfiguration;
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
public class KaReceivedBubbleLayout extends KaBaseBubbleLayout {

    CircularProfileView cpvSenderImage;
    int carouselViewHeight, pieViewHeight, lineHeight, respTableViewHeight;
    final ArrayList<Integer> arrayList = new ArrayList<>();

    public KaReceivedBubbleLayout(Context context) {
        super(context);
        init();
    }

    public KaReceivedBubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KaReceivedBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public KaReceivedBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        cpvSenderImage = findViewById(R.id.cpvSenderImage);
    }

    @Override
    void initializeBubbleBorderPass1() {

        BUBBLE_LEFT_PROFILE_PIC = cpvSenderImage.getMeasuredWidth();
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? (int) (10 * dp1) : 0;
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = 0;
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
        maxContentDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + BUBBLE_LEFT_PROFILE_PIC + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN +
                Collections.max(Arrays.asList(textMediaDimen[0], botCarouselView.getMeasuredWidth()
                        , verticalListView.getMeasuredWidth()
                        , timeStampsTextView.getMeasuredWidth()
                        , timeLineView.getMeasuredWidth()
                        , meetingSlotsView.getMeasuredWidth()
                        , multiSelectView.getMeasuredWidth()
                        , attendeeSlotSelectionView.getMeasuredWidth()
                        , meetingConfirmationView.getMeasuredWidth()
                        , botButtonView.getMeasuredWidth()
                        , tableView.getMeasuredWidth()
                        , responsiveTableView.getMeasuredWidth()
                        , responsiveExpandTableView.getMeasuredWidth()
                        , lineChartView.getMeasuredWidth()
                        , barChartView.getMeasuredWidth()
                        , horizontalBarChartView.getMeasuredWidth()
                        , stackedBarChatView.getMeasuredWidth()
                        , koraCarouselView.getMeasuredWidth()
                        , botListViewTemplateView.getMeasuredWidth()
                        , botCustomTableView.getMeasuredWidth()
                        , linkTemplateView.getMeasuredWidth()
                        , botListTemplateView.getMeasuredWidth()
                        , contactInfoView.getMeasuredWidth()
                        , botPieChartView.getMeasuredWidth()
                        , welcomeSummaryView.getMeasuredWidth()
                        , botFormTemplateView.getMeasuredWidth()
                        , botTableListTemplateView.getMeasuredWidth()
                        , botQuickRepliesTemplateView.getMeasuredWidth()
                        , agentTransferTemplateView.getMeasuredWidth()
                        , feedbackTemplateView.getMeasuredWidth()
                        , listWidgetView.getMeasuredWidth()
                        , botDropDownTemplateView.getMeasuredWidth()
                        , botListWidgetTemplateView.getMeasuredWidth()
                        , imageTemplateView.getMeasuredWidth()
                        , bankingFeedbackTemplateView.getMeasuredWidth()
                        , koraSummaryHelpView.getMeasuredWidth()
                        , universalSearchView.getMeasuredWidth()
                        , botContactTemplateView.getMeasuredWidth()
                        , resultsTemplateView.getMeasuredWidth()
                        , advancedListTemplateView.getMeasuredWidth()
                        , botButtonLinkTemplateView.getMeasuredWidth()
                        , botBeneficiaryTemplateView.getMeasuredWidth()
                        , pdfDownloadView.getMeasuredWidth()
                        , buttonDeepLinkTemplateView.getMeasuredWidth()
                        , cardTemplateView.getMeasuredWidth()
                        , emptyTemplateView.getMeasuredWidth()
                        //Add new template above
                )) + BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;


        // headerLayoutDimen[1] = headerLayout.getMeasuredHeight();
        maxBubbleDimen[0] = maxContentDimen[0];

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +textMediaDimen[1]
                + botCarouselView.getMeasuredHeight()
                + meetingSlotsView.getMeasuredHeight()
                + multiSelectView.getMeasuredHeight()
                + verticalListView.getMeasuredHeight()
                + cpvSenderImage.getMeasuredHeight()
                + timeLineView.getMeasuredHeight()
                + advancedListTemplateView.getMeasuredHeight()
                + meetingConfirmationView.getMeasuredHeight()
                + attendeeSlotSelectionView.getMeasuredHeight()
                + botPieChartView.getMeasuredHeight()
                + tableView.getMeasuredHeight()
                + responsiveTableView.getMeasuredHeight()
                + responsiveExpandTableView.getMeasuredHeight()
                + lineChartView.getMeasuredHeight()
                + barChartView.getMeasuredHeight()
                + horizontalBarChartView.getMeasuredHeight()
                + stackedBarChatView.getMeasuredHeight()
                + koraCarouselView.getMeasuredHeight()
                + botCustomTableView.getMeasuredHeight()
                + linkTemplateView.getMeasuredHeight()
                + botListViewTemplateView.getMeasuredHeight()
                + botTableListTemplateView.getMeasuredHeight()
                + botQuickRepliesTemplateView.getMeasuredHeight()
                + agentTransferTemplateView.getMeasuredHeight()
                + feedbackTemplateView.getMeasuredHeight()
                + botContactTemplateView.getMeasuredHeight()
                + botButtonView.getMeasuredHeight()
                + botListTemplateView.getMeasuredHeight()
                + contactInfoView.getMeasuredHeight()
                + listWidgetView.getMeasuredHeight()
                + botListWidgetTemplateView.getMeasuredHeight()
                + welcomeSummaryView.getMeasuredHeight()
                + universalSearchView.getMeasuredHeight()
                + koraSummaryHelpView.getMeasuredHeight()
                + botFormTemplateView.getMeasuredHeight()
                + botDropDownTemplateView.getMeasuredHeight()
                + imageTemplateView.getMeasuredHeight()
                + bankingFeedbackTemplateView.getMeasuredHeight()
                + resultsTemplateView.getMeasuredHeight()
                + pdfDownloadView.getMeasuredHeight()
                + botButtonLinkTemplateView.getMeasuredHeight()
                + botBeneficiaryTemplateView.getMeasuredHeight()
                + buttonDeepLinkTemplateView.getMeasuredHeight()
                + cardTemplateView.getMeasuredHeight()
                + emptyTemplateView.getMeasuredHeight()
                //Add new template above
                + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER + (int) (botButtonView.getMeasuredHeight() != 0 ||
                meetingSlotsView.getMeasuredHeight() != 0 ? dp2 : 0);
        if (bubbleTextMediaLayout.getMeasuredHeight() != 0 && isTemplatePresent()) {
            maxBubbleDimen[1] = maxBubbleDimen[1] + (int) dp15;
        }
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN + textMediaDimen[1]
                + botCarouselView.getMeasuredHeight()
                + verticalListView.getMeasuredHeight()
                + cpvSenderImage.getMeasuredHeight()
                + timeLineView.getMeasuredHeight()
                + botButtonView.getMeasuredHeight()
                + botListTemplateView.getMeasuredHeight()
                + botPieChartView.getMeasuredHeight()
                + tableView.getMeasuredHeight()
                + responsiveTableView.getMeasuredHeight()
                + responsiveExpandTableView.getMeasuredHeight()
                + lineChartView.getMeasuredHeight()
                + botCustomTableView.getMeasuredHeight()
                + linkTemplateView.getMeasuredHeight()
                + advancedListTemplateView.getMeasuredHeight()
                + barChartView.getMeasuredHeight()
                + horizontalBarChartView.getMeasuredHeight()
                + stackedBarChatView.getMeasuredHeight()
                + koraCarouselView.getMeasuredHeight()
                + agentTransferTemplateView.getMeasuredHeight()
                + botContactTemplateView.getMeasuredHeight()
                + meetingSlotsView.getMeasuredHeight()
                + attendeeSlotSelectionView.getMeasuredHeight()
                + multiSelectView.getMeasuredHeight()
                + botFormTemplateView.getMeasuredHeight()
                + botListViewTemplateView.getMeasuredHeight()
                + imageTemplateView.getMeasuredHeight()
                + feedbackTemplateView.getMeasuredHeight()
                + meetingConfirmationView.getMeasuredHeight()
                + contactInfoView.getMeasuredHeight()
                + welcomeSummaryView.getMeasuredHeight()
                + botTableListTemplateView.getMeasuredHeight()
                + botQuickRepliesTemplateView.getMeasuredHeight()
                + botListWidgetTemplateView.getMeasuredHeight()
                + feedbackTemplateView.getMeasuredHeight()
                + listWidgetView.getMeasuredHeight()
                + universalSearchView.getMeasuredHeight()
                + koraSummaryHelpView.getMeasuredHeight()
                + botDropDownTemplateView.getMeasuredHeight()
                + resultsTemplateView.getMeasuredHeight()
                + pdfDownloadView.getMeasuredHeight()
                + botButtonLinkTemplateView.getMeasuredHeight()
                + botBeneficiaryTemplateView.getMeasuredHeight()
                + buttonDeepLinkTemplateView.getMeasuredHeight()
                + cardTemplateView.getMeasuredHeight()
                + emptyTemplateView.getMeasuredHeight()
                //Add new template above
                + BUBBLE_CONTENT_BOTTOM_MARGIN;

        if (bubbleTextMediaLayout.getMeasuredHeight() != 0 && isTemplatePresent()) {
            maxBubbleDimen[1] = maxBubbleDimen[1] + (int) dp15;
        }
    }

    private boolean isTemplatePresent() {
        return contactInfoView.getMeasuredHeight() > 0
                || botListTemplateView.getMeasuredHeight() > 0
                || botButtonView.getMeasuredHeight() > 0
                || botCarouselView.getMeasuredHeight() > 0
                || attendeeSlotSelectionView.getMeasuredHeight() > 0
                || meetingConfirmationView.getMeasuredHeight() > 0
                || verticalListView.getMeasuredHeight() > 0
                || advancedListTemplateView.getMeasuredHeight() > 0
                || meetingSlotsView.getMeasuredHeight() > 0
                || botPieChartView.getMeasuredHeight() > 0
                || botFormTemplateView.getMeasuredHeight() > 0
                || botListViewTemplateView.getMeasuredHeight() > 0
                || botTableListTemplateView.getMeasuredHeight() > 0
                || botCustomTableView.getMeasuredHeight()  > 0
                || lineChartView.getMeasuredHeight() > 0
                || barChartView.getMeasuredHeight() > 0
                || horizontalBarChartView.getMeasuredHeight() > 0
                || stackedBarChatView.getMeasuredHeight() > 0
                || koraCarouselView.getMeasuredHeight()>0
                || listWidgetView.getMeasuredHeight() > 0
                || linkTemplateView.getMeasuredHeight() > 0
                || tableView.getMeasuredHeight() > 0
                || responsiveTableView.getMeasuredHeight() > 0
                || responsiveExpandTableView.getMeasuredHeight() > 0
                || agentTransferTemplateView.getMeasuredHeight() > 0
                || feedbackTemplateView.getMeasuredHeight() > 0
                || botListWidgetTemplateView.getMeasuredHeight() > 0
                || welcomeSummaryView.getMeasuredHeight() > 0
                || koraSummaryHelpView.getMeasuredHeight()>0
                || universalSearchView.getMeasuredHeight()>0
                || multiSelectView.getMeasuredHeight() > 0
                || botQuickRepliesTemplateView.getMeasuredHeight() > 0
                || botDropDownTemplateView.getMeasuredHeight() > 0
                || imageTemplateView.getMeasuredHeight() > 0
                || bankingFeedbackTemplateView.getMeasuredHeight() > 0
                || botContactTemplateView.getMeasuredHeight() > 0
                || resultsTemplateView.getMeasuredHeight() > 0
                || pdfDownloadView.getMeasuredHeight() > 0
                || botButtonLinkTemplateView.getMeasuredHeight() > 0
                || botBeneficiaryTemplateView.getMeasuredHeight() > 0
                || buttonDeepLinkTemplateView.getMeasuredHeight() > 0
                || cardTemplateView.getMeasuredHeight() > 0
                || emptyTemplateView.getMeasuredHeight() > 0
                ;
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
        verticalListView.prepareDataSetAndPopulate(null, null, false);
        verticalListView.setVisibility(GONE);
        meetingSlotsView.populateData(null, false);
        meetingSlotsView.setVisibility(GONE);
        botContactTemplateView.setVisibility(View.GONE);
        botContactTemplateView.populateContactTemplateView(null,"");
        multiSelectView.populateData(null,false);
        multiSelectView.setVisibility(GONE);
        attendeeSlotSelectionView.populateData(null, false);
        attendeeSlotSelectionView.setVisibility(GONE);
        meetingConfirmationView.populateData(null);
        meetingConfirmationView.setVisibility(GONE);
        contactInfoView.populateData(null);
        contactInfoView.setVisibility(GONE);
        welcomeSummaryView.populateData(null, false);
        welcomeSummaryView.setVisibility(GONE);
        universalSearchView.populateData(null);
        universalSearchView.setVisibility(GONE);
        koraSummaryHelpView.populateData(null);
        koraSummaryHelpView.setVisibility(GONE);
        botListTemplateView.setVisibility(View.GONE);
        botListTemplateView.populateListTemplateView(null, null);
        botPieChartView.setVisibility(View.GONE);
        tableView.setData(null);
        tableView.setVisibility(View.GONE);
        botCustomTableView.setData(null);
        botCustomTableView.setVisibility(View.GONE);
        responsiveTableView.setData(null);
        responsiveTableView.setVisibility(View.GONE);
        responsiveExpandTableView.setData(null);
        responsiveExpandTableView.setVisibility(View.GONE);
        responsiveExpandTableView.setData(null);
        lineChartView.setVisibility(GONE);
        barChartView.setVisibility(GONE);
        horizontalBarChartView.setVisibility(GONE);
        stackedBarChatView.setVisibility(GONE);
        koraCarouselView.populateMiniTable(null,null);
        koraCarouselView.setVisibility(View.GONE);
        timeLineView.setVisibility(GONE);
        timeLineView.setText("");

        botFormTemplateView.populateData(null,false);
        botFormTemplateView.setVisibility(GONE);
        botListViewTemplateView.setVisibility(View.GONE);
        botListViewTemplateView.populateListTemplateView(null, null,null, null, 0, null);
        botListWidgetTemplateView.setVisibility(View.GONE);
        botListWidgetTemplateView.populateListWidgetTemplateView(null, null,null, null, 0, null);
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
        botDropDownTemplateView.setVisibility(GONE);
        botDropDownTemplateView.populateData(null);
        imageTemplateView.setVisibility(GONE);
        imageTemplateView.populateData(null, null);
        bankingFeedbackTemplateView.setVisibility(GONE);
        bankingFeedbackTemplateView.populateData(null, false);
        linkTemplateView.setVisibility(GONE);
        linkTemplateView.populatePdfView(null);
        advancedListTemplateView.setVisibility(GONE);
        advancedListTemplateView.populateAdvancedListTemplateView(null);
        resultsTemplateView.populateResultsTemplateView(null);
        resultsTemplateView.setVisibility(GONE);
        pdfDownloadView.populatePdfView(null);
        pdfDownloadView.setVisibility(GONE);
        botButtonLinkTemplateView.populateButtonList(null, 0);
        botButtonLinkTemplateView.setVisibility(View.GONE);
        botBeneficiaryTemplateView.setVisibility(GONE);
        botBeneficiaryTemplateView.populateListTemplateView(null,null, null, 0, null, null);
        buttonDeepLinkTemplateView.populateButtonDeepLinkView(null, false);
        buttonDeepLinkTemplateView.setVisibility(GONE);
        cardTemplateView.populateCardsView(null);
        cardTemplateView.setVisibility(GONE);
        emptyTemplateView.setVisibility(GONE);
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
        if(SDKConfiguration.BubbleColors.showIcon)
        {
            if(!StringUtils.isNullOrEmpty(SDKConfiguration.BubbleColors.getIcon_url()))
                cpvSenderImage.populateLayout(" ", null, SDKConfiguration.BubbleColors.getIcon_url(), null, SDKConfiguration.BubbleColors.getIcon(), R.color.white, true, BUBBLE_LEFT_PROFILE_PIC, BUBBLE_LEFT_PROFILE_PIC);
            else
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
                if(SDKConfiguration.getCustomTemplateView() != null && SDKConfiguration.getCustomTemplateView().size() > 0
                        && SDKConfiguration.getCustomTemplateView().containsKey(payInner.getTemplate_type()))
                {
                    emptyTemplateView.setVisibility(VISIBLE);
                    CustomTemplateView customTemplateView1 = ((CustomTemplateView) Objects.requireNonNull(SDKConfiguration.getCustomTemplateView().get(payInner.getTemplate_type()))).getNewInstance();

                    if(customTemplateView1 != null)
                    {
                        customTemplateView1.setComposeFooterInterface(composeFooterInterface);
                        customTemplateView1.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);

                        customTemplateView1.populateTemplate(payInner, isLastItem);
                        emptyTemplateView.addView(customTemplateView1);
                    }
                }
                else if (BotResponse.TEMPLATE_TYPE_BUTTON.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    if(!payInner.isUrl_present())
                    {
                        botButtonView.setVisibility(View.VISIBLE);
                        botButtonView.setRestrictedMaxWidth(screenWidth - 28 * dp1 );
                        botButtonView.populateButtonList(payInner.getButtons(),isLastItem);
                        bubbleTextMediaLayout.populateText(payInner.getText());
                    }
                    else
                    {
                        botButtonLinkTemplateView.setVisibility(View.VISIBLE);
                        botButtonLinkTemplateView.populateButtonList(payInner, 0);
                    }
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

                }else if (BotResponse.TEMPLATE_TYPE_TABLE.equalsIgnoreCase(payInner.getTemplate_type())                           )
                {
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
                else if(BotResponse.CUSTOM_TABLE_TEMPLATE.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    botCustomTableView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    botCustomTableView.setData(payInner);
                }
                else if(BotResponse.TEMPLATE_TYPE_MINITABLE.equalsIgnoreCase(payInner.getTemplate_type())){
                    koraCarouselView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    koraCarouselView.populateMiniTable(payInner.getTemplate_type(), payInner);
                } else if(BotResponse.TEMPLATE_TYPE_MULTI_SELECT.equalsIgnoreCase(payInner.getTemplate_type())){
                    multiSelectView.setVisibility(View.VISIBLE);
                    multiSelectView.populateData(payInner, isLastItem);

                    if(!StringUtils.isNullOrEmpty(payInner.getText()))
                        bubbleTextMediaLayout.populateText(payInner.getText());
                    else if(!StringUtils.isNullOrEmpty(payInner.getHeading()))
                        bubbleTextMediaLayout.populateText(payInner.getHeading());

                }else if(BotResponse.ADVANCED_LIST_TEMPLATE.equalsIgnoreCase(payInner.getTemplate_type())){
                    advancedListTemplateView.setVisibility(View.VISIBLE);
                    advancedListTemplateView.populateAdvancedListTemplateView(payInner);
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
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }
                else if(BotResponse.TEMPLATE_TYPE_LIST_WIDGET_2.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    botListWidgetTemplateView.setVisibility(View.VISIBLE);
                    botListWidgetTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    botListWidgetTemplateView.populateListWidgetTemplateView(payInner.getTitle(), payInner.getMoreData(), payInner.getListWidgetElements(), payInner.getButtons(), payInner.getMoreCount(), payInner.getSeeMore());
                }
                else if(BotResponse.TEMPLATE_DROPDOWN.equalsIgnoreCase(payInner.getTemplate_type())){
                    botDropDownTemplateView.setVisibility(View.VISIBLE);
                    botDropDownTemplateView.populateData(payInner);
                }
                else if (BotResponse.TEMPLATE_TYPE_KORA_CAROUSAL.equals(payInner.getTemplate_type()) ) {
                    ArrayList<KnowledgeDetailModel> knowledgeData = payInner.getKnowledgeDetailModels();
                    verticalListView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    verticalListView.prepareDataSetAndPopulate(knowledgeData, payInner.getTemplate_type(), isLastItem);
                }else if (BotResponse.TEMPLATE_TYPE_KORA_ANNOUNCEMENT_CAROUSAL.equals(payInner.getTemplate_type()) ) {
                    //announcement carousal
                    List<AnnoucementResModel> annoucementResModelsData = payInner.getAnnouncementResModels();
                    verticalListView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    verticalListView.prepareDataSetAndPopulate((ArrayList) annoucementResModelsData, payInner.getTemplate_type(), isLastItem);
                }
                else if(BotResponse.CONTACT_CARD_TEMPLATE.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    botContactTemplateView.setVisibility(View.VISIBLE);
                    botContactTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    botContactTemplateView.populateContactTemplateView(payInner.getContactCardModel(), payInner.getTitle());
                }
                else if(BotResponse.CARD_TEMPLATE.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    cardTemplateView.setVisibility(View.VISIBLE);
                    cardTemplateView.populateCardsView(payInner.getCardsModel());
                }
                else if (BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL.equalsIgnoreCase(payInner.getTemplate_type())) {
                    verticalListView.setVisibility(View.VISIBLE);
                    if (payInner.getKoraSearchResultsModel() != null)
                        verticalListView.prepareDataSetAndPopulate(payInner.getKoraSearchResultsModel().get(0).getEmails(), BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL, isLastItem);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_AUTO_FORMS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    if (StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
                        timeStampsTextView.setText("");
                    }
                } else if (BotResponse.TEMPLATE_TYPE_SLOT_PICKER.equalsIgnoreCase(payInner.getTemplate_type())) {
                    ArrayList<MeetingTemplateModel> meetingTemplateModels = payInner.getMeetingTemplateModels();
                    if (meetingTemplateModels != null && meetingTemplateModels.size() > 0) {
                        meetingSlotsView.setVisibility(View.VISIBLE);
                        meetingSlotsView.populateData(meetingTemplateModels.get(0), isLastItem);
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_MEETING_CONFIRM.equalsIgnoreCase(payInner.getTemplate_type())) {
                    ArrayList<MeetingConfirmationModel> meetingTemplateModels = payInner.getMeetingConfirmationModels();
                    if (meetingTemplateModels != null && meetingTemplateModels.size() > 0) {
                        meetingConfirmationView.setVisibility(View.VISIBLE);
                        meetingConfirmationView.populateData(meetingTemplateModels.get(0));
                        // BUBBLE_CONTENT_RIGHT_MARGIN = 0;
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_TASK_VIEW.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TASK_FULLVIEW.equalsIgnoreCase(payInner.getTemplate_type())) {
                    ArrayList<TaskTemplateResponse> taskTemplateModels = payInner.getTaskTemplateModels();
                    if (taskTemplateModels != null && taskTemplateModels.size() > 0) {
                        TaskTemplateResponse taskTemplateModel = taskTemplateModels.get(0);
                        verticalListView.setVisibility(VISIBLE);
                        verticalListView.prepareDataToTasks(taskTemplateModel, BotResponse.TEMPLATE_TYPE_TASK_VIEW, isLastItem && (taskTemplateModel.getButtons() != null && taskTemplateModel.getButtons().size() > 0));
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_ATTENDEE_SLOTS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    ArrayList<AttendeeSlotTemplateModel> meetingTemplateModels = payInner.getAttendeeSlotTemplateModels();
                    if (meetingTemplateModels != null && meetingTemplateModels.size() > 0) {
                        attendeeSlotSelectionView.setVisibility(View.VISIBLE);
                        attendeeSlotSelectionView.populateData(meetingTemplateModels.get(0), isLastItem);
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TYPE_CANCEL_EVENT.equalsIgnoreCase(payInner.getTemplate_type())) {
                    ArrayList<CalEventsTemplateModel> calList = payInner.getCalEventsTemplateModels();
                    if (calList != null && !calList.isEmpty()) {
                        verticalListView.setVisibility(View.VISIBLE);
                        verticalListView.setCursorDuration(payInner.getCursor());
                        verticalListView.prepareDataSetAndPopulate(calList, payInner.getTemplate_type(), isLastItem);
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_FILES_LOOKUP.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    verticalListView.setVisibility(View.VISIBLE);
                    ArrayList<KaFileLookupModel> fileList = payInner.getFileLookupModels();
                    if (fileList != null)
                        verticalListView.prepareDataSetAndPopulate(fileList, BotResponse.TEMPLATE_TYPE_FILES_LOOKUP, isLastItem);
                } else if (BotResponse.KA_CONTACT_VIEW.equalsIgnoreCase(payInner.getTemplate_type())) {
                    contactInfoView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    ArrayList<ContactInfoModel> contactInfoModels = payInner.getContactInfoModels();
                    if (contactInfoModels != null && contactInfoModels.size() > 0)
                        contactInfoView.populateData(contactInfoModels.get(0));
                }else if(BotResponse.WELCOME_SUMMARY_VIEW.equalsIgnoreCase(payInner.getTemplate_type())){
                    welcomeSummaryView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    ArrayList<WelcomeSummaryModel> welcomeSummaryModels = payInner.getWelcomeSummaryModel();
                    if (welcomeSummaryModels != null && welcomeSummaryModels.size()>0)
                        welcomeSummaryView.populateData(welcomeSummaryModels.get(0),isLastItem);

                }else if(BotResponse.TEMPLATE_TYPE_UNIVERSAL_SEARCH.equalsIgnoreCase(payInner.getTemplate_type())){
                    universalSearchView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    universalSearchView.populateData(payInner.getUniversalSearchModels());
                }else if(BotResponse.KORA_SUMMARY_HELP_VIEW.equalsIgnoreCase(payInner.getTemplate_type())){
                    koraSummaryHelpView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    ArrayList<KoraSummaryHelpModel> summaryModels = payInner.getKoraSummaryHelpModel();
                    if (summaryModels != null && summaryModels.size() > 0)
                        koraSummaryHelpView.populateData(summaryModels.get(0));
                }else if(BotResponse.TEMPLATE_TYPE_HIDDEN_DIALOG.equalsIgnoreCase(payInner.getTemplate_type())){
//                    hiddenDialog.setVisibility(View.VISIBLE);
                    timeStampsTextView.setText("");
                    bubbleTextMediaLayout.populateText("");
                }else if(BotResponse.TEMPLATE_BANKING_FEEDBACK.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    bankingFeedbackTemplateView.setVisibility(View.VISIBLE);
                    bankingFeedbackTemplateView.populateData(payInner, isLastItem);
                }
                else if (BotResponse.TEMPLATE_TYPE_CONVERSATION_END.equalsIgnoreCase(payInner.getTemplate_type())) {
                    timeStampsTextView.setText("");
                    timeLineView.setVisibility(VISIBLE);
                    timeLineView.setText(String.format("%s %s", getContext().getString(R.string.conversation_end), DateUtils.getTimeInAmPm(baseBotMessage.getCreatedInMillis())));
                } else if (BotResponse.KA_SWITCH_SKILL.equalsIgnoreCase(payInner.getTemplate_type())) {
                    timeStampsTextView.setText("");
                    timeLineView.setVisibility(VISIBLE);
                    timeLineView.setText(payInner.getText());
                }
                else if(BotResponse.TEMPLATE_TYPE_RESULTS_LIST.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    resultsTemplateView.setVisibility(VISIBLE);
                    resultsTemplateView.populateResultsTemplateView(payInner);
                    bubbleTextMediaLayout.populateText("");
                }
                else if(BotResponse.TEMPLATE_PDF_DOWNLOAD.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    pdfDownloadView.setVisibility(View.VISIBLE);
                    pdfDownloadView.populatePdfView(payInner.getPdfDownloadModels());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }
                else if(BotResponse.TEMPLATE_BUTTON_LINK.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    buttonDeepLinkTemplateView.setVisibility(View.VISIBLE);
                    buttonDeepLinkTemplateView.populateButtonDeepLinkView(payInner, isLastItem);
                    bubbleTextMediaLayout.populateText("");
                }
                else if(BotResponse.TEMPLATE_BENEFICIARY.equalsIgnoreCase(payInner.getTemplate_type())){
                    botBeneficiaryTemplateView.setVisibility(View.VISIBLE);
                    botBeneficiaryTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    botBeneficiaryTemplateView.populateListTemplateView(payInner.getMoreData(), payInner.getBotBeneficiaryModels(), payInner.getButtons(), payInner.getMoreCount(), payInner.getSeeMore(), payInner);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }
                else if (BotResponse.TEMPLATE_TYPE_SHOW_PROGRESS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    timeStampsTextView.setText("");
                } else if (BotResponse.TEMPLATE_TYPE_SESSION_END.equalsIgnoreCase(payInner.getTemplate_type())) {
                    timeStampsTextView.setText("");
                }else if(BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payInner.getTemplate_type()))
                {
                    bubbleTextMediaLayout.populateErrorText(payInner.getText(),payInner.getColor());
                } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getText()))
                {
                    if(!BotResponse.TEMPLATE_TYPE_DATE.equalsIgnoreCase(payInner.getTemplate_type()))
                        bubbleTextMediaLayout.populateText(payInner.getText());
                    else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getText_message()))
                        bubbleTextMediaLayout.populateText(payInner.getText_message());
                }
                else if(!StringUtils.isNullOrEmptyWithTrim(payInner.getTemplate_type()))
                    bubbleTextMediaLayout.populateText(payInner.getTemplate_type());
                else if (StringUtils.isNullOrEmptyWithTrim(payOuter.getText())) {
                    timeStampsTextView.setText("");
                }

            }
            else if (BotResponse.COMPONENT_TYPE_MESSAGE.equalsIgnoreCase(payOuter.getType()) && payInner != null) {

                if(!StringUtils.isNullOrEmpty(payInner.getVideoUrl()))
                {
                    imageTemplateView.setVisibility(View.VISIBLE);
                    imageTemplateView.populateData(payInner, BotResponse.COMPONENT_TYPE_VIDEO);
                }
                else if(!StringUtils.isNullOrEmpty(payInner.getAudioUrl()))
                {
                    imageTemplateView.setVisibility(View.VISIBLE);
                    imageTemplateView.populateData(payInner, BotResponse.COMPONENT_TYPE_AUDIO);
                }
                else if(!StringUtils.isNullOrEmpty(payInner.getText()))
                    bubbleTextMediaLayout.populateText(payInner.getText());

            }
            else if(BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType()) && payInner != null){
                bubbleTextMediaLayout.populateText(payInner.getText());
            }
            else if((BotResponse.COMPONENT_TYPE_IMAGE.equalsIgnoreCase(payOuter.getType())
                || BotResponse.COMPONENT_TYPE_AUDIO.equalsIgnoreCase(payOuter.getType())
                || BotResponse.COMPONENT_TYPE_VIDEO.equalsIgnoreCase(payOuter.getType())) && payInner != null)
            {
                imageTemplateView.setVisibility(View.VISIBLE);
                imageTemplateView.populateData(payInner, payOuter.getType());
            }
            else if(SDKConfiguration.getCustomTemplateView() != null && SDKConfiguration.getCustomTemplateView().size() > 0
                    && SDKConfiguration.getCustomTemplateView().containsKey(payOuter.getType()))
            {
                emptyTemplateView.setVisibility(VISIBLE);
                CustomTemplateView customTemplateView1 = ((CustomTemplateView) Objects.requireNonNull(SDKConfiguration.getCustomTemplateView().get(payOuter.getType()))).getNewInstance();

                if(customTemplateView1 != null)
                {
                    customTemplateView1.populateTemplate(payInner, isLastItem);
                    emptyTemplateView.addView(customTemplateView1);
                }
            }
            else if(BotResponse.COMPONENT_TYPE_LINK.equalsIgnoreCase(payOuter.getType()) && payInner != null)
            {
                linkTemplateView.setVisibility(View.VISIBLE);
                linkTemplateView.populatePdfView(payInner);
            }
            else
            {
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
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int childWidthSpec;
        int childHeightSpec;

        /*
         * For TextMedia Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST);
        int fullWidthSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
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
        MeasureUtils.measure(botContactTemplateView, wrapSpec, wrapSpec);

        /**
         * For PieChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(pieViewHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(botPieChartView, childWidthSpec, childHeightSpec);
        MeasureUtils.measure(advancedListTemplateView, childWidthSpec, wrapSpec);

        /*
         * For List View Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(botListViewTemplateView, childWidthSpec, wrapSpec);

        /*
         * For List Widget Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(botListWidgetTemplateView, childWidthSpec, wrapSpec);

        /**
         * For TableViev
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (tableHeight), MeasureSpec.EXACTLY);
        MeasureUtils.measure(tableView, childWidthSpec,wrapSpec);
        MeasureUtils.measure(botCustomTableView, childWidthSpec,wrapSpec);

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
         * For minitable
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth , MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (botCarouselView.getMeasuredHeight()), MeasureSpec.EXACTLY);
        MeasureUtils.measure(koraCarouselView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(botCarouselView, childWidthSpec, childHeightSpec);

        /**
         * for line chart
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(lineHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(lineChartView, childWidthSpec, childHeightSpec);

//        For BarChart
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(lineHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(barChartView, childWidthSpec, childHeightSpec);

//        For Horizontal BarChart
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(lineHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(horizontalBarChartView, childWidthSpec,childHeightSpec);

        /**
         * For stacked BarChart
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int)dp1, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(lineHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(stackedBarChatView, childWidthSpec,childHeightSpec);

        /*
         * For Table List Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(botTableListTemplateView, childWidthSpec, wrapSpec);

        /*
         * For List Widget Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(botButtonLinkTemplateView, childWidthSpec, wrapSpec);

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
         * For Banking feedback Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(bankingFeedbackTemplateView, childWidthSpec, wrapSpec);

        MeasureUtils.measure(meetingSlotsView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(multiSelectView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(botButtonView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(meetingConfirmationView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(verticalListView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(attendeeSlotSelectionView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(contactInfoView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(welcomeSummaryView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(universalSearchView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(koraSummaryHelpView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(feedbackTemplateView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(botDropDownTemplateView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(linkTemplateView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(resultsTemplateView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(pdfDownloadView, wrapSpec, wrapSpec);
        MeasureUtils.measure(botBeneficiaryTemplateView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(buttonDeepLinkTemplateView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(cardTemplateView, childWidthSpec, wrapSpec);
        MeasureUtils.measure(emptyTemplateView, childWidthSpec, wrapSpec);

        /*
         * For Widget List Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - (50 * (int) dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(imageTemplateView, childWidthSpec, wrapSpec);

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
        layoutView(meetingSlotsView, top, left, arrayList);
        layoutView(multiSelectView, top, left, arrayList);
        layoutView(attendeeSlotSelectionView, top, (int) (left - dp1), arrayList);
        layoutView(meetingConfirmationView, top, (int) (left - dp1), arrayList);
        layoutView(botCarouselView, top, left, arrayList);
        layoutView(verticalListView, top, (int) (left - dp1), arrayList);
        layoutView(botListTemplateView, top, left, arrayList);
        layoutView(botPieChartView, top, left, arrayList);
        layoutView(tableView, top, left, arrayList);
        layoutView(botCustomTableView, top, left, arrayList);
        layoutView(responsiveTableView, top, left, arrayList);
        layoutView(responsiveExpandTableView, top, left, arrayList);
        layoutView(lineChartView, top, left, arrayList);
        layoutView(barChartView, top, left, arrayList);
        layoutView(horizontalBarChartView, top, left, arrayList);
        layoutView(stackedBarChatView, top, left, arrayList);
        layoutView(contactInfoView, top, left, arrayList);
        layoutView(welcomeSummaryView, top, left, arrayList);
        layoutView(universalSearchView, top, left, arrayList);
        layoutView(koraSummaryHelpView, top, left, arrayList);
        layoutView(koraCarouselView, top, left, arrayList);
        layoutView(botFormTemplateView, top, left, arrayList);
        layoutView(botListViewTemplateView, top, left, arrayList);
        layoutView(botListWidgetTemplateView, top, left, arrayList);
        layoutView(botTableListTemplateView, top, left, arrayList);
        layoutView(botQuickRepliesTemplateView, top, left, arrayList);
        layoutView(agentTransferTemplateView, top, left, arrayList);
        layoutView(feedbackTemplateView, top, left, arrayList);
        layoutView(listWidgetView, top, left, arrayList);
        layoutView(botDropDownTemplateView, top, left, arrayList);
        layoutView(imageTemplateView, top, left, arrayList);
        layoutView(bankingFeedbackTemplateView, top, left, arrayList);
        layoutView(botContactTemplateView, top, left, arrayList);
        layoutView(linkTemplateView, top, left, arrayList);
        layoutView(advancedListTemplateView, top, left, arrayList);
        layoutView(resultsTemplateView, top, left, arrayList);
        layoutView(pdfDownloadView, top, left, arrayList);
        layoutView(botButtonLinkTemplateView, top, left, arrayList);
        layoutView(botBeneficiaryTemplateView, top, left, arrayList);
        layoutView(buttonDeepLinkTemplateView, top, left, arrayList);
        layoutView(cardTemplateView, top, left, arrayList);
        layoutView(emptyTemplateView, top, left, arrayList);

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
