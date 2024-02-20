package kore.botssdk.models;

public class BrandingBodyModel {
    private BrandingBodyBackgroundModel background;
    private BrandingBodyFontModel font;
    private BrandingBodyUserMessageModel user_message;
    private BrandingBodyUserMessageModel bot_message;
    private BrandingBodyAgentMsgModel agent_message;
    private BrandingBodyTimeStampModel time_stamp;
    private BrandingBodyIconModel bodyIconModel;
    private String bubble_style;
    private String primaryColor;
    private String primaryHoverColor;
    private String secondaryColor;
    private String secondaryHoverColor;
    private String img;

    public String getImg() {
        return img;
    }

    public BrandingBodyAgentMsgModel getAgent_message() {
        return agent_message;
    }

    public BrandingBodyBackgroundModel getBackground() {
        return background;
    }

    public BrandingBodyFontModel getFont() {
        return font;
    }

    public BrandingBodyUserMessageModel getBot_message() {
        return bot_message;
    }

    public BrandingBodyIconModel getBodyIconModel() {
        return bodyIconModel;
    }

    public BrandingBodyTimeStampModel getTime_stamp() {
        return time_stamp;
    }

    public BrandingBodyUserMessageModel getUser_message() {
        return user_message;
    }

    public String getBubble_style() {
        return bubble_style;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getPrimaryHoverColor() {
        return primaryHoverColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public String getSecondaryHoverColor() {
        return secondaryHoverColor;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setAgent_message(BrandingBodyAgentMsgModel agent_message) {
        this.agent_message = agent_message;
    }

    public void setBackground(BrandingBodyBackgroundModel background) {
        this.background = background;
    }

    public void setBodyIconModel(BrandingBodyIconModel bodyIconModel) {
        this.bodyIconModel = bodyIconModel;
    }

    public void setBot_message(BrandingBodyUserMessageModel bot_message) {
        this.bot_message = bot_message;
    }

    public void setBubble_style(String bubble_style) {
        this.bubble_style = bubble_style;
    }

    public void setFont(BrandingBodyFontModel font) {
        this.font = font;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setPrimaryHoverColor(String primaryHoverColor) {
        this.primaryHoverColor = primaryHoverColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setSecondaryHoverColor(String secondaryHoverColor) {
        this.secondaryHoverColor = secondaryHoverColor;
    }

    public void setTime_stamp(BrandingBodyTimeStampModel time_stamp) {
        this.time_stamp = time_stamp;
    }

    public void setUser_message(BrandingBodyUserMessageModel user_message) {
        this.user_message = user_message;
    }
}
