package com.kore.ui.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUTTON_TYPE_URL
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.utils.BitmapUtils

class AdvanceListButtonAdapter(
    val context: Context,
    val type: String,
    private val buttons: List<Map<String, *>>,
    private val count: Int,
    private val isLastItem: Boolean,
    private val isOptions: Boolean,
    val actionEvent: (event: BotChatEvent) -> Unit
) : RecyclerView.Adapter<ButtonViewHolder>() {
    private var advancedOptionsAdapter: AdvanceOptionsAdapter? = null
    private val sharedPreferences = PreferenceRepositoryImpl()

    fun setAdvancedOptionsAdapter(advancedOptionsAdapter: AdvanceOptionsAdapter?) {
        this.advancedOptionsAdapter = advancedOptionsAdapter
    }

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
//        val txtColor = sharedPrefs.getStringValue(holder.itemView.context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5")
//        holder.buttonTV.setTextColor(txtColor.toColorInt())
        if (isOptions) {
            val gradientDrawable = (holder.buttonTV.parent as ViewGroup).background.mutate() as GradientDrawable
            if (position % 2 == 0) {
                val bgColor: String = sharedPreferences.getStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#ffffff")
                val txtColor: String = sharedPreferences.getStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, "#000000")
                gradientDrawable.setColor(bgColor.toColorInt())
                gradientDrawable.setStroke(1, bgColor.toColorInt())
                holder.buttonTV.setTextColor(txtColor.toColorInt())
            } else {
                val bgColor: String = sharedPreferences.getStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_BG_COLOR, "#ffffff")
                val txtColor: String = sharedPreferences.getStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_TEXT_COLOR, "#000000")
                gradientDrawable.setColor(Color.WHITE)
                gradientDrawable.setStroke(2, bgColor.toColorInt())
                holder.buttonTV.setTextColor(txtColor.toColorInt())
            }
        } else {
            val txtColor: String = sharedPreferences.getStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5")
            holder.buttonTV.setTextColor(txtColor.toColorInt())
//            val parent = holder.tv.getParent() as ViewGroup
//            parent.setBackgroundResource(R.drawable.list_button_background)
        }
        holder.buttonTV.setOnClickListener {
            advancedOptionsAdapter?.let {
                if (button[BotResponseConstants.BTN_TYPE] != null) {
                    if (!isLastItem) return@setOnClickListener
                    if (button[BotResponseConstants.BTN_TYPE] == BotResponseConstants.CONFIRM) {
                        if (it.getSelectedItems().isNotEmpty())
                            actionEvent(BotChatEvent.SendMessage("Confirm: ${it.getSelectedItems().joinToString(",")}"))
                    } else {
                        it.clearSelectedItems()
                    }
                } else {
                    if (button[BotResponseConstants.TYPE] == BUTTON_TYPE_URL) {
                        actionEvent(BotChatEvent.UrlClick(button[BotResponseConstants.URL].toString()))
                    } else {
                        if (!isLastItem) return@setOnClickListener
                        val title: String = button[BotResponseConstants.KEY_TITLE].toString()
                        val payload: String = button[BotResponseConstants.PAYLOAD].toString()
                        actionEvent(BotChatEvent.SendMessage(title, payload))
                    }
                }
            }
        }

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
    val buttonTV: TextView = itemView.findViewById(R.id.buttonTV)
    val ivBtnImage: ImageView = itemView.findViewById(R.id.ivBtnImage)
    val layoutDetails: LinearLayout = itemView.findViewById(R.id.layout_details)
}
