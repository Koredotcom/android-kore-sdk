package com.kore.ui.bottomsheet.row.pinfielditem

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kore.ui.bottomsheet.row.PinFieldItemRowType
import com.kore.ui.databinding.RowPinFieldItemBinding
import com.kore.ui.row.SimpleListRow

class PinFieldItemRow(
    private val item: Int,
    private val width: Int
) : SimpleListRow() {
    override val type: SimpleListRowType = PinFieldItemRowType.Item

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is PinFieldItemRow) return false
        return otherRow.item == item
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        return otherRow is PinFieldItemRow
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowPinFieldItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            binding.root.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
            val param: ViewGroup.LayoutParams = root.layoutParams
            param.width = width
            root.layoutParams = param
        }
    }

    companion object {
        fun createRows(pinLength: Int, parentWidth: Int, itemSpace: Int): List<SimpleListRow> {
            var rows = emptyList<SimpleListRow>()
            val itemWidth = parentWidth / pinLength - itemSpace
            for (i in 1..pinLength) {
                rows = rows + PinFieldItemRow(i, itemWidth)
            }
            return rows
        }
    }
}