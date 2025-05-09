package com.kore.ui.row.botchat

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.google.gson.internal.LinkedTreeMap
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR
import com.kore.model.constants.BotResponseConstants.ELEMENTS
import com.kore.model.constants.BotResponseConstants.FORM_PLACE_HOLDER
import com.kore.model.constants.BotResponseConstants.HEADING
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.LABEL
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.model.constants.BotResponseConstants.VALUE
import com.kore.ui.R
import com.kore.ui.databinding.RowDropdownTemplateBinding
import com.kore.ui.row.SimpleListRow
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
            tvDropDownTitle.text = (payload[LABEL] ?: payload[HEADING]).toString()
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowDropdownTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowDropdownTemplateBinding.commonBind() {
        placeHolder = if (payload[FORM_PLACE_HOLDER] != null) payload[FORM_PLACE_HOLDER] as String else root.context.getString(R.string.select)
        this@DropDownTemplateRow.spinner = this.spinner
        tvDropDownHeading.isVisible = payload[HEADING] != null
        tvDropDownHeading.setText(payload[HEADING].toString())
        val dataAdapter = SpinnerAdapter(root.context)
        spinner.adapter = dataAdapter
        spinner.prompt = root.context.getString(R.string.select)
        spinner.isClickable = isLastItem
        spinner.isEnabled = isLastItem
        spinner.setSelection(selectedPosition)
        val sharedPrefs = PreferenceRepositoryImpl()
        val bgColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
        val txtColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF").toColorInt()
        submit.setRoundedCorner(6.dpToPx(root.context).toFloat())
        submit.setBackgroundColor(bgColor)
        submit.setTextColor(txtColor)
        submit.setOnClickListener {
            if (!isLastItem || selectedPosition <= 0) return@setOnClickListener
            if (elements[selectedPosition - 1][KEY_TITLE]?.equals(placeHolder) == false) {
                actionEvent(BotChatEvent.SendMessage(elements[selectedPosition - 1][KEY_TITLE]!!, elements[selectedPosition - 1][VALUE]!!))
            }
        }
    }

    inner class SpinnerAdapter(context: Context) :
        BaseAdapter() {
        private var sharedPreferences: SharedPreferences = context.getSharedPreferences(BotResponseConstants.THEME_NAME, Context.MODE_PRIVATE)
        private var leftBgColor: String = sharedPreferences.getString(BotResponseConstants.BUBBLE_LEFT_BG_COLOR, "#FFFFFF")!!
        private var rightBgColor: String = sharedPreferences.getString(BUBBLE_RIGHT_BG_COLOR, "#FFFFFF")!!
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
                holder.ivTick = convertView.findViewById(R.id.ivTick)
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }
            holder.ivTick.isVisible = false
            val item = getItem(position)
            holder.item.text = if (item is String) item else (item as Map<String, String>)[KEY_TITLE]
            holder.item.setOnClickListener {
                if (isLastItem) {
                    spinner.setSelection(position)
                    onDropDownSelection(id, position, BotResponseConstants.SELECTED_ITEM)
                }
                hideSpinnerDropDown(spinner)
                spinner.setSelection(position)
            }
            return convertView!!
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = super.getDropDownView(position, convertView, parent) as LinearLayout
            val ivTick = view.findViewById<ImageView>(R.id.ivTick)

            if ((selectedPosition == -1 && position == 0) || selectedPosition == position) {
                view.setBackgroundColor(leftBgColor.toColorInt())
                val colorStateList = ColorStateList.valueOf(rightBgColor.toColorInt())
                ivTick.imageTintList = colorStateList
                ivTick.isVisible = true
            } else {
                view.setBackgroundColor(Color.WHITE)
                ivTick.isVisible = false
            }
            return view
        }

        inner class ViewHolder {
            lateinit var item: TextView
            lateinit var ivTick: ImageView
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