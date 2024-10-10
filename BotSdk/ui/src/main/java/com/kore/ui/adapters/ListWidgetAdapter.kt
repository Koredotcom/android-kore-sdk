package com.kore.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.event.UserActionEvent
import com.kore.common.extensions.dpToPx
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class ListWidgetAdapter(
    val context: Context,
    private val arrListWidgetTemplateModels: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<ListWidgetAdapter.ListWidgetHolder>() {

    class ListWidgetHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        var txtSubTitle: TextView = itemView.findViewById(R.id.txtSubtitle)
        var imageIcon: ImageView = itemView.findViewById(R.id.imageIcon)
        var imgUpDown: ImageView = itemView.findViewById(R.id.img_up_down)
        var divider: View = itemView.findViewById(R.id.divider)
        var rvButtonsList: RecyclerView = itemView.findViewById(R.id.buttonsList)
        var imgMenu: ImageView = itemView.findViewById(R.id.icon_image)
        var iconImageLoad: ImageView = itemView.findViewById(R.id.icon_image_load)
        var tvText: TextView = itemView.findViewById(R.id.tv_text)
        var tvUrl: TextView = itemView.findViewById(R.id.tv_url)
        var tvButton: TextView = itemView.findViewById(R.id.tv_button)
        var tvButtonParent: LinearLayout = itemView.findViewById(R.id.tv_values_layout)
        var rvDetails: RecyclerView = itemView.findViewById(R.id.rvDetails)
        val llMoreButton: LinearLayout = itemView.findViewById(R.id.llMoreButton);

        val sharedPreferences = PreferenceRepositoryImpl().getSharedPreference(context, BotResponseConstants.THEME_NAME)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWidgetHolder {
        return ListWidgetHolder(LayoutInflater.from(context).inflate(R.layout.listwidget_view, parent, false), context)
    }

    override fun getItemCount(): Int {
        return arrListWidgetTemplateModels.size
    }

    override fun onBindViewHolder(holder: ListWidgetHolder, position: Int) {
        val listTemplateModel: MutableMap<String, Any> = arrListWidgetTemplateModels[position] as MutableMap<String, Any>
        holder.imgUpDown.isVisible = false

        val popUpView = View.inflate(context, R.layout.advancelist_drop_down_popup, null)
        val popupWindow = PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val rvPopBtns: RecyclerView = popUpView.findViewById(R.id.rvDropDown)
        val ivDropDownCLose: ImageView = popUpView.findViewById(R.id.ivDropDownCLose)
        rvPopBtns.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.transparant_image_div)!!)
        rvPopBtns.removeItemDecoration(divider)
        rvPopBtns.addItemDecoration(divider)
        popUpView.minimumWidth = (180.dpToPx(context))
        ivDropDownCLose.setOnClickListener { popupWindow.dismiss() }

        if (listTemplateModel[BotResponseConstants.KEY_TITLE] != null) {
            holder.txtTitle.text = listTemplateModel[BotResponseConstants.KEY_TITLE] as String
            holder.txtTitle.setTextColor(
                Color.parseColor(
                    holder.sharedPreferences.getString(
                        BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR, "#000000"
                    )
                )
            )
        } else {
            holder.txtTitle.visibility = View.GONE
        }

        if (listTemplateModel[BotResponseConstants.KEY_SUB_TITLE] != null) {
            holder.txtSubTitle.text = listTemplateModel[BotResponseConstants.KEY_SUB_TITLE] as String
            holder.txtSubTitle.setTextColor(
                Color.parseColor(
                    holder.sharedPreferences.getString(
                        BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR, "#000000"
                    )
                )
            )
        } else {
            holder.txtSubTitle.visibility = View.GONE
        }

        if (listTemplateModel[BotResponseConstants.COMPONENT_TYPE_IMAGE] != null) {
            holder.imageIcon.isVisible = true
            val image = listTemplateModel[BotResponseConstants.COMPONENT_TYPE_IMAGE] as Map<String, *>
            if (image[BotResponseConstants.IMAGE_SRC] != null) {
                var url = image[BotResponseConstants.IMAGE_SRC] as String
                url = url.replace("http://", "https://")

                Glide.with(context).load(url).error(R.drawable.ic_image_photo)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners((10.dpToPx(context)))))
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.imageIcon))
            }
        } else holder.imageIcon.isVisible = false

        if (listTemplateModel[BotResponseConstants.VALUE] != null) {
            val value = listTemplateModel[BotResponseConstants.VALUE] as Map<String, *>
            when (value[BotResponseConstants.TYPE]) {
                BotResponseConstants.TEMPLATE_TYPE_BUTTON -> {
                    holder.iconImageLoad.isVisible = false
                    holder.imgMenu.isVisible = false
                    holder.tvText.isVisible = false
                    holder.tvUrl.isVisible = false
                    holder.tvButtonParent.isVisible = true
                }

                BotResponseConstants.MENU -> {
                    holder.iconImageLoad.isVisible = false
                    holder.imgMenu.isVisible = true
                    holder.tvText.isVisible = false
                    holder.tvUrl.isVisible = false
                    holder.tvButtonParent.isVisible = false

                    holder.imgMenu.setOnClickListener {
                        val menu = value[BotResponseConstants.MENU] as List<Map<String, *>>
                        rvPopBtns.adapter = ListWidgetButtonAdapter(context, menu, menu.size, "", popupWindow, isLastItem, actionEvent)
                        popupWindow.showAsDropDown(holder.imgMenu, -400, 0)
                    }
                }

                BotResponseConstants.TEXT -> {
                    holder.iconImageLoad.isVisible = false
                    holder.imgMenu.isVisible = false
                    holder.tvText.isVisible = true
                    holder.tvUrl.isVisible = false
                    holder.tvButtonParent.isVisible = false
                    if (value[BotResponseConstants.TEXT] != null) holder.tvText.text = value[BotResponseConstants.TEXT] as String
                }

                BotResponseConstants.URL -> {
                    holder.iconImageLoad.isVisible = false
                    holder.imgMenu.isVisible = false
                    holder.tvText.isVisible = false
                    holder.tvUrl.isVisible = true
                    holder.tvButtonParent.isVisible = false
                }

                BotResponseConstants.COMPONENT_TYPE_IMAGE -> {
                    holder.iconImageLoad.isVisible = true
                    holder.imgMenu.isVisible = false
                    holder.tvText.isVisible = false
                    holder.tvUrl.isVisible = false
                    holder.tvButtonParent.isVisible = false

                    if (value[BotResponseConstants.COMPONENT_TYPE_IMAGE] != null) {
                        val image = value[BotResponseConstants.COMPONENT_TYPE_IMAGE] as Map<String, *>
                        if (image[BotResponseConstants.IMAGE_SRC] != null) {
                            Glide.with(context).load(image[BotResponseConstants.IMAGE_SRC]).error(R.drawable.ic_image_photo)
                                .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.iconImageLoad))
                        }
                    }
                }
            }
        }

        if (listTemplateModel[BotResponseConstants.KEY_BUTTONS] != null) {
            val btns = listTemplateModel[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>
            val buttonsLayout = listTemplateModel[BotResponseConstants.BUTTONS_LAYOUT] as Map<String, *>
            val displayLimit = buttonsLayout[BotResponseConstants.DISPLAY_LIMIT] as Map<String, *>
            var limit = btns.size
            if (displayLimit[BotResponseConstants.COUNT] != null) {
                limit = (displayLimit[BotResponseConstants.COUNT] as Any).toString().toInt()
                if (limit > btns.size) limit = btns.size else holder.llMoreButton.isVisible = true

            }

            holder.rvButtonsList.isVisible = true
            var style = ""
            if (buttonsLayout[BotResponseConstants.STYLE] != null) {
                style = buttonsLayout[BotResponseConstants.STYLE] as String
                if (style == BotResponseConstants.FIT_TO_WIDTH) {
                    holder.rvButtonsList.isVisible = false
                    holder.imgUpDown.isVisible = true
                    holder.imgUpDown.setOnClickListener {
                        holder.rvButtonsList.isVisible = holder.rvButtonsList.visibility != View.VISIBLE
                        holder.imgUpDown.rotation = if (holder.rvButtonsList.visibility == View.VISIBLE) 90F else 0F
                    }
                }
            }

            holder.rvButtonsList.layoutManager = LinearLayoutManager(
                context, if (style == BotResponseConstants.FIT_TO_WIDTH) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL, false
            )
            holder.rvButtonsList.adapter = ListWidgetButtonAdapter(context, btns, limit, style, null, isLastItem, actionEvent)

            holder.llMoreButton.setOnClickListener {
                rvPopBtns.adapter = ListWidgetButtonAdapter(context, btns, btns.size, style, popupWindow, isLastItem, actionEvent)
                popupWindow.showAsDropDown(holder.llMoreButton)
            }

        } else {
            holder.rvButtonsList.isVisible = false
        }

        if (listTemplateModel[BotResponseConstants.DETAILS] != null) {
            holder.rvDetails.isVisible = true
            holder.rvDetails.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder.rvDetails.adapter =
                ListWidgetDetailsAdapter(context, listTemplateModel[BotResponseConstants.DETAILS] as List<Map<String, *>>, actionEvent)
        } else holder.rvDetails.isVisible = false

    }
}