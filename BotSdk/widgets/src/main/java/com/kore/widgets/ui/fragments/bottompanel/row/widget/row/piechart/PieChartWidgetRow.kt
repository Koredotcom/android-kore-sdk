package com.kore.widgets.ui.fragments.bottompanel.row.widget.row.piechart

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.kore.widgets.R
import com.kore.widgets.adapters.WidgetsButtonListAdapter
import com.kore.widgets.constants.Constants
import com.kore.widgets.constants.Constants.KEY_TITLE
import com.kore.widgets.databinding.RowPiechartWidgetBinding
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.model.WidgetInfoModel
import com.kore.widgets.model.WidgetsModel
import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.WidgetInfoRowType
import androidx.core.graphics.toColorInt

class PieChartWidgetRow(
    private val skillName: String,
    private val widgetsModel: WidgetsModel,
    private val widgetInfoModel: WidgetInfoModel?,
    private val actionEvent: (event: BaseActionEvent) -> Unit
) : WidgetSimpleListRow {
    override val type: WidgetSimpleListRow.SimpleListRowType = WidgetInfoRowType.PieChart

    override fun areItemsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is PieChartWidgetRow) return false
        return otherRow.widgetsModel.id == widgetsModel.id
    }

    override fun areContentsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is PieChartWidgetRow) return false
        return otherRow.widgetInfoModel == widgetInfoModel
    }

    override fun getChangePayload(otherRow: WidgetSimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as RowPiechartWidgetBinding).commonBind()
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        (binding as RowPiechartWidgetBinding).commonBind()
    }

    private fun RowPiechartWidgetBinding.commonBind() {
        widgetInfoModel?.let { infoModel ->
            val entries = widgetInfoModel.elements ?: return

            val xVal: ArrayList<String> = ArrayList(entries.size)
            val yVal: ArrayList<PieEntry> = ArrayList(entries.size)
            val arrLabels: ArrayList<String> = ArrayList(entries.size)
            val holeRadius: Float
            val transparentCircleRadius: Float
            val pieType = widgetInfoModel.pieType

            for (k in entries.indices) {
                xVal.add(entries[k][KEY_TITLE] as String)
                yVal.add(PieEntry((entries[k][Constants.VALUE] as String).toFloat(), " "))
                arrLabels.add(entries[k][KEY_TITLE] as String + " " + entries[k][Constants.VALUE])
            }

            if (pieType == Constants.PIE_TYPE_DONUT) {
                holeRadius = 56f
                transparentCircleRadius = 61f
            } else {
                holeRadius = 0f
                transparentCircleRadius = 0f
            }
            val description = Description()
            description.text = ""

            mChart.description = description
            mChart.setUsePercentValues(true)
            mChart.isDrawHoleEnabled = true
            mChart.setDrawEntryLabels(false)
            mChart.holeRadius = holeRadius
            mChart.transparentCircleRadius = transparentCircleRadius
            mChart.rotationAngle = 0f
            mChart.isRotationEnabled = true

            mChart.animateY(1400, Easing.EaseInOutQuad)

            addData(mChart, pieType!!, yVal)

            val arrLegendEntries: java.util.ArrayList<LegendEntry> = java.util.ArrayList<LegendEntry>()
            val colorsArray: Array<String> = mChart.context.resources.getStringArray(R.array.color_set)
            for (i in yVal.indices) {
                val legendEntryA = LegendEntry()
                legendEntryA.label = arrLabels[i]
                legendEntryA.formColor = colorsArray[i].toColorInt()
                arrLegendEntries.add(legendEntryA)
            }

            val l: Legend = mChart.legend
            l.setCustom(arrLegendEntries)
            l.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.textSize = 16f
            l.setDrawInside(false)
            l.xEntrySpace = 7f
            l.yEntrySpace = 0f
            l.yOffset = 0f

            if (!infoModel.buttons.isNullOrEmpty()) {
                buttonsList.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                val buttonRecyclerAdapter = WidgetsButtonListAdapter(root.context, infoModel.buttons, "", actionEvent)
                buttonRecyclerAdapter.skillName = skillName
                buttonRecyclerAdapter.setIsFromFullView(false)
                buttonsList.adapter = buttonRecyclerAdapter
                buttonRecyclerAdapter.notifyItemRangeChanged(0, infoModel.buttons.size - 1)
            }
        }
    }

    private fun addData(pieChart: PieChart, pieType: String, yValues: java.util.ArrayList<PieEntry>) {
        val dataSet = PieDataSet(yValues, "")
        dataSet.sliceSpace = 3F
        dataSet.selectionShift = 5F
        val colors = java.util.ArrayList<Int>()
        val colorsArray: Array<String> = pieChart.context.resources.getStringArray(R.array.color_set)
        for (color in colorsArray) {
            colors.add(color.toColorInt())
        }
        dataSet.colors = colors
        if (pieType == Constants.PIE_TYPE_DONUT) {
            dataSet.valueLinePart1OffsetPercentage = 80f
            dataSet.valueLinePart1Length = 0.2f
            dataSet.valueLinePart2Length = 0.4f
            dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        }
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()
    }
}