package kore.botssdk.listener;

import java.util.HashMap;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface InvokeGenericWebViewInterface {
    void invokeGenericWebView(String url);
    void handleUserActions(String payload, HashMap<String, Object> type);
}
