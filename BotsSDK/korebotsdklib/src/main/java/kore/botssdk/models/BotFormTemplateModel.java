package kore.botssdk.models;

public class BotFormTemplateModel
{
    String label;
    String type;
    String placeholder;
    BotFormFieldButtonModel fieldButton;
    String password;

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public String getPlaceHolder() {
        return placeholder;
    }

    public BotFormFieldButtonModel getFieldButton() {
        return fieldButton;
    }

    public String getPassword() {
        return password;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPlaceHolder() {
        this.placeholder = placeholder;
    }

    public void setFieldButton(BotFormFieldButtonModel fieldButton) {
        this.fieldButton = fieldButton;
    }

}
