package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class AdvanceTableListOuterAdapter(
    val context: Context,
    val buttons: List<Map<String, *>>,
) : RecyclerView.Adapter<TableViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        return TableViewHolder(LayoutInflater.from(context).inflate(R.layout.advance_list_table_outer_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return buttons.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val button = buttons[position]
        holder.rvTableList.layoutManager = GridLayoutManager(context, 2)
        holder.rvTableList.adapter = AdvanceTableListAdapter(context, button[BotResponseConstants.ROW_DATA] as List<Map<String, *>>)
    }

}

class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var rvTableList: RecyclerView

    init {
        rvTableList = itemView.findViewById(R.id.rvTableList)
    }
}