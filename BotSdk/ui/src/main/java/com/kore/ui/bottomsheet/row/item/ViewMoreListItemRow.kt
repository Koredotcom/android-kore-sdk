package com.kore.ui.bottomsheet.row.item

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.kore.common.extensions.dpToPx
import com.kore.common.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants.BUTTON_ACTIVE_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR
import com.kore.model.constants.BotResponseConstants.COLOR
import com.kore.model.constants.BotResponseConstants.IMAGE_URL
import com.kore.model.constants.BotResponseConstants.KEY_SUB_TITLE
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.model.constants.BotResponseConstants.VALUE
import com.kore.model.constants.BotResponseConstants.WIDGET_BORDER_COLOR
import com.kore.ui.R
import com.kore.ui.bottomsheet.row.ViewMoreListItemRowType
import com.kore.ui.databinding.RowListviewTemplateItemBinding
import com.kore.ui.transforms.RoundedCornersTransform
import com.squareup.picasso.Picasso

class ViewMoreListItemRow(
    private val item: Map<String, Any>,
) : SimpleListRow() {
    override val type: SimpleListRowType = ViewMoreListItemRowType.Item

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ViewMoreListItemRow) return false
        return otherRow.item == item
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ViewMoreListItemRow) return false
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowListviewTemplateItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val sharedPreferences = root.context.getSharedPreferences(THEME_NAME, Context.MODE_PRIVATE)
            item[KEY_TITLE]?.let { title.text = it.toString() }
            item[KEY_SUB_TITLE]?.let { tvSubTitle.text = it.toString() }
            item[VALUE]?.let { itemCost.text = it.toString() }
            item[COLOR]?.let { itemCost.setTextColor(Color.parseColor(it.toString())) }

            val imageUrl = item[IMAGE_URL]
            imageView.isVisible = imageUrl != null && imageUrl.toString().isNotEmpty()
            item[IMAGE_URL]?.let {
                if (it.toString().isNotEmpty()) {
                    Picasso.get().load(it.toString()).transform(RoundedCornersTransform()).into(imageView)
                }
            }

            val rightDrawable =
                ResourcesCompat.getDrawable(root.resources, R.drawable.rounded_rect_feedback, root.context.theme) as GradientDrawable?
            if (rightDrawable != null && sharedPreferences != null) {
                rightDrawable.setColor(Color.parseColor(sharedPreferences.getString(BUTTON_ACTIVE_BG_COLOR, "#ffffff")))
                rightDrawable.setStroke(
                    1.dpToPx(root.context),
                    Color.parseColor(sharedPreferences.getString(WIDGET_BORDER_COLOR, "#3942f6"))
                )
                root.background = rightDrawable
                title.setTextColor(Color.parseColor(sharedPreferences.getString(BUTTON_ACTIVE_TXT_COLOR, "#505968")))
            }
        }
    }
}