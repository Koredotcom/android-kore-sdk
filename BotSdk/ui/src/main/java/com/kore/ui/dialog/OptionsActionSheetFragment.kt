package com.kore.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kore.common.event.UserActionEvent
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.ui.R
import com.kore.ui.adapters.BottomMenuOptionsAdapter
import com.kore.ui.adapters.WelcomeStarterButtonsAdapter

@SuppressLint("UnknownNullness")
class OptionsActionSheetFragment : BottomSheetDialogFragment() {
    private var models: ArrayList<BrandingQuickStartButtonActionModel>? = null
    private var isFromListMenu: Boolean = false
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var botBrandingModel: BotBrandingModel? = null
    private var actionEvent: (event: UserActionEvent) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.menus_bottom_sheet, container, false)
        val tvOptionsTitle = view.findViewById<TextView>(R.id.tvOptionsTitle)
        val tvMenuTitle = view.findViewById<TextView>(R.id.tvMenuTitle)
        val llCloseBottomSheet = view.findViewById<LinearLayout>(R.id.llCloseBottomSheet)
        val llBottomLayout = view.findViewById<LinearLayout>(R.id.llBottomLayout)

        val llTabHeader = view.findViewById<LinearLayout>(R.id.llTabHeader)
        val rvViewMore = view.findViewById<RecyclerView>(R.id.rvMoreData)
        val rvQuickData = view.findViewById<RecyclerView>(R.id.rvQuickData)

        llTabHeader.isVisible = false
        tvOptionsTitle.isVisible = true
        rvQuickData.isVisible = false
        tvMenuTitle.isVisible = false

        val sharedPreferences = requireActivity().getSharedPreferences(BotResponseConstants.THEME_NAME, Context.MODE_PRIVATE)

        if (sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponseConstants.WIDGET_BG_COLOR, "#FFFFFF")))

        val layoutManager = FlexboxLayoutManager(requireActivity())
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        rvViewMore.layoutManager = layoutManager
        rvViewMore.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        val bottomOptionsCycleAdapter = BottomMenuOptionsAdapter(models!!)
        bottomOptionsCycleAdapter.setBotListModelArrayList(bottomSheetDialog)
        bottomOptionsCycleAdapter.setActionEvent(actionEvent)
        rvViewMore.adapter = bottomOptionsCycleAdapter

//        bottomOptionsCycleAdapter.notifyItemRangeChanged(0, (models!!.size - 1))

        if (botBrandingModel != null) {
            if (!botBrandingModel?.welcomeScreen?.starterBox?.quickStartButtons?.buttons.isNullOrEmpty()) {
                if (botBrandingModel!!.welcomeScreen.starterBox.quickStartButtons.style.equals(BotResponseConstants.TEMPLATE_TYPE_LIST, ignoreCase = true)) {
                    layoutManager.flexDirection = FlexDirection.COLUMN
                    layoutManager.justifyContent = JustifyContent.FLEX_START
                    rvQuickData.layoutManager = layoutManager

                    val quickRepliesAdapter = WelcomeStarterButtonsAdapter(requireActivity(), BotResponseConstants.TEMPLATE_TYPE_LIST)
                    quickRepliesAdapter.setWelcomeStarterButtonsArrayList(botBrandingModel!!.welcomeScreen.starterBox.quickStartButtons.buttons)
                    rvQuickData.adapter = quickRepliesAdapter
                } else {
                    layoutManager.flexDirection = FlexDirection.ROW
                    layoutManager.justifyContent = JustifyContent.FLEX_START
                    rvQuickData.layoutManager = layoutManager

                    val quickRepliesAdapter = WelcomeStarterButtonsAdapter(requireActivity(), BotResponseConstants.TEMPLATE_TYPE_CAROUSEL)
                    quickRepliesAdapter.setWelcomeStarterButtonsArrayList(botBrandingModel!!.welcomeScreen.starterBox.quickStartButtons.buttons)
                    rvQuickData.adapter = quickRepliesAdapter
                }
            }
        }

        tvOptionsTitle.text = requireActivity().resources.getString(R.string.menu)

        if (sharedPreferences != null) tvOptionsTitle.setTextColor(
            Color.parseColor(sharedPreferences.getString(BotResponseConstants.WIDGET_TXT_COLOR, "#000000"))
        )

        llCloseBottomSheet.setOnClickListener { v: View? ->
            if (bottomSheetDialog != null) bottomSheetDialog!!.dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        if (bottomSheetDialog?.window != null) {
            bottomSheetDialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        }

        bottomSheetDialog?.setOnShowListener { dialogInterface: DialogInterface ->
            val d = dialogInterface as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                bottomSheet.layoutParams.height = displayMetrics.heightPixels
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.peekHeight = (models!!.size.dpToPx(bottomSheetDialog?.context!!) * 125).toInt()
                bottomSheetBehavior.isDraggable = false
            }
        }

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog!!
    }

    fun setData(taskTemplateModel: ArrayList<BrandingQuickStartButtonActionModel>?) {
        models = taskTemplateModel
    }

    fun setData(botOptionsModel: ArrayList<BrandingQuickStartButtonActionModel>?, isFromListMenu: Boolean) {
        models = botOptionsModel
        this.isFromListMenu = isFromListMenu
    }

    fun setBrandingModel(botBrandingModel: BotBrandingModel?) {
        this.botBrandingModel = botBrandingModel
    }

    fun setActionEvent(actionEvent: (event: UserActionEvent) -> Unit) {
        this.actionEvent = actionEvent
    }
}
