package kore.botssdk.models;

/**
 * Created by Ramachandra Pradeep on 12/15/2016.
 */
public class PayloadOuter {

    private String type;
    private PayloadInner payload;

    public PayloadOuter(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PayloadInner getPayload() {
        return payload;
    }

    public void setPayload(PayloadInner payload) {
        this.payload = payload;
    }

}