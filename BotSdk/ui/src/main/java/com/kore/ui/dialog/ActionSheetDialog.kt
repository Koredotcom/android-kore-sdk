package com.kore.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.utils.FontUtils.applyCustomFont
import com.kore.ui.R
import com.kore.ui.row.actionsheet.ActionSheetRow
import com.kore.ui.row.actionsheet.ActionSheetRowType

class ActionSheetDialog(private val context: Context) : Dialog(context) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var optionsListView: RecyclerView? = null
    private var adapter: SimpleListAdapter? = null
    private var actionItems: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        val layoutView = layoutInflater.inflate(R.layout.reusable_listview_actionsheet, null)
        setContentView(layoutView)
        optionsListView = layoutView.findViewById<View>(R.id.template_selection_tasksListView) as RecyclerView
        val textView = findViewById<View>(R.id.template_empty_text) as TextView
        optionsListView?.layoutManager = LinearLayoutManager(context)
        textView.visibility = View.VISIBLE
        applyCustomFont(context, layoutView, false)
        if (adapter == null) {
            adapter = SimpleListAdapter(ActionSheetRowType.values().toList())
            optionsListView?.adapter = adapter
        }
    }

    fun show(actionItems: List<String>, onItemClick: (option: String) -> Unit) {
        show()
        this.actionItems = actionItems
        val rows = actionItems.map { data ->
            ActionSheetRow(ActionSheetRowType.Attachments, data, onItemClick)
        }
        adapter?.submitList(rows)
    }


    override fun onStart() {
        super.onStart()
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}