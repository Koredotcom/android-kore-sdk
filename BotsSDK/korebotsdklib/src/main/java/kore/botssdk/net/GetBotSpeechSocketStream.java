package kore.botssdk.net;

import kore.botssdk.models.BotSpeechSocketStream;

/**
 * Created by Pradeep Mahato on 25/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class GetBotSpeechSocketStream extends RestRequest<BotSpeechSocketStream> {

    String emailId;
    public GetBotSpeechSocketStream(String userId, String emailId, String accessToken) {
        super(BotSpeechSocketStream.class, userId, accessToken);
        this.emailId = emailId;
    }

    @Override
    public BotSpeechSocketStream loadDataFromNetwork() throws Exception {
        return getService().getSpeechSocketStream(accessTokenHeader(), emailId);
    }
}
