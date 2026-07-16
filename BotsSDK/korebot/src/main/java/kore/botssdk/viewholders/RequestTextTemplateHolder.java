package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.text.HtmlCompat;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.EmojiUtils;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.view.LinkifyTextView;

public class RequestTextTemplateHolder extends BaseViewHolder {

    public static RequestTextTemplateHolder getInstance(ViewGroup parent) {
        return new RequestTextTemplateHolder(createView(R.layout.template_bubble_text, parent));
    }

    private RequestTextTemplateHolder(@NonNull View view) {
        super(view, view.getContext());
        LinearLayoutCompat layoutBubble = view.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, true);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        RestResponse.BotMessage message = ((BotRequest) baseBotMessage).getMessage();
        String msg = message != null ? message.getBody() : "";
        setRequestText(msg, ((BotRequest) baseBotMessage).getStatus(), baseBotMessage);
    }

    private void setRequestText(String textualContent, BotRequest.MessageStatus status, BaseBotMessage message) {
        LinkifyTextView bubbleText = itemView.findViewById(R.id.bubble_text);
        LinearLayoutCompat llSendAgain = itemView.findViewById(R.id.llSendAgain);
        TextView tvSend = itemView.findViewById(R.id.tvSend);
        TextView tvDelete = itemView.findViewById(R.id.tvDelete);

        llSendAgain.setVisibility(GONE);

        if(status == BotRequest.MessageStatus.FAILED) {
            llSendAgain.setVisibility(VISIBLE);
        }

        bubbleText.setText("");
        Context context = bubbleText.getContext();

        Typeface regular = KaFontUtils.getCustomTypeface("regular", context);
        if(SDKConfiguration.getRegular() != null) {
            regular = SDKConfiguration.getRegular();
        }

        if (textualContent != null && !textualContent.isEmpty()) {
            if (SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable)
                textualContent = EmojiUtils.replaceEmoticonsWithEmojis(textualContent);
            textualContent = unescapeHtml4(textualContent.trim());
            textualContent = StringUtils.unescapeHtml3(textualContent.trim());
            CharSequence sequence = HtmlCompat.fromHtml(
                    textualContent.replace("\n", "<br />"),
                    HtmlCompat.FROM_HTML_MODE_LEGACY,
                    new MarkdownImageTagHandler(context, bubbleText, textualContent),
                    new MarkdownTagHandler()
            );
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            
            // 1. Capture existing spans from Markdown/HTML
            // 1. Capture ALL existing spans from Markdown/HTML (Bold, Italic, Links, etc.)
            Object[] existingSpans = strBuilder.getSpans(0, strBuilder.length(), Object.class);
            int[] starts = new int[existingSpans.length];
            int[] ends = new int[existingSpans.length];
            int[] flags = new int[existingSpans.length];

            for (int i = 0; i < existingSpans.length; i++) {
                starts[i] = strBuilder.getSpanStart(existingSpans[i]);
                ends[i] = strBuilder.getSpanEnd(existingSpans[i]);
                flags[i] = strBuilder.getSpanFlags(existingSpans[i]);
            }

            // 2. Add links for plain text URLs and Phone numbers
            Linkify.addLinks(strBuilder, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);

            // 3. Re-apply all original spans to ensure NO formatting is lost
            for (int i = 0; i < existingSpans.length; i++) {
                if (starts[i] >= 0 && ends[i] <= strBuilder.length() && starts[i] <= ends[i])
                    strBuilder.setSpan(existingSpans[i], starts[i], ends[i], flags[i]);
            }

            URLSpan[] urls = strBuilder.getSpans(0, strBuilder.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }

            bubbleText.setTypeface(regular);
            bubbleText.setText(strBuilder);
            bubbleText.setMovementMethod(null);
            bubbleText.setVisibility(VISIBLE);

            itemView.setOnLongClickListener(v -> {
                composeFooterInterface.copyMessageToComposer(bubbleText.getText().toString(), false);
                return false;
            });

            tvSend.setOnClickListener(v -> {
                composeFooterInterface.onSendClick(message, false);
            });

            tvDelete.setOnClickListener( v -> composeFooterInterface.onDeleteClick(message));

        } else {
            bubbleText.setText("");
            bubbleText.setVisibility(View.GONE);
        }
    }
}
