package com.kore.ui.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.utils.BitmapUtils
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class AdvanceTableListAdapter(val context: Context, private val tables: List<Map<String, *>>) :
    RecyclerView.Adapter<TableViewDataHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewDataHolder {
        return TableViewDataHolder(LayoutInflater.from(context).inflate(R.layout.advance_table_list_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return tables.size
    }

    override fun onBindViewHolder(holder: TableViewDataHolder, position: Int) {
        val table = tables[position]

        if (table[BotResponseConstants.KEY_TITLE] != null) holder.botListItemTitle.text =
            table[BotResponseConstants.KEY_TITLE] as String

        if (table[BotResponseConstants.ICON] != null) {
            holder.botListItemImage.visibility = View.VISIBLE
            val layoutParams = RelativeLayout.LayoutParams(100, 100)
            layoutParams.setMargins(0, 0, 10, 0)
            if (table[BotResponseConstants.ICON_SIZE] != null) {
                when (table[BotResponseConstants.ICON_SIZE]) {
                    BotResponseConstants.LARGE -> {
                        layoutParams.height = 180
                        layoutParams.width = 180
                    }

                    BotResponseConstants.SMALL -> {
                        layoutParams.height = 60
                        layoutParams.width = 60
                    }
                }
            }
            holder.botListItemImage.layoutParams = layoutParams

            try {
                val imageData = table[BotResponseConstants.ICON] as String
                BitmapUtils.loadImage(context, imageData, holder.botListItemImage, R.drawable.ic_image_photo)
            } catch (e: Exception) {
                holder.botListItemImage.visibility = View.GONE
            }
        }

        holder.botListItemTitle.setTypeface(null, Typeface.BOLD)
        if (table[BotResponseConstants.DESCRIPTION] != null) {
            holder.botListItemSubtitle.visibility = View.VISIBLE
            holder.botListItemSubtitle.text = table[BotResponseConstants.DESCRIPTION] as String
        }
    }
}

class TableViewDataHolder(view: View) : RecyclerView.ViewHolder(view) {
    val botListItemTitle: TextView = itemView.findViewById(R.id.bot_list_item_title)
    val botListItemSubtitle: TextView = itemView.findViewById(R.id.bot_list_item_subtitle)
    val botListItemImage: ImageView = itemView.findViewById(R.id.bot_list_item_image)
}