package kore.botssdk.models;


public class TaskTemplateModel {
    private String title;
    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public UserNameModel getAssignee() {
        return assignee;
    }

    public void setAssignee(UserNameModel assignee) {
        this.assignee = assignee;
    }

    public UserNameModel getOwner() {
        return owner;
    }

    public void setOwner(UserNameModel owner) {
        this.owner = owner;
    }

    private String dueDate;
    private UserNameModel assignee;
    private UserNameModel owner;

}
