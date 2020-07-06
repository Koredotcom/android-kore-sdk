package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 30-Jan-19.
 */

public class TimeZoneModel {
    public String getTimeZoneName() {
        return timezonename;
    }

    public void setTimeZoneName(String timezonename) {
        this.timezonename = timezonename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getLocale_code() {
        return locale_code;
    }

    public void setLocale_code(String locale_code) {
        this.locale_code = locale_code;
    }

    private String timezonename;
    private String name;
    private String country_name;
    private String locale_code;
}
