package kore.botssdk.view.row.botrequestresponse

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.viewbinding.ViewBinding
import kore.botssdk.databinding.RowBotRequestResponseMsgBinding
import kore.botssdk.view.row.SimpleListRow

class BotRequestResponseRow(
    override val type: SimpleListRow.SimpleListRowType,
    private val id: String,
    private val message: String,
    private val textColor: String,
    private val bgColor: String,
    private val drawable: GradientDrawable,
    private val isLastItem: Boolean = false
) : SimpleListRow {

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotRequestResponseRow) return false
        return otherRow.id == id || otherRow.message == message
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any {
        return true
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotRequestResponseRow) return false
        return otherRow.textColor == textColor || otherRow.bgColor == bgColor
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as RowBotRequestResponseMsgBinding).commonBind()
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        (binding as RowBotRequestResponseMsgBinding).commonBind()
    }

    private fun RowBotRequestResponseMsgBinding.commonBind() {
        root.text = message
        root.setTextColor(Color.parseColor(textColor))
        drawable.setColor(Color.parseColor(bgColor))
        root.background = drawable
    }
}