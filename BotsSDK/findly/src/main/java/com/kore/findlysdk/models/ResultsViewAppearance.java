package com.kore.findlysdk.models;

import java.io.Serializable;

public class ResultsViewAppearance implements Serializable
{
    private String type;
    private String templateId;
    private ResultsViewTemplate template;

    public void setType(String type) {
        this.type = type;
    }

    public void setTemplate(ResultsViewTemplate template) {
        this.template = template;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getType() {
        return type;
    }

    public ResultsViewTemplate getTemplate() {
        return template;
    }

    public String getTemplateId() {
        return templateId;
    }
}
