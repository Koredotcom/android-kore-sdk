package kore.botssdk.models;

public class BotResponseMessagePayloadText {

    private String type;
    private ComponentModelPayloadText component;
    private BotResponseMessageComponentInfo cInfo;

    public void setType(String type) {
        this.type = type;
    }

    public ComponentModelPayloadText getComponent() {
        return component;
    }

    public void setComponent(ComponentModelPayloadText component) {
        this.component = component;
    }

    public void setcInfo(BotResponseMessageComponentInfo cInfo) {
        this.cInfo = cInfo;
    }



    public String getType() {
        return type;
    }

    public BotResponseMessageComponentInfo getcInfo() {
        return cInfo;
    }
}