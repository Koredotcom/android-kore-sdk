package com.kore.findlysdk.models;

public class LiveSearchModel
{
    private String templateType;
    private String requestId;
    private LiveSearchTemplateModel template;
    private String title;
    private boolean isLeft = false;

    public String getTemplateType() {
        return templateType;
    }

    public LiveSearchTemplateModel getTemplate() {
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

    public void setTemplate(LiveSearchTemplateModel template) {
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
