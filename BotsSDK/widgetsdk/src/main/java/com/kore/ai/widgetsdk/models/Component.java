package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 03-Oct-18.
 */

public class Component {
    private String id;
    private String cT;
    private Data data;
    private List<Object> thumbnails = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCT() {
        return cT;
    }

    public void setCT(String cT) {
        this.cT = cT;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Object> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Object> thumbnails) {
        this.thumbnails = thumbnails;
    }
}
