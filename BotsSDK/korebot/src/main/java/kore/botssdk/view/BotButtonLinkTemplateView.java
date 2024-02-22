package kore.botssdk.view;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;

import kore.botssdk.R;
import kore.botssdk.adapter.BotButtonLinkTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RadioListListner;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class BotButtonLinkTemplateView extends LinearLayout implements RadioListListner {

    private float dp1;
    private ListView autoExpandListView;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private TextView tvButtonLinkTitle;
    private PayloadInner payloadInner;
    private String leftBgColor;
    private SharedPreferences sharedPreferences;

    public BotButtonLinkTemplateView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BotButtonLinkTemplateView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BotButtonLinkTemplateView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        dp1 = DimensionUtil.dp1;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_button_link_view, this, true);
        autoExpandListView = inflatedView.findViewById(R.id.botCustomButtonList);
        tvButtonLinkTitle = inflatedView.findViewById(R.id.tvButtonLinkTitle);

        sharedPreferences = getSharedPreferences(context);
        leftBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#EBEBEB");
    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void populateButtonList(@SuppressLint("UnknownNullness") PayloadInner payloadInner, int type) {
        final BotButtonLinkTemplateAdapter buttonTypeAdapter;
        if (payloadInner != null && payloadInner.getButtons() != null) {
            this.payloadInner = payloadInner;
            GradientDrawable leftDrawable = (GradientDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.theme1_left_bubble_bg, getContext().getTheme());

            if (leftDrawable != null) {
                leftDrawable.setColor(Color.parseColor(leftBgColor));
                leftDrawable.setStroke((int) (1 * dp1), Color.parseColor(leftBgColor));
                tvButtonLinkTitle.setBackground(leftDrawable);
            }

            if (!StringUtils.isNullOrEmpty(payloadInner.getText())) {
                String textualContent = unescapeHtml4(payloadInner.getText().trim());
                textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                textualContent = MarkdownUtil.processMarkDown(textualContent);
                CharSequence sequence = HtmlCompat.fromHtml(textualContent.replace("\n", "<br />"), HtmlCompat.FROM_HTML_MODE_LEGACY,
                        new MarkdownImageTagHandler(getContext(), tvButtonLinkTitle, textualContent), new MarkdownTagHandler());
                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                tvButtonLinkTitle.setText(strBuilder);
                tvButtonLinkTitle.setMovementMethod(null);
            }

            autoExpandListView.setVisibility(VISIBLE);
            buttonTypeAdapter = new BotButtonLinkTemplateAdapter(getContext(), payloadInner.getButtons(), type);
            buttonTypeAdapter.setEnabled(true);
            autoExpandListView.setAdapter(buttonTypeAdapter);
            buttonTypeAdapter.setRadioListInterface(BotButtonLinkTemplateView.this);
            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            buttonTypeAdapter.notifyDataSetChanged();
        } else {
            autoExpandListView.setAdapter(null);
            autoExpandListView.setVisibility(GONE);
        }
    }

    private SharedPreferences getSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    @Override
    public void radioItemClicked(int position) {
        if (payloadInner != null)
            payloadInner.setCheckedPosition(position);
    }
}
