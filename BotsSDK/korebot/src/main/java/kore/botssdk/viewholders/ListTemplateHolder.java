package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotListTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.view.AutoExpandListView;

public class ListTemplateHolder extends BaseViewHolder {
    public static ListTemplateHolder getInstance(ViewGroup parent) {
        return new ListTemplateHolder(createView(R.layout.template_list, parent));
    }

    private ListTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        AutoExpandListView autoExpandListView = itemView.findViewById(R.id.botCustomListView);
        TextView botCustomListViewButton = itemView.findViewById(R.id.botCustomListViewButton);
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        ArrayList<BotListModel> listElements = payloadInner.getListElements();
        ArrayList<BotButtonModel> buttons = payloadInner.getButtons();
        BotListTemplateAdapter botListTemplateAdapter;
        if (autoExpandListView.getAdapter() == null) {
            botListTemplateAdapter = new BotListTemplateAdapter(itemView.getContext(), autoExpandListView);
            autoExpandListView.setAdapter(botListTemplateAdapter);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        } else {
            botListTemplateAdapter = (BotListTemplateAdapter) autoExpandListView.getAdapter();
        }
        botListTemplateAdapter.setBotListModelArrayList(listElements);
        botListTemplateAdapter.notifyDataSetChanged();
        if (buttons != null && buttons.size() > 0) {
            botCustomListViewButton.setText(buttons.get(0).getTitle());
            botCustomListViewButton.setOnClickListener(v -> {
                BotButtonModel botButtonModel = buttons.get(0);
                if (invokeGenericWebViewInterface != null && BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botButtonModel.getType())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(botButtonModel.getUrl());
                } else if (isLastItem() && composeFooterInterface != null && BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botButtonModel.getType())) {
                    String payload = botButtonModel.getPayload();
                    String message = botCustomListViewButton.getText().toString();
                    composeFooterInterface.onSendClick(message, payload, false);
                }
            });
            botCustomListViewButton.setVisibility(listElements != null && listElements.size() > 3 ? View.VISIBLE : View.GONE);
        }
    }
}
