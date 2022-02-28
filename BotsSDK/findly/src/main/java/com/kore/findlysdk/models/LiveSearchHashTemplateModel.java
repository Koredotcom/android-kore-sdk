package com.kore.findlysdk.models;

import java.util.ArrayList;
import java.util.HashMap;

public class LiveSearchHashTemplateModel {
    private String originalQuery;
    private String cleanQuery;
    private ArrayList<LiveSearchFacetsModel> facets;
    private HashMap<String , Object> results;

    public ArrayList<LiveSearchFacetsModel> getFacets() {
        return facets;
    }

    public HashMap<String, Object> getResults() {
        return results;
    }

    public String getCleanQuery() {
        return cleanQuery;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public void setCleanQuery(String cleanQuery) {
        this.cleanQuery = cleanQuery;
    }

    public void setFacets(ArrayList<LiveSearchFacetsModel> facets) {
        this.facets = facets;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    public void setResults(HashMap<String, Object> results) {
        this.results = results;
    }
}
