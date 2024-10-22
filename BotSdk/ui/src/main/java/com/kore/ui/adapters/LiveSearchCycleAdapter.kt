package com.kore.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.event.UserActionEvent
import com.kore.markdown.MarkdownUtil
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants.ANSWER
import com.kore.model.constants.BotResponseConstants.FAQ
import com.kore.model.constants.BotResponseConstants.PAGE_IMAGE_URL
import com.kore.model.constants.BotResponseConstants.PAGE_PREVIEW
import com.kore.model.constants.BotResponseConstants.PAGE_TITLE
import com.kore.model.constants.BotResponseConstants.PAGE_URL
import com.kore.model.constants.BotResponseConstants.QUESTION
import com.kore.model.constants.BotResponseConstants.SYS_CONTENT_TYPE
import com.kore.model.constants.BotResponseConstants.URL
import com.kore.ui.R

class LiveSearchCycleAdapter(
    private val models: ArrayList<HashMap<String, Any>>,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<LiveSearchCycleAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.row_results_live_search, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultsModel: Map<String, Any> = models[position]
        holder.ivPagesCell.visibility = View.GONE
        holder.ivSuggestedPage.visibility = View.GONE
        holder.llPages.visibility = View.VISIBLE
        holder.llTask.visibility = View.GONE
        if (resultsModel[QUESTION] != null) {
            holder.tvTitle.maxLines = 1
            holder.tvTitle.text = resultsModel[QUESTION].toString()
            holder.tvDescription.text = resultsModel[ANSWER].toString()
            holder.tvFullDescription.text = resultsModel[ANSWER].toString()
        } else if (resultsModel[PAGE_TITLE] != null) {
            holder.tvTitle.maxLines = 2
            holder.ivPagesCell.visibility = View.VISIBLE
            holder.tvTitle.text = HtmlCompat.fromHtml(MarkdownUtil.processMarkDown(resultsModel[PAGE_TITLE].toString()), HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.tvDescription.text = HtmlCompat.fromHtml(
                MarkdownUtil.processMarkDown(resultsModel[PAGE_PREVIEW].toString()),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            if (!(resultsModel[PAGE_IMAGE_URL] as String?).isNullOrEmpty()) Glide.with(holder.tvTitle.context)
                .load(resultsModel[PAGE_IMAGE_URL].toString())
                .error(R.mipmap.ic_launcher)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.ivPagesCell))
        }
        holder.tvTitle.setOnClickListener { view ->
            view.isSelected = !view.isSelected
            if (resultsModel[SYS_CONTENT_TYPE] == FAQ) {
                if (view.isSelected) {
                    holder.tvDescription.visibility = View.GONE
                    holder.tvFullDescription.visibility = View.VISIBLE
                } else {
                    holder.tvDescription.visibility = View.VISIBLE
                    holder.tvFullDescription.visibility = View.GONE
                }
            }
        }
        holder.llPages.setOnClickListener {
            if (!(resultsModel[URL] as String?).isNullOrEmpty()) actionEvent(BotChatEvent.UrlClick(resultsModel[URL].toString()))
            else if (!(resultsModel[PAGE_URL] as String?).isNullOrEmpty()) actionEvent(BotChatEvent.UrlClick(resultsModel[URL].toString()))
        }
    }

    override fun getItemCount(): Int = models.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val tvDescription: TextView
        val tvFullDescription: TextView
        val ivPagesCell: ImageView
        val ivSuggestedPage: ImageView
        val llPages: LinearLayout
        val llTask: LinearLayout

        init {
            ivPagesCell = itemView.findViewById(R.id.ivPagesCell)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvFullDescription = itemView.findViewById(R.id.tvFullDescription)
            ivSuggestedPage = itemView.findViewById(R.id.ivSuggestedPage)
            llPages = itemView.findViewById(R.id.llPages)
            llTask = itemView.findViewById(R.id.llTask)
        }
    }
}