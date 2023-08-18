package kore.botssdk.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.adapter.UniversalSearchViewAdapter;
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
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class UniversalSearchView extends ViewGroup implements VerticalListViewActionHelper {
    public UniversalSearchView(Context context) {
        super(context);
        initView();
    }

    public UniversalSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public UniversalSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    View root_layout;
    private float dp1;
    UniversalSearchViewAdapter adapter;
    View view_more;
    ComposeFooterInterface composeFooterInterface;
    boolean viewAllVisiblity=false;
    public final void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.universal_search_view, this, true);
        recycler_view = view.findViewById(R.id.recycler_view);
        root_layout = findViewById(R.id.root_layout);
        view_more = findViewById(R.id.view_more);
        dp1 = (int) DimensionUtil.dp1;
        layoutManager = new LinearLayoutManager(this.getContext());
        recycler_view.setLayoutManager(layoutManager);
        adapter = new UniversalSearchViewAdapter(this);
        adapter.setVerticalListViewActionHelper(this);


        view_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                composeFooterInterface.openFullView(BotResponse.TEMPLATE_TYPE_UNIVERSAL_SEARCH, new Gson().toJson(adapter.getData()), null,0);

            }
        });
    }


    public void itemClickPosition(int position)
    {
        composeFooterInterface.openFullView(BotResponse.TEMPLATE_TYPE_UNIVERSAL_SEARCH, new Gson().toJson(adapter.getData()), null,position);

    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
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
        View rootView = root_layout;
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


    public void populateData(ArrayList<KoraUniversalSearchModel> koraUniversalSearchModel) {

        if (adapter == null) {
            adapter = new UniversalSearchViewAdapter(this);
        }
        if (koraUniversalSearchModel != null&&koraUniversalSearchModel.size()>0) {
            koraUniversalSearchModel=getSortedData(koraUniversalSearchModel);
            root_layout.setVisibility(VISIBLE);
            adapter = new UniversalSearchViewAdapter(this);
            adapter.setVerticalListViewActionHelper(this);
            adapter.setData(koraUniversalSearchModel);
            recycler_view.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            view_more.setVisibility(viewAllVisiblity&&koraUniversalSearchModel.size()>1?VISIBLE:GONE);
        } else {
            root_layout.setVisibility(GONE);
            adapter.setData(null);
            adapter.setVerticalListViewActionHelper(this);
            recycler_view.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            view_more.setVisibility(GONE);
        }
    }


    private ArrayList<KoraUniversalSearchModel> getSortedData(ArrayList<KoraUniversalSearchModel> koraUniversalSearchModel)
    {
        viewAllVisiblity = false;
        ArrayList<KoraUniversalSearchModel> list=new ArrayList<>();
        for(KoraUniversalSearchModel model:koraUniversalSearchModel)
        {
            if(model.getMeetingNotes()!=null&&model.getMeetingNotes().size()>0)
            {
                if(model.getMeetingNotes().size()>1)
                {
                    viewAllVisiblity=true;
                }
                list.add(model);

            }else if(model.getEmails()!=null&&model.getEmails().size()>0)
            {
                if(model.getEmails().size()>1)
                {
                    viewAllVisiblity=true;
                }
                list.add(model);

            }else if(model.getFiles()!=null&&model.getFiles().size()>0)
            {
                if(model.getFiles().size()>1)
                {
                    viewAllVisiblity=true;
                }
                list.add(model);
            }
            else if(model.getKnowledge()!=null&&model.getKnowledge().size()>0)
            {
                if(model.getKnowledge().size()>1)
                {
                    viewAllVisiblity=true;
                }
                list.add(model);
            }
            else if(model.getKnowledgeCollection()!=null&&model.getKnowledgeCollection().getCombinedData()!=null&&model.getKnowledgeCollection().getCombinedData().size()>0)
            {
                if(model.getKnowledgeCollection().getCombinedData().size()>1)
                {
                    viewAllVisiblity=true;
                }
                list.add(model);
            }

        }
        return list;


    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {
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
        Bundle bundle=new Bundle();
        bundle.putString("mId",eId);
        composeFooterInterface.launchActivityWithBundle(BotResponse.US_MEETING_NOTES_TYPE,bundle);
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

        composeFooterInterface.knowledgeCollectionItemClick(elements,id);

    }
}
