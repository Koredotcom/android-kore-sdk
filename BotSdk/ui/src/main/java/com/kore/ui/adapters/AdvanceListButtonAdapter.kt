package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.ui.utils.BitmapUtils
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class AdvanceListButtonAdapter(
    val context: Context,
    val type: String,
    private val buttons: List<Map<String, *>>,
    private val count: Int,
    private val isLastItem: Boolean,
    val actionEvent: (event: BotChatEvent) -> Unit
) : RecyclerView.Adapter<ButtonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        if (type.isNotEmpty() && type == BotResponseConstants.FULL_WIDTH) {
            return ButtonViewHolder(LayoutInflater.from(context).inflate(R.layout.advance_button_full_width, parent, false))
        }

        return ButtonViewHolder(LayoutInflater.from(context).inflate(R.layout.widget_button_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return when (count > 0) {
            true -> count
            else -> buttons.size
        }
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val button = buttons[position]

        holder.buttonTV.text = button[BotResponseConstants.KEY_TITLE] as String
        holder.ivBtnImage.visibility = View.GONE

        if (button[BotResponseConstants.ICON] != null) {
            holder.ivBtnImage.visibility = View.VISIBLE
            try {
                val imageData: String = button[BotResponseConstants.ICON] as String
                BitmapUtils.loadImage(context, imageData, holder.ivBtnImage, R.drawable.ic_image_photo)
            } catch (e: Exception) {
                holder.ivBtnImage.visibility = View.GONE
            }
        }

        holder.buttonTV.setOnClickListener { holder.layoutDetails.performClick() }

        holder.layoutDetails.setOnClickListener {
            val listElementButtonPayload = if (button[BotResponseConstants.PAYLOAD] != null) {
                button[BotResponseConstants.PAYLOAD] as String
            } else ""

            val listElementButtonTitle = if (button[BotResponseConstants.KEY_TITLE] != null) {
                button[BotResponseConstants.KEY_TITLE] as String
            } else ""

            try {
                when (button[BotResponseConstants.TYPE]) {
                    BotResponseConstants.USER_INTENT,
                    BotResponseConstants.URL,
                    BotResponseConstants.WEB_URL -> {
                        if (button[BotResponseConstants.URL] != null) {
                            actionEvent(BotChatEvent.UrlClick(button[BotResponseConstants.URL] as String))
                        }
                    }

                    else -> {
                        if (!isLastItem) return@setOnClickListener
                        actionEvent(BotChatEvent.SendMessage(listElementButtonTitle, listElementButtonPayload))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val buttonTV: TextView
    val ivBtnImage: ImageView
    val layoutDetails: LinearLayout

    init {
        buttonTV = itemView.findViewById(R.id.buttonTV)
        ivBtnImage = itemView.findViewById(R.id.ivBtnImage)
        layoutDetails = itemView.findViewById(R.id.layout_details)
    }
}
