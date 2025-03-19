package com.kore.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.COLLECTION
import com.kore.model.constants.BotResponseConstants.COLLECTION_TITLE
import com.kore.model.constants.BotResponseConstants.HEADING
import com.kore.model.constants.BotResponseConstants.KEY_ELEMENTS
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.databinding.DialogAdvancedMultiSelectBottomSheetBinding
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.advancemultiselect.AdvancedMultiSelectCategoryRowType
import com.kore.ui.row.botchat.advancemultiselect.category.AdvanceMultiSelectCategoryRow

class AdvancedMultiSelectBottomSheet : BottomSheetDialogFragment() {
    private var binding: DialogAdvancedMultiSelectBottomSheetBinding? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var payload: HashMap<String, Any>
    private var displayCount = 1
    private var selectedItems: ArrayList<Map<String, String>> = ArrayList()
    private var actionEvent: (event: UserActionEvent) -> Unit = {}
    private var msgId: String = ""
    private lateinit var adapter: SimpleListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_advanced_multi_select_bottom_sheet, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            if (payload[HEADING] != null) {
                advanceMultiSelectTitle.isVisible = true
                advanceMultiSelectTitle.text = payload[HEADING] as String
            }
            adapter = SimpleListAdapter(AdvancedMultiSelectCategoryRowType.entries)
            adapter.submitList(createRows())
            categoryList.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            categoryList.adapter = adapter

            done.isVisible = selectedItems.isNotEmpty()
            val actualCount = (payload[KEY_ELEMENTS] as List<Map<String, Any>>).size
            viewMore.isVisible = displayCount < actualCount
            viewMore.setOnClickListener {
                viewMore.isVisible = false
                val layoutParams = categoryList.layoutParams as RelativeLayout.LayoutParams
                layoutParams.addRule(RelativeLayout.ABOVE, done.id)
                categoryList.layoutParams = layoutParams
                displayCount = actualCount
                adapter.submitList(createRows())
            }
            done.setOnClickListener {
                if (selectedItems.isEmpty()) return@setOnClickListener
                val stringBuilder = StringBuilder()
                stringBuilder.append("Here are the selected items : ")
                selectedItems.mapIndexed { index, item ->
                    stringBuilder.append(item[BotResponseConstants.VALUE])
                    if (index < selectedItems.size - 1) stringBuilder.append(" ")
                }
                actionEvent(BotChatEvent.SendMessage(stringBuilder.toString(), stringBuilder.toString()))
                bottomSheetDialog?.dismiss()
            }

            val sharedPrefs = PreferenceRepositoryImpl()
            val txtColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF")
            val bgColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5")
            done.setBackgroundColor(bgColor.toColorInt())
            done.setTextColor(txtColor.toColorInt())
            done.setRoundedCorner(6.dpToPx(root.context).toFloat())

            ivClose.setOnClickListener { bottomSheetDialog?.dismiss() }
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
        msgId: String,
        payload: HashMap<String, Any>,
        actionEvent: (UserActionEvent) -> Unit,
        manager: FragmentManager
    ) {
        this.msgId = msgId
        this.payload = payload
        this.actionEvent = actionEvent
        show(manager, AdvancedMultiSelectBottomSheet::class.java.name)
    }

    private fun onSaveState(messageId: String, value: Any?, key: String) {
        selectedItems = (value ?: emptyArray<ArrayList<Map<String, String>>>()) as ArrayList<Map<String, String>>
        adapter.submitList(createRows())
        binding?.done?.isVisible = selectedItems.isNotEmpty()
    }

    private fun createRows(): List<SimpleListRow> {
        val actualCategories = payload[KEY_ELEMENTS] as List<Map<String, Any>>
        val categories: List<Map<String, Any>> = if (displayCount < actualCategories.size) {
            actualCategories.subList(0, displayCount)
        } else {
            actualCategories
        }
        return categories.map { item ->
            AdvanceMultiSelectCategoryRow(
                msgId,
                item[COLLECTION_TITLE] as String,
                item[COLLECTION] as ArrayList<Map<String, String>>,
                true,
                selectedItems,
                this::onSaveState,
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}