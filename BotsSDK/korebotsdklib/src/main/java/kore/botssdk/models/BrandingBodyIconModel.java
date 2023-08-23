package kore.botssdk.models;

public class BrandingBodyIconModel {
    private boolean show;
    private boolean user_icon;
    private boolean bot_icon;
    private boolean agent_icon;

    public boolean isShow() {
        return show;
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
