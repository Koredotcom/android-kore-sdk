package com.kore.ai.widgetsdk.models;


import java.util.Calendar;
import java.util.List;

public class TaskTemplateModel {
    private String title;
    private String status;
    private String id;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<CalEventsTemplateModel.Action> getActions() {
        return actions;
    }

    public void setActions(List<CalEventsTemplateModel.Action> actions) {
        this.actions = actions;
    }

    private List<CalEventsTemplateModel.Action> actions = null;


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

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
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

    private long dueDate;
    private UserNameModel assignee;
    private UserNameModel owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOverDue(){
     return  "open".equalsIgnoreCase(status) && Calendar.getInstance().getTimeInMillis() > dueDate;
    }
}

