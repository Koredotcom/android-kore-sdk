package com.kore.ai.widgetsdk.listeners;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 22/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface InvokeGenericWebViewInterface {
    void invokeGenericWebView(String url);
    void handleUserActions(String payload, HashMap<String, Object> type);
}
