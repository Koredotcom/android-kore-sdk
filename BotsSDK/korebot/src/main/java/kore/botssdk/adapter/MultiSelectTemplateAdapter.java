package kore.botssdk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotMultiSelectElementModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.MultiSelectBase;
import kore.botssdk.utils.KaFontUtils;

public class MultiSelectTemplateAdapter extends RecyclerView.Adapter<MultiSelectTemplateAdapter.ViewHolder> {
    private static final int MULTI_SELECT_ITEM = 0;
    private static final int MULTI_SELECT_BUTTON = 1;
    private final List<MultiSelectBase> multiSelectModels;
    private final boolean isEnabled;
    private List<MultiSelectBase> checkedItems = new ArrayList<>();
    private ChatContentStateListener listener;
    private ComposeFooterInterface composeFooterInterface;
    private final String msgId;

    public MultiSelectTemplateAdapter(String msgId, List<MultiSelectBase> multiSelectModels, boolean isEnabled) {
        this.msgId = msgId;
        this.multiSelectModels = multiSelectModels;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = viewType == MULTI_SELECT_ITEM ? R.layout.multi_select_item : R.layout.multiselect_button;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MultiSelectBase item = getItem(position);
        if (item == null) return;
        if (item instanceof BotMultiSelectElementModel) {
            holder.textView.setTag(item);
            holder.textView.setText(((BotMultiSelectElementModel) item).getTitle());
            holder.checkBox.setChecked(checkedItems.contains(item));
            holder.checkBox.setOnCheckedChangeListener((v, isChecked) -> {
                if (v.isPressed()) {
                    if (isEnabled) {
                        if (isChecked) {
                            checkedItems.add(item);
                        } else {
                            checkedItems.remove(item);
                        }
                        if (listener != null) listener.onSaveState(msgId, checkedItems, BotResponse.SELECTED_ITEM);
                    } else {
                        ((CompoundButton) v).setChecked(!isChecked);
                    }
                }
            });
        } else {
            holder.textView.setTag(item);
            holder.textView.setText(((BotButtonModel) item).getTitle());
            holder.textView.setOnClickListener(v -> {
                if (composeFooterInterface != null && isEnabled && checkedItems.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sbValue = new StringBuilder();
                    for (MultiSelectBase item1 : checkedItems) {
                        if (!sb.toString().isEmpty()) sb.append(" ");
                        sb.append(((BotMultiSelectElementModel) item1).getTitle());

                        if (!sbValue.toString().isEmpty()) sbValue.append(",");
                        sbValue.append(((BotMultiSelectElementModel) item1).getValue());
                    }
                    composeFooterInterface.onSendClick(sb.toString(), sbValue.toString(), false);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof BotMultiSelectElementModel) return MULTI_SELECT_ITEM;
        return MULTI_SELECT_BUTTON;
    }

    public void setListener(ChatContentStateListener listener) {
        this.listener = listener;
    }

    public void setCheckItems(List<MultiSelectBase> checkedItems) {
        this.checkedItems = checkedItems;
    }

    @Override
    public int getItemCount() {
        return multiSelectModels != null ? multiSelectModels.size() : 0;
    }

    private MultiSelectBase getItem(int position) {
        return multiSelectModels != null ? multiSelectModels.get(position) : null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView;
        RelativeLayout root_layout;
        LinearLayout root_layout_btn;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == MULTI_SELECT_ITEM) {
                textView = itemView.findViewById(R.id.text_view);
                checkBox = itemView.findViewById(R.id.check_multi_item);
                root_layout = itemView.findViewById(R.id.root_layout);
            } else {
                textView = itemView.findViewById(R.id.text_view_button);
                root_layout_btn = itemView.findViewById(R.id.root_layout);
            }

            KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }
}