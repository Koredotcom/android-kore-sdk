package kore.botssdk.autobahn;

import kore.botssdk.models.BotSpeechSocketStream;

/**
 * Created by Pradeep Mahato on 25/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface WebSpeechSocketListener {
    void onSpeechSocketConnection(BotSpeechSocketStream botSpeechSocketStream);
}
