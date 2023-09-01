package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotMultiSelectElementModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.MultiSelectBase;
import kore.botssdk.utils.KaFontUtils;

public class MultiSelectButtonAdapter extends BaseAdapter {

    private ArrayList<MultiSelectBase> multiSelectModels = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    private final Context context;
    private final Set<MultiSelectBase> checkedItems = new HashSet<>();
    private final SharedPreferences sharedPreferences;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled;

    public MultiSelectButtonAdapter(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
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
    public MultiSelectBase getItem(int position) {
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

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if (multiSelectModels.get(position) instanceof BotMultiSelectElementModel) {
                convertView = View.inflate(context, R.layout.multi_select_item, null);
                holder.textView = convertView.findViewById(R.id.text_view);
                holder.checkBox = convertView.findViewById(R.id.check_multi_item);
                holder.root_layout = convertView.findViewById(R.id.root_layout);
            } else {
                convertView = View.inflate(context, R.layout.multiselect_button, null);
                holder.textView = convertView.findViewById(R.id.text_view_button);
                holder.root_layout_btn = convertView.findViewById(R.id.root_layout);
            }
            convertView.setTag(holder);
            KaFontUtils.applyCustomFont(context, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(holder != null && sharedPreferences != null && holder.root_layout != null)
        {
            holder.root_layout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#FFFFFF")));
        }

        if(holder != null && sharedPreferences != null && holder.root_layout_btn != null)
        {
            holder.root_layout_btn.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#FFFFFF")));
        }

        if(holder != null && sharedPreferences != null &&  holder.textView != null)
        {
            holder.textView.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#000000")));
        }

        populateVIew(holder, position,multiSelectModels.get(position) instanceof BotMultiSelectElementModel?0:1);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position,int type) {
        final MultiSelectBase item = getItem(position);
        if(type ==0) {
            holder.textView.setTag(item);
            holder.textView.setText(((BotMultiSelectElementModel)item).getTitle());
            holder.checkBox.setChecked(checkedItems.contains(item));
            holder.checkBox.setTag(item);
            holder.checkBox.setOnClickListener(itemSelectionListener);
        }else{
            holder.textView.setTag(item);
            holder.textView.setText(((BotButtonModel)item).getTitle());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(composeFooterInterface!= null && isEnabled && checkedItems.size()>0){
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sbValue = new StringBuilder();
                        for(MultiSelectBase item:checkedItems){
                            if(!sb.toString().isEmpty())
                                sb.append(",");
                            sb.append(((BotMultiSelectElementModel)item).getTitle());

                            if(!sbValue.toString().isEmpty())
                                sbValue.append(",");
                            sbValue.append(((BotMultiSelectElementModel)item).getValue());
                        }
                        composeFooterInterface.onSendClick(sb.toString(),sbValue.toString(),false);
                    }

                }
            });
        }

    }
    private final View.OnClickListener itemSelectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEnabled) {
                MultiSelectBase item = (MultiSelectBase) v.getTag();
                if (((CompoundButton) v).isChecked()) {
                    checkedItems.add(item);
                } else {
                    checkedItems.remove(item);
                }
            }else{
                ((CompoundButton) v).setChecked(false);
            }
        }
    };

    public void setMultiSelectModels(ArrayList<MultiSelectBase> multiSelectModels) {
        this.multiSelectModels = multiSelectModels;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
        RelativeLayout root_layout;
        LinearLayout root_layout_btn;
    }
}
