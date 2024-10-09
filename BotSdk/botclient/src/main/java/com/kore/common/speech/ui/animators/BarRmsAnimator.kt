package com.kore.common.speech.ui.animators

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.kore.common.speech.ui.SpeechBar
import java.util.Random

class BarRmsAnimator(private val bar: SpeechBar) : BarParamsAnimator {
    private var fromHeightPart = 0f
    private var toHeightPart = 0f
    private var startTimestamp: Long = 0
    private var isPlaying = false
    private var isUpAnimation = false
    override fun start() {
        isPlaying = true
    }

    override fun stop() {
        isPlaying = false
    }

    override fun animate() {
        if (isPlaying) {
            update()
        }
    }

    fun onRmsChanged(rmsdB: Float) {
        var newHeightPart: Float
        if (rmsdB < QUIT_RMSDB_MAX) {
            newHeightPart = 0.2f
        } else if (rmsdB >= QUIT_RMSDB_MAX && rmsdB <= MEDIUM_RMSDB_MAX) {
            newHeightPart = 0.3f + Random().nextFloat()
            if (newHeightPart > 0.6f) newHeightPart = 0.6f
        } else {
            newHeightPart = 0.7f + Random().nextFloat()
            if (newHeightPart > 1f) newHeightPart = 1f
        }
        if (newHeightIsSmallerCurrent(newHeightPart)) {
            return
        }
        fromHeightPart = bar.height.toFloat() / bar.maxHeight
        toHeightPart = newHeightPart
        startTimestamp = System.currentTimeMillis()
        isUpAnimation = true
        isPlaying = true
    }

    private fun newHeightIsSmallerCurrent(newHeightPart: Float): Boolean {
        return bar.height.toFloat() / bar.maxHeight > newHeightPart
    }

    private fun update() {
        val currTimestamp = System.currentTimeMillis()
        val delta = currTimestamp - startTimestamp
        if (isUpAnimation) {
            animateUp(delta)
        } else {
            animateDown(delta)
        }
    }

    private fun animateUp(delta: Long) {
        var finished = false
        val minHeight = (fromHeightPart * bar.maxHeight).toInt()
        val toHeight = (bar.maxHeight * toHeightPart).toInt()
        val timePart = delta.toFloat() / BAR_ANIMATION_UP_DURATION
        val interpolator = AccelerateInterpolator()
        var height = minHeight + (interpolator.getInterpolation(timePart) * (toHeight - minHeight)).toInt()
        if (height < bar.height) {
            return
        }
        if (height >= toHeight) {
            height = toHeight
            finished = true
        }
        bar.height = height
        bar.update()
        if (finished) {
            isUpAnimation = false
            startTimestamp = System.currentTimeMillis()
        }
    }

    private fun animateDown(delta: Long) {
        val minHeight = bar.radius * 2
        val fromHeight = (bar.maxHeight * toHeightPart).toInt()
        val timePart = delta.toFloat() / BAR_ANIMATION_DOWN_DURATION
        val interpolator = DecelerateInterpolator()
        val height = minHeight + ((1f - interpolator.getInterpolation(timePart)) * (fromHeight - minHeight)).toInt()
        if (height > bar.height) {
            return
        }
        if (height <= minHeight) {
            finish()
            return
        }
        bar.height = height
        bar.update()
    }

    private fun finish() {
        bar.height = bar.radius * 2
        bar.update()
        isPlaying = false
    }

    companion object {
        private const val QUIT_RMSDB_MAX = 2f
        private const val MEDIUM_RMSDB_MAX = 5.5f
        private const val BAR_ANIMATION_UP_DURATION: Long = 130
        private const val BAR_ANIMATION_DOWN_DURATION: Long = 500
    }
}