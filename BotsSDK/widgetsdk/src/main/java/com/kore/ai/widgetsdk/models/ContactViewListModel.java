package com.kore.ai.widgetsdk.models;

public class ContactViewListModel {
    private String header;
    private String value;
    private String image;
    private boolean isPhone;
    private boolean isEmail;
    private boolean isAddress;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPhone() {
        return isPhone;
    }

    public void setPhone(boolean phone) {
        this.isPhone = phone;
    }

    public boolean isEmail() {
        return isEmail;
    }

    public void setEmail(boolean email) {
        this.isEmail = email;
    }

    public boolean isAddress() {
        return isAddress;
    }

    public void setAddress(boolean address) {
        this.isAddress = address;
    }
}
