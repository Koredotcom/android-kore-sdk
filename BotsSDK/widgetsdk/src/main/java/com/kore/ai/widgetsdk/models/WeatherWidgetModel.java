package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    public class Header {

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
/*   public class Element {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("weather")
        @Expose
        private Weather weather;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("iconId")
        @Expose
        private String iconId;
        @SerializedName("payload")
        @Expose
        private String payload;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIconId() {
            return iconId;
        }

        public void setIconId(String iconId) {
            this.iconId = iconId;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }


}*/


}