package kore.botssdk.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.adapter.AnnouncementAdapter;
import kore.botssdk.adapter.CalendarEventsAdapter;
import kore.botssdk.adapter.KnowledgeRecyclerAdapter;
import kore.botssdk.adapter.KoraEmailRecyclerAdapter;
import kore.botssdk.adapter.KoraFilesRecyclerAdapter;
import kore.botssdk.adapter.TasksListAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.ProfileColorUpdateEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.AnnoucementResModel;
import kore.botssdk.models.BaseCalenderTemplateModel;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.CalEventsTemplateModel.Duration;
import kore.botssdk.models.ContactViewListModel;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.TaskTemplateResponse;
import kore.botssdk.models.WelcomeChatSummaryModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Ramachandra Pradeep on 09-Aug-18.
 */

public class VerticalListView extends ViewGroup implements VerticalListViewActionHelper {
    private RecyclerView recyclerView;
    private View rootLayout;
    private int dp1;
    private TextView viewMore;
    private Duration _cursor;

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }


    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private Gson gson = new Gson();

    public VerticalListView(Context mContext) {

        super(mContext);
        init();
    }

    public VerticalListView(Context mContext, AttributeSet attributes) {
        super(mContext, attributes);
        init();
    }

    public VerticalListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.vertical_list_view, this, true);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(null);
        viewMore = view.findViewById(R.id.view_more);
        rootLayout = view.findViewById(R.id.rootLayoutvertical);
        LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.drawable.shadow_layer_background);
        GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+ BundleConstants.TRANSPERANCY_50_PERCENT);
        rootLayout.setBackground(shape);
        viewMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter == null) return;
                String type = getTemplateType(adapter);
                handleViewMore(type, adapter);

            }
        });

        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;

    }

    public void onEvent(ProfileColorUpdateEvent event){
        LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.drawable.shadow_layer_background);
        GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+BundleConstants.TRANSPERANCY_50_PERCENT);
        rootLayout.setBackground(shape);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KoreEventCenter.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KoreEventCenter.unregister(this);
    }

    private void handleViewMore(String type, RecyclerView.Adapter adapter) {
        if (type.equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_TASK_VIEW) || type.equalsIgnoreCase(BotResponse.TEMPLATE_TASK_FULLVIEW)) {
            if(composeFooterInterface != null)
                composeFooterInterface.openFullView(BotResponse.TEMPLATE_TYPE_TASK_VIEW, gson.toJson(((TasksListAdapter) recyclerView.getAdapter()).getTaskTemplateResponse()), null, 0);
        } else {
            Duration _duration = null;
            if(adapter instanceof CalendarEventsAdapter){
                _duration = ((CalendarEventsAdapter) adapter).getCursorDuration();
            }
            if(composeFooterInterface != null)
                composeFooterInterface.openFullView(getTemplateType(adapter), gson.toJson(((RecyclerViewDataAccessor) recyclerView.getAdapter()).getData()),_duration, 0);
        }
    }

    private String getTemplateType(RecyclerView.Adapter adapter) {
        if (adapter instanceof KoraFilesRecyclerAdapter) {
            return BotResponse.TEMPLATE_TYPE_FILES_LOOKUP;
        } else if (adapter instanceof KoraEmailRecyclerAdapter) {
            return BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL;
        } else if (adapter instanceof KnowledgeRecyclerAdapter) {
            return BotResponse.TEMPLATE_TYPE_KORA_CAROUSAL;
        } else if (adapter instanceof CalendarEventsAdapter) {
            return ((CalendarEventsAdapter) adapter).getType();
        } else if (adapter instanceof AnnouncementAdapter) {
            return BotResponse.TEMPLATE_TYPE_KORA_ANNOUNCEMENT_CAROUSAL;
        } else {
            return BotResponse.TEMPLATE_TYPE_TASK_VIEW;
        }
    }

    private ArrayList<BotButtonModel> getActions(RecyclerView.Adapter adapter) {
        if (adapter instanceof TasksListAdapter) {
            return ((TasksListAdapter) adapter).getTaskTemplateResponse().getButtons();
        }
        return new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) parentWidth - 28 * dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootLayout, childWidthSpec, wrapSpec);

        totalHeight += rootLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
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

    public void prepareDataToTasks(TaskTemplateResponse data, String templateType, boolean isEnabled) {
        TasksListAdapter tasksListAdapter = new TasksListAdapter(getContext(), data, isEnabled);
        tasksListAdapter.setHasStableIds(true);
        setAdapter(tasksListAdapter);
        if (SelectionUtils.getSelectedTasks().size() > 0) {
            tasksListAdapter.setSelectedTasks(SelectionUtils.getSelectedTasks());
            tasksListAdapter.notifyDataSetChanged();
        }
        prepareDataSetAndPopulate(data.getTaskData(), templateType, isEnabled);


    }
/*    public void prepareDataSetAndPopulate(ArrayList data, String templateType,boolean isEnabled) {
        CalendarEventsAdapter calendarEventsAdapter = new CalendarEventsAdapter(getContext(),templateType,isEnabled);
        calendarEventsAdapter.setData(data);
        calendarEventsAdapter.setComposeFooterInterface(composeFooterInterface);
        setAdapter(calendarEventsAdapter);
      //  prepareDataSetAndPopulate(data,templateType);
    }*/

    public void setCursorDuration(Duration cursor){
        _cursor = cursor;
    }
    public void prepareDataSetAndPopulate(ArrayList data, String templateType, boolean isEnabled) {
        if (data == null || data.size() == 0) {
            rootLayout.setVisibility(GONE);
        } else {
            setAdapterByData(data, templateType, isEnabled, _cursor);
            viewMore.setVisibility(data.size() > 3 && isEnabled ? VISIBLE : GONE);
            if (data.size() > 3) {
                viewMore.setText(getContext().getResources().getString(R.string.view_more));
            }
            rootLayout.setVisibility(VISIBLE);
        }

    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setItemAnimator(null);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        /*if (adapter instanceof AnnouncementAdapter) {
            DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.inset_65_divider));
            recyclerView.addItemDecoration(itemDecorator);
        }*/
        ((RecyclerViewDataAccessor) adapter).setVerticalListViewActionHelper(this);
        adapter.notifyDataSetChanged();
    }

    public void setAdapterByData(ArrayList data, String type, boolean isEnabled, Duration _cursor) {
        switch (type) {
            case BotResponse.TEMPLATE_TYPE_FILES_LOOKUP:
                setAdapter(new KoraFilesRecyclerAdapter(data, getContext()));
                break;
            case BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL:
                setAdapter(new KoraEmailRecyclerAdapter(data, getContext()));
                break;
            case BotResponse.TEMPLATE_TYPE_KORA_CAROUSAL:
                setAdapter(new KnowledgeRecyclerAdapter(data, getContext()));
                break;
            case BotResponse.TEMPLATE_TYPE_CAL_EVENTS:
            case BotResponse.TEMPLATE_TYPE_CANCEL_EVENT:
                CalendarEventsAdapter calendarEventsAdapter = new CalendarEventsAdapter(getContext(), type, isEnabled);
                calendarEventsAdapter.setVerticalListViewActionHelper(this);
                calendarEventsAdapter.setCursorDuration(_cursor);
                calendarEventsAdapter.setData(data);
                calendarEventsAdapter.setComposeFooterInterface(composeFooterInterface);
                setAdapter(calendarEventsAdapter);
                break;

            case BotResponse.TEMPLATE_TYPE_KORA_ANNOUNCEMENT_CAROUSAL:
                AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(getContext(), (ArrayList<AnnoucementResModel>) data);
                announcementAdapter.setFullView(false);
                setAdapter(announcementAdapter);
                break;

            default:
        }
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {
        if(composeFooterInterface != null)
            composeFooterInterface.launchActivityWithBundle(BotResponse.TEMPLATE_TYPE_KORA_CAROUSAL, extras);
    }

    @Override
    public void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel) {
        LinkedTreeMap<String, String> map = (LinkedTreeMap<String, String>) botCaourselButtonModel.getCustomData().get("redirectUrl");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map.get("mob")));
        getContext().startActivity(browserIntent);
    }

    @Override
    public void emailItemClicked(String action, HashMap customData) {
        invokeGenericWebViewInterface.handleUserActions(action, customData);
    }

    @Override
    public void calendarItemClicked(String type, BaseCalenderTemplateModel model) {
        Bundle bundle=new Bundle();
        String data = new Gson().toJson(model);
        bundle.putString("data",data);
        if(model instanceof CalEventsTemplateModel) {
            bundle.putString(BundleConstants.REQ_TEXT_TO_DISPLAY, ((CalEventsTemplateModel)model).getReqTextToDisplayForDetails());
        }
        if(composeFooterInterface != null)
            composeFooterInterface.launchActivityWithBundle(type,bundle);
    }

    @Override
    public void tasksSelectedOrDeselected(boolean selecetd) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslationZ(500 * dp1);
        }
        bringToFront();
        if(composeFooterInterface != null)
            composeFooterInterface.updateActionbar(selecetd, getTemplateType(recyclerView.getAdapter()), getActions(recyclerView.getAdapter()));
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
        if(composeFooterInterface != null)
            composeFooterInterface.lauchMeetingNotesAction(context,mId,eId);

    }

    @Override
    public void meetingWidgetViewMoreVisibility(boolean visible) {
        if(viewMore!=null)
            viewMore.setVisibility(visible? View.VISIBLE: View.GONE);
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
