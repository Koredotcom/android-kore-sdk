package kore.botssdk.models;

/**
 * Created by Pradeep Mahato on 06-Jun-16.
 */
public class BotInfoModel {
    public String chatBot;
    public String taskBotId;

    public BotInfoModel(String chatBot, String taskBotId) {
        this.chatBot = chatBot;
        this.taskBotId = taskBotId;
    }

    public void setChatBot(String chatBot) {
        this.chatBot = chatBot;
    }

    public void setTaskBotId(String taskBotId) {
        this.taskBotId = taskBotId;
    }
}
