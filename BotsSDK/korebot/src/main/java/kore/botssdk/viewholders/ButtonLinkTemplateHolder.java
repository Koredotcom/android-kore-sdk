package kore.botssdk.viewholders;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;

import kore.botssdk.R;
import kore.botssdk.adapter.BotButtonLinkTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.AutoExpandListView;

public class ButtonLinkTemplateHolder extends BaseViewHolderNew {
    private final AutoExpandListView autoExpandListView;
    private final TextView tvButtonLinkTitle;
    private final Context context;

    public ButtonLinkTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        context = itemView.getContext();
        autoExpandListView = itemView.findViewById(R.id.botCustomButtonList);
        tvButtonLinkTitle = itemView.findViewById(R.id.tvButtonLinkTitle);
        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        String leftBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#FFFFFF");
        GradientDrawable leftDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.theme1_left_bubble_bg, context.getTheme());
        leftDrawable.setColor(Color.parseColor(leftBgColor));
        leftDrawable.setStroke((int) (1 * dp1), Color.parseColor(leftBgColor));
        tvButtonLinkTitle.setBackground(leftDrawable);
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

        BotButtonLinkTemplateAdapter buttonTypeAdapter = new BotButtonLinkTemplateAdapter(context, payloadInner.getButtons(), 1);
        buttonTypeAdapter.setEnabled(isLastItem());
        autoExpandListView.setAdapter(buttonTypeAdapter);
//        buttonTypeAdapter.setCheckedPosition(payloadInner.getCheckedPosition());
        buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
        buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        buttonTypeAdapter.notifyDataSetChanged();
    }
}
