package kore.botssdk.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.SearchGraphAnswerModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownUtil;

public class SearchCentralPanelAdapter extends RecyclerView.Adapter<SearchCentralPanelAdapter.ViewHolder> {
    private final ArrayList<SearchGraphAnswerModel.Data> model;
    private final Context context;
    private final InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public SearchCentralPanelAdapter(Context context, ArrayList<SearchGraphAnswerModel.Data> model, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.model = model;
        this.context = context;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.central_panel_cell, parent, false);
        return new ViewHolder(listItem);
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
        holder.tvReadMore.setText(HtmlCompat.fromHtml(context.getResources().getString(R.string.read_more), HtmlCompat.FROM_HTML_MODE_LEGACY));

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getSnippet_title()))
            holder.tvTitle.setText(HtmlCompat.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getSnippet_title()), HtmlCompat.FROM_HTML_MODE_LEGACY));
        else
            holder.tvTitle.setVisibility(GONE);

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getSnippet_content()))
        {
            holder.tvDescription.setText(HtmlCompat.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getSnippet_content()), HtmlCompat.FROM_HTML_MODE_LEGACY));
            holder.tvFullDescription.setText(HtmlCompat.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getSnippet_content()), HtmlCompat.FROM_HTML_MODE_LEGACY));
        }
        else
        {
            holder.tvReadMore.setVisibility(GONE);
            holder.tvDescription.setVisibility(GONE);
            holder.tvFullDescription.setVisibility(GONE);
        }

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getSource()))
            holder.tvPanelLinkTitle.setText(HtmlCompat.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getSource()), HtmlCompat.FROM_HTML_MODE_LEGACY));
        else
            holder.tvPanelLinkTitle.setVisibility(GONE);

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getUrl()))
            holder.tvPanelLink.setText(HtmlCompat.fromHtml(MarkdownUtil.processMarkDown(liveSearchResultsModel.getUrl()), HtmlCompat.FROM_HTML_MODE_LEGACY));
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
                    holder.tvReadMore.setText(HtmlCompat.fromHtml(context.getResources().getString(R.string.show_less), HtmlCompat.FROM_HTML_MODE_LEGACY));
                }
                else
                {
                    holder.tvFullDescription.setVisibility(GONE);
                    holder.tvDescription.setVisibility(VISIBLE);
                    view.setTag(true);
                    holder.tvReadMore.setText(HtmlCompat.fromHtml(context.getResources().getString(R.string.read_more), HtmlCompat.FROM_HTML_MODE_LEGACY));
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
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView tvFullDescription;
        final TextView tvPageTitle;
        final TextView tvTaskName;
        final TextView tvPanelLinkTitle;
        final TextView tvPanelLink;
        final TextView tvReadMore;
        final ImageView ivPagesCell;
        final ImageView ivSuggestedPage;
        final ImageView ivTaskCell;
        final LinearLayout llPages;
        final LinearLayout llTask;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ivPagesCell = itemView.findViewById(R.id.ivPagesCell);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvDescription = itemView.findViewById(R.id.tvDescription);
            this.tvFullDescription = itemView.findViewById(R.id.tvFullDescription);
            this.tvPageTitle = itemView.findViewById(R.id.tvPageTitle);
            this.ivSuggestedPage = itemView.findViewById(R.id.ivSuggestedPage);
            this.llPages = itemView.findViewById(R.id.llPages);
            this.ivTaskCell = itemView.findViewById(R.id.ivTaskCell);
            this.tvTaskName = itemView.findViewById(R.id.tvTaskName);
            this.llTask = itemView.findViewById(R.id.llTask);
            this.tvPanelLinkTitle = itemView.findViewById(R.id.tvPanelLinkTitle);
            this.tvPanelLink = itemView.findViewById(R.id.tvPanelLink);
            this.tvReadMore = itemView.findViewById(R.id.tvReadMore);
        }
    }
}
