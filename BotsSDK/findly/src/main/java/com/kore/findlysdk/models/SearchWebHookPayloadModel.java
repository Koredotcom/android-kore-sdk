package com.kore.findlysdk.models;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchWebHookPayloadModel
{
    private Object text;
    private boolean endOfTask;
    private String endReason;
    private String completedTaskId;
    private String completedTaskName;

    public void setText(Object text) {
        this.text = text;
    }

    public Object getText() {
        return text;
    }

    public void setCompletedTaskId(String completedTaskId) {
        this.completedTaskId = completedTaskId;
    }

    public void setCompletedTaskName(String completedTaskName) {
        this.completedTaskName = completedTaskName;
    }

    public void setEndOfTask(boolean endOfTask) {
        this.endOfTask = endOfTask;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    public String getCompletedTaskId() {
        return completedTaskId;
    }

    public String getCompletedTaskName() {
        return completedTaskName;
    }

    public String getEndReason() {
        return endReason;
    }

    public boolean getEndOfTask()
    {
        return endOfTask;
    }
}
