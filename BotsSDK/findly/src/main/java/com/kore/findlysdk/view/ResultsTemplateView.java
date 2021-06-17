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
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.kore.findlysdk.adapters.SearchAssistCarouselAdapter;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.listners.VerticalListViewActionHelper;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.PayloadInner;
import com.kore.findlysdk.models.ResultsViewAppearance;
import com.kore.findlysdk.models.ResultsViewMapping;
import com.kore.findlysdk.models.ResultsViewSetting;
import com.kore.findlysdk.models.ResultsViewTemplate;
import com.kore.findlysdk.models.ResultsViewlayout;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;

import java.util.ArrayList;

public class ResultsTemplateView extends LinearLayout {

    float dp1, layoutItemHeight = 0;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    String title;
//    private RecyclerView lvLiveSearch;
    private TextView tvSeeAllResults;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private LinearLayout llAllresults;
    private ArrayList<LiveSearchResultsModel> faqArrayList;
    private ArrayList<LiveSearchResultsModel> pagesArrayList;
    private ArrayList<LiveSearchResultsModel> taskArrayList;
    private ArrayList<LiveSearchResultsModel> fileArrayList;
    private ArrayList<LiveSearchResultsModel> dataArrayList;
    private ResultsViewAppearance faq;
    private ResultsViewAppearance page;
    private ResultsViewAppearance file;
    private ResultsViewAppearance task;
    private ResultsViewAppearance data;

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
    }

    public void populateResultsTemplateView(final PayloadInner payloadInner) {

        if (payloadInner != null && payloadInner.getElements() != null )
        {
            LiveSearchCyclerAdapter botListTemplateAdapter;
            final ArrayList<LiveSearchResultsModel> botListModelArrayList = (ArrayList<LiveSearchResultsModel>)payloadInner.getElements();
            llAllresults.removeAllViews();

            if(botListModelArrayList != null && botListModelArrayList.size() > 0)
            {
                getSearchSettings(payloadInner.getResultsViewSetting());
                getSortedList(botListModelArrayList);

                if(faqArrayList != null && faqArrayList.size() > 0)
                {
//                    botListTemplateAdapter = new LiveSearchCyclerAdapter(getContext(), getTopFourList(faqArrayList), 0, invokeGenericWebViewInterface, composeFooterInterface);
//                lvLiveSearch.setAdapter(botListTemplateAdapter);

                    addLayoutView(getTopFourList(faqArrayList), faq, 1, BundleConstants.FAQ);
//                    LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.search_template_new_cell, null);
//                    final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
//                    final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
//                    final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
//                    TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
//                    alResults.setHasFixedSize(true);
//                    if(faqArrayList.get(0).getAppearance() != null && faqArrayList.get(0).getAppearance().getTemplate() != null && faqArrayList.get(0).getAppearance().getTemplate().getType() != null)
//                    {
//                        if(faqArrayList.get(0).getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
//                            alResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
//                        else
//                            alResults.setLayoutManager(new LinearLayoutManager(getContext()));
//                    }
//
//                    alResults.setAdapter(botListTemplateAdapter);
//                    tvPageTitle.setText("FAQS");
//
//                    ibResults.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if(alResults.getVisibility() == View.GONE)
//                            {
//                                alResults.setVisibility(VISIBLE);
//                                ibResults.setVisibility(GONE);
//                                ibResults2.setVisibility(VISIBLE);
//                            }
//                        }
//                    });
//
//                    ibResults2.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if(alResults.getVisibility() == View.VISIBLE)
//                            {
//                                alResults.setVisibility(GONE);
//                                ibResults2.setVisibility(GONE);
//                                ibResults.setVisibility(VISIBLE);
//                            }
//                        }
//                    });
//
//                    llAllresults.addView(llFaq);
                }

                if(pagesArrayList != null && pagesArrayList.size() > 0)
                {
//                    botListTemplateAdapter = new LiveSearchCyclerAdapter(getContext(), getTopFourList(pagesArrayList), 0, invokeGenericWebViewInterface, composeFooterInterface);
//                lvLiveSearch.setAdapter(botListTemplateAdapter);

                    addLayoutView(getTopFourList(pagesArrayList), page, 2, BundleConstants.WEB);

//                    LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.search_template_new_cell, null);
//                    final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
//                    final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
//                    final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
//                    TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
//                    alResults.setHasFixedSize(true);
//                    if(pagesArrayList.get(0).getAppearance() != null && pagesArrayList.get(0).getAppearance().getTemplate() != null && pagesArrayList.get(0).getAppearance().getTemplate().getType() != null)
//                    {
//                        if(pagesArrayList.get(0).getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
//                            alResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
//                        else
//                            alResults.setLayoutManager(new LinearLayoutManager(getContext()));
//                    }
//
//                    alResults.setAdapter(botListTemplateAdapter);
//                    tvPageTitle.setText("Web");
//
//                    ibResults.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if(alResults.getVisibility() == View.GONE)
//                            {
//                                ibResults.setVisibility(GONE);
//                                ibResults2.setVisibility(VISIBLE);
//
//                                alResults.setVisibility(VISIBLE);
//                            }
//                        }
//                    });
//
//                    ibResults2.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if(alResults.getVisibility() == View.VISIBLE)
//                            {
//                                ibResults2.setVisibility(GONE);
//                                ibResults.setVisibility(VISIBLE);
//
//                                alResults.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//
//                    llAllresults.addView(llFaq);
                }

                if(taskArrayList != null && taskArrayList.size() > 0)
                {
                    if(task == null)
                        task = getResultsViewAppearance(BundleConstants.TASK);

                    addLayoutView(getTopFourList(taskArrayList), task, 3, BundleConstants.TASK);
                }

                if(fileArrayList != null && fileArrayList.size() > 0)
                {
                    addLayoutView(getTopFourList(fileArrayList), file, 4, BundleConstants.FILE);
                }

                if(dataArrayList != null && dataArrayList.size() > 0)
                {
                    addLayoutView(getTopFourList(dataArrayList), data, 5, BundleConstants.DATA);
                }


                tvSeeAllResults.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(botListModelArrayList != null && botListModelArrayList.size() > 0)
                        {
                            Intent intent = new Intent(mContext, FullResultsActivity.class);
                            intent.putExtra("originalQuery", payloadInner.getTemplate().getCleanQuery());
                            intent.putExtra("searchSetting", payloadInner.getFullSearchresultsViewSetting());

                            if(sharedPreferences != null)
                                intent.putExtra("messagePayload", sharedPreferences.getString("Payload", ""));

                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    private void addLayoutView(ArrayList<LiveSearchResultsModel> arrSearchResults, final ResultsViewAppearance appearance, int from, String constant)
    {
        LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.search_template_new_cell, null);
        final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
        final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
        final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
        TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
        final HeightAdjustableViewPager heightAdjustableViewPager = (HeightAdjustableViewPager) llFaq.findViewById(R.id.carouselViewpager);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
        ViewGroup.LayoutParams params= heightAdjustableViewPager.getLayoutParams();
        params.height = (int)(170 * dp1);    //500px
        heightAdjustableViewPager.setLayoutParams(params);

        heightAdjustableViewPager.setPageMargin(pageMargin);
        if(appearance != null && appearance.getTemplate() != null && appearance.getTemplate().getType() != null)
        {
            heightAdjustableViewPager.setVisibility(GONE);
            alResults.setVisibility(View.VISIBLE);

            if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                alResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
            else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
            {
                heightAdjustableViewPager.setVisibility(View.VISIBLE);
                alResults.setVisibility(GONE);

                if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
                {
                    params.height = (int)(300 * dp1);    //500px
                    heightAdjustableViewPager.setLayoutParams(params);
                }
                else if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_HEADER))
                {
                    params.height = (int)(100 * dp1);    //500px
                    heightAdjustableViewPager.setLayoutParams(params);
                }

                heightAdjustableViewPager.setAdapter(new SearchAssistCarouselAdapter(getContext(), arrSearchResults , 0, invokeGenericWebViewInterface, null));
                alResults.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            }
            else
            {
//                if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_HEADER))
//                {
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ((int)(dp1 * 20) * arrSearchResults.size()));
//                    alResults.setLayoutParams(lp);
//                }
//
                alResults.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }

        alResults.setAdapter(new LiveSearchCyclerAdapter(getContext(), arrSearchResults, 0, invokeGenericWebViewInterface, composeFooterInterface));
        switch (from)
        {
            case 1:
            {
                tvPageTitle.setText("FAQS");
                break;
            }
            case 2:
            {
                tvPageTitle.setText("Web");
                break;
            }
            case 3:
            {
                tvPageTitle.setText("Task");
                break;
            }
            case 4:
            {
                tvPageTitle.setText("File");
                break;
            }
            case 5:
            {
                tvPageTitle.setText("Data");
                break;
            }
        }


        ibResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alResults.getVisibility() == View.GONE || heightAdjustableViewPager.getVisibility() == GONE)
                {
                    if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                        heightAdjustableViewPager.setVisibility(View.VISIBLE);
                    else
                        alResults.setVisibility(View.VISIBLE);

                    ibResults.setVisibility(GONE);
                    ibResults2.setVisibility(View.VISIBLE);
                }
            }
        });

        ibResults2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alResults.getVisibility() == View.VISIBLE || heightAdjustableViewPager.getVisibility() == View.VISIBLE)
                {
                    if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                        heightAdjustableViewPager.setVisibility(GONE);
                    else
                        alResults.setVisibility(GONE);

                    ibResults2.setVisibility(GONE);
                    ibResults.setVisibility(View.VISIBLE);
                }
            }
        });

        llAllresults.addView(llFaq);
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
            if(arrResults.get(i).getSysContentType() != null)
            {
                if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.FAQ))
                {
                    arrTempResults.add(arrResults.get(i));
                    if(arrTempResults.size() == sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2))
                        break;
                }
            }

        }

        if(arrTempResults.size() >= 1)
        {
            int suntoAdd = arrTempResults.size()+2;
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).getSysContentType() != null)
                {
                    if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.WEB))
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
                if(arrResults.get(i).getSysContentType() != null)
                {
                    if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.WEB))
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
                if(arrResults.get(i).getSysContentType() != null)
                {
                    if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.TASK))
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
                if(arrResults.get(i).getSysContentType() != null)
                {
                    if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.TASK))
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
                if(arrResults.get(i).getSysContentType() != null)
                {
                    if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.FILE))
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
                if(arrResults.get(i).getSysContentType() != null)
                {
                    if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.FILE))
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
                if(arrResults.get(i).getSysContentType() != null)
                {
                    if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.DATA))
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
                if(arrResults.get(i).getSysContentType() != null)
                {
                    if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.DATA))
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
        fileArrayList = new ArrayList<>();
        dataArrayList = new ArrayList<>();

        for (int i = 0; i < arrResults.size(); i++)
        {
            if(!StringUtils.isNullOrEmpty(arrResults.get(i).getContentType()))
                arrResults.get(i).setSysContentType(arrResults.get(i).getContentType());

            if(arrResults.get(i).getSysContentType() != null)
            {
                if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.FAQ))
                {
                    arrResults.get(i).setAppearance(faq);
                    faqArrayList.add(arrResults.get(i));
                }
                else if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.WEB))
                {
                    arrResults.get(i).setAppearance(page);
                    pagesArrayList.add(arrResults.get(i));
                }
                else if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.TASK))
                {
                    arrResults.get(i).setAppearance(task);
                    taskArrayList.add(arrResults.get(i));
                }
                else if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.FILE))
                {
                    arrResults.get(i).setAppearance(file);
                    fileArrayList.add(arrResults.get(i));
                }
                else if(arrResults.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.DATA))
                {
                    arrResults.get(i).setAppearance(data);
                    dataArrayList.add(arrResults.get(i));
                }
            }
        }

        return arrTempResults;
    }

    private void getSearchSettings(ResultsViewSetting searchSettings)
    {
        if(searchSettings != null && searchSettings.getAppearance() != null
                && searchSettings.getAppearance().size() > 0)
        {
            for(int i = 0; i < searchSettings.getAppearance().size(); i++)
            {
                if(searchSettings.getAppearance().get(i).getType().equalsIgnoreCase(BundleConstants.FAQ))
                    faq = searchSettings.getAppearance().get(i);
                else if(searchSettings.getAppearance().get(i).getType().equalsIgnoreCase(BundleConstants.PAGE))
                    page = searchSettings.getAppearance().get(i);
                else if(searchSettings.getAppearance().get(i).getType().equalsIgnoreCase(BundleConstants.DOCUMENT))
                    file = searchSettings.getAppearance().get(i);
                else if(searchSettings.getAppearance().get(i).getType().equalsIgnoreCase(BundleConstants.TASK))
                {
                    task = searchSettings.getAppearance().get(i);

                    if(task == null)
                        task = getResultsViewAppearance(BundleConstants.TASK);
                }
                else if(searchSettings.getAppearance().get(i).getType().equalsIgnoreCase(BundleConstants.STRUCTURE_DATA))
                    data = searchSettings.getAppearance().get(i);
            }
        }
    }

    private ResultsViewAppearance getResultsViewAppearance(String type)
    {
        ResultsViewAppearance resultsViewAppearance = new ResultsViewAppearance();
        resultsViewAppearance.setType(type);
        resultsViewAppearance.setTemplateId("fsrt-c86f6f10-76b1-5161-88c3-b1d03b1c0de7");

        ResultsViewTemplate template = new ResultsViewTemplate();
        template.set_id("fsrt-c86f6f10-76b1-5161-88c3-b1d03b1c0de7");
        template.setAppearanceType(type);
        template.setCreatedOn("2021-05-31T07:25:44.364Z");
        template.setStreamId("st-12466d46-dc91-5855-a301-b6ecd7a95d82");
        template.setSearchIndexId("sidx-4c33c7cf-9561-58f2-b547-4745ce12b513");
        template.setIndexPipelineId("fip-f3cef85c-efd0-5e1e-bdb0-4c5b52b288e7");
        template.setlModifiedOn("2021-05-31T07:25:44.364Z");
        template.setType("listTemplate1");

        ResultsViewlayout resultsViewlayout = new ResultsViewlayout();
        resultsViewlayout.setLayoutType("tileWithHeader");
        resultsViewlayout.setIsClickable(true);
        resultsViewlayout.setBehaviour("webpage");
        resultsViewlayout.setTextAlignment("left");
        template.setLayout(resultsViewlayout);

        ResultsViewMapping resultsViewMapping = new ResultsViewMapping();
        resultsViewMapping.setDescription("filePreview");
        resultsViewMapping.setHeading("fileTitle");
        resultsViewMapping.setUrl("fileUrl");
        template.setMapping(resultsViewMapping);

        resultsViewAppearance.setTemplate(template);

        return resultsViewAppearance;
    }
}