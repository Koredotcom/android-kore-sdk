package com.kore.ai.widgetsdk.models;

import java.io.Serializable;

public class BotTableListTitleModel implements Serializable
{
    private String type;
    private String rowColor;
    private BotTableListTextModel text;
    private ImageModel image;
    private BotTableListUrlModel url;

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setRowColor(String rowColor)
    {
        this.rowColor = rowColor;
    }

    public String getRowColor()
    {
        return rowColor;
    }

    public void setImage(ImageModel image)
    {
        this.image = image;
    }

    public ImageModel getImage()
    {
        return image;
    }

    public void setText(BotTableListTextModel text)
    {
        this.text = text;
    }

    public BotTableListTextModel getText()
    {
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


    public class BotTableListTextModel
    {
        private String title;
        private String subtitle;

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return title;
        }

        public void setSubtitle(String subtitle)
        {
            this.subtitle = subtitle;
        }

        public String getSubtitle()
        {
            return subtitle;
        }
    }
}
