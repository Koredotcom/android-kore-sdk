package kore.botssdk.utils;

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

import java.util.ArrayList;

import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.view.AttendeeSlotSelectionView;
import kore.botssdk.view.BotButtonView;
import kore.botssdk.view.BotCarouselView;
import kore.botssdk.view.BotListTemplateView;
import kore.botssdk.view.BotMainTableView;
import kore.botssdk.view.CalendarEventsTemplateView;
import kore.botssdk.view.FormActionView;
import kore.botssdk.view.KoraCarouselView;
import kore.botssdk.view.KoraFilesCarousalView;
import kore.botssdk.view.LineChartView;
import kore.botssdk.view.MeetingConfirmationView;
import kore.botssdk.view.MeetingSlotsView;
import kore.botssdk.view.PieChartView;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.view.TextMediaLayout;
import kore.botssdk.view.TimeLineTextView;

/**
 * Created by Shiva Krishna on 11/20/2017.
 */

public class ViewProvider {
    public static final int TEXTVIEW_ID = 1980081;
    public static final int LIST_ID = 1980045;
    public static final int TEXT_MEDIA_LAYOUT_ID = 73733614;
    public static final int CAROUSEL_VIEW_ID = 1980053;
    public static final int BUTTON_VIEW_ID = 1980098;
    public static final int PIECHART_VIEW_ID = 19800123;
    public static final int TABLE_VIEW_ID = 19800345;
    public static final int LINECHART_VIEW_ID = 19800335;
    public static final int KORA_CAROUSEL_VIEW_ID = 1980050;
    public static final int MEETING_SLOTS_VIEW_ID = 1980089;
    public static final int MEETING_CONFIRMATION_VIEW_ID = 1980032;
    public static final int CALENDER_EVENTS_VIEW_ID = 19800456;
    public static final int FILES_CAROUSAL_VIEW_ID = 19800678;
    public static final int ATTENDEE_SLOT_VIEW_ID = 1980075;
    public static final int QUICK_RPVIEW = 1988881;
    public static final int TIMELINE_VIEW_ID = 1980094;


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


    public static QuickReplyView getQuickReplyView(Context context, ArrayList<QuickReplyTemplate> data, ComposeFooterFragment.ComposeFooterInterface listener, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        if (context != null) {
            QuickReplyView quickReplyView = new QuickReplyView(context);
            quickReplyView.setId(QUICK_RPVIEW);
            quickReplyView.setComposeFooterInterface(listener);
            quickReplyView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            quickReplyView.populateQuickReplyView(data);
            return quickReplyView;
        } else {
            return null;
        }
    }

    public static FormActionView getFormActionView(Context context, ArrayList<FormActionTemplate> data, ComposeFooterFragment.ComposeFooterInterface listener) {
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

    public static BotButtonView getBotButtonView(Context context, ArrayList<BotButtonModel> data, ComposeFooterFragment.ComposeFooterInterface listener) {
        BotButtonView botButtonView = new BotButtonView(context);
        botButtonView.setId(BUTTON_VIEW_ID);
        botButtonView.setComposeFooterInterface(listener);
        botButtonView.populateButtonList(data);
        return botButtonView;
    }

    public static TextMediaLayout getTextMediaLayout(Context context, int linkColors){
        TextMediaLayout bubbleTextMediaLayout = new TextMediaLayout(context,linkColors);
        bubbleTextMediaLayout.setId(TextMediaLayout.TEXT_MEDIA_LAYOUT_ID);
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
    public static BotMainTableView getTableView(Context context){
        BotMainTableView tableView = new BotMainTableView(context);
        tableView.setId(TABLE_VIEW_ID);
        return tableView;
    }
    public static LineChartView getLineChartView(Context context){
        LineChartView lineChartView = new LineChartView(context);
        lineChartView.setId(LINECHART_VIEW_ID);
        return lineChartView;
    }

    public static MeetingSlotsView getMeetingSlotsView(Context context){
        MeetingSlotsView meetingSlotsView = new MeetingSlotsView(context);
        meetingSlotsView.setId(MEETING_SLOTS_VIEW_ID);
        return meetingSlotsView;
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

    public static CalendarEventsTemplateView getCalenderTemplateView(Context context){
        CalendarEventsTemplateView calendarEventsTemplateView = new CalendarEventsTemplateView(context);
        calendarEventsTemplateView.setId(CALENDER_EVENTS_VIEW_ID);
        return calendarEventsTemplateView;
    }
    public static KoraFilesCarousalView getKoraFilesCarouselView(Context context){
        KoraFilesCarousalView koraCarouselView = new KoraFilesCarousalView(context);
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
        KaFontUtils.setCustomTypeface(textView,KaFontUtils.ROBOTO_MEDIUM,context);
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

}
