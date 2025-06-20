package kore.botssdk.adapter;

import static android.content.Context.MODE_PRIVATE;
import static kore.botssdk.models.BotResponse.BUBBLE_LEFT_BG_COLOR;
import static kore.botssdk.models.BotResponse.BUBBLE_RIGHT_BG_COLOR;
import static kore.botssdk.models.BotResponse.THEME_NAME;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.listener.AdvanceMultiSelectListener;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.AdvancedMultiSelectModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.KaFontUtils;

@SuppressLint("UnknownNullness")
public class AdvancedMultiSelectAdapter extends RecyclerView.Adapter<AdvancedMultiSelectAdapter.ViewHolder> {
    private static final int MULTI_SELECT_ITEM = 0;
    private static final int MULTI_SELECT_BUTTON = 1;
    private ArrayList<AdvancedMultiSelectModel> multiSelectModels = new ArrayList<>();
    private int visibleLimit = 1;
    private ArrayList<AdvanceMultiSelectCollectionModel> checkedItems = new ArrayList<>();
    private AdvanceMultiSelectListener advanceMultiSelectListener;
    private boolean isEnabled;
    private SharedPreferences prefs;

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (prefs == null) {
            prefs = parent.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
        }
        int layoutId = viewType == MULTI_SELECT_ITEM ? R.layout.row_advanced_multi_select_item : R.layout.multiselect_button;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMultiSelectTitle.setText(multiSelectModels.get(position).getCollectionTitle());
        ArrayList<AdvanceMultiSelectCollectionModel> multiSelectCollectionModels = multiSelectModels.get(position).getCollection();

        if (multiSelectCollectionModels != null) {
            AdvanceMultiSelectCollectionsAdapter adapter = new AdvanceMultiSelectCollectionsAdapter(multiSelectCollectionModels);
            adapter.setEnabled(isEnabled);
            adapter.setCheckedItems(checkedItems);
            adapter.setAdvanceMultiListener(advanceMultiSelectListener);
            holder.advMultiSelectList.setAdapter(adapter);

            holder.rootLayout.setVisibility(View.GONE);

            if (multiSelectCollectionModels.size() > 1) holder.rootLayout.setVisibility(View.VISIBLE);

            GradientDrawable gradientDrawable = (GradientDrawable) holder.checkSelectAll.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#ffffff"));
            gradientDrawable.setStroke((int) dp1, Color.parseColor(prefs.getString(BUBBLE_LEFT_BG_COLOR, "#ffffff")));
            holder.checkSelectAll.setTag(true);

            if (checkedItems.containsAll(multiSelectCollectionModels)) {
                gradientDrawable.setStroke((int) dp1, Color.parseColor(prefs.getString(BUBBLE_RIGHT_BG_COLOR, "#3F51B5")));
                gradientDrawable.setColor(Color.parseColor(prefs.getString(BUBBLE_RIGHT_BG_COLOR, "#3F51B5")));
                holder.checkSelectAll.setTag(false);
            }

            holder.checkSelectAll.setOnClickListener(v -> {
                advanceMultiSelectListener.allItemsSelected((Boolean) holder.checkSelectAll.getTag(), multiSelectCollectionModels);
                holder.checkSelectAll.setTag(!((Boolean) holder.checkSelectAll.getTag()));
            });
        }

        if (!isEnabled) {
            holder.checkSelectAll.setClickable(false);
            holder.checkSelectAll.setEnabled(false);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (multiSelectModels != null) {
            return visibleLimit;
        } else {
            return 0;
        }
    }

    private AdvancedMultiSelectModel getItem(int position) {
        return multiSelectModels != null ? multiSelectModels.get(position) : null;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) != null)
            return MULTI_SELECT_ITEM;
        return MULTI_SELECT_BUTTON;
    }

    public void setMultiSelectModels(ArrayList<AdvancedMultiSelectModel> multiSelectModels) {
        this.multiSelectModels = multiSelectModels;
    }

    public void setAdvanceMultiListener(AdvanceMultiSelectListener advanceMultiSelectListner) {
        this.advanceMultiSelectListener = advanceMultiSelectListner;
    }

    public void refresh() {
        visibleLimit = multiSelectModels.size();
        notifyItemRangeChanged(0, visibleLimit);
    }

    public void setCheckedItems(ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout checkSelectAll;
        TextView tvMultiSelectTitle;
        RelativeLayout rootLayout;
        LinearLayout rootLayoutBtn;
        RecyclerView advMultiSelectList;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == MULTI_SELECT_ITEM) {
                checkSelectAll = itemView.findViewById(R.id.check_select_all);
                tvMultiSelectTitle = itemView.findViewById(R.id.tvMultiSelectTitle);
                advMultiSelectList = itemView.findViewById(R.id.adv_multi_select_list);
                advMultiSelectList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                advMultiSelectList.addItemDecoration(new VerticalSpaceItemDecoration(10 * (int) dp1));
                rootLayout = itemView.findViewById(R.id.root_layout);
            } else {
                rootLayoutBtn = itemView.findViewById(R.id.root_layout);
            }

            KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        }
    }
}
