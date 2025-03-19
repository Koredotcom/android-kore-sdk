package com.kore.widgets.ui.fragments.widgetmenu

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
import com.kore.widgets.R
import com.kore.widgets.databinding.FragmentWidgetMenusBinding
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.model.ButtonModel
import com.kore.widgets.row.WidgetSimpleListAdapter
import com.kore.widgets.ui.fragments.widgetmenu.row.WidgetMenuItemRow

class WidgetMenuFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentWidgetMenusBinding? = null
    internal val binding: FragmentWidgetMenusBinding
        get() = requireNotNull(_binding)
    private var actionEvent: (event: BaseActionEvent) -> Unit = {}
    private var menus: List<ButtonModel> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_widget_menus, null, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.menus.layoutManager = LinearLayoutManager(requireContext())
        binding.menus.adapter = WidgetSimpleListAdapter(WidgetMenuRowType.entries)
        (binding.menus.adapter as WidgetSimpleListAdapter).submitList(menus.map { WidgetMenuItemRow(it, this::menuActionEvent) })
    }

    fun show(menus: List<ButtonModel>, manager: FragmentManager, actionEvent: (event: BaseActionEvent) -> Unit) {
        this.menus = menus
        this.actionEvent = actionEvent
        show(manager, this::class.java.simpleName)
    }

    private fun menuActionEvent(event: BaseActionEvent) {
        dismiss()
        actionEvent(event)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}