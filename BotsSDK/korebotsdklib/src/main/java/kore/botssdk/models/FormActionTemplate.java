package kore.botssdk.models;

/**
 * Created by Ramachandra Pradeep on 09-Feb-18.
 */

public class FormActionTemplate {

    public String getActionType() {
        return action_type;
    }

    public void setActionType(String action_type) {
        this.action_type = action_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormName() {
        return form_name;
    }

    public void setFormName(String form_name) {
        this.form_name = form_name;
    }

    public CustomData getCustomData() {
        return customData;
    }

    public void setCustomData(CustomData customData) {
        this.customData = customData;
    }

    private String action_type;
    private String title;
    private String form_name;
    private CustomData customData;


}
