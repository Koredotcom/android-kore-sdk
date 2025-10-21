package com.kore.ui.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class BotButtonsTemplateAdapter(
    private val context: Context,
    private val buttons: List<Map<String, *>>,
    private val payload: Map<String, Any?>,
    private val isLastItem: Boolean,
    private val variation: String?,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<BotButtonsTemplateAdapter.BotButtonViewHolder>() {
    private var buttonBgColor: String
    private var activeTextColor: String
    private var invertBgColor: String
    private var invertTextColor: String

    init {
        val sharedPreferences = PreferenceRepositoryImpl().getSharedPreference(context, BotResponseConstants.THEME_NAME)
        buttonBgColor = "#efeffc"
        activeTextColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white))
        invertBgColor = "#3F51B5"
        invertTextColor = activeTextColor
        buttonBgColor = sharedPreferences.getString(BotResponseConstants.BUBBLE_LEFT_BG_COLOR, buttonBgColor)!!
        activeTextColor = sharedPreferences.getString(BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR, activeTextColor)!!

        invertBgColor = sharedPreferences.getString(BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, invertBgColor)!!
        invertTextColor = sharedPreferences.getString(BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, invertTextColor)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotButtonViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.row_button_template_cell, parent, false)
        return BotButtonViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: BotButtonViewHolder, position: Int) {
        val buttonMap: Map<String, *> = buttons[position]
        holder.buttonTitle.text = buttonMap[BotResponseConstants.KEY_TITLE] as String
        holder.rootLayout.setOnClickListener {
            try {
                when (buttonMap[BotResponseConstants.TYPE]) {
                    BotResponseConstants.USER_INTENT, BotResponseConstants.URL, BotResponseConstants.WEB_URL -> {
                        (buttonMap[BotResponseConstants.URL] ?: buttonMap[BotResponseConstants.WEB_URL])?.let {
                            actionEvent(BotChatEvent.UrlClick(it.toString()))
                        }
                    }

                    else -> {
                        if (isLastItem) {
                            if (buttonMap[BotResponseConstants.PAYLOAD] != null) {
                                actionEvent(
                                    BotChatEvent.SendMessage(
                                        buttonMap[BotResponseConstants.KEY_TITLE] as String,
                                        buttonMap[BotResponseConstants.PAYLOAD] as String
                                    )
                                )
                            } else {
                                actionEvent(BotChatEvent.SendMessage(buttonMap[BotResponseConstants.KEY_TITLE] as String))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val isFullWidth = payload[BotResponseConstants.FULL_WIDTH_] as Boolean? ?: false
        val isStackedButtons = payload[BotResponseConstants.BUTTON_STACKED] as Boolean? ?: false
        val bgDrawable = holder.buttonTitle.background as GradientDrawable
        bgDrawable.cornerRadius = 4.dpToPx(context).toFloat()

        if (isFullWidth || isStackedButtons || variation == BotResponseConstants.PLAIN) {
            bgDrawable.setStroke(1.dpToPx(context), buttonBgColor.toColorInt())
            bgDrawable.setColor(Color.WHITE)
            holder.buttonTitle.setTextColor(activeTextColor.toColorInt())
        } else if (variation == BotResponseConstants.BACKGROUND_INVERTED) {
            bgDrawable.setColor(invertBgColor.toColorInt())
            bgDrawable.setStroke(1.dpToPx(context), invertBgColor.toColorInt())
            holder.buttonTitle.setTextColor(invertTextColor.toColorInt())
        } else if (variation == BotResponseConstants.TEXT_INVERTED) {
            holder.buttonTitle.setTextColor(invertBgColor.toColorInt())
            bgDrawable.setColor(buttonBgColor.toColorInt())
            bgDrawable.setStroke(1.dpToPx(context), buttonBgColor.toColorInt())
        } else if (variation == BotResponseConstants.FORM_DATA) {
            holder.buttonTitle.setTextColor(invertBgColor.toColorInt())
            bgDrawable.setColor(Color.TRANSPARENT)
            bgDrawable.setStroke(1.dpToPx(context), Color.TRANSPARENT)
        } else {
            holder.buttonTitle.setTextColor(activeTextColor.toColorInt())
            bgDrawable.setColor(buttonBgColor.toColorInt())
            bgDrawable.setStroke(1.dpToPx(context), buttonBgColor.toColorInt())
        }
        if (isFullWidth) {
            holder.rootLayout.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            holder.buttonTitle.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return buttons.size
    }

    class BotButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonTitle: TextView = view.findViewById(R.id.button_title)
        val rootLayout: LinearLayout = view.findViewById(R.id.root_layout)
    }
}