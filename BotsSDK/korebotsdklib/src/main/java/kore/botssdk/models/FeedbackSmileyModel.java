package kore.botssdk.models;

public class FeedbackSmileyModel
{
    private int smileyId;
    private Object value;

    public int getSmileyId() {
        return smileyId;
    }

    public void setSmileyId(int smileyId) {
        this.smileyId = smileyId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
