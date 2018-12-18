package kore.botssdk.models;

import java.util.ArrayList;

public class TaskTemplateResponse {
    private ArrayList<TaskTemplateModel> taskData;

    public void setTaskData(ArrayList<TaskTemplateModel> taskData) {
        this.taskData = taskData;
    }

    public void setButtons(ArrayList<BotButtonModel> buttons) {
        this.buttons = buttons;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    private ArrayList<BotButtonModel> buttons;
    private boolean showButton;

    public ArrayList<TaskTemplateModel> getTaskData() {
        return taskData;
    }

    public ArrayList<BotButtonModel> getButtons() {
        return buttons;
    }

    public boolean isShowButton() {
        return showButton;
    }
}
