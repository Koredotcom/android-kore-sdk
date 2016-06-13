package kore.botssdk.models;

import java.util.ArrayList;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class BotResponseMessage {
    private String type;
    private BotResponseMessageComponentInfo cInfo;

    public String getType() {
        return type;
    }

    public BotResponseMessageComponentInfo getcInfo() {
        return cInfo;
    }
}
