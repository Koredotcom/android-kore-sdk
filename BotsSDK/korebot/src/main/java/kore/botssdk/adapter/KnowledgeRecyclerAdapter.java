
package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.databinding.KnowledgeItemViewBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.WidgetViewMoreEnum;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;
import kore.botssdk.view.viewUtils.KaRoundedCornersTransform;


/**
 * Created by Shiva Krishna Kongara on 06-feb-19.
 */

public class KnowledgeRecyclerAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<KnowledgeDetailModel> knowledgeDetailModels;
    private boolean isExpanded;
    private int EMPTY_CARD_FLAG = 0;
    String msg;
    Drawable errorIcon;
    private int DATA_CARD_FLAG = 1;
    private int MESSAGE = 2;

    private VerticalListViewActionHelper verticalListViewActionHelper;
    private static KaRoundedCornersTransform roundedCornersTransform = new KaRoundedCornersTransform();
    private static int dp1;

    public KnowledgeRecyclerAdapter(ArrayList<KnowledgeDetailModel> knowledgeDetailModels, Context context) {
        this.knowledgeDetailModels = knowledgeDetailModels;
        this.context = context;
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        notifyDataSetChanged();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DATA_CARD_FLAG) {
            return new ViewHolder((KnowledgeItemViewBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.knowledge_item_view, parent, false));
        } else {
            return new EmptyWidgetViewHolder(LayoutInflater.from(context).inflate(R.layout.card_empty_widget_layout, parent, false));


        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderdata, final int position) {
        if (holderdata instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holderdata;

            //holder.followers_count.setText(knowledgeDetailModels.get(position).getnFollows()+"");
            holder.knowledgeItemViewBinding.setKnowledge(knowledgeDetailModels.get(position));
            holder.knowledgeItemViewBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    extras.putString(BundleConstants.KNOWLEDGE_ID, knowledgeDetailModels.get(position).getId());
                    if (verticalListViewActionHelper != null)
                        verticalListViewActionHelper.knowledgeItemClicked(extras, true);
                }
            });
            if (position == knowledgeDetailModels.size() - 1 && knowledgeDetailModels.size() < 3)
                holder.knowledgeItemViewBinding.divider.setVisibility(View.GONE);
        } else {

            EmptyWidgetViewHolder holder = (EmptyWidgetViewHolder) holderdata;
            holder.tv_disrcription.setText(holder.getItemViewType() == EMPTY_CARD_FLAG ? "No knowledge articles" : msg);
            holder.img_icon.setImageDrawable(holder.getItemViewType() == EMPTY_CARD_FLAG ? ContextCompat.getDrawable(context, R.drawable.no_meeting) : errorIcon);


        }

    }

    @Override
    public long getItemId(int position) {
        if (knowledgeDetailModels != null && knowledgeDetailModels.size() > 0) {
            KnowledgeDetailModel product = knowledgeDetailModels.get(position);
            return product.getCreatedOn() + position;
        } else return position;
    }

    @Override
    public int getItemViewType(int position) {

        if (knowledgeDetailModels != null && knowledgeDetailModels.size() > 0)
            return DATA_CARD_FLAG;


        if (msg != null && !msg.equalsIgnoreCase("")) {
            return MESSAGE;
        }
        return EMPTY_CARD_FLAG;
    }

    /*@Override
    public long getItemId(int position) {
        return position;
    }*/
    WidgetViewMoreEnum widgetViewMoreEnum;
    public void setViewMoreEnum(WidgetViewMoreEnum widgetViewMoreEnum) {
        this.widgetViewMoreEnum=widgetViewMoreEnum;
    }
    @Override
    public int getItemCount() {
        if(widgetViewMoreEnum!=null&&widgetViewMoreEnum==WidgetViewMoreEnum.EXPAND_VIEW){
            return knowledgeDetailModels != null && knowledgeDetailModels.size() > 0 ? knowledgeDetailModels.size() : 1;
        }
        return knowledgeDetailModels != null && knowledgeDetailModels.size() > 0 ? (!isExpanded && knowledgeDetailModels.size() > 3 ? 3 : knowledgeDetailModels.size()) : 1;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    @Override
    public ArrayList getData() {
        return knowledgeDetailModels;
    }

    @Override
    public void setData(ArrayList data) {
        knowledgeDetailModels = data;

    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg = msg;
        this.errorIcon = errorIcon;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        KnowledgeItemViewBinding knowledgeItemViewBinding;

        public ViewHolder(@NonNull KnowledgeItemViewBinding itemView) {
            super(itemView.getRoot());
            this.knowledgeItemViewBinding = itemView;

        }
    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView imageView, String src) {
        if (!StringUtils.isNullOrEmpty(src)) {
            Picasso.get().load(src)
                    .transform(roundedCornersTransform)
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }


    /*@BindingAdapter("loadImage")
    public static void loadImage(ImageView imageView, String src) {
        //.resize(imageView.getWidth()>0?imageView.getWidth():(40*dp1),imageView.getHeight()>0?imageView.getWidth():(40*dp1))
        if (!StringUtils.isNullOrEmpty(src)) {
            Picasso.with().load(src)
                    .transform(roundedCornersTransform)
                    .into(imageView);*//*, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    imageView.setVisibility(View.GONE);
                }
            });*//*
        } else {
            imageView.setVisibility(View.GONE);
        }
    }*/

}
