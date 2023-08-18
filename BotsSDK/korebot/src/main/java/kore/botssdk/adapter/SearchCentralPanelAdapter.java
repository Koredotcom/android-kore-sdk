package kore.botssdk.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.SearchGraphAnswerModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class SearchCentralPanelAdapter extends RecyclerView.Adapter<SearchCentralPanelAdapter.ViewHolder> {
    private ArrayList<SearchGraphAnswerModel.Data> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private int from = 0;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private String cardImage= "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAAJ1BMVEUAAAAAVaoEbq4DbK8GbK4Gbq8Gba0Fba8Fba4Fbq4Eba4Fba7////SVqJwAAAAC3RSTlMAA0hJVYKDqKmq4875bAAAAAABYktHRAyBs1FjAAAAP0lEQVQI12NgwACMJi5A4CzAwLobDBIYOCaAxDknMLCvnAkEsyYwcECkkBicMDV4GGwQxQEMjCogK5wEMC0HALyTIMofpWLWAAAAAElFTkSuQmCC";

    public SearchCentralPanelAdapter(Context context, ArrayList<SearchGraphAnswerModel.Data> model, int from, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
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
        View listItem = layoutInflater.inflate(R.layout.central_panel_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final SearchGraphAnswerModel.Data liveSearchResultsModel = model.get(position);

        holder.ivPagesCell.setVisibility(View.GONE);
        holder.ivSuggestedPage.setVisibility(VISIBLE);
        holder.tvPageTitle.setVisibility(GONE);
        holder.tvDescription.setMaxLines(4);
        holder.llPages.setVisibility(VISIBLE);

        holder.tvTitle.setMaxLines(2);
        holder.tvReadMore.setText(Html.fromHtml(context.getResources().getString(R.string.read_more)));

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getSnippet_title()))
            holder.tvTitle.setText(Html.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getSnippet_title())));
        else
            holder.tvTitle.setVisibility(GONE);

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getSnippet_content()))
        {
            holder.tvDescription.setText(Html.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getSnippet_content())));
            holder.tvFullDescription.setText(Html.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getSnippet_content())));
        }
        else
        {
            holder.tvReadMore.setVisibility(GONE);
            holder.tvDescription.setVisibility(GONE);
            holder.tvFullDescription.setVisibility(GONE);
        }

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getSource()))
            holder.tvPanelLinkTitle.setText(Html.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getSource())));
        else
            holder.tvPanelLinkTitle.setVisibility(GONE);

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getUrl()))
            holder.tvPanelLink.setText(Html.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getUrl())));
        else
        {
            holder.tvPanelLink.setVisibility(GONE);
            holder.ivSuggestedPage.setVisibility(GONE);
        }

        holder.tvReadMore.setTag(true);
        holder.tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((boolean)view.getTag())
                {
                    holder.tvFullDescription.setVisibility(VISIBLE);
                    holder.tvDescription.setVisibility(GONE);
                    view.setTag(false);
                    holder.tvReadMore.setText(Html.fromHtml(context.getResources().getString(R.string.show_less)));
                }
                else
                {
                    holder.tvFullDescription.setVisibility(GONE);
                    holder.tvDescription.setVisibility(VISIBLE);
                    view.setTag(true);
                    holder.tvReadMore.setText(Html.fromHtml(context.getResources().getString(R.string.read_more)));
                }

            }
        });

        holder.tvPanelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getUrl()))
                    invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getUrl());
            }
        });

        holder.ivSuggestedPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getUrl()))
                    invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvFullDescription, tvPageTitle, tvTaskName, tvPanelLinkTitle, tvPanelLink, tvReadMore;
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
            this.tvPanelLinkTitle = itemView.findViewById(R.id.tvPanelLinkTitle);
            this.tvPanelLink = itemView.findViewById(R.id.tvPanelLink);
            this.tvReadMore = itemView.findViewById(R.id.tvReadMore);
        }
    }
}
