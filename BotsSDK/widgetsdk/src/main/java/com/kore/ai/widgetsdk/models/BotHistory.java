package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 03-Oct-18.
 */

public class BotHistory {
    private Integer total;
    private Boolean moreAvailable;
    private List<BotHistoryMessage> messages = null;

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