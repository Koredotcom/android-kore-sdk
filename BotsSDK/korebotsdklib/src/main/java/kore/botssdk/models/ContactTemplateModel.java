package kore.botssdk.models;

public class ContactTemplateModel
{
    private String userIcon;
    private String userName;
    private String userContactNumber;
    private String userEmailId;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public String getUserName() {
        return userName;
    }
}
