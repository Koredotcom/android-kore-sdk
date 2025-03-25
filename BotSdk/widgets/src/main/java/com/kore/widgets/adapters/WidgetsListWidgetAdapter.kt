package com.kore.widgets.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.widgets.R
import com.kore.widgets.constants.Constants
import com.kore.widgets.constants.Constants.BUTTON_ACTIVE_TXT_COLOR
import com.kore.widgets.data.repository.preference.WidgetPreferenceRepositoryImpl
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.extensions.dpToPx

class WidgetsListWidgetAdapter(
    val context: Context,
    private val arrListWidgetTemplateModels: List<Map<String, *>>,
    private val actionEvent: (event: BaseActionEvent) -> Unit
) : RecyclerView.Adapter<WidgetsListWidgetAdapter.ListWidgetHolder>() {
    private val widgetPreferenceRepository = WidgetPreferenceRepositoryImpl()

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWidgetHolder {
        return ListWidgetHolder(LayoutInflater.from(context).inflate(R.layout.listwidget_view, parent, false), context)
    }

    override fun getItemCount(): Int {
        return arrListWidgetTemplateModels.size
    }

    override fun onBindViewHolder(holder: ListWidgetHolder, position: Int) {
        val listTemplateModel: Map<String, Any> = arrListWidgetTemplateModels[position] as Map<String, Any>
        holder.imgUpDown.isVisible = false
        holder.divider.isVisible = position != arrListWidgetTemplateModels.size - 1

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

        if (listTemplateModel[Constants.KEY_TITLE] != null) {
            holder.txtTitle.text = listTemplateModel[Constants.KEY_TITLE] as String
            holder.txtTitle.setTextColor(
                widgetPreferenceRepository.getStringValue(
                    context,
                    Constants.THEME_NAME,
                    BUTTON_ACTIVE_TXT_COLOR,
                    "#000000"
                ).toColorInt()
            )
        } else {
            holder.txtTitle.visibility = View.GONE
        }

        if (listTemplateModel[Constants.KEY_SUB_TITLE] != null) {
            holder.txtSubTitle.text = listTemplateModel[Constants.KEY_SUB_TITLE] as String
            holder.txtSubTitle.setTextColor(
                widgetPreferenceRepository.getStringValue(context, Constants.THEME_NAME, BUTTON_ACTIVE_TXT_COLOR, "#000000").toColorInt()
            )
        } else {
            holder.txtSubTitle.visibility = View.GONE
        }

        if (listTemplateModel[Constants.COMPONENT_TYPE_IMAGE] != null) {
            holder.imageIcon.isVisible = true
            val image = listTemplateModel[Constants.COMPONENT_TYPE_IMAGE] as Map<String, *>
            if (image[Constants.IMAGE_SRC] != null) {
                var url = image[Constants.IMAGE_SRC] as String
                url = url.replace("http://", "https://")

                Glide.with(context).load(url).error(R.drawable.ic_image_photo)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners((10.dpToPx(context)))))
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.imageIcon))
            }
        } else holder.imageIcon.isVisible = false

        if (listTemplateModel[Constants.VALUE] != null) {
            val value = listTemplateModel[Constants.VALUE] as Map<String, *>
            when (value[Constants.TYPE]) {
                Constants.TEMPLATE_TYPE_BUTTON -> {
                    holder.iconImageLoad.isVisible = false
                    holder.imgMenu.isVisible = false
                    holder.tvText.isVisible = false
                    holder.tvUrl.isVisible = false
                    holder.tvButtonParent.isVisible = true
                }

                Constants.MENU -> {
                    holder.iconImageLoad.isVisible = false
                    holder.imgMenu.isVisible = true
                    holder.tvText.isVisible = false
                    holder.tvUrl.isVisible = false
                    holder.tvButtonParent.isVisible = false

                    holder.imgMenu.setOnClickListener {
                        val menu = value[Constants.MENU] as List<Map<String, *>>
                        rvPopBtns.adapter = WidgetsListWidgetButtonAdapter(context, menu, menu.size, "", popupWindow, actionEvent)
                        popupWindow.showAsDropDown(holder.imgMenu, -400, 0)
                    }
                }

                Constants.TEXT -> {
                    holder.iconImageLoad.isVisible = false
                    holder.imgMenu.isVisible = false
                    holder.tvText.isVisible = true
                    holder.tvUrl.isVisible = false
                    holder.tvButtonParent.isVisible = false
                    if (value[Constants.TEXT] != null) holder.tvText.text = value[Constants.TEXT] as String
                }

                Constants.URL -> {
                    holder.iconImageLoad.isVisible = false
                    holder.imgMenu.isVisible = false
                    holder.tvText.isVisible = false
                    holder.tvUrl.isVisible = true
                    holder.tvButtonParent.isVisible = false
                }

                Constants.COMPONENT_TYPE_IMAGE -> {
                    holder.iconImageLoad.isVisible = true
                    holder.imgMenu.isVisible = false
                    holder.tvText.isVisible = false
                    holder.tvUrl.isVisible = false
                    holder.tvButtonParent.isVisible = false

                    if (value[Constants.COMPONENT_TYPE_IMAGE] != null) {
                        val image = value[Constants.COMPONENT_TYPE_IMAGE] as Map<String, *>
                        if (image[Constants.IMAGE_SRC] != null) {
                            Glide.with(context).load(image[Constants.IMAGE_SRC]).error(R.drawable.ic_image_photo)
                                .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.iconImageLoad))
                        }
                    }
                }
            }
        }

        if (listTemplateModel[Constants.KEY_BUTTONS] != null) {
            val btns = listTemplateModel[Constants.KEY_BUTTONS] as List<Map<String, *>>
            val buttonsLayout = listTemplateModel[Constants.BUTTONS_LAYOUT] as Map<String, *>
            val displayLimit = buttonsLayout[Constants.DISPLAY_LIMIT] as Map<String, *>
            var limit = btns.size
            if (displayLimit[Constants.COUNT] != null) {
                limit = (displayLimit[Constants.COUNT] as Any).toString().toInt()
                if (limit > btns.size) limit = btns.size else holder.llMoreButton.isVisible = true

            }

            holder.rvButtonsList.isVisible = true
            var style = ""
            if (buttonsLayout[Constants.STYLE] != null) {
                style = buttonsLayout[Constants.STYLE] as String
                if (style == Constants.FIT_TO_WIDTH) {
                    holder.rvButtonsList.isVisible = false
                    holder.imgUpDown.isVisible = true
                    holder.imgUpDown.setOnClickListener {
                        holder.rvButtonsList.isVisible = holder.rvButtonsList.visibility != View.VISIBLE
                        holder.imgUpDown.rotation = if (holder.rvButtonsList.visibility == View.VISIBLE) 90F else 0F
                    }
                }
            }

            holder.rvButtonsList.layoutManager = LinearLayoutManager(
                context, if (style == Constants.FIT_TO_WIDTH) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL, false
            )
            holder.rvButtonsList.adapter = WidgetsListWidgetButtonAdapter(context, btns, limit, style, null, actionEvent)

            holder.llMoreButton.setOnClickListener {
                rvPopBtns.adapter = WidgetsListWidgetButtonAdapter(context, btns, btns.size, style, popupWindow, actionEvent)
                popupWindow.showAsDropDown(holder.llMoreButton)
            }

        } else {
            holder.rvButtonsList.isVisible = false
        }

        if (listTemplateModel[Constants.DETAILS] != null) {
            holder.rvDetails.isVisible = true
            holder.rvDetails.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder.rvDetails.adapter =
                WidgetsListWidgetDetailsAdapter(context, listTemplateModel[Constants.DETAILS] as List<Map<String, *>>, actionEvent)
        } else holder.rvDetails.isVisible = false

    }
}