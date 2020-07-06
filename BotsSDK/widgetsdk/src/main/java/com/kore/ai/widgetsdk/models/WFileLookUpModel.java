package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class WFileLookUpModel {
    private String template_type;
    private String title;
    private String icon;
    private String iconId;
    private String sub_title;

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public DefaultAction getDefault_action() {
        return default_action;
    }

    public void setDefault_action(DefaultAction default_action) {
        this.default_action = default_action;
    }

    public FileLookUpData getData() {
        return data;
    }

    public void setData(FileLookUpData data) {
        this.data = data;
    }

    private DefaultAction default_action;
    private FileLookUpData data;


    public class DefaultAction{
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String type;
        private String url;
    }
}
