package com.kore.findlysdk.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.kore.findlysdk.adapters.LiveSearchCyclerAdapter;
import com.kore.findlysdk.adapters.SearchAssistCarouselAdapter;
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
import com.kore.findlysdk.models.ResultsViewMapping;
import com.kore.findlysdk.models.ResultsViewModel;
import com.kore.findlysdk.models.ResultsViewSetting;
import com.kore.findlysdk.models.ResultsViewTemplate;
import com.kore.findlysdk.models.ResultsViewlayout;
import com.kore.findlysdk.models.SearchFacetsBucketsModel;
import com.kore.findlysdk.models.SearchModel;
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

public class FullResultsActivity extends AppCompatActivity implements View.OnClickListener, InvokeGenericWebViewInterface, FilterResultsListner, FilterOptionsListner
{
    private ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels, arrFaqSearchResultsModels, arrPageSearchResultsModels, arrActionSearchResultsModels, arrDocumentSearchReultsModels, arrDataSearchReultsModels;
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
    private RecyclerView lvLiveSearch;
    private int currentTab, currentPage = 0;
    private JsonArray jsonTotalArray = new JsonArray();
    private int faqPages, webPages, filePages, dataPages, taskPages;
    private ScrollView svResults;

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
        lvLiveSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        dp1 = Utility.convertDpToPixel(FullResultsActivity.this, 1);

        setPageTitle(0);
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

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                switch (currentTab)
                {
                    case 1:
                        if(currentPage+1 < faqPages)
                        {
                            currentPage++;
                            setPageTitle(currentTab);
                        }
                        break;
                    case 2:
                        if(currentPage+1 < webPages)
                        {
                            currentPage++;
                            setPageTitle(currentTab);
                        }
                        break;
                    case 3:
                        if(currentPage+1 < taskPages)
                        {
                            currentPage++;
                            setPageTitle(currentTab);
                        }
                        break;
                    case 4:
                        if(currentPage+1 < filePages)
                        {
                            currentPage++;
                            setPageTitle(currentTab);
                        }
                        break;
                    case 5:
                        if(currentPage+1 < dataPages)
                        {
                            currentPage++;
                            setPageTitle(currentTab);
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
                    setPageTitle(currentTab);
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
                    default:
                        currentPage = 0;
                }

                setPageTitle(currentTab);
            }
        });
    }

    private void getSortedInfo(ArrayList<LiveSearchResultsModel> arrAllResultsModels)
    {
        arrFaqSearchResultsModels = new ArrayList<>();
        arrPageSearchResultsModels = new ArrayList<>();
        arrActionSearchResultsModels = new ArrayList<>();
        arrDocumentSearchReultsModels = new ArrayList<>();
        arrDataSearchReultsModels = new ArrayList<>();

        for (int i = 0; i < arrAllResultsModels.size(); i++)
        {
            if(arrAllResultsModels.get(i).getSysContentType() != null)
            {
                if(arrAllResultsModels.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.FAQ))
                {
                    arrAllResultsModels.get(i).setAppearance(faq);
                    arrFaqSearchResultsModels.add(arrAllResultsModels.get(i));
                }

                if(arrAllResultsModels.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.WEB))
                {
                    arrAllResultsModels.get(i).setAppearance(page);
                    arrPageSearchResultsModels.add(arrAllResultsModels.get(i));
                }

                if(arrAllResultsModels.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.TASK))
                {
                    if(task == null)
                        task = getResultsViewAppearance(BundleConstants.TASK);

                    arrAllResultsModels.get(i).setAppearance(task);
                    arrActionSearchResultsModels.add(arrAllResultsModels.get(i));
                }

                if(arrAllResultsModels.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.FILE))
                {
                    arrAllResultsModels.get(i).setAppearance(document);
                    arrDocumentSearchReultsModels.add(arrAllResultsModels.get(i));
                }

                if(arrAllResultsModels.get(i).getSysContentType().equalsIgnoreCase(BundleConstants.DATA))
                {
                    arrAllResultsModels.get(i).setAppearance(data);
                    arrDataSearchReultsModels.add(arrAllResultsModels.get(i));
                }
            }
            else if(arrAllResultsModels.get(i).getContentType() != null)
            {
                if(arrAllResultsModels.get(i).getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                {
                    arrAllResultsModels.get(i).setAppearance(faq);
                    arrFaqSearchResultsModels.add(arrAllResultsModels.get(i));
                }

                if(arrAllResultsModels.get(i).getContentType().equalsIgnoreCase(BundleConstants.WEB))
                {
                    arrAllResultsModels.get(i).setAppearance(page);
                    arrPageSearchResultsModels.add(arrAllResultsModels.get(i));
                }

                if(arrAllResultsModels.get(i).getContentType().equalsIgnoreCase(BundleConstants.TASK))
                {
                    if(task == null)
                        task = getResultsViewAppearance(BundleConstants.TASK);

                    arrAllResultsModels.get(i).setAppearance(task);
                    arrActionSearchResultsModels.add(arrAllResultsModels.get(i));
                }

                if(arrAllResultsModels.get(i).getContentType().equalsIgnoreCase(BundleConstants.FILE))
                {
                    arrAllResultsModels.get(i).setAppearance(document);
                    arrDocumentSearchReultsModels.add(arrAllResultsModels.get(i));
                }

                if(arrAllResultsModels.get(i).getContentType().equalsIgnoreCase(BundleConstants.DATA))
                {
                    arrAllResultsModels.get(i).setAppearance(data);
                    arrDataSearchReultsModels.add(arrAllResultsModels.get(i));
                }
            }
        }

        arrLiveSearchResultsModels = new ArrayList<>();
        arrLiveSearchResultsModels.addAll(arrActionSearchResultsModels);
        arrLiveSearchResultsModels.addAll(arrPageSearchResultsModels);
        arrLiveSearchResultsModels.addAll(arrFaqSearchResultsModels);
        arrLiveSearchResultsModels.addAll(arrDocumentSearchReultsModels);
        arrLiveSearchResultsModels.addAll(arrDataSearchReultsModels);
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
                else if(searchSettings.getAppearance().get(i).getType().equalsIgnoreCase(BundleConstants.TASK))
                    task = searchSettings.getAppearance().get(i);
                else if(searchSettings.getAppearance().get(i).getType().equalsIgnoreCase(BundleConstants.STRUCTURE_DATA))
                    data = searchSettings.getAppearance().get(i);
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
//        getSearch(originalQuery);
        currentPage = 0;
        tvPageNumber.setText("1");
        tvClearAll.setVisibility(View.GONE);

        switch (currentTab)
        {
            case 0:
                if(!StringUtils.isNullOrEmpty(originalQuery))
                    getSearch(originalQuery);
                break;
            case 1:
                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.FAQ), messagePayload);
                break;
            case 2:
                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.WEB), messagePayload);
                break;
            case 3:
                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.TASK), messagePayload);
                break;
            case 4:
                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.FILE), messagePayload);
                break;
            case 5:
                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.DATA), messagePayload);
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
            jsonTotalArray.addAll(jsonArray);

            switch (currentTab)
            {
                case 1:
                    jsonArray.add(getFiltersObject(BundleConstants.FAQ));
                    break;
                case 2:
                    jsonArray.add(getFiltersObject(BundleConstants.WEB));
                    break;
                case 3:
                    jsonArray.add(getFiltersObject(BundleConstants.TASK));
                    break;
                case 4:
                    jsonArray.add(getFiltersObject(BundleConstants.FILE));
                    break;
                case 5:
                    jsonArray.add(getFiltersObject(BundleConstants.DATA));
                    break;
                default:
                    break;
            }

            currentPage = 0;
            tvPageNumber.setText("1");
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
//                vpAllResults.setCurrentItem(0);

                if(jsonTotalArray != null && jsonTotalArray.size() > 0)
                {
                    getFilterSearch(originalQuery, jsonTotalArray, messagePayload);
                }
                else
                {
                    if(!StringUtils.isNullOrEmpty(originalQuery))
                        getSearch(originalQuery);
                }

                break;
            case 1:
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

                tvFaqResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivFaqs.setImageResource(R.mipmap.faq_select);

                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.actions_unselect);

                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);

                tvDataResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivData.setImageResource(R.mipmap.pages_unselect);

//                vpAllResults.setCurrentItem(1);
                currentTab = 1;
                lvLiveSearch.setVisibility(View.GONE);

                if(faqPages > 1)
                {
                    llPagination.setVisibility(View.VISIBLE);
                    tvNoofPages.setText(""+faqPages);
                    tvPageNumber.setText((currentPage+1)+"");
                }
                else
                    llPagination.setVisibility(GONE);

                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.FAQ), messagePayload);

                break;
            case 2:
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

                tvPagesResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivPages.setImageResource(R.mipmap.pages_select);

                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.actions_unselect);

                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);

                tvDataResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivData.setImageResource(R.mipmap.pages_unselect);

//                vpAllResults.setCurrentItem(2);
                currentTab = 2;
                lvLiveSearch.setVisibility(View.GONE);

                if(webPages > 1)
                {
                    llPagination.setVisibility(View.VISIBLE);
                    tvNoofPages.setText(""+webPages);
                    tvPageNumber.setText((currentPage+1)+"");
                }
                else
                    llPagination.setVisibility(GONE);

                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.WEB), messagePayload);

                break;
            case 3:
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

                tvActionsResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivActions.setImageResource(R.mipmap.actions_select);

                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);

                tvDataResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivData.setImageResource(R.mipmap.pages_unselect);

//                vpAllResults.setCurrentItem(3);
                currentTab = 3;
                lvLiveSearch.setVisibility(View.GONE);
                if(taskPages > 1)
                {
                    llPagination.setVisibility(View.VISIBLE);
                    tvNoofPages.setText(""+taskPages);
                    tvPageNumber.setText((currentPage+1)+"");
                }
                else
                    llPagination.setVisibility(GONE);
                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.TASK), messagePayload);


                break;

            case 4:
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.actions_unselect);

                tvDocumentsResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                ivDocuments.setImageResource(R.mipmap.docs_select);

                tvDataResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivData.setImageResource(R.mipmap.pages_unselect);

//                vpAllResults.setCurrentItem(4);
                currentTab = 4;
                lvLiveSearch.setVisibility(View.GONE);
                if(filePages > 1)
                {
                    llPagination.setVisibility(View.VISIBLE);
                    tvNoofPages.setText(""+filePages);
                    tvPageNumber.setText((currentPage+1)+"");
                }
                else
                    llPagination.setVisibility(GONE);
                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.FILE), messagePayload);

                break;

             case 5:
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivAllresults.setImageResource(R.mipmap.allresults_unselect);

                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivFaqs.setImageResource(R.mipmap.faq_unselect);

                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivPages.setImageResource(R.mipmap.pages_unselect);

                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivActions.setImageResource(R.mipmap.actions_unselect);

                tvDocumentsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                ivDocuments.setImageResource(R.mipmap.docs_unselect);

                 tvDataResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                 ivData.setImageResource(R.mipmap.pages_select);

//                vpAllResults.setCurrentItem(4);
                currentTab = 5;
                lvLiveSearch.setVisibility(View.GONE);
                if(dataPages > 1)
                {
                    llPagination.setVisibility(View.VISIBLE);
                    tvNoofPages.setText(""+dataPages);
                    tvPageNumber.setText((currentPage+1)+"");
                }
                else
                    llPagination.setVisibility(GONE);

                getFilterSearch(originalQuery, getFiltersArray(BundleConstants.DATA), messagePayload);
                break;
        }
    }

    public void getSearch(String query)
    {
        JsonObject jsonObject = getJsonBody(query, true, messagePayload);
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
                                llAllresultsLayout.removeAllViews();
                                rlData.setVisibility(View.VISIBLE);
                                rlNoData.setVisibility(View.GONE);

                                arrTempAllResults = new ArrayList<>();

                                if(searchModel.getTemplate().getResults().getFaq() != null &&
                                        searchModel.getTemplate().getResults().getFaq().size() > 0)
                                {
                                    getSortedInfo(searchModel.getTemplate().getResults().getFaq());
                                    addLayoutView(searchModel.getTemplate().getResults().getFaq(), faq,1, BundleConstants.FAQ);
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getFaq());
                                }

                                if(searchModel.getTemplate().getResults().getWeb() != null &&
                                        searchModel.getTemplate().getResults().getWeb().size() > 0)
                                {
                                    getSortedInfo(searchModel.getTemplate().getResults().getWeb());
                                    addLayoutView(searchModel.getTemplate().getResults().getWeb(), page,2, BundleConstants.WEB);
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getWeb());
                                }

                                if(searchModel.getTemplate().getResults().getTask() != null &&
                                        searchModel.getTemplate().getResults().getTask().size() > 0)
                                {
                                    getSortedInfo(searchModel.getTemplate().getResults().getTask());
                                    addLayoutView(searchModel.getTemplate().getResults().getTask(), task,3, BundleConstants.TASK);
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getTask());
                                }

                                if(searchModel.getTemplate().getResults().getFile() != null &&
                                        searchModel.getTemplate().getResults().getFile().size() > 0)
                                {
                                    getSortedInfo(searchModel.getTemplate().getResults().getFile());
                                    addLayoutView(searchModel.getTemplate().getResults().getFile(), document,4, BundleConstants.FILE);
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getFile());
                                }

                                if(searchModel.getTemplate().getResults().getData() != null &&
                                        searchModel.getTemplate().getResults().getData().size() > 0)
                                {
                                    getSortedInfo(searchModel.getTemplate().getResults().getData());
                                    addLayoutView(searchModel.getTemplate().getResults().getData(), data,5, BundleConstants.DATA);
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getData());
                                }

//                                getSortedInfo(arrTempAllResults);

//                                if(myResultsAdapter == null)
//                                {
//                                    vpAllResults.setAdapter(myResultsAdapter = new MyResultsAdapter(getSupportFragmentManager(), false));
//                                    view_pager_tab.setupWithViewPager(vpAllResults);
//                                }
//                                else
//                                    myResultsAdapter.refresh(false);

//                                if(arrTempAllResults != null && arrTempAllResults.size() > 0)
//                                {
//                                    lvLiveSearch.setVisibility(View.VISIBLE);
//                                    lvLiveSearch.setAdapter( new LiveSearchCyclerAdapter(FullResultsActivity.this, arrTempAllResults, 1, FullResultsActivity.this, null));

//                                }

//                                if(liveSearchModel.getTemplate().getResults().getFaq() != null && liveSearchModel.getTemplate().getResults().getFaq().size() > 0)
//                                {
//                                    addLayoutView(arrTempAllResults, faq,1, BundleConstants.FAQ);
//                                }
//
//                                if(liveSearchModel.getTemplate().getResults().getWeb() != null && liveSearchModel.getTemplate().getResults().getWeb().size() > 0)
//                                {
//                                    addLayoutView(liveSearchModel.getTemplate().getResults().getWeb(), page, 2, BundleConstants.WEB);
//                                }

                                tvAllResults.setText("All Results ("+ searchModel.getTemplate().getFacets().getAll_results()+")");
                                tvFaqResults.setText("FAQs ("+ searchModel.getTemplate().getFacets().getFaq()+")");
                                tvPagesResults.setText("Web ("+ searchModel.getTemplate().getFacets().getWeb()+")");
                                tvActionsResults.setText("Actions ("+ searchModel.getTemplate().getFacets().getTask()+")");
                                tvDocumentsResults.setText("File ("+ searchModel.getTemplate().getFacets().getFile()+")");
                                tvDataResults.setText("Data ("+ searchModel.getTemplate().getFacets().getData()+")");

                                setPageCounts(searchModel);

                                if(searchModel.getTemplate().getSearchFacets() != null &&
                                    searchModel.getTemplate().getSearchFacets().size() > 0)
                                {
                                    alvFacetsName.setAdapter(facetsFilterAdapter = new FacetsFilterAdapter(FullResultsActivity.this, searchModel.getTemplate().getSearchFacets(), FullResultsActivity.this, arrTempFilters));
                                }
                            }
                            else
                            {
                                rlData.setVisibility(View.GONE);
                                rlNoData.setVisibility(View.VISIBLE);
                            }
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
        if(searchModel.getTemplate().getFacets().getFaq() > 0)
            faqPages = (searchModel.getTemplate().getFacets().getFaq()/10) + (searchModel.getTemplate().getFacets().getFaq() % 10 > 0 ? 1 : 0);

        if(searchModel.getTemplate().getFacets().getWeb() > 0)
            webPages = (searchModel.getTemplate().getFacets().getWeb()/10) + (searchModel.getTemplate().getFacets().getWeb() % 10 > 0 ? 1 : 0);

        if(searchModel.getTemplate().getFacets().getTask() > 0)
            taskPages = (searchModel.getTemplate().getFacets().getTask()/10) + (searchModel.getTemplate().getFacets().getTask() % 10 > 0 ? 1 : 0);

        if(searchModel.getTemplate().getFacets().getFile() > 0)
            filePages = (searchModel.getTemplate().getFacets().getFile()/10) + (searchModel.getTemplate().getFacets().getFile() % 10 > 0 ? 1 : 0);

        if(searchModel.getTemplate().getFacets().getData() > 0)
            dataPages = (searchModel.getTemplate().getFacets().getData()/10) + (searchModel.getTemplate().getFacets().getData() % 10 > 0 ? 1 : 0);

        switch (currentTab)
        {
            case 1:
                tvNoofPages.setText(""+faqPages);
                break;
            case 2:
                tvNoofPages.setText(""+webPages);
                break;
            case 3:
                tvNoofPages.setText(""+taskPages);
                break;
            case 4:
                tvNoofPages.setText(""+filePages);
                break;
            case 5:
                tvNoofPages.setText(""+dataPages);
            default:
                break;
        }

    }

    private void addLayoutView(ArrayList<LiveSearchResultsModel> arrSearchResults, final ResultsViewAppearance appearance, int from, String constant)
    {
        LinearLayout llFaq = (LinearLayout) LayoutInflater.from(FullResultsActivity.this).inflate(R.layout.search_template_new_cell, null);
        final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
        final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
        final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
        TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
        final HeightAdjustableViewPager heightAdjustableViewPager = (HeightAdjustableViewPager) llFaq.findViewById(R.id.carouselViewpager);
        ResultsViewAppearance appearance1;
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
            {
                alResults.setLayoutManager(new GridLayoutManager(FullResultsActivity.this, 2));
            }
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

                heightAdjustableViewPager.setAdapter(new SearchAssistCarouselAdapter(FullResultsActivity.this, arrSearchResults , 0, FullResultsActivity.this, null));
            }
            else
            {
                alResults.setLayoutManager(new LinearLayoutManager(FullResultsActivity.this));
            }
        }

        alResults.setAdapter(new LiveSearchCyclerAdapter(FullResultsActivity.this, arrSearchResults, 0, FullResultsActivity.this, null));
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

    public void getFilterSearch(String query, JsonArray filters, String messagePayload)
    {
        JsonObject jsonObject = getJsonFilterBody(query, filters, messagePayload);
        Call<SearchModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getSearch(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(FullResultsActivity.this).getAccessToken(),  jsonObject, SocketWrapper.getInstance(FullResultsActivity.this).getJWTToken());
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
                            llAllresultsLayout.removeAllViews();
                            rlData.setVisibility(View.VISIBLE);
                            rlNoData.setVisibility(View.GONE);

                            arrTempAllResults = new ArrayList<>();

                            if(filterSearchModel.getTemplate().getResults().getFaq() != null &&
                                    filterSearchModel.getTemplate().getResults().getFaq().size() > 0)
                            {
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getFaq());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getFaq(), faq,1, BundleConstants.FAQ);
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getFaq());
                            }

                            if(filterSearchModel.getTemplate().getResults().getWeb() != null &&
                                    filterSearchModel.getTemplate().getResults().getWeb().size() > 0)
                            {
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getWeb());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getWeb(), page,2, BundleConstants.WEB);
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getWeb());
                            }

                            if(filterSearchModel.getTemplate().getResults().getTask() != null &&
                                    filterSearchModel.getTemplate().getResults().getTask().size() > 0)
                            {
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getTask());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getTask(), task,3, BundleConstants.TASK);
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getTask());
                            }

                            if(filterSearchModel.getTemplate().getResults().getFile() != null &&
                                    filterSearchModel.getTemplate().getResults().getFile().size() > 0)
                            {
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getFile());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getFile(), document,4, BundleConstants.FILE);
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getFile());
                            }

                            if(filterSearchModel.getTemplate().getResults().getData() != null &&
                                    filterSearchModel.getTemplate().getResults().getData().size() > 0)
                            {
                                getSortedInfo(filterSearchModel.getTemplate().getResults().getData());
                                addLayoutView(filterSearchModel.getTemplate().getResults().getData(), data,5, BundleConstants.DATA);
                                arrTempAllResults.addAll(filterSearchModel.getTemplate().getResults().getData());
                            }

                            if(arrTempAllResults.size() > 0)
                                getSortedInfo(arrTempAllResults);
                            else
                            {
                                rlData.setVisibility(View.GONE);
                                rlNoData.setVisibility(View.VISIBLE);
                            }

                            tvAllResults.setText("All Results ("+ filterSearchModel.getTemplate().getFacets().getAll_results()+")");
                            tvFaqResults.setText("FAQs ("+ filterSearchModel.getTemplate().getFacets().getFaq()+")");
                            tvPagesResults.setText("Web ("+ filterSearchModel.getTemplate().getFacets().getWeb()+")");
                            tvActionsResults.setText("Actions ("+ filterSearchModel.getTemplate().getFacets().getTask()+")");
                            tvDocumentsResults.setText("File ("+ filterSearchModel.getTemplate().getFacets().getFile()+")");
                            tvDataResults.setText("Data ("+ filterSearchModel.getTemplate().getFacets().getData()+")");

                            setPageCounts(filterSearchModel);

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

    private JsonObject getJsonBody(String query, boolean smallTalk, String messagePayload)
    {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        jsonObject.addProperty("query", query.toLowerCase());
        jsonObject.addProperty("maxNumOfResults", 16);
        jsonObject.addProperty("userId", SocketWrapper.getInstance(FullResultsActivity.this).getBotUserId());
        jsonObject.addProperty("streamId", SDKConfiguration.Client.bot_id);
        jsonObject.addProperty("lang", "en");
        jsonObject.addProperty("isDev", false);
        jsonObject.addProperty("pageNumber", 0);
        jsonObject.addProperty("messagePayload", messagePayload);

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    private JsonObject getJsonFilterBody(String query, JsonArray filters, String messagePayload)
    {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        jsonObject.add("filters", filters);
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

    private JsonArray getFiltersArray(String facetValue)
    {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fieldName", "sysContentType");
        jsonObject.addProperty("facetType", "value");
        jsonObject.addProperty("facetName", "facetContentType");
        JsonArray facetsValue = new JsonArray();
        facetsValue.add(facetValue);
        jsonObject.add("facetValue", facetsValue);
        jsonArray.add(jsonObject);

        if(jsonTotalArray != null && jsonTotalArray.size() > 0)
            jsonArray.addAll(jsonTotalArray);

        return jsonArray;
    }

    private JsonObject getFiltersObject(String facetValue)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fieldName", "sysContentType");
        jsonObject.addProperty("facetType", "value");
        jsonObject.addProperty("facetName", "facetContentType");
        JsonArray facetsValue = new JsonArray();
        facetsValue.add(facetValue);
        jsonObject.add("facetValue", facetsValue);

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

    }
}
