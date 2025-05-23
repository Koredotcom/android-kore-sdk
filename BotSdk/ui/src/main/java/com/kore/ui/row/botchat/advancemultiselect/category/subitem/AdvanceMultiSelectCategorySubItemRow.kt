package com.kore.ui.row.botchat.advancemultiselect.category.subitem

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.graphics.toColorLong
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants.BUBBLE_LEFT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.DESCRIPTION
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants.IMAGE_URL
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.SELECTED_ITEM
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.databinding.RowAdvanceMultiSelectCategorySubitemBinding
import com.kore.ui.row.botchat.advancemultiselect.category.AdvancedMultiSelectCategorySubItemRowType

class AdvanceMultiSelectCategorySubItemRow(
    private val id: String,
    private val item: Map<String, String>,
    private val selectedItems: ArrayList<Map<String, String>>,
    private val isLastItem: Boolean = false,
    private val onSaveState: (msgId: String, value: Any?, key: String) -> Unit,
) : SimpleListRow() {

    override val type: SimpleListRowType = AdvancedMultiSelectCategorySubItemRowType.SubItem
    private val preferenceRepository = PreferenceRepositoryImpl()

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvanceMultiSelectCategorySubItemRow) return false
        return otherRow.item[KEY_TITLE] == item[KEY_TITLE]
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvanceMultiSelectCategorySubItemRow) return false
        return otherRow.item == item && otherRow.selectedItems == selectedItems && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowAdvanceMultiSelectCategorySubitemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val bgColor = preferenceRepository.getStringValue(root.context, THEME_NAME, BUBBLE_LEFT_BG_COLOR).toColorInt()
            val drawable = rootLayout.background as GradientDrawable
            drawable.setStroke(2, bgColor)
            val titleText = item[KEY_TITLE].toString()
            title.text = titleText
            description.isVisible = item[DESCRIPTION] != null
            description.text = item[DESCRIPTION].toString()
            val imageUrl = item[IMAGE_URL]
            ivAdvMultiSelect.isVisible = !imageUrl.isNullOrEmpty()
            imageUrl?.let {
                Glide.with(root.context).load(it).error(R.drawable.transparant_image)
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(ivAdvMultiSelect))
            }
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowAdvanceMultiSelectCategorySubitemBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowAdvanceMultiSelectCategorySubitemBinding.commonBind() {
        llCheck.isSelected = selectedItems.contains(item)
        val checkedDrawable: GradientDrawable = llCheck.background as GradientDrawable
        val checkedColor = preferenceRepository.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR).toColorInt()
        val uncheckedColor = preferenceRepository.getStringValue(root.context, THEME_NAME, BUBBLE_LEFT_BG_COLOR).toColorInt()
        checkedDrawable.setColor(if (llCheck.isSelected) checkedColor else Color.WHITE)
        checkedDrawable.setStroke(1.dpToPx(root.context), if (llCheck.isSelected) checkedColor else uncheckedColor)
        llCheck.setOnClickListener {
            if (!isLastItem) {
                return@setOnClickListener
            }
            llCheck.isSelected = !llCheck.isSelected
            val items: ArrayList<Map<String, String>> = selectedItems.clone() as ArrayList<Map<String, String>>
            if (llCheck.isSelected) items.add(item) else items.remove(item)

            onSaveState(id, items, SELECTED_ITEM)
        }
    }
}