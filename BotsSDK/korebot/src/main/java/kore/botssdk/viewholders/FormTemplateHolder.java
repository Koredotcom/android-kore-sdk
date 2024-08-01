package kore.botssdk.viewholders;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotFormTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotFormTemplateModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.AutoExpandListView;

public class FormTemplateHolder extends BaseViewHolder {
    private final AutoExpandListView autoExpandListView;
    private final TextView tvFormTemplateTitle;
    private final TextView btFieldButton;
    private final String leftTextColor;

    public static FormTemplateHolder getInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_form, parent, false);
        return new FormTemplateHolder(view);
    }

    private FormTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());

        autoExpandListView = itemView.findViewById(R.id.multi_select_list);
        autoExpandListView.setVerticalScrollBarEnabled(false);
        KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        tvFormTemplateTitle = itemView.findViewById(R.id.tvform_template_title);
        btFieldButton = itemView.findViewById(R.id.btfieldButton);

        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        String leftBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#EBEBEB");
        leftTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#000000");

        GradientDrawable leftDrawable = (GradientDrawable) ResourcesCompat.getDrawable(
                itemView.getContext().getResources(),
                R.drawable.theme1_left_bubble_bg,
                itemView.getContext().getTheme()
        );
        if (leftDrawable != null) {
            leftDrawable.setColor(Color.parseColor(leftBgColor));
            leftDrawable.setStroke((int) (1 * dp1), Color.parseColor(leftBgColor));
            itemView.setBackground(leftDrawable);
        }
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        ArrayList<BotFormTemplateModel> items = new ArrayList<>();

        if (payloadInner.getBotFormTemplateModels() != null && payloadInner.getBotFormTemplateModels().size() > 0)
            items.addAll(payloadInner.getBotFormTemplateModels());

        tvFormTemplateTitle.setText(payloadInner.getHeading());
        BotFormTemplateAdapter botFormTemplateAdapter = new BotFormTemplateAdapter(itemView.getContext(), payloadInner.getBotFormTemplateModels());
        botFormTemplateAdapter.setBotFormTemplates(items);
        botFormTemplateAdapter.setEnabled(isLastItem());
        botFormTemplateAdapter.setTextColor(leftTextColor);
        autoExpandListView.setAdapter(botFormTemplateAdapter);
        botFormTemplateAdapter.notifyDataSetChanged();

        if (!StringUtils.isNullOrEmpty(leftTextColor)) {
            tvFormTemplateTitle.setTextColor(Color.parseColor(leftTextColor));
            btFieldButton.setTextColor(Color.parseColor(leftTextColor));
        }

        if (payloadInner.getFieldButton() != null)
            if (!StringUtils.isNullOrEmpty(payloadInner.getFieldButton().getTitle()))
                btFieldButton.setText(payloadInner.getFieldButton().getTitle());

        btFieldButton.setOnClickListener(view -> {
            if (composeFooterInterface != null && isLastItem()) {
                int listLength = autoExpandListView.getChildCount();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < listLength; i++) {
                    view = autoExpandListView.getChildAt(i);
                    EditText et = view.findViewById(R.id.edtFormInput);
                    sb.append(et.getText().toString());
                }
                composeFooterInterface.onSendClick(getDotMessage(sb.toString()), sb.toString(), false);
            }
        });
    }

    String getDotMessage(String strPassword) {
        StringBuilder strDots = new StringBuilder();
        for (int i = 0; i < strPassword.length(); i++) {
            strDots.append("•");
        }
        return strDots.toString();
    }
}
