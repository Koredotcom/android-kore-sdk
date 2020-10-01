package com.kore.ai.widgetsdk.views.widgetviews;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.kore.ai.widgetsdk.adapters.ButtonListAdapter;
import com.kore.ai.widgetsdk.events.EntityEditEvent;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.formatters.BarChartDataFormatter;
import com.kore.ai.widgetsdk.fragments.WidgetActionSheetFragment;
import com.kore.ai.widgetsdk.interfaces.PinUnPinnCallBack;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BaseChartModel;
import com.kore.ai.widgetsdk.models.BotBarChartDataModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.BotPieChartElementModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.WidgetsDataModel;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.BundleConstants;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.NetworkUtility;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.ToastUtils;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetConstants;
import com.kore.ai.widgetsdk.utils.WidgetDataLoader;
import com.kore.ai.widgetsdk.views.viewutils.LayoutUtils;
import com.kore.ai.widgetsdk.views.viewutils.MeasureUtils;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

public class PieChartWidgetView extends BaseWidgetView {

    private WidgetsModel mWidget;
    private AuthData authData;
    private UserData userData;
    private PieChart mChart;
    public ProgressBar progress;
    private View rootView;
    private TextView chartHeader;
    private TextView chartTitle;
    private final String PIE_TYPE_REGULAR = "regular";
    private final String PIE_TYPE_DONUT = "donut";
    private float holeRadius;
    private float transparentCircleRadius;
    private RecyclerView recyclerView;
    private Context mContext;
    private String trigger;
    private String skillName;
    public ImageView imgMenu, icon_image_load;
    public TextView tvText;
    public TextView tvUrl;
    public TextView tvButton;
    public LinearLayout tvButtonParent;
    private Button login_button;
    View login_View;
    private TextView pin_view,panel_name_view;
    String panelName;
    private String jwtToken;
    private int labelCount = 0;
//    private BarChart mBarChart;

    public PieChartWidgetView(Context context, String skillName) {
        super(context);
        mContext = context;
        this.skillName = skillName;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.piechart_widgetview, this, true);
        rootView = view.findViewById(R.id.chart_root_view);
        chartHeader = view.findViewById(R.id.chart_header);
        icon_image_load = view.findViewById(R.id.icon_image_load);
        chartTitle = view.findViewById(R.id.chart_title);
        recyclerView = view.findViewById(R.id.buttonsList);
        progress = view.findViewById(R.id.chart_progress);
        mChart = view.findViewById(R.id.mChart);
        mChart.setNoDataTextColor(Color.parseColor("#4741fa"));
        login_button = view.findViewById(R.id.login_button);
        login_View = view.findViewById(R.id.login_View);
        imgMenu = view.findViewById(R.id.icon_image);
        tvButton = view.findViewById(R.id.tv_button);
        tvText = view.findViewById(R.id.tv_text);
        tvUrl = view.findViewById(R.id.tv_url);
        tvButtonParent = view.findViewById(R.id.tv_values_layout);
        pin_view = view.findViewById(R.id.pin_view);
        panel_name_view=view.findViewById(R.id.panel_name_view);

//        //BarChart
//        mBarChart = view.findViewById(R.id.mBarChart);
        getUserData();


        pin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                KaRestAPIHelper.actionPinnAndUnPinn(authData.accessToken, userData.getId(),mWidget,panelData, new PinUnPinnCallBack() {
//                    @Override
//                    public void success() {
//                        mWidget.setPinned(!mWidget.isPinned());
//                        KaUtility.setPinnedViewState(mContext, mWidget.isPinned(), pin_view,panelData.get_id());
//
//                    }
//
//                    @Override
//                    public void failure(Throwable t) {
//                        if(!Utils.isNetworkAvailable(mContext))
//                            ToastUtils.showToast(mContext,mContext.getResources().getString(R.string.no_internet_connection));
//                        else
//                            ToastUtils.showToast(mContext,mContext.getResources().getString(R.string.oops));
//                    }
//                });


            }
        });


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

                        if (model != null && model.getData() != null && model.getData().get(0).getElements() != null && model.getData().get(0).getElements().size() > 0 && !model.getData().get(0).getTemplateType().equals("loginURL")) {
                            login_View.setVisibility(GONE);
                            mChart.setVisibility(VISIBLE);
                            model.getData().get(0).convertElementsToModel();
                            afterDataLoad(model.getData().get(0));
                        }

                        if (model!=null && model.getData().get(0) != null && model.getData().get(0).getTemplateType()!=null&&model.getData().get(0).getTemplateType().equals("loginURL")) {
                            mChart.setVisibility(GONE);
                            login_View.setVisibility(VISIBLE);
                            login_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(mContext instanceof Activity&&model.getData().get(0).getLogin()!=null&&!StringUtils.isNullOrEmptyWithTrim(model.getData().get(0).getLogin().getUrl())) {
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
                        if (!NetworkUtility.isNetworkConnectionAvailable(PieChartWidgetView.this.getContext())) {
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

    public void buttonAction(String utt, boolean appendUtterance) {
        String utterance = null;

        utterance = utt;

        if (utterance == null) return;
        if (utterance != null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))) {
            if (utterance.startsWith("tel:")) {
                KaUtility.launchDialer(getContext(), utterance);
            } else if (utterance.startsWith("mailto:")) {
                KaUtility.showEmailIntent((Activity) getContext(), utterance.split(":")[1]);
            }
            return;
        }
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if (appendUtterance && trigger != null)
            msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);

    }

    public void buttonAction(Widget.Button button, boolean appendUtterance) {
        String utterance = null;
        if (button != null) {
            utterance = button.getUtterance();
        }
        if (utterance == null) return;
        if (utterance != null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))) {
            if (utterance.startsWith("tel:")) {
                KaUtility.launchDialer(getContext(), utterance);
            } else if (utterance.startsWith("mailto:")) {
                KaUtility.showEmailIntent((Activity) getContext(), utterance.split(":")[1]);
            }
            return;
        }
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if (appendUtterance && trigger != null)
            msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);

    }

    private void afterDataLoad(final BaseChartModel model) {
        mChart.setVisibility(View.VISIBLE);

        if (model.getHeaderOptions() != null && model.getHeaderOptions().getType() != null) {
            switch (model.getHeaderOptions().getType()) {
                case "button":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    tvButtonParent.setVisibility(VISIBLE);
                    String btnTitle = "";
                    if (model.getHeaderOptions().getButton() != null && model.getHeaderOptions().getButton().getTitle() != null)
                        btnTitle = model.getHeaderOptions().getButton().getTitle();
                    else
                        btnTitle = model.getHeaderOptions().getText();
                    if (!StringUtils.isNullOrEmpty(btnTitle))
                        tvButton.setText(btnTitle);
                    else
                        tvButtonParent.setVisibility(GONE);


                    tvButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                    (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))) {
                                buttonAction(model.getHeaderOptions().getButton(), true);
                            } else {
                                buttonAction(model.getHeaderOptions().getButton(), false);
                            }
                        }
                    });

                    break;
                case "menu":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(VISIBLE);
                    tvText.setVisibility(GONE);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);


                    imgMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.getHeaderOptions() != null && model.getHeaderOptions().getMenu() != null && model.getHeaderOptions().getMenu().size() > 0) {

                                WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                                bottomSheetDialog.setisFromFullView(false);
                                bottomSheetDialog.setSkillName(skillName, trigger);
                                bottomSheetDialog.setData(model, true);
                                bottomSheetDialog.setVerticalListViewActionHelper(null);
                                bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");

                            }
                        }
                    });
                    break;
                case "text":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(VISIBLE);
                    tvText.setText(model.getHeaderOptions().getText());
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    break;
                case "url":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    SpannableString content = new SpannableString(model.getHeaderOptions().getUrl().getTitle() != null ? model.getHeaderOptions().getUrl().getTitle() : model.getHeaderOptions().getUrl().getLink());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    tvUrl.setText(content);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(VISIBLE);

                    tvUrl.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
                case "image":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    icon_image_load.setVisibility(VISIBLE);


                    if (model.getHeaderOptions().getImage() != null && model.getHeaderOptions().getImage().getImage_src() != null) {
                        Picasso.get().load(model.getHeaderOptions().getImage().getImage_src()).into(icon_image_load);
                        icon_image_load.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (model.getHeaderOptions().getImage() != null && model.getHeaderOptions().getImage().getType() != null && model.getHeaderOptions().getImage().getType().equals("url")) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getHeaderOptions().getImage().getUrl()));
                                    try {
                                        mContext.startActivity(browserIntent);
                                    } catch (ActivityNotFoundException ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (model.getHeaderOptions().getImage() != null && model.getHeaderOptions().getImage().getType() != null && model.getHeaderOptions().getImage().getType().equals("postback")) {
                                    if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                            (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))) {
                                        buttonAction(model.getHeaderOptions().getImage().getUtterance() != null ? model.getHeaderOptions().getImage().getUtterance() : model.getHeaderOptions().getImage().getPayload() != null ? model.getHeaderOptions().getImage().getPayload() : "", true);

                                    } else {
                                        buttonAction(model.getHeaderOptions().getImage().getUtterance() != null ? model.getHeaderOptions().getImage().getUtterance() : model.getHeaderOptions().getImage().getPayload() != null ? model.getHeaderOptions().getImage().getPayload() : "", false);

                                    }
                                }

                                //  buttonAction(model.getHeaderOptions().getImage().getUtterance()!=null?model.getHeaderOptions().getImage().getUtterance():model.getHeaderOptions().getImage().getPayload()!=null?model.getHeaderOptions().getImage().getPayload():"",true);
                            }
                        });
                    }
                    break;
            }

        }

        if(WidgetConstants.PIE_CHART.equals(model.getTemplateType()))
        {
            ArrayList<BotPieChartElementModel> elementModels = model.getPieChartElements();
            if (elementModels != null && !elementModels.isEmpty()) {
                ArrayList<String> xVal = new ArrayList<>(elementModels.size());
                ArrayList<PieEntry> yVal = new ArrayList<>(elementModels.size());
                for (int i = 0; i < elementModels.size(); i++) {
                    xVal.add(elementModels.get(i).getTitle());
                    yVal.add(new PieEntry((float) elementModels.get(i).getValue(), elementModels.get(i).getTitle() + ":" + elementModels.get(i).getDisplayValue()));
                }
                chartTitle.setText(model.getTitle());
                populatePieChart("", model.getPie_type(), xVal, yVal);
            }
        }
        else if(WidgetConstants.BAR_CHART.equals(model.getTemplateType()))
        {
            ArrayList<BotBarChartDataModel> barChartElements = model.getBarChartElements();

            float barWidth = 0.2f;
            float groupSpace = 0.08f;
            float barSpace = 0.03f; // x4 DataSet
            int startYear = 1;
            int groupCount = 4;
            labelCount = 0;
            ArrayList<BarEntry> yVals1[];// = new ArrayList<BarEntry>();
            BarDataSet dataSet[];
            List<IBarDataSet> barDataSets = new ArrayList<>();

            if (barChartElements != null && barChartElements.size() > 0) {
                int size = barChartElements.size();

                yVals1 = new ArrayList[size];
                for (int index = 0; index < size; index++) {
                    BotBarChartDataModel barmodel = barChartElements.get(index);
                    yVals1[index] = new ArrayList<>();
                    for (int inner = 0; inner < barmodel.getValues().size(); inner++) {
                        yVals1[index].add(new BarEntry(inner + 1, barmodel.getValues().get(inner), barmodel.getDisplayValues().get(inner)));
                    }
                }
                dataSet = new BarDataSet[size];

                for (int k = 0; k < size; k++) {
                    dataSet[k] = new BarDataSet(yVals1[k],barChartElements.get(k).getTitle());
                    dataSet[k].setColor(ColorTemplate.MATERIAL_COLORS[k % 4]);
                    barDataSets.add(dataSet[k]);
                }

                BarData data = new BarData(barDataSets);
                data.setValueFormatter(new BarChartDataFormatter());
                XAxis xAxis = mChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextSize(8f);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(0.5f); // only intervals of 1 day
                xAxis.setLabelCount(4);
                ValueFormatter xAxisFormatter = new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float v, AxisBase axisBase) {
                        return "";
                    }
                };

                xAxis.setValueFormatter(xAxisFormatter);

                Legend l = mChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                l.setForm(Legend.LegendForm.SQUARE);
                l.setFormSize(9f);
                l.setTextSize(11f);
                l.setXEntrySpace(4f);

//                mBarChart.setData(data);
//
//                mBarChart.getBarData().setBarWidth(barWidth);

                // restrict the x-axis range
                mChart.getXAxis().setAxisMinimum(startYear);


                // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
//                mChart.getXAxis().setAxisMaximum(startYear + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
//                mBarChart.groupBars(startYear, groupSpace, barSpace);
                mChart.invalidate();
            }
        }
//        else if(WidgetConstants.LINE_CHART.equals(templateType)){
//            Type lineType = new TypeToken<ArrayList<BotLineChartDataModel>>() {
//            }.getType();
//            lineChartElements = gson.fromJson(elementsAsString, lineType);
//        }



        if (model.getButtons() != null && model.getButtons().size() > 0) {
            /*FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);*/

            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

            ButtonListAdapter buttonRecyclerAdapter = new ButtonListAdapter(mContext, model.getButtons(), trigger);
            buttonRecyclerAdapter.setSkillName(skillName);
            buttonRecyclerAdapter.setIsFromFullView(false);
            recyclerView.setAdapter(buttonRecyclerAdapter);
            buttonRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        KoreEventCenter.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        KoreEventCenter.unregister(this);
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

    public void populatePieChart(String desc, String pieType, ArrayList<String> xVals, ArrayList<PieEntry> yVals) {
        if (pieType != null && pieType.equals(PIE_TYPE_DONUT)) {
            holeRadius = 50f;
            transparentCircleRadius = 61f;
        } else {
            holeRadius = 0f;
            transparentCircleRadius = 0f;
        }
        mChart.setUsePercentValues(true);
        mChart.setDrawEntryLabels(false);
        mChart.setDrawHoleEnabled(true);
        Description sdesc = new Description();
        sdesc.setText(desc);
        mChart.setDescription(sdesc);
        mChart.setHoleRadius(holeRadius);
        mChart.setTransparentCircleRadius(transparentCircleRadius);


        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        mChart.animateY(1400, Easing.EaseInOutQuad);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                if (e == null)
                    return;
            }

            @Override
            public void onNothingSelected() {
            }
        });
        addData(xVals, yVals);

        Legend l = mChart.getLegend();
       /* l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        Legend l = chart.getLegend();*/

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(16);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

    }

    private void addData(ArrayList<String> xVals, ArrayList<PieEntry> yVals) {
        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        String[] colorsArray = this.getResources().getStringArray(R.array.color_set);
        for (String color : colorsArray) {
            colors.add(Color.parseColor(color));
        }

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
//        data.setDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();

    }
}
