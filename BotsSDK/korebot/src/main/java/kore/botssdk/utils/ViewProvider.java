package kore.botssdk.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.view.AdvancedListTemplateView;
import kore.botssdk.view.AdvancedMultiSelectView;
import kore.botssdk.view.AgentTransferTemplateView;
import kore.botssdk.view.AttendeeSlotSelectionView;
import kore.botssdk.view.BankingFeedbackTemplateView;
import kore.botssdk.view.BarChartView;
import kore.botssdk.view.BotBeneficiaryTemplateView;
import kore.botssdk.view.BotButtonLinkTemplateView;
import kore.botssdk.view.BotButtonView;
import kore.botssdk.view.BotCarouselStacked;
import kore.botssdk.view.BotCarouselView;
import kore.botssdk.view.BotContactTemplateView;
import kore.botssdk.view.BotCustomTableView;
import kore.botssdk.view.BotDropDownTemplateView;
import kore.botssdk.view.BotFormTemplateView;
import kore.botssdk.view.BotListTemplateView;
import kore.botssdk.view.BotListViewTemplateView;
import kore.botssdk.view.BotListWidgetTemplateView;
import kore.botssdk.view.BotQuickRepliesTemplateView;
import kore.botssdk.view.BotResponsiveExpandTableView;
import kore.botssdk.view.BotResponsiveTableView;
import kore.botssdk.view.BotTableListTemplateView;
import kore.botssdk.view.BotTableView;
import kore.botssdk.view.ButtonDeepLinkTemplateView;
import kore.botssdk.view.CardTemplateView;
import kore.botssdk.view.EmptyTemplateView;
import kore.botssdk.view.FeedbackTemplateView;
import kore.botssdk.view.HorizontalBarChartView;
import kore.botssdk.view.ImageTemplateView;
import kore.botssdk.view.KoraCarouselView;
import kore.botssdk.view.LineChartView;
import kore.botssdk.view.LinkTemplateView;
import kore.botssdk.view.ListWidgetView;
import kore.botssdk.view.MeetingConfirmationView;
import kore.botssdk.view.MeetingSlotsView;
import kore.botssdk.view.MultiSelectView;
import kore.botssdk.view.PdfDownloadView;
import kore.botssdk.view.PieChartView;
import kore.botssdk.view.ResultsTemplateView;
import kore.botssdk.view.StackedBarChatView;
import kore.botssdk.view.TextMediaLayout;
import kore.botssdk.view.TimeLineTextView;
import kore.botssdk.view.UniversalSearchView;
import kore.botssdk.view.VerticalListView;

@SuppressWarnings("UnKnownNullness")
public class ViewProvider {
    private static final int TEXTVIEW_ID = 1980001;
    private static final int LIST_ID = 1980002;
    private static final int CAROUSEL_VIEW_ID = 1980003;
    private static final int CAROUSEL_STACKED_VIEW_ID = 1980004;
    private static final int BUTTON_VIEW_ID = 1980005;
    private static final int PIE_CHART_VIEW_ID = 1980006;
    private static final int TABLE_VIEW_ID = 1980007;
    private static final int LINE_CHART_VIEW_ID = 1980008;
    private static final int KORA_CAROUSEL_VIEW_ID = 1980009;
    private static final int MEETING_SLOTS_VIEW_ID = 1980010;
    private static final int MULTI_SELECT_VIEW_ID = 1980011;
    private static final int MEETING_CONFIRMATION_VIEW_ID = 1980012;
    private static final int FILES_CAROUSAL_VIEW_ID = 1980016;
    private static final int ATTENDEE_SLOT_VIEW_ID = 1980017;
    private static final int QUICK_RP_VIEW = 1980018;
    private static final int TIMELINE_VIEW_ID = 1980019;
    private static final int UNIVERSAL_SEARCH_VIEW_ID = 1980020;
    private static final int TABLE_RESPONSIVE_VIEW_ID = 1980021;
    private static final int ADVANCE_MULTISELECT_VIEW_ID = 1980021;
    private static final int CUSTOM_TABLE_VIEW_ID = 1980022;
    private static final int TABLE_RESPONSIVE_EXPAND_VIEW_ID = 1980023;
    private static final int BOT_CONTACT_VIEW_ID = 1980024;
    private static final int FEEDBACK_VIEW_ID = 1980025;
    private static final int LIST_WIDGET_VIEW_ID = 1980026;
    private static final int DROP_DOWN_VIEW_ID = 1980027;
    private static final int IMAGE_VIEW_ID = 1980028;
    private static final int BANKING_FEEDBACK_VIEW_ID = 1980029;
    private static final int LIST_VIEW_VIEW_ID = 1980030;
    private static final int LIST_WIDGET_ID = 1980031;
    private static final int TABLE_LIST_VIEW_ID = 1980032;
    private static final int AGENT_TRANSFER_VIEW_ID = 1980033;
    private static final int LINK_VIEW_ID = 1980034;
    private static final int ADVANCE_LIST_VIEW_ID = 1980035;
    private static final int RESULTS_LIST_VIEW_ID = 1980036;
    private static final int PDF_LIST_VIEW_ID = 1980037;
    private static final int BUTTON_LINK_ID = 1980038;
    private static final int BENEFICIARY_VIEW_ID = 1980039;
    private static final int BUTTON_DEEP_LINK_VIEW_ID = 1980040;
    private static final int CARD_VIEW_ID = 1980041;
    private static final int EMPTY_CARD_VIEW_ID = 1980042;

    public static BotButtonView getBotButtonView(Context context, ComposeFooterInterface listener) {
        BotButtonView botButtonView = new BotButtonView(context);
        botButtonView.setId(BUTTON_VIEW_ID);
        botButtonView.setComposeFooterInterface(listener);
        return botButtonView;
    }

    public static TextMediaLayout getTextMediaLayout(Context context, int linkColors) {
        TextMediaLayout bubbleTextMediaLayout = new TextMediaLayout(context, linkColors);
        bubbleTextMediaLayout.setId(BubbleConstants.TEXT_MEDIA_LAYOUT_ID);
        return bubbleTextMediaLayout;
    }

    public static BotListTemplateView getBotListTemplateView(Context context) {
        BotListTemplateView botListTemplateView = new BotListTemplateView(context);
        botListTemplateView.setId(LIST_ID);
        return botListTemplateView;
    }

    public static BotCarouselView getBotCarousalView(Context context) {
        BotCarouselView botCarouselView = new BotCarouselView(context);
        botCarouselView.setId(CAROUSEL_VIEW_ID);
        return botCarouselView;
    }

    public static BotCarouselStacked getBotCarousalStacked(Context context) {
        BotCarouselStacked botCarouselView = new BotCarouselStacked(context);
        botCarouselView.setId(CAROUSEL_STACKED_VIEW_ID);
        return botCarouselView;
    }

    public static KoraCarouselView getKoraCarouselView(Context context) {
        KoraCarouselView koraCarouselView = new KoraCarouselView(context);
        koraCarouselView.setId(KORA_CAROUSEL_VIEW_ID);
        return koraCarouselView;
    }

    public static PieChartView getPieChartView(Context context) {
        PieChartView botPieChartView = new PieChartView(context);
        botPieChartView.setId(PIE_CHART_VIEW_ID);
        return botPieChartView;
    }

    public static BotTableView getTableView(Context context) {
        BotTableView tableView = new BotTableView(context);
        tableView.setId(TABLE_VIEW_ID);
        return tableView;
    }

    public static BotCustomTableView getCustomTableView(Context context) {
        BotCustomTableView tableView = new BotCustomTableView(context);
        tableView.setId(CUSTOM_TABLE_VIEW_ID);
        return tableView;
    }

    public static BotResponsiveExpandTableView getResponsiveExpandTableView(Context context) {
        BotResponsiveExpandTableView tableView = new BotResponsiveExpandTableView(context);
        tableView.setId(TABLE_RESPONSIVE_EXPAND_VIEW_ID);
        return tableView;
    }

    public static BotResponsiveTableView getResponsiveTableView(Context context) {
        BotResponsiveTableView tableView = new BotResponsiveTableView(context);
        tableView.setId(TABLE_RESPONSIVE_VIEW_ID);
        return tableView;
    }

    public static LineChartView getLineChartView(Context context) {
        LineChartView lineChartView = new LineChartView(context);
        lineChartView.setId(LINE_CHART_VIEW_ID);
        return lineChartView;
    }

    public static BarChartView getBarChartView(Context context) {
        BarChartView barChartView = new BarChartView(context);
        barChartView.setId(BubbleConstants.BARCHART_VIEW_ID);
        return barChartView;
    }

    public static StackedBarChatView getStackedBarChartView(Context context) {
        StackedBarChatView barChartView = new StackedBarChatView(context);
        barChartView.setId(BubbleConstants.STACK_BARCHAT_VIEW_ID);
        return barChartView;
    }

    public static MeetingSlotsView getMeetingSlotsView(Context context) {
        MeetingSlotsView meetingSlotsView = new MeetingSlotsView(context);
        meetingSlotsView.setId(MEETING_SLOTS_VIEW_ID);
        return meetingSlotsView;
    }

    public static BotContactTemplateView getBotContactView(Context context, ComposeFooterInterface listener) {
        BotContactTemplateView botButtonView = new BotContactTemplateView(context);
        botButtonView.setId(BOT_CONTACT_VIEW_ID);
        botButtonView.setComposeFooterInterface(listener);
        return botButtonView;
    }

    public static MultiSelectView getMultiSelectView(Context context) {
        MultiSelectView multiSelectView = new MultiSelectView(context);
        multiSelectView.setId(MULTI_SELECT_VIEW_ID);
        return multiSelectView;
    }

    public static MeetingConfirmationView getMeetingConfirmationView(Context context) {
        MeetingConfirmationView meetingConfirmationView = new MeetingConfirmationView(context);
        meetingConfirmationView.setId(MEETING_CONFIRMATION_VIEW_ID);
        return meetingConfirmationView;
    }

    public static AttendeeSlotSelectionView getAttendeeSlotSelectionView(Context context) {
        AttendeeSlotSelectionView attendeeSlotSelectionView = new AttendeeSlotSelectionView(context);
        attendeeSlotSelectionView.setId(ATTENDEE_SLOT_VIEW_ID);
        return attendeeSlotSelectionView;
    }

    public static UniversalSearchView getUniversalSearchView(Context context) {
        UniversalSearchView universalSearchView = new UniversalSearchView(context);
        universalSearchView.setId(UNIVERSAL_SEARCH_VIEW_ID);
        return universalSearchView;
    }

    public static VerticalListView getVerticalListView(Context context) {
        VerticalListView koraCarouselView = new VerticalListView(context);
        koraCarouselView.setId(FILES_CAROUSAL_VIEW_ID);
        return koraCarouselView;
    }

    public static TextView getTimeStampTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setId(TEXTVIEW_ID);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        textView.setTextColor(Color.parseColor("#B0B0B0"));
        textView.setTag(KaFontUtils.ROBOTO_MEDIUM);
        KaFontUtils.setCustomTypeface(textView, KaFontUtils.ROBOTO_MEDIUM, context);
        return textView;
    }

    public static TimeLineTextView getTimeLineView(Context context) {
        TimeLineTextView textView = new TimeLineTextView(context);
        textView.setId(TIMELINE_VIEW_ID);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setTag(KaFontUtils.ROBOTO_MEDIUM);
        return textView;
    }

    //Added by Sudheer
    public static HorizontalBarChartView getHorizontalBarChartView(Context context) {
        HorizontalBarChartView horizontalBarChartView = new HorizontalBarChartView(context);
        horizontalBarChartView.setId(BubbleConstants.HORIZONTAL_BARCHART_VIEW_ID);
        return horizontalBarChartView;
    }

    public static BotFormTemplateView getBotFormTemplateView(Context context) {
        BotFormTemplateView botFormTemplateView = new BotFormTemplateView(context);
        botFormTemplateView.setId(BubbleConstants.FORM_TEMPLATE_ID);
        return botFormTemplateView;
    }

    public static FeedbackTemplateView getFeedbackTemplateView(Context context) {
        FeedbackTemplateView feedbackTemplateView = new FeedbackTemplateView(context);
        feedbackTemplateView.setId(FEEDBACK_VIEW_ID);
        return feedbackTemplateView;
    }

    public static ListWidgetView getListWidgetTemplateView(Context context) {
        ListWidgetView feedbackTemplateView = new ListWidgetView(context);
        feedbackTemplateView.setId(LIST_WIDGET_VIEW_ID);
        return feedbackTemplateView;
    }

    public static BotDropDownTemplateView getDropDownTemplateView(Context context) {
        BotDropDownTemplateView botDropDownTemplateView = new BotDropDownTemplateView(context);
        botDropDownTemplateView.setId(DROP_DOWN_VIEW_ID);
        return botDropDownTemplateView;
    }

    public static ImageTemplateView getImageTemplateView(Context context) {
        ImageTemplateView imageTemplateView = new ImageTemplateView(context);
        imageTemplateView.setId(IMAGE_VIEW_ID);
        return imageTemplateView;
    }

    public static BankingFeedbackTemplateView getBankingFeedbackTemplateView(Context context) {
        BankingFeedbackTemplateView feedbackTemplateView = new BankingFeedbackTemplateView(context);
        feedbackTemplateView.setId(BANKING_FEEDBACK_VIEW_ID);
        return feedbackTemplateView;
    }

    public static BotListViewTemplateView getBotListViewTemplateView(Context context) {
        BotListViewTemplateView botListTemplateView = new BotListViewTemplateView(context);
        botListTemplateView.setId(LIST_VIEW_VIEW_ID);
        return botListTemplateView;
    }

    public static BotListWidgetTemplateView getBotListWidgetTemplateView(Context context) {
        BotListWidgetTemplateView botListTemplateView = new BotListWidgetTemplateView(context);
        botListTemplateView.setId(LIST_WIDGET_ID);
        return botListTemplateView;
    }

    public static BotTableListTemplateView getBotTableListTemplateView(Context context) {
        BotTableListTemplateView botTableListTemplateView = new BotTableListTemplateView(context);
        botTableListTemplateView.setId(TABLE_LIST_VIEW_ID);
        return botTableListTemplateView;
    }

    public static BotQuickRepliesTemplateView getBotQuickRepliesTemplateView(Context context) {
        BotQuickRepliesTemplateView botTableListTemplateView = new BotQuickRepliesTemplateView(context);
        botTableListTemplateView.setId(QUICK_RP_VIEW);
        return botTableListTemplateView;
    }

    public static AgentTransferTemplateView getAgentTransferTemplateView(Context context) {
        AgentTransferTemplateView agentTransferTemplateView = new AgentTransferTemplateView(context);
        agentTransferTemplateView.setId(AGENT_TRANSFER_VIEW_ID);
        return agentTransferTemplateView;
    }

    public static LinkTemplateView getLinkTemplateView(Context context) {
        LinkTemplateView linkTemplateView = new LinkTemplateView(context);
        linkTemplateView.setId(LINK_VIEW_ID);
        return linkTemplateView;
    }

    public static AdvancedListTemplateView getAdvancedListTemplateView(Context context) {
        AdvancedListTemplateView feedbackTemplateView = new AdvancedListTemplateView(context);
        feedbackTemplateView.setId(ADVANCE_LIST_VIEW_ID);
        return feedbackTemplateView;
    }

    public static ResultsTemplateView getResultsTemplateView(Context context) {
        ResultsTemplateView resultsTemplateView = new ResultsTemplateView(context);
        resultsTemplateView.setId(RESULTS_LIST_VIEW_ID);
        return resultsTemplateView;
    }

    public static PdfDownloadView getPdfListView(Context context) {
        PdfDownloadView multiSelectView = new PdfDownloadView(context);
        multiSelectView.setId(PDF_LIST_VIEW_ID);
        return multiSelectView;
    }

    public static BotButtonLinkTemplateView getBotButtonLinkView(Context context, ComposeFooterInterface listener) {
        BotButtonLinkTemplateView botButtonView = new BotButtonLinkTemplateView(context);
        botButtonView.setId(BUTTON_LINK_ID);
        botButtonView.setComposeFooterInterface(listener);
        return botButtonView;
    }

    public static BotBeneficiaryTemplateView getBotBeneficiaryTemplateView(Context context) {
        BotBeneficiaryTemplateView botListTemplateView = new BotBeneficiaryTemplateView(context);
        botListTemplateView.setId(BENEFICIARY_VIEW_ID);
        return botListTemplateView;
    }

    public static ButtonDeepLinkTemplateView getButtonDeepLinkTemplateView(Context context) {
        ButtonDeepLinkTemplateView multiSelectView = new ButtonDeepLinkTemplateView(context);
        multiSelectView.setId(BUTTON_DEEP_LINK_VIEW_ID);
        return multiSelectView;
    }

    public static CardTemplateView getCardTemplateView(Context context) {
        CardTemplateView multiSelectView = new CardTemplateView(context);
        multiSelectView.setId(CARD_VIEW_ID);
        return multiSelectView;
    }

    public static EmptyTemplateView getEmptyTemplateView(Context context) {
        EmptyTemplateView multiSelectView = new EmptyTemplateView(context);
        multiSelectView.setId(EMPTY_CARD_VIEW_ID);
        return multiSelectView;
    }

    public static AdvancedMultiSelectView getAdvancedMultiSelectView(Context context) {
        AdvancedMultiSelectView multiSelectView = new AdvancedMultiSelectView(context);
        multiSelectView.setId(ADVANCE_MULTISELECT_VIEW_ID);
        return multiSelectView;
    }

}
