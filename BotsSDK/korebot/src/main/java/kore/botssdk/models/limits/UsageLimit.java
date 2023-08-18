package kore.botssdk.models.limits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsageLimit implements Serializable {
    @SerializedName("type")
    @Expose
    protected String type;
    @SerializedName("size")
    @Expose
    private int size;
    @SerializedName("used")
    @Expose
    private int used;
    @SerializedName("limit")
    @Expose
    private int limit;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
