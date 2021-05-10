package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveSearchTemplateModel implements Serializable
{
    private String originalQuery;
    private String cleanQuery;
    private LiveSearchFacetsModel facets;
    private LiveSearchResultsOuterModel results;

    public void setCleanQuery(String cleanQuery) {
        this.cleanQuery = cleanQuery;
    }

    public void setFacets(LiveSearchFacetsModel facets) {
        this.facets = facets;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    public void setResults(LiveSearchResultsOuterModel results) {
        this.results = results;
    }

    public LiveSearchResultsOuterModel getResults() {
        return results;
    }

    public LiveSearchFacetsModel getFacets() {
        return facets;
    }

    public String getCleanQuery() {
        return cleanQuery;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

}
