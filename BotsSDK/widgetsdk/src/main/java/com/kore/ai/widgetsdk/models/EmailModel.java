package com.kore.ai.widgetsdk.models;

import com.kore.ai.widgetsdk.utils.DateUtils;

import java.util.ArrayList;

/**
 * Created by Shiva Krishna on 2/8/2018.
 */

public class EmailModel{
    ArrayList<BotCaourselButtonModel> buttons;
    ArrayList<KoreComponentModel> components;

    public ArrayList<BotCaourselButtonModel> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<BotCaourselButtonModel> buttons) {
        this.buttons = buttons;
    }

    public ArrayList<KoreComponentModel> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<KoreComponentModel> components) {
        this.components = components;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    private String from;
    private String[] to;
    private String[] cc;
    private String subject;
    private String desc;
    private String date;
    private String source;

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }

    private String[] attachments;
    public String getDateFormat(){
     return DateUtils.getDateFromStringByDate(date);
    }
}