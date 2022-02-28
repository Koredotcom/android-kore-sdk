package com.kore.findlysdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.kore.findlysdk.adapters.LiveSearchAdaper;
import com.kore.findlysdk.adapters.LiveSearchCyclerAdapter;
import com.kore.findlysdk.adapters.LiveSearchDynamicAdapter;
import com.kore.findlysdk.adapters.LiveSearchListAdapter;
import com.kore.findlysdk.adapters.SearchAssistCarouselAdapter;
import com.kore.findlysdk.adapters.SearchResultsCarouselAdapter;
import com.kore.findlysdk.adapters.TabFacetsAdapter;
import com.kore.findlysdk.fragments.AllResultsFragment;
import com.kore.findlysdk.fragments.FilterOptionsSheetFragment;
import com.kore.findlysdk.fragments.FindlyFragment;
import com.kore.findlysdk.listners.BotSocketConnectionManager;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.FilterOptionsListner;
import com.kore.findlysdk.listners.FilterResultsListner;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.listners.TabFacetClickListner;
import com.kore.findlysdk.models.BotButtonModel;
import com.kore.findlysdk.models.BotOptionModel;
import com.kore.findlysdk.models.BotOptionsModel;
import com.kore.findlysdk.models.BotResponse;
import com.kore.findlysdk.models.BotResponseMessage;
import com.kore.findlysdk.models.CalEventsTemplateModel;
import com.kore.findlysdk.models.ComponentModel;
import com.kore.findlysdk.models.FormActionTemplate;
import com.kore.findlysdk.models.KnowledgeCollectionModel;
import com.kore.findlysdk.models.LiveSearchResultsDataModel;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.PayloadOuter;
import com.kore.findlysdk.models.ResultsViewAppearance;
import com.kore.findlysdk.models.ResultsViewMapping;
import com.kore.findlysdk.models.ResultsViewModel;
import com.kore.findlysdk.models.ResultsViewSetting;
import com.kore.findlysdk.models.ResultsViewTemplate;
import com.kore.findlysdk.models.ResultsViewlayout;
import com.kore.findlysdk.models.SearchFacetsBucketsModel;
import com.kore.findlysdk.models.SearchModel;
import com.kore.findlysdk.models.TabFacetsModel;
import com.kore.findlysdk.net.BotRestBuilder;
import com.kore.findlysdk.net.SDKConfiguration;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.utils.ToastUtils;
import com.kore.findlysdk.utils.Utility;
import com.kore.findlysdk.view.AutoExpandListView;
import com.kore.findlysdk.view.HeightAdjustableViewPager;
import com.kore.findlysdk.websocket.SocketWrapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FullResultsActivity extends AppCompatActivity implements View.OnClickListener, InvokeGenericWebViewInterface, FilterResultsListner, FilterOptionsListner, ComposeFooterInterface, TabFacetClickListner
{
    private ViewPager vpAllResults;
    private TabLayout view_pager_tab;
    private TextView tvAllResults, tvFaqResults, tvPagesResults, tvActionsResults, tvDocumentsResults,tvClearAll, tvApply;
    private AllResultsFragment allResultsFragment;
    private String originalQuery, requestId;
    private SearchModel searchModel, filterSearchModel;
    private Gson gson = new Gson();
    private PopupWindow popupWindow;
    private View popUpView;
    private RelativeLayout llFilters;
    private float dp1;
    private ListView alvFacetsName;
    private String messagePayload;
    private ResultsViewSetting searchSettings;
    private ArrayList<SearchFacetsBucketsModel> arrTempFilters = new ArrayList<>();
    private FacetsFilterAdapter facetsFilterAdapter;
    private FloatingActionButton fabFilters;
    private FilterOptionsSheetFragment bottomSheetDialog;
    private RelativeLayout rlFilter, rlData, rlNoData;
    private TextView tvFilterCount, tvFilterTopCount, tvNoofPages, tvPageNumber, tvDataResults;
    private ImageView ivAllresults, ivFaqs, ivPages, ivActions, ivDocuments, ivData, ivPreviousFirst, ivPrevious, ivNext, ivNextLast;
    private LinearLayout llAllresults,llFaqs,llPages,llActions,llDocuments, llStructuredData, llAllresultsLayout, llPagination;
    private ArrayList<BotOptionModel> arrBotOptionModels;
    private HorizontalScrollView hsvOptionsFooter;
    private ResultsViewAppearance faq;
    private ResultsViewAppearance page;
    private ResultsViewAppearance document;
    private ResultsViewAppearance task;
    private ResultsViewAppearance data;
    private ResultsViewTemplate defaultTemplate;
    private RecyclerView lvLiveSearch, rvTabs;
    private int currentTab, currentPage = 0;
    private JsonArray jsonTotalArray = new JsonArray();
    private int faqPages, webPages, filePages, dataPages, taskPages;
    private ScrollView svResults;
    private int from = 0;
    private SharedPreferences sharedPreferences;
    private TabFacetsModel tabFacetsModel;
    private int allCount;
    private boolean isFilterDataAvailable;
    private String tabFacetSelected = "";
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
        tvDataResults = (TextView) findViewById(R.id.tvDataResults);
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
        ivData = (ImageView) findViewById(R.id.ivData);
        llAllresults = (LinearLayout) findViewById(R.id.llAllresults);
        llFaqs = (LinearLayout)findViewById(R.id.llFaqs);
        llPages = (LinearLayout) findViewById(R.id.llPages);
        llActions = (LinearLayout) findViewById(R.id.llActions);
        llDocuments = (LinearLayout) findViewById(R.id.llDocuments);
        llStructuredData = (LinearLayout) findViewById(R.id.llStructuredData);
        hsvOptionsFooter = (HorizontalScrollView) findViewById(R.id.hsvOptionsFooter);
        lvLiveSearch = (RecyclerView) findViewById(R.id.lvLiveSearch);
        rlData = (RelativeLayout) findViewById(R.id.rlData);
        rlNoData = (RelativeLayout) findViewById(R.id.rlNoData);
        llAllresultsLayout = (LinearLayout) findViewById(R.id.llAllresultsLayout);
        llPagination = (LinearLayout) findViewById(R.id.llPagination);
        tvNoofPages = (TextView) findViewById(R.id.tvNoofPages);
        ivPreviousFirst = (ImageView) findViewById(R.id.ivPreviousFirst);
        ivPrevious = (ImageView) findViewById(R.id.ivPrevious);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivNextLast = (ImageView) findViewById(R.id.ivNextLast);
        svResults = (ScrollView) findViewById(R.id.svResults);
        tvPageNumber = (TextView) findViewById(R.id.tvPageNumber);
        rvTabs = (RecyclerView) findViewById(R.id.rvTabs);
        rvTabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        lvLiveSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FullResultsActivity.this);
        dp1 = Utility.convertDpToPixel(FullResultsActivity.this, 1);

        String json = sharedPreferences.getString(BundleConstants.TAB_FACETS, "");
        if(!StringUtils.isNullOrEmpty(json))
        {
            tabFacetsModel = gson.fromJson(json, TabFacetsModel.class);
        }

//        setPageTitle(0);
        getBundleInfo();

        llAllresults.setOnClickListener(this);
        llFaqs.setOnClickListener(this);
        llPages.setOnClickListener(this);
        llActions.setOnClickListener(this);
        llDocuments.setOnClickListener(this);
        llStructuredData.setOnClickListener(this);

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
//                if(searchModel != null && searchModel.getTemplate() != null && searchModel.getTemplate().getFacets() != null
//                    && searchModel.getTemplate().getFacets().size() > 0)
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
                        && searchModel.getTemplate().getFacets() != null && searchModel.getTemplate().getFacets().size() > 0
                        && arrTempFilters != null && arrTempFilters.size() > 0)
                {
                    getFilterSearch(originalQuery, getFiltersArray(arrTempFilters), messagePayload, getTabFiltersObject(tabFacetSelected), 0);
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

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                switch (currentTab)
                {
                    case 1:
                        {
                            switch ((String) tabFacetSelected)
                            {
                                case BundleConstants.FAQ:
                                {
                                    if(currentPage+1 < faqPages)
                                    {
                                        currentPage++;
                                        getFilterSearch(originalQuery, jsonTotalArray, messagePayload, getTabFiltersObject(BundleConstants.FAQ), 1);
                                    }
                                }
                                break;
                                case BundleConstants.WEB:
                                {
                                    if(currentPage+1 < webPages)
                                    {
                                        currentPage++;
                                        getFilterSearch(originalQuery, jsonTotalArray, messagePayload, getTabFiltersObject(BundleConstants.WEB), 1);
                                    }
                                }
                                break;
                                case BundleConstants.TASK:
                                {
                                    if(currentPage+1 < taskPages)
                                    {
                                        currentPage++;
                                        getFilterSearch(originalQuery, jsonTotalArray, messagePayload, getTabFiltersObject(BundleConstants.TASK), 1);
                                    }
                                }
                                break;
                                case BundleConstants.FILE:
                                {
                                    if(currentPage+1 < filePages)
                                    {
                                        currentPage++;
                                        getFilterSearch(originalQuery, jsonTotalArray, messagePayload, getTabFiltersObject(BundleConstants.FILE), 1);
                                    }
                                }
                                break;
                                case BundleConstants.DATA:
                                {
                                    if(currentPage+1 < dataPages)
                                    {
                                        currentPage++;
                                        getFilterSearch(originalQuery, jsonTotalArray, messagePayload, getTabFiltersObject(BundleConstants.DATA), 1);
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    default:
                        currentPage = 0;
                        break;
                }
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(currentPage > 0)
                {
                    currentPage--;
                    getFilterSearch(originalQuery, jsonTotalArray, messagePayload, getTabFiltersObject(tabFacetSelected), 1);
//                    setPageTitle(currentTab);
                }
            }
        });

        ivPreviousFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(currentPage != 0)
                {
                    currentPage = 0;
                    setPageTitle(currentTab);
                }
            }
        });

        ivNextLast.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (currentTab)
                {
                    case 1:
                        currentPage = faqPages - 1;
                    break;
                    case 2:
                        currentPage = webPages - 1;
                    break;
                    case 3:
                        currentPage = taskPages - 1;
                    break;
                    case 4:
                        currentPage = filePages - 1;
                        break;
                    case 5:
                        currentPage = dataPages - 1;
                    break;
                    default:
                        currentPage = 0;
                }

                setPageTitle(currentTab);
            }
        });
    }

    private void getSortedInfo(ArrayList<LiveSearchResultsModel> arrAllResultsModels)
    {
        for (int i = 0; i < arrAllResultsModels.size(); i++)
        {
            if(arrAllResultsModels.get(i).getSys_content_type() != null)
            {
                switch (arrAllResultsModels.get(i).getSys_content_type())
                {
                    case BundleConstants.FAQ:
                        arrAllResultsModels.get(i).setAppearance(faq);
                    break;
                    case BundleConstants.WEB:
                        arrAllResultsModels.get(i).setAppearance(page);
                    break;
                    case BundleConstants.TASK:
                        arrAllResultsModels.get(i).setAppearance(task);
                    break;
                    case BundleConstants.FILE:
                        arrAllResultsModels.get(i).setAppearance(document);
                    break;
                    case BundleConstants.DATA:
                        arrAllResultsModels.get(i).setAppearance(data);
                    break;
                    default:
                        break;

                }
            }
        }
    }

    private void getDataSortedInfo(ArrayList<HashMap<String, Object>> arrAllResultsModels)
    {
        for (int i = 0; i < arrAllResultsModels.size(); i++)
        {
            if(arrAllResultsModels.get(i).get(BundleConstants.SYS_CONTENT_TYPE) != null)
            {
                switch ((String) arrAllResultsModels.get(i).get(BundleConstants.SYS_CONTENT_TYPE))
                {
                    case BundleConstants.FAQ:
                        arrAllResultsModels.get(i).put(BundleConstants.APPEARANCE, faq);
                        break;
                    case BundleConstants.WEB:
                        arrAllResultsModels.get(i).put(BundleConstants.APPEARANCE, page);
                        break;
                    case BundleConstants.TASK:
                        arrAllResultsModels.get(i).put(BundleConstants.APPEARANCE, task);
                    break;
                    case BundleConstants.FILE:
                        arrAllResultsModels.get(i).put(BundleConstants.APPEARANCE, document);
                        break;
                    case BundleConstants.DATA:
                        arrAllResultsModels.get(i).put(BundleConstants.APPEARANCE, data);
                        break;
                    default:
                        break;

                }
            }
        }
    }

    private void getBundleInfo()
    {
        originalQuery = getIntent().getExtras().getString("originalQuery");
        messagePayload = getIntent().getExtras().getString("messagePayload");
        searchSettings = (ResultsViewSetting) getIntent().getExtras().getSerializable("searchSetting");
        requestId = getIntent().getExtras().getString("searchRequestId");
        from = getIntent().getExtras().getInt("from");

        if(searchSettings != null && searchSettings.getGroupSetting() != null
            && searchSettings.getGroupSetting().getConditions() != null
            && searchSettings.getGroupSetting().getConditions().size() > 0)
        {
            for(int i = 0; i < searchSettings.getGroupSetting().getConditions().size(); i++)
            {
                if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.FAQ))
                    faq = searchSettings.getGroupSetting().getConditions().get(i);
                else if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.WEB))
                    page = searchSettings.getGroupSetting().getConditions().get(i);
                else if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.FILE))
                    document = searchSettings.getGroupSetting().getConditions().get(i);
                else if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.TASK))
                    task = searchSettings.getGroupSetting().getConditions().get(i);
                else if(searchSettings.getGroupSetting().getConditions().get(i).getFieldValue().equalsIgnoreCase(BundleConstants.DATA))
                    data = searchSettings.getGroupSetting().getConditions().get(i);
            }

            if(searchSettings.getDefaultTemplate() != null)
                defaultTemplate = searchSettings.getDefaultTemplate();

            if(faq == null)
                faq = getDefaultApperence(BundleConstants.FAQ);

            if(page == null)
                page = getDefaultApperence(BundleConstants.WEB);

            if(document == null)
                document = getDefaultApperence(BundleConstants.FILE);

            if(data == null)
                data = getDefaultApperence(BundleConstants.DATA);

            if(task == null)
                task = getDefaultApperence(BundleConstants.TASK);
        }

        currentPage = 0;
        tvPageNumber.setText("1");

        switch (from)
        {
            case 1:
                setPageTitle(1);
            break;
            case 2:
                setPageTitle(2);
            break;
            case 3:
                setPageTitle(3);
            break;
            case 4:
                setPageTitle(4);
            break;
            case 5:
                setPageTitle(5);
            break;
            default:
            {
                hsvOptionsFooter.scrollTo(0, 0);
                setPageTitle(0);
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


    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.llAllresults)  {
            hsvOptionsFooter.scrollTo(0, 0);
            setPageTitle(0);
        }
        else if (view.getId() == R.id.llFaqs) {
            currentPage = 0;
            tvPageNumber.setText("1");
            setPageTitle(1);
        }
        else if (view.getId() == R.id.llPages) {
            currentPage = 0;
            tvPageNumber.setText("1");
            setPageTitle(2);
        }
        else if (view.getId() == R.id.llActions) {
            currentPage = 0;
            tvPageNumber.setText("1");
            setPageTitle(3);
        }
        else if (view.getId() == R.id.llDocuments) {
            currentPage = 0;
            tvPageNumber.setText("1");
            hsvOptionsFooter.scrollTo((int)hsvOptionsFooter.getScrollX() + (int)(53 * dp1), (int)hsvOptionsFooter.getScrollY());
            setPageTitle(4);
        }
        else if (view.getId() == R.id.llStructuredData) {
            currentPage = 0;
            tvPageNumber.setText("1");
            hsvOptionsFooter.scrollTo((int)hsvOptionsFooter.getScrollX() + (int)(53 * dp1), (int)hsvOptionsFooter.getScrollY());
            setPageTitle(5);
        }
        else
            {
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
        jsonTotalArray = new JsonArray();
        currentPage = 0;
        tvPageNumber.setText("1");
        tvClearAll.setVisibility(View.GONE);

        switch (currentTab)
        {
            case 0:
                if(!StringUtils.isNullOrEmpty(originalQuery))
                    getSearch(originalQuery);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFilterApplied(ArrayList<SearchFacetsBucketsModel> arrTempFilters)
    {
        this.arrTempFilters = arrTempFilters;

        if(searchModel != null && searchModel.getTemplate() != null
                && searchModel.getTemplate().getFacets() != null && searchModel.getTemplate().getFacets().size() > 0
                && arrTempFilters != null && arrTempFilters.size() > 0)
        {
            for (int k = 0; k < searchModel.getTemplate().getFacets().size(); k++)
            {
                String fldName = searchModel.getTemplate().getFacets().get(k).getFieldName();
                searchModel.getTemplate().getFacets().get(k).setArrSearchBucket(new ArrayList<SearchFacetsBucketsModel>());

                for(int j = 0; j < arrTempFilters.size(); j++)
                {
                    if(fldName.equalsIgnoreCase(arrTempFilters.get(j).getFieldName()))
                    {
                        searchModel.getTemplate().getFacets().get(k).getArrSearchBucket().add(arrTempFilters.get(j));
                    }
                }
            }

            JsonArray jsonArray = new JsonArray();

            for(int i = 0; i < searchModel.getTemplate().getFacets().size(); i++)
            {
                if(searchModel.getTemplate().getFacets().get(i).getArrSearchBucket().size() > 0)
                {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("fieldName", searchModel.getTemplate().getFacets().get(i).getFieldName());
                    jsonObject.addProperty("subtype", searchModel.getTemplate().getFacets().get(i).getSubtype());
                    jsonObject.addProperty("name", searchModel.getTemplate().getFacets().get(i).getName());
                    JsonArray facetsValue = new JsonArray();

                    for (int j = 0; j < searchModel.getTemplate().getFacets().get(i).getArrSearchBucket().size(); j++)
                    {
                        facetsValue.add(searchModel.getTemplate().getFacets().get(i).getArrSearchBucket().get(j).getKey());
                    }
                    jsonObject.add("facetValue", facetsValue);
                    jsonArray.add(jsonObject);
                }
            }

            jsonTotalArray.addAll(jsonArray);

            currentPage = 0;
            tvPageNumber.setText("1");
            getFilterSearch(originalQuery, jsonArray, messagePayload, getTabFiltersObject(tabFacetSelected), 0);
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

    @Override
    public void onSendClick(String message, boolean isFromUtterance) {

    }

    @Override
    public void onSendClick(String message, String payload, boolean isFromUtterance) {
        BotSocketConnectionManager.getInstance().sendPayload(message, payload);
        finish();
    }

    @Override
    public void onFormActionButtonClicked(FormActionTemplate fTemplate) {

    }

    @Override
    public void launchActivityWithBundle(String type, Bundle payload) {

    }

    @Override
    public void sendWithSomeDelay(String message, String payload, long time, boolean isScrollUpNeeded) {

    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {

    }

    @Override
    public void showMentionNarratorContainer(boolean show, String natxt, String cotext, String handFocus, boolean isEnd, boolean showOverlay, String templateType) {

    }

    @Override
    public void openFullView(String templateType, String data, CalEventsTemplateModel.Duration duration, int position) {

    }

    @Override
    public void updateActionbar(boolean selected, String templateType, ArrayList<BotButtonModel> buttonModels) {

    }

    @Override
    public void lauchMeetingNotesAction(Context context, String mid, String eid) {

    }

    @Override
    public void showAfterOnboard(boolean isDiscardClicked) {

    }

    @Override
    public void onPanelClicked(Object pModel, boolean isFirstLaunch) {

    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }

    private void setPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                tvAllResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivAllresults.setImageResource(R.mipmap.allresults_select);

                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.actions_unselect);

                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);

                tvDataResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivData.setImageResource(R.mipmap.pages_unselect);

                llPagination.setVisibility(GONE);
                currentTab = 0;

                if(jsonTotalArray != null && jsonTotalArray.size() > 0)
                {
                    getFilterSearch(originalQuery, jsonTotalArray, messagePayload, null, 0);
                }
                else
                {
                    if(!StringUtils.isNullOrEmpty(originalQuery))
                        getSearch(originalQuery);
                }

                break;
            default:
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

                currentTab = 1;
                lvLiveSearch.setVisibility(View.GONE);
                llPagination.setVisibility(GONE);

                if(tabFacetSelected.equalsIgnoreCase(BundleConstants.FAQ)) {
                    if(faqPages > 1)
                    {
                        llPagination.setVisibility(View.VISIBLE);
                        tvNoofPages.setText(""+faqPages);
                        tvPageNumber.setText((currentPage+1)+"");
                    }
                }
                else if(tabFacetSelected.equalsIgnoreCase(BundleConstants.WEB)) {
                    if(webPages > 1)
                    {
                        llPagination.setVisibility(View.VISIBLE);
                        tvNoofPages.setText(""+webPages);
                        tvPageNumber.setText((currentPage+1)+"");
                    }
                }
                else if(tabFacetSelected.equalsIgnoreCase(BundleConstants.TASK)) {
                    if(taskPages > 1)
                    {
                        llPagination.setVisibility(View.VISIBLE);
                        tvNoofPages.setText(""+taskPages);
                        tvPageNumber.setText((currentPage+1)+"");
                    }
                }
                else if(tabFacetSelected.equalsIgnoreCase(BundleConstants.FILE)) {
                    if(filePages > 1)
                    {
                        llPagination.setVisibility(View.VISIBLE);
                        tvNoofPages.setText(""+filePages);
                        tvPageNumber.setText((currentPage+1)+"");
                    }
                }
                else if(tabFacetSelected.equalsIgnoreCase(BundleConstants.DATA)) {
                    if(dataPages > 1)
                    {
                        llPagination.setVisibility(View.VISIBLE);
                        tvNoofPages.setText(""+dataPages);
                        tvPageNumber.setText((currentPage+1)+"");
                    }
                }
            break;
        }
    }

    public void getSearch(String query)
    {
        JsonObject jsonObject = getJsonBody(query, true, messagePayload, requestId);
        Call<SearchModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getSearch(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(FullResultsActivity.this).getAccessToken(), jsonObject, SocketWrapper.getInstance(FullResultsActivity.this).getJWTToken());
        getJWTTokenService.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response)
            {
                if (response.isSuccessful())
                {
                    searchModel = response.body();

                    if(searchModel != null && searchModel.getTemplate() != null)
                    {
                        if(searchModel.getTemplate().getResults() != null)
                        {
                            llAllresultsLayout.removeAllViews();
                            rlData.setVisibility(View.VISIBLE);
                            rlNoData.setVisibility(View.GONE);

                            if(searchModel.getTemplate().getResults().getFaq() != null &&
                                searchModel.getTemplate().getResults().getFaq().getData() != null &&
                                searchModel.getTemplate().getResults().getFaq().getData().size() > 0)
                            {
                                getSortedInfo(searchModel.getTemplate().getResults().getFaq().getData());
                                addLayoutView(searchModel.getTemplate().getResults().getFaq(), faq,1, BundleConstants.FAQ);
                            }

                            if(searchModel.getTemplate().getResults().getWeb() != null &&
                                searchModel.getTemplate().getResults().getWeb().getData() != null &&
                                searchModel.getTemplate().getResults().getWeb().getData().size() > 0)
                            {
                                getSortedInfo(searchModel.getTemplate().getResults().getWeb().getData());
                                addLayoutView(searchModel.getTemplate().getResults().getWeb(), page,2, BundleConstants.WEB);
                            }

                            if(searchModel.getTemplate().getResults().getTask() != null &&
                                searchModel.getTemplate().getResults().getTask().getData() != null &&
                                searchModel.getTemplate().getResults().getTask().getData().size() > 0)
                            {
                                getSortedInfo(searchModel.getTemplate().getResults().getTask().getData());
                                addLayoutView(searchModel.getTemplate().getResults().getTask(), task,3, BundleConstants.TASK);
                            }

                            if(searchModel.getTemplate().getResults().getFile() != null &&
                                searchModel.getTemplate().getResults().getFile().getData() != null &&
                                searchModel.getTemplate().getResults().getFile().getData().size() > 0)
                            {
                                getSortedInfo(searchModel.getTemplate().getResults().getFile().getData());
                                addLayoutView(searchModel.getTemplate().getResults().getFile(), document,4, BundleConstants.FILE);
                            }

                            if(searchModel.getTemplate().getResults().getDefault_group() != null &&
                                searchModel.getTemplate().getResults().getDefault_group().getData() != null &&
                                searchModel.getTemplate().getResults().getDefault_group().getData().size() > 0)
                            {
                                getSortedInfo(searchModel.getTemplate().getResults().getDefault_group().getData());
                                addLayoutView(searchModel.getTemplate().getResults().getDefault_group(), data,5, BundleConstants.DATA);
                            }

                            if(searchModel.getTemplate().getTabFacet() != null &&
                                searchModel.getTemplate().getTabFacet().getBuckets() != null &&
                                searchModel.getTemplate().getTabFacet().getBuckets().size() > 0)
                            {

                                for (int i = 0; i < tabFacetsModel.getTabs().size(); i++)
                                {
                                    for (int j = 0; j < searchModel.getTemplate().getTabFacet().getBuckets().size(); j++)
                                    {
                                        if(searchModel.getTemplate().getTabFacet().getBuckets().get(j).getKey().equalsIgnoreCase(tabFacetsModel.getTabs().get(i).getFieldValue()))
                                        {
                                            allCount += searchModel.getTemplate().getTabFacet().getBuckets().get(j).getDoc_count();
                                            tabFacetsModel.getTabs().get(i).setBucketCount(searchModel.getTemplate().getTabFacet().getBuckets().get(j).getDoc_count());
                                        }
                                    }
                                }

                                rvTabs.setAdapter(new TabFacetsAdapter(FullResultsActivity.this, tabFacetsModel.getTabs(), FullResultsActivity.this));
                            }

                            tvAllResults.setText("All Results ("+ allCount +")");
                            setPageCounts(searchModel);

                            if(searchModel.getTemplate().getFacets() != null &&
                                searchModel.getTemplate().getFacets().size() > 0)
                            {
                                alvFacetsName.setAdapter(facetsFilterAdapter = new FacetsFilterAdapter(FullResultsActivity.this, searchModel.getTemplate().getFacets(), FullResultsActivity.this, arrTempFilters));
                            }
                        }
                        else
                        {
                            rlData.setVisibility(View.GONE);
                            rlNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        rlData.setVisibility(View.GONE);
                        rlNoData.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    rlData.setVisibility(View.GONE);
                    rlNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Log.e("Search data", t.toString());
            }
        });
    }

    private void setPageCounts(SearchModel searchModel)
    {
        if(searchModel.getTemplate().getResults() != null)
        {
            if(searchModel.getTemplate().getResults().getFaq() != null &&
                searchModel.getTemplate().getResults().getFaq().getDoc_count() > 0)
                faqPages = (searchModel.getTemplate().getResults().getFaq().getDoc_count()/10) + (searchModel.getTemplate().getResults().getFaq().getDoc_count() % 10 > 0 ? 1 : 0);

            if(searchModel.getTemplate().getResults().getWeb() != null &&
                searchModel.getTemplate().getResults().getWeb().getDoc_count() > 0)
                webPages = (searchModel.getTemplate().getResults().getWeb().getDoc_count()/10) + (searchModel.getTemplate().getResults().getWeb().getDoc_count() % 10 > 0 ? 1 : 0);

            if(searchModel.getTemplate().getResults().getTask() != null &&
                searchModel.getTemplate().getResults().getTask().getDoc_count() > 0)
                taskPages = (searchModel.getTemplate().getResults().getTask().getDoc_count()/10) + (searchModel.getTemplate().getResults().getTask().getDoc_count() % 10 > 0 ? 1 : 0);

            if(searchModel.getTemplate().getResults().getFile() != null &&
                searchModel.getTemplate().getResults().getFile().getDoc_count() > 0)
                filePages = (searchModel.getTemplate().getResults().getFile().getDoc_count()/10) + (searchModel.getTemplate().getResults().getFile().getDoc_count() % 10 > 0 ? 1 : 0);

            if(searchModel.getTemplate().getResults().getDefault_group() != null &&
                    searchModel.getTemplate().getResults().getDefault_group().getDoc_count() > 0)
                dataPages = (searchModel.getTemplate().getResults().getDefault_group().getDoc_count()/10) + (searchModel.getTemplate().getResults().getDefault_group().getDoc_count() % 10 > 0 ? 1 : 0);

            if(tabFacetSelected.equalsIgnoreCase(BundleConstants.FAQ))
                tvNoofPages.setText(""+faqPages);
            else if(tabFacetSelected.equalsIgnoreCase(BundleConstants.WEB))
                tvNoofPages.setText(""+webPages);
            else if(tabFacetSelected.equalsIgnoreCase(BundleConstants.TASK))
                tvNoofPages.setText(""+taskPages);
            else if(tabFacetSelected.equalsIgnoreCase(BundleConstants.FILE))
                tvNoofPages.setText(""+filePages);
            else if(tabFacetSelected.equalsIgnoreCase(BundleConstants.DATA))
                tvNoofPages.setText(""+dataPages);
        }
    }

    private void addLayoutView(LiveSearchResultsDataModel liveSearchResultsDataModel, final ResultsViewAppearance appearance, int from, String constant)
    {
        if(liveSearchResultsDataModel.getData() != null && liveSearchResultsDataModel.getData().size() > 0)
        {
            LinearLayout llFaq = (LinearLayout) LayoutInflater.from(FullResultsActivity.this).inflate(R.layout.search_template_new_cell, null);
            final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
            final RelativeLayout rlTitle = (RelativeLayout) llFaq.findViewById(R.id.rlTitle);
            final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
            final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
            TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
            final HeightAdjustableViewPager heightAdjustableViewPager = (HeightAdjustableViewPager) llFaq.findViewById(R.id.carouselViewpager);
            final AutoExpandListView lvResults = (AutoExpandListView) llFaq.findViewById(R.id.lvResults);
            alResults.addItemDecoration(new VerticalSpaceItemDecoration((int)(10 * dp1)));
            int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
            heightAdjustableViewPager.setPageMargin(pageMargin);

            if(appearance != null && appearance.getTemplate() != null && appearance.getTemplate().getType() != null)
            {
                heightAdjustableViewPager.setVisibility(GONE);
                alResults.setVisibility(GONE);
                lvResults.setVisibility(GONE);

                if(appearance.getTemplate().getLayout() != null)
                {
                    if(appearance.getTemplate().getLayout().getRenderTitle())
                        rlTitle.setVisibility(VISIBLE);
                    else
                        rlTitle.setVisibility(GONE);
                }

                if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                {
                    alResults.setVisibility(VISIBLE);
                    alResults.setLayoutManager(new GridLayoutManager(FullResultsActivity.this, 2));
                    alResults.setAdapter(new LiveSearchCyclerAdapter(FullResultsActivity.this, liveSearchResultsDataModel.getData(), 0, FullResultsActivity.this, FullResultsActivity.this));
                }
                else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                {
                    heightAdjustableViewPager.setVisibility(View.VISIBLE);
                    heightAdjustableViewPager.setAdapter(new SearchAssistCarouselAdapter(FullResultsActivity.this, liveSearchResultsDataModel.getData() , 0, FullResultsActivity.this, FullResultsActivity.this));
                }
                else
                {
                    lvResults.setVisibility(VISIBLE);

                    if(!appearance.getTemplate().getLayout().getListType().equalsIgnoreCase(BundleConstants.PLAIN))
                    {
                        lvResults.setDivider(getResources().getDrawable(android.R.color.transparent));
                        lvResults.setDividerHeight((int)(10 * dp1));
                    }

                    lvResults.setAdapter(new LiveSearchAdaper(FullResultsActivity.this, liveSearchResultsDataModel.getData(), 0, FullResultsActivity.this, FullResultsActivity.this));
                }
            }
            else
            {
                heightAdjustableViewPager.setVisibility(GONE);
                alResults.setVisibility(View.VISIBLE);
                alResults.setAdapter(new LiveSearchCyclerAdapter(FullResultsActivity.this, liveSearchResultsDataModel.getData(), 0, FullResultsActivity.this, FullResultsActivity.this));
            }

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

                    switch ((String) tabFacetSelected)
                    {
                        case BundleConstants.FAQ:
                            tvPageTitle.setText("FAQS");
                            break;
                        case BundleConstants.WEB:
                            tvPageTitle.setText("Web");
                            break;
                        case BundleConstants.TASK:
                            tvPageTitle.setText("Task");
                            break;
                        case BundleConstants.FILE:
                            tvPageTitle.setText("File");
                            break;
                        default:
                            break;
                    }
                    break;
                }
            }


            ibResults.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alResults.getVisibility() == GONE || heightAdjustableViewPager.getVisibility() == GONE
                        || lvResults.getVisibility() == GONE)
                    {
                        if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                            heightAdjustableViewPager.setVisibility(View.VISIBLE);
                        else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                            alResults.setVisibility(View.VISIBLE);
                        else
                            lvResults.setVisibility(View.VISIBLE);

                        ibResults.setVisibility(GONE);
                        ibResults2.setVisibility(View.VISIBLE);
                    }
                }
            });

            ibResults2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alResults.getVisibility() == View.VISIBLE || heightAdjustableViewPager.getVisibility() == View.VISIBLE
                        || lvResults.getVisibility() == VISIBLE)
                    {
                        if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                            heightAdjustableViewPager.setVisibility(GONE);
                        else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                            alResults.setVisibility(GONE);
                        else
                            lvResults.setVisibility(GONE);

                        ibResults2.setVisibility(GONE);
                        ibResults.setVisibility(View.VISIBLE);
                    }
                }
            });

            llAllresultsLayout.addView(llFaq);
        }
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
        }
    }

    private void addResultsView(ArrayList<HashMap<String, Object>> arrSearchResults, final ResultsViewAppearance appearance, int from, String constant)
    {
        LinearLayout llFaq = (LinearLayout) LayoutInflater.from(FullResultsActivity.this).inflate(R.layout.search_template_new_cell, null);
        final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
        final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
        final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
        TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
        final HeightAdjustableViewPager heightAdjustableViewPager = (HeightAdjustableViewPager) llFaq.findViewById(R.id.carouselViewpager);
        final AutoExpandListView lvResults = (AutoExpandListView) llFaq.findViewById(R.id.lvResults);

        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
        ViewGroup.LayoutParams params= heightAdjustableViewPager.getLayoutParams();
        params.height = (int)(170 * dp1);    //500px
        heightAdjustableViewPager.setLayoutParams(params);
        heightAdjustableViewPager.setPageMargin(pageMargin);

        if(appearance != null && appearance.getTemplate() != null && appearance.getTemplate().getType() != null)
        {
            heightAdjustableViewPager.setVisibility(GONE);
            alResults.setVisibility(GONE);
            lvResults.setVisibility(GONE);

            if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
            {
                alResults.setVisibility(View.VISIBLE);
                alResults.setLayoutManager(new GridLayoutManager(FullResultsActivity.this, 2));
                alResults.setAdapter(new LiveSearchDynamicAdapter(FullResultsActivity.this, arrSearchResults, 0, FullResultsActivity.this, FullResultsActivity.this));
            }
            else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
            {
                heightAdjustableViewPager.setVisibility(View.VISIBLE);

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

                heightAdjustableViewPager.setAdapter(new SearchResultsCarouselAdapter(FullResultsActivity.this, arrSearchResults , 0, FullResultsActivity.this, FullResultsActivity.this));
            }
            else
            {
                lvResults.setVisibility(VISIBLE);
                lvResults.setAdapter(new LiveSearchListAdapter(FullResultsActivity.this, arrSearchResults, 0, FullResultsActivity.this, FullResultsActivity.this));
            }
        }

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

        llAllresultsLayout.addView(llFaq);
    }

    public void getFilterSearch(String query, JsonArray filters, String messagePayload, final JsonObject jsonFilterObject, int from)
    {
        JsonObject  jsonObject = getJsonFilterBody(query, filters, jsonFilterObject,  messagePayload, from);

        Call<SearchModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getSearch(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(FullResultsActivity.this).getAccessToken(),  jsonObject, SocketWrapper.getInstance(FullResultsActivity.this).getJWTToken());
        getJWTTokenService.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response)
            {
                if (response.isSuccessful())
                {
                    filterSearchModel = response.body();
                    isFilterDataAvailable = false;

                    if(filterSearchModel != null && filterSearchModel.getTemplate() != null)
                    {
                        if(filterSearchModel.getTemplate().getResults() != null)
                        {
                            llAllresultsLayout.removeAllViews();
                            rlData.setVisibility(View.VISIBLE);
                            rlNoData.setVisibility(View.GONE);

                            if(filterSearchModel.getTemplate().getResults().getFaq() != null &&
                                filterSearchModel.getTemplate().getResults().getFaq().getData() != null &&
                                filterSearchModel.getTemplate().getResults().getFaq().getData().size() > 0)
                            {
                                isFilterDataAvailable = true;
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getFaq().getData());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getFaq(), faq,1, BundleConstants.FAQ);
                            }

                            if(filterSearchModel.getTemplate().getResults().getWeb() != null &&
                                filterSearchModel.getTemplate().getResults().getWeb().getData() != null &&
                                filterSearchModel.getTemplate().getResults().getWeb().getData().size() > 0)
                            {
                                isFilterDataAvailable = true;
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getWeb().getData());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getWeb(), page,2, BundleConstants.WEB);
                            }

                            if(filterSearchModel.getTemplate().getResults().getTask() != null &&
                                filterSearchModel.getTemplate().getResults().getTask().getData() != null &&
                                filterSearchModel.getTemplate().getResults().getTask().getData().size() > 0)
                            {
                                isFilterDataAvailable = true;
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getTask().getData());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getTask(), task,3, BundleConstants.TASK);
                            }

                            if(filterSearchModel.getTemplate().getResults().getFile() != null &&
                                filterSearchModel.getTemplate().getResults().getFile().getData() != null &&
                                filterSearchModel.getTemplate().getResults().getFile().getData().size() > 0)
                            {
                                isFilterDataAvailable = true;
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getFile().getData());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getFile(), document,4, BundleConstants.FILE);
                            }

                            if(filterSearchModel.getTemplate().getResults().getData() != null &&
                                filterSearchModel.getTemplate().getResults().getData().size() > 0)
                            {
                                isFilterDataAvailable = true;
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getData());
                                LiveSearchResultsDataModel liveSearchResultsDataModel = new LiveSearchResultsDataModel();
                                liveSearchResultsDataModel.setData(filterSearchModel.getTemplate().getResults().getData());
                                liveSearchResultsDataModel.setDoc_count(filterSearchModel.getTemplate().getResults().getData().size());
                                addLayoutView(liveSearchResultsDataModel , data,5, BundleConstants.DATA);
                            }

                            if(filterSearchModel.getTemplate().getResults().getDefault_group() != null)
                            {
                                if(filterSearchModel.getTemplate().getResults().getDefault_group().getData() != null &&
                                        filterSearchModel.getTemplate().getResults().getDefault_group().getData().size() > 0)
                                {
                                    isFilterDataAvailable = true;
                                    getSortedInfo(filterSearchModel.getTemplate().getResults().getDefault_group().getData());
                                    addLayoutView(filterSearchModel.getTemplate().getResults().getDefault_group(), data,5, BundleConstants.DATA);
                                }
                            }


                            rlData.setVisibility(View.GONE);
                            rlNoData.setVisibility(View.VISIBLE);

                            if(isFilterDataAvailable)
                            {
                                rlData.setVisibility(View.VISIBLE);
                                rlNoData.setVisibility(GONE);
                            }


                            if(filterSearchModel.getTemplate().getTabFacet() != null &&
                                    filterSearchModel.getTemplate().getTabFacet().getBuckets() != null &&
                                    filterSearchModel.getTemplate().getTabFacet().getBuckets().size() > 0)
                            {

                                for (int i = 0; i < tabFacetsModel.getTabs().size(); i++)
                                {
                                    for (int j = 0; j < filterSearchModel.getTemplate().getTabFacet().getBuckets().size(); j++)
                                    {
                                        if(filterSearchModel.getTemplate().getTabFacet().getBuckets().get(j).getKey().equalsIgnoreCase(tabFacetsModel.getTabs().get(i).getFieldValue()))
                                        {
                                            allCount += filterSearchModel.getTemplate().getTabFacet().getBuckets().get(j).getDoc_count();
                                            tabFacetsModel.getTabs().get(i).setBucketCount(filterSearchModel.getTemplate().getTabFacet().getBuckets().get(j).getDoc_count());
                                        }
                                    }
                                }

                                rvTabs.setAdapter(new TabFacetsAdapter(FullResultsActivity.this, tabFacetsModel.getTabs(), FullResultsActivity.this));
                            }

                            setPageCounts(filterSearchModel);
                            if(jsonFilterObject != null)
                                setPageTitle(1);
                            svResults.scrollTo(0, 0);
                        }
                        else
                        {
                            rlData.setVisibility(View.GONE);
                            rlNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        rlData.setVisibility(View.GONE);
                        rlNoData.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    rlData.setVisibility(View.GONE);
                    rlNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Log.e("Search data", t.toString());
            }
        });
    }

    private JsonObject getJsonBody(String query, boolean smallTalk, String messagePayload, String requestId)
    {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        jsonObject.addProperty("query", query.toLowerCase());
        jsonObject.addProperty("maxNumOfResults", 5);
        jsonObject.addProperty("userId", SocketWrapper.getInstance(FullResultsActivity.this).getBotUserId());
        jsonObject.addProperty("streamId", SDKConfiguration.Client.bot_id);
        jsonObject.addProperty("lang", "en");
        jsonObject.addProperty("isDev", false);
        jsonObject.addProperty("pageNumber", 0);
        jsonObject.addProperty("messagePayload", messagePayload);
        jsonObject.addProperty("searchRequestId", requestId);

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    private JsonObject getJsonFilterBody(String query, JsonArray filters, JsonObject tabfilter, String messagePayload, int from)
    {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        if(filters != null)
            jsonObject.add("filters", filters);

        if(tabfilter != null)
            jsonObject.add("tabConfig", tabfilter);

        if(from == 1)
            jsonObject.add("resultGroups", tabfilter);

        jsonObject.addProperty("query", query.toLowerCase());
        jsonObject.addProperty("maxNumOfResults", 10);
        jsonObject.addProperty("userId", SocketWrapper.getInstance(FullResultsActivity.this).getBotUserId());
        jsonObject.addProperty("streamId", SDKConfiguration.Client.bot_id);
        jsonObject.addProperty("lang", "en");
        jsonObject.addProperty("isDev", false);
        jsonObject.addProperty("pageNumber", currentPage);

        jsonObject.add("messagePayload", new JsonParser().parse(messagePayload).getAsJsonObject());

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    private JsonArray getFiltersArray(ArrayList<SearchFacetsBucketsModel> arrFilters)
    {
        JsonArray jsonArray = new JsonArray();

        if(searchModel != null && searchModel.getTemplate() != null
                && searchModel.getTemplate().getFacets() != null && searchModel.getTemplate().getFacets().size() > 0
                && arrFilters != null && arrFilters.size() > 0)
        {
            for (int k = 0; k < searchModel.getTemplate().getFacets().size(); k++)
            {
                String fldName = searchModel.getTemplate().getFacets().get(k).getFieldName();
                searchModel.getTemplate().getFacets().get(k).setArrSearchBucket(new ArrayList<SearchFacetsBucketsModel>());

                for(int j = 0; j < arrFilters.size(); j++)
                {
                    if(fldName.equalsIgnoreCase(arrFilters.get(j).getFieldName()))
                    {
                        searchModel.getTemplate().getFacets().get(k).getArrSearchBucket().add(arrFilters.get(j));
                    }
                }
            }



            for(int i = 0; i < searchModel.getTemplate().getFacets().size(); i++)
            {
                if(searchModel.getTemplate().getFacets().get(i).getArrSearchBucket().size() > 0)
                {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("fieldName", searchModel.getTemplate().getFacets().get(i).getFieldName());
                    jsonObject.addProperty("subtype", searchModel.getTemplate().getFacets().get(i).getSubtype());
                    jsonObject.addProperty("name", searchModel.getTemplate().getFacets().get(i).getName());

                    JsonArray facetsValue = new JsonArray();

                    for (int j = 0; j < searchModel.getTemplate().getFacets().get(i).getArrSearchBucket().size(); j++)
                    {
                        facetsValue.add(searchModel.getTemplate().getFacets().get(i).getArrSearchBucket().get(j).getKey());
                    }
                    jsonObject.add("facetValue", facetsValue);
                    jsonArray.add(jsonObject);
                }
            }
        }

        return jsonArray;
    }

    private JsonObject getTabFiltersObject(String facetValue)
    {
        JsonObject jsonMainObject = new JsonObject();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fieldName", "sys_content_type");
        JsonArray facetsValue = new JsonArray();
        facetsValue.add(facetValue);
        jsonObject.add("facetValue", facetsValue);
        jsonMainObject.add("filter", jsonObject);

        return jsonMainObject;
    }

    private JsonObject getFiltersObject(String facetValue)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fieldName", "sys_content_type");
        jsonObject.addProperty("facetType", "value");
        jsonObject.addProperty("facetName", "facetContentType");
        JsonArray facetsValue = new JsonArray();
        facetsValue.add(facetValue);
        jsonObject.add("facetValue", facetsValue);

        return jsonObject;
    }

    @Override
    public void tabFacetClicked(String fieldValue)
    {
        currentPage = 0;
        this.tabFacetSelected = fieldValue;
        getFilterSearch(originalQuery, getFiltersArray(arrTempFilters), messagePayload, getTabFiltersObject(fieldValue), 0);
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

    }
}
