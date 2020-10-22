package kore.botssdk.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BaseCalenderTemplateModel;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ContactViewListModel;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.KoraUniversalSearchModel;
import kore.botssdk.models.WelcomeChatSummaryModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.Constants;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class DisclaimerTemplateView extends ViewGroup implements VerticalListViewActionHelper {
    View disclaimer_layout,disclaimer_inner;
    private float dp1;
    public DisclaimerTemplateView(Context context) {
        super(context);
        initView();
    }
    public DisclaimerTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DisclaimerTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    TextView tv_shortdescription,tv_link;
    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.disclaimer_template_view, this, true);
        disclaimer_layout = view.findViewById(R.id.disclaimer_layout);
        disclaimer_inner=view.findViewById(R.id.disclaimer_inner);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        tv_shortdescription=view.findViewById(R.id.tv_shortdescription);
        tv_link=view.findViewById(R.id.tv_link);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        View rootView = disclaimer_layout;
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.EXACTLY);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {

    }

    @Override
    public void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel) {

    }

    @Override
    public void emailItemClicked(String action, HashMap customData) {

    }

    @Override
    public void calendarItemClicked(String action, BaseCalenderTemplateModel model) {

    }

    @Override
    public void tasksSelectedOrDeselected(boolean selecetd) {

    }

    @Override
    public void widgetItemSelected(boolean isSelected, int count) {

    }

    @Override
    public void navigationToDialAndJoin(String actiontype, String actionLink) {

    }

    @Override
    public void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel) {

    }

    @Override
    public void meetingNotesNavigation(Context context, String mId, String eId) {

    }

    @Override
    public void meetingWidgetViewMoreVisibility(boolean visible) {

    }

    @Override
    public void calendarContactItemClick(ContactViewListModel model) {

    }

    @Override
    public void welcomeSummaryItemClick(WelcomeChatSummaryModel model) {

    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }
    ComposeFooterInterface composeFooterInterface;
    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }
    String shortDesc, contentPrev, discription;
    public void populateData(String shortDesc,String contentPrev,String discription) {
        this.shortDesc=shortDesc;
        this.contentPrev=contentPrev;
        this.discription=discription;

        if(shortDesc!=null)
        {
             int transparency;
            disclaimer_layout.setVisibility(VISIBLE);
            transparency = 0x26000000;
            GradientDrawable   rightDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.rounded_rectangle_bubble);
            rightDrawable.setColor(Color.parseColor("#A7A9BE")+transparency);
            rightDrawable.setStroke((int) (1*dp1), Color.parseColor("#A7A9BE")+transparency);
            disclaimer_inner.setBackground(rightDrawable);
            tv_shortdescription.setText(shortDesc);
            tv_link.setText(contentPrev);

            tv_link.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("msg",discription);
                    composeFooterInterface.launchActivityWithBundle(BotResponse.DISCLAIMER_DAILOG_TEMPLATE,bundle);
                }
            });


        }else {
            disclaimer_layout.setVisibility(GONE);
        }
    }
}
