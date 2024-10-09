package com.kore.ui.row.botchat

import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.kore.common.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BAR_CHART_DIRECTION_VERTICAL
import com.kore.model.constants.BotResponseConstants.DIRECTION
import com.kore.model.constants.BotResponseConstants.DISPLAY_VALUES
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.STACKED
import com.kore.model.constants.BotResponseConstants.VALUES
import com.kore.model.constants.BotResponseConstants.X_AXIS
import com.kore.ui.R
import com.kore.ui.databinding.BarChartTemplateBinding
import com.kore.ui.row.formatters.BarChartDataFormatter
import com.kore.ui.utils.ColorTemplate
import com.kore.ui.views.CustomMarkerView

class BarChartTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>
) : SimpleListRow(), OnChartValueSelectedListener {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_BARCHART_PROVIDER)

    private companion object {
        private const val BAR_STACKED = 0
        private const val BAR_VERTICAL = 1
        private const val BAR_HORIZONTAL = 2
    }

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BarChartTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BarChartTemplateRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = BarChartTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val elements = payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>
            val barType = if (payload[STACKED] as Boolean) BAR_STACKED
            else if (payload[DIRECTION].toString() == BAR_CHART_DIRECTION_VERTICAL) BAR_VERTICAL
            else BAR_HORIZONTAL
            barChart.visibility = if (barType != BAR_HORIZONTAL) View.VISIBLE else View.GONE
            horizontalBarChart.visibility = if (barType == BAR_HORIZONTAL) View.VISIBLE else View.GONE
            val mChart = if (barType == BAR_HORIZONTAL) horizontalBarChart else barChart
            initBarChart(barType, mChart)

            val barWidth = 0.2f
            val groupSpace = 0.08f
            val barSpace = 0.03f // x4 DataSet

            val startYear = 1
            val groupCount = 4
            val yValues: Array<ArrayList<BarEntry>?>

            val dataSet: Array<BarDataSet?>
            val barDataSets: MutableList<IBarDataSet?> = ArrayList()

            if (elements.isNotEmpty()) {
                val size: Int = elements.size
                val labels = arrayOfNulls<String>(size)
                val dataList: ArrayList<Map<String, *>> = ArrayList(size)
                yValues = arrayOfNulls(size)
                if (barType == BAR_STACKED) {
                    for (index in 0 until size) {
                        dataList.add(elements[index])
                        labels[index] = elements[index][KEY_TITLE].toString()
                    }
                    val values = (dataList[0][VALUES] as List<Float>)
                    for (k in values.indices) {
                        val arr = FloatArray(size)
                        for (j in 0 until size) {
                            val innerValues = (dataList[j][VALUES] as List<Float>)
                            arr[j] = innerValues[k]
                        }
                        yValues[0] = ArrayList()
                        yValues[0]?.add(BarEntry((k + 1).toFloat(), arr, ""))
                    }
                } else {
                    for (index in 0 until size) {
                        val model: Map<String, *> = elements[index]
                        yValues[index] = ArrayList()
                        val values = model[VALUES] as List<Float>
                        val displayValues = model[DISPLAY_VALUES] as List<String>
                        for (inner in values.indices) {
                            yValues[index]?.add(BarEntry((inner + 1).toFloat(), values[inner], displayValues[inner]))
                        }
                    }
                }

                dataSet = arrayOfNulls(size)

                if (barType == BAR_STACKED) {
                    dataSet[0] = BarDataSet(yValues[0], "")
                    dataSet[0]?.colors = getColors(size).toMutableList()
                    dataSet[0]?.stackLabels = labels
                    barDataSets.add(dataSet[0])
                } else {
                    for (k in 0 until size) {
                        dataSet[k] = BarDataSet(yValues[k], elements[k][KEY_TITLE] as String?)
                        dataSet[k]!!.color = ColorTemplate.MATERIAL_COLORS[k % 4]
                        barDataSets.add(dataSet[k])
                    }
                }

                val data = BarData(barDataSets)
                data.setValueFormatter(BarChartDataFormatter())
                val xAxis = mChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textSize = 8f
                xAxis.setDrawGridLines(false)
                if (barType == BAR_STACKED) {
                    xAxis.labelCount = 4
                }
                xAxis.granularity = if (barType == BAR_STACKED) 1f else 0.5f // only intervals of 1 day

                val xAxises: List<String> = payload[X_AXIS] as List<String>
                xAxis.labelCount = xAxises.size
                if (barType == BAR_HORIZONTAL) {
                    xAxis.setDrawLabels(true)
                    xAxis.setCenterAxisLabels(true)
                }
                xAxis.labelRotationAngle = -60f

                val xAxisFormatter: ValueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(v: Float): String {
                        return if ((v * 2).toInt() - 2 >= 0 && (v * 2).toInt() - 2 < xAxises.size) xAxises
                            .get((v * 2).toInt() - 2) else ""
                    }
                }

                xAxis.valueFormatter = xAxisFormatter

                val legend = mChart.legend
                legend.verticalAlignment = if (barType == BAR_HORIZONTAL) Legend.LegendVerticalAlignment.BOTTOM else Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.orientation = Legend.LegendOrientation.HORIZONTAL
                legend.setDrawInside(false)
                legend.form = Legend.LegendForm.SQUARE
                legend.formSize = 9f
                legend.textSize = 11f
                legend.xEntrySpace = 4f

                mChart.data = data

                mChart.barData.barWidth = barWidth
                if (barType == BAR_STACKED) {
                    mChart.setFitBars(true)
                    return
                }

                // restrict the x-axis range
                mChart.xAxis.axisMinimum = startYear.toFloat()

                // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
                mChart.xAxis.axisMaximum = startYear + mChart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
                mChart.groupBars(startYear.toFloat(), groupSpace, barSpace)
            }
        }
    }

    override fun onValueSelected(p0: Entry?, p1: Highlight?) {
    }

    override fun onNothingSelected() {
    }

    private fun getColors(stackSize: Int): IntArray {
        // have as many colors as stack-values per entry
        val colors = IntArray(stackSize)
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, colors.size)
        return colors
    }

    private fun initBarChart(type: Int, mChart: BarChart) {
        mChart.setTouchEnabled(true)
        mChart.setPinchZoom(type == BAR_VERTICAL)
        mChart.setDrawBarShadow(false)
        mChart.setDrawGridBackground(false)
        mChart.setDrawValueAboveBar(true)
        mChart.description.isEnabled = false
        if (type == BAR_VERTICAL) {
            mChart.isDragXEnabled = true
            mChart.zoom(1f, 0f, 0f, 0f)
        }

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        if (type != BAR_VERTICAL) mChart.setMaxVisibleValueCount(60)
        val mv = CustomMarkerView(mChart.context, R.layout.marker_content)

        // set the marker to the chart
        mv.chartView = mChart
        mChart.marker = mv
        mChart.setOnChartValueSelectedListener(this)
        mChart.setTouchEnabled(type != BAR_STACKED)
        val legend: Legend = mChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(true)
        legend.yOffset = 0f
        legend.xOffset = 10f
        legend.yEntrySpace = 0f
        legend.textSize = 8f
        val xAxis: XAxis = mChart.xAxis
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        if (type != BAR_HORIZONTAL) {
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
        }
        val leftAxis: YAxis = mChart.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        mChart.axisRight.isEnabled = false
        if (type == BAR_VERTICAL) {
            mChart.invalidate()
            mChart.setMaxVisibleValueCount(3)
        }
    }
}