package com.kore.common.speech.ui.animators

import com.kore.common.speech.ui.SpeechBar

class RmsAnimator(speechBars: List<SpeechBar>) : BarParamsAnimator {
    private val barAnimators: MutableList<BarRmsAnimator>

    init {
        barAnimators = ArrayList()
        for (bar in speechBars) {
            barAnimators.add(BarRmsAnimator(bar))
        }
    }

    override fun start() {
        for (barAnimator in barAnimators) {
            barAnimator.start()
        }
    }

    override fun stop() {
        for (barAnimator in barAnimators) {
            barAnimator.stop()
        }
    }

    override fun animate() {
        for (barAnimator in barAnimators) {
            barAnimator.animate()
        }
    }

    fun onRmsChanged(rmsDB: Float) {
        for (barAnimator in barAnimators) {
            barAnimator.onRmsChanged(rmsDB)
        }
    }
}