package kore.botssdk.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kore.botssdk.databinding.BotLocationCardViewBinding
import kore.botssdk.extensions.dpToPx
import kore.botssdk.models.BotResponse
import kore.botssdk.net.SDKConfiguration
import kore.botssdk.view.viewUtils.LayoutUtils
import kore.botssdk.view.viewUtils.MeasureUtils

class BotLocationCardView : LinearLayout {

    private lateinit var binding: BotLocationCardViewBinding

    private var themeName: String? = null

    private lateinit var sharedPreferences: SharedPreferences

    private val restrictedMaxWidth = 1f

    private lateinit var backgroundDrawable: GradientDrawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        binding = BotLocationCardViewBinding.inflate(LayoutInflater.from(context), this, true)
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE)
        themeName = sharedPreferences.getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1)
        backgroundDrawable = binding.root.background as GradientDrawable
    }

    fun populateData() {
        if (themeName.equals(BotResponse.THEME_NAME_1, ignoreCase = true)) {
            backgroundDrawable.setStroke(
                1.dpToPx(context), Color.parseColor(
                    sharedPreferences.getString(BotResponse.WIDGET_BORDER_COLOR, "#ffffff")
                )
            )
        } else {
            backgroundDrawable.setStroke(
                1.dpToPx(context), Color.parseColor(
                    sharedPreferences.getString(BotResponse.WIDGET_BORDER_COLOR, SDKConfiguration.BubbleColors.rightBubbleUnSelected)
                )
            )
        }
        binding.root.background = backgroundDrawable
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

        var totalHeight = paddingTop
        var totalWidth = paddingLeft

        val childWidthSpec: Int = MeasureSpec.makeMeasureSpec(restrictedMaxWidth.toInt(), MeasureSpec.EXACTLY)
        MeasureUtils.measure(binding.root, childWidthSpec, wrapSpec)

        totalHeight += binding.root.measuredHeight + paddingBottom + paddingTop
        totalWidth += binding.root.measuredWidth + paddingLeft + paddingRight
        if (totalHeight != 0) {
            totalWidth += 3.dpToPx(context)
        }

        val parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY)
        val parentWidthSpec = MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST)
        setMeasuredDimension(parentWidthSpec, parentHeightSpec)
    }

    override fun onLayout(p0: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val count = childCount
        //get the available size of child view
        val childLeft = 0
        var childTop = 0

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop)
                childTop += child.measuredHeight
            }
        }
    }
}