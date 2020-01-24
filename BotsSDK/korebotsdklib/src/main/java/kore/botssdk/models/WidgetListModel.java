package kore.botssdk.models;

public class WidgetListModel {
    private String templateType;
    private String heading;
    private String text;

    public BotButtonModel getButtons() {
        return buttons;
    }

    public void setButtons(BotButtonModel buttons) {
        this.buttons = buttons;
    }

    private BotButtonModel buttons;

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public WidgetListElementModel getElements() {
        return elements;
    }

    public void setElements(WidgetListElementModel elements) {
        this.elements = elements;
    }

    private WidgetListElementModel elements;
}
