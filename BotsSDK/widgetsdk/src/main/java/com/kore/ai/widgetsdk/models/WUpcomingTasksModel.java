package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 11-Mar-19.
 */

public class WUpcomingTasksModel extends WidgetBaseDataModel {



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


    private List<WTaskTemplateModel> elements = null;

    public List<WidgetButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<WidgetButton> buttons) {
        this.buttons = buttons;
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

    private List<WidgetButton> buttons = null;
    private Summary summary;

    private ArrayList<MultiAction> multi_actions = null;

    public List<WTaskTemplateModel> getElements() {
        return elements;
    }

    public void setElements(List<WTaskTemplateModel> elements) {
        this.elements = elements;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public ArrayList<MultiAction> getMultiActions() {
        return multi_actions;
    }

    public void setMultiActions(ArrayList<MultiAction> multiActions) {
        this.multi_actions = multiActions;
    }

    @Override
    protected void setWidgetType(int WIDGET_TYPE) {
        this.WIDGET_TYPE = WIDGET_TYPE;
    }

    public class Summary{
        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIconId() {
            return iconId;
        }

        public void setIconId(String iconId) {
            this.iconId = iconId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        private String icon;
        private String iconId;
        private String text;
    }
}
