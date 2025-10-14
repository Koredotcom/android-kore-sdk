package com.kore.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kore.common.event.UserActionEvent
import com.kore.model.BaseBotMessage
import com.kore.ui.R
import com.kore.ui.adapters.BotChatAdapter
import com.kore.ui.row.botchat.BotChatRowType

class TemplateBottomSheetFragment : BottomSheetDialogFragment() {
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var chatAdapter: BotChatAdapter

    private var onActionEvent: (event: UserActionEvent) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.template_bottom_sheet, container, false)
        val llCloseBottomSheet = view.findViewById<LinearLayout>(R.id.llCloseBottomSheet)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setLayoutManager(
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        )
        recyclerView.isVisible = true
        chatAdapter.setActionEvent(onActionEvent)
        recyclerView.setAdapter(chatAdapter)

        llCloseBottomSheet.setOnClickListener { v: View? ->
            if (bottomSheetDialog != null) bottomSheetDialog!!.dismiss()
        }
        return view
    }

    fun show(botResponse: BaseBotMessage, manager: FragmentManager) {
        show(manager, this.javaClass.name)
        val messages = ArrayList<BaseBotMessage>()
        messages.add(botResponse)
        Handler(Looper.getMainLooper()).postDelayed({
            chatAdapter.submitList(
                chatAdapter.addAndCreateRows(messages, isHistory = false, isReconnection = false)
            )
        }, 200)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        chatAdapter = BotChatAdapter(requireContext(), BotChatRowType.getAllRowTypes())
        chatAdapter.setBottomSheetDialog(bottomSheetDialog)
        bottomSheetDialog?.setOnShowListener(OnShowListener { dialogInterface: DialogInterface? ->
            val d = dialogInterface as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout?>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            if (bottomSheet == null) return@OnShowListener
            bottomSheet.layoutParams.height = requireContext().resources.displayMetrics.heightPixels
            val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet)
            bottomSheetBehavior.peekHeight = (requireContext().resources.displayMetrics.heightPixels * 0.50).toInt()
            bottomSheetBehavior.maxHeight = (requireContext().resources.displayMetrics.heightPixels * 0.95).toInt()
        })

        return bottomSheetDialog!!
    }

    fun setOnActionEvent(onActionEvent: (event: UserActionEvent) -> Unit) {
        this.onActionEvent = onActionEvent
    }
}
