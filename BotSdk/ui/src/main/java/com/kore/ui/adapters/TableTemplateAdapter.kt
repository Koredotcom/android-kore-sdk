package com.kore.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class TableTemplateAdapter(
    private val context: Context,
    private val listItems: List<Map<String, *>>,
    private val listHeaderItems: List<List<String>>,
) : RecyclerView.Adapter<TableTemplateAdapter.TableViewHolder>() {
    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rvChildTableLayout: RecyclerView = view.findViewById(R.id.rvChild)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.table_template_child_layout, parent, false)
        return TableViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val listElementsMap: Map<String, *> = listItems[position]
        val values = listElementsMap[BotResponseConstants.VALUES_CAP] as List<String>
        holder.rvChildTableLayout.setBackgroundColor(if (position % 2 == 0) Color.TRANSPARENT else Color.WHITE)

        if (values.isNotEmpty()) {
            holder.rvChildTableLayout.layoutManager = GridLayoutManager(context, values.size)
            holder.rvChildTableLayout.adapter = TableTemplateDataAdapter(context, values, listHeaderItems)
        }
    }
}