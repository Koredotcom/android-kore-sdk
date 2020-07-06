package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 26-Feb-18.
 */

public class AutoSuggestions {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String type;
    private int threshold;
    private boolean enabled;
    private String url;



}
