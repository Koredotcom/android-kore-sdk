package kore.botssdk.adapter;

import static android.content.Context.MODE_PRIVATE;
import static kore.botssdk.models.BotResponsePayLoadText.THEME_NAME;
import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import kore.botssdk.net.SDKConfiguration;
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

            GradientDrawable gradientDrawable = (GradientDrawable) holder.checkBox.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#ffffff"));
            gradientDrawable.setStroke((int) dp1, Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleSelected));
            holder.checkBox.setTag(true);

            if (checkedItems.contains(item)) {
                gradientDrawable.setStroke((int) dp1, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
                gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
                holder.checkBox.setTag(false);
            }

            holder.checkBox.setOnClickListener(v -> {
                if (isEnabled) {
                    if ((Boolean) holder.checkBox.getTag()) {
                        checkedItems.add(item);
                    } else {
                        checkedItems.remove(item);
                    }

                    holder.checkBox.setTag(!((Boolean) holder.checkBox.getTag()));
                    if (listener != null) listener.onSaveState(msgId, checkedItems, BotResponse.SELECTED_ITEM);
                }
            });
        } else {
            GradientDrawable gradientDrawable = (GradientDrawable) holder.textView.getBackground().mutate();
            gradientDrawable.setStroke((int) (1 * dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
            gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
            holder.textView.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor));

            holder.textView.setTag(item);
            holder.textView.setText(((BotButtonModel) item).getTitle());
            holder.textView.setOnClickListener(v -> {
                if (composeFooterInterface != null && isEnabled && !checkedItems.isEmpty()) {
                    StringBuilder titles = new StringBuilder();
                    StringBuilder values = new StringBuilder();
                    for (int i = 0; i < checkedItems.size(); i++) {
                        titles.append(((BotMultiSelectElementModel) checkedItems.get(i)).getTitle());
                        values.append(((BotMultiSelectElementModel) checkedItems.get(i)).getValue());
                        if (i != checkedItems.size() - 1) {
                            titles.append(" ");
                            values.append(", ");
                        }
                    }
                    composeFooterInterface.onSendClick(titles.toString(), values.toString(), false);
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
        LinearLayout checkBox;
        TextView textView;
        LinearLayout rootLayout;
        LinearLayout root_layout_btn;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == MULTI_SELECT_ITEM) {
                textView = itemView.findViewById(R.id.text_view);
                checkBox = itemView.findViewById(R.id.check_multi_item);
                rootLayout = itemView.findViewById(R.id.multi_row);
                if (rootLayout != null && rootLayout.getBackground() != null) {
                    SharedPreferences pref = rootLayout.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
                    GradientDrawable drawable = (GradientDrawable) rootLayout.getBackground().mutate();
                    drawable.setStroke(2, Color.parseColor(pref.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff")));
                }
            } else {
                textView = itemView.findViewById(R.id.text_view_button);
                root_layout_btn = itemView.findViewById(R.id.root_layout);

                GradientDrawable gradientDrawable = (GradientDrawable) textView.getBackground().mutate();
                gradientDrawable.setStroke((int) (1 * dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
                gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
                textView.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor));
            }

            KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }
}