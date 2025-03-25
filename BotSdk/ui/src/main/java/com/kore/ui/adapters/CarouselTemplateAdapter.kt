package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.row.SimpleListRow

class CarouselTemplateAdapter(
    private val context: Context,
    private val buttons: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.row_carousel_item, parent, false)
        return CarouselViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return buttons.size
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val buttonMap: Map<String, *> = buttons[position]
        if (buttonMap[BotResponseConstants.DEFAULT_ACTION] != null) {
            val btn: Map<*, *> = buttonMap[BotResponseConstants.DEFAULT_ACTION] as Map<*, *>
            holder.hashTagsView.text = btn[BotResponseConstants.URL] as String
            val sharedPrefs = PreferenceRepositoryImpl()
            val bgColor = sharedPrefs.getStringValue(holder.hashTagsView.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
            holder.hashTagsView.setTextColor(bgColor)
        }

        holder.carouselItemTitle.text = buttonMap[BotResponseConstants.KEY_TITLE] as String
        holder.carouselItemSubTitle.text = buttonMap[BotResponseConstants.KEY_SUB_TITLE] as String

        when (buttonMap[BotResponseConstants.COMPONENT_TYPE_IMAGE_URL].toString().isNotEmpty()) {
            true -> {
                Glide.with(context)
                    .load(buttonMap[BotResponseConstants.COMPONENT_TYPE_IMAGE_URL])
                    .into(holder.carouselItemImage)
            }

            else -> {}
        }

        holder.carouselButtonListview.addItemDecoration(SimpleListRow.VerticalSpaceItemDecoration(10))
        val buttons: List<Map<String, *>>? = buttonMap[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>?
        buttons?.isNotEmpty().let {
            holder.carouselButtonListview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder.carouselButtonListview.adapter = CarouselItemButtonAdapter(context, buttons!!, isLastItem, actionEvent)
        }
    }
}

class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val carouselItemImage: ImageView = view.findViewById(R.id.carousel_item_image)
    val carouselItemTitle: TextView = view.findViewById(R.id.carousel_item_title)
    val carouselItemSubTitle: TextView = view.findViewById(R.id.carousel_item_subtitle)
    val hashTagsView: TextView = view.findViewById(R.id.hash_tags_view)
    val carouselButtonListview: RecyclerView = view.findViewById(R.id.carousel_button_listview)
}