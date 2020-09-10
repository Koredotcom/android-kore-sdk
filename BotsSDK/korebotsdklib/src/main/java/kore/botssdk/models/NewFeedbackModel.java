package kore.botssdk.models;

import java.util.ArrayList;

public class NewFeedbackModel
{
    private String text;
    private String template_type;
    private String view;
    private String sliderView;
    private String[] starArrays;
    private ArrayList<FeedbackSmileyModel> smileyArrays;
    private String messageTodisplay;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<FeedbackSmileyModel> getSmileyArrays() {
        return smileyArrays;
    }

    public void setSmileyArrays(ArrayList<FeedbackSmileyModel> smileyArrays) {
        this.smileyArrays = smileyArrays;
    }

    public void setMessageTodisplay(String messageTodisplay) {
        this.messageTodisplay = messageTodisplay;
    }

    public void setSliderView(String sliderView) {
        this.sliderView = sliderView;
    }

    public void setStarArrays(String[] starArrays) {
        this.starArrays = starArrays;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getMessageTodisplay() {
        return messageTodisplay;
    }

    public String getSliderView() {
        return sliderView;
    }

    public String getTemplate_type() {
        return template_type;
    }

    public String getView() {
        return view;
    }

    public String[] getStarArrays() {
        return starArrays;
    }

}
