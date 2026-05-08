package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

public class AcknowledgeModel {
    private String type;
    private boolean ok;
    private long replyto;
    @SerializedName("k-traceid")
    private String kTraceId;

    public long getReplyto() {
        return replyto;
    }

    public String getType() {
        return type;
    }
}