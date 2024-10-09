package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.ui.R

class TableResponsiveChildAdapter(
    private val context: Context,
    private val listItems: List<String>,
    private val listHeaderItems: List<List<String>>
) : RecyclerView.Adapter<TableResponsiveChildAdapter.TableViewHolder>() {

    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTableColumnName: TextView
        val tvTableValue: TextView

        init {
            tvTableColumnName = view.findViewById(R.id.tvTableColumnName)
            tvTableValue = view.findViewById(R.id.tvTableValue)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        return TableViewHolder(LayoutInflater.from(context).inflate(R.layout.table_responsive_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return listHeaderItems.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val col = listHeaderItems[position]
        holder.tvTableColumnName.text = col[0]
        holder.tvTableValue.text = listItems[position]
    }

}