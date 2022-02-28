package com.kore.findlysdk.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LinkedBotNLModel;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.ResultsViewAppearance;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.utils.markdown.MarkdownImageTagHandler;
import com.kore.findlysdk.utils.markdown.MarkdownTagHandler;
import com.kore.findlysdk.utils.markdown.MarkdownUtil;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

public class LiveSearchDynamicAdapter extends RecyclerView.Adapter<LiveSearchDynamicAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private int from = 0;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;
    private SharedPreferences sharedPreferences;

    public LiveSearchDynamicAdapter(Context context, ArrayList<HashMap<String, Object>> model, int from, InvokeGenericWebViewInterface invokeGenericWebViewInterface, ComposeFooterInterface composeFooterInterface) {
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.from = from;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        this.composeFooterInterface = composeFooterInterface;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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
    public void onBindViewHolder(@NonNull final LiveSearchDynamicAdapter.ViewHolder holder, int position) {
        final HashMap<String, Object> objectHashMap = model.get(position);

        holder.ivSuggestedPage.setVisibility(View.GONE);
        holder.llPages.setVisibility(VISIBLE);
        holder.llTask.setVisibility(GONE);
        holder.rlArrows.setVisibility(VISIBLE);

        holder.ibResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.tvFullDescription.getVisibility() == GONE)
                {
                    holder.tvDescription.setVisibility(GONE);
                    holder.tvFullDescription.setVisibility(VISIBLE);
                }

                holder.ibResults.setVisibility(GONE);
                holder.ibResults2.setVisibility(VISIBLE);
            }
        });

        holder.ibResults2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.tvFullDescription.getVisibility() == VISIBLE)
                {
                    holder.tvDescription.setVisibility(VISIBLE);
                    holder.tvFullDescription.setVisibility(GONE);
                }

                holder.ibResults.setVisibility(VISIBLE);
                holder.ibResults2.setVisibility(GONE);
            }
        });

        String sys_content_type = (String)objectHashMap.get(BundleConstants.SYS_CONTENT_TYPE);

        if(objectHashMap.get(BundleConstants.SYS_CONTENT_TYPE) != null)
        {
            if (!StringUtils.isNullOrEmpty(sys_content_type))
            {
                holder.llPages.setVisibility(VISIBLE);

                final ResultsViewAppearance resultsViewAppearance = (ResultsViewAppearance) objectHashMap.get(BundleConstants.APPEARANCE);

                if(resultsViewAppearance != null && resultsViewAppearance.getTemplate() != null)
                {
                    if(resultsViewAppearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_TEXT))
                        holder.ivPagesCell.setVisibility(View.GONE);
                    else if(resultsViewAppearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_IMAGE))
                    {
                        holder.ivPagesCell.setVisibility(VISIBLE);

                        if(resultsViewAppearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                        {
                            holder.ivPagesCell.setVisibility(GONE);
                            holder.ivPageGridCell.setVisibility(VISIBLE);
                        }
                    }
                    else if(resultsViewAppearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
                    {
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.llPages.setVisibility(GONE);
                        holder.llImageCentredContent.setVisibility(VISIBLE);
                        holder.llCentredContent.setVisibility(VISIBLE);
                    }
                    else
                    {
                        holder.tvDescription.setVisibility(GONE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                    }

                    if (objectHashMap.containsKey(resultsViewAppearance.getTemplate().getMapping().getHeading()))
                    {
                        String heading = (String) objectHashMap.get(resultsViewAppearance.getTemplate().getMapping().getHeading());
                        String description = (String) objectHashMap.get(resultsViewAppearance.getTemplate().getMapping().getDescription());

                        if(!StringUtils.isNullOrEmpty(heading) &&
                                !StringUtils.isNullOrEmpty(description))
                        {
                            heading = unescapeHtml4(heading);
                            heading = StringUtils.unescapeHtml3(heading);
                            heading = MarkdownUtil.processMarkDown(heading);
                            CharSequence sequence = Html.fromHtml(heading.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvTitle , heading), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                            holder.tvTitle.setText(strBuilder);
                            holder.tvDescription.setText(description);
                            holder.tvFullDescription.setText(description);

                            holder.tvTitleCentredContent.setText(heading);
                            holder.tvDescriptionCentredContent.setText(description);
                            holder.tvFullDescriptionCentredContent.setText(description);
                        }
                    }

                    if(resultsViewAppearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                    {
                        holder.rlArrows.setVisibility(GONE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.tvFullDescription.setVisibility(VISIBLE);
//                        holder.tvTitle.setMaxLines(Integer.MAX_VALUE);
                    }

                    if(resultsViewAppearance.getTemplate().getLayout().getIsClickable())
                        holder.rlArrows.setVisibility(GONE);

                    String imageUrl = (String) objectHashMap.get(BundleConstants.FILE_IMAGE_URL);
                    if (!StringUtils.isNullOrEmpty(imageUrl))
                    {
                        Glide.with(context)
                                .load(imageUrl)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .error(R.mipmap.imageplaceholder_left)
                                .into(new DrawableImageViewTarget(holder.ivPagesCell));
                        Glide.with(context)
                                .load(imageUrl)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .error(R.mipmap.imageplaceholder_left)
                                .into(new DrawableImageViewTarget(holder.ivCenteredContent));

                        if(resultsViewAppearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                        {
                            Glide.with(context)
                                    .load(imageUrl)
                                    .apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                                    .error(R.mipmap.imageplaceholder_left)
                                    .into(new DrawableImageViewTarget(holder.ivPageGridCell));
                        }
                    }

                    final String file_url = (String) objectHashMap.get(BundleConstants.FILE_URL);
                    holder.llPages.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(resultsViewAppearance.getTemplate().getLayout() != null && resultsViewAppearance.getTemplate().getLayout().getIsClickable() &&
                                    invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(file_url))
                                invokeGenericWebViewInterface.invokeGenericWebView(file_url);
                        }
                    });
                }
            }
        }

        holder.tvPageTitle.setVisibility(View.GONE);

        if(this.from == 1)
        {
            if(position == 0)
            {
                holder.tvPageTitle.setVisibility(VISIBLE);

                if(sys_content_type != null)
                    holder.tvPageTitle.setText(sys_content_type);
            }

            if ((position - 1) >= 0)
            {
                if(sys_content_type != null)
                {
                    if (!sys_content_type.equalsIgnoreCase((String) model.get(position - 1).get(BundleConstants.SYS_CONTENT_TYPE))) {
                        holder.tvPageTitle.setVisibility(View.VISIBLE);
                        holder.tvPageTitle.setText(sys_content_type);
                    }
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvFullDescription, tvPageTitle, tvTaskName, tvTitleCentredContent, tvDescriptionCentredContent, tvFullDescriptionCentredContent;
        ImageView ivPagesCell, ivPageGridCell, ivSuggestedPage, ivTaskCell, ivCenteredContent;
        LinearLayout llPages, llTask, llImageCentredContent, llCentredContent;
        ImageButton ibResults, ibResults2;
        RelativeLayout rlArrows;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ivPagesCell = (ImageView) itemView.findViewById(R.id.ivPagesCell);
            this.ivPageGridCell = (ImageView) itemView.findViewById(R.id.ivPageGridCell);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            this.tvFullDescription = (TextView) itemView.findViewById(R.id.tvFullDescription);
            this.tvPageTitle = (TextView) itemView.findViewById(R.id.tvPageTitle);
            this.ivSuggestedPage = (ImageView) itemView.findViewById(R.id.ivSuggestedPage);
            this.llPages = (LinearLayout) itemView.findViewById(R.id.llPages);
            this.ivTaskCell = (ImageView) itemView.findViewById(R.id.ivTaskCell);
            this.tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            this.llTask = (LinearLayout) itemView.findViewById(R.id.llTask);
            this.llCentredContent = (LinearLayout) itemView.findViewById(R.id.llImageCentredContent);
            this.llImageCentredContent = (LinearLayout) itemView.findViewById(R.id.llCentredContent);
            this.tvTitleCentredContent = (TextView) itemView.findViewById(R.id.tvTitleCentredContent);
            this.tvDescriptionCentredContent = (TextView) itemView.findViewById(R.id.tvDescriptionCentredContent);
            this.tvFullDescriptionCentredContent = (TextView) itemView.findViewById(R.id.tvFullDescriptionCentredContent);
            this.ivCenteredContent = (ImageView) itemView.findViewById(R.id.ivCenteredContent);
            this.ibResults = (ImageButton) itemView.findViewById(R.id.ibResults);
            this.ibResults2 = (ImageButton) itemView.findViewById(R.id.ibResults2);
            this.rlArrows = (RelativeLayout) itemView.findViewById(R.id.rlArrows);
        }
    }

}
