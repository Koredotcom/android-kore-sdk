package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 11-Mar-19.
 */

public class WUpcomingMeetingModel extends WidgetBaseDataModel {

    private CalEventsTemplateModel.Duration cursor = null;

    private List<WCalEventsTemplateModel> elements = null;

    public List<WidgetButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<WidgetButton> buttons) {
        this.buttons = buttons;
    }

    private List<WidgetButton> buttons = null;
    private WUpcomingTasksModel.Summary summary;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }


    public int getPreview_length() {
        return previewLength;
    }

    public void setPreview_length(int previewLength) {
        this.previewLength = previewLength;
    }

    private List<MultiAction> multi_actions = null;

    public List<WCalEventsTemplateModel> getElements() {
        return elements;
    }

    public void setElements(List<WCalEventsTemplateModel> elements) {
        this.elements = elements;
    }

    public WUpcomingTasksModel.Summary getSummary() {
        return summary;
    }

    public void setSummary(WUpcomingTasksModel.Summary summary) {
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

    public CalEventsTemplateModel.Duration getCursor() {
        return cursor;
    }

    public void setCursor(CalEventsTemplateModel.Duration cursor) {
        this.cursor = cursor;
    }
}
