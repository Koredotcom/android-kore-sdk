package com.kore.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerCoverFlow : RecyclerView {
    private var isScrollable = true
    private var mDownX = 0f
    private var mManagerBuilder: CoverFlowLayoutManger.Builder? = null

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        init()
    }

    fun setScrollable(isScrollable: Boolean) {
        this.isScrollable = isScrollable
    }

    private fun init() {
        createManageBuilder()
        layoutManager = mManagerBuilder!!.build()
        isChildrenDrawingOrderEnabled = true
        overScrollMode = OVER_SCROLL_NEVER
    }

    private fun createManageBuilder() {
        if (mManagerBuilder == null) {
            mManagerBuilder = CoverFlowLayoutManger.Builder()
        }
    }

    fun setFlatFlow(isFlat: Boolean) {
        createManageBuilder()
        mManagerBuilder!!.setFlat(isFlat)
        layoutManager = mManagerBuilder!!.build()
    }

    fun setGreyItem(greyItem: Boolean) {
        createManageBuilder()
        mManagerBuilder!!.setGreyItem(greyItem)
        layoutManager = mManagerBuilder!!.build()
    }

    fun setAlphaItem(alphaItem: Boolean) {
        createManageBuilder()
        mManagerBuilder!!.setAlphaItem(alphaItem)
        layoutManager = mManagerBuilder!!.build()
    }

    fun setLoop() {
        createManageBuilder()
        mManagerBuilder!!.loop()
        layoutManager = mManagerBuilder!!.build()
    }

    fun set3DItem(d3: Boolean) {
        createManageBuilder()
        mManagerBuilder!!.set3DItem(d3)
        layoutManager = mManagerBuilder!!.build()
    }

    fun setIntervalRatio(intervalRatio: Float) {
        createManageBuilder()
        mManagerBuilder!!.setIntervalRatio(intervalRatio)
        layoutManager = mManagerBuilder!!.build()
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        require(layout is CoverFlowLayoutManger) { "The layout manager must be CoverFlowLayoutManger" }
        super.setLayoutManager(layout)
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        val center = coverFlowLayout!!.centerPosition
        val actualPos = coverFlowLayout!!.getChildActualPos(i)

        val dist = actualPos - center
        var order: Int
        order = if (dist < 0) {
            i
        } else {
            childCount - 1 - dist
        }
        if (order < 0) order = 0 else if (order > childCount - 1) order = childCount - 1
        return order
    }

    private val coverFlowLayout: CoverFlowLayoutManger?
        get() = layoutManager as CoverFlowLayoutManger?
    val selectedPos: Int
        get() = coverFlowLayout!!.selectedPos

    fun setOnItemSelectedListener(l: CoverFlowLayoutManger.OnSelected?) {
        coverFlowLayout!!.setOnSelectedListener(l)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return if (isScrollable) super.onTouchEvent(e) else false
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return if (isScrollable) super.onInterceptTouchEvent(e) else false
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.x
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE ->
                parent.requestDisallowInterceptTouchEvent(
                    (!(ev.x > mDownX) || coverFlowLayout!!.centerPosition != 0) &&
                            (!(ev.x < mDownX) || coverFlowLayout!!.centerPosition != coverFlowLayout!!.itemCount - 1)
                )
        }
        return super.dispatchTouchEvent(ev)
    }
}
