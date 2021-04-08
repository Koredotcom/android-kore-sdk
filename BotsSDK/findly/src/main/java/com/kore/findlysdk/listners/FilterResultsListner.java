package com.kore.findlysdk.listners;

import com.kore.findlysdk.models.SearchFacetsBucketsModel;

import java.util.ArrayList;

public interface FilterResultsListner
{
    public void onFilterSelected(ArrayList<SearchFacetsBucketsModel> filters, String fieldName, String facetsType);
    public void onFilterClear();
}
