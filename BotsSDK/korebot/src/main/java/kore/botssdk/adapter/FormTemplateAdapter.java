package kore.botssdk.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.InputType;
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
import kore.botssdk.net.SDKConfiguration;
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

        String str = item.getLabel();
        if (item.getType().equals("password")) {
            holder.edtFormInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        holder.tvFormFieldTitle.setText(str);
        holder.edtFormInput.setHint(item.getPlaceHolder());
        holder.edtFormInput.setBackground(createEditTextBackground(SDKConfiguration.BubbleColors.quickReplyColor, "#A7A9BE"));

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

    public static StateListDrawable createEditTextBackground(String focusedColor, String unfocusedColor) {
        // Focused state drawable
        GradientDrawable focusedDrawable = new GradientDrawable();
        focusedDrawable.setShape(GradientDrawable.RECTANGLE);
        focusedDrawable.setCornerRadius(6f); // adjust corner radius as needed
        focusedDrawable.setStroke(2, Color.parseColor(focusedColor)); // stroke width and color
        focusedDrawable.setColor(Color.WHITE); // background color

        // Unfocused state drawable
        GradientDrawable unfocusedDrawable = new GradientDrawable();
        unfocusedDrawable.setShape(GradientDrawable.RECTANGLE);
        unfocusedDrawable.setCornerRadius(6f);
        unfocusedDrawable.setStroke(2, Color.parseColor(unfocusedColor));
        unfocusedDrawable.setColor(Color.WHITE);

        // State list drawable
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_focused}, focusedDrawable);
        states.addState(new int[]{}, unfocusedDrawable); // default state

        return states;
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
