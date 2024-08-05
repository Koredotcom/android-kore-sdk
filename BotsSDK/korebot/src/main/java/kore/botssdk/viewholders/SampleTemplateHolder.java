package kore.botssdk.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;

public class SampleTemplateHolder extends BaseViewHolder {
    public SampleTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
    }
}
