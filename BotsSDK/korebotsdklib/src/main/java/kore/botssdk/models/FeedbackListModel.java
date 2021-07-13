package kore.botssdk.models;

public class FeedbackListModel
{
    private String id;
    private String value;
    private boolean isChecked;

    public void setValue(String value) {
        this.value = value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean getChecked()
    {
        return isChecked;
    }
}
