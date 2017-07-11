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

    public String getTemplate_type() {
        return template_type;
    }

    public String getText() {
        return text;
    }

    public ArrayList<ButtonTemplate> getButtons() {
        return buttons;
    }

    public ArrayList<QuickReplyTemplate> getQuick_replies() {
        return quick_replies;
    }

    public ArrayList<ListTemplate> getElements() {
        return elements;
    }

    public ArrayList<ButtonTemplate> convertQuickReplyToButton(ArrayList<QuickReplyTemplate> quick_replies) {
        ArrayList<ButtonTemplate> buttonTemplates = new ArrayList<>();
        if (quick_replies != null && quick_replies.size() > 0) {
            buttonTemplates = new ArrayList<>(quick_replies.size());
            for (QuickReplyTemplate template : quick_replies) {
                ButtonTemplate buttonTemplate = new ButtonTemplate();
                buttonTemplate.setTitle(template.getTitle());
                buttonTemplate.setPayload(template.getPayload());
                buttonTemplate.setType(template.getContent_type());
                buttonTemplates.add(buttonTemplate);
            }

        }
        return buttonTemplates;
    }

}
