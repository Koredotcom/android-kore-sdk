package com.kore.findlysdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kore.findlysdk.R;
import com.kore.findlysdk.fragments.AllResultsFragment;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.utils.BundleConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class FullResultsActivity extends AppCompatActivity implements View.OnClickListener, InvokeGenericWebViewInterface
{
    private ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels, arrFaqSearchResultsModels, arrPageSearchResultsModels, arrActionSearchResultsModels;
    private ViewPager vpAllResults;
    private MyResultsAdapter myResultsAdapter;
    private TabLayout view_pager_tab;
    private TextView tvAllResults, tvFaqResults, tvPagesResults, tvActionsResults;
    private AllResultsFragment allResultsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_results_findly_layout);
        getBundleInfo();

        vpAllResults = (ViewPager)findViewById(R.id.vpAllResults);
        view_pager_tab = (TabLayout) findViewById(R.id.view_pager_tab);
        tvAllResults = (TextView) findViewById(R.id.tvAllResults);
        tvFaqResults = (TextView) findViewById(R.id.tvFaqResults);
        tvPagesResults = (TextView) findViewById(R.id.tvPagesResults);
        tvActionsResults = (TextView) findViewById(R.id.tvActionsResults);

        vpAllResults.setAdapter(new MyResultsAdapter(getSupportFragmentManager()));
        view_pager_tab.setupWithViewPager(vpAllResults);

        tvAllResults.setText("All Results ("+ arrLiveSearchResultsModels.size()+")");
        tvFaqResults.setText("FAQ's ("+ arrFaqSearchResultsModels.size()+")");
        tvPagesResults.setText("Pages ("+ arrPageSearchResultsModels.size()+")");
        tvActionsResults.setText("Actions ("+ arrActionSearchResultsModels.size()+")");

        setPageTitle(0);

        tvAllResults.setOnClickListener(this);
        tvFaqResults.setOnClickListener(this);
        tvPagesResults.setOnClickListener(this);
        tvActionsResults.setOnClickListener(this);
    }

    private void getBundleInfo()
    {
        arrFaqSearchResultsModels = new ArrayList<>();
        arrPageSearchResultsModels = new ArrayList<>();
        arrActionSearchResultsModels = new ArrayList<>();

        arrLiveSearchResultsModels = (ArrayList<LiveSearchResultsModel>)getIntent().getExtras().getSerializable("Results");

        for (int i = 0; i < arrLiveSearchResultsModels.size(); i++)
        {
            if(arrLiveSearchResultsModels.get(i).getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                arrFaqSearchResultsModels.add(arrLiveSearchResultsModels.get(i));
        }

        for (int i = 0; i < arrLiveSearchResultsModels.size(); i++)
        {
            if(arrLiveSearchResultsModels.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                arrPageSearchResultsModels.add(arrLiveSearchResultsModels.get(i));
        }

        for (int i = 0; i < arrLiveSearchResultsModels.size(); i++)
        {
            if(arrLiveSearchResultsModels.get(i).getContentType().equalsIgnoreCase(BundleConstants.TASK))
                arrActionSearchResultsModels.add(arrLiveSearchResultsModels.get(i));
        }

        arrLiveSearchResultsModels.clear();
        arrLiveSearchResultsModels.addAll(arrActionSearchResultsModels);
        arrLiveSearchResultsModels.addAll(arrPageSearchResultsModels);
        arrLiveSearchResultsModels.addAll(arrFaqSearchResultsModels);
    }


    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.tvAllResults)  {
            setPageTitle(0);
        }
        else if (view.getId() == R.id.tvFaqResults) {
            setPageTitle(1);
        }
        else if (view.getId() == R.id.tvPagesResults) {
            setPageTitle(2);
        }
        else if (view.getId() == R.id.tvActionsResults) {
            setPageTitle(3);
        }
        else {
            setPageTitle(0);
        }
    }

    @Override
    public void invokeGenericWebView(String url)
    {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), GenericWebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("header", getResources().getString(R.string.app_name));
            startActivity(intent);
        }
    }

    @Override
    public void handleUserActions(String payload, HashMap<String, Object> type) {

    }

    public class MyResultsAdapter extends FragmentPagerAdapter
    {
        private int NUM_ITEMS = 4;

        public MyResultsAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
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
                    }
                    return allResultsFragment;
                case 1:
                    {
                        allResultsFragment = AllResultsFragment.newInstance(arrFaqSearchResultsModels);
                        allResultsFragment.setInvokeGenericWebViewInterface(FullResultsActivity.this);
                    }
                    return allResultsFragment;
                case 2:
                    {
                        allResultsFragment = AllResultsFragment.newInstance(arrPageSearchResultsModels);
                        allResultsFragment.setInvokeGenericWebViewInterface(FullResultsActivity.this);
                    }
                    return allResultsFragment;
                case 3:
                    {
                        allResultsFragment = AllResultsFragment.newInstance(arrActionSearchResultsModels);
                        allResultsFragment.setInvokeGenericWebViewInterface(FullResultsActivity.this);
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
                tvAllResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvAllResults.setTextColor(getResources().getColor(R.color.txtFontBlack));

                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                vpAllResults.setCurrentItem(0);
                break;
            case 1:
                tvAllResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvFaqResults.setTextColor(getResources().getColor(R.color.txtFontBlack));

                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                vpAllResults.setCurrentItem(1);
                break;
            case 2:
                tvAllResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvPagesResults.setTextColor(getResources().getColor(R.color.txtFontBlack));

                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvActionsResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));
                vpAllResults.setCurrentItem(2);
                break;
            case 3:
                tvAllResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvAllResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvFaqResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvFaqResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvPagesResults.setBackgroundColor(getResources().getColor(R.color.bgtransparent));
                tvPagesResults.setTextColor(getResources().getColor(R.color.tabs_text_bg));

                tvActionsResults.setBackgroundColor(getResources().getColor(R.color.white));
                tvActionsResults.setTextColor(getResources().getColor(R.color.txtFontBlack));
                vpAllResults.setCurrentItem(3);
                break;
        }
    }
}
