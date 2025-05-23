package com.kore.ui.adapters

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_LEFT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R

class CarouselStackedTemplateAdapter(
    private val context: Context,
    private val buttons: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<CarouselStackedViewHolder>() {

    private val sharedPrefs = PreferenceRepositoryImpl()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselStackedViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.row_carousel_stacked_item, parent, false)
        return CarouselStackedViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return buttons.size
    }

    override fun onBindViewHolder(holder: CarouselStackedViewHolder, position: Int) {
        val buttonMap: Map<String, *> = buttons[position]
        val parentBgColor = sharedPrefs.getStringValue(holder.itemView.context, THEME_NAME, BUBBLE_LEFT_BG_COLOR, "#3F51B5").toColorInt()
        val drawable = holder.parent.background as GradientDrawable
        drawable.setStroke(2, parentBgColor)
        val topSection = buttonMap[BotResponseConstants.TOP_SECTION] as Map<String, *>
        val middleSection = buttonMap[BotResponseConstants.MIDDLE_SECTION] as Map<String, *>
        val bottomSection = buttonMap[BotResponseConstants.BOTTOM_SECTION] as Map<String, *>
        holder.carouselItemTitle.text = topSection[BotResponseConstants.KEY_TITLE] as String
        holder.carouselItemSubtitle.text = middleSection[BotResponseConstants.DESCRIPTION] as String
        holder.carouselBottomTitle.text = bottomSection[BotResponseConstants.KEY_TITLE] as String
        holder.carouselBottomValue.text = bottomSection[BotResponseConstants.DESCRIPTION] as String

        val buttons: List<Map<String, *>>? = buttonMap[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>?
        buttons?.isNotEmpty().let {
            holder.carouselButtonListview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder.carouselButtonListview.adapter = CarouselItemButtonAdapter(context, buttons!!, isLastItem, actionEvent)
        }
    }
}

class CarouselStackedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var parent: FrameLayout = view.findViewById(R.id.parent)
    var carouselItemTitle: TextView = view.findViewById(R.id.carousel_item_title)
    var carouselItemSubtitle: TextView = view.findViewById(R.id.carousel_item_subtitle)
    var carouselBottomTitle: TextView = view.findViewById(R.id.carousel_bottom_title)
    var carouselBottomValue: TextView = view.findViewById(R.id.carousel_bottom_value)
    var carouselButtonListview: RecyclerView = view.findViewById(R.id.carousel_button_listview)
}