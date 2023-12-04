package kore.botssdk.view;

import static kore.botssdk.net.SDKConfiguration.BubbleColors.BubbleUI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.models.AnnoucementResModel;
import kore.botssdk.models.AttendeeSlotTemplateModel;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotPieChartElementModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.ContactInfoModel;
import kore.botssdk.models.ContactTemplateModel;
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
import kore.botssdk.utils.ViewProvider;
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
    ArrayList<Integer> arrayList = new ArrayList<>();

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
        cpvSenderImage = (CircularProfileView) findViewById(R.id.cpvSenderImage);
    }

    @Override
    void initializeBubbleBorderPass1() {

        BUBBLE_LEFT_PROFILE_PIC = cpvSenderImage.getMeasuredWidth();
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? (int) (10 * dp1) : 0;
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? 0 : 0;
        if (isContinuousMessage) {
            BUBBLE_TOP_BORDER = 0;
        } else {
            BUBBLE_TOP_BORDER = (int) (7 * dp1);
        }
        BUBBLE_LEFT_BORDER = (int) ((!isGroupMessage) ? BubbleUI ? dp4 : 0 : dp1);
        BUBBLE_RIGHT_BORDER = (int) dp1;
        BUBBLE_DOWN_BORDER = (int) dp1;
        BUBBLE_ARROW_WIDTH = (int) ((isGroupMessage) ? dp1 : BubbleUI ? dp10 : 0);
        BUBBLE_LEFT_ARROW_WIDTH = (int) (BubbleUI ? BUBBLE_ARROW_WIDTH / 2 + 7 * dp1 : 0);
        BUBBLE_RIGHT_ARROW_WIDTH = 0;
        if (bubbleTextMediaLayout.getMeasuredHeight() != 0) {
            BUBBLE_CONTENT_TOP_MARGIN = 0;
            BUBBLE_CONTENT_BOTTOM_MARGIN = BubbleUI ? (int) (10 * dp1) : (int) (5 * dp1);
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
                + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + Collections.max(Arrays.asList(textMediaDimen[0], botCarouselView != null ? botCarouselView.getMeasuredWidth() : 0, verticalListView != null ? verticalListView.getMeasuredWidth() : 0, timeStampsTextView.getMeasuredWidth(), timeLineView.getMeasuredWidth(), articleTemplateView != null ? articleTemplateView.getMeasuredWidth() : 0,
                meetingSlotsView != null ? meetingSlotsView.getMeasuredWidth() : 0, multiSelectView != null ? multiSelectView.getMeasuredWidth() : 0, attendeeSlotSelectionView != null ? attendeeSlotSelectionView.getMeasuredWidth() : 0, meetingConfirmationView != null ? meetingConfirmationView.getMeasuredWidth() : 0, botButtonView != null ? botButtonView.getMeasuredWidth() : 0, tableView != null ? tableView.getMeasuredWidth() : 0, responsiveTableView.getMeasuredWidth(), responsiveExpandTableView != null ? responsiveExpandTableView.getMeasuredWidth() : 0,
                lineChartView != null ? lineChartView.getMeasuredWidth() : 0, barChartView != null ? barChartView.getMeasuredWidth() : 0, (horizontalBarChartView != null ? horizontalBarChartView.getMeasuredWidth() : 0), stackedBarChatView != null ? stackedBarChatView.getMeasuredWidth() : 0, (koraCarouselView != null ? koraCarouselView.getMeasuredWidth() : 0), botListViewTemplateView != null ? botListViewTemplateView.getMeasuredWidth() : 0, botCustomTableView != null ? botCustomTableView.getMeasuredWidth() : 0,
                botListTemplateView != null ? botListTemplateView.getMeasuredWidth() : 0, contactInfoView != null ? contactInfoView.getMeasuredWidth() : 0, botPieChartView != null ? botPieChartView.getMeasuredWidth() : 0, welcomeSummaryView != null ? welcomeSummaryView.getMeasuredWidth() : 0, (botFormTemplateView != null ? botFormTemplateView.getMeasuredWidth() : 0), botTableListTemplateView.getMeasuredWidth(), botQuickRepliesTemplateView.getMeasuredWidth(),
                agentTransferTemplateView.getMeasuredWidth(), feedbackTemplateView.getMeasuredWidth(), listWidgetView != null ? listWidgetView.getMeasuredWidth() : 0, botDropDownTemplateView.getMeasuredWidth(), botListWidgetTemplateView.getMeasuredWidth(), imageTemplateView.getMeasuredWidth(), bankingFeedbackTemplateView.getMeasuredWidth(), advancedListTemplateView.getMeasuredWidth(),
                (koraSummaryHelpView != null ? koraSummaryHelpView.getMeasuredWidth() : 0), universalSearchView != null ? universalSearchView.getMeasuredWidth() : 0, botContactTemplateView != null ? botContactTemplateView.getMeasuredWidth() : 0, nearByStockAvailableStoreListView != null ? nearByStockAvailableStoreListView.getMeasuredWidth() : 0, productInventoryActionFormView != null ? productInventoryActionFormView.getMeasuredWidth() : 0)) + BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;


        // headerLayoutDimen[1] = headerLayout.getMeasuredHeight();
        maxBubbleDimen[0] = maxContentDimen[0];

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                textMediaDimen[1] + (botCarouselView != null ? botCarouselView.getMeasuredHeight() : 0) + (meetingSlotsView != null ? meetingSlotsView.getMeasuredHeight() : 0) + (multiSelectView != null ? multiSelectView.getMeasuredHeight() : 0) + (verticalListView != null ? verticalListView.getMeasuredHeight() : 0) + cpvSenderImage.getMeasuredHeight() + timeLineView.getMeasuredHeight() + advancedListTemplateView.getMeasuredHeight() + (articleTemplateView != null ? articleTemplateView.getMeasuredHeight() : 0) +
                (meetingConfirmationView != null ? meetingConfirmationView.getMeasuredHeight() : 0) + (attendeeSlotSelectionView != null ? attendeeSlotSelectionView.getMeasuredHeight() : 0) + (botPieChartView != null ? botPieChartView.getMeasuredHeight() : 0) + (tableView != null ? tableView.getMeasuredHeight() : 0) + responsiveTableView.getMeasuredHeight() + (responsiveExpandTableView != null ? responsiveExpandTableView.getMeasuredHeight() : 0) +
                (lineChartView != null ? lineChartView.getMeasuredHeight() : 0) + (barChartView != null ? barChartView.getMeasuredHeight() : 0) + (horizontalBarChartView != null ? horizontalBarChartView.getMeasuredHeight() : 0) + (stackedBarChatView != null ? stackedBarChatView.getMeasuredHeight() : 0) + (koraCarouselView != null ? koraCarouselView.getMeasuredHeight() : 0) + (botCustomTableView != null ? botCustomTableView.getMeasuredHeight() : 0)
                + (botListViewTemplateView != null ? botListViewTemplateView.getMeasuredHeight() : 0) + botTableListTemplateView.getMeasuredHeight() + botQuickRepliesTemplateView.getMeasuredHeight() + agentTransferTemplateView.getMeasuredHeight() + feedbackTemplateView.getMeasuredHeight() + (botContactTemplateView != null ? botContactTemplateView.getMeasuredHeight() : 0) + (nearByStockAvailableStoreListView != null ? nearByStockAvailableStoreListView.getMeasuredHeight() : 0) +
                (productInventoryActionFormView != null ? productInventoryActionFormView.getMeasuredHeight() : 0)
                + (botButtonView != null ? botButtonView.getMeasuredHeight() : 0) + (botListTemplateView != null ? botListTemplateView.getMeasuredHeight() : 0) + (contactInfoView != null ? contactInfoView.getMeasuredHeight() : 0) + (listWidgetView != null ? listWidgetView.getMeasuredHeight() : 0) + botListWidgetTemplateView.getMeasuredHeight() +
                (welcomeSummaryView != null ? welcomeSummaryView.getMeasuredHeight() : 0) + (universalSearchView != null ? universalSearchView.getMeasuredHeight() : 0) + (koraSummaryHelpView != null ? koraSummaryHelpView.getMeasuredHeight() : 0) + (botFormTemplateView != null ? botFormTemplateView.getMeasuredHeight() : 0) + botDropDownTemplateView.getMeasuredHeight() + imageTemplateView.getMeasuredHeight() + bankingFeedbackTemplateView.getMeasuredHeight()
                + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER + (int) ((botButtonView != null && botButtonView.getMeasuredHeight() != 0) ||
                meetingSlotsView != null && meetingSlotsView.getMeasuredHeight() != 0 ? dp2 : 0);
        if (bubbleTextMediaLayout.getMeasuredHeight() != 0 && isTemplatePresent()) {
            maxBubbleDimen[1] = maxBubbleDimen[1] + (int) dp15;
        }
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN + textMediaDimen[1] + (botCarouselView != null ? botCarouselView.getMeasuredHeight() : 0) + (verticalListView != null ? verticalListView.getMeasuredHeight() : 0)
                + cpvSenderImage.getMeasuredHeight() + timeLineView.getMeasuredHeight() + (botButtonView != null ? botButtonView.getMeasuredHeight() : 0) + (botListTemplateView != null ? botListTemplateView.getMeasuredHeight() : 0) + (botPieChartView != null ? botPieChartView.getMeasuredHeight() : 0) + advancedListTemplateView.getMeasuredHeight() + (articleTemplateView != null ? articleTemplateView.getMeasuredHeight() : 0) +
                (tableView != null ? tableView.getMeasuredHeight() : 0) + responsiveTableView.getMeasuredHeight() + (responsiveExpandTableView != null ? responsiveExpandTableView.getMeasuredHeight() : 0) + (lineChartView != null ? lineChartView.getMeasuredHeight() : 0) + (botCustomTableView != null ? botCustomTableView.getMeasuredHeight() : 0)
                + (barChartView != null ? barChartView.getMeasuredHeight() : 0) + (horizontalBarChartView != null ? horizontalBarChartView.getMeasuredHeight() : 0) + (stackedBarChatView != null ? stackedBarChatView.getMeasuredHeight() : 0) + (koraCarouselView != null ? koraCarouselView.getMeasuredHeight() : 0) + agentTransferTemplateView.getMeasuredHeight() + (botContactTemplateView != null ? botContactTemplateView.getMeasuredHeight() : 0) + (nearByStockAvailableStoreListView != null ? nearByStockAvailableStoreListView.getMeasuredHeight() : 0) +
                (productInventoryActionFormView != null ? productInventoryActionFormView.getMeasuredHeight() : 0) +
                (meetingSlotsView != null ? meetingSlotsView.getMeasuredHeight() : 0) + (attendeeSlotSelectionView != null ? attendeeSlotSelectionView.getMeasuredHeight() : 0) + (multiSelectView != null ? multiSelectView.getMeasuredHeight() : 0) + (botFormTemplateView != null ? botFormTemplateView.getMeasuredHeight() : 0) + (botListViewTemplateView != null ? botListViewTemplateView.getMeasuredHeight() : 0) + imageTemplateView.getMeasuredHeight() + feedbackTemplateView.getMeasuredHeight()
                + (meetingConfirmationView != null ? meetingConfirmationView.getMeasuredHeight() : 0) + (contactInfoView != null ? contactInfoView.getMeasuredHeight() : 0) + (welcomeSummaryView != null ? welcomeSummaryView.getMeasuredHeight() : 0) + botTableListTemplateView.getMeasuredHeight() + botQuickRepliesTemplateView.getMeasuredHeight() + botListWidgetTemplateView.getMeasuredHeight() +
                +feedbackTemplateView.getMeasuredHeight() + (listWidgetView != null ? listWidgetView.getMeasuredHeight() : 0) + (universalSearchView != null ? universalSearchView.getMeasuredHeight() : 0) + (koraSummaryHelpView != null ? koraSummaryHelpView.getMeasuredHeight() : 0) + botDropDownTemplateView.getMeasuredHeight() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        if (bubbleTextMediaLayout.getMeasuredHeight() != 0 && isTemplatePresent()) {
            maxBubbleDimen[1] = maxBubbleDimen[1] + (int) dp15;
        }
    }

    private boolean isTemplatePresent() {
        if ((contactInfoView != null && contactInfoView.getMeasuredHeight() > 0) || (botListTemplateView != null && botListTemplateView.getMeasuredHeight() > 0) /*|| botButtonView.getMeasuredHeight() > 0*/
                || (botCarouselView != null && botCarouselView.getMeasuredHeight() > 0) || (attendeeSlotSelectionView != null && attendeeSlotSelectionView.getMeasuredHeight() > 0) ||
                (meetingConfirmationView != null && meetingConfirmationView.getMeasuredHeight() > 0) || (verticalListView != null && verticalListView.getMeasuredHeight() > 0) || advancedListTemplateView.getMeasuredHeight() > 0 ||
                (meetingSlotsView != null && meetingSlotsView.getMeasuredHeight() > 0) || (botPieChartView != null && botPieChartView.getMeasuredHeight() > 0) || (botFormTemplateView != null && botFormTemplateView.getMeasuredHeight() > 0) || (botListViewTemplateView != null && botListViewTemplateView.getMeasuredHeight() > 0) || botTableListTemplateView.getMeasuredHeight() > 0 || (botCustomTableView != null && botCustomTableView.getMeasuredHeight() > 0) ||
                (lineChartView != null && lineChartView.getMeasuredHeight() > 0) || (barChartView != null && barChartView.getMeasuredHeight() > 0) || (horizontalBarChartView != null && horizontalBarChartView.getMeasuredHeight() > 0) || (stackedBarChatView != null && stackedBarChatView.getMeasuredHeight() > 0) || (koraCarouselView != null && koraCarouselView.getMeasuredHeight() > 0) /*|| listWidgetView.getMeasuredHeight() > 0*/ ||
                (tableView != null && tableView.getMeasuredHeight() > 0) || responsiveTableView.getMeasuredHeight() > 0 || (responsiveExpandTableView != null && responsiveExpandTableView.getMeasuredHeight() > 0) || agentTransferTemplateView.getMeasuredHeight() > 0 || feedbackTemplateView.getMeasuredHeight() > 0 || botListWidgetTemplateView.getMeasuredHeight() > 0 ||
                (welcomeSummaryView != null && welcomeSummaryView.getMeasuredHeight() > 0) || (koraSummaryHelpView != null && koraSummaryHelpView.getMeasuredHeight() > 0) || (universalSearchView != null && universalSearchView.getMeasuredHeight() > 0) || (multiSelectView != null && multiSelectView.getMeasuredHeight() > 0) || botQuickRepliesTemplateView.getMeasuredHeight() > 0 || botDropDownTemplateView.getMeasuredHeight() > 0 ||
                imageTemplateView.getMeasuredHeight() > 0 || bankingFeedbackTemplateView.getMeasuredHeight() > 0 || (botContactTemplateView != null && botContactTemplateView.getMeasuredHeight() > 0) || (nearByStockAvailableStoreListView != null && nearByStockAvailableStoreListView.getMeasuredHeight() > 0) || (productInventoryActionFormView != null && productInventoryActionFormView.getMeasuredHeight() > 0) || (articleTemplateView != null && articleTemplateView.getMeasuredHeight() > 0)) {
            return true;
        }

        if (listWidgetView != null && listWidgetView.getMeasuredHeight() > 0) {
            return true;
        }
        if (botButtonView != null && botButtonView.getMeasuredHeight() > 0) {
            return true;
        }

        return false;

    }

    @Override
    protected void preCosmeticChanges() {
        super.preCosmeticChanges();
        resetAll();

    }

    private void resetAll() {
        arrayList.clear();
        if (botButtonView != null) {
            botButtonView.setVisibility(View.GONE);
            botButtonView.populateButtonList(null, false);
        }
        if (botCarouselView != null) {
            botCarouselView.populateCarouselView(null);
            botCarouselView.setVisibility(View.GONE);
        }
        if (verticalListView != null) {
            verticalListView.prepareDataSetAndPopulate(null, null, false);
            verticalListView.setVisibility(GONE);
        }
        if (meetingSlotsView != null) {
            meetingSlotsView.populateData(null, false);
            meetingSlotsView.setVisibility(GONE);
        }
        if (botContactTemplateView != null) {
            botContactTemplateView.setVisibility(View.GONE);
            botContactTemplateView.populateContactTemplateView(null, "");
        }
        if (multiSelectView != null) {
            multiSelectView.populateData(null, false);
            multiSelectView.setVisibility(GONE);
        }
        if (attendeeSlotSelectionView != null) {
            attendeeSlotSelectionView.populateData(-1, null, false);
            attendeeSlotSelectionView.setVisibility(GONE);
        }
        if (meetingConfirmationView != null) {
            meetingConfirmationView.populateData(null);
            meetingConfirmationView.setVisibility(GONE);
        }
        if (contactInfoView != null) {
            contactInfoView.populateData(null);
            contactInfoView.setVisibility(GONE);
        }
        if (welcomeSummaryView != null) {
            welcomeSummaryView.populateData(null, false);
            welcomeSummaryView.setVisibility(GONE);
        }
        if (universalSearchView != null) {
            universalSearchView.populateData(null);
            universalSearchView.setVisibility(GONE);
        }
        if (koraSummaryHelpView != null) {
            koraSummaryHelpView.populateData(null);
            koraSummaryHelpView.setVisibility(GONE);
        }
        if (botListTemplateView != null) {
            botListTemplateView.setVisibility(View.GONE);
            botListTemplateView.populateListTemplateView(null, null);
        }
        if (botPieChartView != null) {
            botPieChartView.setVisibility(View.GONE);
        }
        if (tableView != null) {
            tableView.setData(null);
            tableView.setVisibility(View.GONE);
        }
        if (botCustomTableView != null) {
            botCustomTableView.setData(null);
            botCustomTableView.setVisibility(View.GONE);
        }
        if (responsiveTableView != null) {
            responsiveTableView.setData(null);
            responsiveTableView.setVisibility(View.GONE);
        }
        if (responsiveExpandTableView != null) {
            responsiveExpandTableView.setData(null);
            responsiveExpandTableView.setVisibility(View.GONE);
            responsiveExpandTableView.setData(null);
        }
        if (lineChartView != null) {
            lineChartView.setVisibility(GONE);
        }
        if (barChartView != null) {
            barChartView.setVisibility(GONE);
        }
        if (horizontalBarChartView != null) {
            horizontalBarChartView.setVisibility(GONE);
        }
        if (stackedBarChatView != null) {
            stackedBarChatView.setVisibility(GONE);
        }
        if (koraCarouselView != null) {
            koraCarouselView.populateMiniTable(null, null);
            koraCarouselView.setVisibility(View.GONE);
        }
        if (timeLineView != null) {
            timeLineView.setVisibility(GONE);
            timeLineView.setText("");
        }

        if (botFormTemplateView != null) {
            botFormTemplateView.populateData(null, false);
            botFormTemplateView.setVisibility(GONE);
        }
        if (botListViewTemplateView != null) {
            botListViewTemplateView.setVisibility(View.GONE);
            botListViewTemplateView.populateListTemplateView(null, null, null, null, 0, null);
        }

        if (botListWidgetTemplateView != null) {
            botListWidgetTemplateView.setVisibility(View.GONE);
            botListWidgetTemplateView.populateListWidgetTemplateView(null, null, null, null, 0, null);
        }
        if (botTableListTemplateView != null) {
            botTableListTemplateView.setVisibility(View.GONE);
            botTableListTemplateView.populateListTemplateView(null);
        }
        if (botQuickRepliesTemplateView != null) {
            botQuickRepliesTemplateView.setVisibility(View.GONE);
            botQuickRepliesTemplateView.populateQuickReplyView(null);
        }
        if (agentTransferTemplateView != null) {
            agentTransferTemplateView.setVisibility(View.GONE);
            agentTransferTemplateView.populateAgentTemplateView(null);
        }
        if (feedbackTemplateView != null) {
            feedbackTemplateView.setVisibility(View.GONE);
            feedbackTemplateView.populateData(null, false);
        }
        if (listWidgetView != null) {
            listWidgetView.setVisibility(GONE);
            listWidgetView.populateListWidgetData(null);
        }
        if (botDropDownTemplateView != null) {
            botDropDownTemplateView.setVisibility(GONE);
            botDropDownTemplateView.populateData(null);
        }
        if (imageTemplateView != null) {
            imageTemplateView.setVisibility(GONE);
            imageTemplateView.populateData(null, null);
        }
        if (bankingFeedbackTemplateView != null) {
            bankingFeedbackTemplateView.setVisibility(GONE);
            bankingFeedbackTemplateView.populateData(null, false);
        }
        if (nearByStockAvailableStoreListView != null) {
            nearByStockAvailableStoreListView.setVisibility(View.GONE);
            nearByStockAvailableStoreListView.populateNearByStockAvailableStores(null, false);
        }
        if (productInventoryActionFormView != null) {
            productInventoryActionFormView.setVisibility(View.GONE);
            productInventoryActionFormView.showInputForm(null);
        }
        if (advancedListTemplateView != null) {
            advancedListTemplateView.setVisibility(GONE);
            advancedListTemplateView.populateAdvancedListTemplateView(null);
        }
        if (articleTemplateView != null) {
            articleTemplateView.setVisibility(GONE);
            articleTemplateView.populateArticleListTemplateView(null);
        }
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
        if (SDKConfiguration.BubbleColors.showIcon) {
            if (!StringUtils.isNullOrEmpty(SDKConfiguration.BubbleColors.getIcon_url()))
                cpvSenderImage.populateLayout(" ", null, SDKConfiguration.BubbleColors.getIcon_url(), null, SDKConfiguration.BubbleColors.getIcon(), R.color.white, true, BUBBLE_LEFT_PROFILE_PIC, BUBBLE_LEFT_PROFILE_PIC);
            else
                cpvSenderImage.populateLayout(" ", null, null, null, SDKConfiguration.BubbleColors.getIcon(), R.color.white, true, BUBBLE_LEFT_PROFILE_PIC, BUBBLE_LEFT_PROFILE_PIC);
            cpvSenderImage.setVisibility(StringUtils.isNullOrEmptyWithTrim(timeStampsTextView.getText()) ? GONE : VISIBLE);
        } else {
            cpvSenderImage.setVisibility(GONE);
        }
    }


    protected void populateForTemplates(int position, boolean isLastItem, ComponentModel compModel, BaseBotMessage baseBotMessage) {
        resetAll();
        if (compModel != null) {

            //composeFooterInterface.showMentionNarratorContainer(false, "","" ,null);

            PayloadOuter payOuter = compModel.getPayload();
            if (payOuter == null) return;
            PayloadInner payInner;
            if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
//                Gson gson = new Gson();
                payOuter.getText().replace("&quot;", "\"");
            }
            payInner = payOuter.getPayload();

            if (payInner != null)
                payInner.convertElementToAppropriate();

            if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(payOuter.getType()) && payInner != null) {
                Log.i("TemplateType", "payInner.getTemplate_type() --->:" + payInner.getTemplate_type());
                checkBubbleVisibilityAndHideCpv(payInner);
                if (BotResponse.TEMPLATE_TYPE_BUTTON.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (botButtonView == null) {
                        botButtonView = ViewProvider.getBotButtonView(context, composeFooterInterface);
                        botButtonView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        addView(botButtonView);
                    }
                    botButtonView.setVisibility(View.VISIBLE);
                    botButtonView.setRestrictedMaxWidth(screenWidth - 28 * dp1);
                    botButtonView.populateButtonList(payInner.getButtons(), isLastItem);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TYPE_FORM_ACTIONS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.setClicable(isLastItem);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    if (StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
                        timeStampsTextView.setText("");
                    }
                } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TYPE_WELCOME_CAROUSEL.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (botCarouselView == null) {
                        botCarouselView = ViewProvider.getBotCarousalView(context);
                        botCarouselView.setComposeFooterInterface(composeFooterInterface);
                        addView(botCarouselView);
                    }
                    botCarouselView.setVisibility(View.VISIBLE);
                    botCarouselView.populateCarouselView(payInner.getCarouselElements(), payInner.getTemplate_type());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (botListTemplateView == null) {
                        botListTemplateView = ViewProvider.getBotListTempleteView(context);
                        addView(botListTemplateView);
                    }
                    botListTemplateView.setVisibility(View.VISIBLE);
                    botListTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() + BUBBLE_CONTENT_RIGHT_MARGIN);
                    botListTemplateView.populateListTemplateView(payInner.getListElements(), payInner.getButtons());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_PIECHART.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (botPieChartView == null) {
                        botPieChartView = ViewProvider.getPieChartView(context);
                        addView(botPieChartView);
                    }
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
                            arrLables.add(elementModels.get(i).getTitle() + " " + elementModels.get(i).getValue());
                        }
                        botPieChartView.populatePieChart("", payInner.getPie_type(), xVal, yVal, arrLables);
                    }

                }/*else if (BotResponse.TEMPLATE_TYPE_TABLE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    tableView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    tableView.setData(payInner);

                }*/ else if (BotResponse.TEMPLATE_TYPE_TABLE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (payInner.getTableDesign() != null && (payInner.getTableDesign().equalsIgnoreCase(BotResponse.TABLE_VIEW_RESPONSIVE) ||
                            payInner.getTableDesign().equalsIgnoreCase(""))) {
                        if (responsiveExpandTableView == null) {
                            responsiveExpandTableView = ViewProvider.getResponsiveExpandTableView(context);
                            addView(responsiveExpandTableView);
                        }
                        responsiveExpandTableView.setVisibility(View.VISIBLE);
                        bubbleTextMediaLayout.populateText(payInner.getText());
                        responsiveExpandTableView.setData(payInner);
                    } else {
                        if (tableView == null) {
                            tableView = ViewProvider.getTableView(context);
                            tableView.setComposeFooterInterface(composeFooterInterface);
                            addView(tableView);
                        }
                        tableView.setVisibility(View.VISIBLE);
                        bubbleTextMediaLayout.populateText(payInner.getText());
                        tableView.setData(payInner);
                    }
                } else if (BotResponse.CUSTOM_TABLE_TEMPLATE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (botCustomTableView == null) {
                        botCustomTableView = ViewProvider.getCustomTableView(context);
                        botCustomTableView.setComposeFooterInterface(composeFooterInterface);
                        addView(botCustomTableView);

                    }
                    botCustomTableView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    botCustomTableView.setData(payInner);
                } else if (BotResponse.TEMPLATE_TYPE_MINITABLE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (koraCarouselView == null) {
                        koraCarouselView = ViewProvider.getKoraCarouselView(context);
                        addView(koraCarouselView);
                    }
                    koraCarouselView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    koraCarouselView.populateMiniTable(payInner.getTemplate_type(), payInner);
                } else if (BotResponse.TEMPLATE_TYPE_MULTI_SELECT.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (multiSelectView == null) {
                        multiSelectView = ViewProvider.getMultiSelectView(context);
                        multiSelectView.setComposeFooterInterface(composeFooterInterface);
                        addView(multiSelectView);
                    }
                    multiSelectView.setVisibility(View.VISIBLE);
                    multiSelectView.populateData(payInner, isLastItem);

                    if (!StringUtils.isNullOrEmpty(payInner.getText()))
                        bubbleTextMediaLayout.populateText(payInner.getText());
                    else if (!StringUtils.isNullOrEmpty(payInner.getHeading()))
                        bubbleTextMediaLayout.populateText(payInner.getHeading());

                } else if (BotResponse.TEMPLATE_TYPE_LINECHART.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (lineChartView == null) {
                        lineChartView = ViewProvider.getLineChartView(context);
                        addView(lineChartView);
                    }
                    lineChartView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    lineChartView.setData(payInner);
                } else if (BotResponse.ADVANCED_LIST_TEMPLATE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    advancedListTemplateView.setVisibility(View.VISIBLE);
                    advancedListTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth());
                    advancedListTemplateView.populateAdvancedListTemplateView(payInner);
                } else if (BotResponse.TEMPLATE_TYPE_BARCHART.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    if (!payInner.isStacked()) {
                        if (!BotResponse.BAR_CHART_DIRECTION_VERTICAL.equalsIgnoreCase(payInner.getDirection())) {
                            if (barChartView == null) {
                                barChartView = ViewProvider.getBarChartView(context);
                                addView(barChartView);
                            }
                            barChartView.setVisibility(View.VISIBLE);
                            barChartView.setData(payInner);
                        } else {
                            if (horizontalBarChartView == null) {
                                horizontalBarChartView = ViewProvider.getHorizontalBarChartView(context);
                                addView(horizontalBarChartView);
                            }
                            horizontalBarChartView.setVisibility(VISIBLE);
                            horizontalBarChartView.setData(payInner);
                        }
                    } else {
                        if (stackedBarChatView == null) {
                            stackedBarChatView = ViewProvider.getStackedBarChartView(context);
                            addView(stackedBarChatView);
                        }
                        stackedBarChatView.setVisibility(View.VISIBLE);
                        stackedBarChatView.setData(payInner);
                    }
                } else if (BotResponse.TEMPLATE_TYPE_FORM.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (botFormTemplateView == null) {
                        botFormTemplateView = ViewProvider.getBotFormTemplateView(context);
                        botFormTemplateView.setComposeFooterInterface(composeFooterInterface);
                        addView(botFormTemplateView);
                    }
                    botFormTemplateView.setVisibility(View.VISIBLE);
                    botFormTemplateView.populateData(payInner, isLastItem);

//                    bubbleTextMediaLayout.populateText(payInner.getHeading());
                } else if (BotResponse.TEMPLATE_TYPE_LIST_VIEW.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (botListViewTemplateView == null) {
                        botListViewTemplateView = ViewProvider.getBotListViewTempleteView(context);
                        botListViewTemplateView.setComposeFooterInterface(composeFooterInterface);
                        addView(botListViewTemplateView);
                    }

                    botListViewTemplateView.setVisibility(View.VISIBLE);
                    botListViewTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    botListViewTemplateView.populateListTemplateView(payInner.getText(), payInner.getMoreData(), payInner.getListElements(), payInner.getButtons(), payInner.getMoreCount(), payInner.getSeeMore());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_TABLE_LIST.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botTableListTemplateView.setVisibility(View.VISIBLE);
                    botTableListTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    botTableListTemplateView.populateListTemplateView(payInner.getTableListElements());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_WELCOME_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botQuickRepliesTemplateView.setVisibility(View.VISIBLE);
                    botQuickRepliesTemplateView.populateQuickReplyView(payInner.getQuick_replies());
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_NOTIFICATIONS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    agentTransferTemplateView.setVisibility(View.VISIBLE);
                    agentTransferTemplateView.populateAgentTemplateView(payInner);
                } else if (BotResponse.TEMPLATE_TYPE_FEEDBACK.equalsIgnoreCase(payInner.getTemplate_type())) {
                    feedbackTemplateView.setVisibility(View.VISIBLE);
                    feedbackTemplateView.populateData(payInner, true);
                } else if (BotResponse.TEMPLATE_TYPE_LIST_WIDGET.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (listWidgetView == null) {
                        listWidgetView = ViewProvider.getListWidgetTemplateView(context);
                        listWidgetView.setComposeFooterInterface(composeFooterInterface);
                        addView(listWidgetView);
                    }
                    listWidgetView.setVisibility(View.VISIBLE);
                    listWidgetView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth());
                    listWidgetView.populateListWidgetData(payInner);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_DROPDOWN.equalsIgnoreCase(payInner.getTemplate_type())) {
                    botDropDownTemplateView.setVisibility(View.VISIBLE);
                    botDropDownTemplateView.populateData(payInner);
                } else if (BotResponse.TEMPLATE_TYPE_KORA_CAROUSAL.equals(payInner.getTemplate_type())) {
                    ArrayList<KnowledgeDetailModel> knowledgeData = payInner.getKnowledgeDetailModels();
                    initVerticalListView();
                    verticalListView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    verticalListView.prepareDataSetAndPopulate(knowledgeData, payInner.getTemplate_type(), isLastItem);
                } else if (BotResponse.TEMPLATE_TYPE_KORA_ANNOUNCEMENT_CAROUSAL.equals(payInner.getTemplate_type())) {
                    //announcement carousal
                    List<AnnoucementResModel> annoucementResModelsData = payInner.getAnnouncementResModels();
                    initVerticalListView();
                    verticalListView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    verticalListView.prepareDataSetAndPopulate((ArrayList) annoucementResModelsData, payInner.getTemplate_type(), isLastItem);
                } else if (BotResponse.CONTACT_CARD_TEMPLATE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (botContactTemplateView == null) {
                        botContactTemplateView = ViewProvider.getBotContactView(context, composeFooterInterface);
                        botContactTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        addView(botContactTemplateView);
                    }
                    botContactTemplateView.setVisibility(View.VISIBLE);
                    botContactTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    botContactTemplateView.populateContactTemplateView((ArrayList<ContactTemplateModel>) payInner.getCards(), payInner.getTitle());
                } else if (BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL.equalsIgnoreCase(payInner.getTemplate_type())) {
                    initVerticalListView();
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
                        if (meetingConfirmationView == null) {
                            meetingConfirmationView = ViewProvider.getMeetingConfirmationView(context);
                            meetingConfirmationView.setComposeFooterInterface(composeFooterInterface);
                            //   meetingConfirmationView.setRestrictedLayoutWidth(BubbleViewUtil.getMeetingSlotConfirmationWidth());
                            addView(meetingConfirmationView);
                        }
                        meetingConfirmationView.setVisibility(View.VISIBLE);
                        meetingConfirmationView.populateData(meetingTemplateModels.get(0));
                        // BUBBLE_CONTENT_RIGHT_MARGIN = 0;
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_TASK_VIEW.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TASK_FULLVIEW.equalsIgnoreCase(payInner.getTemplate_type())) {
                    ArrayList<TaskTemplateResponse> taskTemplateModels = payInner.getTaskTemplateModels();
                    if (taskTemplateModels != null && taskTemplateModels.size() > 0) {
                        TaskTemplateResponse taskTemplateModel = taskTemplateModels.get(0);
                        initVerticalListView();
                        verticalListView.setVisibility(VISIBLE);
                        verticalListView.prepareDataToTasks(taskTemplateModel, BotResponse.TEMPLATE_TYPE_TASK_VIEW, isLastItem && (taskTemplateModel.getButtons() != null && taskTemplateModel.getButtons().size() > 0));
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_ATTENDEE_SLOTS.equalsIgnoreCase(payInner.getTemplate_type())) {
                    ArrayList<AttendeeSlotTemplateModel> meetingTemplateModels = payInner.getAttendeeSlotTemplateModels();
                    if (meetingTemplateModels != null && meetingTemplateModels.size() > 0) {
                        if (attendeeSlotSelectionView == null) {
                            attendeeSlotSelectionView = ViewProvider.getAttendeeSlotSelectionView(context);
                            attendeeSlotSelectionView.setComposeFooterInterface(composeFooterInterface);
                            addView(attendeeSlotSelectionView);
                        }
                        attendeeSlotSelectionView.setVisibility(View.VISIBLE);
                        attendeeSlotSelectionView.populateData(position, meetingTemplateModels.get(0), isLastItem);
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS.equalsIgnoreCase(payInner.getTemplate_type()) || BotResponse.TEMPLATE_TYPE_CANCEL_EVENT.equalsIgnoreCase(payInner.getTemplate_type())) {
                    ArrayList<CalEventsTemplateModel> calList = payInner.getCalEventsTemplateModels();
                    if (calList != null && !calList.isEmpty()) {
                        initVerticalListView();
                        verticalListView.setVisibility(View.VISIBLE);
                        verticalListView.setCursorDuration(payInner.getCursor());
                        verticalListView.prepareDataSetAndPopulate(calList, payInner.getTemplate_type(), isLastItem);
                    }
                    bubbleTextMediaLayout.populateText(payInner.getText());
                } else if (BotResponse.TEMPLATE_TYPE_FILES_LOOKUP.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    initVerticalListView();
                    verticalListView.setVisibility(View.VISIBLE);
                    ArrayList<KaFileLookupModel> fileList = payInner.getFileLookupModels();
                    if (fileList != null)
                        verticalListView.prepareDataSetAndPopulate(fileList, BotResponse.TEMPLATE_TYPE_FILES_LOOKUP, isLastItem);
                } else if (BotResponse.KA_CONTACT_VIEW.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (contactInfoView == null) {
                        contactInfoView = ViewProvider.getContactInfoView(context);
                        contactInfoView.setComposeFooterInterface(composeFooterInterface);
                        contactInfoView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        addView(contactInfoView);
                    }
                    contactInfoView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    ArrayList<ContactInfoModel> contactInfoModels = payInner.getContactInfoModels();
                    if (contactInfoModels != null && contactInfoModels.size() > 0)
                        contactInfoView.populateData(contactInfoModels.get(0));
                } else if (BotResponse.WELCOME_SUMMARY_VIEW.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (welcomeSummaryView == null) {
                        welcomeSummaryView = ViewProvider.getWelcomeSummaryView(context);
                        welcomeSummaryView.setComposeFooterInterface(composeFooterInterface);
                        addView(welcomeSummaryView);
                    }
                    welcomeSummaryView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    ArrayList<WelcomeSummaryModel> welcomeSummaryModels = payInner.getWelcomeSummaryModel();
                    if (welcomeSummaryModels != null && welcomeSummaryModels.size() > 0)
                        welcomeSummaryView.populateData(welcomeSummaryModels.get(0), isLastItem);

                } else if (BotResponse.TEMPLATE_TYPE_UNIVERSAL_SEARCH.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (universalSearchView == null) {
                        universalSearchView = ViewProvider.getUniversalSearchView(context);
                        universalSearchView.setComposeFooterInterface(composeFooterInterface);
                        universalSearchView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        addView(universalSearchView);
                    }
                    universalSearchView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    universalSearchView.populateData(payInner.getUniversalSearchModels());

                } else if (BotResponse.KORA_SUMMARY_HELP_VIEW.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (koraSummaryHelpView == null) {
                        koraSummaryHelpView = ViewProvider.getKoraSummaryHelpView(context);
                        koraSummaryHelpView.setComposeFooterInterface(composeFooterInterface);
                        addView(koraSummaryHelpView);
                    }
                    koraSummaryHelpView.setVisibility(View.VISIBLE);
                    bubbleTextMediaLayout.populateText(payInner.getText());
                    ArrayList<KoraSummaryHelpModel> summaryModels = payInner.getKoraSummaryHelpModel();
                    if (summaryModels != null && summaryModels.size() > 0)
                        koraSummaryHelpView.populateData(summaryModels.get(0));
                } else if (BotResponse.TEMPLATE_TYPE_HIDDEN_DIALOG.equalsIgnoreCase(payInner.getTemplate_type())) {
//                    hiddenDialog.setVisibility(View.VISIBLE);
                    timeStampsTextView.setText("");
                    bubbleTextMediaLayout.populateText("");
                } else if (BotResponse.NARRATOR_TEXT.equalsIgnoreCase(payInner.getTemplate_type())) {
//                    bubbleTextMediaLayout.populateText(payInner.getText());
//                    ArrayList<NarratorTextModel> narratorModels = payInner.getNarratorTextModel();
//                    if (narratorModels != null){}

                } else if (BotResponse.TEMPLATE_BANKING_FEEDBACK.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bankingFeedbackTemplateView.setVisibility(View.VISIBLE);
                    bankingFeedbackTemplateView.populateData(payInner, isLastItem);
                }/*else if(BotResponse.NARRATOR_TEXT.equalsIgnoreCase(payInner.getTemplate_type())){
                    String narrateText = payInner.getText();
                    String composeText = payInner.getComposeText();
                    if(payInner.getChildTemplate()!=null){
                        ((BotResponse) baseBotMessage).getMessage().get(0).getComponent().getPayload().setPayload(payInner.getChildTemplate().getPayload());
                        BotResponse botRes = (BotResponse) baseBotMessage;

                        composeFooterInterface.showMentionNarratorContainer(true, narrateText, composeText, botRes);
                    }else {
                        composeFooterInterface.showMentionNarratorContainer(true, narrateText,composeText,null);
                    }
                }*/ else if (BotResponse.TEMPLATE_TYPE_CONVERSATION_END.equalsIgnoreCase(payInner.getTemplate_type())) {
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
                } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.populateErrorText(payInner.getText(), payInner.getColor());
                } else if (BotResponse.TEMPLATE_TYPE_LIST_WIDGET_LOCATION.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (nearByStockAvailableStoreListView == null) {
                        nearByStockAvailableStoreListView = ViewProvider.getNearByStockAvailableStoreListView(context);
                        nearByStockAvailableStoreListView.setComposeFooterInterface(composeFooterInterface);
                        nearByStockAvailableStoreListView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        addView(nearByStockAvailableStoreListView);
                    }
                    nearByStockAvailableStoreListView.setVisibility(View.VISIBLE);
                    nearByStockAvailableStoreListView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                    nearByStockAvailableStoreListView.populateNearByStockAvailableStores(payInner.getNearByStockAvailableStores(), isLastItem);
                } else if (BotResponse.TEMPLATE_PROD_INVENTORY_ACTION_FORM.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (isLastItem) {
                        if (productInventoryActionFormView == null) {
                            productInventoryActionFormView = ViewProvider.getProductInventoryActionFormView(context);
                            productInventoryActionFormView.setComposeFooterInterface(composeFooterInterface);
                            addView(productInventoryActionFormView);
                        }
                        productInventoryActionFormView.setVisibility(View.VISIBLE);
                        productInventoryActionFormView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth() - BUBBLE_CONTENT_RIGHT_LIST_MARGIN);
                        productInventoryActionFormView.showInputForm(payInner.getProductInventoryActionFormTitle());
                    }
                } else if (BotResponse.ARTICLE_LIST_TEMPLATE.equalsIgnoreCase(payInner.getTemplate_type())) {
                    if (articleTemplateView == null) {
                        articleTemplateView = ViewProvider.getArticleListTemplateView(context);
                        articleTemplateView.setComposeFooterInterface(composeFooterInterface);
                        articleTemplateView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        addView(articleTemplateView);
                    }
                    articleTemplateView.setVisibility(View.VISIBLE);
                    articleTemplateView.setRestrictedMaxWidth(BUBBLE_CONTENT_LEFT_MARGIN + BubbleViewUtil.getBubbleContentWidth());
                    articleTemplateView.populateArticleListTemplateView(payInner);
                } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
                    if (!BotResponse.TEMPLATE_TYPE_DATE.equalsIgnoreCase(payInner.getTemplate_type()))
                        bubbleTextMediaLayout.populateText(payInner.getText());
                    else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getText_message()))
                        bubbleTextMediaLayout.populateText(payInner.getText_message());
                } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getTemplate_type())) {
                    bubbleTextMediaLayout.populateText(payInner.getTemplate_type());
                } else if (StringUtils.isNullOrEmptyWithTrim(payOuter.getText())) {
                    timeStampsTextView.setText("");
                }

            } else if (BotResponse.COMPONENT_TYPE_MESSAGE.equalsIgnoreCase(payOuter.getType()) && payInner != null) {
                Log.i("TemplateType", "payOuter.getType() --->:" + payOuter.getType());
                if (!StringUtils.isNullOrEmpty(payInner.getVideoUrl())) {
                    imageTemplateView.setVisibility(View.VISIBLE);
                    imageTemplateView.populateData(payInner, BotResponse.COMPONENT_TYPE_VIDEO);
                } else if (!StringUtils.isNullOrEmpty(payInner.getAudioUrl())) {
                    imageTemplateView.setVisibility(View.VISIBLE);
                    imageTemplateView.populateData(payInner, BotResponse.COMPONENT_TYPE_AUDIO);
                } else if (!StringUtils.isNullOrEmpty(payInner.getText())) {
                    bubbleTextMediaLayout.populateText(payInner.getText());
                }

            } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType()) && payInner != null) {
                bubbleTextMediaLayout.populateText(payInner.getText());
            } else if ((BotResponse.COMPONENT_TYPE_IMAGE.equalsIgnoreCase(payOuter.getType())
                    || BotResponse.COMPONENT_TYPE_AUDIO.equalsIgnoreCase(payOuter.getType())
                    || BotResponse.COMPONENT_TYPE_VIDEO.equalsIgnoreCase(payOuter.getType())) && payInner != null) {
                imageTemplateView.setVisibility(View.VISIBLE);
                imageTemplateView.populateData(payInner, payOuter.getType());
            } else {
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

    private void initVerticalListView() {
        if (verticalListView == null) {
            verticalListView = ViewProvider.getVerticalListView(context);
            verticalListView.setComposeFooterInterface(composeFooterInterface);
            verticalListView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            addView(verticalListView);
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
        if (bubbleTextMediaLayout != null) {
            MeasureUtils.measure(bubbleTextMediaLayout, childWidthSpec, wrapSpec);
        }
        // MeasureUtils.measure(meetingSlotsView, childWidthSpec, wrapSpec);
        if (timeStampsTextView != null) {
            MeasureUtils.measure(timeStampsTextView, wrapSpec, wrapSpec);
        }
        if (timeLineView != null) {
            MeasureUtils.measure(timeLineView, fullWidthSpec, wrapSpec);
        }

        /*
         * For Sender icon [CPV]
         */
        if (cpvSenderImage != null) {
            float cpvSenderImageDimen = dp1 * 21;
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) cpvSenderImageDimen, MeasureSpec.EXACTLY);
            childHeightSpec = MeasureSpec.makeMeasureSpec((int) cpvSenderImageDimen, MeasureSpec.EXACTLY);
            cpvSenderImage.setDimens(cpvSenderImageDimen, cpvSenderImageDimen);
            MeasureUtils.measure(cpvSenderImage, childWidthSpec, childHeightSpec);
        }

        /*
         * For List Templates
         */
        if (botListTemplateView != null) {
            MeasureUtils.measure(botListTemplateView, wrapSpec, wrapSpec);
        }
        if (botContactTemplateView != null) {
            MeasureUtils.measure(botContactTemplateView, wrapSpec, wrapSpec);
        }
        /*
         * For List View Templates
         */
//        MeasureUtils.measure(botListViewTemplateView, wrapSpec, wrapSpec);

        /*For calendar events*/

        /**
         * For PieChart
         */
        if (botPieChartView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
            childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
            MeasureUtils.measure(botPieChartView, childWidthSpec, childHeightSpec);
        }

        /*
         * For List View Templates
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
        if (botListViewTemplateView != null) {
            MeasureUtils.measure(botListViewTemplateView, childWidthSpec, wrapSpec);
        }

        /*
         * For List Widget Templates
         */

        if (botListWidgetTemplateView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
            MeasureUtils.measure(botListWidgetTemplateView, childWidthSpec, wrapSpec);
        }

        /**
         * For TableViev
         */

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (tableHeight), MeasureSpec.EXACTLY);
        if (tableView != null) {
            MeasureUtils.measure(tableView, childWidthSpec, wrapSpec);
        }
        if (botCustomTableView != null) {
            MeasureUtils.measure(botCustomTableView, childWidthSpec, wrapSpec);
        }

        /**
         * For Responsive TableViev
         */

        if (responsiveTableView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (respTableViewHeight), MeasureSpec.EXACTLY);
//        MeasureUtils.measure(responsiveTableView, childWidthSpec,childHeightSpec);
            MeasureUtils.measure(responsiveTableView, childWidthSpec, wrapSpec);
        }

        /**
         * For Responsive TableViev
         */

        if (botFormTemplateView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (respTableViewHeight), MeasureSpec.EXACTLY);
//        MeasureUtils.measure(responsiveTableView, childWidthSpec,childHeightSpec);
            MeasureUtils.measure(botFormTemplateView, childWidthSpec, wrapSpec);
        }

        /**
         * For Bot Form TableViev
         */

        if (responsiveExpandTableView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (respTableViewHeight), MeasureSpec.EXACTLY);
//        MeasureUtils.measure(responsiveExpandTableView, childWidthSpec,childHeightSpec);
            MeasureUtils.measure(responsiveExpandTableView, childWidthSpec, wrapSpec);
        }

        /**
         * For minitable
         */
        if (koraCarouselView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (koraCarouselView.getMeasuredHeight()), MeasureSpec.EXACTLY);
            MeasureUtils.measure(koraCarouselView, childWidthSpec, wrapSpec);
        }

        /**
         * for line chart
         */

        if (lineChartView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
            childHeightSpec = MeasureSpec.makeMeasureSpec((int) lineHeight, MeasureSpec.EXACTLY);
            MeasureUtils.measure(lineChartView, childWidthSpec, childHeightSpec);
        }

        /**
         * For BarChart
         */
        if (barChartView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
            childHeightSpec = MeasureSpec.makeMeasureSpec((int) (lineHeight), MeasureSpec.EXACTLY);
            MeasureUtils.measure(barChartView, childWidthSpec, childHeightSpec);
        }

        /**
         * For Horizontal BarChart
         */
        if (horizontalBarChartView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
            childHeightSpec = MeasureSpec.makeMeasureSpec((int) (lineHeight), MeasureSpec.EXACTLY);
            MeasureUtils.measure(horizontalBarChartView, childWidthSpec, childHeightSpec);
        }

        /**
         * For stacked BarChart
         */
        if (stackedBarChatView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 40 * (int) dp1, MeasureSpec.EXACTLY);
            childHeightSpec = MeasureSpec.makeMeasureSpec((int) (lineHeight), MeasureSpec.EXACTLY);
            MeasureUtils.measure(stackedBarChatView, childWidthSpec, childHeightSpec);
        }

        /*
         * For Table List Templates
         */
        if (botTableListTemplateView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
            MeasureUtils.measure(botTableListTemplateView, childWidthSpec, wrapSpec);
        }

        /*
         * For Agent Transfer Templates
         */
        if (agentTransferTemplateView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
            MeasureUtils.measure(agentTransferTemplateView, childWidthSpec, wrapSpec);
        }

        /*
         * For Table List Templates
         */
        if (botQuickRepliesTemplateView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
//        childHeightSpec = MeasureSpec.makeMeasureSpec((int) (pieViewHeight), MeasureSpec.EXACTLY);
            MeasureUtils.measure(botQuickRepliesTemplateView, childWidthSpec, wrapSpec);
        }

        /*
         * For Widget List Templates
         */
        if (listWidgetView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
            MeasureUtils.measure(listWidgetView, childWidthSpec, wrapSpec);
        }

        /*
         * For Banking feedback Templates
         */
        if (bankingFeedbackTemplateView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - 70 * (int) dp1, MeasureSpec.EXACTLY);
            MeasureUtils.measure(bankingFeedbackTemplateView, childWidthSpec, wrapSpec);
        }

        if (meetingSlotsView != null) {
            MeasureUtils.measure(meetingSlotsView, childWidthSpec, wrapSpec);
        }
        if (multiSelectView != null) {
            MeasureUtils.measure(multiSelectView, childWidthSpec, wrapSpec);
        }
        if (botButtonView != null) {
            MeasureUtils.measure(botButtonView, childWidthSpec, wrapSpec);
        }
        if (botCarouselView != null) {
            MeasureUtils.measure(botCarouselView, childWidthSpec, wrapSpec);
        }
        if (meetingConfirmationView != null) {
            MeasureUtils.measure(meetingConfirmationView, childWidthSpec, wrapSpec);
        }
        if (verticalListView != null) {
            MeasureUtils.measure(verticalListView, childWidthSpec, wrapSpec);
        }
        if (attendeeSlotSelectionView != null) {
            MeasureUtils.measure(attendeeSlotSelectionView, childWidthSpec, wrapSpec);
        }
        if (contactInfoView != null) {
            MeasureUtils.measure(contactInfoView, childWidthSpec, wrapSpec);
        }
        if (welcomeSummaryView != null) {
            MeasureUtils.measure(welcomeSummaryView, childWidthSpec, wrapSpec);
        }

        if (universalSearchView != null) {
            MeasureUtils.measure(universalSearchView, childWidthSpec, wrapSpec);
        }

        if (koraSummaryHelpView != null) {
            MeasureUtils.measure(koraSummaryHelpView, childWidthSpec, wrapSpec);
        }
        if (feedbackTemplateView != null) {
            MeasureUtils.measure(feedbackTemplateView, childWidthSpec, wrapSpec);
        }
        if (botDropDownTemplateView != null) {
            MeasureUtils.measure(botDropDownTemplateView, childWidthSpec, wrapSpec);
        }

        /*
         * For Widget List Templates
         */
        if (imageTemplateView != null) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) screenWidth - (50 * (int) dp1), MeasureSpec.EXACTLY);
            MeasureUtils.measure(imageTemplateView, childWidthSpec, wrapSpec);
        }
        if (nearByStockAvailableStoreListView != null) {
            MeasureUtils.measure(nearByStockAvailableStoreListView, wrapSpec, wrapSpec);
        }
        if (productInventoryActionFormView != null) {
            MeasureUtils.measure(productInventoryActionFormView, wrapSpec, wrapSpec);
        }

        if (advancedListTemplateView != null) {
            MeasureUtils.measure(advancedListTemplateView, wrapSpec, wrapSpec);
        }

        if (articleTemplateView != null) {
            MeasureUtils.measure(articleTemplateView, wrapSpec, wrapSpec);
        }

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

        if (botButtonView != null) {
            layoutView(botButtonView, top, left, arrayList);
        }
        if (meetingSlotsView != null) {
            layoutView(meetingSlotsView, top, left, arrayList);
        }
        if (multiSelectView != null) {
            layoutView(multiSelectView, top, left, arrayList);
        }
        if (attendeeSlotSelectionView != null) {
            layoutView(attendeeSlotSelectionView, top, (int) (left - dp1), arrayList);
        }
        if (meetingConfirmationView != null) {
            layoutView(meetingConfirmationView, top, (int) (left - dp1), arrayList);
        }
        if (botCarouselView != null) {
            layoutView(botCarouselView, top, left, arrayList);
        }
        if (verticalListView != null) {
            layoutView(verticalListView, top, (int) (left - dp1), arrayList);
        }
        if (botListTemplateView != null) {
            layoutView(botListTemplateView, top, left, arrayList);
        }
        if (botPieChartView != null) {
            layoutView(botPieChartView, top, left, arrayList);
        }
        if (tableView != null) {
            layoutView(tableView, top, left, arrayList);
        }
        if (botCustomTableView != null) {
            layoutView(botCustomTableView, top, left, arrayList);
        }
        if (responsiveTableView != null) {
            layoutView(responsiveTableView, top, left, arrayList);
        }
        if (responsiveExpandTableView != null) {
            layoutView(responsiveExpandTableView, top, left, arrayList);
        }
        if (lineChartView != null) {
            layoutView(lineChartView, top, left, arrayList);
        }
        if (barChartView != null) {
            layoutView(barChartView, top, left, arrayList);
        }
        if (horizontalBarChartView != null) {
            layoutView(horizontalBarChartView, top, left, arrayList);
        }
        if (stackedBarChatView != null) {
            layoutView(stackedBarChatView, top, left, arrayList);
        }
        if (contactInfoView != null) {
            layoutView(contactInfoView, top, left, arrayList);
        }
        if (welcomeSummaryView != null) {
            layoutView(welcomeSummaryView, top, left, arrayList);
        }
        if (universalSearchView != null) {
            layoutView(universalSearchView, top, left, arrayList);
        }
        if (koraSummaryHelpView != null) {
            layoutView(koraSummaryHelpView, top, left, arrayList);
        }
        if (koraCarouselView != null) {
            layoutView(koraCarouselView, top, left, arrayList);
        }
        if (botFormTemplateView != null) {
            layoutView(botFormTemplateView, top, left, arrayList);
        }
        if (botListViewTemplateView != null) {
            layoutView(botListViewTemplateView, top, left, arrayList);
        }
        if (botListWidgetTemplateView != null) {
            layoutView(botListWidgetTemplateView, top, left, arrayList);
        }
        if (botTableListTemplateView != null) {
            layoutView(botTableListTemplateView, top, left, arrayList);
        }
        if (botQuickRepliesTemplateView != null) {
            layoutView(botQuickRepliesTemplateView, top, left, arrayList);
        }
        if (agentTransferTemplateView != null) {
            layoutView(agentTransferTemplateView, top, left, arrayList);
        }
        if (feedbackTemplateView != null) {
            layoutView(feedbackTemplateView, top, left, arrayList);
        }
        if (listWidgetView != null) {
            layoutView(listWidgetView, top, left, arrayList);
        }
        if (botDropDownTemplateView != null) {
            layoutView(botDropDownTemplateView, top, left, arrayList);
        }
        if (imageTemplateView != null) {
            layoutView(imageTemplateView, top, left, arrayList);
        }
        if (bankingFeedbackTemplateView != null) {
            layoutView(bankingFeedbackTemplateView, top, left, arrayList);
        }
        if (botContactTemplateView != null) {
            layoutView(botContactTemplateView, top, left, arrayList);
        }
        if (advancedListTemplateView != null) {
            layoutView(advancedListTemplateView, top, left, arrayList);
        }
        if (nearByStockAvailableStoreListView != null) {
            layoutView(nearByStockAvailableStoreListView, top, left, arrayList);
        }
        if (productInventoryActionFormView != null) {
            layoutView(productInventoryActionFormView, top, left, arrayList);
        }
        if (articleTemplateView != null) {
            layoutView(articleTemplateView, top, left, arrayList);
        }

        left = bubbleTextMediaLayout.getLeft();
        top = Collections.max(arrayList);
        LayoutUtils.layoutChild(cpvSenderImage, left, top);
        if (cpvSenderImage.getMeasuredWidth() > 0) {
            left = cpvSenderImage.getRight() + (int) (9 * dp1);
            top = top + (int) (1 * dp1);
        }

        LayoutUtils.layoutChild(timeStampsTextView, left, top);
        LayoutUtils.layoutChild(timeLineView, 0, top);

        if (botCarouselView != null) {
            botCarouselView.bringToFront();
        }
        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...

    }

    private void layoutView(View view, int top, int left, ArrayList<Integer> arrayList) {
        if (view.getVisibility() == VISIBLE) {
            LayoutUtils.layoutChild(view, left, top);
            arrayList.add(view.getBottom());
        }
    }
}

