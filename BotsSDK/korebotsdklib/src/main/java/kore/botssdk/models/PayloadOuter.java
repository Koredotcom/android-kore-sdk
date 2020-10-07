package kore.botssdk.models;

/**
 * Created by Ramachandra Pradeep on 12/15/2016.
 */
public class PayloadOuter {

    private String type;
    private PayloadInner payload;
    private String text;

    public boolean isEnableAattachment() {
        return enableAattachment;
    }

    public void setEnableAattachment(boolean enableAattachment) {
        this.enableAattachment = enableAattachment;
    }

    private boolean enableAattachment;

    public String getType() {
        return type;
    }

    public PayloadInner getPayload() {
        return payload;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(PayloadInner payload) {
        this.payload = payload;
    }
}
