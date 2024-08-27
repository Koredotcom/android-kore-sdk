package kore.botssdk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import kore.botssdk.listener.MultiSelectAllListener;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.AdvancedMultiSelectModel;
import kore.botssdk.utils.KaFontUtils;

@SuppressLint("UnknownNullness")
public class AdvancedMultiSelectAdapter extends RecyclerView.Adapter<AdvancedMultiSelectAdapter.ViewHolder> implements MultiSelectAllListener {
    private static final int MULTI_SELECT_ITEM = 0;
    private static final int MULTI_SELECT_BUTTON = 1;
    private ArrayList<AdvancedMultiSelectModel> multiSelectModels = new ArrayList<>();
    private final Context context;
    private int visibleLimit = 1;
    private ArrayList<AdvanceMultiSelectCollectionModel> checkedItems = new ArrayList<>();
    private AdvanceMultiSelectListener advanceMultiSelectListener;

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    AdvanceMultiSelectCollectionsAdapter advanceMultiSelectCollectionsAdapter;
    boolean isEnabled;

    public AdvancedMultiSelectAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = viewType == MULTI_SELECT_ITEM ? R.layout.row_advanced_multi_select_item : R.layout.multiselect_button;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMultiSelectTitle.setText(multiSelectModels.get(position).getCollectionTitle());
        ArrayList<AdvanceMultiSelectCollectionModel> multiSelectCollectionModels = multiSelectModels.get(position).getCollection();

        if (multiSelectCollectionModels != null) {
            holder.advMultiSelectList.setAdapter(advanceMultiSelectCollectionsAdapter = new AdvanceMultiSelectCollectionsAdapter(context, multiSelectCollectionModels));
            advanceMultiSelectCollectionsAdapter.setEnabled(isEnabled);
            advanceMultiSelectCollectionsAdapter.setCheckAll(checkedItems);
            advanceMultiSelectCollectionsAdapter.setCheckBoxAll(holder.checkSelectAll);
            advanceMultiSelectCollectionsAdapter.setAdvanceMultiListener(advanceMultiSelectListener);
            advanceMultiSelectCollectionsAdapter.setMultiListener(AdvancedMultiSelectAdapter.this);

            holder.rootLayout.setVisibility(View.GONE);

            if (multiSelectCollectionModels.size() > 1) holder.rootLayout.setVisibility(View.VISIBLE);

            if (holder.checkSelectAll.getTag() == null) holder.checkSelectAll.setTag(false);

            holder.checkSelectAll.setOnClickListener(v -> {
                if (!(boolean) (v.getTag())) {
                    v.setTag(true);
                    holder.advMultiSelectList.setAdapter(advanceMultiSelectCollectionsAdapter = new AdvanceMultiSelectCollectionsAdapter(context, multiSelectCollectionModels));
                    advanceMultiSelectCollectionsAdapter.setEnabled(isEnabled);
                    advanceMultiSelectCollectionsAdapter.setCheckAll(multiSelectCollectionModels);
                    advanceMultiSelectCollectionsAdapter.setCheckBoxAll(holder.checkSelectAll);
                    advanceMultiSelectCollectionsAdapter.setAdvanceMultiListener(advanceMultiSelectListener);
                    advanceMultiSelectCollectionsAdapter.setMultiListener(AdvancedMultiSelectAdapter.this);
                    checkedItems.addAll(multiSelectCollectionModels);

                    if (advanceMultiSelectListener != null) {
                        advanceMultiSelectListener.allItemsSelected(true, multiSelectCollectionModels);
                    }
                } else {
                    v.setTag(false);
                    holder.advMultiSelectList.setAdapter(advanceMultiSelectCollectionsAdapter = new AdvanceMultiSelectCollectionsAdapter(context, multiSelectCollectionModels));
                    advanceMultiSelectCollectionsAdapter.setEnabled(isEnabled);
                    advanceMultiSelectCollectionsAdapter.unCheckAll();
                    advanceMultiSelectCollectionsAdapter.setCheckBoxAll(holder.checkSelectAll);
                    advanceMultiSelectCollectionsAdapter.setAdvanceMultiListener(advanceMultiSelectListener);
                    advanceMultiSelectCollectionsAdapter.setMultiListener(AdvancedMultiSelectAdapter.this);
                    checkedItems = new ArrayList<>();

                    if (advanceMultiSelectListener != null) {
                        advanceMultiSelectListener.allItemsSelected(false, multiSelectCollectionModels);
                    }
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

    public void setAdvanceMultiListener(AdvanceMultiSelectListener advanceMultiSelectListener) {
        this.advanceMultiSelectListener = advanceMultiSelectListener;
    }

    public void refresh() {
        visibleLimit = multiSelectModels.size();
        notifyDataSetChanged();
    }

    @Override
    public void isSelectAll(boolean selectAll, ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkSelectAll;
        TextView tvMultiSelectTitle;
        RelativeLayout rootLayout;
        LinearLayout root_layout_btn;
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
                root_layout_btn = itemView.findViewById(R.id.root_layout);
            }

            KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        }
    }
}
