package com.kore.widgets.adapters

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
import com.kore.widgets.R
import com.kore.widgets.constants.Constants
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.event.WidgetActionEvent

class WidgetsListWidgetButtonAdapter(
    val context: Context,
    private val buttons: List<Map<String, *>>,
    private val displayLimit: Int,
    private val style: String,
    private val popupWindow: PopupWindow?,
    private val actionEvent: (event: BaseActionEvent) -> Unit

) : RecyclerView.Adapter<WidgetsListWidgetButtonAdapter.ButtonViewHolder>() {


    class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvButtonTv: TextView = itemView.findViewById(R.id.buttonTV)
        val ivBtnImage: ImageView = itemView.findViewById(R.id.ivBtnImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        return if (style == Constants.FIT_TO_WIDTH) ButtonViewHolder(
            LayoutInflater.from(context).inflate(R.layout.widget_button_list_full_item, parent, false)
        )
        else ButtonViewHolder(LayoutInflater.from(context).inflate(R.layout.widget_button_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val btn = buttons[position]
        val image = btn[Constants.COMPONENT_TYPE_IMAGE] as Map<String, *>
        holder.tvButtonTv.text = btn[Constants.KEY_TITLE] as String

        holder.tvButtonTv.setOnClickListener {
            if (btn[Constants.PAYLOAD] != null) {
                popupWindow?.dismiss()
                actionEvent(WidgetActionEvent.SendMessage(holder.tvButtonTv.text.toString(), btn[Constants.PAYLOAD] as String))
            }
        }

        if (image[Constants.IMAGE_SRC] != null && image[Constants.IMAGE_SRC].toString().isNotEmpty()) {
            holder.ivBtnImage.isVisible = true
            Glide.with(context).load(image[Constants.IMAGE_SRC]).error(R.drawable.ic_correct)
                .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.ivBtnImage))
        } else holder.ivBtnImage.isVisible = false
    }

    override fun getItemCount(): Int {
        return displayLimit
    }
}