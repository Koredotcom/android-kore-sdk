package kore.botssdk.viewholders;

import static kore.botssdk.models.BotResponse.DIGITAL_FORM;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.ButtonTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;

public class DigitalFormTemplateHolder extends BaseViewHolder {

    public static DigitalFormTemplateHolder getInstance(ViewGroup parent) {
        return new DigitalFormTemplateHolder(createView(R.layout.template_digital_form, parent));
    }

    private DigitalFormTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText(), baseBotMessage.getTimeStamp());
        ArrayList<BotButtonModel> botButtonModels = payloadInner.getButtons();
        final ButtonTemplateAdapter buttonTypeAdapter;
        RecyclerView buttonsList = itemView.findViewById(R.id.buttonsList);
        TextView tvTitle = itemView.findViewById(R.id.title);
        tvTitle.setText(payloadInner.getText());
        buttonsList.setLayoutManager(new LinearLayoutManager(buttonsList.getContext(), LinearLayoutManager.HORIZONTAL, false));

        if (botButtonModels != null && !botButtonModels.isEmpty()) {
            buttonTypeAdapter = new ButtonTemplateAdapter(
                    buttonsList.getContext(), botButtonModels, isLastItem(), payloadInner.isFullWidth(), payloadInner.isStackedButtons(), DIGITAL_FORM
            );
            buttonsList.setAdapter(buttonTypeAdapter);
            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
    }
}
