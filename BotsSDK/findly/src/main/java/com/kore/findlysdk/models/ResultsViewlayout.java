package com.kore.findlysdk.models;

import java.io.Serializable;

public class ResultsViewlayout implements Serializable
{
    private String layoutType;
    private boolean isClickable;
    private String behaviour;
    private String textAlignment;

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }

    public void setIsClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public void setTextAlignment(String textAlignment) {
        this.textAlignment = textAlignment;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public boolean getIsClickable() {
        return isClickable;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public String getTextAlignment() {
        return textAlignment;
    }
}
