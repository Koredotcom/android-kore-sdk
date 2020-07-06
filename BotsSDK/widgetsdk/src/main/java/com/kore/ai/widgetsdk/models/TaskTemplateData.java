package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class TaskTemplateData {
    private String lN;
    private String fN;

    private String creator;
    private long cOn;
    private long lMod;

    public String getlN() {
        return lN;
    }

    public void setlN(String lN) {
        this.lN = lN;
    }

    public String getfN() {
        return fN;
    }

    public void setfN(String fN) {
        this.fN = fN;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getcOn() {
        return cOn;
    }

    public void setcOn(long cOn) {
        this.cOn = cOn;
    }

    public long getlMod() {
        return lMod;
    }

    public void setlMod(long lMod) {
        this.lMod = lMod;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public String getlMUId() {
        return lMUId;
    }

    public void setlMUId(String lMUId) {
        this.lMUId = lMUId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TaskAssignee getAssignee() {
        return assignee;
    }

    public void setAssignee(TaskAssignee assignee) {
        this.assignee = assignee;
    }

    public TaskOwner getOwner() {
        return owner;
    }

    public void setOwner(TaskOwner owner) {
        this.owner = owner;
    }

    private long dueDate;

    private String lMUId;
    private String orgId;
    private String status;

    private TaskAssignee assignee;
    private TaskOwner owner;

}
