package com.kore.ai.widgetsdk.models;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class WTaskTemplateModel {
    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskTemplateData getData() {
        return data;
    }

    public void setData(TaskTemplateData data) {
        this.data = data;
    }

    public List<CalEventsTemplateModel.Action> getActions() {
        return actions;
    }

    public void setActions(List<CalEventsTemplateModel.Action> actions) {
        this.actions = actions;
    }

    private String template_type;
    private String title;
    private String id;

    private TaskTemplateData data;
    private List<CalEventsTemplateModel.Action> actions = null;

    public boolean isOverDue(){
        return  "open".equalsIgnoreCase(getData().getStatus()) && Calendar.getInstance().getTimeInMillis() > getData().getDueDate();
    }
}
