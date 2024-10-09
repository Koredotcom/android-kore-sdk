package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kore.common.event.UserActionEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

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

        holder.carouselButtonListview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        val buttons: List<Map<String, *>> = buttonMap[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>
        buttons.isNotEmpty().let {
            holder.carouselButtonListview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder.carouselButtonListview.adapter = CarouselItemButtonAdapter(context, buttons, isLastItem, actionEvent)
        }
    }
}

class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val carouselItemImage: ImageView
    val carouselItemTitle: TextView
    val carouselItemSubTitle: TextView
    val hashTagsView: TextView
    val carouselButtonListview: RecyclerView

    init {
        carouselItemImage = view.findViewById(R.id.carousel_item_image)
        carouselItemTitle = view.findViewById(R.id.carousel_item_title)
        carouselItemSubTitle = view.findViewById(R.id.carousel_item_subtitle)
        carouselButtonListview = view.findViewById(R.id.carousel_button_listview)
        hashTagsView = view.findViewById(R.id.hash_tags_view)
    }
}