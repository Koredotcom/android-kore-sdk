package kore.botssdk.models;

public class BrandingOverrideConfigModel {
    private boolean enable;
    private boolean emoji_short_cut;
    private int typing_indicator_timeout;
    private BrandingOverrideHistoryModel history;

    public BrandingOverrideHistoryModel getHistory() {
        return history;
    }

    public int getTyping_indicator_timeout() {
        return typing_indicator_timeout;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean isEmoji_short_cut() {
        return emoji_short_cut;
    }
}
