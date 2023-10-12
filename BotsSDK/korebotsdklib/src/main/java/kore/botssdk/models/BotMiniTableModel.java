package kore.botssdk.models;

import java.util.List;

public class BotMiniTableModel {
    private List<List<String>> primary = null;
    private List<List<Object>> additional = null;

    public List<List<String>> getPrimary() {
        return primary;
    }

    public void setPrimary(List<List<String>> primary) {
        this.primary = primary;
    }

    public List<List<Object>> getAdditional() {
        return additional;
    }

    public void setAdditional(List<List<Object>> additional) {
        this.additional = additional;
    }


}
