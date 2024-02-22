package kore.botssdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.view.AdvancedListTemplateView;
import kore.botssdk.view.AgentTransferTemplateView;
import kore.botssdk.view.AttendeeSlotSelectionView;
import kore.botssdk.view.BankingFeedbackTemplateView;
import kore.botssdk.view.BarChartView;
import kore.botssdk.view.BotBeneficiaryTemplateView;
import kore.botssdk.view.BotButtonLinkTemplateView;
import kore.botssdk.view.BotButtonView;
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
import kore.botssdk.view.ContactInfoView;
import kore.botssdk.view.EmptyTemplateView;
import kore.botssdk.view.FeedbackTemplateView;
import kore.botssdk.view.HorizontalBarChartView;
import kore.botssdk.view.ImageTemplateView;
import kore.botssdk.view.KoraCarouselView;
import kore.botssdk.view.KoraSummaryHelpView;
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
import kore.botssdk.view.WelcomeSummaryView;

/**
 * Created by Shiva Krishna on 11/20/2017.
 */
@SuppressLint("UnknownNullness")
public class ViewProvider {
    private static final int TEXTVIEW_ID = 1980081;
    private static final int LIST_ID = 1980045;
    private static final int CAROUSEL_VIEW_ID = 1980053;
    private static final int BUTTON_VIEW_ID = 1980098;
    private static final int PIECHART_VIEW_ID = 19800123;
    private static final int TABLE_VIEW_ID = 19800345;
    private static final int LINECHART_VIEW_ID = 19800335;
    private static final int KORA_CAROUSEL_VIEW_ID = 1980050;
    private static final int MEETING_SLOTS_VIEW_ID = 1980089;
    private static final int MULTI_SELECT_VIEW_ID = 1980090;
    private static final int MEETING_CONFIRMATION_VIEW_ID = 1980032;
    private static final int CONTACT_VIEW_ID = 19800456;
    private static final int WELCOME_SUMMARY_VIEW_ID = 19800786;
    private static final int KORA_SUMMARY_HELP_VIEW_ID = 19800787;
    private static final int FILES_CAROUSAL_VIEW_ID = 19800678;
    private static final int ATTENDEE_SLOT_VIEW_ID = 1980075;
    private static final int TIMELINE_VIEW_ID = 1980094;
    private static final int UNIVERSAL_SEARCH_VIEW_ID = 1980099;
    private static final int TABLE_RESPONSIVE_VIEW_ID = 19800350;


    public static BotButtonView getBotButtonView(Context context, ComposeFooterInterface listener) {
        BotButtonView botButtonView = new BotButtonView(context);
        botButtonView.setId(BUTTON_VIEW_ID);
        botButtonView.setComposeFooterInterface(listener);
        return botButtonView;
    }

    public static TextMediaLayout getTextMediaLayout(Context context, int linkColors){
        TextMediaLayout bubbleTextMediaLayout = new TextMediaLayout(context,linkColors);
        bubbleTextMediaLayout.setId(BubbleConstants.TEXT_MEDIA_LAYOUT_ID);
        return bubbleTextMediaLayout;
    }
    public static BotListTemplateView getBotListTempleteView(Context context){
        BotListTemplateView botListTemplateView = new BotListTemplateView(context);
        botListTemplateView.setId(LIST_ID);
        return botListTemplateView;
    }

    public static BotCarouselView getBotCarousalView(Context context){
        BotCarouselView botCarouselView = new BotCarouselView(context);
        botCarouselView.setId(CAROUSEL_VIEW_ID);
        return botCarouselView;
    }
    public static KoraCarouselView getKoraCarouselView(Context context){
        KoraCarouselView koraCarouselView = new KoraCarouselView(context);
        koraCarouselView.setId(KORA_CAROUSEL_VIEW_ID);
        return koraCarouselView;
    }
    public static PieChartView getPieChartView(Context context){
        PieChartView botPieChartView = new PieChartView(context);
        botPieChartView.setId(PIECHART_VIEW_ID);
        return botPieChartView;
    }
    public static BotTableView getTableView(Context context){
        BotTableView tableView = new BotTableView(context);
        tableView.setId(TABLE_VIEW_ID);
        return tableView;
    }
    public static BotCustomTableView getCustomTableView(Context context){
        BotCustomTableView tableView = new BotCustomTableView(context);
        tableView.setId(TABLE_VIEW_ID);
        return tableView;
    }
    public static BotResponsiveExpandTableView getResponsiveExpandTableView(Context context){
        BotResponsiveExpandTableView tableView = new BotResponsiveExpandTableView(context);
        tableView.setId(TABLE_RESPONSIVE_VIEW_ID);
        return tableView;
    }
    public static BotResponsiveTableView getResponsiveTableView(Context context){
        BotResponsiveTableView tableView = new BotResponsiveTableView(context);
        tableView.setId(TABLE_RESPONSIVE_VIEW_ID);
        return tableView;
    }
    public static LineChartView getLineChartView(Context context){
        LineChartView lineChartView = new LineChartView(context);
        lineChartView.setId(LINECHART_VIEW_ID);
        return lineChartView;
    }

    public static BarChartView getBarChartView(Context context){
        BarChartView barChartView = new BarChartView(context);
        barChartView.setId(BubbleConstants.BARCHART_VIEW_ID);
       return  barChartView;
    }

    public static StackedBarChatView getStackedBarChartView(Context context){
        StackedBarChatView barChartView = new StackedBarChatView(context);
        barChartView.setId(BubbleConstants.STACK_BARCHAT_VIEW_ID);
        return  barChartView;
    }

    public static MeetingSlotsView getMeetingSlotsView(Context context){
        MeetingSlotsView meetingSlotsView = new MeetingSlotsView(context);
        meetingSlotsView.setId(MEETING_SLOTS_VIEW_ID);
        return meetingSlotsView;
    }
    public static BotContactTemplateView getBotContactView(Context context, ComposeFooterInterface listener) {
        BotContactTemplateView botButtonView = new BotContactTemplateView(context);
        botButtonView.setId(BUTTON_VIEW_ID);
        botButtonView.setComposeFooterInterface(listener);
        return botButtonView;
    }
    public static MultiSelectView getMultiSelectView(Context context){
        MultiSelectView multiSelectView = new MultiSelectView(context);
        multiSelectView.setId(MULTI_SELECT_VIEW_ID);
        return multiSelectView;
    }
    public static MeetingConfirmationView getMeetingConfirmationView(Context context){
        MeetingConfirmationView meetingConfirmationView = new MeetingConfirmationView(context);
        meetingConfirmationView.setId(MEETING_CONFIRMATION_VIEW_ID);
        return meetingConfirmationView;
    }


    public static AttendeeSlotSelectionView getAttendeeSlotSelectionView(Context context){
        AttendeeSlotSelectionView attendeeSlotSelectionView = new AttendeeSlotSelectionView(context);
        attendeeSlotSelectionView.setId(ATTENDEE_SLOT_VIEW_ID);
        return attendeeSlotSelectionView;
    }

    public static ContactInfoView getContactInfoView(Context context){
        ContactInfoView contactInfoView = new ContactInfoView(context);
        contactInfoView.setId(CONTACT_VIEW_ID);
        return contactInfoView;
    }

    public static WelcomeSummaryView getWelcomeSummaryView(Context context){
        WelcomeSummaryView welcomeSummaryView = new WelcomeSummaryView(context,"");
        welcomeSummaryView.setId(WELCOME_SUMMARY_VIEW_ID);
        return welcomeSummaryView;
    }



    public static UniversalSearchView getUniversalSearchView(Context context){
        UniversalSearchView universalSearchView = new UniversalSearchView(context);
        universalSearchView.setId(UNIVERSAL_SEARCH_VIEW_ID);
        return universalSearchView;
    }

    public static KoraSummaryHelpView getKoraSummaryHelpView(Context context){
        KoraSummaryHelpView koraSummaryHelpView = new KoraSummaryHelpView(context);
        koraSummaryHelpView.setId(KORA_SUMMARY_HELP_VIEW_ID);
        return koraSummaryHelpView;
    }

    public static VerticalListView getVerticalListView(Context context){
        VerticalListView koraCarouselView = new VerticalListView(context);
        koraCarouselView.setId(FILES_CAROUSAL_VIEW_ID);
        return koraCarouselView;
    }

    public static TextView getTimeStampTextView(Context context){
        TextView textView = new TextView(context);
        textView.setId(TEXTVIEW_ID);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
        textView.setTextColor(Color.parseColor("#B0B0B0"));
        textView.setTag(KaFontUtils.ROBOTO_MEDIUM);
        KaFontUtils.setCustomTypeface(textView, KaFontUtils.ROBOTO_MEDIUM,context);
        return textView;
    }

    public static TimeLineTextView getTimeLineView(Context context){
        TimeLineTextView textView = new TimeLineTextView(context);
        textView.setId(TIMELINE_VIEW_ID);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setTag(KaFontUtils.ROBOTO_MEDIUM);
        return textView;
    }

    //Added by Sudheer

    public static HorizontalBarChartView getHorizontalBarChartView(Context context){
        HorizontalBarChartView horizontalBarChartView = new HorizontalBarChartView(context);
        horizontalBarChartView.setId(BubbleConstants.HORIZONTAL_BARCHART_VIEW_ID);
        return  horizontalBarChartView;
    }

    public static BotFormTemplateView getBotFormTemplateView(Context context){
        BotFormTemplateView botFormTemplateView = new BotFormTemplateView(context);
        botFormTemplateView.setId(BubbleConstants.FORM_TEMPLATE_ID);
        return  botFormTemplateView;
    }

    public static FeedbackTemplateView getFeedbackTemplateView(Context context){
        FeedbackTemplateView feedbackTemplateView = new FeedbackTemplateView(context);
        feedbackTemplateView.setId(BubbleConstants.FORM_TEMPLATE_ID);
        return  feedbackTemplateView;
    }

    public static ListWidgetView getListWidgetTemplateView(Context context){
        ListWidgetView feedbackTemplateView = new ListWidgetView(context);
        feedbackTemplateView.setId(BubbleConstants.FORM_TEMPLATE_ID);
        return  feedbackTemplateView;
    }

    public static BotDropDownTemplateView getDropDownTemplateView(Context context){
        BotDropDownTemplateView botDropDownTemplateView = new BotDropDownTemplateView(context);
        botDropDownTemplateView.setId(BubbleConstants.FORM_TEMPLATE_ID);
        return  botDropDownTemplateView;
    }

    public static ImageTemplateView getImageTemplateView(Context context){
        ImageTemplateView imageTemplateView = new ImageTemplateView(context);
        imageTemplateView.setId(BubbleConstants.FORM_TEMPLATE_ID);
        return  imageTemplateView;
    }

    public static BankingFeedbackTemplateView getBankingFeedbackTemplateView(Context context){
        BankingFeedbackTemplateView feedbackTemplateView = new BankingFeedbackTemplateView(context);
        feedbackTemplateView.setId(BubbleConstants.FEEDBACK_TEMPLATE_ID);
        return  feedbackTemplateView;
    }

    public static BotListViewTemplateView getBotListViewTempleteView(Context context){
        BotListViewTemplateView botListTemplateView = new BotListViewTemplateView(context);
        botListTemplateView.setId(LIST_ID);
        return botListTemplateView;
    }

    public static BotListWidgetTemplateView getBotListWidgetTempleteView(Context context){
        BotListWidgetTemplateView botListTemplateView = new BotListWidgetTemplateView(context);
        botListTemplateView.setId(LIST_ID);
        return botListTemplateView;
    }

    public static BotTableListTemplateView getBotTableListTempleteView(Context context){
        BotTableListTemplateView botTableListTemplateView = new BotTableListTemplateView(context);
        botTableListTemplateView.setId(BubbleConstants.TABLE_LIST_TEMPLATE_ID);
        return botTableListTemplateView;
    }

    public static BotQuickRepliesTemplateView getBotQuickRepliesTemplateView(Context context){
        BotQuickRepliesTemplateView botTableListTemplateView = new BotQuickRepliesTemplateView(context);
        botTableListTemplateView.setId(BubbleConstants.QUICK_REPLY_TEMPLATE_ID);
        return botTableListTemplateView;
    }

    public static AgentTransferTemplateView getAgentTransferTemplateView(Context context){
        AgentTransferTemplateView agentTransferTemplateView = new AgentTransferTemplateView(context);
        agentTransferTemplateView.setId(BubbleConstants.AGENT_TRANSFER_TEMPLATE_ID);
        return agentTransferTemplateView;
    }

    public static LinkTemplateView getLinkTemplateView(Context context) {
        LinkTemplateView linkTemplateView = new LinkTemplateView(context);
        linkTemplateView.setId(BubbleConstants.LIST_ID);
        return linkTemplateView;
    }
    public static AdvancedListTemplateView getAdvancedListTemplateView(Context context){
        AdvancedListTemplateView feedbackTemplateView = new AdvancedListTemplateView(context);
        feedbackTemplateView.setId(BubbleConstants.FEEDBACK_TEMPLATE_ID);
        return  feedbackTemplateView;
    }
    public static ResultsTemplateView getResultsTemplateView(Context context){
        ResultsTemplateView resultsTemplateView = new ResultsTemplateView(context);
        resultsTemplateView.setId(MULTI_SELECT_VIEW_ID);
        return resultsTemplateView;
    }
    public static PdfDownloadView getPdfListView(Context context){
        PdfDownloadView multiSelectView = new PdfDownloadView(context);
        multiSelectView.setId(MULTI_SELECT_VIEW_ID);
        return multiSelectView;
    }
    public static BotButtonLinkTemplateView getBotButtonLinkView(Context context, ComposeFooterInterface listener) {
        BotButtonLinkTemplateView botButtonView = new BotButtonLinkTemplateView(context);
        botButtonView.setId(BUTTON_VIEW_ID);
        botButtonView.setComposeFooterInterface(listener);
        return botButtonView;
    }
    public static BotBeneficiaryTemplateView getBotBeneficiaryTemplateView(Context context){
        BotBeneficiaryTemplateView botListTemplateView = new BotBeneficiaryTemplateView(context);
        botListTemplateView.setId(LIST_ID);
        return botListTemplateView;
    }

    public static ButtonDeepLinkTemplateView getButtonDeepLinkTemplateView(Context context){
        ButtonDeepLinkTemplateView multiSelectView = new ButtonDeepLinkTemplateView(context);
        multiSelectView.setId(MULTI_SELECT_VIEW_ID);
        return multiSelectView;
    }
    public static CardTemplateView getCardTemplateView(Context context){
        CardTemplateView multiSelectView = new CardTemplateView(context);
        multiSelectView.setId(MULTI_SELECT_VIEW_ID);
        return multiSelectView;
    }

    public static EmptyTemplateView getEmptyTemplateView(Context context){
        EmptyTemplateView multiSelectView = new EmptyTemplateView(context);
        multiSelectView.setId(LIST_ID);
        return multiSelectView;
    }

}
