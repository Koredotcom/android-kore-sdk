package com.kore.ui.adapters

import android.content.Context
import android.content.res.Resources
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
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R

class ListWidgetAdapter(
    val context: Context,
    private val arrListWidgetTemplateModels: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<ListWidgetAdapter.ListWidgetHolder>() {

    private val popUpView: View = View.inflate(context, R.layout.advancelist_drop_down_popup, null)
    private val popupWindow = PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
    private val popupRvButtons: RecyclerView = popUpView.findViewById(R.id.rvDropDown)
    private val ivDropDownClose: ImageView = popUpView.findViewById(R.id.ivDropDownCLose)

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
        var tvButtonMore: TextView = itemView.findViewById(R.id.btnMore)
        var tvButtonParent: LinearLayout = itemView.findViewById(R.id.tv_values_layout)
        var rvDetails: RecyclerView = itemView.findViewById(R.id.rvDetails)
        val llMoreButton: LinearLayout = itemView.findViewById(R.id.llMoreButton);

        val sharedPreferences = PreferenceRepositoryImpl().getSharedPreference(context, THEME_NAME)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWidgetHolder {
        return ListWidgetHolder(LayoutInflater.from(context).inflate(R.layout.listwidget_view, parent, false), context)
    }

    override fun getItemCount(): Int {
        return arrListWidgetTemplateModels.size
    }

    override fun onBindViewHolder(holder: ListWidgetHolder, position: Int) {
        val listTemplateModel: MutableMap<String, Any> = arrListWidgetTemplateModels[position] as MutableMap<String, Any>
        holder.divider.isVisible = position != arrListWidgetTemplateModels.size - 1
        holder.imgUpDown.isVisible = false
        if (listTemplateModel[BotResponseConstants.KEY_TITLE] != null) {
            holder.txtTitle.text = listTemplateModel[BotResponseConstants.KEY_TITLE] as String
            holder.sharedPreferences.getString(BUTTON_ACTIVE_TXT_COLOR, "#000000")?.toColorInt()?.let {
                holder.txtTitle.setTextColor(it)
            }
        } else {
            holder.txtTitle.visibility = View.GONE
        }

        if (listTemplateModel[BotResponseConstants.KEY_SUB_TITLE] != null) {
            holder.txtSubTitle.text = listTemplateModel[BotResponseConstants.KEY_SUB_TITLE] as String
            holder.sharedPreferences.getString(BUTTON_ACTIVE_TXT_COLOR, "#000000")?.toColorInt()?.let {
                holder.txtSubTitle.setTextColor(it)
            }
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

                    holder.imgMenu.setOnClickListener { view ->
                        showMenuPopup(view, value[BotResponseConstants.MENU] as List<Map<String, *>>)
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
                        holder.imgUpDown.rotation = if (holder.rvButtonsList.isVisible) 90F else 0F
                    }
                }
            }

            holder.rvButtonsList.layoutManager = LinearLayoutManager(
                context, if (style == BotResponseConstants.FIT_TO_WIDTH) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL, false
            )
            holder.rvButtonsList.adapter = ListWidgetButtonAdapter(context, btns, limit, style, null, isLastItem, false, actionEvent)
            val sharedPrefs = PreferenceRepositoryImpl()
            val txtColor = sharedPrefs.getStringValue(context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
            holder.tvButtonMore.setTextColor(txtColor)
            holder.llMoreButton.setOnClickListener { view ->
                showMenuPopup(view, btns)
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

    private fun showMenuPopup(anchorView: View, buttons: List<Map<String, *>>) {
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.transparant_image_div)!!)
        popupRvButtons.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        popupRvButtons.removeItemDecoration(divider)
        popupRvButtons.addItemDecoration(divider)
        popUpView.minimumWidth = (200.dpToPx(context))
        ivDropDownClose.setOnClickListener { popupWindow.dismiss() }
        popupRvButtons.adapter = ListWidgetButtonAdapter(context, buttons, buttons.size, "", popupWindow, isLastItem, true, actionEvent)
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        val anchorY = location[1]

        // Get screen height
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        // Measure the popup content
        popUpView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight: Int = popUpView.measuredHeight

        val showAbove = (screenHeight - anchorY - anchorView.height) < popupHeight

        if (showAbove) {
            // Show above the anchor
            popupWindow.showAsDropDown(anchorView, 0, -anchorView.height - popupHeight)
        } else {
            // Show below the anchor
            popupWindow.showAsDropDown(anchorView)
        }
    }
}