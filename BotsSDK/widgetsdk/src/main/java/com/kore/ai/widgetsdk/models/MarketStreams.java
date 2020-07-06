package com.kore.ai.widgetsdk.models;

/**
 * Created by Pradeep Mahato on 06-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class MarketStreams {

    private String _id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private String websiteUrl;
    private String lastModifiedBy;
    private String lastModifiedOn;
    private String createdOn;
    private boolean hasUserEndpoint;
    private int __v;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public boolean isHasUserEndpoint() {
        return hasUserEndpoint;
    }

    public int get__v() {
        return __v;
    }
}
