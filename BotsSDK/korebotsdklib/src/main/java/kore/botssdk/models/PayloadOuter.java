package kore.botssdk.models;

/**
 * Created by Ramachandra Pradeep on 12/15/2016.
 */
public class PayloadOuter {

    private String type;
    private PayloadInner payload;

    public String getType() {
        return type;
    }

    public PayloadInner getPayload() {
        return payload;
    }
}