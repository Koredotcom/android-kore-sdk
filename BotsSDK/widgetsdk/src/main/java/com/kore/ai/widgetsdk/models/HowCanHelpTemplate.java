package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HowCanHelpTemplate {




    @SerializedName("template_type")
    @Expose
    private String templateType;
    @SerializedName("elements")
    @Expose
    private List<Element> elements = null;
    @SerializedName("isNewVolley")
    @Expose
    private Boolean isNewVolley;

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public Boolean getIsNewVolley() {
        return isNewVolley;
    }

    public void setIsNewVolley(Boolean isNewVolley) {
        this.isNewVolley = isNewVolley;
    }

     public static class Element {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("buttons")
        @Expose
        private List<Button> buttons = null;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Button> getButtons() {
            return buttons;
        }

        public void setButtons(List<Button> buttons) {
            this.buttons = buttons;
        }

    }

    public static class Button {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("payload")
        @Expose
        private String payload;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

    }
}