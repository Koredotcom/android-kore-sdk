package kore.botssdk.listener;

import kore.botssdk.models.BotRequest;

/**
 * Created by Pradeep Mahato on 13-Jun-16.
 */
public interface BotContentFragmentUpdate {
    void updateContentListOnSend(BotRequest botRequest);
}
