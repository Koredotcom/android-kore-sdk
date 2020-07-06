package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

/**
 * Created by Ramachandra Pradeep on 08-Mar-19.
 */

public class Header {

    private String title;
    private String message;
    private String refreshInterval;
    private String cacheInterval;
    private Weather weather;
    private String api;

    public ArrayList<String> getQuery() {
        return query;
    }

    public void setQuery(ArrayList<String> query) {
        this.query = query;
    }

    private ArrayList<String> query;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(String refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public String getCacheInterval() {
        return cacheInterval;
    }

    public void setCacheInterval(String cacheInterval) {
        this.cacheInterval = cacheInterval;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

}
