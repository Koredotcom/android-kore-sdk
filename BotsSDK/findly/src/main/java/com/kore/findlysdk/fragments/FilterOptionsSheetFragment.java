package com.kore.findlysdk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kore.findlysdk.R;
import com.kore.findlysdk.activity.FullResultsActivity;
import com.kore.findlysdk.adapters.FacetsFilterAdapter;
import com.kore.findlysdk.listners.FilterOptionsListner;
import com.kore.findlysdk.listners.FilterResultsListner;
import com.kore.findlysdk.models.SearchFacetsBucketsModel;
import com.kore.findlysdk.models.SearchModel;

import java.util.ArrayList;

public class FilterOptionsSheetFragment extends BottomSheetDialogFragment implements FilterResultsListner
{
    private View view;
    private ListView alvFacetsName;
    private TextView tvClearAll, tvApply;
    private SearchModel searchModel;
    private FacetsFilterAdapter facetsFilterAdapter;
    private ArrayList<SearchFacetsBucketsModel> arrTempFilters = new ArrayList<>();
    private FilterOptionsListner filterListner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.facets_filter_layout, container,false);
        alvFacetsName = (ListView) view.findViewById(R.id.alvFacetsName);
        tvClearAll = (TextView) view.findViewById(R.id.tvClearAll);
        tvApply = (TextView) view.findViewById(R.id.tvApply);

        if(arrTempFilters != null && arrTempFilters.size() > 0)
        {
            tvApply.setVisibility(View.VISIBLE);
            tvClearAll.setVisibility(View.VISIBLE);
        }

        if(searchModel.getTemplate().getSearchFacets() != null &&
                searchModel.getTemplate().getSearchFacets().size() > 0)
        {
            alvFacetsName.setAdapter(facetsFilterAdapter = new FacetsFilterAdapter(getActivity(), searchModel.getTemplate().getSearchFacets(), FilterOptionsSheetFragment.this, arrTempFilters));
        }

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
                filterListner.onFilterApplied(arrTempFilters);
            }
        });

        return view;
    }

    public void setFilterOptions(SearchModel searchModel)
    {
        this.searchModel = searchModel;
    }

    public void setButtonVisible(boolean isShow)
    {
        if(isShow)
        {
            tvClearAll.setVisibility(View.VISIBLE);
            tvApply.setVisibility(View.VISIBLE);
        }
        else
        {
            tvClearAll.setVisibility(View.GONE);
            tvApply.setVisibility(View.GONE);
        }
    }

    public void setFilterListner(FilterOptionsListner filterListner)
    {
        this.filterListner = filterListner;
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

            }
            else
            {
                tvClearAll.setVisibility(View.GONE);
            }
        }
    }

    public void setAppliedFilter(ArrayList<SearchFacetsBucketsModel> appliedFilters)
    {
        this.arrTempFilters = appliedFilters;
    }

    @Override
    public void onFilterClear()
    {
        arrTempFilters = new ArrayList<>();
        tvClearAll.setVisibility(View.GONE);
        filterListner.onFilterApplied(arrTempFilters);
    }


}
