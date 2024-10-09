package com.kore.ui.row.botchat.feedback.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.COLOR
import com.kore.model.constants.BotResponseConstants.NUMBER_ID
import com.kore.ui.R
import com.kore.ui.textview.CustomTextView

@SuppressLint("UnknownNullness")
class FeedbackRatingScaleAdapter(
    private val id: String,
    private val items: List<HashMap<String, Int>>,
    private val selectedPos: Int,
    private val isLastItem: Boolean,
    private val onFeedBackSelected: (id: String, selectedPosition: Int, key: String) -> Unit,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<FeedbackRatingScaleAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.getContext())
        val listItem: View = layoutInflater.inflate(R.layout.feedback_scale_cell, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val botListModel = items[position]
        val context = holder.tvRating.context
        holder.tvRating.text = botListModel[NUMBER_ID].toString()
        if (selectedPos == position) {
            holder.tvRating.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.tvRating.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary))
        } else {
            holder.tvRating.backgroundTintList = ColorStateList.valueOf(Color.parseColor(botListModel[COLOR].toString()))
            holder.tvRating.setTextColor(ContextCompat.getColor(context, R.color.gray_modern))
        }
        holder.tvRating.setOnClickListener {
            if (!isLastItem) return@setOnClickListener
            onFeedBackSelected(id, holder.adapterPosition, BotResponseConstants.SELECTED_FEEDBACK)
            actionEvent(BotChatEvent.SendMessage(botListModel[NUMBER_ID].toString()))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRating: CustomTextView

        init {
            tvRating = itemView.findViewById(R.id.tvRatingScale)
        }
    }
}