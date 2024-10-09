package com.kore.ui.row.formatters

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

open class BarChartDataFormatter : ValueFormatter(), IAxisValueFormatter {
    var SUFFIX = arrayOf("", "k", "m", "b", "t")
    lateinit var mFormat: DecimalFormat
    private var mText: String? = null

    @Deprecated("Deprecated in Java", ReplaceWith("this.makePretty(value.toDouble()) + this.mText"))
    override fun getFormattedValue(
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String {
        return this.makePretty(value.toDouble()) + this.mText
    }

    @Deprecated("Deprecated in Java", ReplaceWith("\"HI\""))
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return "HI"
    }

    fun setAppendix(appendix: String) {
        this.mText = appendix
    }

    fun setSuffix(suff: Array<String>) {
        SUFFIX = suff
    }

    open fun makePretty(number: Double): String {
        var r: String = this.mFormat.format(number)
        val numericValue1 = Character.getNumericValue(r[r.length - 1])
        val numericValue2 = Character.getNumericValue(r[r.length - 2])
        val combined = Integer.valueOf(numericValue2.toString() + "" + numericValue1)
        r = r.replace("E[0-9][0-9]".toRegex(), SUFFIX[combined / 3])
        while (r.length > 5 || r.matches("[0-9]+\\.[a-z]".toRegex())) {
            r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
        }
        return r
    }

    fun getDecimalDigits(): Int {
        return 0
    }
}