package com.kore.findlysdk.models;

import java.util.ArrayList;

public class SearchFacetsModel
{
    private String fieldName;
    private boolean multiselect;
    private String name;
    private String subtype;
    private ArrayList<SearchFacetsBucketsModel> buckets;
    private ArrayList<SearchFacetsBucketsModel> arrSearchBucket = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }

    public String getName() {
        return name;
    }

    public String getSubtype() {
        return subtype;
    }

    public boolean getMultiselect()
    {
        return multiselect;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setBuckets(ArrayList<SearchFacetsBucketsModel> buckets) {
        this.buckets = buckets;
    }

    public ArrayList<SearchFacetsBucketsModel> getBuckets() {
        return buckets;
    }

    public void setArrSearchBucket(ArrayList<SearchFacetsBucketsModel> arrSearchBucket) {
        this.arrSearchBucket = arrSearchBucket;
    }

    public ArrayList<SearchFacetsBucketsModel> getArrSearchBucket() {
        return arrSearchBucket;
    }
}
