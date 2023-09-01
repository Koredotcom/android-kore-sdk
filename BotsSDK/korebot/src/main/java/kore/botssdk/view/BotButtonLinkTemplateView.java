package kore.botssdk.view;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;

import java.util.Locale;

import kore.botssdk.R;
import kore.botssdk.adapter.BotButtonLinkTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RadioListListner;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class BotButtonLinkTemplateView extends LinearLayout implements RadioListListner {

    private float dp1, layoutItemHeight = 0;
    private ListView autoExpandListView;
    private float restrictedMaxWidth, restrictedMaxHeight;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private TextView tvButtonLinkTitle;
    private LinearLayout llRoot;
    private PayloadInner payloadInner;
    private View inflatedView;
    private GradientDrawable leftDrawable;
    private String leftbgColor, leftTextColor;
    private SharedPreferences sharedPreferences;

    public BotButtonLinkTemplateView(Context context) {
        super(context);
        init(context);
    }

    public BotButtonLinkTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BotButtonLinkTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        dp1 = DimensionUtil.dp1;
        inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_button_link_view, this, true);
        autoExpandListView = inflatedView.findViewById(R.id.botCustomButtonList);
        llRoot = inflatedView.findViewById(R.id.llRoot);
        tvButtonLinkTitle = inflatedView.findViewById(R.id.tvButtonLinkTitle);

        sharedPreferences = getSharedPreferences(context);
        leftbgColor= sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#EBEBEB");
        leftTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#000000");

        layoutItemHeight = getResources().getDimension(R.dimen.dimen_40dp);
    }

    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
        this.restrictedMaxWidth = restrictedMaxWidth;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void populateButtonList(PayloadInner payloadInner, final boolean enabled, int type)
    {
        final BotButtonLinkTemplateAdapter buttonTypeAdapter;
        if (payloadInner != null && payloadInner.getButtons() != null)
        {
            this.payloadInner = payloadInner;
            leftDrawable = (GradientDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.theme1_left_bubble_bg, getContext().getTheme());

            if(leftDrawable != null)
            {
                leftDrawable.setColor(Color.parseColor(leftbgColor));
                leftDrawable.setStroke((int) (1*dp1), Color.parseColor(leftbgColor));
                tvButtonLinkTitle.setBackground(leftDrawable);
            }

            if(!StringUtils.isNullOrEmpty(payloadInner.getText()))
            {
                String textualContent = unescapeHtml4(payloadInner.getText().trim());
                textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                textualContent = MarkdownUtil.processMarkDown(textualContent);
                CharSequence sequence = HtmlCompat.fromHtml(textualContent.replace("\n", "<br />"), HtmlCompat.FROM_HTML_MODE_LEGACY,
                        new MarkdownImageTagHandler(getContext(), tvButtonLinkTitle , textualContent), new MarkdownTagHandler());
                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                tvButtonLinkTitle.setText(strBuilder);
                tvButtonLinkTitle.setMovementMethod(null);
            }

            autoExpandListView.setVisibility(VISIBLE);
            buttonTypeAdapter = new BotButtonLinkTemplateAdapter(getContext(), payloadInner.getButtons(), type);
            buttonTypeAdapter.setEnabled(true);
            autoExpandListView.setAdapter(buttonTypeAdapter);
            buttonTypeAdapter.setRadioListInterface(BotButtonLinkTemplateView.this);
            buttonTypeAdapter.setCheckedPosition(payloadInner.getCheckedPosition());
            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            buttonTypeAdapter.notifyDataSetChanged();
        } else {
            autoExpandListView.setAdapter(null);
            autoExpandListView.setVisibility(GONE);
        }
    }

    private SharedPreferences getSharedPreferences(Context context)
    {
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    @Override
    public void radioItemClicked(int position) {
        if(payloadInner != null)
            payloadInner.setCheckedPosition(position);
    }
}
