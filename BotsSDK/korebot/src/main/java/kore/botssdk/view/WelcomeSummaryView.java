package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import kore.botssdk.databinding.WelcomeChatSummaryBinding;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.ActionItem;
import kore.botssdk.models.BaseCalenderTemplateModel;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ContactViewListModel;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.QuickRepliesPayloadModel;
import kore.botssdk.models.Weather;
import kore.botssdk.models.WelcomeChatSummaryModel;
import kore.botssdk.models.WelcomeSummaryModel;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.DialogCaller;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

@SuppressLint("UnknownNullness")
public class WelcomeSummaryView extends ViewGroup implements VerticalListViewActionHelper {

    private ComposeFooterInterface composeFooterInterface;
    private WelcomeChatSummaryBinding welcomeChatSummaryViewBinding;
    private WelcomeSummaryRecyclerAdapter myRecyclerViewAdapter;
    private float dp1;
    private RecyclerView welcomeChatSummaryList;
    private boolean isWeatherDesc = true;
    private final String skillName;
    private final Context context;

    public WelcomeSummaryView(Context context, String skillName) {
        super(context);
        this.context = context;
        this.skillName = skillName;
        init();
    }


    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void populateData(final WelcomeSummaryModel welcomeSummaryModel, boolean isEnabled) {
        if(welcomeSummaryModel != null){
            welcomeChatSummaryViewBinding.getRoot().setVisibility(VISIBLE);
            welcomeChatSummaryViewBinding.getRoot().setEnabled(isEnabled);
            welcomeChatSummaryViewBinding.getRoot().setClickable(isEnabled);
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
            if(welcomeSummaryModel.getWeather() != null)
                bindWeatherInfo(welcomeSummaryModel.getWeather());


            myRecyclerViewAdapter.setData(list);
            myRecyclerViewAdapter.setEnabled(isEnabled);


        }else{
            welcomeChatSummaryViewBinding.getRoot().setVisibility(GONE);
            welcomeChatSummaryViewBinding.setWelcomeSummaryInfo(null);
            welcomeChatSummaryViewBinding.getRoot().setEnabled(isEnabled);
            welcomeChatSummaryViewBinding.getRoot().setClickable(isEnabled);
        }
    }

    private void bindWeatherInfo(final Weather weather) {
        try {
            Picasso.get().load(weather.getIcon()).into(welcomeChatSummaryViewBinding.imgWetherIcon);
        } catch (Exception e) {
        }

        welcomeChatSummaryViewBinding.imgWetherIcon.setOnClickListener(new OnClickListener() {
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

        dp1 = (int) DimensionUtil.dp1;
        welcomeChatSummaryViewBinding.setViewBase(this);

        welcomeChatSummaryList = findViewById(R.id.weather_chat_LV);
        myRecyclerViewAdapter = new WelcomeSummaryRecyclerAdapter(getContext());
        myRecyclerViewAdapter.setExpanded(false);
        myRecyclerViewAdapter.setVerticalListViewActionHelper(this);
        welcomeChatSummaryList.setLayoutManager(new LinearLayoutManager(getContext()));
        welcomeChatSummaryViewBinding.setMyAdapter(myRecyclerViewAdapter);
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
    public void welcomeSummaryItemClick(final WelcomeChatSummaryModel model) {
            if(!StringUtils.isNullOrEmpty(model.getType())&& model.getType().equals("postback") && model.getPayload() != null){
                if (Utility.checkIsSkillKora()) {
                    if(composeFooterInterface != null) {
                        String quickReplyPayload = null;
                        try {
                            quickReplyPayload = (String) model.getPayload();
                        }catch (Exception e)
                        {
                            try {
                                QuickRepliesPayloadModel quickRepliesPayloadModel = (QuickRepliesPayloadModel) model.getPayload();
                                quickReplyPayload = quickRepliesPayloadModel.getName();
                            }
                            catch (Exception exception)
                            {
                                quickReplyPayload = "";
                            }
                        }

                        composeFooterInterface.onSendClick(quickReplyPayload, true);
                    }
                } else {
                        DialogCaller.showDialog(context, null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(composeFooterInterface != null)
                                    composeFooterInterface.onSendClick(Constants.SKILL_UTTERANCE+model.getPayload(),true);
                                dialog.dismiss();
                            }
                        });

                }

            }else if(!StringUtils.isNullOrEmpty(model.getType())&& model.getType().equals("open_form")){
                if(composeFooterInterface != null)
                    composeFooterInterface.launchActivityWithBundle(BotResponse.WELCOME_SUMMARY_VIEW_NOTIFICAION,null);
            }
    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }
}
