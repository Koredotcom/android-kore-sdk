package kore.botssdk.models;

import java.util.List;

import kore.botssdk.utils.StringUtils;

public class KoraSummaryHelpModel {

    private String title;
    private String text;
    private List<ButtonTemplate> body = null;

    public List<ButtonTemplate> getBody() {
        return body;
    }

    public void setBody(List<ButtonTemplate> body) {
        this.body = body;
    }

    private List<ButtonTemplate> buttons = null;

    public String getTitle() {
        if(!StringUtils.isNullOrEmpty(title))
        return title;
        else return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ButtonTemplate> getButtons() {
        if(body != null && body.size()>0)
        return body;
        else return buttons;
    }

    public void setButtons(List<ButtonTemplate> body) {
        this.body = body;
    }

}
