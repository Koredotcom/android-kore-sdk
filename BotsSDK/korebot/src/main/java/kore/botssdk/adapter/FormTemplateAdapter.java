package kore.botssdk.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.models.BotFormTemplateModel;
import kore.botssdk.utils.StringUtils;

public class FormTemplateAdapter extends RecyclerView.Adapter<FormTemplateAdapter.ViewHolder> {
    private final List<BotFormTemplateModel> list;
    private final String textColor;

    public FormTemplateAdapter(List<BotFormTemplateModel> list, String textColor) {
        this.list = list;
        this.textColor = textColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.form_templete_cell_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotFormTemplateModel item = getItem(position);
        if (item == null) return;

        if (item.getFieldButton() != null)
            holder.btnFieldButton.setText(item.getFieldButton().getTitle());
        else
            holder.btnFieldButton.setText(R.string.ok);

        String str = item.getLabel() + " : ";
        holder.tvFormFieldTitle.setText(str);
        holder.edtFormInput.setHint(item.getPlaceHolder());

        if (!StringUtils.isNullOrEmpty(textColor)) {
            holder.tvFormFieldTitle.setTextColor(Color.parseColor(textColor));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    private BotFormTemplateModel getItem(int position) {
        return list != null ? list.get(position) : null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button btnFieldButton;
        TextView tvFormFieldTitle;
        EditText edtFormInput;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFormFieldTitle = itemView.findViewById(R.id.tvFormFieldTitle);
            btnFieldButton = itemView.findViewById(R.id.btfieldButton);
            edtFormInput = itemView.findViewById(R.id.edtFormInput);
        }
    }
}
