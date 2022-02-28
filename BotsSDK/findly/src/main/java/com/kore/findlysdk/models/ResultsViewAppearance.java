package com.kore.findlysdk.models;

import java.io.Serializable;

public class ResultsViewAppearance implements Serializable
{
    private String op;
    private String fieldValue;
    private String templateId;
    private ResultsViewTemplate template;

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public String getOp() {
        return op;
    }

    public void setTemplate(ResultsViewTemplate template) {
        this.template = template;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public ResultsViewTemplate getTemplate() {
        return template;
    }

    public String getTemplateId() {
        return templateId;
    }
}
