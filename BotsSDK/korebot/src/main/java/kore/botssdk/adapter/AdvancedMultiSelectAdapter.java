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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceMultiSelectListner;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.MultiSelectAllListner;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.AdvancedMultiSelectModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.AutoExpandListView;

@SuppressLint("UnknownNullness")
public class AdvancedMultiSelectAdapter extends BaseAdapter implements MultiSelectAllListner{

    ArrayList<AdvancedMultiSelectModel> multiSelectModels = new ArrayList<>();
    ComposeFooterInterface composeFooterInterface;
    private final LayoutInflater ownLayoutInflator;
    Context context;
    int visibleLimit = 1;
    ArrayList<AdvanceMultiSelectCollectionModel> checkedItems = new ArrayList<>();
    AdvanceMultiSelectListner advanceMultiSelectListner;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    AdvanceMultiSelectCollectionsAdapter advanceMultiSelectCollectionsAdapter;
    boolean isEnabled;

    public AdvancedMultiSelectAdapter(Context context) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        if (multiSelectModels != null) {
            return visibleLimit;
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
        if (convertView == null)
        {
            holder = new ViewHolder();
            if (multiSelectModels.get(position) != null)
            {
                convertView = ownLayoutInflator.inflate(R.layout.advanced_multi_select_view, null);
                holder.check_select_all = convertView.findViewById(R.id.check_select_all);
                holder.tvMultiSelectTitle = convertView.findViewById(R.id.tvMultiSelectTitle);
                holder.adv_multi_select_list = convertView.findViewById(R.id.adv_multi_select_list);
                holder.root_layout = convertView.findViewById(R.id.root_layout);
            } else {
                convertView = ownLayoutInflator.inflate(R.layout.multiselect_button, null);
                holder.root_layout_btn = convertView.findViewById(R.id.root_layout);
            }

            convertView.setTag(holder);
            KaFontUtils.applyCustomFont(context, convertView);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMultiSelectTitle.setText(multiSelectModels.get(position).getCollectionTitle());
        ArrayList<AdvanceMultiSelectCollectionModel> multiSelectCollectionModels = multiSelectModels.get(position).getCollection();

        if(multiSelectCollectionModels != null)
        {
            holder.adv_multi_select_list.setAdapter(advanceMultiSelectCollectionsAdapter = new AdvanceMultiSelectCollectionsAdapter(context, multiSelectCollectionModels));
            advanceMultiSelectCollectionsAdapter.setEnabled(isEnabled);
            advanceMultiSelectCollectionsAdapter.setCheckAll(checkedItems);
            advanceMultiSelectCollectionsAdapter.setCheckBoxAll(holder.check_select_all);
            advanceMultiSelectCollectionsAdapter.setAdvanceMultiListner(advanceMultiSelectListner);
            advanceMultiSelectCollectionsAdapter.setMultiListner(AdvancedMultiSelectAdapter.this);

            holder.root_layout.setVisibility(View.GONE);

            if(multiSelectCollectionModels.size() > 1)
                holder.root_layout.setVisibility(View.VISIBLE);

            if(holder.check_select_all.getTag() == null)
                holder.check_select_all.setTag(false);

            holder.check_select_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(boolean)(v.getTag()))
                    {
                        v.setTag(true);
                        holder.adv_multi_select_list.setAdapter(advanceMultiSelectCollectionsAdapter = new AdvanceMultiSelectCollectionsAdapter(context, multiSelectCollectionModels));
                        advanceMultiSelectCollectionsAdapter.setEnabled(isEnabled);
                        advanceMultiSelectCollectionsAdapter.setCheckAll(multiSelectCollectionModels);
                        advanceMultiSelectCollectionsAdapter.setCheckBoxAll(holder.check_select_all);
                        advanceMultiSelectCollectionsAdapter.setAdvanceMultiListner(advanceMultiSelectListner);
                        advanceMultiSelectCollectionsAdapter.setMultiListner(AdvancedMultiSelectAdapter.this);
                        checkedItems.addAll(multiSelectCollectionModels);

                        if(advanceMultiSelectListner != null)
                        {
                            advanceMultiSelectListner.allItemsSelected(true, multiSelectCollectionModels);
                        }
                    }
                    else
                    {
                        v.setTag(false);
                        holder.adv_multi_select_list.setAdapter(advanceMultiSelectCollectionsAdapter = new AdvanceMultiSelectCollectionsAdapter(context, multiSelectCollectionModels));
                        advanceMultiSelectCollectionsAdapter.setEnabled(isEnabled);
                        advanceMultiSelectCollectionsAdapter.unCheckAll();
                        advanceMultiSelectCollectionsAdapter.setCheckBoxAll(holder.check_select_all);
                        advanceMultiSelectCollectionsAdapter.setAdvanceMultiListner(advanceMultiSelectListner);
                        advanceMultiSelectCollectionsAdapter.setMultiListner(AdvancedMultiSelectAdapter.this);
                        checkedItems = new ArrayList<>();

                        if(advanceMultiSelectListner != null)
                        {
                            advanceMultiSelectListner.allItemsSelected(false, multiSelectCollectionModels);
                        }
                    }
                }
            });
        }

        if(!isEnabled)
        {
            holder.check_select_all.setClickable(false);
            holder.check_select_all.setEnabled(false);
        }

        return convertView;
    }

    public void setMultiSelectModels(ArrayList<AdvancedMultiSelectModel> multiSelectModels) {
        this.multiSelectModels = multiSelectModels;
    }

    public void setAdvanceMultiListner(AdvanceMultiSelectListner advanceMultiSelectListner)
    {
        this.advanceMultiSelectListner = advanceMultiSelectListner;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void refresh()
    {
        visibleLimit = multiSelectModels.size();
        notifyDataSetChanged();
    }

    @Override
    public void isSelectAll(boolean selectAll, ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        this.checkedItems = checkedItems;
    }

    static class ViewHolder {
        CheckBox check_select_all;
        TextView tvMultiSelectTitle;
        RelativeLayout root_layout;
        LinearLayout root_layout_btn;
        AutoExpandListView adv_multi_select_list;
    }
}
