package kore.botssdk.models;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotButtonModel implements MultiSelectBase {
    String type;
    String url;
    String title;
    String payload;
    String name;
    BotOptionPostBackModel botOptionPostBackModel;
    private String btnType;
    private String label;

    public boolean isIs_actionable() {
        return is_actionable;
    }

    public void setIs_actionable(boolean is_actionable) {
        this.is_actionable = is_actionable;
    }

    boolean is_actionable;
    public HashMap<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(HashMap<String, Object> customData) {
        this.customData = customData;
    }

    HashMap<String,Object> customData;
    private boolean isSamePageNavigation;

    public boolean isSamePageNavigation() {
        return isSamePageNavigation;
    }

    public void setSamePageNavigation(boolean samePageNavigation) {
        isSamePageNavigation = samePageNavigation;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    String action;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    private ImageModel image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BotOptionPostBackModel getBotOptionPostBackModel() {
        return botOptionPostBackModel;
    }

    public void setBotOptionPostBackModel(BotOptionPostBackModel botOptionPostBackModel) {
        this.botOptionPostBackModel = botOptionPostBackModel;
    }

    public String getLabel() {
        return label;
    }

    public String getBtnType() {
        return btnType;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setBtnType(String btnType) {
        this.btnType = btnType;
    }
}
