package com.kore.common.speech.ui.animators

import com.kore.common.speech.ui.SpeechBar

class IdleAnimator(private val bars: List<SpeechBar>, private val floatingAmplitude: Int) : BarParamsAnimator {
    private var startTimestamp: Long = 0
    private var isPlaying = false
    override fun start() {
        isPlaying = true
        startTimestamp = System.currentTimeMillis()
    }

    override fun stop() {
        isPlaying = false
    }

    override fun animate() {
        if (isPlaying) {
            update(bars)
        }
    }

    private fun update(bars: List<SpeechBar>) {
        val currTimestamp = System.currentTimeMillis()
        if (currTimestamp - startTimestamp > IDLE_DURATION) {
            startTimestamp += IDLE_DURATION
        }
        val delta = currTimestamp - startTimestamp
        for ((i, bar) in bars.withIndex()) {
            updateCirclePosition(bar, delta, i)
        }
    }

    private fun updateCirclePosition(bar: SpeechBar, delta: Long, num: Int) {
        val angle = delta.toFloat() / IDLE_DURATION * 360f + 120f * num
        val y = (Math.sin(Math.toRadians(angle.toDouble())) * floatingAmplitude).toInt() + bar.startY
        bar.y = y
        bar.update()
    }

    companion object {
        private const val IDLE_DURATION: Long = 1500
    }
}