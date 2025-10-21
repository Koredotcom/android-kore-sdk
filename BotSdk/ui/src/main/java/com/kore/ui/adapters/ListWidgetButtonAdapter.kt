package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R

class ListWidgetButtonAdapter(
    val context: Context,
    private val buttons: List<Map<String, *>>,
    private val displayLimit: Int,
    private val style: String,
    private val popupWindow: PopupWindow?,
    private val isLastItem: Boolean,
    private var isMenus: Boolean = false,
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
        val context = holder.itemView.context
        val sharedPrefs = PreferenceRepositoryImpl()
        val txtColor = sharedPrefs.getStringValue(context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
        holder.tvButtonTv.setTextColor(txtColor)
        holder.tvButtonTv.text = btn[BotResponseConstants.KEY_TITLE] as String
        holder.ivBtnImage.isVisible = !isMenus

        holder.tvButtonTv.setOnClickListener {
            popupWindow?.dismiss()
            if (BotResponseConstants.BUTTON_TYPE_WEB_URL == btn[BotResponseConstants.TYPE] ||
                BotResponseConstants.BUTTON_TYPE_URL == btn[BotResponseConstants.TYPE]
            ) {
                actionEvent(BotChatEvent.UrlClick(btn[BotResponseConstants.URL].toString()))
            } else if (isLastItem && btn[BotResponseConstants.PAYLOAD] != null) {
                actionEvent(BotChatEvent.SendMessage(holder.tvButtonTv.text.toString(), btn[BotResponseConstants.PAYLOAD] as String))
            }
        }
        val errorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_correct)
        errorDrawable?.setTint(txtColor)
        if (image[BotResponseConstants.IMAGE_SRC] != null && !(image[BotResponseConstants.IMAGE_SRC] as String?).isNullOrEmpty()) {
            Glide.with(context).load(image[BotResponseConstants.IMAGE_SRC]).error(errorDrawable)
                .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.ivBtnImage))
        } else holder.ivBtnImage.setImageDrawable(errorDrawable)
    }

    override fun getItemCount(): Int {
        return displayLimit
    }
}