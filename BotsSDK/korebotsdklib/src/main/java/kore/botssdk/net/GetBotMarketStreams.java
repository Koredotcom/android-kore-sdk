package kore.botssdk.net;

import android.content.Context;

/**
 * Created by Pradeep Mahato on 06-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class GetBotMarketStreams extends RestRequest<MarketStreamList> {

    public GetBotMarketStreams(String userId, String accessToken) {
        super(MarketStreamList.class, userId, accessToken);
    }

    @Override
    public MarketStreamList loadDataFromNetwork() throws Exception {

        MarketStreamList marketStreamList = getService().getMarketStreams(userId, accessTokenHeader());
        return marketStreamList;
    }
}
