package com.kore.ai.widgetsdk.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactInfoModel extends UserNameModel implements Serializable {

    private String contactUrl;
    private String id;
    private String emailId;

    private String manager;
    private String empId;
    private String department;
    private String address;

    public String getEmailId() {
        return emailId!=null?emailId:"";
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

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
    private String source;
    private ArrayList<String> phone;


    private List<Email> emails = null;
    private List<Phone> phones = null;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}