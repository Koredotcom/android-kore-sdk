package kore.botssdk.models;

public class RadioOptionModel {
    private String title;
    private String value;
    private BotOptionPostBackModel postback;

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public BotOptionPostBackModel getPostback() {
        return postback;
    }
}
