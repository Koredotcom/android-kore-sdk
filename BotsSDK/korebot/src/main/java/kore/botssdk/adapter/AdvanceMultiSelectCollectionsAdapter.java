package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceMultiSelectListener;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;

@SuppressLint("UnknownNullness")
public class AdvanceMultiSelectCollectionsAdapter extends RecyclerView.Adapter<AdvanceMultiSelectCollectionsAdapter.ViewHolder> {
    private static final int MULTI_SELECT_ITEM = 0;
    private static final int MULTI_SELECT_BUTTON = 1;
    private final ArrayList<AdvanceMultiSelectCollectionModel> multiSelectModels;
    private ArrayList<AdvanceMultiSelectCollectionModel> checkedItems = new ArrayList<>();
    private AdvanceMultiSelectListener multiSelectListener;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled;

    public AdvanceMultiSelectCollectionsAdapter(ArrayList<AdvanceMultiSelectCollectionModel> multiSelectModels) {
        this.multiSelectModels = multiSelectModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = viewType == MULTI_SELECT_ITEM ? R.layout.row_advance_multi_select_sub_item : R.layout.multiselect_button;
        return new AdvanceMultiSelectCollectionsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType);
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
        final AdvanceMultiSelectCollectionModel item = (AdvanceMultiSelectCollectionModel) getItem(position);
        if (item == null) return;
        holder.textView.setTag(item);
        holder.textView.setText(item.getTitle());

        if (!StringUtils.isNullOrEmpty(item.getImage_url())) {
            holder.ivAdvMultiSelect.setVisibility(View.VISIBLE);

            Picasso.get().load(item.getImage_url())
                    .centerInside()
                    .placeholder(R.drawable.transparant_image)
                    .resize((int) (40 * dp1), (int) (40 * dp1))
                    .into(holder.ivAdvMultiSelect);
        }

        holder.checkBox.setChecked(checkedItems.contains(item));

        holder.checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (compoundButton.isPressed()) {
                if (!isEnabled) {
                    compoundButton.setChecked(!isChecked);
                    return;
                }
                multiSelectListener.itemSelected(item);
            }
        });
        if (!isEnabled) {
            holder.checkBox.setClickable(false);
            holder.checkBox.setEnabled(false);
            holder.checkBox.setFocusableInTouchMode(false);
        }
    }

    public void setCheckedItems(ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public void setAdvanceMultiListener(AdvanceMultiSelectListener checkAllListener) {
        this.multiSelectListener = checkAllListener;
    }

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
