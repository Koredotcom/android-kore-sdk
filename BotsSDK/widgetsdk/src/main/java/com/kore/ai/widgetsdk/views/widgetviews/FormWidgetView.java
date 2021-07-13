package com.kore.ai.widgetsdk.views.widgetviews;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BaseChartModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.WidgetsDataModel;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.BundleConstants;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.NetworkUtility;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetConstants;
import com.kore.ai.widgetsdk.utils.WidgetDataLoader;
import com.kore.ai.widgetsdk.views.viewutils.LayoutUtils;
import com.kore.ai.widgetsdk.views.viewutils.MeasureUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

public class FormWidgetView extends LinearLayout implements VerticalListViewActionHelper
{
    private Context mContext;
    private String skillName;
    private View rootView;
    private TextView chartHeader;
    private TextView chartTitle;
    public ImageView imgMenu, icon_image_load;
    public ProgressBar progress;
    private Button login_button;
    View login_View;
    public TextView tvText;
    public TextView tvUrl;
    public TextView tvButton;
    public LinearLayout tvButtonParent;
    private TextView pin_view,panel_name_view,tvFillForm;
    private AuthData authData;
    private UserData userData;
    private WidgetsModel mWidget;
    String panelName;
    private String jwtToken;
    private float dp1;

    public FormWidgetView(Context context, String skillName) {
        super(context);
        mContext = context;
        this.skillName = skillName;
        init();
    }

    private void init()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_widgetview, this, true);
        rootView = view.findViewById(R.id.chart_root_view);
        chartHeader = view.findViewById(R.id.chart_header);
        icon_image_load = view.findViewById(R.id.icon_image_load);
        chartTitle = view.findViewById(R.id.chart_title);
        progress = view.findViewById(R.id.chart_progress);
        login_button = view.findViewById(R.id.login_button);
        login_View = view.findViewById(R.id.login_View);
        imgMenu = view.findViewById(R.id.icon_image);
        tvButton = view.findViewById(R.id.tv_button);
        tvText = view.findViewById(R.id.tv_text);
        tvUrl = view.findViewById(R.id.tv_url);
        tvButtonParent = view.findViewById(R.id.tv_values_layout);
        pin_view = view.findViewById(R.id.pin_view);
        panel_name_view=view.findViewById(R.id.panel_name_view);
        dp1 = (int) Utility.convertDpToPixel(getContext(), 1);
        tvFillForm = (TextView) view.findViewById(R.id.tvFillForm);

        getUserData();
    }

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void getUserData() {
        authData = UserDataManager.getAuthData();
        userData = UserDataManager.getUserData();
    }

    PanelLevelData panelData;
    public void setWidget(String panelName, WidgetsModel mWidget, PanelLevelData panelData, String jwtToken) {
        this.mWidget = mWidget;
        this.panelData=panelData;
//        this.trigger=mWidget.getTrigger();
        this.panelName=panelName;
        chartHeader.setText(mWidget.getTitle());
//        pin_view.setText(mWidget.isPinned() ? mContext.getResources().getString(R.string.icon_31) :mContext.getResources().getString(R.string.icon_32));
        panel_name_view.setText(KaUtility.getPanelFormatedName(mWidget.getName()));
        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(mWidget.getName(),panelName));
        this.jwtToken = jwtToken;

        loadData();
    }

    private void loadData() {

        if (mWidget.getCallbackURL() == null) {
            return;
        }
        progress.setVisibility(View.VISIBLE);
        Map<String,Object> result = getMapObject(mWidget.getParam());
        WidgetDataLoader.loadWidgetChartData(mWidget.getCallbackURL(), Utils.ah(jwtToken), result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WidgetsDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final WidgetsDataModel model) {

                        if (model != null && model.getData() != null && model.getData().get(0).getTemplateType() != null && model.getData().get(0).getTemplateType().equals(WidgetConstants.FORM)) {
                            login_View.setVisibility(GONE);
                            tvFillForm.setVisibility(VISIBLE);
                            afterDataLoad(model.getData().get(0));
                        }

                        if (model!=null && model.getData().get(0) != null && model.getData().get(0).getTemplateType()!=null&&model.getData().get(0).getTemplateType().equals("loginURL")) {
                            tvFillForm.setVisibility(GONE);
                            login_View.setVisibility(VISIBLE);
                            login_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(mContext instanceof Activity &&model.getData().get(0).getLogin()!=null&&!StringUtils.isNullOrEmptyWithTrim(model.getData().get(0).getLogin().getUrl())) {
                                        Intent intent = new Intent(mContext, GenericWebViewActivity.class);
                                        intent.putExtra("url", model.getData().get(0).getLogin().getUrl());
                                        intent.putExtra("header", mContext.getResources().getString(R.string.app_name));
                                        ((Activity)mContext).startActivityForResult(intent, BundleConstants.REQ_CODE_REFRESH_CURRENT_PANEL);
                                    }else{
                                        Toast.makeText(mContext,"Instance not activity",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.setVisibility(View.GONE);


                        String msg;
                        Drawable drawable = null;
                        if (!NetworkUtility.isNetworkConnectionAvailable(FormWidgetView.this.getContext())) {
                            //No Internet Connect
                            msg = getResources().getString(R.string.no_internet_connection);
                            drawable = getResources().getDrawable(R.drawable.no_internet);
                        } else {
                            //Oops some thing went wrong
                            msg = getResources().getString(R.string.oops);
                            drawable = getResources().getDrawable(R.drawable.oops_icon);
                        }
                    }

                    @Override
                    public void onComplete() {
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void afterDataLoad(final BaseChartModel model)
    {
        if(model != null)
        {
//            mWebView.loadUrl(model.getFormLink());
            tvFillForm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContext instanceof Activity &&model.getFormLink()!=null&&!StringUtils.isNullOrEmptyWithTrim(model.getFormLink())) {
                        Intent intent = new Intent(mContext, GenericWebViewActivity.class);
                        intent.putExtra("url", model.getFormLink());
                        intent.putExtra("header",panelName);
                        ((Activity)mContext).startActivityForResult(intent, BundleConstants.REQ_CODE_REFRESH_CURRENT_PANEL);
                    }else{
                        Toast.makeText(mContext,"Instance not activity",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
        int parentWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = View.MeasureSpec.makeMeasureSpec((int) (parentWidth - (2 * dp1)), View.MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = View.MeasureSpec.makeMeasureSpec(totalHeight, View.MeasureSpec.EXACTLY);
        int parentWidthSpec = View.MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
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
}
