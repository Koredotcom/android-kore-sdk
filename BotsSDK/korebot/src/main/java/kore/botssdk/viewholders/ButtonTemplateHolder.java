package kore.botssdk.viewholders;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.ButtonTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.PayloadInner;

public class ButtonTemplateHolder extends BaseViewHolderNew {

    public static ButtonTemplateHolder getInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_button, parent, false);
        return new ButtonTemplateHolder(view);
    }

    private ButtonTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) layoutBubble.getLayoutParams();
        params.bottomMargin = (int) (10 * dp1);
        initBubbleText(layoutBubble, false);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        ArrayList<BotButtonModel> botButtonModels = payloadInner.getButtons();
        final ButtonTemplateAdapter buttonTypeAdapter;
        RecyclerView buttonsList = itemView.findViewById(R.id.buttonsList);
        buttonsList.setLayoutManager(new LinearLayoutManager(buttonsList.getContext()));

        buttonTypeAdapter = new ButtonTemplateAdapter(buttonsList.getContext());
        buttonsList.setAdapter(buttonTypeAdapter);
        buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
        buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        buttonTypeAdapter.populateData(botButtonModels, isLastItem());
    }
}
