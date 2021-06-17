package com.kore.findlysdk.models;

/**
 * Created by Ramachandra Pradeep on 12/15/2016.
 */
public class PayloadOuter {

    private String type;
    private PayloadInner payload;
    private String text;
    private SearchTemplateModel template;
    private String templateType;

    public void setTemplate(SearchTemplateModel template) {
        this.template = template;
    }

    public SearchTemplateModel getTemplate() {
        return template;
    }

    public String getType() {
        return type;
    }

    public PayloadInner getPayload() {
        return payload;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(PayloadInner payload) {
        this.payload = payload;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateType() {
        return templateType;
    }
}
