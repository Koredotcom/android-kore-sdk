package kore.botssdk.viewholders;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.ButtonLinkTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;

public class ButtonLinkTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;
    private final TextView tvButtonLinkTitle;
    private final Context context;

    public static ButtonLinkTemplateHolder getInstance(ViewGroup parent) {
        return new ButtonLinkTemplateHolder(createView(R.layout.template_button_link, parent));
    }

    private ButtonLinkTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        context = itemView.getContext();
        recyclerView = itemView.findViewById(R.id.botCustomButtonList);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        tvButtonLinkTitle = itemView.findViewById(R.id.tvButtonLinkTitle);
        LinearLayout llButtonLinkRoot = itemView.findViewById(R.id.llButtonLinkRoot);

        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        String leftBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#FFFFFF");
        String textColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary));
        String dividerColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#FFFFFF");
        String titleColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#000000");
        String listBgColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_BG_COLOR, textColor);

        GradientDrawable leftDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_link_bg, context.getTheme());
        GradientDrawable dividerDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.divider_white, context.getTheme());
        GradientDrawable listDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_link_bg, context.getTheme());

        if (leftDrawable != null) {
            leftDrawable.setColor(Color.parseColor(leftBgColor));
            leftDrawable.setStroke((int) (1 * dp1), Color.parseColor(leftBgColor));
            tvButtonLinkTitle.setBackground(leftDrawable);
            tvButtonLinkTitle.setTextColor(Color.parseColor(titleColor));
        }

        DividerItemDecoration divider = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );

        if (dividerDrawable != null) {
            dividerDrawable.setColor(Color.parseColor(dividerColor));
            divider.setDrawable(dividerDrawable);
        }

        if (listDrawable != null) {
            listDrawable.setColor(Color.parseColor(listBgColor));
            llButtonLinkRoot.setBackground(listDrawable);
        }

        recyclerView.addItemDecoration(divider);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (!StringUtils.isNullOrEmpty(payloadInner.getText())) {
            tvButtonLinkTitle.setVisibility(View.VISIBLE);
            String textualContent = unescapeHtml4(payloadInner.getText().trim());
            textualContent = StringUtils.unescapeHtml3(textualContent.trim());
            textualContent = MarkdownUtil.processMarkDown(textualContent);
            CharSequence sequence = HtmlCompat.fromHtml(
                    textualContent.replace("\n", "<br />"),
                    HtmlCompat.FROM_HTML_MODE_LEGACY,
                    new MarkdownImageTagHandler(context, tvButtonLinkTitle, textualContent),
                    new MarkdownTagHandler()
            );
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            tvButtonLinkTitle.setText(strBuilder);
            tvButtonLinkTitle.setMovementMethod(null);
        } else {
            tvButtonLinkTitle.setVisibility(View.GONE);
        }

        ButtonLinkTemplateAdapter buttonTypeAdapter = new ButtonLinkTemplateAdapter(recyclerView.getContext(), payloadInner.getButtons(), isLastItem());
        recyclerView.setAdapter(buttonTypeAdapter);
        buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
        buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
    }
}