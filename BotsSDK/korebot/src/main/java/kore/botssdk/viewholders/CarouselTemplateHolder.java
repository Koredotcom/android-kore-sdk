package kore.botssdk.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.CarouselTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.models.PayloadInner;

public class CarouselTemplateHolder extends BaseViewHolder {
    private final RecyclerView viewPager;
    private final ImageView ivPrevious;
    private final ImageView ivNext;
    private int selectedPosition = 0;

    public static CarouselTemplateHolder getInstance(ViewGroup parent) {
        return new CarouselTemplateHolder(createView(R.layout.template_carousel, parent));
    }

    private CarouselTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        ivPrevious = itemView.findViewById(R.id.previous);
        ivNext = itemView.findViewById(R.id.next);
        viewPager = itemView.findViewById(R.id.carouselViewpager);
        viewPager.setLayoutManager(new LinearLayoutManager(viewPager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewPager.setClipToPadding(false);
        new PagerSnapHelper().attachToRecyclerView(viewPager);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        Context context = itemView.getContext();

        ArrayList<? extends BotCarouselModel> elements = payloadInner.getCarouselElements();
        CarouselTemplateAdapter botCarouselAdapter = new CarouselTemplateAdapter(context);
        viewPager.setAdapter(botCarouselAdapter);
        botCarouselAdapter.setComposeFooterInterface(composeFooterInterface);
        botCarouselAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        botCarouselAdapter.populateData(elements, isLastItem(), payloadInner.getTemplate_type());
        ivPrevious.setVisibility(elements.size() > 1 && selectedPosition > 0 ? View.VISIBLE : View.GONE);
        ivNext.setVisibility(elements.size() > 1 && selectedPosition < elements.size() - 1 ? View.VISIBLE : View.GONE);
        ivPrevious.setOnClickListener(view -> {
            if (selectedPosition > 0) {
                selectedPosition--;
                viewPager.smoothScrollToPosition(selectedPosition);
                ivPrevious.setVisibility(selectedPosition > 0 ? View.VISIBLE : View.GONE);
                ivNext.setVisibility(selectedPosition < elements.size() - 1 ? View.VISIBLE : View.GONE);
            }
        });
        ivNext.setOnClickListener(view -> {
            if (selectedPosition < elements.size() - 1) {
                selectedPosition++;
                viewPager.smoothScrollToPosition(selectedPosition);
                ivPrevious.setVisibility(selectedPosition > 0 ? View.VISIBLE : View.GONE);
                ivNext.setVisibility(selectedPosition < elements.size() - 1 ? View.VISIBLE : View.GONE);
            }
        });
    }
}