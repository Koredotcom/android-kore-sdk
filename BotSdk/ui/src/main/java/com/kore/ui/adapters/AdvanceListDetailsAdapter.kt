package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R
import com.kore.ui.utils.BitmapUtils

class AdvanceListDetailsAdapter(
    val context: Context,
    private val arrTextInformation: List<Map<String, *>>,
) : RecyclerView.Adapter<AdvanceListDetailsAdapter.DetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        return DetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.listwidget_details_item, parent, false))
    }

    override fun getItemCount(): Int {
        return arrTextInformation.size
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val textInfo = arrTextInformation[position]
        if (textInfo[BotResponseConstants.KEY_TITLE] != null) {
            holder.tvBtnText.text = textInfo[BotResponseConstants.KEY_TITLE] as String
        }

        if (textInfo[BotResponseConstants.ICON] != null) {
            holder.ivListBtnIcon.visibility = View.VISIBLE
            try {
                val imageData = textInfo[BotResponseConstants.ICON] as String
                BitmapUtils.loadImage(context, imageData, holder.ivListBtnIcon, R.drawable.ic_image_photo)
            } catch (ex: Exception) {
                holder.ivListBtnIcon.visibility = View.GONE
            }
        }
    }

    class DetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvBtnText: TextView = view.findViewById(R.id.tvBtnText)
        var ivListBtnIcon: ImageView = view.findViewById(R.id.ivListBtnIcon)
    }
}