package kore.botssdk.models;

import java.io.Serializable;

public class ResultsViewlayout implements Serializable
{
    private boolean renderTitle;
    private String layoutType;
    private boolean isClickable;
    private String behaviour;
    private String textAlignment;
    private String title;
    private String listType;

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

    public String getTitle() {
        return title;
    }

    public String getListType() {
        return listType;
    }

    public boolean getRenderTitle() {
        return renderTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public void setRenderTitle(boolean renderTitle) {
        this.renderTitle = renderTitle;
    }
}
