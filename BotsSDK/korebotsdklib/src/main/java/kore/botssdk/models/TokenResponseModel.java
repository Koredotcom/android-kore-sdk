package kore.botssdk.models;

public class TokenResponseModel {
    private String jwt;
    private String koreAPIUrl;
    private BotInfoModel botInfo;

    public void setBotInfo(BotInfoModel botInfo) {
        this.botInfo = botInfo;
    }

    public BotInfoModel getBotInfo() {
        return botInfo;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setKoreAPIUrl(String koreAPIUrl) {
        this.koreAPIUrl = koreAPIUrl;
    }

    public String getKoreAPIUrl() {
        return koreAPIUrl;
    }
}
