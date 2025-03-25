package com.kore.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.ui.R

class MiniTableTemplateChildAdapter(
    private val context: Context,
    private val listItems: List<List<String>>,
    private val listHeaderItems: List<List<String>>,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<MiniTableTemplateChildAdapter.TableViewHolder>() {
    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rvChildTableLayout: RecyclerView = view.findViewById(R.id.rvChild)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val convertView: View =
            LayoutInflater.from(context)
                .inflate(R.layout.table_template_child_layout, parent, false)
        return TableViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.rvChildTableLayout.setBackgroundColor(if ((position % 2) == 0) Color.TRANSPARENT else Color.WHITE)
        if (listItems[position].isNotEmpty()) {
            holder.rvChildTableLayout.layoutManager = GridLayoutManager(context, listItems[position].size)

            holder.rvChildTableLayout.adapter =
                TableTemplateDataAdapter(context, listItems[position], listHeaderItems)
        }
    }
}