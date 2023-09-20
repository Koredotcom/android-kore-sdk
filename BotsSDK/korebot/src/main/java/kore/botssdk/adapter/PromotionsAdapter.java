package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.activity.WelcomeScreenActivity;
import kore.botssdk.models.PdfDownloadModel;
import kore.botssdk.models.PromotionsModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class PromotionsAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<PromotionsModel> arrPromotions;

    public PromotionsAdapter(Context context, ArrayList<PromotionsModel> arrPromotions)
    {
        this.context = context;
        this.arrPromotions = arrPromotions;
    }
    @Override
    public int getCount() {
        return arrPromotions.size();
    }

    @Override
    public PromotionsModel getItem(int position) {
        return arrPromotions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.promotions_banner, null);
        }

        if (convertView.getTag() == null)
        {
            ViewHolder holder = new ViewHolder();
            holder.ivPromotionsBanner = convertView.findViewById(R.id.ivPromotionsBanner);

            convertView.setTag(holder);
        }

        convertView.setClipToOutline(true);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        PromotionsModel promotionsModel = getItem(position);
        if(!StringUtils.isNullOrEmpty(promotionsModel.getBanner())) {

            Glide.with(context)
                .load(promotionsModel.getBanner())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.ivPromotionsBanner.setBackground(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {}
                });
        }
    }

    static class ViewHolder {
        ImageView ivPromotionsBanner;
    }
}
