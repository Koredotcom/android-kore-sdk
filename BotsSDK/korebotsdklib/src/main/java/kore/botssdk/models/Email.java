package kore.botssdk.models;

import java.util.List;

public class Email {
    private String name;
    private List<EmailItem> emailArray;

    public Email(String name, List emailArray){
        this.name = name;
        this.emailArray = emailArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmailItem> getEmailArray() {
        return emailArray;
    }

    public void setEmailArray(List<EmailItem> emailArray) {
        this.emailArray = emailArray;
    }
}