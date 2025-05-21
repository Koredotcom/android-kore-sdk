package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.utils.StringUtils;

public class ResponseTextTemplateHolder extends BaseViewHolder {
    public static ResponseTextTemplateHolder getInstance(ViewGroup parent) {
        return new ResponseTextTemplateHolder(createView(R.layout.template_bubble_text, parent));
    }

    private ResponseTextTemplateHolder(@NonNull View view) {
        super(view, view.getContext());
        initBubbleText((LinearLayoutCompat) view, false);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        ComponentModel componentModel = getComponentModel(baseBotMessage);
        String compType = componentModel.getType();
        PayloadOuter payOuter = componentModel.getPayload();
        String message = "";
        boolean isError = false;
        if (BotResponse.COMPONENT_TYPE_TEXT.equalsIgnoreCase(compType)) {
            message = payOuter.getText();
        } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType())) {
            message = payOuter.getPayload().getText();
            isError = true;
        } else if (payOuter.getType() != null && payOuter.getType().equals(BotResponse.COMPONENT_TYPE_TEXT)) {
            message = payOuter.getText();
        }
        PayloadInner payInner;
        if (payOuter.getText() != null) {
            if (payOuter.getText().contains("&quot")) message = payOuter.getText().replace("&quot;", "\"");
            else message = payOuter.getText();
        }
        payInner = payOuter.getPayload();
        if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
            if (!BotResponse.TEMPLATE_TYPE_DATE.equalsIgnoreCase(payInner.getTemplate_type()) &&
                    !BotResponse.TEMPLATE_TYPE_DATE_RANGE.equalsIgnoreCase(payInner.getTemplate_type()))
                message = payInner.getText();
        } else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getText_message()))
            message = payInner.getText_message();
        else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getTitle()))
            message = payInner.getTitle();
        else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getHeading()))
            message = payInner.getHeading();
        else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getTemplate_type())) {
            message = payInner.getTemplate_type();
        } else if (StringUtils.isNullOrEmptyWithTrim(payOuter.getText()) && payOuter.getType() != null) {
            message = payOuter.getType();
        }
        if (!isError) {
            setResponseText(itemView.findViewById(R.id.layoutBubble), message, baseBotMessage.getTimeStamp());
        } else {
            populateErrorText((LinearLayoutCompat) itemView, message, payInner != null && payInner.getColor() != null ? payInner.getColor() : "#000000");
        }
    }
}
