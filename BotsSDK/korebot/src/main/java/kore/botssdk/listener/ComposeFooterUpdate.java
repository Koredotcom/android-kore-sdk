package kore.botssdk.listener;

import java.util.HashMap;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface ComposeFooterUpdate extends BaseView {
    void enableSendButton();
    void addAttachmentToAdapter(HashMap<String, String> attachmentKey);
}
