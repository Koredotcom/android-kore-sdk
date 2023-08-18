package kore.botssdk.models;

import java.io.Serializable;

public class LiveSearchPostBackPayloadModel implements Serializable
{
    private String type;
    private String payload;

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
