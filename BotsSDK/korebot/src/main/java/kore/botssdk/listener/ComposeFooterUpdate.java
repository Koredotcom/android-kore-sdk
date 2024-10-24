package kore.botssdk.listener;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 13-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface ComposeFooterUpdate extends BaseView{
    void enableSendButton();

    void showFreemiumDialog();

    void addAttachmentToAdapter(HashMap<String, String> attachmentKey);
}
