package com.kore.ai.widgetsdk.views.widgetviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.interfaces.PinUnPinnCallBack;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.ToastUtils;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.widgets.GenericWidgetViewPagerAdapter;

import java.util.HashMap;

public class ArticlesWidgetView extends LinearLayout implements VerticalListViewActionHelper {
    Context context;
    private TabLayout sliding_tabs;
    private TextView view_more, widget_header;
    private ImageView widget_menu;
    private ProgressBar files_progress;
    private ViewPager viewpager;
    WidgetsModel widget;
    String title;
    int position;
    private TextView pin_view,panel_name_view;
    boolean isPaaginationRequired;
    PanelLevelData panelData;
    WidgetViewMoreEnum widgetViewMoreEnum;
    private UserData userData;
    private AuthData authData;
    public ArticlesWidgetView(Context context, WidgetViewMoreEnum widgetViewMoreEnum) {
        super(context);
        this.context = context;
        this.widgetViewMoreEnum=widgetViewMoreEnum;
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.widger_pager_layout, this, true);
        widget_menu = view.findViewById(R.id.widget_menu);
        panel_name_view=view.findViewById(R.id.panel_name_view);
        pin_view = view.findViewById(R.id.pin_view);
        view_more = view.findViewById(R.id.view_more);
        widget_header = view.findViewById(R.id.widget_header);
        sliding_tabs = view.findViewById(R.id.sliding_tabs);
        files_progress = view.findViewById(R.id.files_progress);
        viewpager = view.findViewById(R.id.pager);

    }

    String panelName;
    public void setWidget(String panelName, WidgetsModel widget, int position, boolean isPaaginationRequired, PanelLevelData panelData) {
        this.widget = widget;
        this.panelData=panelData;
        this.panelName=panelName;
        this.title = widget.getTitle();
        this.position = position;
        this.isPaaginationRequired=isPaaginationRequired;
        bindData();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }


    private void bindData() {
//        pin_view.setText(widget.isPinned() ? getResources().getString(R.string.icon_31) : getResources().getString(R.string.icon_32));
        panel_name_view.setText(KaUtility.getPanelFormatedName(widget.getName()));
        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(widget.getName(),panelName));


        widget_header.setText(widget.getTitle());
        GenericWidgetViewPagerAdapter adapter = new GenericWidgetViewPagerAdapter(((Activity) context), this,
                widget, title,isPaaginationRequired,widgetViewMoreEnum);


        viewpager.setAdapter(adapter);
        viewpager.setId(position * 2);


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        sliding_tabs.setupWithViewPager(viewpager);


        try {
            TabLayout.Tab selectedTab = sliding_tabs.getTabAt(0);
            View tabView = selectedTab.view;
            GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.tab_background_selected_widgets);
//            gradientDrawable.setColor(Color.parseColor(widget.getTheme()));
            tabView.setBackground(gradientDrawable);

            TabLayout.Tab selectedTab2 = sliding_tabs.getTabAt(1);
            View tabView2 = selectedTab2.view;
            tabView2.setBackground(context.getResources().getDrawable(R.drawable.tab_background_unselected));

            sliding_tabs.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));


            sliding_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    View tabView = tab.view;
                    GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.tab_background_selected_widgets);
//                    gradientDrawable.setColor(Color.parseColor(widget.getTheme()));
                    tabView.setBackground(gradientDrawable);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View tabView = tab.view;
                    tabView.setBackground(context.getResources().getDrawable(R.drawable.tab_background_unselected));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            sliding_tabs.setupWithViewPager(viewpager);
        } catch (Exception e) {

        }
        widget_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(context, KaAddInformationActivity.class);
//                ((Activity) context).startActivityForResult(intent, BundleConstants.CREATE_KNOWLEDGE_FROM_RIGHT_WIDGET_ID);
            }
        });

        pin_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                KaRestAPIHelper.actionPinnAndUnPinn(authData.accessToken, userData.getId(),widget,panelData, new PinUnPinnCallBack() {
//                    @Override
//                    public void success() {
//                        widget.setPinned(!widget.isPinned());
//                        KaUtility.setPinnedViewState(context, widget.isPinned(), pin_view,panelData.get_id());
//
//                    }
//
//                    @Override
//                    public void failure(Throwable t) {
//                        if(!Utils.isNetworkAvailable(context))
//                            ToastUtils.showToast(context,context.getResources().getString(R.string.no_internet_connection));
//                        else
//                            ToastUtils.showToast(context,context.getResources().getString(R.string.oops));
//                    }
//                });


            }
        });
    }


    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {
//        KaUtility.launchViewDetailsActivity((Activity) context, extras, false);
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
