package kore.botssdk.view.row.botrequestresponse

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.method.LinkMovementMethod
import android.view.Gravity
import androidx.viewbinding.ViewBinding
import kore.botssdk.databinding.RowBotRequestResponseMsgBinding
import kore.botssdk.extensions.formatEmojis
import kore.botssdk.extensions.spannable
import kore.botssdk.view.row.SimpleListRow

class BotRequestResponseRow(
    override val type: SimpleListRow.SimpleListRowType,
    private val id: String,
    private val botMessage: String,
    private val textColor: String,
    private val bgColor: String,
    private val drawable: GradientDrawable,
    private val isBotRequest: Boolean,
    private val isLastItem: Boolean = false,
) : SimpleListRow {

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotRequestResponseRow) return false
        return otherRow.id == id || otherRow.botMessage == botMessage
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
        message.text = botMessage.formatEmojis().spannable(root.context)
        message.setTextColor(Color.parseColor(textColor))
        drawable.setColor(Color.parseColor(bgColor))
        message.background = drawable
        message.movementMethod = LinkMovementMethod.getInstance()
        message.isClickable = isLastItem
        root.gravity = if (isBotRequest) Gravity.END else Gravity.START
    }
}