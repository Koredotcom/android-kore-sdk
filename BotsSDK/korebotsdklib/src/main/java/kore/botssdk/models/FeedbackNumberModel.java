package kore.botssdk.models;

public class FeedbackNumberModel {
    int numberId;
    int value;
    String color;

    boolean isSelected;

    public int getNumberId() {
        return numberId;
    }

    public int getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
