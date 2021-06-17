package com.kore.findlysdk.models;

public class AutoSuggestionsModel
{
    private String requestId;
    private String originalQuery;
    private AutoCompleteModel autoComplete;

    public void setAutoComplete(AutoCompleteModel autoComplete) {
        this.autoComplete = autoComplete;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public AutoCompleteModel getAutoComplete() {
        return autoComplete;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public String getRequestId() {
        return requestId;
    }
}
