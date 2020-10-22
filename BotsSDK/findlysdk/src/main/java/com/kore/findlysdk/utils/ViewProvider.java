package com.kore.findlysdk.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.FormActionTemplate;
import com.kore.findlysdk.models.QuickReplyTemplate;
import com.kore.findlysdk.view.AgentTransferTemplateView;
import com.kore.findlysdk.view.BarChartView;
import com.kore.findlysdk.view.BotButtonView;
import com.kore.findlysdk.view.BotCarouselView;
import com.kore.findlysdk.view.BotFormTemplateView;
import com.kore.findlysdk.view.BotListTemplateView;
import com.kore.findlysdk.view.BotListViewTemplateView;
import com.kore.findlysdk.view.BotQuickRepliesTemplateView;
import com.kore.findlysdk.view.BotResponsiveExpandTableView;
import com.kore.findlysdk.view.BotResponsiveTableView;
import com.kore.findlysdk.view.BotTableListTemplateView;
import com.kore.findlysdk.view.BotTableView;
import com.kore.findlysdk.view.FeedbackTemplateView;
import com.kore.findlysdk.view.FormActionView;
import com.kore.findlysdk.view.HorizontalBarChartView;
import com.kore.findlysdk.view.LineChartView;
import com.kore.findlysdk.view.ListWidgetView;
import com.kore.findlysdk.view.MultiSelectView;
import com.kore.findlysdk.view.PieChartView;
import com.kore.findlysdk.view.QuickReplyFindlyView;
import com.kore.findlysdk.view.ResultsTemplateView;
import com.kore.findlysdk.view.StackedBarChatView;
import com.kore.findlysdk.view.TextMediaLayout;
import com.kore.findlysdk.view.TimeLineTextView;

import java.util.ArrayList;

/**
 * Created by Shiva Krishna on 11/20/2017.
 */

public class ViewProvider {
    private static final int TEXTVIEW_ID = 1980081;
    private static final int LIST_ID = 1980045;
    public static final int TEXT_MEDIA_LAYOUT_ID = 73733614;
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
    private static final int QUICK_RPVIEW = 1988881;
    private static final int TIMELINE_VIEW_ID = 1980094;
    private static final int UNIVERSAL_SEARCH_VIEW_ID = 1980099;
    public static final int TASK_VIEW_ID = 1981234;
    private static final int TABLE_RESPONSIVE_VIEW_ID = 19800350;


    public static Path RoundedRect(
            float left, float top, float right, float bottom, float rx, float ry,
            boolean tl, boolean tr, boolean br, boolean bl
    ){
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else{
            path.rLineTo(0, -ry);
            path.rLineTo(-rx,0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else{
            path.rLineTo(-rx, 0);
            path.rLineTo(0,ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else{
            path.rLineTo(0, ry);
            path.rLineTo(rx,0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else{
            path.rLineTo(rx,0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }
    public static void drawRoundRect(Canvas canvas, RectF rect, Paint paint,
                                    int leftTop, int rightTop, int leftBottom,
                                     int rightBottom) {
        float roundRadius[] = new float[8];
        roundRadius[0] = leftTop;
        roundRadius[1] = leftTop;
        roundRadius[2] = rightTop;
        roundRadius[3] = rightTop;
        roundRadius[4] = rightBottom;
        roundRadius[5] = rightBottom;
        roundRadius[6] = leftBottom;
        roundRadius[7] = leftBottom;

        Path path = new Path();
        path.addRoundRect(rect, roundRadius, Path.Direction.CCW);
        canvas.drawPath(path, paint);
    }


    public static QuickReplyFindlyView getQuickReplyView(Context context, ArrayList<QuickReplyTemplate> data, ComposeFooterInterface listener, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        if (context != null) {
            QuickReplyFindlyView quickReplyView = new QuickReplyFindlyView(context);
            quickReplyView.setId(QUICK_RPVIEW);
            quickReplyView.setComposeFooterInterface(listener);
            quickReplyView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            quickReplyView.populateQuickReplyView(data);
            return quickReplyView;
        } else {
            return null;
        }
    }

    public static FormActionView getFormActionView(Context context, ArrayList<FormActionTemplate> data, ComposeFooterInterface listener) {
        if (context != null) {
            FormActionView formActionView = new FormActionView(context);
            formActionView.setId(QUICK_RPVIEW);
            formActionView.setComposeFooterInterface(listener);
            formActionView.populateFormActionView(data);
            return formActionView;
        } else {
            return null;
        }
    }

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

    public static MultiSelectView getMultiSelectView(Context context){
        MultiSelectView multiSelectView = new MultiSelectView(context);
        multiSelectView.setId(MULTI_SELECT_VIEW_ID);
        return multiSelectView;
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
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        //textView.setTextColor(Color.parseColor("#B0B0B0"));
        textView.setTag(KaFontUtils.ROBOTO_MEDIUM);
      //  KaFontUtils.setCustomTypeface(textView,KaFontUtils.ROBOTO_MEDIUM,context);
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


    public static BotListViewTemplateView getBotListViewTempleteView(Context context){
        BotListViewTemplateView botListTemplateView = new BotListViewTemplateView(context);
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

    public static ResultsTemplateView getResultsTemplateView(Context context){
        ResultsTemplateView resultsTemplateView = new ResultsTemplateView(context);
        resultsTemplateView.setId(BubbleConstants.FORM_TEMPLATE_ID);
        return  resultsTemplateView;
    }

}
