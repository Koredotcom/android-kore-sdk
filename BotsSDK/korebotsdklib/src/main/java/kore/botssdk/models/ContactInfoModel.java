package kore.botssdk.models;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

import kore.botssdk.utils.Utils;

public class ContactInfoModel extends UserNameModel {

    private String contactUrl;
    private String id;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    private String color;
    private String title;
    private String email;
    private ArrayList<String> phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getPhoneNoByIndex(int index){
        if(phone != null && phone.size() > index){
            return phone.get(index);
        }
        return "";
    }
}