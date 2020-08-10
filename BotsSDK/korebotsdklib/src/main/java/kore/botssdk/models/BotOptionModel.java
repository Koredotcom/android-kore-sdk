package kore.botssdk.models;

import java.io.Serializable;

public class BotOptionModel implements Serializable
{
    private String title;
    private String icon;
    private BotOptionPostBackModel postback;

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setPostback(BotOptionPostBackModel postback)
    {
        this.postback = postback;
    }

    public BotOptionPostBackModel getPostback()
    {
        return postback;
    }
}
