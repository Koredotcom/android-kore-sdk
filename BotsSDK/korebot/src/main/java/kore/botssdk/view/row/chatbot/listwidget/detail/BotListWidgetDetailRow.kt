package kore.botssdk.view.row.chatbot.listwidget.detail

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.squareup.picasso.Picasso
import kore.botssdk.databinding.ListwidgetDetailsItemBinding
import kore.botssdk.models.ContentModel
import kore.botssdk.utils.KaFontUtils
import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.chatbot.listwidget.BotListWidgetRowType
import kore.botssdk.view.viewUtils.RoundedCornersTransform

class BotListWidgetDetailRow(
    private val id: String,
    private val contentModel: ContentModel,
    private val activeTxtColor: String
) : SimpleListRow {
    override val type: SimpleListRow.SimpleListRowType = BotListWidgetRowType.Details

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotListWidgetDetailRow) return false
        return otherRow.id == id && otherRow.contentModel == contentModel
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotListWidgetDetailRow) return false
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as ListwidgetDetailsItemBinding).apply {
            KaFontUtils.applyCustomFont(root.context, root)
            tvBtnText.text = contentModel.description
            tvBtnText.setTextColor(Color.parseColor(activeTxtColor))
            ivListBtnIcon.isVisible = !contentModel.image.image_src.isNullOrEmpty()
            if (!contentModel.image.image_src.isNullOrEmpty()) {
                val url = contentModel.image.image_src.trim().replace("http://", "https://")
                Picasso.get().load(url).transform(RoundedCornersTransform()).into(ivListBtnIcon)
            }
        }
    }
}