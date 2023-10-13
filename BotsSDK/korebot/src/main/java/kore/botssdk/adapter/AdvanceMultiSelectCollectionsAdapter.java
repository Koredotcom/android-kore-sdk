package kore.botssdk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceMultiSelectListner;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.MultiSelectAllListner;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;

@SuppressLint("UnknownNullness")
public class AdvanceMultiSelectCollectionsAdapter extends BaseAdapter {

    ArrayList<AdvanceMultiSelectCollectionModel> multiSelectModels;
    ComposeFooterInterface composeFooterInterface;
    private final LayoutInflater ownLayoutInflator;
    private final Context context;
    ArrayList<AdvanceMultiSelectCollectionModel> checkedItems = new ArrayList<>();
    AdvanceMultiSelectListner multiSelectListner;
    MultiSelectAllListner multiSelectAllListner;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    CheckBox selectAll;
    boolean isEnabled;
    private final int dp1;

    public AdvanceMultiSelectCollectionsAdapter(Context context, ArrayList<AdvanceMultiSelectCollectionModel> multiSelectModels) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.multiSelectModels = multiSelectModels;
        this.context = context;
        this.dp1 = (int) Utility.convertDpToPixel(context, 1);
    }

    @Override
    public int getCount() {
        if (multiSelectModels != null) {
            return multiSelectModels.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return multiSelectModels.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (multiSelectModels.get(position) != null) {
                convertView = ownLayoutInflator.inflate(R.layout.advance_multi_select_item, null);
                holder.textView = convertView.findViewById(R.id.text_view);
                holder.checkBox = convertView.findViewById(R.id.check_multi_item);
                holder.ivAdvMultiSelect = convertView.findViewById(R.id.ivAdvMultiSelect);

            } else {
                convertView = ownLayoutInflator.inflate(R.layout.multiselect_button, null);
                holder.textView = convertView.findViewById(R.id.text_view_button);
            }
            convertView.setTag(holder);
            KaFontUtils.applyCustomFont(context, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        final AdvanceMultiSelectCollectionModel item = (AdvanceMultiSelectCollectionModel) getItem(position);
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

    public void unCheckAll()
    {
        this.checkedItems = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public boolean isObjectInSet(AdvanceMultiSelectCollectionModel currentItem) {
        for (AdvanceMultiSelectCollectionModel item : checkedItems) {
            if (currentItem.getTitle().equals(item.getTitle()))
            {
                return true;
            }
        }

        return false;
    }

    public void setAdvanceMultiListner(AdvanceMultiSelectListner checkAllListner) {
        this.multiSelectListner = checkAllListner;
    }

    public void setMultiListner(MultiSelectAllListner checkAllListner) {
        this.multiSelectAllListner = checkAllListner;
    }

    private final View.OnClickListener itemSelectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isEnabled)
            {
                AdvanceMultiSelectCollectionModel item = (AdvanceMultiSelectCollectionModel) v.getTag();
                if (((CompoundButton) v).isChecked()) {
                    checkedItems.add(item);
                } else {
                    checkedItems.remove(item);
                }

                if (checkedItems.size() == multiSelectModels.size()) {
                    if (multiSelectListner != null)
                        multiSelectListner.itemSelected(item);

                    if (multiSelectAllListner != null)
                        multiSelectAllListner.isSelectAll(true, checkedItems);

                    if(selectAll != null) {
                        selectAll.setChecked(true);
                        selectAll.setTag(true);
                    }
                } else {
                    if (multiSelectListner != null)
                        multiSelectListner.itemSelected(item);

                    if (multiSelectAllListner != null)
                        multiSelectAllListner.isSelectAll(false, checkedItems);

                    if(selectAll != null) {
                        selectAll.setChecked(false);
                        selectAll.setTag(false);
                    }
                }
            }
            else {
                ((CompoundButton) v).setChecked(false);
            }
        }
    };

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

//    public ArrayList<AdvanceMultiSelectCollectionModel> getCheckedItems() {
//        if (checkedItems != null && checkedItems.size() > 0)
//            return checkedItems;
//        else return new ArrayList<>();
//    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
        ImageView ivAdvMultiSelect;
    }
}
