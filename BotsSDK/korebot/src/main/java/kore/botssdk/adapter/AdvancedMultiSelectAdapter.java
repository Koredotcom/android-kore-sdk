package kore.botssdk.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceMultiSelectListener;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.AdvancedMultiSelectModel;
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

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = viewType == MULTI_SELECT_ITEM ? R.layout.row_advanced_multi_select_item : R.layout.multiselect_button;
        return new AdvancedMultiSelectAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType);
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
            holder.checkSelectAll.setChecked(checkedItems.containsAll(multiSelectCollectionModels));

            holder.checkSelectAll.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (compoundButton.isPressed()) {
                    if (!isEnabled) {
                        holder.checkSelectAll.setChecked(!isChecked);
                        return;
                    }
                    advanceMultiSelectListener.allItemsSelected(isChecked, multiSelectCollectionModels);
                }
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
        notifyDataSetChanged();
    }

    public void setCheckedItems(ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkSelectAll;
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
                rootLayout = itemView.findViewById(R.id.root_layout);
            } else {
                rootLayoutBtn = itemView.findViewById(R.id.root_layout);
            }

            KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        }
    }
}
