package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class CarouselItemButtonAdapter(
    private val context: Context,
    private val buttons: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<CarouselItemButtonAdapter.ButtonViewHolder>() {

    class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carouselButtonTitle: TextView

        init {
            carouselButtonTitle = view.findViewById(R.id.bot_carousel_item_button)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.bot_carousel_item_button_layout, parent, false)
        return ButtonViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return buttons.size
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val buttonMap: Map<String, *> = buttons[position]
        val title = buttonMap[BotResponseConstants.KEY_TITLE] as String
        holder.carouselButtonTitle.text = title

        holder.carouselButtonTitle.setOnClickListener {
            if (!isLastItem) return@setOnClickListener
            try {
                when (buttonMap[BotResponseConstants.TYPE]) {
                    BotResponseConstants.USER_INTENT,
                    BotResponseConstants.URL,
                    BotResponseConstants.WEB_URL -> {
                        actionEvent(BotChatEvent.UrlClick(buttonMap[BotResponseConstants.URL] as String))
                    }

                    else -> {
                        actionEvent(BotChatEvent.SendMessage(title, buttonMap[BotResponseConstants.PAYLOAD] as String))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}