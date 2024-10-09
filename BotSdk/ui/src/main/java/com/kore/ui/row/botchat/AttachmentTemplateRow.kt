package com.kore.ui.row.botchat

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.row.SimpleListRow
import com.kore.common.utils.FontUtils
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.databinding.RowAttachmentTemplateBinding

class AttachmentTemplateRow(
    private val id: String,
    private val payload: HashMap<String, Any>,
    private val iconUrl: String?,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_ADVANCE_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AttachmentTemplateRow) return false
        return otherRow.id == id
    }


    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AttachmentTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowAttachmentTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val context = root.context
            val sharedPrefs = PreferenceRepositoryImpl()
            val drawable = upload.background as GradientDrawable
            val btnBgColor = Color.parseColor(getColor(context, sharedPrefs, BotResponseConstants.BUTTON_ACTIVE_BG_COLOR, "#FF5E00"))
            val textColor = Color.parseColor(getColor(context, sharedPrefs, BotResponseConstants.BUBBLE_LEFT_TEXT_COLOR, "#313131"))
            val rootBgColor = Color.parseColor(getColor(context, sharedPrefs, BotResponseConstants.BUBBLE_LEFT_BG_COLOR, "#FCE9E6"))
            note.setTextColor(textColor)
            drawable.setColor(btnBgColor)
            upload.background = drawable
            val rootDrawable = root.background as GradientDrawable
            rootDrawable.setColor(rootBgColor)
            root.background = rootDrawable
            val buttons = payload[BotResponseConstants.KEY_BUTTONS] as ArrayList<HashMap<String, String>>?
            if (!buttons.isNullOrEmpty()) {
                upload.text = buttons[0][BotResponseConstants.KEY_TITLE]
            }
            FontUtils.applyCustomFont(root.context, root, true)
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowAttachmentTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun getColor(context: Context, sharedPrefs: PreferenceRepositoryImpl, key: String, defaultValue: String): String {
        return sharedPrefs.getStringValue(context, BotResponseConstants.THEME_NAME, key, defaultValue)
    }

    private fun RowAttachmentTemplateBinding.commonBind() {
        upload.setOnClickListener {
            upload.setOnClickListener {
                if (!isLastItem) return@setOnClickListener
                actionEvent(BotChatEvent.ShowAttachmentOptions)
            }
        }
    }
}