package com.kore.widgets.ui.fragments.bottompanel

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kore.widgets.R
import com.kore.widgets.databinding.FragmentBottomPanelBinding
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.event.WidgetActionEvent
import com.kore.widgets.model.ButtonModel
import com.kore.widgets.model.Panel
import com.kore.widgets.row.WidgetSimpleListAdapter
import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.ui.fragments.bottompanel.row.BottomPanelRowType
import com.kore.widgets.ui.fragments.bottompanel.row.widget.PanelWidgetRowType
import com.kore.widgets.ui.fragments.widgetmenu.WidgetMenuFragment
import com.kore.widgets.views.WidgetCustomBottomSheetBehavior
import com.squareup.picasso.Picasso

class BottomPanelFragment : Fragment(), BottomPanelView, GestureDetector.OnGestureListener {
    private var bottomSheetBehavior: WidgetCustomBottomSheetBehavior<LinearLayout>? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var gestureScanner: GestureDetector
    private var screenHeight = 0f
    private var _binding: FragmentBottomPanelBinding? = null
    internal val binding: FragmentBottomPanelBinding
        get() = requireNotNull(_binding)
    private lateinit var panelAdapter: WidgetSimpleListAdapter
    private var actionEvent: (event: BaseActionEvent) -> Unit = {}

    private val viewModel: BottomPanelFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bottom_panel, null, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.setIsPanelOpen(false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setView(this)
        viewModel.init("")
        screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
        binding.closeBtnPanel.setOnClickListener { closePanel() }
        gestureScanner = GestureDetector(requireContext(), this)
        binding.panelDrag.setOnTouchListener { _, event -> gestureScanner.onTouchEvent(event) }
    }

    override fun init() {
        panelAdapter = WidgetSimpleListAdapter(BottomPanelRowType.values().asList())
        binding.panelRecycler.adapter = panelAdapter
        binding.recyclerViewPanel.adapter = WidgetSimpleListAdapter(PanelWidgetRowType.values().asList())
        bottomSheetBehavior = WidgetCustomBottomSheetBehavior.from(binding.persistentPanel) as WidgetCustomBottomSheetBehavior<LinearLayout>
        bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)
        bottomSheetBehavior?.setBottomSheetCallback(object : WidgetCustomBottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                viewModel.setIsPanelOpen(newState == BottomSheetBehavior.STATE_EXPANDED)
                actionEvent(WidgetActionEvent.OnPanelState(newState == BottomSheetBehavior.STATE_EXPANDED))
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior?.setLocked(true)
                    BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomSheetBehavior?.setLocked(false)
                        binding.recyclerViewPanel.adapter = null
                        binding.recyclerViewPanel.adapter = WidgetSimpleListAdapter(PanelWidgetRowType.values().asList())
                        binding.persistentPanel.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        binding.panelRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPanel.layoutManager = LinearLayoutManager(activity)
    }

    override fun onBottomPanels(rows: List<WidgetSimpleListRow>) {
        panelAdapter.submitList(rows)
        if (rows.isEmpty()) return
        binding.progressBarPanel.isVisible = false
        binding.panelRecycler.isVisible = true
    }

    override fun onBottomPanelError() {
        binding.progressBarPanel.isVisible = false
        binding.emptyView.isVisible = panelAdapter.itemCount <= 0
        binding.emptyView.text = getString(R.string.oops)
    }

    override fun onPanelItemClicked(panel: Panel) {
        if (bottomSheetBehavior != null) {
            binding.panelSubLayout.isVisible = true
            binding.persistentPanel.isVisible = true
            handler.postDelayed({ updatePanelData(panel) }, 500)
            bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }

    override fun onPanelWidgets(rows: List<WidgetSimpleListRow>) {
        if (bottomSheetBehavior?.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            (binding.recyclerViewPanel.adapter as WidgetSimpleListAdapter).submitList(rows)
        }
    }

    override fun showMenus(menus: List<ButtonModel>) {
        WidgetMenuFragment().show(menus, childFragmentManager, actionEvent)
    }

    fun closePanel() {
        bottomSheetBehavior?.setLocked(false)
        binding.persistentPanel.visibility = View.GONE
        bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)
    }

    fun setActionEvent(actionEvent: (event: BaseActionEvent) -> Unit) {
        this.actionEvent = actionEvent
        viewModel.setUserActionEvent(actionEvent)
    }

    private fun updatePanelData(panel: Panel) {
        binding.panelTitleIcon.root.setBackgroundResource(0)
        try {
            binding.panelTitleIcon.imgSkill.isVisible = true
            panel.icon?.let { icon ->
                if (icon.contains(",")) {
                    val imageData = icon.substring(icon.indexOf(",") + 1)
                    val decodedString = Base64.decode(imageData.toByteArray(), Base64.DEFAULT)
                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    binding.panelTitleIcon.imgSkill.setImageBitmap(decodedByte)
                } else {
                    Picasso.get().load(icon).into(binding.panelTitleIcon.imgSkill)
                }
            }
        } catch (e: Exception) {
            binding.panelTitleIcon.imgSkill.isVisible = false
        }
        binding.recyclerViewPanel.isVisible = true
        binding.recyclerViewPanel.minimumHeight = screenHeight.toInt()
        binding.singleItemContainer.removeAllViews()
        binding.singleItemContainer.isVisible = false
        binding.txtTitle.text = panel.name
    }


    @Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
    override fun onDown(motionEvent: MotionEvent): Boolean {
        bottomSheetBehavior?.setLocked(false)
        try {
            if (bottomSheetBehavior != null && bottomSheetBehavior?.getState() === BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior?.onInterceptTouchEvent(binding.coordinateLayout, binding.persistentPanel as LinearLayout, motionEvent)
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return false
    }

    override fun onShowPress(motionEvent: MotionEvent) {}

    override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean = false

    override fun onLongPress(motionEvent: MotionEvent) {}

    override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean = false
}