package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.listeners.QuickRepliesClickListener
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R

class QuickRepliesTemplateAdapter(
    private val context: Context,
    private val type: String?,
    private val quickReplies: List<Map<String, *>>,
    private val quickReliesClickListener: QuickRepliesClickListener
) : RecyclerView.Adapter<QuickRepliesTemplateAdapter.QuickReplyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickReplyViewHolder {
        val convertView: View = if (type.equals(BotResponseConstants.TEMPLATE_TYPE_LIST, ignoreCase = true))
            LayoutInflater.from(context).inflate(R.layout.quick_replies_full, parent, false)
        else LayoutInflater.from(context).inflate(R.layout.quick_replies_cell, parent, false)
        return QuickReplyViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: QuickReplyViewHolder, position: Int) {
        val quickReplyTemplate = quickReplies[position]
        val sharedPrefs = PreferenceRepositoryImpl()
        val bgColor = sharedPrefs.getStringValue(
            holder.quickReplyView.context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_BG_COLOR, "#ffffff"
        ).toColorInt()
        val textColor = sharedPrefs.getStringValue(
            holder.quickReplyView.context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5"
        ).toColorInt()
        holder.quickReplyView.setRoundedCorner(4.dpToPx(holder.itemView.context).toFloat())
        holder.quickReplyView.setBackgroundColor(bgColor)
        holder.quickReplyTitle.setTextColor(textColor)

        if (quickReplyTemplate[BotResponseConstants.KEY_TITLE] != null) {
            holder.quickReplyTitle.text = quickReplyTemplate[BotResponseConstants.KEY_TITLE] as String
        }
        holder.quickReplyImage.isVisible = quickReplyTemplate[BotResponseConstants.COMPONENT_TYPE_IMAGE_URL] != null
        quickReplyTemplate[BotResponseConstants.COMPONENT_TYPE_IMAGE_URL]?.let {
            Glide.with(context)
                .load(it)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
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
        val quickReplyView: RelativeLayout = view.findViewById(R.id.quick_reply_view)
        val quickReplyTitle: TextView = view.findViewById(R.id.quick_reply_item_text)
        val quickReplyRoot: RelativeLayout = view.findViewById(R.id.quick_reply_item_root)
        val quickReplyImage: ImageView = view.findViewById(R.id.quick_reply_item_image)
    }
}