package com.kore.findlysdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kore.findlysdk.R;
import com.kore.findlysdk.adapters.FacetsFilterAdapter;
import com.kore.findlysdk.fragments.AllResultsFragment;
import com.kore.findlysdk.fragments.FilterOptionsSheetFragment;
import com.kore.findlysdk.listners.FilterOptionsListner;
import com.kore.findlysdk.listners.FilterResultsListner;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.BotOptionModel;
import com.kore.findlysdk.models.BotOptionsModel;
import com.kore.findlysdk.models.BotResponse;
import com.kore.findlysdk.models.BotResponseMessage;
import com.kore.findlysdk.models.ComponentModel;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.PayloadOuter;
import com.kore.findlysdk.models.ResultsViewAppearance;
import com.kore.findlysdk.models.ResultsViewModel;
import com.kore.findlysdk.models.ResultsViewSetting;
import com.kore.findlysdk.models.SearchFacetsBucketsModel;
import com.kore.findlysdk.models.SearchModel;
import com.kore.findlysdk.net.BotRestBuilder;
import com.kore.findlysdk.net.SDKConfiguration;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.utils.ToastUtils;
import com.kore.findlysdk.utils.Utility;
import com.kore.findlysdk.view.AutoExpandListView;
import com.kore.findlysdk.websocket.SocketWrapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullResultsActivity extends AppCompatActivity implements View.OnClickListener, InvokeGenericWebViewInterface, FilterResultsListner, FilterOptionsListner
{
    private ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels, arrFaqSearchResultsModels, arrPageSearchResultsModels, arrActionSearchResultsModels, arrDocumentSearchReultsModels;
    private ViewPager vpAllResults;
    private TabLayout view_pager_tab;
    private TextView tvAllResults, tvFaqResults, tvPagesResults, tvActionsResults, tvDocumentsResults,tvClearAll, tvApply;
    private AllResultsFragment allResultsFragment;
    private String originalQuery;
    private SearchModel searchModel, filterSearchModel;
    private Gson gson = new Gson();
    private ArrayList<LiveSearchResultsModel> arrTempAllResults;
    private PopupWindow popupWindow;
    private View popUpView;
    private RelativeLayout llFilters;
    private float dp1;
    private ListView alvFacetsName;
    private String messagePayload;
    private ResultsViewSetting searchSettings;
    private MyResultsAdapter myResultsAdapter;
    private ArrayList<SearchFacetsBucketsModel> arrTempFilters = new ArrayList<>();
    private FacetsFilterAdapter facetsFilterAdapter;
    private RecyclerView rlFilterOptions;
    private FloatingActionButton fabFilters;
    private FilterOptionsSheetFragment bottomSheetDialog;
    private RelativeLayout rlFilter;
    private TextView tvFilterCount, tvFilterTopCount;
    private ImageView ivAllresults, ivFaqs, ivPages, ivActions, ivDocuments;
    private LinearLayout llAllresults,llFaqs,llPages,llActions,llDocuments;
    private ArrayList<BotOptionModel> arrBotOptionModels;
    private HorizontalScrollView hsvOptionsFooter;
    private ResultsViewAppearance faq;
    private ResultsViewAppearance page;
    private ResultsViewAppearance document;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_results_findly_layout);

        vpAllResults = (ViewPager)findViewById(R.id.vpAllResults);
        view_pager_tab = (TabLayout) findViewById(R.id.view_pager_tab);
        tvAllResults = (TextView) findViewById(R.id.tvAllResults);
        tvFaqResults = (TextView) findViewById(R.id.tvFaqResults);
        tvPagesResults = (TextView) findViewById(R.id.tvPagesResults);
        tvActionsResults = (TextView) findViewById(R.id.tvActionsResults);
        tvDocumentsResults = (TextView) findViewById(R.id.tvDocumentsResults);
        rlFilterOptions = (RecyclerView) findViewById(R.id.rlFilterOptions);
        llFilters = (RelativeLayout)findViewById(R.id.llFilters);
        fabFilters = (FloatingActionButton) findViewById(R.id.fabFilters);
        rlFilter = (RelativeLayout) findViewById(R.id.rlFilter);
        tvFilterCount = (TextView) findViewById(R.id.tvFilterCount);
        tvFilterTopCount = (TextView) findViewById(R.id.tvFilterTopCount);
        ivAllresults = (ImageView) findViewById(R.id.ivAllResults);
        ivFaqs = (ImageView) findViewById(R.id.ivFaq);
        ivPages = (ImageView) findViewById(R.id.ivPages);
        ivActions = (ImageView) findViewById(R.id.ivActions);
        ivDocuments = (ImageView) findViewById(R.id.ivDocs);
        llAllresults = (LinearLayout) findViewById(R.id.llAllresults);
        llFaqs = (LinearLayout)findViewById(R.id.llFaqs);
        llPages = (LinearLayout) findViewById(R.id.llPages);
        llActions = (LinearLayout) findViewById(R.id.llActions);
        llDocuments = (LinearLayout) findViewById(R.id.llDocuments);
        hsvOptionsFooter = (HorizontalScrollView) findViewById(R.id.hsvOptionsFooter);

        dp1 = Utility.convertDpToPixel(FullResultsActivity.this, 1);

        setPageTitle(0);
        getBundleInfo();

        llAllresults.setOnClickListener(this);
        llFaqs.setOnClickListener(this);
        llPages.setOnClickListener(this);
        llActions.setOnClickListener(this);
        llDocuments.setOnClickListener(this);

        if(SDKConfiguration.BubbleColors.filterTopIcon)
        {
            llFilters.setVisibility(View.VISIBLE);
            rlFilter.setVisibility(View.GONE);
        }
        else
        {
            llFilters.setVisibility(View.GONE);
            rlFilter.setVisibility(View.VISIBLE);
        }

        popUpView = LayoutInflater.from(this).inflate(R.layout.facets_filter_layout, null);
        alvFacetsName = (ListView) popUpView.findViewById(R.id.alvFacetsName);
        tvClearAll = (TextView) popUpView.findViewById(R.id.tvClearAll);
        tvApply = (TextView) popUpView.findViewById(R.id.tvApply);
        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, (int )(450 * dp1), true);

        llFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                if(searchModel != null && searchModel.getTemplate() != null && searchModel.getTemplate().getSearchFacets() != null
//                    && searchModel.getTemplate().getSearchFacets().size() > 0)
//                {
//                    if(facetsFilterAdapter != null && arrTempFilters != null && arrTempFilters.size() > 0)
//                        facetsFilterAdapter.refresh(arrTempFilters);
//
//                    popupWindow.showAtLocation(llFilters, Gravity.TOP|Gravity.RIGHT, (int)(20 * dp1), (int)(70 * dp1));
//                }
//                else
//                    ToastUtils.showToast(FullResultsActivity.this, "Filters are not available for specified search.");
                bottomSheetDialog = new FilterOptionsSheetFragment();
                bottomSheetDialog.setFilterOptions(searchModel);
                bottomSheetDialog.setAppliedFilter(arrTempFilters);
                bottomSheetDialog.setFilterListner(FullResultsActivity.this);
                bottomSheetDialog.show(getSupportFragmentManager(), "add_tags");
            }
        });

        tvClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilterClear();
            }
        });

        tvApply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(searchModel != null && searchModel.getTemplate() != null
                        && searchModel.getTemplate().getSearchFacets() != null && searchModel.getTemplate().getSearchFacets().size() > 0
                        && arrTempFilters != null && arrTempFilters.size() > 0)
                {
                    for (int k = 0; k < searchModel.getTemplate().getSearchFacets().size(); k++)
                    {
                        String fldName = searchModel.getTemplate().getSearchFacets().get(k).getFieldName();
                        searchModel.getTemplate().getSearchFacets().get(k).setArrSearchBucket(new ArrayList<SearchFacetsBucketsModel>());

                        for(int j = 0; j < arrTempFilters.size(); j++)
                        {
                            if(fldName.equalsIgnoreCase(arrTempFilters.get(j).getFieldName()))
                            {
                                searchModel.getTemplate().getSearchFacets().get(k).getArrSearchBucket().add(arrTempFilters.get(j));
                            }
                        }
                    }

                    JsonArray jsonArray = new JsonArray();

                    for(int i = 0; i < searchModel.getTemplate().getSearchFacets().size(); i++)
                    {
                        if(searchModel.getTemplate().getSearchFacets().get(i).getArrSearchBucket().size() > 0)
                        {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("fieldName", searchModel.getTemplate().getSearchFacets().get(i).getFieldName());
                            jsonObject.addProperty("facetType", searchModel.getTemplate().getSearchFacets().get(i).getFacetType());

                            JsonArray facetsValue = new JsonArray();

                            for (int j = 0; j < searchModel.getTemplate().getSearchFacets().get(i).getArrSearchBucket().size(); j++)
                            {
                                facetsValue.add(searchModel.getTemplate().getSearchFacets().get(i).getArrSearchBucket().get(j).getKey());
                            }
                            jsonObject.add("facetValue", facetsValue);
                            jsonArray.add(jsonObject);
                        }
                    }

                    getFilterSearch(originalQuery, jsonArray, messagePayload);
                }
                else
                    onFilterClear();

                popupWindow.dismiss();
            }
        });

        fabFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new FilterOptionsSheetFragment();
                bottomSheetDialog.setFilterOptions(searchModel);
                bottomSheetDialog.setAppliedFilter(arrTempFilters);
                bottomSheetDialog.setFilterListner(FullResultsActivity.this);
                bottomSheetDialog.show(getSupportFragmentManager(), "add_tags");
            }
        });

        rlFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new FilterOptionsSheetFragment();
                bottomSheetDialog.setFilterOptions(searchModel);
                bottomSheetDialog.setAppliedFilter(arrTempFilters);
                bottomSheetDialog.setFilterListner(FullResultsActivity.this);
                bottomSheetDialog.show(getSupportFragmentManager(), "add_tags");
            }
        });
    }

    private void getSortedInfo(ArrayList<LiveSearchResultsModel> arrAllResultsModels)
    {
        arrFaqSearchResultsModels = new ArrayList<>();
        arrPageSearchResultsModels = new ArrayList<>();
        arrActionSearchResultsModels = new ArrayList<>();
        arrDocumentSearchReultsModels = new ArrayList<>();

        for (int i = 0; i < arrAllResultsModels.size(); i++)
        {
            if(arrAllResultsModels.get(i).get__contentType().equalsIgnoreCase(BundleConstants.FAQ))
            {
                arrAllResultsModels.get(i).setAppearance(faq);
                arrFaqSearchResultsModels.add(arrAllResultsModels.get(i));
            }

            if(arrAllResultsModels.get(i).get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
            {
                arrAllResultsModels.get(i).setAppearance(page);
                arrPageSearchResultsModels.add(arrAllResultsModels.get(i));
            }

            if(arrAllResultsModels.get(i).get__contentType().equalsIgnoreCase(BundleConstants.TASK))
                arrActionSearchResultsModels.add(arrAllResultsModels.get(i));

            if(arrAllResultsModels.get(i).get__contentType().equalsIgnoreCase(BundleConstants.DOCUMENT))
            {
                arrAllResultsModels.get(i).setAppearance(document);
                arrDocumentSearchReultsModels.add(arrAllResultsModels.get(i));
            }
        }

        arrLiveSearchResultsModels = new ArrayList<>();
        arrLiveSearchResultsModels.addAll(arrActionSearchResultsModels);
        arrLiveSearchResultsModels.addAll(arrPageSearchResultsModels);
        arrLiveSearchResultsModels.addAll(arrFaqSearchResultsModels);
        arrLiveSearchResultsModels.addAll(arrDocumentSearchReultsModels);
    }

    private void getBundleInfo()
    {
        originalQuery = getIntent().getExtras().getString("originalQuery");
        messagePayload = getIntent().getExtras().getString("messagePayload");
        searchSettings = (ResultsViewSetting) getIntent().getExtras().getSerializable("searchSetting");

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
                    document = searchSettings.getAppearance().get(i);
            }
        }

        if(!StringUtils.isNullOrEmpty(originalQuery))
            getSearch(originalQuery);
    }


    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.llAllresults)  {
            hsvOptionsFooter.scrollTo(0, 0);
            setPageTitle(0);
        }
        else if (view.getId() == R.id.llFaqs) {
            setPageTitle(1);
        }
        else if (view.getId() == R.id.llPages) {
            setPageTitle(2);
        }
        else if (view.getId() == R.id.llActions) {
            setPageTitle(3);
        }
        else if (view.getId() == R.id.llDocuments) {
            hsvOptionsFooter.scrollTo((int)hsvOptionsFooter.getScrollX() + (int)(53 * dp1), (int)hsvOptionsFooter.getScrollY());
            setPageTitle(4);
        }
        else {
            setPageTitle(0);
        }
    }

    @Override
    public void invokeGenericWebView(String url)
    {
        if (url != null && !StringUtils.isNullOrEmpty(url)) {
            Intent intent = new Intent(getApplicationContext(), GenericWebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("header", getResources().getString(R.string.app_name));
            startActivity(intent);
        }
    }

    @Override
    public void handleUserActions(String payload, HashMap<String, Object> type) {

    }

    @Override
    public void onFilterSelected(ArrayList<SearchFacetsBucketsModel> filters, String fieldName, String facetsType)
    {
        if(arrTempFilters != null)
        {
            for (int i = 0; i < arrTempFilters.size(); i ++)
            {
                if(arrTempFilters.get(i).getFieldName().equalsIgnoreCase(fieldName))
                {
                    arrTempFilters.remove(i);
                    i--;
                }
            }

//            for (int i = 0; i < filters.size(); i++)
//            {
//                filters.get(i).setFieldName(fieldName);
//            }

            arrTempFilters.addAll(filters);

            if(arrTempFilters.size() > 0)
            {
                tvApply.setVisibility(View.VISIBLE);
                tvClearAll.setVisibility(View.VISIBLE);

                if(bottomSheetDialog != null)
                    bottomSheetDialog.setButtonVisible(true);
            }
            else
            {
                tvClearAll.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFilterClear()
    {
        arrTempFilters = new ArrayList<>();
        getSearch(originalQuery);
        tvClearAll.setVisibility(View.GONE);
    }

    @Override
    public void onFilterApplied(ArrayList<SearchFacetsBucketsModel> arrTempFilters)
    {
        this.arrTempFilters = arrTempFilters;

        if(searchModel != null && searchModel.getTemplate() != null
                && searchModel.getTemplate().getSearchFacets() != null && searchModel.getTemplate().getSearchFacets().size() > 0
                && arrTempFilters != null && arrTempFilters.size() > 0)
        {
            for (int k = 0; k < searchModel.getTemplate().getSearchFacets().size(); k++)
            {
                String fldName = searchModel.getTemplate().getSearchFacets().get(k).getFieldName();
                searchModel.getTemplate().getSearchFacets().get(k).setArrSearchBucket(new ArrayList<SearchFacetsBucketsModel>());

                for(int j = 0; j < arrTempFilters.size(); j++)
                {
                    if(fldName.equalsIgnoreCase(arrTempFilters.get(j).getFieldName()))
                    {
                        searchModel.getTemplate().getSearchFacets().get(k).getArrSearchBucket().add(arrTempFilters.get(j));
                    }
                }
            }

            JsonArray jsonArray = new JsonArray();

            for(int i = 0; i < searchModel.getTemplate().getSearchFacets().size(); i++)
            {
                if(searchModel.getTemplate().getSearchFacets().get(i).getArrSearchBucket().size() > 0)
                {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("fieldName", searchModel.getTemplate().getSearchFacets().get(i).getFieldName());
                    jsonObject.addProperty("facetType", searchModel.getTemplate().getSearchFacets().get(i).getFacetType());

                    JsonArray facetsValue = new JsonArray();

                    for (int j = 0; j < searchModel.getTemplate().getSearchFacets().get(i).getArrSearchBucket().size(); j++)
                    {
                        facetsValue.add(searchModel.getTemplate().getSearchFacets().get(i).getArrSearchBucket().get(j).getKey());
                    }
                    jsonObject.add("facetValue", facetsValue);
                    jsonArray.add(jsonObject);
                }
            }

            getFilterSearch(originalQuery, jsonArray, messagePayload);
        }
        else
            onFilterClear();

        if(bottomSheetDialog != null)
            bottomSheetDialog.dismiss();

        if(arrTempFilters != null && arrTempFilters.size() > 0)
        {
            if(SDKConfiguration.BubbleColors.filterTopIcon)
            {
                tvFilterTopCount.setVisibility(View.VISIBLE);
                tvFilterTopCount.setText(arrTempFilters.size()+"");
            }
            else
            {
                tvFilterCount.setText(arrTempFilters.size()+"");
                tvFilterCount.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            tvFilterTopCount.setVisibility(View.INVISIBLE);
            tvFilterCount.setVisibility(View.GONE);
        }
    }

    public class MyResultsAdapter extends FragmentStatePagerAdapter
    {
        private int NUM_ITEMS = 5;
        private boolean showCount = false;

        public MyResultsAdapter(FragmentManager fragmentManager, boolean showCount)
        {
            super(fragmentManager);
            this.showCount = showCount;
        }

        public void refresh(boolean showCount)
        {
            this.showCount = showCount;
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NonNull Object object)
        {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            switch (position) {
                case 0:
                    {
                        allResultsFragment = AllResultsFragment.newInstance(arrLiveSearchResultsModels);
                        allResultsFragment.setInvokeGenericWebViewInterface(FullResultsActivity.this);
                        allResultsFragment.setFloatingActionButton(fabFilters);
                        allResultsFragment.setFilterCountView(tvFilterCount);
                        allResultsFragment.showFilterCount(showCount);
                    }
                    return allResultsFragment;
                case 1:
                    {
                        allResultsFragment = AllResultsFragment.newInstance(arrFaqSearchResultsModels);
                        allResultsFragment.setInvokeGenericWebViewInterface(FullResultsActivity.this);
                        allResultsFragment.setFloatingActionButton(fabFilters);
                        allResultsFragment.setFilterCountView(tvFilterCount);
                        allResultsFragment.showFilterCount(showCount);
                    }
                    return allResultsFragment;
                case 2:
                    {
                        allResultsFragment = AllResultsFragment.newInstance(arrPageSearchResultsModels);
                        allResultsFragment.setInvokeGenericWebViewInterface(FullResultsActivity.this);
                        allResultsFragment.setFloatingActionButton(fabFilters);
                        allResultsFragment.setFilterCountView(tvFilterCount);
                        allResultsFragment.showFilterCount(showCount);
                    }
                    return allResultsFragment;
                case 3:
                    {
                        allResultsFragment = AllResultsFragment.newInstance(arrActionSearchResultsModels);
                        allResultsFragment.setInvokeGenericWebViewInterface(FullResultsActivity.this);
                        allResultsFragment.setFloatingActionButton(fabFilters);
                        allResultsFragment.setFilterCountView(tvFilterCount);
                        allResultsFragment.showFilterCount(showCount);
                    }
                    return allResultsFragment;
                case 4:
                    {
                        allResultsFragment = AllResultsFragment.newInstance(arrDocumentSearchReultsModels);
                        allResultsFragment.setInvokeGenericWebViewInterface(FullResultsActivity.this);
                        allResultsFragment.setFloatingActionButton(fabFilters);
                        allResultsFragment.setFilterCountView(tvFilterCount);
                        allResultsFragment.showFilterCount(showCount);
                    }
                    return allResultsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All Results (" + arrLiveSearchResultsModels.size()+")";
                case 1:
                    return "FAQ's (" + arrFaqSearchResultsModels.size()+")";
                case 2:
                    return "Pages (" + arrPageSearchResultsModels.size()+")";
                case 3:
                    return "Actions (" + arrLiveSearchResultsModels.size()+")";
                case 4:
                    return "Documents (" + arrDocumentSearchReultsModels.size()+")";
                default:
                    return "Pages (" + arrLiveSearchResultsModels.size()+")";
            }
        }
    }

    private void setPageTitle(int position)
    {
        switch (position)
        {
            case 0:
//                tvAllResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvAllResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivAllresults.setImageResource(R.mipmap.allresults_select);

//                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

//                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

//                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.pages_unselect);

//                tvDocumentsResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);

                vpAllResults.setCurrentItem(0);
                break;
            case 1:
//                tvAllResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

//                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvFaqResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivFaqs.setImageResource(R.mipmap.faq_select);

//                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

//                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.actions_unselect);

//                tvDocumentsResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);
                vpAllResults.setCurrentItem(1);
                break;
            case 2:
//                tvAllResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

//                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

//                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvPagesResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivPages.setImageResource(R.mipmap.pages_select);

//                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.actions_unselect);

//                tvDocumentsResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);

                vpAllResults.setCurrentItem(2);
                break;
            case 3:
//                tvAllResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

//                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

//                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

//                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvActionsResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivActions.setImageResource(R.mipmap.actions_select);

//                tvDocumentsResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);

                vpAllResults.setCurrentItem(3);
                break;

            case 4:
//                tvAllResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

//                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

//                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

//                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.tabs_bg));
                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.actions_unselect);

//                tvDocumentsResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvDocumentsResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivDocuments.setImageResource(R.mipmap.docs_select);

                vpAllResults.setCurrentItem(4);
                break;
        }
    }

    public void getSearch(String query)
    {
        JsonObject jsonObject = getJsonBody(query, true, messagePayload);
        Call<SearchModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getSearch(SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(FullResultsActivity.this).getAccessToken(), jsonObject);
        getJWTTokenService.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response)
            {
                if (response.isSuccessful())
                {
                    searchModel = response.body();

                    if(searchModel != null && searchModel.getTemplate() != null)
                    {
                        if(searchModel.getTemplate().getWebhookPayload() != null)
                        {
                            if(searchModel.getTemplate().getWebhookPayload().getText() instanceof String)
                            {
                                try
                                {
                                    Type carouselType = new TypeToken<PayloadOuter>() {
                                    }.getType();
                                    PayloadOuter carouselElements = gson.fromJson((String) searchModel.getTemplate().getWebhookPayload().getText(), carouselType);

                                    ComponentModel componentModel = new ComponentModel();
                                    componentModel.setType("template");
                                    componentModel.setPayload(carouselElements);

                                    BotResponseMessage botResponseMessage = new BotResponseMessage();
                                    botResponseMessage.setType("template");
                                    botResponseMessage.setComponent(componentModel);

                                    ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                                    arrBotResponseMessages.add(botResponseMessage);

                                    BotResponse botResponse = new BotResponse();
                                    botResponse.setType("template");
                                    botResponse.setMessage(arrBotResponseMessages);

//                                    processPayload("", botResponse);
                                }
                                catch (JsonSyntaxException e)
                                {
                                }
                            }
                            else
                            {
                                try
                                {
                                    ComponentModel componentModel = new ComponentModel();
                                    componentModel.setType("template");

                                    Type carouselType = new TypeToken<PayloadOuter>() {
                                    }.getType();
                                    PayloadOuter carouselElements = gson.fromJson(((ArrayList<String>)(searchModel.getTemplate().getWebhookPayload().getText())).get(0), carouselType);

                                    componentModel.setPayload(carouselElements);

                                    BotResponseMessage botResponseMessage = new BotResponseMessage();
                                    botResponseMessage.setType("template");
                                    botResponseMessage.setComponent(componentModel);

                                    ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                                    arrBotResponseMessages.add(botResponseMessage);

                                    BotResponse botResponse = new BotResponse();
                                    botResponse.setType("template");
                                    botResponse.setMessage(arrBotResponseMessages);

//                                    processPayload("", botResponse);
                                }
                                catch (JsonSyntaxException e)
                                {
                                }
                            }
                        }
                        else
                        {
                            if(searchModel.getTemplate().getResults() != null)
                            {
                                arrTempAllResults = new ArrayList<>();

                                if(searchModel.getTemplate().getResults().getFaq() != null &&
                                        searchModel.getTemplate().getResults().getFaq().size() > 0)
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getFaq());

                                if(searchModel.getTemplate().getResults().getPage() != null &&
                                        searchModel.getTemplate().getResults().getPage().size() > 0)
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getPage());

                                if(searchModel.getTemplate().getResults().getTask() != null &&
                                        searchModel.getTemplate().getResults().getTask().size() > 0)
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getTask());

                                if(searchModel.getTemplate().getResults().getDocument() != null &&
                                        searchModel.getTemplate().getResults().getDocument().size() > 0)
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getDocument());

                                getSortedInfo(arrTempAllResults);

                                if(myResultsAdapter == null)
                                {
                                    vpAllResults.setAdapter(myResultsAdapter = new MyResultsAdapter(getSupportFragmentManager(), false));
                                    view_pager_tab.setupWithViewPager(vpAllResults);
                                }
                                else
                                    myResultsAdapter.refresh(false);

                                tvAllResults.setText("All Results ("+ arrLiveSearchResultsModels.size()+")");
                                tvFaqResults.setText("FAQ's ("+ arrFaqSearchResultsModels.size()+")");
                                tvPagesResults.setText("Pages ("+ arrPageSearchResultsModels.size()+")");
                                tvActionsResults.setText("Actions ("+ arrActionSearchResultsModels.size()+")");
                                tvDocumentsResults.setText("Documents ("+ arrDocumentSearchReultsModels.size()+")");

                                if(searchModel.getTemplate().getSearchFacets() != null &&
                                    searchModel.getTemplate().getSearchFacets().size() > 0)
                                {
                                    alvFacetsName.setAdapter(facetsFilterAdapter = new FacetsFilterAdapter(FullResultsActivity.this, searchModel.getTemplate().getSearchFacets(), FullResultsActivity.this, arrTempFilters));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Log.e("Search data", t.toString());
            }
        });
    }

    public void getFilterSearch(String query, JsonArray filters, String messagePayload)
    {
        JsonObject jsonObject = getJsonFilterBody(query, filters, messagePayload);
        Call<SearchModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getSearch(SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(FullResultsActivity.this).getAccessToken(),  jsonObject);
        getJWTTokenService.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response)
            {
                if (response.isSuccessful())
                {
                    filterSearchModel = response.body();

                    if(filterSearchModel != null && filterSearchModel.getTemplate() != null)
                    {
                        if(filterSearchModel.getTemplate().getResults() != null)
                        {
                            arrTempAllResults = new ArrayList<>();

                            if(filterSearchModel.getTemplate().getResults().getFaq() != null &&
                                    filterSearchModel.getTemplate().getResults().getFaq().size() > 0)
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getFaq());

                            if(filterSearchModel.getTemplate().getResults().getPage() != null &&
                                    filterSearchModel.getTemplate().getResults().getPage().size() > 0)
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getPage());

                            if(filterSearchModel.getTemplate().getResults().getTask() != null &&
                                    filterSearchModel.getTemplate().getResults().getTask().size() > 0)
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getTask());

                            if(filterSearchModel.getTemplate().getResults().getDocument() != null &&
                                    filterSearchModel.getTemplate().getResults().getDocument().size() > 0)
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getDocument());

                            getSortedInfo(arrTempAllResults);

                            if(myResultsAdapter == null)
                            {
                                vpAllResults.setAdapter(myResultsAdapter = new MyResultsAdapter(getSupportFragmentManager(), true));
                                view_pager_tab.setupWithViewPager(vpAllResults);
                            }
                            else
                                myResultsAdapter.refresh(true);

                            tvAllResults.setText("All Results ("+ arrLiveSearchResultsModels.size()+")");
                            tvFaqResults.setText("FAQ's ("+ arrFaqSearchResultsModels.size()+")");
                            tvPagesResults.setText("Pages ("+ arrPageSearchResultsModels.size()+")");
                            tvActionsResults.setText("Actions ("+ arrActionSearchResultsModels.size()+")");
                            tvDocumentsResults.setText("Documents ("+ arrDocumentSearchReultsModels.size()+")");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Log.e("Search data", t.toString());
            }
        });
    }

    private JsonObject getJsonBody(String query, boolean smallTalk, String messagePayload)
    {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

//        jsonObject.add("filters", jsonArray);
        jsonObject.addProperty("query", query.toLowerCase());
        jsonObject.addProperty("maxNumOfResults", 16);
        jsonObject.addProperty("userId", SocketWrapper.getInstance(FullResultsActivity.this).getBotUserId());
        jsonObject.addProperty("streamId", SDKConfiguration.Client.bot_id);
        jsonObject.addProperty("lang", "en");
        jsonObject.addProperty("isDev", false);
        jsonObject.addProperty("pageNumber", 0);
        jsonObject.addProperty("messagePayload", messagePayload);


//        if(smallTalk)
//            jsonObject.addProperty("smallTalk", true);

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    private JsonObject getJsonFilterBody(String query, JsonArray filters, String messagePayload)
    {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        jsonObject.add("filters", filters);
        jsonObject.addProperty("query", query.toLowerCase());
        jsonObject.addProperty("maxNumOfResults", 16);
        jsonObject.addProperty("userId", SocketWrapper.getInstance(FullResultsActivity.this).getBotUserId());
        jsonObject.addProperty("streamId", SDKConfiguration.Client.bot_id);
        jsonObject.addProperty("lang", "en");
        jsonObject.addProperty("isDev", false);
        jsonObject.addProperty("pageNumber", 0);
        jsonObject.add("messagePayload", new JsonParser().parse(messagePayload).getAsJsonObject());

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {
        // ...

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                   View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                    dyUnconsumed, type);

            if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
                child.hide();
            } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
                child.show();
            }
        }

        // ...
    }
}
