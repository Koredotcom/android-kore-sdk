package com.kore.ai.widgetsdk.activities;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.adapters.ChartListWidgetAdapter;
import com.kore.ai.widgetsdk.adapters.DefaultWidgetAdapter;
import com.kore.ai.widgetsdk.adapters.ListWidgetAdapter;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.ShootUtteranceEvent;
import com.kore.ai.widgetsdk.listeners.RecyclerViewDataAccessor;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.BotResponse;
import com.kore.ai.widgetsdk.models.CalEventsTemplateModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.MultiAction;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.WidgetListElementModel;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.BundleConstants;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.KaUtility;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FullViewActivity extends KaAppCompatActivity implements VerticalListViewActionHelper {
    RecyclerView recyclerView;
    String type;
    Gson gson = new Gson();
    private ActionMode actionMode;
    private RecyclerView actionsContainer;
    //    private View footerContainer;
    private boolean loading;
    private boolean hasMore = true;
    private AuthData authData;
    private RecyclerView.Adapter adapter;
    boolean fromWidget;
    private MenuItem openWidget;
    private UserData userData;
    private String  title;
    private CalEventsTemplateModel.Duration _duration;
    Widget.Hook hook;
    private String trigger;
    @Override
    protected void onCreate(Bundle saveState) {
        super.onCreate(saveState);
        setContentView(R.layout.full_view_layout);
        actionsContainer = findViewById(R.id.actions_container);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        actionsContainer.setLayoutManager(layoutManager);
//        footerContainer = findViewById(R.id.composer_view);
        authData = UserDataManager.getAuthData();
        userData = UserDataManager.getUserData();
        setupActionBar();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        type = getIntent().getStringExtra(BundleConstants.TEMPLATE_TYPE);
        trigger = getIntent().getStringExtra(BundleConstants.TRIGGER);
        String dataString = getIntent().getStringExtra(BundleConstants.DATA);
        try {
            title = getIntent().getStringExtra(BundleConstants.TITLE_NAME);
        }catch (Exception e)
        {
            title="";
        }
        getSupportActionBar().setTitle(title);
        if(getIntent().getExtras().get(BundleConstants.DURATION)!=null) {
          _duration =   ((CalEventsTemplateModel.Duration) getIntent().getExtras().get(BundleConstants.DURATION));
        }

        String calendarMultiCation = getIntent().getStringExtra(BundleConstants.DATA_CALENDER_MULTICATION);
        String multiActions = getIntent().getStringExtra(BundleConstants.MULTI_ACTIONS);
        try {
            fromWidget = getIntent().getBooleanExtra(BundleConstants.FROM_WIDGET, false);
        } catch (Exception e) {
            //from other module
            fromWidget = false;
        }
        Type template;
        String url = "";

        switch (type) {
            case BotResponse.TEMPLATE_TYPE_DEFAULT_LIST:
                template = new TypeToken<ArrayList<Widget.Element>>() {
                }.getType();

                DefaultWidgetAdapter defaultWidgetAdapter = new DefaultWidgetAdapter(FullViewActivity.this, BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, trigger);
                defaultWidgetAdapter.setWidgetData(gson.fromJson(dataString, template));
                defaultWidgetAdapter.setFromFullView(true);
                setAdapter(defaultWidgetAdapter);
                break;

            case BotResponse.TEMPLATE_TYPE_CHART_LIST:
                template = new TypeToken<ArrayList<Widget.Element>>() {
                }.getType();

                ChartListWidgetAdapter widgetAdapter = new ChartListWidgetAdapter(FullViewActivity.this, BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, true, true);
                widgetAdapter.setCalData(gson.fromJson(dataString, template));
                recyclerView.setLayoutManager(new GridLayoutManager(FullViewActivity.this, 4));
                setAdapter(widgetAdapter);
                break;

            case BotResponse.TEMPLATE_TYPE_LIST:
                template = new TypeToken<ArrayList<WidgetListElementModel>>(){}.getType();
                ListWidgetAdapter listWidgetAdapter = new ListWidgetAdapter(FullViewActivity.this, BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, trigger);
                listWidgetAdapter.setWidgetData(gson.fromJson(dataString, template));
                listWidgetAdapter.setFromWidget(true);
                listWidgetAdapter.setFromFullView(true);
                listWidgetAdapter.setVerticalListViewActionHelper(this);
                setAdapter(listWidgetAdapter);
                break;



        }

        configurePagination(type, url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_fullview_widgets, menu);
        openWidget = menu.findItem(R.id.open_app);
        if (fromWidget) {
            if (type != null) {
                if (type.equals(BotResponse.TEMPLATE_TYPE_FILES_LOOKUP_WIDGET))
                    openWidget.setIcon(getResources().getDrawable(R.drawable.ic_drive_widget));
                else if (type.equals(BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET) || type.equals(BotResponse.TEMPLATE_TYPE_CAL_EVENTS))
                    openWidget.setIcon(getResources().getDrawable(R.drawable.ic_calendar_widget));
                else
                    openWidget.setVisible(false);
            }
        } else {
            openWidget.setVisible(false);
        }
//        notify = menu.findItem(R.id.notify);
//        notify.setChecked(false);
        return true;
    }

    private static final String ACTION_DRIVE_OPEN = "https://drive.google.com/drive/u/0/my-drive";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.open_app)
        {
            if (type.equals(BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET) || type.equals(BotResponse.TEMPLATE_TYPE_CAL_EVENTS)) {
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, Calendar.getInstance().getTimeInMillis());
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(BotResponse.TEMPLATE_TYPE_FILES_LOOKUP_WIDGET) && UserDataManager.getUserData().getSsoType().equals("google")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ACTION_DRIVE_OPEN));
                startActivity(browserIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void configurePagination(String type, final String url) {
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        recyclerView.setItemAnimator(null);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        /*if (adapter instanceof AnnouncementAdapter) {
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(ContextCompat.getDrawable(this, kore.botssdk.R.drawable.inset_65_divider));
            recyclerView.addItemDecoration(itemDecorator);
        }*/
        ((RecyclerViewDataAccessor) adapter).setVerticalListViewActionHelper(this);
        ((RecyclerViewDataAccessor) adapter).setExpanded(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {

    }

    @Override
    public void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel) {
        LinkedTreeMap<String, String> map = (LinkedTreeMap<String, String>) botCaourselButtonModel.getCustomData().get("redirectUrl");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map.get("mob")));
        startActivity(browserIntent);
    }

    @Override
    public void emailItemClicked(String action, HashMap customData) {
        LinkedTreeMap redirectUrls = (LinkedTreeMap) customData.get("redirectUrl");
        String desktopUrl = (String) redirectUrls.get("dweb");
        String mobUrl = (String) redirectUrls.get("mob");
        KaUtility.launchWebViewActivity(this, URLDecoder.decode(mobUrl != null ? mobUrl : desktopUrl));
    }

    @Override
    public void calendarItemClicked(String action, BaseCalenderTemplateModel model) {
    }

    @Override
    public void meetingNotesNavigation(Context context, String mid, String eid){
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
    public void tasksSelectedOrDeselected(boolean selected) {
    }

    @Override
    public void widgetItemSelected(boolean selected, int size) {
        if (selected) {
            actionMode.setTitle(getResources().getQuantityString(R.plurals.meeting_selected, size, size));
            if (size <= 0) {
                actionMode.finish();
                finishActionMode();
            }
        } else {
            actionMode.finish();
            finishActionMode();
        }
    }

    @Override
    public void navigationToDialAndJoin(String actiontype, String actionLink) {
        if (actiontype.equalsIgnoreCase("dial")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", "dial");
            map.put("dial", actionLink);
            KaUtility.handleCustomActions(map, this);
        } else if (actiontype.equalsIgnoreCase("url")) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("type", "url");
            map2.put("url", actionLink);
            KaUtility.handleCustomActions(map2, this);
        } else if (actiontype.equalsIgnoreCase("meetingUrl")) {
            Intent intent = new Intent(this, GenericWebViewActivity.class);
            intent.putExtra("url", actionLink);
            intent.putExtra("header", getResources().getString(R.string.app_name));
            startActivity(intent);
        }
    }

    @Override
    public void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel) {
    }

    private void animateLayoutVisible(boolean showActions) {
        if (showActions) {
            actionsContainer.setVisibility(View.VISIBLE);
            actionsContainer.animate().translationY(0).setDuration(500).setListener(null);
        } else {
            actionsContainer.setVisibility(View.GONE);
            animateLayoutGone(actionsContainer);
        }

    }

    private void animateLayoutGone(final View view) {
        view.animate().translationY(view.getHeight()).setDuration(500).setListener(null);

    }

    private void toggleView(boolean showActions) {
        animateLayoutVisible(showActions);
    }

    private void finishActionMode() {
        actionMode = null;
//        SelectionUtils.resetSelectionTasks();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //hold current color of status bar
            //set your gray color
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
    }


    private void postMultiAction(ArrayList<String> selectedTasks, MultiAction botButtonModel, boolean appendUtterance){
        HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("action", ((MultiAction) botButtonModel).getTitle());
        hashMap.put("ids", selectedTasks);
        //    hashMap.put("isFullView",true);
        ShootUtteranceEvent event = new ShootUtteranceEvent();
        event.setScrollUpNeeded(appendUtterance);
        event.setMessage((appendUtterance? Constants.SKILL_UTTERANCE:"")+((MultiAction) botButtonModel).getUtterance());
        event.setBody((appendUtterance? Constants.SKILL_UTTERANCE:"")+((MultiAction) botButtonModel).getUtterance());
        event.setPayLoad(gson.toJson(hashMap));
        KoreEventCenter.post(event);
        actionMode.finish();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
