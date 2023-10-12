package kore.botssdk.models;

import java.util.HashMap;

public class FormActionTemplate {



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

    public HashMap<String,Object> getCustomData() {
        return customData;
    }

    public void setCustomData(HashMap<String,Object> customData) {
        this.customData = customData;
    }


    private String title;
    private String form_name;
    private HashMap<String,Object> customData;


}
