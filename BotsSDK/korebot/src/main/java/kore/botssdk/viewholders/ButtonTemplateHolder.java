package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;

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

public class ButtonTemplateHolder extends BaseViewHolder {

    public static ButtonTemplateHolder getInstance(ViewGroup parent) {
        return new ButtonTemplateHolder(createView(R.layout.template_button, parent));
    }

    private ButtonTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText(), baseBotMessage.getTimeStamp());
        ArrayList<BotButtonModel> botButtonModels = payloadInner.getButtons();
        final ButtonTemplateAdapter buttonTypeAdapter;
        RecyclerView buttonsList = itemView.findViewById(R.id.buttonsList);
        String variation = payloadInner.getVariation() != null ? payloadInner.getVariation() : "";

         switch (variation) {
            case BotResponse.PLAIN:
            case BotResponse.TEXT_INVERTED:
            case BotResponse.BACKGROUND_INVERTED:
                buttonsList.setLayoutManager(new LinearLayoutManager(buttonsList.getContext(), LinearLayoutManager.HORIZONTAL, false));
                break;
            case BotResponse.STACKED_BUTTONS:
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(itemView.getContext());
                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                layoutManager.setFlexDirection(FlexDirection.ROW);
                buttonsList.setLayoutManager(layoutManager);
                break;
            default:
                buttonsList.setLayoutManager(new LinearLayoutManager(buttonsList.getContext(), LinearLayoutManager.VERTICAL, false));
        }

        if (payloadInner.isFullWidth()) {
            buttonsList.setLayoutManager(new LinearLayoutManager(buttonsList.getContext(), LinearLayoutManager.VERTICAL, false));
        }

        if (payloadInner.isStackedButtons()) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(itemView.getContext());
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            buttonsList.setLayoutManager(layoutManager);
        }

        if (botButtonModels != null && !botButtonModels.isEmpty()) {
            buttonTypeAdapter = new ButtonTemplateAdapter(
                    buttonsList.getContext(), botButtonModels, isLastItem(), payloadInner.isFullWidth(), payloadInner.isStackedButtons(), payloadInner.getVariation()
            );
            buttonsList.setAdapter(buttonTypeAdapter);
            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
    }
}
