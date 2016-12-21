package kore.botssdk.models;

import java.util.ArrayList;

/**
 * Created by Ramachandra Pradeep on 12/15/2016.
 */
public class PayloadInner {

    private String template_type;
    private String text;
    private ArrayList<ButtonTemplate> buttons;
    private ArrayList<QuickReplyTemplate> quick_replies;
    private ArrayList<ListTemplate> elements;

    public PayloadInner(){}

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<ButtonTemplate> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<ButtonTemplate> buttons) {
        this.buttons = buttons;
    }

    public void setQuick_replies(ArrayList<QuickReplyTemplate> quick_replies) {
        this.quick_replies = quick_replies;
    }

    public ArrayList<QuickReplyTemplate> getQuick_replies() {
        return quick_replies;
    }

    public void setElements(ArrayList<ListTemplate> elements) {
        this.elements = elements;
    }

    public ArrayList<ListTemplate> getElements() {
        return elements;
    }
}
