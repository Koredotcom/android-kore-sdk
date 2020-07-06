package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 12/15/2016.
 */
public class ButtonTemplate {

    private String type;
    private String title;
    private String payload;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ButtonTemplate(){}

}
