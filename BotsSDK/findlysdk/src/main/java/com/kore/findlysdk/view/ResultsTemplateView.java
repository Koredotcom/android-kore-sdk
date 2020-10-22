package com.kore.findlysdk.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.activity.FullResultsActivity;
import com.kore.findlysdk.activity.MainActivity;
import com.kore.findlysdk.adapters.LiveSearchCyclerAdapter;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.listners.VerticalListViewActionHelper;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.PayloadInner;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BundleConstants;

import java.util.ArrayList;

public class ResultsTemplateView extends LinearLayout {

    float dp1, layoutItemHeight = 0;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    VerticalListViewActionHelper verticalListViewActionHelper;
    String title;
    private RecyclerView lvLiveSearch;
    private TextView tvSeeAllResults;
    private ArrayList<LiveSearchResultsModel> botListModelArrayList;
    private Context mContext;

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
        lvLiveSearch = (RecyclerView) findViewById(R.id.lvLiveSearch);
        tvSeeAllResults = (TextView) findViewById(R.id.tvSeeAllResults);
        lvLiveSearch.setLayoutManager(new LinearLayoutManager(mContext));
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);

    }

    public void populateResultsTemplateView(PayloadInner payloadInner) {

        if (payloadInner != null && payloadInner.getElements() != null )
        {
            LiveSearchCyclerAdapter botListTemplateAdapter;
            final ArrayList<LiveSearchResultsModel> botListModelArrayList = (ArrayList<LiveSearchResultsModel>)payloadInner.getElements();

            if(botListModelArrayList != null && botListModelArrayList.size() > 0)
            {
                botListTemplateAdapter = new LiveSearchCyclerAdapter(getContext(), getTopFourList(botListModelArrayList), 1, invokeGenericWebViewInterface);
                lvLiveSearch.setAdapter(botListTemplateAdapter);

                tvSeeAllResults.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(botListModelArrayList != null && botListModelArrayList.size() > 0)
                        {
                            Intent intent = new Intent(mContext, FullResultsActivity.class);
                            intent.putExtra("Results", botListModelArrayList);
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
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
        if (lvLiveSearch != null) {
            int count = 0;
            if (lvLiveSearch.getAdapter() != null) {
                count = lvLiveSearch.getAdapter().getItemCount();
            }
            viewHeight = (int) (layoutItemHeight * count);
        }
        return viewHeight;
    }

    public int getViewWidth() {
        int viewHeight = 0;
        if (lvLiveSearch != null) {
            int count = 0;
            if (lvLiveSearch.getAdapter() != null) {
                count = lvLiveSearch.getAdapter().getItemCount();
            }
            viewHeight = (count > 0) ? (int) (restrictedMaxWidth -2*dp1) : 0;
        }
        return viewHeight;
    }

    private ArrayList<LiveSearchResultsModel> getTopFourList(ArrayList<LiveSearchResultsModel> arrResults)
    {
        ArrayList<LiveSearchResultsModel> arrTempResults = new ArrayList<>();
        for (int i = 0; i < arrResults.size(); i++)
        {
            if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.FAQ))
            {
                arrTempResults.add(arrResults.get(i));
                if(arrTempResults.size() == 2)
                    break;
            }
        }

        if(arrTempResults.size() >= 1)
        {
            int suntoAdd = arrTempResults.size()+2;
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                {
                    arrTempResults.add(arrResults.get(i));
                    if(arrTempResults.size() == suntoAdd)
                        break;
                }
            }
        }
        else
        {
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                {
                    arrTempResults.add(arrResults.get(i));
                    if(arrTempResults.size() == 2)
                        break;
                }
            }
        }

        return arrTempResults;
    }
}