package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchTemplateModel implements Serializable
{
    private String originalQuery;
    private String cleanQuery;
    private LiveSearchFacetsModel facets;
    private SearchResultsModel results;
    private SearchWebHookPayloadModel webhookPayload;
    private ArrayList<SearchFacetsModel> searchFacets;


    public void setCleanQuery(String cleanQuery) {
        this.cleanQuery = cleanQuery;
    }

    public void setFacets(LiveSearchFacetsModel facets) {
        this.facets = facets;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
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

    public void setResults(SearchResultsModel results) {
        this.results = results;
    }

    public SearchResultsModel getResults() {
        return results;
    }

    public void setWebhookPayload(SearchWebHookPayloadModel webhookPayload) {
        this.webhookPayload = webhookPayload;
    }

    public SearchWebHookPayloadModel getWebhookPayload() {
        return webhookPayload;
    }

    public void setSearchFacets(ArrayList<SearchFacetsModel> searchFacets) {
        this.searchFacets = searchFacets;
    }

    public ArrayList<SearchFacetsModel> getSearchFacets() {
        return searchFacets;
    }
}
