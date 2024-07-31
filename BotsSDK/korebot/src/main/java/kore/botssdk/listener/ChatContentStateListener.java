package kore.botssdk.listener;

import java.util.List;

public interface ChatContentStateListener {
    public void onFeedbackSelected(String id, int selectedPosition);

    public void onMultiSelectItems(String id, String key, List<String> selItems, boolean isChecked);

    public void onSelect(String id, Object value, String key);
}
