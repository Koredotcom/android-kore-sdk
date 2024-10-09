package com.kore.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kore.common.extensions.dpToPx
import com.kore.common.row.SimpleListAdapter
import com.kore.common.row.SimpleListRow
import com.kore.ui.R
import com.kore.ui.bottomsheet.row.item.ViewMoreListItemRow
import com.kore.ui.databinding.DialogBottomSheetViewMoreBinding
import com.kore.ui.row.botchat.listview.ListViewTemplateItemRowType

class ViewMoreListBottomSheet : BottomSheetDialogFragment() {
    private var binding: DialogBottomSheetViewMoreBinding? = null
    private var isShowHeader = false
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var items: List<Map<String, Any>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_sheet_view_more, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rvViewMore.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            rvViewMore.adapter = SimpleListAdapter(ListViewTemplateItemRowType.values().asList()).apply {
                submitList(createRows())
            }

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

    fun showData(isShowHeader: Boolean, items: List<Map<String, Any>>, manager: FragmentManager) {
        this.items = items
        this.isShowHeader = isShowHeader
        show(manager, ViewMoreListBottomSheet::class.java.name)
    }

    private fun createRows(): List<SimpleListRow> {
        return items.map { element -> ViewMoreListItemRow(element) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}