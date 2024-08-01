package kore.botssdk.viewholders;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.text.HtmlCompat;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.view.LinkifyTextView;

public class RequestTextTemplateHolderNew extends BaseViewHolder {

    public static RequestTextTemplateHolderNew getInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_bubble_text, parent, false);
        return new RequestTextTemplateHolderNew(view);
    }
    private RequestTextTemplateHolderNew(@NonNull View view) {
        super(view, view.getContext());
        initBubbleText((LinearLayoutCompat) view, true);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        RestResponse.BotMessage message = ((BotRequest)baseBotMessage).getMessage();
        String msg =  message!= null ? message.getBody() : "";
        setRequestText(msg);
    }

    private void setRequestText(String textualContent) {
        LinkifyTextView bubbleText = itemView.findViewById(R.id.bubble_text);
//        if (bubbleText == null) return;
        bubbleText.setText("");
        Context context = bubbleText.getContext();
        if (textualContent != null && !textualContent.isEmpty()) {
            textualContent = unescapeHtml4(textualContent.trim());
            textualContent = StringUtils.unescapeHtml3(textualContent.trim());
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

            bubbleText.setText(strBuilder);
            bubbleText.setMovementMethod(null);
            bubbleText.setVisibility(View.VISIBLE);
        } else {
            bubbleText.setText("");
            bubbleText.setVisibility(View.GONE);
        }
    }
}
