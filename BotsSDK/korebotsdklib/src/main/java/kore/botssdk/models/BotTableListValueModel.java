package kore.botssdk.models;

import java.io.Serializable;

public class BotTableListValueModel implements Serializable
{
    private String type;
    private String text;
    private BotTableListUrlModel url;
    private BotListLayoutModel layout;

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setUrl(BotTableListUrlModel url)
    {
        this.url = url;
    }

    public BotTableListUrlModel getUrl()
    {
        return url;
    }

    public void setLayout(BotListLayoutModel layout)
    {
        this.layout = layout;
    }

    public BotListLayoutModel getLayout()
    {
        return layout;
    }


}
