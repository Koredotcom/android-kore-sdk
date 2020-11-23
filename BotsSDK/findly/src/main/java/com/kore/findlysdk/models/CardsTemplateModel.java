package com.kore.findlysdk.models;

public class CardsTemplateModel
{
    private String biller_name;
    private String card_type;
    private String card_number;
    private String bill_amount;
    private String due_date;
    private String postback_value;

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public void setBiller_name(String biller_name) {
        this.biller_name = biller_name;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public void setPostback_value(String postback_value) {
        this.postback_value = postback_value;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public String getBiller_name() {
        return biller_name;
    }

    public String getCard_number() {
        return card_number;
    }

    public String getCard_type() {
        return card_type;
    }

    public String getDue_date() {
        return due_date;
    }

    public String getPostback_value() {
        return postback_value;
    }
}
