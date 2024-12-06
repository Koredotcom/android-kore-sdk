package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCarouselStackModel;
import kore.botssdk.net.SDKConfiguration;

public class CarouselStackTemplateAdapter extends RecyclerView.Adapter<CarouselStackTemplateAdapter.ViewHolder> {
    private final ArrayList<BotCarouselStackModel> botCarouselModels;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final LayoutInflater layoutInflater;

    public CarouselStackTemplateAdapter(Context context, ArrayList<BotCarouselStackModel> botCarouselModels, boolean isEnabled) {
        layoutInflater = LayoutInflater.from(context);
        this.botCarouselModels = botCarouselModels;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.carousel_stacked_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotCarouselStackModel botCarouselModel = getItem(position);
        if (botCarouselModel == null) return;

        GradientDrawable leftDrawable = (GradientDrawable) holder.cardView.getBackground();
        leftDrawable.setStroke((int)(1*dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));

        holder.carouselItemTitle.setText(botCarouselModel.getTopSection().title);
        holder.carouselItemSubtitle.setText(botCarouselModel.getMiddleSection().description);
        holder.carouselBottomTitle.setText(botCarouselModel.getBottomSection().title);
        holder.carouselBottomValue.setText(botCarouselModel.getBottomSection().description);

        if (botCarouselModel.getButtons() != null) {
            CarouselItemButtonAdapter botCarouselItemButtonAdapter = new CarouselItemButtonAdapter(holder.itemView.getContext());
            holder.buttons.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.buttons.setAdapter(botCarouselItemButtonAdapter);
            botCarouselItemButtonAdapter.setComposeFooterInterface(composeFooterInterface);
            botCarouselItemButtonAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            botCarouselItemButtonAdapter.populateData(botCarouselModel.getButtons(), isEnabled);
        }
    }

    private BotCarouselStackModel getItem(int position) {
        return botCarouselModels != null ? botCarouselModels.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return botCarouselModels != null ? botCarouselModels.size() : 0;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView buttons;
        TextView carouselItemTitle;
        TextView carouselItemSubtitle;
        TextView carouselBottomTitle;
        TextView carouselBottomValue;
        FrameLayout cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttons = itemView.findViewById(R.id.carousel_button_listview);
            carouselItemTitle = itemView.findViewById(R.id.carousel_item_title);
            carouselItemSubtitle = itemView.findViewById(R.id.carousel_item_subtitle);
            carouselBottomTitle = itemView.findViewById(R.id.carousel_bottom_title);
            carouselBottomValue = itemView.findViewById(R.id.carousel_bottom_value);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
