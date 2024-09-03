package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.LiveSearchCyclerAdapter;
import kore.botssdk.adapter.SearchCentralPanelAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;

public class ResultsTemplateHolder extends BaseViewHolder {
    private final RecyclerView lvLiveSearch;
    private final RecyclerView lvCentralPanel;
    private final TextView tvSeeAllResults;
    private final LinearLayout llResults;
    private final TextView tvPageTitle;
    private final RelativeLayout llLiveSearch;

    public static ResultsTemplateHolder getInstance(ViewGroup parent) {
        return new ResultsTemplateHolder(createView(R.layout.template_results, parent));
    }

    private ResultsTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        tvSeeAllResults = itemView.findViewById(R.id.tvSeeAllResults);
        llLiveSearch = itemView.findViewById(R.id.llLiveSearch);
        lvLiveSearch = itemView.findViewById(R.id.lvLiveSearch);
        lvCentralPanel = itemView.findViewById(R.id.lvCentralPanel);
        llResults = itemView.findViewById(R.id.llResults);
        tvPageTitle = itemView.findViewById(R.id.tvPageTitle);
        lvLiveSearch.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        lvCentralPanel.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        if (payloadInner.getGraph_answer() != null && payloadInner.getGraph_answer().getPayload() != null
                && payloadInner.getGraph_answer().getPayload().getCenter_panel() != null
                && payloadInner.getGraph_answer().getPayload().getCenter_panel().getData() != null &&
                payloadInner.getGraph_answer().getPayload().getCenter_panel().getData().size() > 0) {
            SearchCentralPanelAdapter panelAdapter = new SearchCentralPanelAdapter(itemView.getContext(), payloadInner.getGraph_answer().getPayload().getCenter_panel().getData(), invokeGenericWebViewInterface);
            lvCentralPanel.setAdapter(panelAdapter);
        } else {
            llLiveSearch.setVisibility(View.GONE);
            tvSeeAllResults.setVisibility(View.GONE);
            tvPageTitle.setVisibility(View.VISIBLE);
            llResults.setVisibility(View.VISIBLE);
        }

        if (payloadInner.getResults() != null && payloadInner.getResults().get("web") != null
                && payloadInner.getResults().get("web").getData() != null) {
            LiveSearchCyclerAdapter liveSearchCyclerAdapter = new LiveSearchCyclerAdapter(itemView.getContext(), payloadInner.getResults().get("web").getData(), invokeGenericWebViewInterface);
            lvLiveSearch.setAdapter(liveSearchCyclerAdapter);
        }

        tvSeeAllResults.setOnClickListener(view -> {
            tvSeeAllResults.setVisibility(View.GONE);
            tvPageTitle.setVisibility(View.VISIBLE);
            llResults.setVisibility(View.VISIBLE);
        });
    }
}
