package com.kore.ui.row.botchat.form

import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_LEFT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.FORM_FIELDS
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.adapters.FormListAdapter
import com.kore.ui.databinding.FormTempleteViewBinding
import com.kore.ui.row.botchat.BotChatRowType

class FormTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: Map<String, Any>,
    private val isLastItem: Boolean = false,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {

    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_FORM_PROVIDER)
    private val sharedPrefs = PreferenceRepositoryImpl()

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is FormTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is FormTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = FormTempleteViewBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val parentBgColor = sharedPrefs.getStringValue(binding.root.context, THEME_NAME, BUBBLE_LEFT_BG_COLOR, "#3F51B5").toColorInt()
            val drawable = multiSelectLayout.background as GradientDrawable
            drawable.setColor(parentBgColor)
            if (payload[BotResponseConstants.HEADING] != null)
                tvFormTitle.text = payload[BotResponseConstants.HEADING] as String

            formFieldView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        FormTempleteViewBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun FormTempleteViewBinding.commonBind() {
        if (payload[FORM_FIELDS] != null && payload[FORM_FIELDS] is List<*>) {
            formFieldView.adapter = FormListAdapter(root.context, payload[FORM_FIELDS] as List<Map<String, *>>, isLastItem, actionEvent)
        }
    }
}