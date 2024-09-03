package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.adapter.RadioOptionsItemAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.RadioOptionModel;

public class RadioOptionsTemplateHolder extends BaseViewHolder {
    private final TextView title;
    private final RecyclerView list;
    private final TextView confirm;

    public static RadioOptionsTemplateHolder getInstance(ViewGroup parent) {
        return new RadioOptionsTemplateHolder(createView(R.layout.template_radio_options, parent));
    }

    private RadioOptionsTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        title = itemView.findViewById(R.id.title);
        list = itemView.findViewById(R.id.list);
        confirm = itemView.findViewById(R.id.confirm);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        List<RadioOptionModel> radioOptions = payloadInner.getRadioOptions();
        String msgId = ((BotResponse) baseBotMessage).getMessageId();
        Map<String, Object> contentState = ((BotResponse) baseBotMessage).getContentState();
        int selectedItem = contentState != null ? (Integer) contentState.get(BotResponse.SELECTED_ITEM) : -1;
        RadioOptionsItemAdapter adapter = new RadioOptionsItemAdapter(msgId, radioOptions, isLastItem(), selectedItem, contentStateListener);
        confirm.setOnClickListener(view -> {
            Map<String, Object> changedState = ((BotResponse) baseBotMessage).getContentState();
            if (isLastItem() && composeFooterInterface != null && changedState != null) {
                int selectedPosition = (Integer) changedState.get(BotResponse.SELECTED_ITEM);
                String value = radioOptions.get(selectedPosition).getPostback().getValue();
                composeFooterInterface.onSendClick(value, value, false);
            }
        });

        title.setText(payloadInner.getHeading());
        list.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);
    }
}
