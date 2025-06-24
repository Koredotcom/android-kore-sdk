package kore.botssdk.models;

public class BrandingOverrideConfigModel {
    private Boolean enable;
    private Boolean emoji_short_cut;
    private int typing_indicator_timeout;
    private BrandingOverrideHistoryModel history;

    public BrandingOverrideConfigModel updateWith(BrandingOverrideConfigModel configModel) {
        enable = configModel.enable != null ? configModel.enable : enable;
        emoji_short_cut = configModel.emoji_short_cut != null ? configModel.emoji_short_cut : emoji_short_cut;
        history = history != null && configModel.history != null ? history.updateWith(configModel.history) : history;
        return this;
    }

    public BrandingOverrideHistoryModel getHistory() {
        return history;
    }

    public int getTyping_indicator_timeout() {
        return typing_indicator_timeout;
    }

    public boolean isEnable() {
        return enable != null ? enable : false;
    }

    public boolean isEmoji_short_cut() {
        return emoji_short_cut != null ? emoji_short_cut : false;
    }
}
