package kore.botssdk.models;

import java.util.ArrayList;

import kore.botssdk.utils.StringUtils;

public class WidgetListModel {
    private String templateType;
    private String heading;
    private String text;
    private HeaderOptionsModel headerOptions;

    public LoginModel getLoginModel() {
        return login;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.login = loginModel;
    }

    private LoginModel login;

    public HeaderOptionsModel getHeaderOptions() {
        return headerOptions;
    }

    public void setHeaderOptions(HeaderOptionsModel headerOptions) {
        this.headerOptions = headerOptions;
    }



    public String getTemplateType() {
        if(!StringUtils.isNullOrEmpty(templateType)) {
            return templateType;
        }
        return "";
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<WidgetListElementModel> getElements() {
        return elements;
    }

    public void setElements(ArrayList<WidgetListElementModel> elements) {
        this.elements = elements;
    }

    private ArrayList<WidgetListElementModel> elements;
}
