package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingBodyIconModel implements Serializable {
    private Boolean show;
    private Boolean user_icon;
    private Boolean bot_icon;
    private Boolean agent_icon;

    public BrandingBodyIconModel updateWith(BrandingBodyIconModel configModel) {
        show = configModel.show != null ? configModel.show : show;
        user_icon = configModel.user_icon != null ? configModel.user_icon : user_icon;
        bot_icon = configModel.bot_icon != null ? configModel.bot_icon : bot_icon;
        agent_icon = configModel.agent_icon != null ? configModel.agent_icon : agent_icon;
        return this;
    }

    public boolean isShow() {
        return show != null ? show : false;
    }

    public boolean isAgent_icon() {
        return agent_icon;
    }

    public boolean isBot_icon() {
        return bot_icon;
    }

    public boolean isUser_icon() {
        return user_icon;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setBot_icon(boolean bot_icon) {
        this.bot_icon = bot_icon;
    }

    public void setAgent_icon(boolean agent_icon) {
        this.agent_icon = agent_icon;
    }

    public void setUser_icon(boolean user_icon) {
        this.user_icon = user_icon;
    }
}
