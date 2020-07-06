package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ramachandra Pradeep on 08-Mar-19.
 */

public class Filter implements Serializable {

    private String id;
    private String title;
//    private String api;
    @SerializedName("hook")
    @Expose
    private Widget.Hook hook;


    @SerializedName("default")
    @Expose
    private String _default;
    @SerializedName("type")
    @Expose
    private String type;

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Widget.Hook getHook() {
        return hook;
    }

    public void setHook(Widget.Hook hook) {
        this.hook = hook;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getApi() {
//        return api;
//    }
//
//    public void setApi(String api) {
//        this.api = api;
//    }

}
