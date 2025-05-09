package com.kore.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import androidx.core.graphics.toColorInt

class AdvanceOptionsAdapter(
    val context: Context,
    private val options: List<Map<String, *>>,
    private val isLastItem: Boolean,
) : RecyclerView.Adapter<OptionsViewHolder>() {

    private var selectedItems: List<Int> = emptyList()
    private val preferenceRepository = PreferenceRepositoryImpl()

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
                        val color = preferenceRepository.getStringValue(holder.itemView.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#ffffff")
                        holder.ivOptions.background = getTintDrawable(holder.ivOptions.context, color, R.drawable.radio_check)
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
                    selectedItems += (position)
                } else {
                    selectedItems += (position)
                }
                notifyDataSetChanged()
            }
        }
    }

    private fun getTintDrawable(context: Context, color: String, drawable: Int): Drawable {
        val buttonDrawable = AppCompatResources.getDrawable(context, drawable)
        val wrappedDrawable = DrawableCompat.wrap(buttonDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, color.toColorInt())
        return wrappedDrawable
    }
}

class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvBtnText: TextView = itemView.findViewById(R.id.tvBtnText)
    val ivOptions: ImageView = itemView.findViewById(R.id.ivOptions)
}