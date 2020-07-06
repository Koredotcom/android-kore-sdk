package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 11-Mar-19.
 */

public class WUpcomingFilesModel extends WidgetBaseDataModel {

    private List<WFileLookUpModel> elements = null;
    private String summary;
    private List<MultiAction> multi_actions = null;

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
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


    public List<WidgetButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<WidgetButton> buttons) {
        this.buttons = buttons;
    }

    private List<WidgetButton> buttons = null;

    public List<WFileLookUpModel> getElements() {
        return elements;
    }

    public void setElements(List<WFileLookUpModel> elements) {
        this.elements = elements;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<MultiAction> getMultiActions() {
        return multi_actions;
    }

    public void setMultiActions(List<MultiAction> multiActions) {
        this.multi_actions = multiActions;
    }

    @Override
    protected void setWidgetType(int WIDGET_TYPE) {
        this.WIDGET_TYPE = WIDGET_TYPE;
    }
}
