package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.event.UserActionEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class ListWidgetDetailsAdapter(
    val context: Context, private val arrDetails: List<Map<String, *>>, private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<ListWidgetDetailsAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBtnText: TextView = view.findViewById(R.id.tvBtnText)
        val ivListBtnIcon: ImageView = view.findViewById(R.id.ivListBtnIcon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        return DetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.listwidget_details_item, parent, false))
    }

    override fun getItemCount(): Int {
        return arrDetails.size
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val detail = arrDetails[position]

        if (detail[BotResponseConstants.DESCRIPTION] != null)
            holder.tvBtnText.text = detail[BotResponseConstants.DESCRIPTION] as String

        if(detail[BotResponseConstants.COMPONENT_TYPE_IMAGE] != null)
        {
            val image = detail[BotResponseConstants.COMPONENT_TYPE_IMAGE] as Map<String, *>
            if(image[BotResponseConstants.IMAGE_SRC] != null)
            {
                Glide.with(context).load(image[BotResponseConstants.IMAGE_SRC]).error(com.kore.botclient.R.drawable.ic_launcher)
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.ivListBtnIcon))
            }
        }

    }
}
