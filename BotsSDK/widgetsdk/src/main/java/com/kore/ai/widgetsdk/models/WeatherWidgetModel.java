package com.kore.ai.widgetsdk.models;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressLint("UnknownNullness")
public class WeatherWidgetModel {

    @SerializedName("header")
    @Expose
    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @SerializedName("elements")
@Expose
private List<ActionItem> elements = null;

public List<ActionItem> getElements() {
return elements;



}

public void setElements(List<ActionItem> elements) {
this.elements = elements;
}
    public static class Header {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("weather")
        @Expose
        private Weather weather;

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

        public Weather getWeather() {
            return weather;
        }

        public void setWeather(Weather weather) {
            this.weather = weather;
        }

    }

}