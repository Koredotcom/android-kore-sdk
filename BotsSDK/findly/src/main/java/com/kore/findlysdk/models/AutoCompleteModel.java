package com.kore.findlysdk.models;

import java.util.ArrayList;

public class AutoCompleteModel
{
    private ArrayList<String> querySuggestions;
    private ArrayList<String> typeAheads;

    public ArrayList<String> getQuerySuggestions() {
        return querySuggestions;
    }

    public ArrayList<String> getTypeAheads() {
        return typeAheads;
    }

    public void setQuerySuggestions(ArrayList<String> querySuggestions) {
        this.querySuggestions = querySuggestions;
    }

    public void setTypeAheads(ArrayList<String> typeAheads) {
        this.typeAheads = typeAheads;
    }
}
