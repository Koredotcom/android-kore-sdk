package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.markdown.MarkdownUtil
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants.SNIPPET_CONTENT
import com.kore.model.constants.BotResponseConstants.SNIPPET_TITLE
import com.kore.model.constants.BotResponseConstants.SOURCE
import com.kore.model.constants.BotResponseConstants.URL
import com.kore.ui.R

class SearchCentralPanelAdapter(
    private val context: Context,
    private val models: ArrayList<HashMap<String, Any>>,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<SearchCentralPanelAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.row_results_central_panel, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val liveSearchResultsModel: Map<String, Any> = models[position]
        holder.ivPagesCell.visibility = View.GONE
        holder.ivSuggestedPage.visibility = View.VISIBLE
        holder.tvPageTitle.visibility = View.GONE
        holder.tvDescription.maxLines = 4
        holder.llPages.visibility = View.VISIBLE
        holder.tvTitle.maxLines = 2
        holder.tvReadMore.text = HtmlCompat.fromHtml(context.resources.getString(R.string.read_more), HtmlCompat.FROM_HTML_MODE_LEGACY)
        if ((liveSearchResultsModel[SNIPPET_TITLE] as String?)?.isEmpty() == false) holder.tvTitle.text = HtmlCompat.fromHtml(
            MarkdownUtil.processMarkDown(liveSearchResultsModel[SNIPPET_TITLE].toString()), HtmlCompat.FROM_HTML_MODE_LEGACY
        ) else holder.tvTitle.visibility = View.GONE
        if ((liveSearchResultsModel[SNIPPET_CONTENT] as String?)?.isEmpty() == false) {
            holder.tvDescription.text = HtmlCompat.fromHtml(
                MarkdownUtil.processMarkDown(liveSearchResultsModel[SNIPPET_CONTENT].toString()),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.tvFullDescription.text = HtmlCompat.fromHtml(MarkdownUtil.processMarkDown(SNIPPET_CONTENT), HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            holder.tvReadMore.visibility = View.GONE
            holder.tvDescription.visibility = View.GONE
            holder.tvFullDescription.visibility = View.GONE
        }
        if ((liveSearchResultsModel[SOURCE] as String?)?.isEmpty() == false) holder.tvPanelLinkTitle.text = HtmlCompat.fromHtml(
            MarkdownUtil.processMarkDown(liveSearchResultsModel[SOURCE].toString()), HtmlCompat.FROM_HTML_MODE_LEGACY
        ) else holder.tvPanelLinkTitle.visibility = View.GONE
        if ((liveSearchResultsModel[URL] as String?)?.isEmpty() == false) holder.tvPanelLink.text = HtmlCompat.fromHtml(
            MarkdownUtil.processMarkDown(liveSearchResultsModel[URL].toString()), HtmlCompat.FROM_HTML_MODE_LEGACY
        ) else {
            holder.tvPanelLink.visibility = View.GONE
            holder.ivSuggestedPage.visibility = View.GONE
        }
        holder.tvReadMore.setOnClickListener { view ->
            view.isSelected = !view.isSelected
            if (view.isSelected) {
                holder.tvFullDescription.visibility = View.VISIBLE
                holder.tvDescription.visibility = View.GONE
                view.tag = false
                holder.tvReadMore.text = HtmlCompat.fromHtml(context.resources.getString(R.string.show_less), HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                holder.tvFullDescription.visibility = View.GONE
                holder.tvDescription.visibility = View.VISIBLE
                view.tag = true
                holder.tvReadMore.text = HtmlCompat.fromHtml(context.resources.getString(R.string.read_more), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
        holder.tvPanelLink.setOnClickListener {
            if ((liveSearchResultsModel[URL] as String?)?.isEmpty() == false)
                actionEvent(BotChatEvent.UrlClick(liveSearchResultsModel[URL].toString()))
        }
        holder.ivSuggestedPage.setOnClickListener {
            if ((liveSearchResultsModel[URL] as String?)?.isEmpty() == false)
                actionEvent(BotChatEvent.UrlClick(liveSearchResultsModel[URL].toString()))
        }
    }

    override fun getItemCount(): Int = models.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val tvDescription: TextView
        val tvFullDescription: TextView
        val tvPageTitle: TextView
        val tvPanelLinkTitle: TextView
        val tvPanelLink: TextView
        val tvReadMore: TextView
        val ivPagesCell: ImageView
        val ivSuggestedPage: ImageView
        val llPages: LinearLayout

        init {
            ivPagesCell = itemView.findViewById(R.id.ivPagesCell)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvFullDescription = itemView.findViewById(R.id.tvFullDescription)
            tvPageTitle = itemView.findViewById(R.id.tvPageTitle)
            ivSuggestedPage = itemView.findViewById(R.id.ivSuggestedPage)
            llPages = itemView.findViewById(R.id.llPages)
            tvPanelLinkTitle = itemView.findViewById(R.id.tvPanelLinkTitle)
            tvPanelLink = itemView.findViewById(R.id.tvPanelLink)
            tvReadMore = itemView.findViewById(R.id.tvReadMore)
        }
    }
}