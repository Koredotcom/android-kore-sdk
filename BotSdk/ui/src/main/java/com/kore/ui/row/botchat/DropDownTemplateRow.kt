package com.kore.ui.row.botchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.viewbinding.ViewBinding
import com.google.gson.internal.LinkedTreeMap
import com.kore.common.event.UserActionEvent
import com.kore.common.row.SimpleListRow
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.ELEMENTS
import com.kore.model.constants.BotResponseConstants.FORM_PLACE_HOLDER
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.LABEL
import com.kore.ui.R
import com.kore.ui.databinding.RowDropdownTemplateBinding
import java.lang.reflect.AccessibleObject

class DropDownTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val selectedPosition: Int,
    private val isLastItem: Boolean,
    private val onDropDownSelection: (id: String, item: Int?, key: String) -> Unit,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_DROP_DOWN_PROVIDER)
    private var elements: List<LinkedTreeMap<String, String>> = payload[ELEMENTS] as List<LinkedTreeMap<String, String>>
    private lateinit var spinner: AppCompatSpinner
    private var placeHolder: String = ""

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DropDownTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DropDownTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem && otherRow.selectedPosition == selectedPosition
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any {
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, true, true)
        val childBinding = RowDropdownTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            tvDropDownTitle.text = payload[LABEL] as String
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowDropdownTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowDropdownTemplateBinding.commonBind() {
        placeHolder = if (payload[FORM_PLACE_HOLDER] != null) payload[FORM_PLACE_HOLDER] as String else root.context.getString(R.string.select)
        this@DropDownTemplateRow.spinner = this.spinner
        val dataAdapter = SpinnerAdapter(root.context)
        spinner.adapter = dataAdapter
        spinner.prompt = root.context.getString(R.string.select)
        spinner.isClickable = isLastItem
        spinner.isEnabled = isLastItem
        spinner.setSelection(selectedPosition)
        submit.setOnClickListener {
            if (!isLastItem || selectedPosition <= 0) return@setOnClickListener
            if (elements[selectedPosition - 1][KEY_TITLE]?.equals(placeHolder) == false) {
                actionEvent(BotChatEvent.SendMessage(elements[selectedPosition - 1][KEY_TITLE]!!))
            }
        }
    }

    inner class SpinnerAdapter(context: Context) :
        BaseAdapter() {
        private var inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return elements.size + 1
        }

        override fun getItem(position: Int): Any {
            return if (position <= 0) placeHolder else elements[position - 1]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            var convertView = view
            val holder: ViewHolder
            if (convertView?.tag == null) {
                convertView = inflater.inflate(R.layout.drop_down_item_view, null)
                holder = ViewHolder()
                holder.item = convertView.findViewById(R.id.item)
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }
            val item = getItem(position)
            holder.item.text = if (item is String) item else (item as Map<String, String>)[KEY_TITLE]
            holder.item.setOnClickListener {
                if (isLastItem) {
                    onDropDownSelection(id, position, BotResponseConstants.SELECTED_ITEM)
                }
                hideSpinnerDropDown(spinner)
                spinner.setSelection(position)
            }
            return convertView!!
        }

        inner class ViewHolder {
            lateinit var item: TextView
        }
    }

    fun hideSpinnerDropDown(spinner: Spinner?) {
        try {
            val method = Spinner::class.java.getDeclaredMethod("onDetachedFromWindow")
            AccessibleObject.setAccessible(arrayOf<AccessibleObject>(method), true)
            method.invoke(spinner)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}