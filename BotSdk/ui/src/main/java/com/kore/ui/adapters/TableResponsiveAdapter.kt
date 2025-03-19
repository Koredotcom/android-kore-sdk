package com.kore.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class TableResponsiveAdapter(
    private val context: Context,
    private val listItems: List<Map<String, *>>,
    private val listHeaderItems: List<List<String>>,
) : RecyclerView.Adapter<TableResponsiveAdapter.TableViewHolder>() {

    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rlTableResponsive: RelativeLayout = view.findViewById(R.id.rlTableResponsive)
        val tvTableSerial: TextView = view.findViewById(R.id.tvTableSerial)
        val tvTableTitle: TextView = view.findViewById(R.id.tvTableTitle)
        val rvTableResponsive: RecyclerView = view.findViewById(R.id.rvTableResponsive)
        val llTableResponsive: LinearLayout = view.findViewById(R.id.llTableResponsive)
        val ivTableResponsiveExpand: ImageView = view.findViewById(R.id.ivTableResponsiveExpand)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        return TableViewHolder(
            LayoutInflater.from(context).inflate(R.layout.table_responsive, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (listItems.size > 4) 4 else listItems.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val listElementsMap: Map<String, *> = listItems[position]
        val values = listElementsMap[BotResponseConstants.VALUES_CAP] as List<String>
        holder.rlTableResponsive.setBackgroundColor(if (position % 2 == 0) Color.TRANSPARENT else Color.WHITE)

        holder.tvTableSerial.text = values[0]
        holder.tvTableTitle.text = values[1]

        holder.rvTableResponsive.layoutManager = GridLayoutManager(context, values.size / 2)
        holder.rvTableResponsive.adapter = TableResponsiveChildAdapter(context, values, listHeaderItems)

        holder.ivTableResponsiveExpand.setOnClickListener {
            if (holder.rvTableResponsive.visibility == View.GONE) {
                holder.rvTableResponsive.isVisible = true
                holder.llTableResponsive.isVisible = false
                holder.ivTableResponsiveExpand.rotation = 90f
            } else {
                holder.rvTableResponsive.isVisible = false
                holder.llTableResponsive.isVisible = true
                holder.ivTableResponsiveExpand.rotation = 0f
            }
        }
    }
}