package com.kore.findlysdk.listners;


import com.kore.findlysdk.models.BotRequest;

/**
 * Created by Pradeep Mahato on 13-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface BotContentFragmentUpdate {
    void updateContentListOnSend(BotRequest botRequest);
}
