package com.kore.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kore.common.event.UserActionEvent
import com.kore.common.extensions.dpToPx
import com.kore.common.row.SimpleListAdapter
import com.kore.common.row.SimpleListRow
import com.kore.ui.R
import com.kore.ui.databinding.ListBottomSheetFragmentBinding
import com.kore.ui.row.botchat.listview.ListViewTemplateItemRowType
import com.kore.ui.row.botchat.listview.item.ListViewTemplateItemRow

class ShowMoreListBottomSheet : BottomSheetDialogFragment() {
    companion object {
        private const val TAB1 = "Tab1"
        private const val TAB2 = "Tab2"
    }

    private var binding: ListBottomSheetFragmentBinding? = null
    private var isShowHeader = false
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var data: Map<String, Any>
    private var isLastItem: Boolean = false
    private lateinit var actionEvent: (event: UserActionEvent) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_bottom_sheet_fragment, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rvMoreData.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            val adapter = SimpleListAdapter(ListViewTemplateItemRowType.values().asList())
            rvMoreData.adapter = adapter
            llTabHeader.isVisible = isShowHeader

            tvTab1.setOnClickListener {
                tvTab1.isSelected = true
                tvTab2.isSelected = false
                adapter.submitList(createRows(TAB1))
            }

            tvTab2.setOnClickListener {
                tvTab1.isSelected = false
                tvTab2.isSelected = true
                adapter.submitList(createRows(TAB2))
            }

            tvTab1.performClick()

            llCloseBottomSheet.setOnClickListener { bottomSheetDialog?.dismiss() }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog?.setOnShowListener { dialogInterface ->
            val d = dialogInterface as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                bottomSheet.layoutParams.height = resources.displayMetrics.heightPixels
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.peekHeight = 500.dpToPx(requireContext())
            }
        }

        return bottomSheetDialog!!
    }

    fun showData(
        isShowHeader: Boolean,
        data: Map<String, Any>,
        isLastItem: Boolean,
        manager: FragmentManager,
        actionEvent: (event: UserActionEvent) -> Unit
    ) {
        this.data = data
        this.isLastItem = isLastItem
        this.actionEvent = actionEvent
        this.isShowHeader = isShowHeader
        show(manager, ShowMoreListBottomSheet::class.java.name)
    }

    private fun createRows(tabName: String): List<SimpleListRow> {
        val elements = data[tabName] as List<Map<String, Any>>
        return elements.map { element -> ListViewTemplateItemRow(element, isLastItem, actionEvent) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}