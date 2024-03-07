package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotFormTemplateModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;

public class BotFormTemplateAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<BotFormTemplateModel> arrBotFormTemplateModels;
    private String textColor;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled;

    public void setTextColor(@NonNull String textColor) {
        this.textColor = textColor;
    }

    public BotFormTemplateAdapter(@NonNull Context context, @NonNull ArrayList<BotFormTemplateModel> arrBotFormTemplateModels) {
        this.context = context;
        this.arrBotFormTemplateModels = arrBotFormTemplateModels;
    }

    @Override
    public int getCount() {
        if (arrBotFormTemplateModels != null) {
            return arrBotFormTemplateModels.size();
        } else {
            return 0;
        }
    }

    @Override
    public BotFormTemplateModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return arrBotFormTemplateModels.get(position);
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
            convertView = View.inflate(context, R.layout.form_templete_cell_view, null);
            holder.tvFormFieldTitle = convertView.findViewById(R.id.tvFormFieldTitle);
            holder.btfieldButton = convertView.findViewById(R.id.btfieldButton);
            holder.edtFormInput = convertView.findViewById(R.id.edtFormInput);
            convertView.setTag(holder);
            KaFontUtils.applyCustomFont(context, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        final BotFormTemplateModel item = getItem(position);
        holder.btfieldButton.setTag(item);

        if (item.getFieldButton() != null)
            holder.btfieldButton.setText(item.getFieldButton().getTitle());
        else
            holder.btfieldButton.setText(R.string.ok);

        String str = item.getLabel() + " : ";
        holder.tvFormFieldTitle.setText(str);
        holder.edtFormInput.setHint(item.getPlaceHolder());

        if (!StringUtils.isNullOrEmpty(textColor)) {
            holder.tvFormFieldTitle.setTextColor(Color.parseColor(textColor));
        }
    }

    public void setBotFormTemplates(@NonNull ArrayList<BotFormTemplateModel> arrBotFormTemplateModels) {
        this.arrBotFormTemplateModels = arrBotFormTemplateModels;
    }

    static class ViewHolder {
        Button btfieldButton;
        TextView tvFormFieldTitle;
        EditText edtFormInput;
    }
}

