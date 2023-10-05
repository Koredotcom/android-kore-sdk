package kore.botssdk.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCarouselStackedModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.AutoExpandListView;

public class BotCarouselStackedAdapter extends PagerAdapter {

    private ArrayList<? extends BotCarouselStackedModel> botCarouselModels = new ArrayList<>();
    private final Activity activityContext;
    private final ComposeFooterInterface composeFooterInterface;
    private final InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final LayoutInflater ownLayoutInflater;
    private final float pageWidth;
    private String type;

    public BotCarouselStackedAdapter(ComposeFooterInterface composeFooterInterface,
                              InvokeGenericWebViewInterface invokeGenericWebViewInterface,
                              Activity activityContext) {
        super();
        this.activityContext = activityContext;
        this.composeFooterInterface = composeFooterInterface;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        ownLayoutInflater = activityContext.getLayoutInflater();

        TypedValue typedValue = new TypedValue();
        activityContext.getResources().getValue(R.dimen.carousel_item_width_factor, typedValue, true);
        pageWidth = typedValue.getFloat();
    }

    @Override
    public int getCount() {
        if (botCarouselModels == null) {
            return 0;
        } else {
            return botCarouselModels.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ( object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View carouselItemLayout = ownLayoutInflater.inflate(R.layout.carousel_stacked_layout, container, false);
        CarouselStackedViewHolder carouselStackedViewHolder = new CarouselStackedViewHolder();
        carouselStackedViewHolder.carousel_item_title = carouselItemLayout.findViewById(R.id.carousel_item_title);
        carouselStackedViewHolder.carousel_item_subtitle = carouselItemLayout.findViewById(R.id.carousel_item_subtitle);
        carouselStackedViewHolder.carousel_bottom_title = carouselItemLayout.findViewById(R.id.carousel_bottom_title);
        carouselStackedViewHolder.carousel_bottom_value = carouselItemLayout.findViewById(R.id.carousel_bottom_value);
        carouselStackedViewHolder.carouselButtonListview = carouselItemLayout.findViewById(R.id.carouselButtonListview);

        BotCarouselStackedModel botCarouselModel = botCarouselModels.get(position);

        if(botCarouselModel != null)
        {
            if(botCarouselModel.getTopSection() != null && !StringUtils.isNullOrEmpty(botCarouselModel.getTopSection().getTitle())) {
                carouselStackedViewHolder.carousel_item_title.setText(botCarouselModel.getTopSection().getTitle());
            }

            if(botCarouselModel.getMiddleSection() != null && !StringUtils.isNullOrEmpty(botCarouselModel.getMiddleSection().getDescription())) {
                carouselStackedViewHolder.carousel_item_subtitle.setText(botCarouselModel.getMiddleSection().getDescription());
            }

            if(botCarouselModel.getBottomSection() != null) {
                if(!StringUtils.isNullOrEmpty(botCarouselModel.getBottomSection().getTitle())) {
                    carouselStackedViewHolder.carousel_bottom_title.setText(botCarouselModel.getBottomSection().getTitle());
                }

                if(!StringUtils.isNullOrEmpty(botCarouselModel.getBottomSection().getDescription())) {
                    carouselStackedViewHolder.carousel_bottom_value.setText(botCarouselModel.getBottomSection().getDescription());
                }
            }

            if(botCarouselModel.getButtons() != null) {
                BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
                carouselStackedViewHolder.carouselButtonListview.setAdapter(botCarouselItemButtonAdapter);
                botCarouselItemButtonAdapter.setBotCaourselButtonModels(botCarouselModel.getButtons());
            }
        }
        container.addView(carouselItemLayout);
        return carouselItemLayout;
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
        // applyParams();
    }

    public void setBotCarouselModels(ArrayList<? extends BotCarouselStackedModel> botCarouselModels) {
        this.botCarouselModels = botCarouselModels;
    }

    @Override
    public float getPageWidth(int position) {
        if (getCount() == 0) {
            return super.getPageWidth(position);
        } else {
            return pageWidth;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SuppressLint("UnknownNullness")
    public static class CarouselStackedViewHolder {
        public TextView carousel_item_title;
        public TextView carousel_item_subtitle;
        public TextView carousel_bottom_title;
        public TextView carousel_bottom_value;
        AutoExpandListView carouselButtonListview;
    }
}
