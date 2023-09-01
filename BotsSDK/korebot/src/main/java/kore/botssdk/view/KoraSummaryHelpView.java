package kore.botssdk.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.databinding.SummaryHelpLayoutBinding;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BaseCalenderTemplateModel;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.ButtonTemplate;
import kore.botssdk.models.ContactViewListModel;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.KoraSummaryHelpModel;
import kore.botssdk.models.QuickRepliesPayloadModel;
import kore.botssdk.models.WelcomeChatSummaryModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class KoraSummaryHelpView extends ViewGroup implements VerticalListViewActionHelper {

    private ComposeFooterInterface composeFooterInterface;
    private SummaryHelpLayoutBinding summaryViewBinding;
    private KoraSummaryHelpRecyclerAdapter myRecyclerViewAdapter;
    private float dp1;
    private RecyclerView summaryList;
    private final boolean isWeatherDesc = true;

    public KoraSummaryHelpView(Context context) {
        super(context);
        init();
    }


    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void populateData(final KoraSummaryHelpModel summaryModel) {
        if(summaryModel != null){
            summaryViewBinding.getRoot().setVisibility(VISIBLE);
            summaryViewBinding.setSummaryInfo(summaryModel);

            ArrayList<WelcomeChatSummaryModel> list = new ArrayList<WelcomeChatSummaryModel>();

            if(summaryModel.getButtons() != null && summaryModel.getButtons().size() > 0){
                for(ButtonTemplate item : summaryModel.getButtons()){
                    WelcomeChatSummaryModel mdl = new WelcomeChatSummaryModel();
                    mdl.setSummary(item.getTitle());
                    mdl.setPayload(item.getPayload());
                    mdl.setType(item.getType());
                    list.add(mdl);
                }
            }


            myRecyclerViewAdapter.setData(list);


        }else{
            summaryViewBinding.getRoot().setVisibility(GONE);
            summaryViewBinding.setSummaryInfo(null);
        }
    }

    private void init() {
        summaryViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.summary_help_layout, this, true);

        dp1 = (int) DimensionUtil.dp1;
        summaryViewBinding.setViewBase(this);

        summaryList = findViewById(R.id.summary_items_list);
        myRecyclerViewAdapter = new KoraSummaryHelpRecyclerAdapter(getContext());
        myRecyclerViewAdapter.setExpanded(false);
        myRecyclerViewAdapter.setVerticalListViewActionHelper(this);
        summaryList.setLayoutManager(new LinearLayoutManager(getContext()));
        summaryViewBinding.setMyAdapter(myRecyclerViewAdapter);
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
        View rootView = summaryViewBinding.getRoot();
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
        if(!StringUtils.isNullOrEmpty(model.getType())&& model.getType().equals("postback") && model.getPayload() != null
                && composeFooterInterface != null){
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

            composeFooterInterface.onSendClick(quickReplyPayload ,true);
        }
    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }
}
