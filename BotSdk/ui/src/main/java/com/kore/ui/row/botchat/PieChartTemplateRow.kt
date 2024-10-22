package com.kore.ui.row.botchat

import android.graphics.Color
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R
import com.kore.ui.databinding.PieChartTemplateBinding

class PieChartTemplateRow(
    private val id: String,
    private val payload: HashMap<String, Any>,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_PIE_CHART_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is PieChartTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is PieChartTemplateRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = PieChartTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val entries = payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>
            val xVal: ArrayList<String> = ArrayList(entries.size)
            val yVal: ArrayList<PieEntry> = ArrayList(entries.size)
            val arrLabels: ArrayList<String> = ArrayList(entries.size)
            val holeRadius: Float
            val transparentCircleRadius: Float
            val pieType = payload[BotResponseConstants.PIE_TYPE] as String

            for (k in entries.indices) {
                xVal.add(entries[k][BotResponseConstants.KEY_TITLE] as String)
                yVal.add(PieEntry((entries[k][BotResponseConstants.VALUE] as String).toFloat(), " "))
                arrLabels.add(entries[k][BotResponseConstants.KEY_TITLE] as String + " " + entries[k][BotResponseConstants.VALUE])
            }

            if (pieType == BotResponseConstants.PIE_TYPE_DONUT) {
                holeRadius = 56f
                transparentCircleRadius = 61f
            } else {
                holeRadius = 0f
                transparentCircleRadius = 0f
            }
            val description = Description()
            description.text = ""

            root.description = description
            root.setUsePercentValues(true)
            root.isDrawHoleEnabled = true
            root.holeRadius = holeRadius
            root.transparentCircleRadius = transparentCircleRadius
            root.rotationAngle = -30f
            root.isRotationEnabled = true

            addData(childBinding, pieType, yVal)

            val arrLegendEntries: java.util.ArrayList<LegendEntry> = java.util.ArrayList<LegendEntry>()
            val colorsArray: Array<String> = root.context.resources.getStringArray(R.array.color_set)
            for (i in yVal.indices) {
                val legendEntryA = LegendEntry()
                legendEntryA.label = arrLabels[i]
                legendEntryA.formColor = Color.parseColor(colorsArray[i])
                arrLegendEntries.add(legendEntryA)
            }

            val l: Legend = root.legend
            l.setCustom(arrLegendEntries)
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            root.setEntryLabelColor(Color.WHITE)
            root.setEntryLabelTextSize(12f)
        }
    }

    private fun addData(binding: PieChartTemplateBinding, pieType: String, yValues: java.util.ArrayList<PieEntry>) {
        val dataSet = PieDataSet(yValues, "")
        dataSet.sliceSpace = 3F
        dataSet.selectionShift = 5F
        val colors = java.util.ArrayList<Int>()
        val colorsArray: Array<String> = binding.root.context.resources.getStringArray(R.array.color_set)
        for (color in colorsArray) {
            colors.add(Color.parseColor(color))
        }
        dataSet.colors = colors
        if (pieType == BotResponseConstants.PIE_TYPE_DONUT) {
            dataSet.valueLinePart1OffsetPercentage = 80f
            dataSet.valueLinePart1Length = 0.2f
            dataSet.valueLinePart2Length = 0.4f
            dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        }
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        binding.root.data = data
        binding.root.highlightValues(null)
        binding.root.invalidate()
    }
}