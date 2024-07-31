package kore.botssdk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.RadioOptionModel;

public class RadioOptionsItemAdapter extends RecyclerView.Adapter<RadioOptionsItemAdapter.ViewHolder> {
    private AppCompatRadioButton rbItem;
    private TextView value;
    private final List<RadioOptionModel> items;
    private final boolean isEnabled;
    public int selectedItem = -1;
    private final ChatContentStateListener listener;
    private final String msgId;

    public RadioOptionsItemAdapter(String msgId, List<RadioOptionModel> items, boolean isEnabled, int selectedItem, ChatContentStateListener listener) {
        this.msgId = msgId;
        this.items = items;
        this.isEnabled = isEnabled;
        this.selectedItem = selectedItem;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_options_row_item, parent, false);
        rbItem = view.findViewById(R.id.rbItem);
        value = view.findViewById(R.id.value);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RadioOptionModel item = getItem(position);
        if (item == null) return;
        rbItem.setText(item.getTitle());
        value.setText(item.getValue());
        rbItem.setChecked(selectedItem != -1 && selectedItem == position);
        rbItem.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (compoundButton.isPressed()) {
                if (!isEnabled) {
                    compoundButton.setChecked(!isChecked);
                    return;
                }
                listener.onSelect(msgId, position, BotResponse.SELECTED_ITEM);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    private RadioOptionModel getItem(int position) {
        return items != null ? items.get(position) : null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
