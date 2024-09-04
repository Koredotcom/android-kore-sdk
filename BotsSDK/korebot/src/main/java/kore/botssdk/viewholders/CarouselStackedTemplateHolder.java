package kore.botssdk.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private final ImageView ivPrevious;
    private final ImageView ivNext;
    private int selectedPosition = 0;

    public static CarouselStackedTemplateHolder getInstance(ViewGroup parent) {
        return new CarouselStackedTemplateHolder(createView(R.layout.template_carousel_stacked, parent));
    }

    private CarouselStackedTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        ivPrevious = itemView.findViewById(R.id.previous);
        ivNext = itemView.findViewById(R.id.next);
        recyclerCoverFlow = itemView.findViewById(R.id.rcfCarousel);
        recyclerCoverFlow.setScrollable(false);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        Context context = itemView.getContext();
        ArrayList<BotCarouselStackModel> elements = payloadInner.getCarouselStackElements();
        CarouselStackTemplateAdapter botCarouselAdapter = new CarouselStackTemplateAdapter(context, elements, isLastItem());
        botCarouselAdapter.setComposeFooterInterface(composeFooterInterface);
        botCarouselAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        recyclerCoverFlow.setAdapter(botCarouselAdapter);
        ivPrevious.setVisibility(elements.size() > 1 && selectedPosition > 0 ? View.VISIBLE : View.GONE);
        ivNext.setVisibility(elements.size() > 1 && selectedPosition < elements.size() - 1 ? View.VISIBLE : View.GONE);
        ivPrevious.setOnClickListener(view -> {
            if (selectedPosition > 0) {
                selectedPosition--;
                recyclerCoverFlow.smoothScrollToPosition(selectedPosition);
                ivPrevious.setVisibility(selectedPosition > 0 ? View.VISIBLE : View.GONE);
                ivNext.setVisibility(selectedPosition < elements.size() - 1 ? View.VISIBLE : View.GONE);
            }
        });
        ivNext.setOnClickListener(view -> {
            if (selectedPosition < elements.size() - 1) {
                selectedPosition++;
                recyclerCoverFlow.smoothScrollToPosition(selectedPosition);
                ivPrevious.setVisibility(selectedPosition > 0 ? View.VISIBLE : View.GONE);
                ivNext.setVisibility(selectedPosition < elements.size() - 1 ? View.VISIBLE : View.GONE);
            }
        });
    }
}
