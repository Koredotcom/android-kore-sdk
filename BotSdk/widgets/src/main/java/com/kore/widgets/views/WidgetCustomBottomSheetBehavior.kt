package com.kore.widgets.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.RestrictTo
import androidx.annotation.VisibleForTesting
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import androidx.customview.view.AbsSavedState
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager.widget.ViewPager
import com.google.android.material.R
import com.google.android.material.R.dimen
import com.kore.widgets.extensions.getCurrentView
import java.lang.ref.WeakReference
import kotlin.math.abs

open class WidgetCustomBottomSheetBehavior<V : View> : CoordinatorLayout.Behavior<V> {
    private val dragCallback: ViewDragHelper.Callback
    var fitToContentsOffset = 0
    var halfExpandedOffset = 0
    var collapsedOffset = 0
    var isHide = false
    var currentState = 4
    var viewDragHelper: ViewDragHelper? = null
    var parentHeight = 0
    var viewRef: WeakReference<V>? = null
    var nestedScrollingChildRef: WeakReference<View>? = null
    var activePointerId = 0
    var touchingScrollingChild = false
    private var fitToContents = true
    private var maximumVelocity = 0f
    private var peekHeight = 0
    private var peekHeightAuto = false

    @get:VisibleForTesting
    var peekHeightMin = 0
    private var lastPeekHeight = 0
    var skipCollapsed = false
    private var ignoreEvents = false
    private var lastNestedScrollDy = 0
    private var nestedScrolled = false
    private var callback: BottomSheetCallback? = null
    private var velocityTracker: VelocityTracker? = null
    private var initialY = 0
    private var importantForAccessibilityMap: MutableMap<View?, Int?>? = null
    private var allowDragging = true
    private var mLocked = false

    constructor() {
        dragCallback = NamelessClass1()
    }

    fun setAllowDragging(allowDragging: Boolean) {
        this.allowDragging = allowDragging
    }

    fun setLocked(locked: Boolean) {
        mLocked = locked
    }

    @SuppressLint("PrivateResource")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        dragCallback = NamelessClass1()
        val a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetBehavior_Layout)
        val value = a.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight)
        if (value != null && value.data == -1) {
            setPeekHeight(value.data)
        } else {
            setPeekHeight(a.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1))
        }
        isHide = a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false)
        if (fitToContents != a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true)) {
            fitToContents = a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true)
            if (viewRef != null) {
                calculateCollapsedOffset()
            }
            setStateInternal(if (fitToContents && currentState == 6) 3 else currentState)
        }
        skipCollapsed = a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false)
        a.recycle()
        val configuration = ViewConfiguration.get(context)
        maximumVelocity = configuration.scaledMaximumFlingVelocity.toFloat()
    }

    override fun onSaveInstanceState(parent: CoordinatorLayout, child: V): Parcelable? {
        return SavedState(super.onSaveInstanceState(parent, child), currentState)
    }

    override fun onRestoreInstanceState(parent: CoordinatorLayout, child: V, state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(parent, child, ss.superState!!)
        if (ss.state != 1 && ss.state != 2) {
            this.currentState = ss.state
        } else {
            this.currentState = 4
        }
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) {
            child.fitsSystemWindows = true
        }
        val savedTop = child.top
        parent.onLayoutChild(child, layoutDirection)
        parentHeight = parent.height
        if (peekHeightAuto) {
            if (peekHeightMin == 0) {
                peekHeightMin = parent.resources.getDimensionPixelSize(dimen.design_bottom_sheet_peek_height_min)
            }
            lastPeekHeight = peekHeightMin.coerceAtLeast(parentHeight - parent.width * 9 / 16)
        } else {
            lastPeekHeight = peekHeight
        }
        fitToContentsOffset = 0.coerceAtLeast(parentHeight - child.height)
        halfExpandedOffset = parentHeight / 2
        calculateCollapsedOffset()
        if (currentState == 3) {
            ViewCompat.offsetTopAndBottom(child, expandedOffset)
        } else if (currentState == 6) {
            ViewCompat.offsetTopAndBottom(child, halfExpandedOffset)
        } else if (isHide && currentState == 5) {
            ViewCompat.offsetTopAndBottom(child, parentHeight)
        } else if (currentState == 4) {
            ViewCompat.offsetTopAndBottom(child, collapsedOffset)
        } else if (currentState == 1 || currentState == 2) {
            ViewCompat.offsetTopAndBottom(child, savedTop - child.top)
        }
        if (viewDragHelper == null) {
            viewDragHelper = ViewDragHelper.create(parent, dragCallback)
        }
        viewRef = WeakReference<V>(child)
        nestedScrollingChildRef = WeakReference<View>(findScrollingChild(child))
        return true
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        if (mLocked) {
            return false
        }
        return if (!child.isShown) {
            ignoreEvents = true
            false
        } else {
            val action = event.actionMasked
            if (action == 0) {
                reset()
            }
            if (velocityTracker == null) {
                velocityTracker = VelocityTracker.obtain()
            }
            velocityTracker!!.addMovement(event)
            when (action) {
                0 -> {
                    val initialX = event.x.toInt()
                    initialY = event.y.toInt()
                    val scroll = if (nestedScrollingChildRef != null) nestedScrollingChildRef!!.get() else null
                    if (scroll != null && parent.isPointInChildBounds(scroll, initialX, initialY)) {
                        activePointerId = event.getPointerId(event.actionIndex)
                        touchingScrollingChild = true
                    }
                    ignoreEvents = activePointerId == -1 && !parent.isPointInChildBounds(child, initialX, initialY)
                }

                1, 3 -> {
                    touchingScrollingChild = false
                    activePointerId = -1
                    if (ignoreEvents) {
                        ignoreEvents = false
                        return false
                    }
                }

                2 -> {}
            }
            if (!ignoreEvents && viewDragHelper != null && viewDragHelper!!.shouldInterceptTouchEvent(event)) {
                true
            } else {
                val scroll = if (nestedScrollingChildRef != null) nestedScrollingChildRef!!.get() else null
                action == 2 && scroll != null && !ignoreEvents && currentState != 1 && !parent.isPointInChildBounds(
                    scroll,
                    event.x.toInt(),
                    event.y.toInt()
                ) && viewDragHelper != null && abs(initialY.toFloat() - event.y) > viewDragHelper!!.touchSlop.toFloat()
            }
        }
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        if (mLocked) {
            return false
        }
        return if (!child.isShown) {
            false
        } else {
            val action = event.actionMasked
            if (currentState == 1 && action == 0) {
                true
            } else {
                if (viewDragHelper != null) {
                    viewDragHelper!!.processTouchEvent(event)
                }
                if (action == 0) {
                    reset()
                }
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain()
                }
                velocityTracker!!.addMovement(event)
                if (action == 2 && !ignoreEvents && abs(initialY.toFloat() - event.y) > viewDragHelper!!.touchSlop.toFloat()) {
                    viewDragHelper!!.captureChildView(child, event.getPointerId(event.actionIndex))
                }
                !ignoreEvents
            }
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        lastNestedScrollDy = 0
        nestedScrolled = false
        return axes and 2 != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (type != 1) {
            val scrollingChild = nestedScrollingChildRef!!.get()
            if (target === scrollingChild) {
                val currentTop = child.top
                val newTop = currentTop - dy
                if (dy > 0) {
                    if (newTop < expandedOffset) {
                        consumed[1] = currentTop - expandedOffset
                        ViewCompat.offsetTopAndBottom(child, -consumed[1])
                        setStateInternal(3)
                    } else {
                        consumed[1] = dy
                        ViewCompat.offsetTopAndBottom(child, -dy)
                        setStateInternal(1)
                    }
                } else if (dy < 0 && !target.canScrollVertically(-1)) {
                    if (newTop > collapsedOffset && !isHide) {
                        consumed[1] = currentTop - collapsedOffset
                        ViewCompat.offsetTopAndBottom(child, -consumed[1])
                        setStateInternal(4)
                    } else {
                        consumed[1] = dy
                        ViewCompat.offsetTopAndBottom(child, -dy)
                        setStateInternal(1)
                    }
                }
                dispatchOnSlide(child.top)
                lastNestedScrollDy = dy
                nestedScrolled = true
            }
        }
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, type: Int) {
        if (child.top == expandedOffset) {
            setStateInternal(3)
        } else if (target === nestedScrollingChildRef!!.get() && nestedScrolled) {
            val top: Int
            val targetState: Byte
            if (lastNestedScrollDy > 0) {
                top = expandedOffset
                targetState = 3
            } else if (isHide && shouldHide(child, yVelocity)) {
                top = parentHeight
                targetState = 5
            } else if (lastNestedScrollDy == 0) {
                val currentTop = child.top
                if (fitToContents) {
                    if (abs(currentTop - fitToContentsOffset) < abs(currentTop - collapsedOffset)) {
                        top = fitToContentsOffset
                        targetState = 3
                    } else {
                        top = collapsedOffset
                        targetState = 4
                    }
                } else if (currentTop < halfExpandedOffset) {
                    if (currentTop < abs(currentTop - collapsedOffset)) {
                        top = 0
                        targetState = 3
                    } else {
                        top = halfExpandedOffset
                        targetState = 6
                    }
                } else if (abs(currentTop - halfExpandedOffset) < abs(currentTop - collapsedOffset)) {
                    top = halfExpandedOffset
                    targetState = 6
                } else {
                    top = collapsedOffset
                    targetState = 4
                }
            } else {
                top = collapsedOffset
                targetState = 4
            }
            if (viewDragHelper!!.smoothSlideViewTo(child, child.left, top)) {
                setStateInternal(2)
                ViewCompat.postOnAnimation(child, SettleRunnable(child, targetState.toInt()))
            } else {
                setStateInternal(targetState.toInt())
            }
            nestedScrolled = false
        }
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return target === nestedScrollingChildRef!!.get() && (currentState != 3 || super.onNestedPreFling(
            coordinatorLayout,
            child,
            target,
            velocityX,
            velocityY
        ))
    }

    fun isFitToContents(): Boolean {
        return fitToContents
    }

    fun setFitToContents(fitToContents: Boolean) {}
    fun getPeekHeight(): Int {
        return if (peekHeightAuto) -1 else peekHeight
    }

    fun setPeekHeight(peekHeight: Int) {
        var layout = false
        if (peekHeight == -1) {
            if (!peekHeightAuto) {
                peekHeightAuto = true
                layout = true
            }
        } else if (peekHeightAuto || this.peekHeight != peekHeight) {
            peekHeightAuto = false
            this.peekHeight = 0.coerceAtLeast(peekHeight)
            collapsedOffset = parentHeight - peekHeight
            layout = true
        }
        if (layout && currentState == 4 && viewRef != null) {
            val view = viewRef!!.get()
            view?.requestLayout()
        }
    }

    fun setBottomSheetCallback(callback: BottomSheetCallback?) {
        this.callback = callback
    }

    fun getState(): Int {
        return currentState
    }

    fun setState(state: Int) {
        if (state != this.currentState) {
            if (viewRef == null) {
                if (state == 4 || state == 3 || state == 6 || isHide && state == 5) {
                    this.currentState = state
                }
            } else {
                val child = viewRef!!.get()
                if (child != null) {
                    val parent = child.parent
                    if (parent != null && parent.isLayoutRequested && ViewCompat.isAttachedToWindow(child)) {
                        child.post(Runnable { startSettlingAnimation(child, state) })
                    } else {
                        startSettlingAnimation(child, state)
                    }
                }
            }
        }
    }

    fun setStateInternal(state: Int) {
        if (this.currentState != state) {
            this.currentState = state
            if (state != 6 && state != 3) {
                if (state == 5 || state == 4) {
                    updateImportantForAccessibility(false)
                }
            } else {
                updateImportantForAccessibility(true)
            }
            val bottomSheet: View? = viewRef!!.get()
            if (bottomSheet != null && callback != null) {
                callback!!.onStateChanged(bottomSheet, state)
            }
        }
    }

    private fun calculateCollapsedOffset() {
        if (fitToContents) {
            collapsedOffset = (parentHeight - lastPeekHeight).coerceAtLeast(fitToContentsOffset)
        } else {
            collapsedOffset = parentHeight - lastPeekHeight
        }
    }

    private fun reset() {
        activePointerId = -1
        if (velocityTracker != null) {
            velocityTracker!!.recycle()
            velocityTracker = null
        }
    }

    fun shouldHide(child: View, level: Float): Boolean {
        return if (skipCollapsed) {
            true
        } else if (child.top < collapsedOffset) {
            false
        } else {
            val newTop = child.top.toFloat() + level * 0.1f
            abs(newTop - collapsedOffset.toFloat()) / peekHeight.toFloat() > 0.5f
        }
    }

    @VisibleForTesting
    fun findScrollingChild(view: View?): View? {
        if (view is ViewPager) {
            val currentViewPagerChild = view.getCurrentView() ?: return null
            return findScrollingChild(currentViewPagerChild)
        }
        return null
    }

    private val yVelocity: Float
        get() = if (velocityTracker == null) {
            0.0f
        } else {
            velocityTracker!!.computeCurrentVelocity(1000, maximumVelocity)
            velocityTracker!!.getYVelocity(activePointerId)
        }
    internal val expandedOffset: Int
        get() = if (fitToContents) fitToContentsOffset else 0

    fun startSettlingAnimation(child: View, state: Int) {
        var targetState = state
        var top: Int
        if (targetState == 4) {
            top = collapsedOffset
        } else if (targetState == 6) {
            top = halfExpandedOffset
            if (fitToContents && top <= fitToContentsOffset) {
                targetState = 3
                top = fitToContentsOffset
            }
        } else if (targetState == 3) {
            top = expandedOffset
        } else {
            require(!(!isHide || targetState != 5)) { "Illegal state argument: $targetState" }
            top = parentHeight
        }
        if (viewDragHelper!!.smoothSlideViewTo(child, child.left, top)) {
            setStateInternal(2)
            ViewCompat.postOnAnimation(child, SettleRunnable(child, targetState))
        } else {
            setStateInternal(targetState)
        }
    }

    fun dispatchOnSlide(top: Int) {
        val bottomSheet: View? = viewRef!!.get()
        if (bottomSheet != null && callback != null) {
            if (top > collapsedOffset) {
                callback!!.onSlide(bottomSheet, (collapsedOffset - top).toFloat() / (parentHeight - collapsedOffset).toFloat())
            } else {
                callback!!.onSlide(bottomSheet, (collapsedOffset - top).toFloat() / (collapsedOffset - expandedOffset).toFloat())
            }
        }
    }

    private fun updateImportantForAccessibility(expanded: Boolean) {
        if (viewRef != null) {
            val viewParent = viewRef!!.get()!!.parent
            if (viewParent is CoordinatorLayout) {
                val parent = viewParent
                val childCount = parent.childCount
                if (expanded) {
                    if (importantForAccessibilityMap != null) {
                        return
                    }
                    importantForAccessibilityMap = HashMap(childCount)
                }
                for (i in 0 until childCount) {
                    val child = parent.getChildAt(i)
                    if (child !== viewRef!!.get()) {
                        if (!expanded) {
                            if (importantForAccessibilityMap != null && importantForAccessibilityMap!!.containsKey(child)) {
                                ViewCompat.setImportantForAccessibility(child, importantForAccessibilityMap!![child]!!)
                            }
                        } else {
                            importantForAccessibilityMap!![child] = child.importantForAccessibility
                            ViewCompat.setImportantForAccessibility(child, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS)
                        }
                    }
                }
                if (!expanded) {
                    importantForAccessibilityMap = null
                }
            }
        }
    }



    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @RestrictTo(*[RestrictTo.Scope.LIBRARY_GROUP])
    annotation class State
    class SavedState : AbsSavedState {
        val state: Int

        @JvmOverloads
        constructor(source: Parcel, loader: ClassLoader? = null) : super(source, loader) {
            state = source.readInt()
        }

        constructor(superState: Parcelable?, state: Int) : super(superState!!) {
            this.state = state
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(state)
        }

        override fun describeContents(): Int {
            return 0
        }
    }

    abstract class BottomSheetCallback {
        abstract fun onStateChanged(var1: View, var2: Int)
        abstract fun onSlide(var1: View, var2: Float)
    }

    private inner class SettleRunnable internal constructor(private val view: View, private val targetState: Int) : Runnable {
        override fun run() {
            if (viewDragHelper != null && viewDragHelper!!.continueSettling(true)) {
                ViewCompat.postOnAnimation(view, this)
            } else {
                setStateInternal(targetState)
            }
        }
    }

    private inner class NamelessClass1 internal constructor() : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return if (currentState == 1) {
                false
            } else if (touchingScrollingChild) {
                false
            } else {
                if (currentState == 3 && activePointerId == pointerId) {
                    val scroll = nestedScrollingChildRef!!.get()
                    if (scroll != null && scroll.canScrollVertically(-1)) {
                        return false
                    }
                }
                viewRef != null && viewRef!!.get() === child
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            dispatchOnSlide(top)
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state == 1) {
                setStateInternal(1)
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val top: Int
            val targetState: Byte
            val currentTop: Int
            if (yvel < 0.0f) {
                if (fitToContents) {
                    top = fitToContentsOffset
                    targetState = 3
                } else {
                    currentTop = releasedChild.top
                    if (currentTop > halfExpandedOffset) {
                        top = halfExpandedOffset
                        targetState = 6
                    } else {
                        top = 0
                        targetState = 3
                    }
                }
            } else if (!isHide || !shouldHide(releasedChild, yvel) || releasedChild.top <= collapsedOffset && abs(
                    xvel
                ) >= abs(
                    yvel
                )
            ) {
                if (yvel != 0.0f && abs(xvel) <= abs(yvel)) {
                    top = collapsedOffset
                    targetState = 4
                } else {
                    currentTop = releasedChild.top
                    if (fitToContents) {
                        if (abs(currentTop - fitToContentsOffset) < abs(currentTop - collapsedOffset)) {
                            top = fitToContentsOffset
                            targetState = 3
                        } else {
                            top = collapsedOffset
                            targetState = 4
                        }
                    } else if (currentTop < halfExpandedOffset) {
                        if (currentTop < abs(currentTop - collapsedOffset)) {
                            top = 0
                            targetState = 3
                        } else {
                            top = halfExpandedOffset
                            targetState = 6
                        }
                    } else if (abs(currentTop - halfExpandedOffset) < abs(currentTop - collapsedOffset)) {
                        top = halfExpandedOffset
                        targetState = 6
                    } else {
                        top = collapsedOffset
                        targetState = 4
                    }
                }
            } else {
                top = parentHeight
                targetState = 5
            }
            if (viewDragHelper!!.settleCapturedViewAt(releasedChild.left, top)) {
                setStateInternal(2)
                ViewCompat.postOnAnimation(releasedChild, this@WidgetCustomBottomSheetBehavior.SettleRunnable(releasedChild, targetState.toInt()))
            } else {
                setStateInternal(targetState.toInt())
            }
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return MathUtils.clamp(top, expandedOffset, if (isHide) parentHeight else collapsedOffset)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return child.left
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return if (isHide) parentHeight else collapsedOffset
        }
    }

    companion object {
        fun <V : View?> from(view: V): WidgetCustomBottomSheetBehavior<*> {
            val params = view!!.layoutParams
            return if (params !is CoordinatorLayout.LayoutParams) {
                throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            } else {
                val behavior = params.behavior
                if (behavior !is WidgetCustomBottomSheetBehavior<*>) {
                    throw IllegalArgumentException("The view is not associated with BottomSheetBehavior")
                } else {
                    behavior
                }
            }
        }
    }
}