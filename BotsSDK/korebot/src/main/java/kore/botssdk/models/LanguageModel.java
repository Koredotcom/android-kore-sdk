package kore.botssdk.models;

public class LanguageModel {
    private String type;
    private String from;
    private BotInfoModel botInfo;
    private LanguageDetailsModel langDetails;

    public void setBotInfo(BotInfoModel botInfo) {
        this.botInfo = botInfo;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setLangDetails(LanguageDetailsModel langDetails) {
        this.langDetails = langDetails;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LanguageDetailsModel getLangDetails() {
        return langDetails;
    }

    public BotInfoModel getBotInfo() {
        return botInfo;
    }

    public String getFrom() {
        return from;
    }

    public String getType() {
        return type;
    }
}