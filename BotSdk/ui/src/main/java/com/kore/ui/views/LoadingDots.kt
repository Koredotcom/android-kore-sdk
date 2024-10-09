package com.kore.ui.views

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import com.kore.ui.R

@SuppressLint("UnknownNullness")
class LoadingDots : LinearLayout {
    private var mDots: MutableList<View>? = null
    private var mAnimation: ValueAnimator? = null
    private var mIsAttachedToWindow = false
    private var mAutoPlay = false

    // Dots appearance attributes
    private var mDotsColor = 0
    private var mDotsCount = 0
    private var mDotSize = 0
    private var mDotSpace = 0

    // Animation time attributes
    private var mLoopDuration = 0
    private var mLoopStartDelay = 0

    // Animation behavior attributes
    private var mJumpDuration = 0
    private var mJumpHeight = 0

    // Cached Calculations
    private var mJumpHalfTime = 0
    private lateinit var mDotsStartTime: IntArray
    private lateinit var mDotsJumpUpEndTime: IntArray
    private lateinit var mDotsJumpDownEndTime: IntArray

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val context = context
        val resources = context.resources
        val a = context.obtainStyledAttributes(attrs, R.styleable.LoadingDots)
        mAutoPlay = a.getBoolean(R.styleable.LoadingDots_LoadingDots_auto_play, true)
        mDotsColor = a.getColor(R.styleable.LoadingDots_LoadingDots_dots_color, Color.GRAY)
        mDotsCount = a.getInt(R.styleable.LoadingDots_LoadingDots_dots_count, DEFAULT_DOTS_COUNT)
        mDotSize = a.getDimensionPixelSize(
            R.styleable.LoadingDots_LoadingDots_dots_size,
            resources.getDimensionPixelSize(R.dimen.LoadingDots_dots_size_default)
        )
        mDotSpace = a.getDimensionPixelSize(
            R.styleable.LoadingDots_LoadingDots_dots_space,
            resources.getDimensionPixelSize(R.dimen.LoadingDots_dots_space_default)
        )
        mLoopDuration =
            a.getInt(R.styleable.LoadingDots_LoadingDots_loop_duration, DEFAULT_LOOP_DURATION)
        mLoopStartDelay =
            a.getInt(R.styleable.LoadingDots_LoadingDots_loop_start_delay, DEFAULT_LOOP_START_DELAY)
        mJumpDuration =
            a.getInt(R.styleable.LoadingDots_LoadingDots_jump_duration, DEFAULT_JUMP_DURATION)
        mJumpHeight = a.getDimensionPixelSize(
            R.styleable.LoadingDots_LoadingDots_jump_height,
            resources.getDimensionPixelSize(R.dimen.LoadingDots_jump_height_default)
        )
        a.recycle()

        // Setup LinerLayout
        orientation = HORIZONTAL
        gravity = Gravity.BOTTOM
        calculateCachedValues()
        initializeDots(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // We allow the height to save space for the jump height
        setMeasuredDimension(measuredWidth, measuredHeight + mJumpHeight)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mIsAttachedToWindow = true
        createAnimationIfAutoPlay()
        if (mAnimation != null && visibility == VISIBLE) {
            mAnimation!!.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mIsAttachedToWindow = false
        if (mAnimation != null) {
            mAnimation!!.end()
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        when (visibility) {
            VISIBLE -> {
                createAnimationIfAutoPlay()
                startAnimationIfAttached()
            }

            INVISIBLE, GONE -> if (mAnimation != null) {
                mAnimation!!.end()
            }
        }
    }

    private fun createDotView(context: Context): View {
        val dot = ImageView(context)
        dot.setImageResource(R.drawable.loading_dots_dot)
        (dot.drawable as GradientDrawable).setColor(mDotsColor)
        return dot
    }

    private fun startAnimationIfAttached() {
        if (mIsAttachedToWindow && !mAnimation!!.isRunning) {
            mAnimation!!.start()
        }
    }

    private fun createAnimationIfAutoPlay() {
        if (mAutoPlay) {
            createAnimation()
        }
    }

    private fun createAnimation() {
        if (mAnimation != null) {
            // We already have an animation
            return
        }
        calculateCachedValues()
        initializeDots(context)
        mAnimation = ValueAnimator.ofInt(0, mLoopDuration)
        mAnimation?.addUpdateListener(AnimatorUpdateListener { valueAnimator: ValueAnimator ->
            val dotsCount = mDots!!.size
            val from = 0
            val animationValue = valueAnimator.animatedValue as Int
            if (animationValue < mLoopStartDelay) {
                // Do nothing
                return@AnimatorUpdateListener
            }
            for (i in 0 until dotsCount) {
                val dot = mDots!![i]
                val dotStartTime = mDotsStartTime[i]
                val animationFactor: Float = if (animationValue < dotStartTime) {
                    // No animation is needed for this dot yet
                    0f
                } else if (animationValue < mDotsJumpUpEndTime[i]) {
                    // Animate jump up
                    (animationValue - dotStartTime).toFloat() / mJumpHalfTime
                } else if (animationValue < mDotsJumpDownEndTime[i]) {
                    // Animate jump down
                    1 - (animationValue - dotStartTime - mJumpHalfTime).toFloat() / mJumpHalfTime
                } else {
                    // Dot finished animation for this loop
                    0f
                }
                val translationY = (-mJumpHeight - from) * animationFactor
                dot.translationY = translationY
            }
        })
        mAnimation?.setDuration(mLoopDuration.toLong())
        mAnimation?.repeatCount = Animation.INFINITE
    }

    private fun calculateCachedValues() {
        verifyNotRunning()

        // The offset is the time delay between dots start animation
        val startOffset = (mLoopDuration - (mJumpDuration + mLoopStartDelay)) / (mDotsCount - 1)

        // Dot jump half time ( jumpTime/2 == going up == going down)
        mJumpHalfTime = mJumpDuration / 2
        mDotsStartTime = IntArray(mDotsCount)
        mDotsJumpUpEndTime = IntArray(mDotsCount)
        mDotsJumpDownEndTime = IntArray(mDotsCount)
        for (i in 0 until mDotsCount) {
            val startTime = mLoopStartDelay + startOffset * i
            mDotsStartTime[i] = startTime
            mDotsJumpUpEndTime[i] = startTime + mJumpHalfTime
            mDotsJumpDownEndTime[i] = startTime + mJumpDuration
        }
    }

    private fun verifyNotRunning() {
        check(mAnimation == null) { "Can't change properties while animation is running!" }
    }

    private fun initializeDots(context: Context) {
        verifyNotRunning()
        removeAllViews()

        // Create the dots
        mDots = ArrayList(mDotsCount)
        val dotParams = LayoutParams(mDotSize, mDotSize)
        val spaceParams = LayoutParams(mDotSpace, mDotSize)
        for (i in 0 until mDotsCount) {
            // Add dot
            val dotView = createDotView(context)
            addView(dotView, dotParams)
            (mDots as ArrayList<View>).add(dotView)

            // Add space
            if (i < mDotsCount - 1) {
                addView(View(context), spaceParams)
            }
        }
    }

    companion object {
        const val DEFAULT_DOTS_COUNT = 3
        const val DEFAULT_LOOP_DURATION = 600
        const val DEFAULT_LOOP_START_DELAY = 100
        const val DEFAULT_JUMP_DURATION = 400
    }
}