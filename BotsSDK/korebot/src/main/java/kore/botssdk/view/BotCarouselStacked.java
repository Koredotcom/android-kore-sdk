package kore.botssdk.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselAdapter;
import kore.botssdk.adapter.BotCarouselStackedAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.models.BotCarouselStackedModel;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.HorizontalInfiniteCycleViewPager;

public class BotCarouselStacked extends LinearLayout {
    HorizontalInfiniteCycleViewPager carouselViewpager;
    int dp1;
    private int carouselViewWidth;
    Activity activityContext;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    BotCarouselStackedAdapter botCarouselAdapter;


    public BotCarouselStacked(Context context) {
        super(context);
        init();
    }

    public BotCarouselStacked(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotCarouselStacked(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp1 = (int) DimensionUtil.dp1;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_carousel_infinite, this, true);
        carouselViewpager = inflatedView.findViewById(R.id.carouselViewpager);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
        // Disable clip to padding
        carouselViewpager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        carouselViewpager.setPadding(40 * dp1, 0, 20, 0);
        carouselViewpager.setPageMargin(pageMargin);

    }
    public void populateCarouselView(ArrayList<? extends BotCarouselStackedModel> botCarouselModelArrayList){
        populateCarouselView(botCarouselModelArrayList,null);
    }

    public void populateCarouselView(ArrayList<? extends BotCarouselStackedModel> botCarouselModelArrayList, String type) {
        if (composeFooterInterface != null && activityContext != null)
        {
            botCarouselAdapter = new BotCarouselStackedAdapter(composeFooterInterface, invokeGenericWebViewInterface, activityContext);
            botCarouselAdapter.setBotCarouselModels(botCarouselModelArrayList);
            botCarouselAdapter.setType(type);
            carouselViewpager.setAdapter(botCarouselAdapter);
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }
}
