package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class ListWidgetButtonAdapter(
    val context: Context,
    private val buttons: List<Map<String, *>>,
    private val displayLimit: Int,
    private val style: String,
    private val popupWindow: PopupWindow?,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<ListWidgetButtonAdapter.ButtonViewHolder>() {

    class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvButtonTv: TextView = itemView.findViewById(R.id.buttonTV)
        val ivBtnImage: ImageView = itemView.findViewById(R.id.ivBtnImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        return if (style == BotResponseConstants.FIT_TO_WIDTH) ButtonViewHolder(
            LayoutInflater.from(context).inflate(R.layout.widget_button_list_full_item, parent, false)
        )
        else ButtonViewHolder(LayoutInflater.from(context).inflate(R.layout.widget_button_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val btn = buttons[position]
        val image = btn[BotResponseConstants.COMPONENT_TYPE_IMAGE] as Map<String, *>
        holder.tvButtonTv.text = btn[BotResponseConstants.KEY_TITLE] as String

        holder.tvButtonTv.setOnClickListener {
            popupWindow?.dismiss()
            if (isLastItem && btn[BotResponseConstants.PAYLOAD] != null) {
                actionEvent(BotChatEvent.SendMessage(holder.tvButtonTv.text.toString(), btn[BotResponseConstants.PAYLOAD] as String))
            }
        }

        if (image[BotResponseConstants.IMAGE_SRC] != null && image[BotResponseConstants.IMAGE_SRC].toString().isNotEmpty()) {
            holder.ivBtnImage.isVisible = true
            Glide.with(context).load(image[BotResponseConstants.IMAGE_SRC]).error(R.drawable.ic_correct)
                .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.ivBtnImage))
        } else holder.ivBtnImage.isVisible = false
    }

    override fun getItemCount(): Int {
        return displayLimit
    }
}