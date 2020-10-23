package com.kore.findlysdk.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LiveSearchCyclerAdapter extends RecyclerView.Adapter<LiveSearchCyclerAdapter.ViewHolder> {
    private ArrayList<LiveSearchResultsModel> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private int from = 0;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private String cardImage= "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAAJ1BMVEUAAAAAVaoEbq4DbK8GbK4Gbq8Gba0Fba8Fba4Fbq4Eba4Fba7////SVqJwAAAAC3RSTlMAA0hJVYKDqKmq4875bAAAAAABYktHRAyBs1FjAAAAP0lEQVQI12NgwACMJi5A4CzAwLobDBIYOCaAxDknMLCvnAkEsyYwcECkkBicMDV4GGwQxQEMjCogK5wEMC0HALyTIMofpWLWAAAAAElFTkSuQmCC";

    public LiveSearchCyclerAdapter(Context context, ArrayList<LiveSearchResultsModel> model, int from, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.from = from;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.live_search_pages_findly_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final LiveSearchResultsModel liveSearchResultsModel = model.get(position);

        holder.ivPagesCell.setVisibility(View.GONE);
        holder.ivSuggestedPage.setVisibility(View.GONE);

        holder.llPages.setVisibility(VISIBLE);
        holder.llTask.setVisibility(GONE);

        if (liveSearchResultsModel.getQuestion() != null)
        {
            holder.tvTitle.setMaxLines(1);
            holder.tvTitle.setText(liveSearchResultsModel.getQuestion());
            holder.tvDescription.setText(liveSearchResultsModel.getAnswer());
            holder.tvFullDescription.setText(liveSearchResultsModel.getAnswer());
        }
        else if (liveSearchResultsModel.getTitle() != null) {
            holder.tvTitle.setMaxLines(2);
            holder.ivPagesCell.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(liveSearchResultsModel.getTitle());
            holder.tvDescription.setText(liveSearchResultsModel.getSearchResultPreview());
            holder.ivSuggestedPage.setVisibility(View.VISIBLE);

            if (!StringUtils.isNullOrEmpty(liveSearchResultsModel.getImageUrl()))
                Glide.with(context).load(liveSearchResultsModel.getImageUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(holder.ivPagesCell));
        }
        else
        {
            holder.llPages.setVisibility(View.GONE);
            holder.llTask.setVisibility(View.VISIBLE);
            holder.tvTaskName.setText(liveSearchResultsModel.getTaskName());
//            if(!StringUtils.isNullOrEmpty(cardImage))
//            {
                try
                {
//                    if (cardImage.contains(","))
//                    {
                        cardImage = cardImage.substring(cardImage.indexOf(",") + 1);
                        byte[] decodedString = Base64.decode(cardImage.getBytes(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holder.ivTaskCell.setImageBitmap(decodedByte);
//                    }
                } catch (Exception e) {
                }
//            }

        }

        if ((position - 1) >= 0)
        {
            if (!liveSearchResultsModel.getContentType().equalsIgnoreCase(model.get(position - 1).getContentType())) {
                holder.tvPageTitle.setVisibility(View.VISIBLE);

                switch (from)
                {
                    case 0:
                        if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                            holder.tvPageTitle.setText("Suggested Pages");
                        else if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                            holder.tvPageTitle.setText("Suggested FAQS");
                        else
                            holder.tvPageTitle.setText("Suggested ACTIONS");
                        break;
                    case 1:
                        if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                            holder.tvPageTitle.setText("Matched Pages");
                        else if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                            holder.tvPageTitle.setText("Matched FAQS");
                        else
                            holder.tvPageTitle.setText("Matched ACTIONS");
                        break;
                }


            } else
                holder.tvPageTitle.setVisibility(View.GONE);
        } else if (position == 0) {
            holder.tvPageTitle.setVisibility(View.VISIBLE);

            switch (from)
            {
                case 0:
                    if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                        holder.tvPageTitle.setText("Suggested Pages");
                    else if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                        holder.tvPageTitle.setText("Suggested FAQS");
                    else
                        holder.tvPageTitle.setText("Suggested ACTIONS");
                    break;
                case 1:
                    if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                        holder.tvPageTitle.setText("Matched Pages");
                    else if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                        holder.tvPageTitle.setText("Matched FAQS");
                     else
                        holder.tvPageTitle.setText("Matched ACTIONS");
                    break;
            }


        } else
            holder.tvPageTitle.setVisibility(View.GONE);

        holder.tvTitle.setTag(true);
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.FAQ)) {
                    if ((boolean) view.getTag()) {
                        holder.tvDescription.setVisibility(View.GONE);
                        holder.tvFullDescription.setVisibility(View.VISIBLE);
                        view.setTag(false);
                    } else {
                        holder.tvDescription.setVisibility(View.VISIBLE);
                        holder.tvFullDescription.setVisibility(View.GONE);
                        view.setTag(true);
                    }
                }
            }
        });

        holder.llPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getUrl()))
                    invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvFullDescription, tvPageTitle, tvTaskName;
        ImageView ivPagesCell, ivSuggestedPage, ivTaskCell;
        LinearLayout llPages, llTask;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ivPagesCell = (ImageView) itemView.findViewById(R.id.ivPagesCell);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            this.tvFullDescription = (TextView) itemView.findViewById(R.id.tvFullDescription);
            this.tvPageTitle = (TextView) itemView.findViewById(R.id.tvPageTitle);
            this.ivSuggestedPage = (ImageView) itemView.findViewById(R.id.ivSuggestedPage);
            this.llPages = (LinearLayout) itemView.findViewById(R.id.llPages);
            this.ivTaskCell = (ImageView) itemView.findViewById(R.id.ivTaskCell);
            this.tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            this.llTask = (LinearLayout) itemView.findViewById(R.id.llTask);
        }
    }
}
