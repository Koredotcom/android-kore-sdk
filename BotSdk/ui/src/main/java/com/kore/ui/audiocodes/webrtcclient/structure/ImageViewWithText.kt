package com.kore.ui.audiocodes.webrtcclient.structure

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.kore.ui.R

class ImageViewWithText : RelativeLayout {
    private var rootView: View? = null
    var valueTextView: TextView? = null
    private var valueImageView: ImageView? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        rootView = inflate(context, R.layout.image_view_with_text, this)
        valueTextView = rootView?.findViewById(R.id.imageviewwithtext_text)
        valueImageView = rootView?.findViewById(R.id.imageviewwithtext_imageview)
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.ImageViewWithTextStyle, 0, 0
            )
            val attBackDrawable = typedArray.getDrawable(R.styleable.ImageViewWithTextStyle_BackgroundImage)
            val attSrcDrawable = typedArray.getDrawable(R.styleable.ImageViewWithTextStyle_srcImage)
            val attText = typedArray.getText(R.styleable.ImageViewWithTextStyle_Text)
            val textColor = typedArray.getColor(R.styleable.ImageViewWithTextStyle_TextColor, -2)
            //            float attTextSize = typedArray.getDimensionPixelSize(R.styleable.ImageViewWithTextStyle_TextSize,-1);
            if (attBackDrawable != null) {
                valueImageView?.background = attBackDrawable
            }
            if (attSrcDrawable != null) {
                valueImageView?.setImageDrawable(attSrcDrawable)
            }
            if (attText != null) {
                valueTextView?.text = attText
            }
            if (textColor != -2) {
                valueTextView?.setTextColor(textColor)
            }
            valueTextView?.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            typedArray.recycle()
        }
    }

    companion object {
        private const val TAG = "ImageViewWithText"
    }
}