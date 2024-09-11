package kore.botssdk.listener;

public interface ChatContentStateListener {
    void onSaveState(String messageId, Object value, String key);
}
