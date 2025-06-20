package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.RadioOptionModel;
import kore.botssdk.net.SDKConfiguration;

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
        setTintColor(rbItem);
        value.setText(item.getValue());
        rbItem.setChecked(selectedItem != -1 && selectedItem == position);
        rbItem.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (compoundButton.isPressed()) {
                if (!isEnabled) {
                    compoundButton.setChecked(!isChecked);
                    return;
                }
                listener.onSaveState(msgId, position, BotResponse.SELECTED_ITEM);
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

    private void setTintColor(AppCompatRadioButton radioButton) {
        SharedPreferences sharedPreferences = rbItem.getContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        String colorUnchecked = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#FFFFFF");
        String colorChecked = SDKConfiguration.BubbleColors.quickReplyColor;

        ColorStateList tintList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor(colorChecked),
                        Color.parseColor(colorUnchecked)
                }
        );

        CompoundButtonCompat.setButtonTintList(radioButton, tintList);
        radioButton.getButtonDrawable().setTintList(tintList);
        radioButton.invalidate();
    }
}