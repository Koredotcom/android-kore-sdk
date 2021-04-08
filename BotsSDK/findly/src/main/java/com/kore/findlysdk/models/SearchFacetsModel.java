package com.kore.findlysdk.models;

import java.util.ArrayList;

public class SearchFacetsModel
{
    private String fieldName;
    private String facetName;
    private String facetType;
    private ArrayList<SearchFacetsBucketsModel> buckets;
    private ArrayList<SearchFacetsBucketsModel> arrSearchBucket = new ArrayList<>();

    public String getFacetName() {
        return facetName;
    }

    public String getFacetType() {
        return facetType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFacetName(String facetName) {
        this.facetName = facetName;
    }

    public void setFacetType(String facetType) {
        this.facetType = facetType;
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
