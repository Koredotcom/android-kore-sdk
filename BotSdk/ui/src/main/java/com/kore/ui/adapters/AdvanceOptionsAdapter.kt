package com.kore.ui.adapters

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
) : RecyclerView.Adapter<OptionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(LayoutInflater.from(context).inflate(R.layout.advance_options_view, parent, false))
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val option = options[position]
        if (option[BotResponseConstants.LABEL] != null) {
            holder.tvBtnText.text = option[BotResponseConstants.LABEL] as String
        }

        if (option[BotResponseConstants.TYPE] != null) {
            when (option[BotResponseConstants.TYPE]) {
                BotResponseConstants.RADIO -> {
                    holder.ivOptions.background = ContextCompat.getDrawable(context, R.drawable.radio_uncheck)
                    if (option[BotResponseConstants.IS_CHECKED] != null && option[BotResponseConstants.IS_CHECKED] as Boolean) {
                        holder.ivOptions.background = ContextCompat.getDrawable(context, R.drawable.radio_check)
                    }
                }

                else -> {
                    holder.ivOptions.background = ContextCompat.getDrawable(context, R.drawable.check_box_uncheck)
                    if (option[BotResponseConstants.IS_CHECKED] != null && option[BotResponseConstants.IS_CHECKED] as Boolean) {
                        holder.ivOptions.background = ContextCompat.getDrawable(context, R.drawable.check_box_checked)
                    }
                }
            }

        }
    }
}

class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvBtnText: TextView
    val ivOptions: ImageView

    init {
        tvBtnText = itemView.findViewById(R.id.tvBtnText)
        ivOptions = itemView.findViewById(R.id.ivOptions)
    }
}