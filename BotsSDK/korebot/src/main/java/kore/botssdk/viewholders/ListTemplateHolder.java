package kore.botssdk.viewholders;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.ListTemplateAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.BundleConstants;

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
        RecyclerView recyclerView = itemView.findViewById(R.id.botCustomListView);
        TextView botCustomListViewButton = itemView.findViewById(R.id.botCustomListViewButton);
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText(), baseBotMessage.getTimeStamp());
        ArrayList<BotListModel> listElements = payloadInner.getListElements();
        ArrayList<BotButtonModel> buttons = payloadInner.getButtons();
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration((int) (1*dp1)));
        ListTemplateAdapter botListTemplateAdapter;
        botListTemplateAdapter = new ListTemplateAdapter(itemView.getContext(), listElements, isLastItem());
        recyclerView.setAdapter(botListTemplateAdapter);
        botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
        botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        if (buttons != null && !buttons.isEmpty()) {
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
