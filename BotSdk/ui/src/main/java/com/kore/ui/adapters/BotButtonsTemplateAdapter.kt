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
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.common.extensions.dpToPx
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class BotButtonsTemplateAdapter(
    private val context: Context,
    private val buttons: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<BotButtonsTemplateAdapter.QuickReplyViewHolder>() {
    private var splashColour: String
    private var textColor: String

    init {
        val sharedPreferences = PreferenceRepositoryImpl().getSharedPreference(context, BotResponseConstants.THEME_NAME)
        splashColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary))
        textColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white))
        splashColour = sharedPreferences.getString(BotResponseConstants.BUTTON_ACTIVE_BG_COLOR, splashColour)!!
        textColor = sharedPreferences.getString(BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR, textColor)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickReplyViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.row_button_template_cell, parent, false)
        return QuickReplyViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: QuickReplyViewHolder, position: Int) {
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

        (holder.buttonTitle.background as GradientDrawable).setColor(Color.parseColor(splashColour))
        (holder.buttonTitle.background as GradientDrawable).setStroke(
            2.dpToPx(context),
            Color.parseColor(splashColour)
        )
        holder.buttonTitle.setTextColor(Color.parseColor(textColor))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return buttons.size
    }

    class QuickReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonTitle: TextView
        val rootLayout: LinearLayout

        init {
            buttonTitle = view.findViewById(R.id.button_title)
            rootLayout = view.findViewById(R.id.root_layout)
        }
    }
}