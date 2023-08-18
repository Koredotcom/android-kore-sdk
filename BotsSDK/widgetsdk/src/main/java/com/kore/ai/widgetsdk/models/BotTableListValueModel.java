package com.kore.ai.widgetsdk.models;

import java.io.Serializable;

public class BotTableListValueModel implements Serializable
{
    private String type;
    private String text;
    private BotTableListUrlModel url;
    private BotTableListLayoutModel layout;

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

    public void setLayout(BotTableListLayoutModel layout)
    {
        this.layout = layout;
    }

    public BotTableListLayoutModel getLayout()
    {
        return layout;
    }

    public static class BotTableListLayoutModel implements Serializable
    {
        private String align;
        private String colSize;
        private String color;

        public void setAlign(String align)
        {
            this.align = align;
        }

        public String getAlign()
        {
            return align;
        }

        public void setColSize(String colSize)
        {
            this.colSize = colSize;
        }

        public String getColSize()
        {
            return colSize;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

}
