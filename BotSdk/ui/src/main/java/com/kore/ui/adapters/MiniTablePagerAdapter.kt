package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R

class MiniTablePagerAdapter(
    private val context: Context,
    private val values: List<Map<String, *>>,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<MiniTablePagerAdapter.MiniViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniViewHolder {
        return MiniViewHolder(LayoutInflater.from(context).inflate(R.layout.mini_table_template_child, parent, false))
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: MiniViewHolder, position: Int) {
        val value = values[position]
        val cols: List<List<String>> = value[BotResponseConstants.PRIMARY] as List<List<String>>
        val rows: List<List<String>> = value[BotResponseConstants.ADDITIONAL] as List<List<String>>

        val sharedPrefs = PreferenceRepositoryImpl()
        val bgColor = sharedPrefs.getStringValue(
            holder.rlMiniTable.context,
            BotResponseConstants.THEME_NAME,
            BotResponseConstants.BUBBLE_LEFT_BG_COLOR,
            "#efeffc"
        ).toColorInt()
        holder.rlMiniTable.setBackgroundColor(bgColor)
        holder.rlMiniTable.setRoundedCorner(4.dpToPx(context).toFloat())

        holder.rvTableView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        holder.rvTableView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        holder.rvTableView.adapter = MiniTableTemplateChildAdapter(context, rows, cols, actionEvent)

        holder.rvTableViewHeader.layoutManager = GridLayoutManager(context, cols.size)

        holder.rvTableViewHeader.adapter = TableTemplateHeaderAdapter(context, cols)
    }

    class MiniViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rlMiniTable: RelativeLayout = view.findViewById(R.id.rlMiniTable)
        val rvTableViewHeader: RecyclerView = view.findViewById(R.id.rvTableViewHeader)
        val rvTableView: RecyclerView = view.findViewById(R.id.rvTableView)
    }
}