package com.kore.findlysdk.models;

import java.io.Serializable;

public class ResultsViewMapping implements Serializable
{
    private String description;
    private String img;
    private String heading;

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getHeading() {
        return heading;
    }
}
