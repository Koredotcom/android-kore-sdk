package com.kore.findlysdk.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.kore.findlysdk.adapters.LiveSearchAdaper;
import com.kore.findlysdk.adapters.LiveSearchCyclerAdapter;
import com.kore.findlysdk.adapters.LiveSearchDynamicAdapter;
import com.kore.findlysdk.adapters.SearchAssistCarouselAdapter;
import com.kore.findlysdk.adapters.SearchResultsCarouselAdapter;
import com.kore.findlysdk.fragments.FindlyFragment;
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
import java.util.HashMap;

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
    private ResultsViewAppearance faq;
    private ResultsViewAppearance page;
    private ResultsViewAppearance file;
    private ResultsViewAppearance task;
    private ResultsViewAppearance data;
    private int allResultsCount = 0;
    private ResultsViewTemplate defaultTemplate;

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
        tvSeeAllResults = (TextView) findViewById(R.id.tvSeeAllResults);
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
            allResultsCount = 0;
            if(botListModelArrayList != null && botListModelArrayList.size() > 0)
            {
                getSearchSettings(payloadInner.getResultsViewSetting());

                if(payloadInner.getTemplate().getResults().getFaq() != null && payloadInner.getTemplate().getResults().getFaq().getData() != null
                        && payloadInner.getTemplate().getResults().getFaq().getData().size() > 0)
                {
                    allResultsCount += payloadInner.getTemplate().getResults().getFaq().getDoc_count();
                    addLayoutView(getTopList(payloadInner.getTemplate().getResults().getFaq().getData(), BundleConstants.FAQ), faq, 1, payloadInner);
                }

                if(payloadInner.getTemplate().getResults().getWeb() != null && payloadInner.getTemplate().getResults().getWeb().getData() != null
                        && payloadInner.getTemplate().getResults().getWeb().getData().size() > 0)
                {
                    allResultsCount += payloadInner.getTemplate().getResults().getWeb().getDoc_count();
                    addLayoutView(getTopList(payloadInner.getTemplate().getResults().getWeb().getData(), BundleConstants.WEB), page, 2, payloadInner);
                }

                if(payloadInner.getTemplate().getResults().getTask() != null && payloadInner.getTemplate().getResults().getTask().getData() != null
                        && payloadInner.getTemplate().getResults().getTask().getData().size() > 0)
                {
                    allResultsCount += payloadInner.getTemplate().getResults().getTask().getDoc_count();
                    addLayoutView(getTopList(payloadInner.getTemplate().getResults().getTask().getData(), BundleConstants.TASK), task, 3, payloadInner);
                }

                if(payloadInner.getTemplate().getResults().getFile() != null && payloadInner.getTemplate().getResults().getFile().getData() != null
                        && payloadInner.getTemplate().getResults().getFile().getData().size() > 0)
                {
                    allResultsCount += payloadInner.getTemplate().getResults().getFile().getDoc_count();
                    addLayoutView(getTopList(payloadInner.getTemplate().getResults().getFile().getData(), BundleConstants.FILE), file, 4, payloadInner);
                }

                if(payloadInner.getTemplate().getResults().getDefault_group() != null && payloadInner.getTemplate().getResults().getDefault_group().getData() != null
                        && payloadInner.getTemplate().getResults().getDefault_group().getData().size() > 0)
                {
                    allResultsCount += payloadInner.getTemplate().getResults().getDefault_group().getDoc_count();
//                    addLayoutView(getTopList(payloadInner.getTemplate().getResults().getDefault_group().getData(), BundleConstants.DATA), data, 5, payloadInner);
                }

//                if(payloadInner.getTemplate().getResults().getData() != null
//                 && payloadInner.getTemplate().getResults().getData().size() > 0)
//                {
//                    allResultsCount += payloadInner.getTemplate().getResults().getData().size();
//                    addLayoutView(getTopList(payloadInner.getTemplate().getResults().getData(), BundleConstants.DATA), data, 5, payloadInner);
//                }

                tvSeeAllResults.setText(Html.fromHtml("See all <font color=#A4A4A4>("+allResultsCount+" results) </font>"));

                tvSeeAllResults.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(botListModelArrayList != null && botListModelArrayList.size() > 0)
                        {
                            Intent intent = new Intent(mContext, FullResultsActivity.class);
                            intent.putExtra("originalQuery", payloadInner.getTemplate().getCleanQuery());
                            intent.putExtra("searchSetting", payloadInner.getFullSearchresultsViewSetting());
                            intent.putExtra("searchRequestId", payloadInner.getRequestId());

                            if(sharedPreferences != null)
                                intent.putExtra("messagePayload", sharedPreferences.getString("Payload", ""));

                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    private void addLayoutView(ArrayList<LiveSearchResultsModel> arrSearchResults, final ResultsViewAppearance appearance, final int from, final PayloadInner payloadInner)
    {
        LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.search_template_new_cell, null);
        final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
        final RelativeLayout rlTitle = (RelativeLayout) llFaq.findViewById(R.id.rlTitle);
        final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
        final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
        TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
        TextView tvShowAll = (TextView) llFaq.findViewById(R.id.tvShowAll);
        final HeightAdjustableViewPager heightAdjustableViewPager = (HeightAdjustableViewPager) llFaq.findViewById(R.id.carouselViewpager);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
        alResults.addItemDecoration(new VerticalSpaceItemDecoration((int)(10 * dp1)));
        heightAdjustableViewPager.setPageMargin(pageMargin);

        if(appearance != null && appearance.getTemplate() != null && appearance.getTemplate().getType() != null)
        {
            heightAdjustableViewPager.setVisibility(GONE);
            alResults.setVisibility(View.VISIBLE);
            tvShowAll.setVisibility(GONE);

            if(appearance.getTemplate().getLayout() != null)
            {
                if(appearance.getTemplate().getLayout().getRenderTitle())
                    rlTitle.setVisibility(VISIBLE);
                else
                    rlTitle.setVisibility(GONE);
            }

            if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                alResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
            else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
            {
                heightAdjustableViewPager.setVisibility(View.VISIBLE);
                alResults.setVisibility(GONE);

//                if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
//                {
//                    params.height = (int)(300 * dp1);    //500px
//                    heightAdjustableViewPager.setLayoutParams(params);
//                }
//                else if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_HEADER))
//                {
//                    params.height = (int)(100 * dp1);    //500px
//                    heightAdjustableViewPager.setLayoutParams(params);
//                }

                heightAdjustableViewPager.setAdapter(new SearchAssistCarouselAdapter(getContext(), arrSearchResults , 0, invokeGenericWebViewInterface, null));
            }
            else
            {
                alResults.setLayoutManager(new LinearLayoutManager(getContext()));
                alResults.setAdapter(new LiveSearchCyclerAdapter(getContext(), arrSearchResults, 0, invokeGenericWebViewInterface, composeFooterInterface));
            }
        }

        switch (from)
        {
            case 1:
            {
                tvPageTitle.setText("FAQS");
                tvShowAll.setText("Show all Faqs");
                break;
            }
            case 2:
            {
                tvPageTitle.setText("Web");
                tvShowAll.setText("Show all WebPages");
                break;
            }
            case 3:
            {
                tvPageTitle.setText("Task");
                tvShowAll.setText("Show all Tasks");
                break;
            }
            case 4:
            {
                tvPageTitle.setText("File");
                tvShowAll.setText("Show all Files");
                break;
            }
            case 5:
            {
                tvPageTitle.setText("Data");
                tvShowAll.setText("Show all data");
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

        tvShowAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, FullResultsActivity.class);
                intent.putExtra("originalQuery", payloadInner.getTemplate().getCleanQuery());
                intent.putExtra("searchSetting", payloadInner.getFullSearchresultsViewSetting());
                intent.putExtra("from", from);
                if(sharedPreferences != null)
                    intent.putExtra("messagePayload", sharedPreferences.getString("Payload", ""));

                mContext.startActivity(intent);
            }
        });

        llAllresults.addView(llFaq);
    }

    private void addResultsView(ArrayList<HashMap<String, Object>> arrSearchResults, final ResultsViewAppearance appearance, final int from, final PayloadInner payloadInner)
    {
        LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.search_template_new_cell, null);
        final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
        final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
        final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
        TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
        TextView tvShowAll = (TextView) llFaq.findViewById(R.id.tvShowAll);
        final HeightAdjustableViewPager heightAdjustableViewPager = (HeightAdjustableViewPager) llFaq.findViewById(R.id.carouselViewpager);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
//        ViewGroup.LayoutParams params= heightAdjustableViewPager.getLayoutParams();
//        params.height = (int)(170 * dp1);    //500px
//        heightAdjustableViewPager.setLayoutParams(params);

        heightAdjustableViewPager.setPageMargin(pageMargin);
        if(appearance != null && appearance.getTemplate() != null && appearance.getTemplate().getType() != null)
        {
            heightAdjustableViewPager.setVisibility(GONE);
            alResults.setVisibility(View.VISIBLE);
            tvShowAll.setVisibility(VISIBLE);

            if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
            {
                alResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
                alResults.setAdapter(new LiveSearchDynamicAdapter(getContext(), arrSearchResults, 0, invokeGenericWebViewInterface, composeFooterInterface));
            }
            else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
            {
                heightAdjustableViewPager.setVisibility(View.VISIBLE);
                alResults.setVisibility(GONE);

//                if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
//                {
//                    params.height = (int)(300 * dp1);    //500px
//                    heightAdjustableViewPager.setLayoutParams(params);
//                }
//                else if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_HEADER))
//                {
//                    params.height = (int)(100 * dp1);    //500px
//                    heightAdjustableViewPager.setLayoutParams(params);
//                }

                heightAdjustableViewPager.setAdapter(new SearchResultsCarouselAdapter(getContext(), arrSearchResults , 0, invokeGenericWebViewInterface, null));
            }
            else
            {
                alResults.setLayoutManager(new LinearLayoutManager(getContext()));
                alResults.setAdapter(new LiveSearchDynamicAdapter(getContext(), arrSearchResults, 0, invokeGenericWebViewInterface, composeFooterInterface));
            }
        }

        switch (from)
        {
            case 1:
            {
                tvPageTitle.setText("FAQS");
                tvShowAll.setText("Show all Faqs");
                break;
            }
            case 2:
            {
                tvPageTitle.setText("Web");
                tvShowAll.setText("Show all WebPages");
                break;
            }
            case 3:
            {
                tvPageTitle.setText("Task");
                tvShowAll.setText("Show all Tasks");
                break;
            }
            case 4:
            {
                tvPageTitle.setText("File");
                tvShowAll.setText("Show all Files");
                break;
            }
            case 5:
            {
                tvPageTitle.setText("Data");
                tvShowAll.setText("Show all data");
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

        tvShowAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, FullResultsActivity.class);
                intent.putExtra("originalQuery", payloadInner.getTemplate().getCleanQuery());
                intent.putExtra("searchSetting", payloadInner.getFullSearchresultsViewSetting());
                intent.putExtra("from", from);
                if(sharedPreferences != null)
                    intent.putExtra("messagePayload", sharedPreferences.getString("Payload", ""));

                mContext.startActivity(intent);
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

    private ArrayList<LiveSearchResultsModel> getTopList(ArrayList<LiveSearchResultsModel> results, String type)
    {
        ArrayList<LiveSearchResultsModel> arrTempResults = new ArrayList<>();

        if(sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int maxValue = 0;

        if(results != null && results.size() > 0)
        {
            if(results.size() > sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2))
                maxValue = sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2);
            else
                maxValue = results.size();

            for (int i = 0; i < maxValue; i++)
            {
                if (type.equalsIgnoreCase(BundleConstants.FAQ))
                    results.get(i).setAppearance(faq);
                else if(type.equalsIgnoreCase(BundleConstants.WEB))
                    results.get(i).setAppearance(page);
                else if(type.equalsIgnoreCase(BundleConstants.TASK))
                {
                    if(task == null)
                        task = getResultsViewAppearance(BundleConstants.TASK);

                    results.get(i).setAppearance(task);
                }
                else if(type.equalsIgnoreCase(BundleConstants.FILE))
                    results.get(i).setAppearance(file);
                else if(type.equalsIgnoreCase(BundleConstants.DATA))
                    results.get(i).setAppearance(data);

                arrTempResults.add(results.get(i));
            }
        }

        return arrTempResults;
    }

    private ArrayList<HashMap<String, Object>> getResultsTopList(ArrayList<HashMap<String, Object>> results, String type)
    {
        ArrayList<HashMap<String, Object>> arrTempResults = new ArrayList<>();

        if(sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int maxValue = 0;

        if(results != null && results.size() > 0)
        {
            if(results.size() > sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2))
                maxValue = sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2);
            else
                maxValue = results.size();

            for (int i = 0; i < maxValue; i++)
            {
                if (type.equalsIgnoreCase(BundleConstants.FAQ))
                    results.get(i).put("appearance", faq);
                else if(type.equalsIgnoreCase(BundleConstants.WEB))
                    results.get(i).put("appearance",page);
                else if(type.equalsIgnoreCase(BundleConstants.TASK))
                {
                    if(task == null)
                        task = getResultsViewAppearance(BundleConstants.TASK);

                    results.get(i).put("appearance",task);
                }
                else if(type.equalsIgnoreCase(BundleConstants.FILE))
                    results.get(i).put("appearance",file);
                else if(type.equalsIgnoreCase(BundleConstants.DATA))
                    results.get(i).put("appearance",data);

                arrTempResults.add(results.get(i));
            }
        }

        return arrTempResults;
    }


    private void getSearchSettings(ResultsViewSetting searchSettings)
    {
        if(searchSettings != null && searchSettings.getGroupSetting() != null
                && searchSettings.getGroupSetting().getConditions() != null
                && searchSettings.getGroupSetting().getConditions().size()  > 0)
        {
            for(int i = 0; i < searchSettings.getGroupSetting().getConditions().size(); i++)
            {
                if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.FAQ))
                    faq = searchSettings.getGroupSetting().getConditions().get(i);
                else if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.WEB))
                    page = searchSettings.getGroupSetting().getConditions().get(i);
                else if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.FILE))
                    file = searchSettings.getGroupSetting().getConditions().get(i);
                else if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.TASK))
                    task = searchSettings.getGroupSetting().getConditions().get(i);
                else if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.DATA))
                    data = searchSettings.getGroupSetting().getConditions().get(i);

                if(searchSettings.getDefaultTemplate() != null)
                    defaultTemplate = searchSettings.getDefaultTemplate();

                if(faq == null)
                    faq = getDefaultApperence(BundleConstants.FAQ);

                if(page == null)
                    page = getDefaultApperence(BundleConstants.WEB);

                if(file == null)
                    file = getDefaultApperence(BundleConstants.FILE);

                if(data == null)
                    data = getDefaultApperence(BundleConstants.DATA);

                if(task == null)
                    task = getDefaultApperence(BundleConstants.TASK);
            }
        }
    }

    private ResultsViewAppearance getDefaultApperence(String type)
    {
        ResultsViewAppearance resultsViewAppearance = new ResultsViewAppearance();
        resultsViewAppearance.setOp("equals");
        resultsViewAppearance.setFieldValue(type);

        if(defaultTemplate != null)
        {
            resultsViewAppearance.setTemplateId(defaultTemplate.get_id());
            resultsViewAppearance.setTemplate(defaultTemplate);
            return resultsViewAppearance;
        }

        return null;
    }

    private ResultsViewAppearance getResultsViewAppearance(String type)
    {
        ResultsViewAppearance resultsViewAppearance = new ResultsViewAppearance();
        resultsViewAppearance.setFieldValue(type);
        resultsViewAppearance.setTemplateId("fsrt-c86f6f10-76b1-5161-88c3-b1d03b1c0de7");

        ResultsViewTemplate template = new ResultsViewTemplate();
        template.set_id("fsrt-c86f6f10-76b1-5161-88c3-b1d03b1c0de7");
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

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
            outRect.right = verticalSpaceHeight;
        }
    }
}