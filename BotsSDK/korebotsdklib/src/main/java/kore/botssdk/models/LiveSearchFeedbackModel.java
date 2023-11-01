package kore.botssdk.models;

import java.io.Serializable;

public class LiveSearchFeedbackModel implements Serializable
{
    private int thumbsup;
    private int thumbsdown;
    private int click;
    private int expand;
    private int appearance;

    public int getAppearance() {
        return appearance;
    }

    public int getClick() {
        return click;
    }

    public int getExpand() {
        return expand;
    }

    public int getThumbsdown() {
        return thumbsdown;
    }

    public int getThumbsup() {
        return thumbsup;
    }

    public void setAppearance(int appearance) {
        this.appearance = appearance;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public void setExpand(int expand) {
        this.expand = expand;
    }

    public void setThumbsdown(int thumbsdown) {
        this.thumbsdown = thumbsdown;
    }

    public void setThumbsup(int thumbsup) {
        this.thumbsup = thumbsup;
    }
}
