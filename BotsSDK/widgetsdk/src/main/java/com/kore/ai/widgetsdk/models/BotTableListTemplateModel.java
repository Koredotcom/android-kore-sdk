package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class BotTableListTemplateModel
{
    public String templateType;
    public String title;
    public String description;
    public HeaderOptionsModel headerOptions;
    public ArrayList<BotTableListElementsModel> records;
    private String formLink;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHeaderOptions(HeaderOptionsModel headerOptions) {
        this.headerOptions = headerOptions;
    }

    public void setRecords(ArrayList<BotTableListElementsModel> records) {
        this.records = records;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<BotTableListElementsModel> getRecords() {
        return records;
    }

    public HeaderOptionsModel getHeaderOptions() {
        return headerOptions;
    }

    public String getDescription() {
        return description;
    }

    public String getTemplateType() {
        return templateType;
    }

    public String getFormLink() {
        return formLink;
    }

    public void setFormLink(String formLink) {
        this.formLink = formLink;
    }
}
