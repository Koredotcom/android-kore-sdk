package com.kore.ui.row.botchat

import android.graphics.Color
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.databinding.LineChartTemplateBinding
import com.kore.ui.row.SimpleListRow

class LineChartTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_LINE_CHART_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is LineChartTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is LineChartTemplateRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = LineChartTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val entries = payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>
            val sets: ArrayList<ILineDataSet> = ArrayList(entries.size)
            val dataSet = ArrayList<LineDataSet>(entries.size)

            payload[BotResponseConstants.TEXT]?.let {
                val description = Description()
                description.text = payload[BotResponseConstants.TEXT] as String
                root.description = description
            }

            for (baseIndex in entries.indices) {
                val entry: ArrayList<Entry> = ArrayList()

                for (index in 0 until (entries[baseIndex][BotResponseConstants.VALUES] as List<*>).size) {
                    entry.add(
                        Entry(index.toFloat(), (entries[baseIndex][BotResponseConstants.VALUES] as List<*>)[index].toString().toFloat())
                    )
                }

                dataSet.add(LineDataSet(entry, entries[baseIndex][BotResponseConstants.KEY_TITLE] as String))
                dataSet[baseIndex].lineWidth = 2.5f
                dataSet[baseIndex].circleRadius = 4.5f
                dataSet[baseIndex].setDrawValues(false)
                dataSet[baseIndex].color = MATERIAL_COLORS[baseIndex % 4]
                dataSet[baseIndex].setCircleColor(HOLO_BLUE)
                dataSet[baseIndex].lineWidth = 1f
                dataSet[baseIndex].circleRadius = 3f
                dataSet[baseIndex].setDrawCircleHole(false)
                dataSet[baseIndex].valueTextSize = 9f
                dataSet[baseIndex].setDrawFilled(true)
                dataSet[baseIndex].formLineWidth = 1f
                dataSet[baseIndex].formSize = 15f
                sets.add(dataSet[baseIndex])
            }

            val lineData = LineData(sets)

            // set data
            root.data = lineData
            root.xAxis.textSize = 8f
            root.xAxis.setDrawAxisLine(true)
            root.xAxis.position = XAxis.XAxisPosition.BOTTOM
            root.setDrawGridBackground(false)
            root.xAxis.setDrawGridLines(false) // disable grid lines for the XAxis
            root.axisLeft.setDrawGridLines(false) // disable grid lines for the left YAxis
            root.axisRight.setDrawGridLines(false)
            root.axisRight.isEnabled = false // disable grid lines for the right YAxis

            val xAxis: XAxis = root.xAxis
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return (payload[BotResponseConstants.X_AXIS] as List<*>)[value.toInt()] as String
                }
            }
            xAxis.labelRotationAngle = -60f
            xAxis.setLabelCount((payload[BotResponseConstants.X_AXIS] as List<*>).size, true)

            val l: Legend = root.legend

            l.form = Legend.LegendForm.LINE
            l.textSize = 11f
            l.textColor = Color.BLACK
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
        }
    }
}