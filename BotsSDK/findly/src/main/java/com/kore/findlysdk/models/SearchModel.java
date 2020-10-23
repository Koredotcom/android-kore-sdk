package com.kore.findlysdk.models;

import java.io.Serializable;

public class SearchModel implements Serializable
{
    private String templateType;
    private String requestId;
    private SearchTemplateModel template;
    private String title;
    private boolean isLeft = false;


    public String getTemplateType() {
        return templateType;
    }

    public SearchTemplateModel getTemplate() {
        return template;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setTemplate(SearchTemplateModel template) {
        this.template = template;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public boolean getLeft() {
        return isLeft;
    }

}
