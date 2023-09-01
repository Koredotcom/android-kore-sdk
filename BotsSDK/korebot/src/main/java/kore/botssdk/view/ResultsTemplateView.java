package kore.botssdk.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import kore.botssdk.R;
import kore.botssdk.adapter.LiveSearchCyclerAdapter;
import kore.botssdk.adapter.SearchCentralPanelAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.ResultsViewTemplate;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class ResultsTemplateView extends LinearLayout {
    float dp1, layoutItemHeight = 0;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    String title;
    private RecyclerView lvLiveSearch, lvCentralPanel;
    private TextView tvSeeAllResults;
    private final Context mContext;
    private SharedPreferences sharedPreferences;
    private final int allResultsCount = 0;
    private ResultsViewTemplate defaultTemplate;
    private Gson gson;
    private LinearLayout llResults;
    private TextView tvPageTitle;
    private RelativeLayout llLiveSearch;

    public ResultsTemplateView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ResultsTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public ResultsTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.results_template_layout, this, true);
        tvSeeAllResults = findViewById(R.id.tvSeeAllResults);
        llLiveSearch = findViewById(R.id.llLiveSearch);
        lvLiveSearch = findViewById(R.id.lvLiveSearch);
        lvCentralPanel = findViewById(R.id.lvCentralPanel);
        llResults = findViewById(R.id.llResults);
        tvPageTitle = findViewById(R.id.tvPageTitle);
        dp1 = (int) DimensionUtil.dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        gson = new Gson();
        lvLiveSearch.setLayoutManager(new LinearLayoutManager(mContext));
        lvCentralPanel.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void populateResultsTemplateView(final PayloadInner payloadInner) {

        if (payloadInner != null)
        {
           if(payloadInner.getGraph_answer() != null && payloadInner.getGraph_answer().getPayload() != null
                && payloadInner.getGraph_answer().getPayload().getCenter_panel() != null
                && payloadInner.getGraph_answer().getPayload().getCenter_panel().getData() != null &&
                   payloadInner.getGraph_answer().getPayload().getCenter_panel().getData().size() > 0)
           {
               SearchCentralPanelAdapter liveSearchCyclerAdapter = new SearchCentralPanelAdapter(mContext, payloadInner.getGraph_answer().getPayload().getCenter_panel().getData(), invokeGenericWebViewInterface);
               lvCentralPanel.setAdapter(liveSearchCyclerAdapter);
           }
           else
           {
               llLiveSearch.setVisibility(GONE);
               tvSeeAllResults.setVisibility(GONE);
               tvPageTitle.setVisibility(VISIBLE);
               llResults.setVisibility(VISIBLE);
           }

            if(payloadInner.getResults() != null && payloadInner.getResults().get("web") != null
                    && payloadInner.getResults().get("web").getData() != null)
            {
                LiveSearchCyclerAdapter liveSearchCyclerAdapter = new LiveSearchCyclerAdapter(mContext, payloadInner.getResults().get("web").getData(),  invokeGenericWebViewInterface);
                lvLiveSearch.setAdapter(liveSearchCyclerAdapter);
            }

           tvSeeAllResults.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    tvSeeAllResults.setVisibility(GONE);
                    tvPageTitle.setVisibility(VISIBLE);
                    llResults.setVisibility(VISIBLE);
                }
            });
        }
    }

    public void setRestrictedMaxHeight(float restrictedMaxHeight) {
        this.restrictedMaxHeight = restrictedMaxHeight;
    }

    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
        this.restrictedMaxWidth = restrictedMaxWidth;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public int getViewHeight() {
        int viewHeight = 0;
//        if (lvLiveSearch != null) {
//            int count = 0;
//            if (lvLiveSearch.getAdapter() != null) {
//                count = lvLiveSearch.getAdapter().getItemCount();
//            }
        viewHeight = (int) (layoutItemHeight * 5);
//        }
        return viewHeight;
    }
}