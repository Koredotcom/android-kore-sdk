package kore.botssdk.models;

import java.io.Serializable;

public class BotOptionPostBackModel implements Serializable
{
    private String title;
    private String value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String type) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
