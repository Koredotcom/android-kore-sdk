package kore.botssdk.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import kore.botssdk.databinding.BotTypingStatusViewBinding

class BotTypingStatusView : LinearLayout {

    private lateinit var binding: BotTypingStatusViewBinding

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
    }

    private fun init() {
        binding = BotTypingStatusViewBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    fun setBotTypingStatus(isBotTyping: Boolean) {
        Log.e("isBotTyping", "$isBotTyping + $this")
        binding.root.visibility = if (isBotTyping) VISIBLE else GONE
    }
}