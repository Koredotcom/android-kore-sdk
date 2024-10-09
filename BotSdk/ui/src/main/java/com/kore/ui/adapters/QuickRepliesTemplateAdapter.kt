package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.listeners.QuickRepliesClickListener
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class QuickRepliesTemplateAdapter(
    private val context: Context,
    private val type: String?,
    private val quickReplies: List<Map<String, *>>,
    private val quickReliesClickListener: QuickRepliesClickListener
) : RecyclerView.Adapter<QuickRepliesTemplateAdapter.QuickReplyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickReplyViewHolder {
        val convertView: View = if (type.equals(
                BotResponseConstants.TEMPLATE_TYPE_LIST, ignoreCase = true
            )
        ) LayoutInflater.from(context)
            .inflate(R.layout.quick_replies_full, parent, false) else LayoutInflater.from(context)
            .inflate(R.layout.quick_replies_cell, parent, false)
        return QuickReplyViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: QuickReplyViewHolder, position: Int) {
        val quickReplyTemplate = quickReplies[position]

        if (quickReplyTemplate[BotResponseConstants.KEY_TITLE] != null) {
            holder.quickReplyTitle.text = quickReplyTemplate[BotResponseConstants.KEY_TITLE] as String
        }

        if (quickReplyTemplate[BotResponseConstants.COMPONENT_TYPE_IMAGE_URL] != null) {
            holder.quickReplyImage.isVisible = true
            Glide.with(context)
                .load(quickReplyTemplate[BotResponseConstants.COMPONENT_TYPE_IMAGE_URL])
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).error(R.drawable.ic_image_photo)
                .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.quickReplyImage))
        }

        holder.quickReplyRoot.setOnClickListener {
            quickReliesClickListener.quickRepliesClicked(quickReplyTemplate)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return quickReplies.size
    }

    class QuickReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quickReplyTitle: TextView
        val quickReplyRoot: RelativeLayout
        val quickReplyImage: ImageView

        init {
            quickReplyTitle = view.findViewById(R.id.quick_reply_item_text)
            quickReplyRoot = view.findViewById(R.id.quick_reply_item_root)
            quickReplyImage = view.findViewById(R.id.quick_reply_item_image)
        }
    }
}