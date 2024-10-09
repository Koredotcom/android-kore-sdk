package com.kore.widgets.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.widgets.R
import com.kore.widgets.constants.Constants
import com.kore.widgets.event.BaseActionEvent

class WidgetsListWidgetDetailsAdapter(
    val context: Context, private val arrDetails: List<Map<String, *>>, private val actionEvent: (event: BaseActionEvent) -> Unit
) : RecyclerView.Adapter<WidgetsListWidgetDetailsAdapter.DetailsViewHolder>() {

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

        if (detail[Constants.DESCRIPTION] != null)
            holder.tvBtnText.text = detail[Constants.DESCRIPTION] as String

        if (detail[Constants.COMPONENT_TYPE_IMAGE] != null) {
            val image = detail[Constants.COMPONENT_TYPE_IMAGE] as Map<String, *>
            if (image[Constants.IMAGE_SRC] != null) {
                Glide.with(context).load(image[Constants.IMAGE_SRC]).error(R.drawable.ic_launcher)
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.ivListBtnIcon))
            }
        }

    }
}
