package com.kore.findlysdk.models;

import java.util.ArrayList;

public class SearchWebHookPayloadModel
{
//    private ArrayList<PayloadOuter> text;
//
//    public void setText(ArrayList<PayloadOuter> text) {
//        this.text = text;
//    }
//
//    public ArrayList<PayloadOuter> getText() {
//        return text;
//    }

    private ArrayList<String> text;

    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    public ArrayList<String> getText() {
        return text;
    }
}
