package kore.botssdk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceMultiSelectListener;
import kore.botssdk.listener.MultiSelectAllListener;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;

@SuppressLint("UnknownNullness")
public class AdvanceMultiSelectCollectionsAdapter extends RecyclerView.Adapter<AdvanceMultiSelectCollectionsAdapter.ViewHolder> {
    private static final int MULTI_SELECT_ITEM = 0;
    private static final int MULTI_SELECT_BUTTON = 1;
    private final ArrayList<AdvanceMultiSelectCollectionModel> multiSelectModels;
    private ArrayList<AdvanceMultiSelectCollectionModel> checkedItems = new ArrayList<>();
    private AdvanceMultiSelectListener multiSelectListener;
    private MultiSelectAllListener multiSelectAllListener;

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private CheckBox selectAll;
    private boolean isEnabled;
    private final int dp1;

    public AdvanceMultiSelectCollectionsAdapter(Context context, ArrayList<AdvanceMultiSelectCollectionModel> multiSelectModels) {
        this.multiSelectModels = multiSelectModels;
        this.dp1 = (int) Utility.convertDpToPixel(context, 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = viewType == MULTI_SELECT_ITEM ? R.layout.row_advance_multi_select_sub_item : R.layout.multiselect_button;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        populateVIew(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) != null)
            return MULTI_SELECT_ITEM;
        return MULTI_SELECT_BUTTON;
    }

    private AdvanceMultiSelectCollectionModel getItem(int position) {
        return multiSelectModels != null ? multiSelectModels.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (multiSelectModels != null) {
            return multiSelectModels.size();
        } else {
            return 0;
        }
    }

    private void populateVIew(ViewHolder holder, int position) {
        final AdvanceMultiSelectCollectionModel item = getItem(position);
        if (item == null) return;
        holder.textView.setTag(item);
        holder.textView.setText(item.getTitle());

        if (!StringUtils.isNullOrEmpty(item.getImage_url())) {
            holder.ivAdvMultiSelect.setVisibility(View.VISIBLE);

            Picasso.get().load(item.getImage_url())
                    .centerInside()
                    .placeholder(R.drawable.transparant_image)
                    .resize(40 * dp1, 40 * dp1)
                    .into(holder.ivAdvMultiSelect);
        }

        holder.checkBox.setChecked(isObjectInSet(item));
        holder.checkBox.setTag(item);
        holder.checkBox.setOnClickListener(itemSelectionListener);

        if (!isEnabled) {
            holder.checkBox.setClickable(false);
            holder.checkBox.setEnabled(false);
            holder.checkBox.setFocusableInTouchMode(false);
        }
    }

    public void setCheckAll(ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        this.checkedItems = new ArrayList<>();
        this.checkedItems.addAll(checkedItems);
        this.notifyDataSetChanged();
    }

    public void setCheckBoxAll(CheckBox selectAll) {
        this.selectAll = selectAll;
    }

    public void unCheckAll() {
        this.checkedItems = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public boolean isObjectInSet(AdvanceMultiSelectCollectionModel currentItem) {
        for (AdvanceMultiSelectCollectionModel item : checkedItems) {
            if (currentItem.getTitle().equals(item.getTitle())) {
                return true;
            }
        }

        return false;
    }

    public void setAdvanceMultiListener(AdvanceMultiSelectListener checkAllListener) {
        this.multiSelectListener = checkAllListener;
    }

    public void setMultiListener(MultiSelectAllListener multiSelectAllListener) {
        this.multiSelectAllListener = multiSelectAllListener;
    }

    private final View.OnClickListener itemSelectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isEnabled) {
                AdvanceMultiSelectCollectionModel item = (AdvanceMultiSelectCollectionModel) v.getTag();
                if (((CompoundButton) v).isChecked()) {
                    checkedItems.add(item);
                } else {
                    checkedItems.remove(item);
                }

                if (checkedItems.size() == multiSelectModels.size()) {
                    if (multiSelectListener != null)
                        multiSelectListener.itemSelected(item);

                    if (multiSelectAllListener != null)
                        multiSelectAllListener.isSelectAll(true, checkedItems);

                    if (selectAll != null) {
                        selectAll.setChecked(true);
                        selectAll.setTag(true);
                    }
                } else {
                    if (multiSelectListener != null)
                        multiSelectListener.itemSelected(item);

                    if (multiSelectAllListener != null)
                        multiSelectAllListener.isSelectAll(false, checkedItems);

                    if (selectAll != null) {
                        selectAll.setChecked(false);
                        selectAll.setTag(false);
                    }
                }
            } else {
                ((CompoundButton) v).setChecked(false);
            }
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView;
        ImageView ivAdvMultiSelect;

        public ViewHolder(@NonNull View convertView, int viewType) {
            super(convertView);
            if (viewType == MULTI_SELECT_ITEM) {
                textView = convertView.findViewById(R.id.text_view);
                checkBox = convertView.findViewById(R.id.check_multi_item);
                ivAdvMultiSelect = convertView.findViewById(R.id.ivAdvMultiSelect);
            } else {
                textView = convertView.findViewById(R.id.text_view_button);
            }

            KaFontUtils.applyCustomFont(convertView.getContext(), convertView);
        }
    }
}
