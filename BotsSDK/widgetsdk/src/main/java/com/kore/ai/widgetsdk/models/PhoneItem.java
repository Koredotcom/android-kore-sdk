package com.kore.ai.widgetsdk.models;

public class PhoneItem {
    private String type;
    private String value;

    public PhoneItem(String type,String value){
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
