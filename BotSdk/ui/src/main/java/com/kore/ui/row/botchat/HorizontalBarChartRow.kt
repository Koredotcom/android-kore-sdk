package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
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
import com.kore.model.constants.BotResponseConstants.DISPLAY_VALUES
import com.kore.model.constants.BotResponseConstants.X_AXIS
import com.kore.ui.databinding.HorizontalBarChartTemplateBinding
import com.kore.ui.row.formatters.BarChartDataFormatter
import com.kore.ui.utils.ColorTemplate

class HorizontalBarChartRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
) : SimpleListRow(), OnChartValueSelectedListener {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_HORIZONTAL_BARCHART_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is HorizontalBarChartRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is HorizontalBarChartRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = HorizontalBarChartTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val entries = payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>
            val barDataSets: ArrayList<IBarDataSet> = ArrayList()
            val barWidth = 0.2f
            val groupSpace = 0.08f
            val barSpace = 0.03f // x4 DataSet

            val startYear = 1
            val groupCount = 4

            root.setTouchEnabled(true)
            root.setPinchZoom(true)
            root.setDrawBarShadow(false)
            root.setDrawGridBackground(false)
            root.setDrawValueAboveBar(true)
            root.isDragXEnabled = true
            root.description.isEnabled = false
            root.zoom(1f, 0f, 0f, 0f)

//            val mv = CustomMarkerView(mContext, R.layout.marker_content)
//
//            // set the marker to the chart
//
//            // set the marker to the chart
//            mv.setChartView(root)
//            root.marker = mv
            root.setOnChartValueSelectedListener(this@HorizontalBarChartRow)
            root.setTouchEnabled(true)

            val l: Legend = root.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(true)
            l.yOffset = 0f
            l.xOffset = 10f
            l.yEntrySpace = 0f
            l.textSize = 8f

            val xAxis: XAxis = root.xAxis
            xAxis.granularity = 1f
            xAxis.setCenterAxisLabels(true)
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }

            val leftAxis: YAxis = root.axisLeft
            leftAxis.valueFormatter = LargeValueFormatter()
            leftAxis.setDrawGridLines(false)
            leftAxis.spaceTop = 35f
            leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


            root.axisRight.isEnabled = false


            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            root.invalidate()
            root.setMaxVisibleValueCount(3)

            when (entries.isNotEmpty()) {
                true -> {
                    val yVals1 = ArrayList<ArrayList<BarEntry>>(entries.size)
                    for (barChart in entries) {
                        val barEry: ArrayList<BarEntry> = ArrayList()
                        for (i in 0 until (barChart[BotResponseConstants.VALUES] as ArrayList<*>).size) {
                            val double = (barChart[BotResponseConstants.VALUES] as ArrayList<*>)[i] as Double
                            barEry.add(BarEntry((i + 1).toFloat(), double.toFloat(), (barChart[DISPLAY_VALUES] as ArrayList<*>)[i]))
                        }

                        yVals1.add(barEry)
                    }

                    for (k in entries.indices) {
                        val barDataSet = BarDataSet(yVals1[k], entries[k][BotResponseConstants.KEY_TITLE] as String)
                        barDataSet.color = ColorTemplate.MATERIAL_COLORS[k % 4]
                        barDataSets.add(barDataSet)
                    }

                    val data = BarData(barDataSets)
                    data.setValueFormatter(BarChartDataFormatter())
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.textSize = 8f
                    xAxis.setDrawGridLines(false)
                    xAxis.granularity = 0.5f // only intervals of 1 day
                    xAxis.labelCount = (payload[X_AXIS] as List<String>).size
                    xAxis.labelRotationAngle = -60f

                    val xAxisFormatter: ValueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(v: Float): String {
                            return if ((v * 2).toInt() - 2 >= 0 && (v * 2).toInt() - 2 < (payload[X_AXIS] as List<String>).size) (payload[X_AXIS] as List<String>)[(v * 2).toInt() - 2] else ""
                        }
                    }

                    xAxis.valueFormatter = xAxisFormatter

                    l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    l.orientation = Legend.LegendOrientation.HORIZONTAL
                    l.setDrawInside(false)
                    l.form = Legend.LegendForm.SQUARE
                    l.formSize = 9f
                    l.textSize = 11f
                    l.xEntrySpace = 4f

                    root.data = data
                    root.barData.barWidth = barWidth
                    // restrict the x-axis range
                    root.xAxis.axisMinimum = startYear.toFloat()
                    root.xAxis.axisMaximum = startYear + root.barData.getGroupWidth(groupSpace, barSpace) * groupCount
                    root.groupBars(startYear.toFloat(), groupSpace, barSpace)
                }

                else -> {}
            }
        }
    }

    override fun onValueSelected(p0: Entry?, p1: Highlight?) {
    }

    override fun onNothingSelected() {
    }
}