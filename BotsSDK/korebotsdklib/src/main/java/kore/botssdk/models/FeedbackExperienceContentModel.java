package kore.botssdk.models;

public class FeedbackExperienceContentModel
{
    private String id;
    private String value;
    private String empathyMessage;
    private boolean isChecked;

    public String getEmpathyMessage() {
        return empathyMessage;
    }

    public void setEmpathyMessage(String empathyMessage) {
        this.empathyMessage = empathyMessage;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean getChecked()
    {
        return isChecked;
    }
}
