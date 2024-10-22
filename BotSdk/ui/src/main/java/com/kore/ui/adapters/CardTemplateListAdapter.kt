package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.ui.utils.BitmapUtils
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class CardTemplateListAdapter(
    val context: Context,
    private val listItems: List<Map<String, *>>,
) : RecyclerView.Adapter<CardListViewHolder>() {
    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvBtnText.text = item[BotResponseConstants.KEY_TITLE] as String

        if (item[BotResponseConstants.DESCRIPTION] != null) {
            holder.tvDescriptionTitle.visibility = View.VISIBLE
            holder.tvDescriptionTitle.text = item[BotResponseConstants.KEY_TITLE] as String
            holder.tvBtnText.text = item[BotResponseConstants.DESCRIPTION] as String
        }

        if (item[BotResponseConstants.ICON] != null) {
            holder.ivListBtnIcon.visibility = View.VISIBLE
            try {
                val imageData = item[BotResponseConstants.ICON] as String
                BitmapUtils.loadImage(context, imageData, holder.ivListBtnIcon, R.drawable.ic_image_photo)
            } catch (ex: Exception) {
                holder.ivListBtnIcon.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        return CardListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_desc_list_cell, parent, false)
        )
    }
}


class CardListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvBtnText: TextView
    val tvDescriptionTitle: TextView
    val ivListBtnIcon: ImageView

    init {
        tvBtnText = itemView.findViewById<TextView>(R.id.tvDescription)
        tvDescriptionTitle = itemView.findViewById<TextView>(R.id.tvDescriptionTitle)
        ivListBtnIcon = itemView.findViewById<ImageView>(R.id.ivListBtnIcon)
    }
}

