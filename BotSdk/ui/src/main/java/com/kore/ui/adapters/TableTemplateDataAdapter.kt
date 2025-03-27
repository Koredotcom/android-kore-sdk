package com.kore.ui.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class TableTemplateDataAdapter(
    private val context: Context,
    private val listItems: List<String>,
    private val listHeaderItems: List<List<String>>,
) : RecyclerView.Adapter<TableTemplateDataAdapter.TableViewHolder>() {
    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvChildValue: TextView = view.findViewById(R.id.tvTableValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        return TableViewHolder(LayoutInflater.from(context).inflate(R.layout.table_child_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val value = listItems[position]
        val cols = listHeaderItems[position]

        holder.tvChildValue.text = value
        holder.tvChildValue.gravity = Gravity.START or Gravity.CENTER_VERTICAL

        if (cols.size > 1) {
            when (cols[1]) {
                BotResponseConstants.CENTER -> holder.tvChildValue.gravity = Gravity.CENTER
                BotResponseConstants.RIGHT -> holder.tvChildValue.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            }
        }
    }
}