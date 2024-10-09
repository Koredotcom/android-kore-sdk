package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class FormListAdapter(
    val context: Context,
    private val arrFormTemplateModels: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<FormListAdapter.FormHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormHolder {
        return FormHolder(LayoutInflater.from(context).inflate(R.layout.form_templete_cell_view, parent, false))
    }

    override fun getItemCount(): Int {
        return arrFormTemplateModels.size
    }

    override fun onBindViewHolder(holder: FormHolder, position: Int) {
        val form = arrFormTemplateModels[position]
        holder.tvFormFieldTitle.text = form[BotResponseConstants.FORM_LABEL] as String
        holder.edtFormInput.hint = form[BotResponseConstants.FORM_PLACE_HOLDER] as String

        if (form[BotResponseConstants.FORM_FIELD_BUTTON] != null) {
            val fieldButton = form[BotResponseConstants.FORM_FIELD_BUTTON] as Map<*, *>
            holder.btnFieldButton.isVisible = true
            holder.btnFieldButton.text = fieldButton[BotResponseConstants.KEY_TITLE] as String
            holder.edtFormInput.isEnabled = isLastItem
            holder.edtFormInput.isFocusable = isLastItem
            holder.edtFormInput.isClickable = isLastItem
            holder.btnFieldButton.setOnClickListener {
                if (isLastItem) actionEvent(
                    BotChatEvent.SendMessage(
                        getDotMessage(holder.edtFormInput.text.toString()),
                        holder.edtFormInput.text.toString()
                    )
                )
            }
        }
    }

    private fun getDotMessage(strPassword: String): String {
        val strDots = StringBuilder()
        for (i in strPassword.indices) {
            strDots.append("•")
        }
        return strDots.toString()
    }

    class FormHolder(view: View) : RecyclerView.ViewHolder(view) {
        var btnFieldButton: TextView = view.findViewById(R.id.btFieldButton)
        var tvFormFieldTitle: TextView = view.findViewById(R.id.tvFormFieldTitle)
        var edtFormInput: EditText = view.findViewById(R.id.edtFormInput)
    }
}