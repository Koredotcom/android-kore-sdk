package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 12-Apr-18.
 */

public class BotMiniTableModel {
    private List<List<String>> primary = null;
    private List<List<String>> additional = null;

    public List<List<String>> getPrimary() {
        return primary;
    }

    public void setPrimary(List<List<String>> primary) {
        this.primary = primary;
    }

    public List<List<String>> getAdditional() {
        return additional;
    }

    public void setAdditional(List<List<String>> additional) {
        this.additional = additional;
    }
}
