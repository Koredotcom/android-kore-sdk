
package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KnowledgeDetailModelResponseNew extends WidgetBaseDataModel{


    private Object params;

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public Widget.Hook getHook() {
        return hook;
    }

    public void setHook(Widget.Hook hook) {
        this.hook = hook;
    }

    private Widget.Hook hook;


    @SerializedName("buttons")
    @Expose
    private List<Button> buttons = null;

    @SerializedName("elements")
    @Expose
    private List<KnowledgeDetailModel> elements = null;


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

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<KnowledgeDetailModel> getElements() {
        return elements;
    }

    public void setElements(List<KnowledgeDetailModel> elements) {
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
