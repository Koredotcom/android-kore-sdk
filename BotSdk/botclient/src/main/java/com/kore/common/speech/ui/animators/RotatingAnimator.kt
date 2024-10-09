package com.kore.common.speech.ui.animators

import android.graphics.Point
import android.view.animation.AccelerateDecelerateInterpolator
import com.kore.common.speech.ui.SpeechBar

class RotatingAnimator(private val bars: List<SpeechBar>, private val centerX: Int, private val centerY: Int) : BarParamsAnimator {
    private var startTimestamp: Long = 0
    private var isPlaying = false
    private val startPositions: MutableList<Point>

    init {
        startPositions = ArrayList()
        for (bar in bars) {
            startPositions.add(Point(bar.x, bar.y))
        }
    }

    override fun start() {
        isPlaying = true
        startTimestamp = System.currentTimeMillis()
    }

    override fun stop() {
        isPlaying = false
    }

    override fun animate() {
        if (!isPlaying) return
        val currTimestamp = System.currentTimeMillis()
        if (currTimestamp - startTimestamp > DURATION) {
            startTimestamp += DURATION
        }
        val delta = currTimestamp - startTimestamp
        val interpolatedTime = delta.toFloat() / DURATION
        val angle = interpolatedTime * ROTATION_DEGREES
        var i = 0
        for (bar in bars) {
            var finalAngle = angle
            if (i > 0 && delta > ACCELERATE_ROTATION_DURATION) {
                finalAngle += decelerate(delta, bars.size - i)
            } else if (i > 0) {
                finalAngle += accelerate(delta, bars.size - i)
            }
            rotate(bar, finalAngle.toDouble(), startPositions[i])
            i++
        }
    }

    private fun decelerate(delta: Long, scale: Int): Float {
        val accelerationDelta = delta - ACCELERATE_ROTATION_DURATION
        val interpolator = AccelerateDecelerateInterpolator()
        val interpolatedTime = interpolator.getInterpolation(accelerationDelta.toFloat() / DECELERATE_ROTATION_DURATION)
        val decelerationAngle = -interpolatedTime * (ACCELERATION_ROTATION_DEGREES * scale)
        return ACCELERATION_ROTATION_DEGREES * scale + decelerationAngle
    }

    private fun accelerate(delta: Long, scale: Int): Float {
        val interpolator = AccelerateDecelerateInterpolator()
        val interpolatedTime =
            interpolator.getInterpolation(delta.toFloat() / ACCELERATE_ROTATION_DURATION)
        return interpolatedTime * (ACCELERATION_ROTATION_DEGREES * scale)
    }

    /**
     * X = x0 + (x - x0) * cos(a) - (y - y0) * sin(a);
     * Y = y0 + (y - y0) * cos(a) + (x - x0) * sin(a);
     */
    private fun rotate(bar: SpeechBar, degrees: Double, startPosition: Point) {
        val angle = Math.toRadians(degrees)
        val x = centerX + ((startPosition.x - centerX) * Math.cos(angle) -
                (startPosition.y - centerY) * Math.sin(angle)).toInt()
        val y = centerY + ((startPosition.x - centerX) * Math.sin(angle) +
                (startPosition.y - centerY) * Math.cos(angle)).toInt()
        bar.x = x
        bar.y = y
        bar.update()
    }

    companion object {
        private const val DURATION: Long = 2000
        private const val ACCELERATE_ROTATION_DURATION: Long = 1000
        private const val DECELERATE_ROTATION_DURATION: Long = 1000
        private const val ROTATION_DEGREES = 720f
        private const val ACCELERATION_ROTATION_DEGREES = 40f
    }
}