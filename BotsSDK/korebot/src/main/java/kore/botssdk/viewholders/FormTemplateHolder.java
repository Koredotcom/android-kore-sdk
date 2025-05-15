package kore.botssdk.viewholders;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.FormTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.ToastUtils;

public class FormTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;
    private final TextView tvFormTemplateTitle;
    private final TextView btFieldButton;
    private final String leftTextColor;
    final LinearLayout llFormRoot;

    public static FormTemplateHolder getInstance(ViewGroup parent) {
        return new FormTemplateHolder(createView(R.layout.template_form, parent));
    }

    private FormTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        llFormRoot = itemView.findViewById(R.id.llFormRoot);
        recyclerView = itemView.findViewById(R.id.multi_select_list);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        tvFormTemplateTitle = itemView.findViewById(R.id.tvform_template_title);
        btFieldButton = itemView.findViewById(R.id.btfieldButton);

        GradientDrawable gradientDrawable = (GradientDrawable) btFieldButton.getBackground().mutate();
        gradientDrawable.setStroke((int) (1 * dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        btFieldButton.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor));

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
            llFormRoot.setBackground(leftDrawable);
        }
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;

        tvFormTemplateTitle.setText(payloadInner.getHeading());
        FormTemplateAdapter botFormTemplateAdapter = new FormTemplateAdapter(payloadInner.getBotFormTemplateModels(), leftTextColor);
        recyclerView.setAdapter(botFormTemplateAdapter);

        if (!StringUtils.isNullOrEmpty(leftTextColor)) {
            tvFormTemplateTitle.setTextColor(Color.parseColor(leftTextColor));
        }

        if (payloadInner.getFieldButton() != null)
            btFieldButton.setText(payloadInner.getFieldButton().getTitle());

        btFieldButton.setOnClickListener(view -> {
            if (composeFooterInterface != null && isLastItem()) {
                int listLength = recyclerView.getChildCount();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < listLength; i++) {
                    view = recyclerView.getChildAt(i);
                    EditText et = view.findViewById(R.id.edtFormInput);
                    sb.append(et.getText().toString());
                }

                if (StringUtils.isNotEmpty(sb.toString()))
                    composeFooterInterface.onSendClick(getDotMessage(sb.toString()), sb.toString(), false);
                else
                    ToastUtils.showToast(context, "Text should not be empty.");
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
