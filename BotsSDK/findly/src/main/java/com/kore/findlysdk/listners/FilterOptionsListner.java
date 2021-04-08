package com.kore.findlysdk.listners;

import com.kore.findlysdk.models.SearchFacetsBucketsModel;

import java.util.ArrayList;

public interface FilterOptionsListner
{
    public void onFilterApplied(ArrayList<SearchFacetsBucketsModel> arrTempFilters);
}
