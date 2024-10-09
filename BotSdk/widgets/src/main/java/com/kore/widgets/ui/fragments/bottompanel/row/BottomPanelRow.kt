package com.kore.widgets.ui.fragments.bottompanel.row

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Base64
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.viewbinding.ViewBinding
import com.kore.widgets.databinding.RowBottomPanelItemBinding
import com.kore.widgets.extensions.dpToPx
import com.kore.widgets.model.Panel
import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.utils.WidgetFontUtils
import com.squareup.picasso.Picasso

class BottomPanelRow(
    private val panel: Panel,
    private val onItemClick: (panel: Panel) -> Unit
) : WidgetSimpleListRow {

    override val type: WidgetSimpleListRow.SimpleListRowType = BottomPanelRowType.Panel

    override fun areItemsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is BottomPanelRow) return false
        return otherRow.panel.id == panel.id
    }

    override fun areContentsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is BottomPanelRow) return false
        return otherRow.panel.isItemClicked == panel.isItemClicked
    }

    override fun getChangePayload(otherRow: WidgetSimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as RowBottomPanelItemBinding).apply {
            WidgetFontUtils.applyCustomFont(root.context, root, false)
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        (binding as RowBottomPanelItemBinding).commonBind()
    }

    private fun RowBottomPanelItemBinding.commonBind() {
        if (panel.icon != null && panel.icon.equals("url", true)) {
            imgSkill.isVisible = false
            imgIcon.isVisible = true
            root.setBackgroundColor(Color.parseColor(panel.getThemeValue()))
            root.isSelected = panel.isItemClicked
        } else {
            imgIcon.isVisible = false
            if (panel.icon != null) {
                try {
                    imgSkill.isVisible = true
                    var imageData: String
                    imageData = panel.icon
                    if (imageData.contains(",")) {
                        imageData = imageData.substring(imageData.indexOf(",") + 1)
                        val decodedString = Base64.decode(imageData.toByteArray(), Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        imgSkill.setImageBitmap(decodedByte)
                    } else {
                        Picasso.get().load(imageData).into(imgSkill)
                    }
                } catch (e: Exception) {
                    imgSkill.isVisible = false
                }
            } else {
                imgSkill.isVisible = false
            }
            imgSkill.setPadding(if (panel.isItemClicked) 3.dpToPx(root.context) else 0)
            if (panel.isItemClicked) {
                root.setBackgroundColor(Color.parseColor(panel.getThemeValue()))
            } else {
                root.background = null
            }
            root.isSelected = panel.isItemClicked
        }

        txtTitle.text = panel.name

        txtTitle.post { txtTitle.isVisible = true }
        root.animate().scaleX(1f).scaleY(1f).interpolator = AccelerateDecelerateInterpolator()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            root.tooltipText = panel.name
        }
        unreadImg.isVisible = false

        root.setOnClickListener { onItemClick(panel) }
    }
}