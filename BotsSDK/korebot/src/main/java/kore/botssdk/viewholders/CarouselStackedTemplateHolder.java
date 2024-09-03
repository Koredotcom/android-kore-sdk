package kore.botssdk.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.CarouselStackTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotCarouselStackModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.coverflow.RecyclerCoverFlow;

public class CarouselStackedTemplateHolder extends BaseViewHolder {
    private final RecyclerCoverFlow recyclerCoverFlow;

    public static CarouselStackedTemplateHolder getInstance(ViewGroup parent) {
        return new CarouselStackedTemplateHolder(createView(R.layout.template_carousel_stacked, parent));
    }

    private CarouselStackedTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        recyclerCoverFlow = itemView.findViewById(R.id.rcfCarousel);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        Context context = itemView.getContext();
        ArrayList<BotCarouselStackModel> botCarouselModelArrayList = payloadInner.getCarouselStackElements();
        CarouselStackTemplateAdapter botCarouselAdapter = new CarouselStackTemplateAdapter(context, botCarouselModelArrayList, isLastItem());
        recyclerCoverFlow.setAdapter(botCarouselAdapter);
    }
}
