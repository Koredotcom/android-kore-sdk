package kore.botssdk.listener;

/**
 * Created by Pradeep Mahato on 18-May-17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface TTSUpdate {
    void ttsUpdateListener(boolean isTTSEnabled);
    void ttsOnStop();
}
