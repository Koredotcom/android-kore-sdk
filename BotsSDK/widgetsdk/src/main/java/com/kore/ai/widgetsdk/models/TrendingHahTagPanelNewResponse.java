package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendingHahTagPanelNewResponse extends WidgetBaseDataModel {

    @SerializedName("buttons")
    @Expose
    private List<Button> buttons = null;

    @SerializedName("elements")
    @Expose
    private List<TrendingHashTagModel> elements = null;

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public int getPreviewLength() {
        return previewLength;
    }

    public void setPreviewLength(int previewLength) {
        this.previewLength = previewLength;
    }

    public boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<TrendingHashTagModel> getElements() {
        return elements;
    }

    public void setElements(List<TrendingHashTagModel> elements) {
        this.elements = elements;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void setWidgetType(int WIDGET_TYPE) {

    }



}