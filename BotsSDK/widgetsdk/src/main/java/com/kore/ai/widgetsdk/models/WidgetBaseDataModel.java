package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public abstract class WidgetBaseDataModel {

    public int getWIDGET_TYPE() {
        return WIDGET_TYPE;
    }

    public void setWIDGET_TYPE(int WIDGET_TYPE) {
        this.WIDGET_TYPE = WIDGET_TYPE;
    }

    public int getPreview_length() {
        return previewLength;
    }

    public void setPreview_length(int previewLength) {
        this.previewLength = previewLength;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    protected  int WIDGET_TYPE;

    protected int previewLength;
    protected boolean hasMore;
    protected String placeholder;

    protected abstract void setWidgetType(int WIDGET_TYPE);
}
