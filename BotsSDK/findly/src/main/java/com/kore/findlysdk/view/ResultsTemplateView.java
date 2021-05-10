package com.kore.findlysdk.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;

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
//    private RecyclerView lvLiveSearch;
    private TextView tvSeeAllResults;
    private ArrayList<LiveSearchResultsModel> botListModelArrayList;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private LinearLayout llAllresults;
    private ArrayList<LiveSearchResultsModel> faqArrayList;
    private ArrayList<LiveSearchResultsModel> pagesArrayList;
    private ArrayList<LiveSearchResultsModel> taskArrayList;
    private Transition transitionTop;
    private Transition transitionBottom;

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
//        lvLiveSearch = (RecyclerView) findViewById(R.id.lvLiveSearch);
        tvSeeAllResults = (TextView) findViewById(R.id.tvSeeAllResults);
//        lvLiveSearch.setLayoutManager(new LinearLayoutManager(mContext));
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        llAllresults = (LinearLayout) findViewById(R.id.llAllresults);
        transitionTop = new Slide(Gravity.TOP);
        transitionBottom = new Slide(Gravity.BOTTOM);
    }

    public void populateResultsTemplateView(final PayloadInner payloadInner) {

        if (payloadInner != null && payloadInner.getElements() != null )
        {
            LiveSearchCyclerAdapter botListTemplateAdapter;
            final ArrayList<LiveSearchResultsModel> botListModelArrayList = (ArrayList<LiveSearchResultsModel>)payloadInner.getElements();
            llAllresults.removeAllViews();

            if(botListModelArrayList != null && botListModelArrayList.size() > 0)
            {
                getSortedList(botListModelArrayList);

                if(faqArrayList != null && faqArrayList.size() > 0)
                {
                    botListTemplateAdapter = new LiveSearchCyclerAdapter(getContext(), getTopFourList(faqArrayList), 0, invokeGenericWebViewInterface, composeFooterInterface);
//                lvLiveSearch.setAdapter(botListTemplateAdapter);
                    LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.search_template_new_cell, null);
                    final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
                    final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
                    final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
                    TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
                    alResults.setHasFixedSize(true);
                    if(faqArrayList.get(0).getAppearance() != null && faqArrayList.get(0).getAppearance().getTemplate() != null && faqArrayList.get(0).getAppearance().getTemplate().getType() != null)
                    {
                        if(faqArrayList.get(0).getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                            alResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        else
                            alResults.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    alResults.setAdapter(botListTemplateAdapter);
                    tvPageTitle.setText("FAQS");

                    ibResults.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(alResults.getVisibility() == View.GONE)
                            {
                                alResults.setVisibility(VISIBLE);
                                ibResults.setVisibility(GONE);
                                ibResults2.setVisibility(VISIBLE);
                            }
                        }
                    });

                    ibResults2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(alResults.getVisibility() == View.VISIBLE)
                            {
                                alResults.setVisibility(GONE);
                                ibResults2.setVisibility(GONE);
                                ibResults.setVisibility(VISIBLE);
                            }
                        }
                    });

                    llAllresults.addView(llFaq);
                }

                if(pagesArrayList != null && pagesArrayList.size() > 0)
                {
                    botListTemplateAdapter = new LiveSearchCyclerAdapter(getContext(), getTopFourList(pagesArrayList), 0, invokeGenericWebViewInterface, composeFooterInterface);
//                lvLiveSearch.setAdapter(botListTemplateAdapter);
                    LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.search_template_new_cell, null);
                    final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
                    final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
                    final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
                    TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
                    alResults.setHasFixedSize(true);
                    if(pagesArrayList.get(0).getAppearance() != null && pagesArrayList.get(0).getAppearance().getTemplate() != null && pagesArrayList.get(0).getAppearance().getTemplate().getType() != null)
                    {
                        if(pagesArrayList.get(0).getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                            alResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        else
                            alResults.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    alResults.setAdapter(botListTemplateAdapter);
                    tvPageTitle.setText("PAGES");

                    ibResults.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(alResults.getVisibility() == View.GONE)
                            {
                                ibResults.setVisibility(GONE);
                                ibResults2.setVisibility(VISIBLE);

                                alResults.setVisibility(VISIBLE);
                            }
                        }
                    });

                    ibResults2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(alResults.getVisibility() == View.VISIBLE)
                            {
                                ibResults2.setVisibility(GONE);
                                ibResults.setVisibility(VISIBLE);

                                alResults.setVisibility(View.GONE);
                            }
                        }
                    });

                    llAllresults.addView(llFaq);
                }


                tvSeeAllResults.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(botListModelArrayList != null && botListModelArrayList.size() > 0)
                        {
                            Intent intent = new Intent(mContext, FullResultsActivity.class);
                            intent.putExtra("originalQuery", payloadInner.getTemplate().getCleanQuery());
                            intent.putExtra("searchSetting", payloadInner.getResultsViewSetting());

                            if(sharedPreferences != null)
                                intent.putExtra("messagePayload", sharedPreferences.getString("Payload", ""));

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
//        if (lvLiveSearch != null) {
//            int count = 0;
//            if (lvLiveSearch.getAdapter() != null) {
//                count = lvLiveSearch.getAdapter().getItemCount();
//            }
            viewHeight = (int) (layoutItemHeight * 5);
//        }
        return viewHeight;
    }

    public int getViewWidth() {
        int viewHeight = 0;
//        if (lvLiveSearch != null) {
//            int count = 0;
//            if (lvLiveSearch.getAdapter() != null) {
//                count = lvLiveSearch.getAdapter().getItemCount();
//            }
            viewHeight = (5 > 0) ? (int) (restrictedMaxWidth -2*dp1) : 0;
//        }
        return viewHeight;
    }

    private ArrayList<LiveSearchResultsModel> getTopFourList(ArrayList<LiveSearchResultsModel> arrResults)
    {
        ArrayList<LiveSearchResultsModel> arrTempResults = new ArrayList<>();

        for (int i = 0; i < arrResults.size(); i++)
        {
            if(arrResults.get(i).get__contentType() != null)
            {
                if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.FAQ))
                {
                    arrTempResults.add(arrResults.get(i));
                    if(arrTempResults.size() == 2)
                        break;
                }
            }
            else if(arrResults.get(i).getContentType() != null)
            {
                if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                {
                    arrTempResults.add(arrResults.get(i));
                    if(arrTempResults.size() == 2)
                        break;
                }
            }

        }

        if(arrTempResults.size() >= 1)
        {
            int suntoAdd = arrTempResults.size()+2;
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).get__contentType() != null)
                {
                    if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
                    {
                        arrTempResults.add(arrResults.get(i));
                        if(arrTempResults.size() == suntoAdd)
                            break;
                    }
                }
                else if(arrResults.get(i).getContentType() != null)
                {
                    if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                    {
                        arrTempResults.add(arrResults.get(i));
                        if(arrTempResults.size() == suntoAdd)
                            break;
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).get__contentType() != null)
                {
                    if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
                    {
                        arrTempResults.add(arrResults.get(i));
                        if(arrTempResults.size() == 2)
                            break;
                    }
                }
                else if(arrResults.get(i).getContentType() != null)
                {
                    if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                    {
                        arrTempResults.add(arrResults.get(i));
                        if(arrTempResults.size() == 2)
                            break;
                    }
                }
            }
        }

        if(arrTempResults.size() >= 1)
        {
            int suntoAdd = arrTempResults.size()+2;
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).get__contentType() != null)
                {
                    if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.TASK))
                    {
                        arrTempResults.add(arrResults.get(i));
                        if(arrTempResults.size() == suntoAdd)
                            break;
                    }
                }
                else if(arrResults.get(i).getContentType() != null)
                {
                    if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.TASK))
                    {
                        arrTempResults.add(arrResults.get(i));
                        if(arrTempResults.size() == suntoAdd)
                            break;
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).get__contentType() != null)
                {
                    if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.TASK))
                    {
                        arrTempResults.add(arrResults.get(i));
                        if(arrTempResults.size() == 2)
                            break;
                    }
                }
                else if(arrResults.get(i).getContentType() != null)
                {
                    if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.TASK))
                    {
                        arrTempResults.add(arrResults.get(i));
                        if(arrTempResults.size() == 2)
                            break;
                    }
                }
            }
        }

        return arrTempResults;
    }

    private ArrayList<LiveSearchResultsModel> getSortedList(ArrayList<LiveSearchResultsModel> arrResults)
    {
        ArrayList<LiveSearchResultsModel> arrTempResults = new ArrayList<>();
        faqArrayList = new ArrayList<>();
        pagesArrayList = new ArrayList<>();
        taskArrayList = new ArrayList<>();

        for (int i = 0; i < arrResults.size(); i++)
        {
            if(arrResults.get(i).get__contentType() != null)
            {
                if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.FAQ))
                {
                    faqArrayList.add(arrResults.get(i));
                }
                else if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
                {
                    pagesArrayList.add(arrResults.get(i));
                }
                else if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.TASK))
                {
                    taskArrayList.add(arrResults.get(i));
                }
            }
            else if(arrResults.get(i).getContentType() != null)
            {
                if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                {
                    faqArrayList.add(arrResults.get(i));
                }
                else if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                {
                    pagesArrayList.add(arrResults.get(i));
                }
                else if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.TASK))
                {
                    taskArrayList.add(arrResults.get(i));
                }
            }
        }

        return arrTempResults;
    }
}