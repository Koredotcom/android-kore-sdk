package kore.botssdk.viewholders;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;
import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.itemdecoration.ChatAdapterItemDecoration;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.EntityEditModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.EmojiUtils;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.LinkifyTextView;

@SuppressWarnings("UnKnownNullness")
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public static final int[] MATERIAL_COLORS = {
            Color.parseColor("#4A9AF2"), Color.parseColor("#5BC8C4"), Color.parseColor("#e74c3c"), Color.parseColor("#3498db")
    };
    public static final int HOLO_BLUE = Color.rgb(51, 181, 229);
    private final Context context;
    private final String REGEX_CHAR = "%%.*?%%";
    private final Gson gson = new Gson();
    private boolean isLastItem = true;
    private LinkifyTextView bubbleText;

    protected ComposeFooterInterface composeFooterInterface;
    protected InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    protected ChatContentStateListener contentStateListener;
    SharedPreferences sharedPreferences;

    protected static View createView(int layoutId, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View baseView = inflater.inflate(R.layout.base_view_holder, parent, false);
        View childView = inflater.inflate(layoutId, null);
        LinearLayoutCompat contentLayout = (LinearLayoutCompat) baseView.findViewById(R.id.contentLayout);
        contentLayout.addView(childView, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        return baseView;
    }

    public BaseViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        TextView timeStamp = itemView.findViewById(R.id.time_stamp);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) timeStamp.getLayoutParams();
        if (this instanceof RequestTextTemplateHolder) {
            params.setMarginEnd(ChatAdapterItemDecoration.messageMargin / 2);
        } else {
            params.setMarginStart(ChatAdapterItemDecoration.messageMargin / 2);
        }
        if (SDKConfiguration.BubbleColors.showIcon && !SDKConfiguration.OverrideKoreConfig.showIconTop) {
            ImageView botIcon = itemView.findViewById(R.id.bot_icon);
            params = (LinearLayoutCompat.LayoutParams) botIcon.getLayoutParams();
            params.gravity = Gravity.BOTTOM;
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setBotIcon(String iconUrl) {
        ImageView botIcon = itemView.findViewById(R.id.bot_icon);
        if (botIcon != null) {
            boolean isShowIcon = SDKConfiguration.BubbleColors.showIcon && iconUrl != null;
            botIcon.setVisibility(isShowIcon ? View.VISIBLE : View.GONE);
            if (isShowIcon) {
                Glide.with(context).load(iconUrl).error(R.drawable.ic_launcher).into(new DrawableImageViewTarget(botIcon));
            }

            if (SDKConfiguration.isTimeStampsRequired() && SDKConfiguration.OverrideKoreConfig.showIconTop) {
                LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) botIcon.getLayoutParams();
                params.topMargin = (int) (21 * dp1);
            }
        }
    }

    public void setMsgTime(String msgTime, boolean isBotRequest) {
        TextView msgTimeView = itemView.findViewById(R.id.msg_time);
        msgTimeView.setVisibility(SDKConfiguration.isTimeStampsRequired() ? View.VISIBLE : View.GONE);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) msgTimeView.getLayoutParams();

        if (SDKConfiguration.isTimeStampsRequired()) {
            LinearLayoutCompat contentLayout = itemView.findViewById(R.id.contentLayout);
            if (isBotRequest) {
                contentLayout.setGravity(Gravity.END);
                params.rightMargin = (int) (5 * dp1);
            }

            msgTimeView.setText(HtmlCompat.fromHtml(msgTime, HtmlCompat.FROM_HTML_MODE_COMPACT));
            msgTimeView.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#B0B0B0")));
        }
    }

    public void setTimeStamp(String timeStamp) {
        TextView timeStampView = itemView.findViewById(R.id.time_stamp);
        timeStampView.setVisibility(timeStamp != null && !timeStamp.isEmpty() ? View.VISIBLE : View.GONE);
        timeStampView.setText(timeStamp);
        timeStampView.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#B0B0B0")));
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public boolean isLastItem() {
        return isLastItem;
    }

    public void setIsLastItem(boolean lastItem) {
        isLastItem = lastItem;
    }

    public abstract void bind(BaseBotMessage baseBotMessage);

    protected ComponentModel getComponentModel(BaseBotMessage baseBotMessage) {
        ComponentModel compModel = null;
        if (baseBotMessage instanceof BotResponse && ((BotResponse) baseBotMessage).getMessage() != null && !((BotResponse) baseBotMessage).getMessage().isEmpty()) {
            compModel = ((BotResponse) baseBotMessage).getMessage().get(0).getComponent();
        }
        return compModel;
    }

    private String getRemovedEntityEditString(String _str) {
        String str = _str.replaceAll(REGEX_CHAR, "");
        str = str.replaceAll("\\s{2,}", " ");
        return str;
    }

    private String getReqText(String str) {
        String newT = str;

        Pattern pattern = Pattern.compile(REGEX_CHAR);
        Matcher matcher1 = pattern.matcher(str);

        while (matcher1.find()) {
            int _start = matcher1.start();
            int _end = matcher1.end();

            String replaceText = str.substring(_start, _end);
            String _payload = replaceText.substring(2, replaceText.length() - 2);
            LogUtils.d("!@#$% getReqText(", _payload);
            _payload = _payload.substring(_payload.indexOf("{"));
            EntityEditModel model = gson.fromJson(_payload, EntityEditModel.class);
            String addableText = !StringUtils.isNullOrEmpty(model.getTitle()) ? model.getTitle().trim() : "";

            newT = newT.replace(replaceText, addableText + replaceText);
        }
        return newT;
    }

    protected void initBubbleText(LinearLayoutCompat layoutBubble, boolean isBotRequest) {
        bubbleText = layoutBubble.findViewById(R.id.bubble_text);
        bubbleText.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        String leftBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#FFFFFF");
        String leftTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#000000");
        String rightBgColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_BG_COLOR, "#B2E3E9");
        String rightTextColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, "#000000");
        String bubble_style = sharedPreferences.getString(BotResponse.BUBBLE_STYLE, "rounded");

        //1st & 2nd - topLeft, 3rd & 4th - topRight, 5th & 6th - bottomRight 7th & 8th - bottomLeft
        float[] roundedRadii = {16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1};
        float[] recRightRadii = {8 * dp1, 8 * dp1, 2 * dp1, 2 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1};
        float[] recLeftRadii = {2 * dp1, 2 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1};
        float[] sqrRightRadii = {8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 2 * dp1, 2 * dp1, 8 * dp1, 8 * dp1};
        float[] balRightRadii = {8 * dp1, 8 * dp1, 2 * dp1, 2 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1};
        float[] sqrLeftRadii = {8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 2 * dp1, 2 * dp1};
        float[] balLeftRadii = {2 * dp1, 2 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1};
        final boolean circle = bubble_style.equalsIgnoreCase(BundleConstants.ROUNDED) || bubble_style.equalsIgnoreCase(BundleConstants.CIRCLE);

        if (!isBotRequest) {
            if (!(this instanceof ResponseTextTemplateHolder)) {
                LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) bubbleText.getLayoutParams();
                params.bottomMargin = ChatAdapterItemDecoration.commonVerticalMargin * 2;
            }
            Typeface regular = KaFontUtils.getCustomTypeface("regular", context);

            GradientDrawable leftDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.theme1_left_bubble_bg, context.getTheme());

            if (leftDrawable != null) {
                leftDrawable.setColor(Color.parseColor(leftBgColor));
                leftDrawable.setStroke((int) (1 * dp1), Color.parseColor(leftBgColor));

                if (circle) leftDrawable.setCornerRadii(roundedRadii);
                else if (bubble_style.equalsIgnoreCase(BundleConstants.RECTANGLE)) leftDrawable.setCornerRadii(recLeftRadii);
                else if (bubble_style.equalsIgnoreCase(BundleConstants.SQUARE)) leftDrawable.setCornerRadii(sqrLeftRadii);
                else leftDrawable.setCornerRadii(balLeftRadii);
            }
            layoutBubble.setGravity(Gravity.START);
            bubbleText.setTypeface(regular);
            bubbleText.setBackground(leftDrawable);
            bubbleText.setTextColor(Color.parseColor(leftTextColor));
            bubbleText.setAutoLinkMask(Linkify.WEB_URLS);
            bubbleText.setLinkTextColor(Color.parseColor(SDKConfiguration.BubbleColors.leftLinkColor));
        } else {
            layoutBubble.setGravity(Gravity.END);

            Typeface medium = KaFontUtils.getCustomTypeface("medium", context);

            GradientDrawable rightDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.theme1_right_bubble_bg, context.getTheme());

            if (rightDrawable != null) {
                rightDrawable.setColor(Color.parseColor(rightBgColor));
                rightDrawable.setStroke((int) (1 * dp1), Color.parseColor(rightBgColor));

                if (circle) rightDrawable.setCornerRadii(roundedRadii);
                else if (bubble_style.equalsIgnoreCase(BundleConstants.RECTANGLE)) rightDrawable.setCornerRadii(recRightRadii);
                else if (bubble_style.equalsIgnoreCase(BundleConstants.SQUARE)) rightDrawable.setCornerRadii(sqrRightRadii);
                else rightDrawable.setCornerRadii(balRightRadii);
            }

            LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) bubbleText.getLayoutParams();
            params.rightMargin = (int) (5 * dp1);

            bubbleText.setLinkTextColor(Color.parseColor(SDKConfiguration.BubbleColors.rightLinkColor));
            bubbleText.setTypeface(medium);
            bubbleText.setBackground(rightDrawable);
            bubbleText.setTextColor(Color.parseColor(rightTextColor));
        }
    }

    protected void setResponseText(LinearLayoutCompat layoutBubble, String textualContent) {
        if (bubbleText == null) initBubbleText(layoutBubble, false);
        bubbleText.setVisibility(View.VISIBLE);
        bubbleText.setText("");
        if (textualContent != null && !textualContent.isEmpty()) {
            if (SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable)
                textualContent = EmojiUtils.replaceEmoticonsWithEmojis(textualContent);
            textualContent = unescapeHtml4(textualContent.trim());
            textualContent = StringUtils.unescapeHtml3(textualContent.trim());
            textualContent = MarkdownUtil.processMarkDown(textualContent);
            CharSequence sequence = HtmlCompat.fromHtml(
                    textualContent.replace("\n", "<br />"),
                    HtmlCompat.FROM_HTML_MODE_LEGACY,
                    new MarkdownImageTagHandler(context, bubbleText, textualContent),
                    new MarkdownTagHandler()
            );
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }

            Pattern pattern = Pattern.compile(REGEX_CHAR);

            ImageSpan pencilImageSpan;
            boolean isPencilSpanClick = false;

            if (textualContent.indexOf("%%{") > 0) {
                LogUtils.d("!@#$% BEFORE ", textualContent);// munduki%%{} %%
                textualContent = getReqText(textualContent);
                LogUtils.d("!@#$% AFTER ", textualContent);
                strBuilder = new SpannableStringBuilder(textualContent);
            }
            Matcher matcher = pattern.matcher(textualContent);

            while (matcher.find()) {
                pencilImageSpan = new ImageSpan(context, R.drawable.pencil_18);
                isPencilSpanClick = true;
                int start = matcher.start();
                int end = matcher.end();

                String reqText = textualContent.substring(start + 2, end - 2);
                reqText = reqText.substring(reqText.indexOf("{"));

                LogUtils.d("!@#$% REQ_TEXT while", reqText);

                EntityEditModel model = gson.fromJson(reqText, EntityEditModel.class);
                String addableText = !StringUtils.isNullOrEmpty(model.getTitle()) ? model.getTitle().trim() : "";

                int addableTextLength = 0;
                if (!StringUtils.isNullOrEmpty(addableText)) {
                    addableTextLength = addableText.length();
                }

                boolean isIconNeeded = Boolean.parseBoolean(model.isIcon());
                if (isIconNeeded) {
                    strBuilder.setSpan(pencilImageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    strBuilder.setSpan(new ImageSpan(context, R.drawable.transparant_image), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                int dashStart = start - addableTextLength /*- iconLength*/;

                final SpannableStringBuilder finalStrBuilder = strBuilder;
                final String finalReqText = reqText;
                ClickableSpan clickable = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (isLastItem()) {
                            bubbleText.setText(getRemovedEntityEditString(finalStrBuilder.toString()));
                            EntityEditModel model = gson.fromJson(finalReqText, EntityEditModel.class);
                            EntityEditEvent event = new EntityEditEvent();
                            event.setScrollUpNeeded(false);
                            event.setMessage(model.getPostback());
                            KoreEventCenter.post(event);
                        }
                    }
                };
                strBuilder.setSpan(clickable, dashStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (isPencilSpanClick && !isLastItem()) {
                bubbleText.setText(getRemovedEntityEditString(strBuilder.toString()));
            }

            bubbleText.setText(strBuilder);
            bubbleText.setMovementMethod(LinkMovementMethod.getInstance());
            bubbleText.setVisibility(View.VISIBLE);
        } else {
            bubbleText.setText("");
            bubbleText.setVisibility(View.GONE);
        }
    }

    void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(context, GenericWebViewActivity.class);
                intent.putExtra("url", span.getURL());
                intent.putExtra("header", context.getResources().getString(R.string.app_name));
                context.startActivity(intent);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void populateErrorText(LinearLayoutCompat layoutBubble, String textualContent, String color) {
        LinkifyTextView bubbleText = layoutBubble.findViewById(R.id.bubble_text);
        if (bubbleText == null) return;
        if (textualContent != null && !textualContent.isEmpty()) {
            textualContent = unescapeHtml4(textualContent.trim());
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(textualContent);
            URLSpan[] urls = strBuilder.getSpans(0, textualContent.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            bubbleText.setTextColor(Color.parseColor(color));
            bubbleText.setText(strBuilder);
            bubbleText.setMovementMethod(null);
            bubbleText.setVisibility(View.VISIBLE);
        } else {
            bubbleText.setText("");
            bubbleText.setVisibility(View.GONE);
        }
    }

    protected PayloadInner getPayloadInner(BaseBotMessage baseBotMessage) {
        ComponentModel componentModel = getComponentModel(baseBotMessage);
        PayloadOuter payloadOuter = componentModel.getPayload();
        if (payloadOuter == null) return null;
        return payloadOuter.getPayload();
    }

    protected PayloadOuter getPayloadOuter(BaseBotMessage baseBotMessage) {
        ComponentModel componentModel = getComponentModel(baseBotMessage);
        PayloadOuter payloadOuter = componentModel.getPayload();
        return payloadOuter;
    }

    public void setContentStateListener(ChatContentStateListener contentStateListener) {
        this.contentStateListener = contentStateListener;
    }

    protected void setRoundedCorner(View view, float radius) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // Set the corner radius
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        view.setClipToOutline(true);
    }
}