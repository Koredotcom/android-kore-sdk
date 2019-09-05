package kore.botssdk.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.databinding.WelcomeChatSummaryBinding;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.ActionItem;
import kore.botssdk.models.BaseCalenderTemplateModel;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.ContactViewListModel;
import kore.botssdk.models.Weather;
import kore.botssdk.models.WelcomeChatSummaryModel;
import kore.botssdk.models.WelcomeSummaryModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class WelcomeSummaryView extends ViewGroup implements VerticalListViewActionHelper {

    private ComposeFooterInterface composeFooterInterface;
    private WelcomeChatSummaryBinding welcomeChatSummaryViewBinding;
    private WelcomeSummaryRecyclerAdapter myRecyclerViewAdapter;
    private float dp1;
    private RecyclerView welcomeChatSummaryList;
    private boolean isWeatherDesc = true;

    public WelcomeSummaryView(Context context) {
        super(context);
        init();
    }


    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void populateData(final WelcomeSummaryModel welcomeSummaryModel) {
        if(welcomeSummaryModel != null){
            welcomeChatSummaryViewBinding.getRoot().setVisibility(VISIBLE);
            welcomeChatSummaryViewBinding.setWelcomeSummaryInfo(welcomeSummaryModel);

            ArrayList<WelcomeChatSummaryModel> list = new ArrayList<WelcomeChatSummaryModel>();

            if(welcomeSummaryModel!=null && welcomeSummaryModel.getActionItems()!=null && welcomeSummaryModel.getActionItems().size()>0){
                for(ActionItem actItem : welcomeSummaryModel.getActionItems()){
                    WelcomeChatSummaryModel mdl = new WelcomeChatSummaryModel();
                    mdl.setSummary(actItem.getTitle());
                    mdl.setType(actItem.getType());
                    mdl.setIconId(actItem.getIconId());
                    mdl.setPayload(actItem.getPayload());
                    list.add(mdl);
                }
            }
            if(welcomeSummaryModel != null && welcomeSummaryModel.getWeather()!=null)
            bindWeatherInfo(welcomeSummaryModel.getWeather());

            myRecyclerViewAdapter = new WelcomeSummaryRecyclerAdapter(getContext());
            myRecyclerViewAdapter.setExpanded(false);
            myRecyclerViewAdapter.setVerticalListViewActionHelper(this);
            myRecyclerViewAdapter.setData(list);
            welcomeChatSummaryList.setLayoutManager(new LinearLayoutManager(getContext()));

            welcomeChatSummaryViewBinding.setMyAdapter(myRecyclerViewAdapter);

        }else{
            welcomeChatSummaryViewBinding.getRoot().setVisibility(GONE);
            welcomeChatSummaryViewBinding.setWelcomeSummaryInfo(null);
        }
    }

    private void bindWeatherInfo(Weather weather) {
        try {
            Picasso.get().load(weather.getIcon()).into(welcomeChatSummaryViewBinding.imgWetherIcon);
        } catch (Exception e) {
        }

        welcomeChatSummaryViewBinding.imgWetherIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWeatherDesc) {
                    welcomeChatSummaryViewBinding.tvWetherType.setText(weather.getTemp());
                } else {
                    welcomeChatSummaryViewBinding.tvWetherType.setText(weather.getDesc());

                }
                isWeatherDesc = !isWeatherDesc;
            }
        });

        if(isWeatherDesc) {
            welcomeChatSummaryViewBinding.tvWetherType.setText(weather.getDesc());
        }else{
            welcomeChatSummaryViewBinding.tvWetherType.setText(weather.getTemp());
        }
    }

    private void init() {
        welcomeChatSummaryViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.welcome_chat_summary, this, true);
        //LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.drawable.shadow_layer_background);
        //GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        //outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor()) + BundleConstants.TRANSPERANCY_50_PERCENT);
        //contactInfoViewBinding.getRoot().setBackground(shape);


        //LayerDrawable shape1 = (LayerDrawable) getResources().getDrawable(R.drawable.contact_card_background_style);
        //contactInfoViewBinding.contactTop.setBackground(shape1);




        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        welcomeChatSummaryViewBinding.setViewBase(this);

        welcomeChatSummaryList = ((RecyclerView)findViewById(R.id.weather_chat_LV));
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
        View rootView = welcomeChatSummaryViewBinding.getRoot();
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
            if(!StringUtils.isNullOrEmpty(model.getType())&& model.getType().equals("postback") && !StringUtils.isNullOrEmpty(model.getPayload())){
                composeFooterInterface.onSendClick(model.getPayload(),true);
            }
    }

    public Drawable getTitleIcon(WelcomeChatSummaryModel mdl){
        switch(mdl.getIconId()){
            case "meeting":
                return getResources().getDrawable(R.drawable.widget_calender);
            case "form":
                return getResources().getDrawable(R.drawable.ic_notification_active);
            case "overdue":
                return getResources().getDrawable(R.drawable.ic_overdue);
            case "email":
                return getResources().getDrawable(R.drawable.ic_emails);
            case "upcoming_tasks":
                return getResources().getDrawable(R.drawable.ic_tasks);
            default:
                return getResources().getDrawable(R.drawable.ic_tasks);
        }
    }
}
