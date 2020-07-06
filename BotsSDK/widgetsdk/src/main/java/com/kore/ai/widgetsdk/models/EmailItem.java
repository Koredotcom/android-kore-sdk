package com.kore.ai.widgetsdk.models;

public class EmailItem {
    private String type;
    private String value;

    public EmailItem(String type,String value){
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return value;
    }

    public void setNumber(String number) {
        this.value = number;
    }
}
