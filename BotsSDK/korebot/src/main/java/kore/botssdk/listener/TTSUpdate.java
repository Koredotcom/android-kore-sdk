package kore.botssdk.listener;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface TTSUpdate {
    void ttsUpdateListener(boolean isTTSEnabled);
    void ttsOnStop();
}
