package com.kore.ui.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

@SuppressLint("UnknownNullness")
class CoverFlowLayoutManger internal constructor(
    private val isFlatFlow: Boolean,
    private val itemGradualGrey: Boolean,
    private val itemGradualAlpha: Boolean,
    cstInterval: Float,
    private val isLoop: Boolean,
    private val item3D: Boolean
) : RecyclerView.LayoutManager() {
    var offsetAll = 0
    private var decoratedChildWidth = 0
    private var decoratedChildHeight = 0
    private var intervalRatio = 0.5f
    private var startX = 0
    private var startY = 0
    private val allItemFrames = SparseArray<Rect>()
    private val hasAttachedItems = SparseBooleanArray()
    private var recycler: Recycler? = null
    private var state: RecyclerView.State? = null
    private var animation: ValueAnimator? = null
    var selectedPos = 0
        private set
    private var lastSelectPosition = 0
    private var selectedListener: OnSelected? = null

    init {
        if (cstInterval >= 0) {
            intervalRatio = cstInterval
        } else {
            if (isFlatFlow) {
                intervalRatio = 1.1f
            }
        }
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (itemCount <= 0 || state.isPreLayout) {
            offsetAll = 0
            return
        }
        allItemFrames.clear()
        hasAttachedItems.clear()
        val scrap = recycler.getViewForPosition(0)
        addView(scrap)
        measureChildWithMargins(scrap, 0, 0)
        decoratedChildWidth = getDecoratedMeasuredWidth(scrap)
        decoratedChildHeight = getDecoratedMeasuredHeight(scrap)
        startX = Math.round((horizontalSpace - decoratedChildWidth) * 1.0f / 2)
        startY = Math.round((verticalSpace - decoratedChildHeight) * 1.0f / 2)
        var offset = startX.toFloat()
        var i = 0
        while (i < itemCount && i < MAX_RECT_COUNT) {
            var frame = allItemFrames[i]
            if (frame == null) {
                frame = Rect()
            }
            frame[offset.roundToInt(), startY, (offset + decoratedChildWidth).roundToInt()] = startY + decoratedChildHeight
            allItemFrames.put(i, frame)
            hasAttachedItems.put(i, false)
            offset += intervalDistance
            i++
        }
        detachAndScrapAttachedViews(recycler)
        if ((this.recycler == null || this.state == null) && selectedPos != 0) {
            offsetAll = calculateOffsetForPosition(selectedPos)
            onSelectedCallBack()
        }
        layoutItems(recycler, state, SCROLL_TO_LEFT)
        this.recycler = recycler
        this.state = state
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        if (animation != null && animation!!.isRunning) animation?.cancel()
        var travel = dx
        if (!isLoop) {
            if (dx + offsetAll < 0) {
                travel = -offsetAll
            } else if (dx + offsetAll > maxOffset) {
                travel = (maxOffset - offsetAll).toInt()
            }
        }
        offsetAll += travel
        layoutItems(recycler, state, if (dx > 0) SCROLL_TO_LEFT else SCROLL_TO_RIGHT)
        return travel
    }

    fun layoutItems(recycler: Recycler?, state: RecyclerView.State?, scrollDirection: Int) {
        if (state == null || state.isPreLayout) return
        val displayFrame = Rect(offsetAll, 0, offsetAll + horizontalSpace, verticalSpace)
        var position = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child != null) {
                position = if (child.tag != null) {
                    val tag = checkTag(child.tag)
                    tag?.pos ?: 0
                } else {
                    getPosition(child)
                }
                val rect = getFrame(position)
                if (!Rect.intersects(displayFrame, rect)) {
                    removeAndRecycleView(child, recycler!!)
                    hasAttachedItems.delete(position)
                } else {
                    layoutItem(child, rect)
                    hasAttachedItems.put(position, true)
                }
            }
        }
        if (position == 0) position = centerPosition
        var min = position - 20
        var max = position + 20
        if (!isLoop) {
            if (min < 0) min = 0
            if (max > itemCount) max = itemCount
        }
        for (i in min until max) {
            val rect = getFrame(i)
            if (Rect.intersects(displayFrame, rect) && !hasAttachedItems[i]) {
                var actualPos = i % itemCount
                if (actualPos < 0) actualPos += itemCount
                val scrap = recycler!!.getViewForPosition(actualPos)
                checkTag(scrap.tag)
                scrap.tag = TAG(i)
                measureChildWithMargins(scrap, 0, 0)
                if (scrollDirection == SCROLL_TO_RIGHT || isFlatFlow) {
                    addView(scrap, 0)
                } else {
                    addView(scrap)
                }
                layoutItem(scrap, rect)
                hasAttachedItems.put(i, true)
            }
        }
    }

    private fun layoutItem(child: View, frame: Rect) {
        layoutDecorated(child, frame.left - offsetAll, frame.top, frame.right - offsetAll, frame.bottom)
        if (!isFlatFlow) {
            child.scaleX = computeScale(frame.left - offsetAll)
            child.scaleY = computeScale(frame.left - offsetAll)
        }
        if (itemGradualAlpha) {
            child.alpha = computeAlpha(frame.left - offsetAll)
        }
        if (itemGradualGrey) {
            greyItem(child, frame)
        }
        if (item3D) {
            item3D(child, frame)
        }
    }

    /**
     * 动态获取Item的位置信息
     *
     * @param index item位置
     * @return item的Rect信息
     */
    private fun getFrame(index: Int): Rect {
        var frame = allItemFrames[index]
        if (frame == null) {
            frame = Rect()
            val offset = (startX + intervalDistance * index).toFloat() //原始位置累加（即累计间隔距离）
            frame[offset.roundToInt(), startY, (offset + decoratedChildWidth).roundToInt()] = startY + decoratedChildHeight
        }
        return frame
    }

    private fun greyItem(child: View, frame: Rect) {
        val value = computeGreyScale(frame.left - offsetAll)
        val cm = ColorMatrix(
            floatArrayOf(
                value,
                0f,
                0f,
                0f,
                120 * (1 - value),
                0f,
                value,
                0f,
                0f,
                120 * (1 - value),
                0f,
                0f,
                value,
                0f,
                120 * (1 - value),
                0f,
                0f,
                0f,
                1f,
                250 * (1 - value)
            )
        )
        val greyPaint = Paint()
        greyPaint.colorFilter = ColorMatrixColorFilter(cm)

        // Create a hardware layer with the grey paint
        child.setLayerType(View.LAYER_TYPE_HARDWARE, greyPaint)
        if (value >= 1) {
            // Remove the hardware layer
            child.setLayerType(View.LAYER_TYPE_NONE, null)
        }
    }

    private fun item3D(child: View, frame: Rect) {
        val center = (frame.left + frame.right - 2 * offsetAll) / 2f
        var value = (center - (startX + decoratedChildWidth / 2f)) / (itemCount * intervalDistance)
        value = sqrt(abs(value).toDouble()).toFloat()
        val symbol = (if (center > startX + decoratedChildWidth / 2f) -1 else 1).toFloat()
        child.rotationY = symbol * 50 * value
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> fixOffsetWhenFinishScroll()
            RecyclerView.SCROLL_STATE_DRAGGING, RecyclerView.SCROLL_STATE_SETTLING -> {}
        }
    }

    override fun scrollToPosition(position: Int) {
        if (position < 0 || position > itemCount - 1) return
        offsetAll = calculateOffsetForPosition(position)
        if (recycler == null || state == null) {
            selectedPos = position
        } else {
            layoutItems(recycler, state, if (position > selectedPos) SCROLL_TO_LEFT else SCROLL_TO_RIGHT)
            onSelectedCallBack()
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        if (isLoop) return
        val finalOffset = calculateOffsetForPosition(position)
        if (recycler == null || this.state == null) {
            selectedPos = position
        } else {
            startScroll(offsetAll, finalOffset)
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?) {
        removeAllViews()
        recycler = null
        state = null
        offsetAll = 0
        selectedPos = 0
        lastSelectPosition = 0
        hasAttachedItems.clear()
        allItemFrames.clear()
    }

    private val horizontalSpace: Int
        get() = width - paddingRight - paddingLeft
    private val verticalSpace: Int
        get() = height - paddingBottom - paddingTop
    private val maxOffset: Float
        get() = ((itemCount - 1) * intervalDistance).toFloat()

    private fun computeScale(x: Int): Float {
        var scale = 1 - Math.abs(x - startX) * 1.0f / Math.abs(startX + decoratedChildWidth / intervalRatio)
        if (scale < 0) scale = 0f
        if (scale > 1) scale = 1f
        return scale
    }

    private fun computeGreyScale(x: Int): Float {
        val itemMidPos = x + decoratedChildWidth.toFloat() / 2 //item中点x坐标
        val itemDx2Mid = Math.abs(itemMidPos - horizontalSpace / 2f) //item中点距离控件中点距离
        var value = 1 - itemDx2Mid / (horizontalSpace.toFloat() / 2)
        if (value < 0.1) value = 0.1f
        if (value > 1) value = 1f
        value = Math.pow(value.toDouble(), .8).toFloat()
        return value
    }

    /**
     * 计算Item半透值
     *
     * @param x Item的偏移量
     * @return 缩放系数
     */
    private fun computeAlpha(x: Int): Float {
        var alpha = 1 - abs(x - startX) * 1.0f / abs(startX + decoratedChildWidth / intervalRatio)
        if (alpha < 0.3f) alpha = 0.3f
        if (alpha > 1) alpha = 1.0f
        return alpha
    }

    private fun calculateOffsetForPosition(position: Int): Int {
        return (intervalDistance * position).toFloat().roundToInt()
    }

    private fun fixOffsetWhenFinishScroll() {
        if (intervalDistance != 0) {
            var scrollN = (offsetAll * 1.0f / intervalDistance).toInt()
            val moreDx = (offsetAll % intervalDistance).toFloat()
            if (abs(moreDx) > intervalDistance * 0.5) {
                if (moreDx > 0) scrollN++ else scrollN--
            }
            val finalOffset = scrollN * intervalDistance
            startScroll(offsetAll, finalOffset)
            selectedPos = Math.abs((finalOffset * 1.0f / intervalDistance).roundToInt()) % itemCount
        }
    }

    private fun startScroll(from: Int, to: Int) {
        if (animation != null && animation!!.isRunning) {
            animation?.cancel()
        }
        val direction = if (from < to) SCROLL_TO_LEFT else SCROLL_TO_RIGHT
        animation = ValueAnimator.ofFloat(from.toFloat(), to.toFloat())
        animation?.duration = 500
        animation?.interpolator = DecelerateInterpolator()
        animation?.addUpdateListener { animation: ValueAnimator ->
            offsetAll = (animation.animatedValue as Float).roundToInt()
            layoutItems(recycler, state, direction)
        }
        animation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                onSelectedCallBack()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animation?.start()
    }

    /**
     * 获取Item间隔
     */
    private val intervalDistance: Int
        get() = (decoratedChildWidth * intervalRatio).roundToInt()

    /**
     * 计算当前选中位置，并回调
     */
    fun onSelectedCallBack() {
        selectedPos = (offsetAll.toFloat() / intervalDistance).roundToInt()
        selectedPos = abs(selectedPos % itemCount)
        if (selectedPos != lastSelectPosition) selectedListener?.onItemSelected(selectedPos)
        lastSelectPosition = selectedPos
    }

    private fun checkTag(tag: Any?): TAG? {
        return if (tag != null) {
            if (tag is TAG) {
                tag
            } else {
                throw IllegalArgumentException("You should not use View#setTag(Object tag), use View#setTag(int key, Object tag) instead!")
            }
        } else {
            null
        }
    }

    val firstVisiblePosition: Int
        get() {
            val displayFrame = Rect(offsetAll, 0, offsetAll + horizontalSpace, verticalSpace)
            val cur = centerPosition
            var i = cur - 1
            while (true) {
                val rect = getFrame(i)
                if (rect.left <= displayFrame.left) {
                    return Math.abs(i) % itemCount
                }
                i--
            }
        }
    val lastVisiblePosition: Int
        get() {
            val displayFrame = Rect(offsetAll, 0, offsetAll + horizontalSpace, verticalSpace)
            val cur = centerPosition
            var i = cur + 1
            while (true) {
                val rect = getFrame(i)
                if (rect.right >= displayFrame.right) {
                    return abs(i) % itemCount
                }
                i++
            }
        }

    fun getChildActualPos(index: Int): Int {
        val child = getChildAt(index)
        return if (child != null) {
            if (child.tag != null) {
                val tag = checkTag(child.tag)
                tag!!.pos
            } else {
                getPosition(child)
            }
        } else index
    }

    val maxVisibleCount: Int
        get() {
            val oneSide = (horizontalSpace - startX) / intervalDistance
            return oneSide * 2 + 1
        }
    val centerPosition: Int
        get() {
            var pos = offsetAll / intervalDistance
            val more = offsetAll % intervalDistance
            if (abs(more) >= intervalDistance * 0.5f) {
                if (more >= 0) pos++ else pos--
            }
            return pos
        }

    fun setOnSelectedListener(l: OnSelected?) {
        selectedListener = l
    }

    interface OnSelected {
        fun onItemSelected(position: Int)
    }

    private class TAG internal constructor(var pos: Int)
    internal class Builder {
        var isFlat = false
        var isGreyItem = false
        var isAlphaItem = false
        var cstIntervalRatio = -1f
        var isLoop = false
        var is3DItem = false
        fun setFlat(flat: Boolean): Builder {
            isFlat = flat
            return this
        }

        fun setGreyItem(greyItem: Boolean): Builder {
            isGreyItem = greyItem
            return this
        }

        fun setAlphaItem(alphaItem: Boolean): Builder {
            isAlphaItem = alphaItem
            return this
        }

        fun setIntervalRatio(ratio: Float): Builder {
            cstIntervalRatio = ratio
            return this
        }

        fun loop(): Builder {
            isLoop = true
            return this
        }

        fun set3DItem(d3: Boolean): Builder {
            is3DItem = d3
            return this
        }

        fun build(): CoverFlowLayoutManger {
            return CoverFlowLayoutManger(isFlat, isGreyItem, isAlphaItem, cstIntervalRatio, isLoop, is3DItem)
        }
    }

    companion object {
        private const val SCROLL_TO_RIGHT = 1
        private const val SCROLL_TO_LEFT = 2
        private const val MAX_RECT_COUNT = 100
    }
}