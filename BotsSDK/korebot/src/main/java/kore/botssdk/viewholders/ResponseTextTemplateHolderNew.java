package kore.botssdk.viewholders;

import android.view.LayoutInflater;
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

public class ResponseTextTemplateHolderNew extends BaseViewHolderNew {
    public static ResponseTextTemplateHolderNew getInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_bubble_text, parent, false);
        return new ResponseTextTemplateHolderNew(view);
    }

    private ResponseTextTemplateHolderNew(@NonNull View view) {
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
//                return new ResponseTextTemplateHolderNew(ownLayoutInflater.inflate(R.layout.layout_bubble_text_template, parent, false), payOuter.getText());
        } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType())) {
            message = payOuter.getPayload().getText();
            isError = true;
//                return new ResponseTextTemplateHolderNew(ownLayoutInflater.inflate(R.layout.layout_bubble_text_template, parent, false), payOuter.getPayload().getText());
        }
        PayloadInner payInner;
        if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
            message = payOuter.getText().replace("&quot;", "\"");
        }
        payInner = payOuter.getPayload();
        if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
            if (!BotResponse.TEMPLATE_TYPE_DATE.equalsIgnoreCase(payInner.getTemplate_type()))
                message = payInner.getText();
            else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getText_message()))
                message = payInner.getText_message();
        } else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getTemplate_type()))
            message = payInner.getTemplate_type();
        else if (StringUtils.isNullOrEmptyWithTrim(payOuter.getText())) {
//                            timeStampsTextView.setText("");
            message = "default";
        }
        if (!isError) {
            setResponseText((LinearLayoutCompat) itemView, message);
        } else {
            populateErrorText((LinearLayoutCompat) itemView, message, payInner != null && payInner.getColor() != null ? payInner.getColor() : "#000000");
        }
    }
}
