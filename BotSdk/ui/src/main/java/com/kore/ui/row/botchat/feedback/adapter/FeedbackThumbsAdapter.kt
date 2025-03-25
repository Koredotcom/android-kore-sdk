package com.kore.ui.row.botchat.feedback.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.kore.event.BotChatEvent
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.THUMBS_UP_ID
import com.kore.ui.R

class FeedbackThumbsAdapter(
    private val msgId: String,
    private val items: List<Map<String, Any>>,
    private val selectedItem: Int,
    private val isEnabled: Boolean,
    private val onFeedBackSelected: (id: String, selectedPosition: Int, key: String) -> Unit,
    private val actionEvent: (event: BotChatEvent) -> Unit
) : RecyclerView.Adapter<FeedbackThumbsAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.row_feedback_thumbs, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ratingModel = items[position]
        holder.tvFeedbackThumbs.text = ratingModel[BotResponseConstants.REVIEW_TEXT].toString()
        val index = position % SRC_IMAGES.size
        setTextColor(holder.tvFeedbackThumbs, SELECTED_TEXT_COLORS[index], UNSELECTED_TEXT_COLORS[index])
        setImageViewSrc(holder.ivFeedbackThumbs, SRC_IMAGES[index], SELECTED_TEXT_COLORS[index], UNSELECTED_TEXT_COLORS[index])
        setBgDrawable(holder.thumbsFeedbackRoot, SELECTED_BG_COLORS[index], UNSELECTED_BG_COLORS[index])
        val id = ratingModel[THUMBS_UP_ID] as Double
        holder.thumbsFeedbackRoot.isSelected = selectedItem == id.toInt()
        holder.ivFeedbackThumbs.isSelected = selectedItem == id.toInt()
        holder.tvFeedbackThumbs.isSelected = selectedItem == id.toInt()
        holder.thumbsFeedbackRoot.setOnClickListener {
            if (!isEnabled) return@setOnClickListener
            onFeedBackSelected(msgId, id.toInt(), BotResponseConstants.SELECTED_FEEDBACK)
            val value = ratingModel.getValue(BotResponseConstants.VALUE) as String
            actionEvent(BotChatEvent.SendMessage(value, value))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun setTextColor(textView: TextView, selectedColor: String, unselectedColor: String) {
        val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected))
        val colors = intArrayOf(
            selectedColor.toColorInt(),
            unselectedColor.toColorInt()
        )
        val colorStateList = ColorStateList(states, colors)
        textView.setTextColor(colorStateList)
    }

    private fun setBgDrawable(view: View, selectedColor: String, unselectedColor: String) {
        val drawable = ContextCompat.getDrawable(view.context, R.color.selector_feedback_thumbs_color)
        drawable?.mutate()
        val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected))
        val colors = intArrayOf(selectedColor.toColorInt(), unselectedColor.toColorInt())
        val colorStateList = ColorStateList(states, colors)
        drawable?.setTintList(colorStateList)
        view.background = drawable
    }

    private fun setImageViewSrc(imageView: ImageView, srcId: Int, selectedColor: String, unselectedColor: String) {
        val drawable = ContextCompat.getDrawable(imageView.context, srcId) ?: return
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected))
        val colors = intArrayOf(selectedColor.toColorInt(), unselectedColor.toColorInt())
        val colorStateList = ColorStateList(states, colors)
        DrawableCompat.setTintList(wrappedDrawable, colorStateList)
        imageView.setImageDrawable(wrappedDrawable)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivFeedbackThumbs: ImageView = itemView.findViewById(R.id.ivFeedbackThumbs)
        var tvFeedbackThumbs: TextView = itemView.findViewById(R.id.tvFeedbackThumbs)
        var thumbsFeedbackRoot: LinearLayoutCompat = itemView.findViewById(R.id.thumbs_feedback_root)

        init {
            thumbsFeedbackRoot.setRoundedCorner(8f)
        }
    }

    companion object {
        private val SELECTED_TEXT_COLORS = arrayOf("#ffffff", "#ffffff")
        private val UNSELECTED_TEXT_COLORS = arrayOf("#cc16A346", "#ccDC2626")
        private val SELECTED_BG_COLORS = arrayOf("#16A346", "#DC2626")
        private val UNSELECTED_BG_COLORS = arrayOf("#aa16A346", "#aaDC2626")
        private val SRC_IMAGES = intArrayOf(R.drawable.thumbs_up, R.drawable.thumbs_down)
    }
}