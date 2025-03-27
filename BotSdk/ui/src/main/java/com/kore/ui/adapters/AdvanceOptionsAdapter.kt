package com.kore.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class AdvanceOptionsAdapter(
    val context: Context,
    private val options: List<Map<String, *>>,
    private val isLastItem: Boolean,
) : RecyclerView.Adapter<OptionsViewHolder>() {

    private var selectedItems: List<Int> = emptyList()

    fun getSelectedItems(): List<Int> = selectedItems

    fun clearSelectedItems() {
        selectedItems = emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(LayoutInflater.from(context).inflate(R.layout.advance_options_view, parent, false))
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val option = options[position]
        if (option[BotResponseConstants.LABEL] != null) {
            holder.tvBtnText.text = option[BotResponseConstants.LABEL] as String
        }

        if (option[BotResponseConstants.TYPE] != null) {
            when (option[BotResponseConstants.TYPE]) {
                BotResponseConstants.RADIO -> {
                    holder.ivOptions.background = ContextCompat.getDrawable(context, R.drawable.radio_uncheck)
                    if (selectedItems.contains(position)) {
                        holder.ivOptions.background = ContextCompat.getDrawable(context, R.drawable.radio_check)
                    }
                }

                else -> {
                    holder.ivOptions.background = ContextCompat.getDrawable(context, R.drawable.check_box_uncheck)
                    if (selectedItems.contains(position)) {
                        holder.ivOptions.background = ContextCompat.getDrawable(context, R.drawable.check_box_checked)
                    }
                }
            }
            holder.ivOptions.setOnClickListener {
                if (!isLastItem) return@setOnClickListener
                if (option[BotResponseConstants.TYPE] == BotResponseConstants.RADIO) {
                    selectedItems = emptyList()
                    selectedItems += (position + 1)
                } else {
                    selectedItems += (position + 1)
                }
                notifyDataSetChanged()
            }
        }
    }
}

class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvBtnText: TextView = itemView.findViewById(R.id.tvBtnText)
    val ivOptions: ImageView = itemView.findViewById(R.id.ivOptions)
}