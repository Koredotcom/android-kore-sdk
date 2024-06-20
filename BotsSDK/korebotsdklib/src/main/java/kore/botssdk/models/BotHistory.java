package kore.botssdk.models;

import java.util.List;

public class BotHistory {
    private Integer total;
    private Boolean moreAvailable;
    private List<BotHistoryMessage> messages = null;

    private String icon;

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Boolean getMoreAvailable() {
        return moreAvailable;
    }

    public void setMoreAvailable(Boolean moreAvailable) {
        this.moreAvailable = moreAvailable;
    }

    public List<BotHistoryMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<BotHistoryMessage> messages) {
        this.messages = messages;
    }

}