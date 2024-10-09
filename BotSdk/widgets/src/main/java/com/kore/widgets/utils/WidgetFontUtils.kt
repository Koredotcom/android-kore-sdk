package com.kore.widgets.utils

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.kore.widgets.R

object WidgetFontUtils {
    private var robotoLight: Typeface? = null
    private var robotoLightItalic: Typeface? = null
    private var robotoRegular: Typeface? = null
    private var robotoRegularItalic: Typeface? = null
    private var robotoMedium: Typeface? = null
    private var robotoMediumItalic: Typeface? = null
    private var robotoBold: Typeface? = null
    private var robotoBoldItalic: Typeface? = null
    private var robotoExtraBold: Typeface? = null
    private const val ROBOTO_LIGHT = "light"
    private const val ROBOTO_LIGHT_ITALICS = "light-italic"
    private const val ROBOTO_REGULAR = "regular"
    private const val ROBOTO_REGULAR_ITALICS = "regular-italic"
    private const val ROBOTO_MEDIUM = "medium"
    private const val ROBOTO_MEDIUM_ITALICS = "medium-italic"
    private const val ROBOTO_BOLD = "bold"
    private const val ROBOTO_BOLD_ITALICS = "bold-italic"
    private const val ROBOTO_EXTRA_BOLD = "extra-bold"

    fun applyCustomFont(context: Context, root: View, isApplyFontStyle: Boolean) {
        if (isApplyFontStyle) {
            if (root is ViewGroup) {
                for (count in 0..root.childCount) {
                    applyCustomFont(context, root.getChildAt(count), isApplyFontStyle)
                }
            } else if (root is TextView || root is Button || root is EditText) {
                setCustomFont(root as TextView, context)
            }
        }
    }

    private fun setCustomFont(myView: TextView, context: Context) {
        val tag = myView.tag
        if (tag is String) {
            if (tag.equals(ROBOTO_LIGHT, ignoreCase = true)) {
                if (robotoLight == null) {
                    robotoLight = ResourcesCompat.getFont(context, R.font.latoregular)
                }
                myView.typeface = robotoLight
            } else if (tag.equals(ROBOTO_LIGHT_ITALICS, ignoreCase = true)) {
                if (robotoLightItalic == null) {
                    robotoLightItalic = ResourcesCompat.getFont(context, R.font.latoregular)
                }
                myView.typeface = robotoLightItalic
            } else if (tag.equals(ROBOTO_REGULAR, ignoreCase = true)) {
                if (robotoRegular == null) {
                    robotoRegular = ResourcesCompat.getFont(context, R.font.latoregular)
                }
                myView.typeface = robotoRegular
            } else if (tag.equals(ROBOTO_REGULAR_ITALICS, ignoreCase = true)) {
                if (robotoRegularItalic == null) {
                    robotoRegularItalic = ResourcesCompat.getFont(context, R.font.latoregular)
                }
                myView.typeface = robotoRegularItalic
            } else if (tag.equals(ROBOTO_MEDIUM, ignoreCase = true)) {
                if (robotoMedium == null) {
                    robotoMedium = ResourcesCompat.getFont(context, R.font.latoregular)
                }
                myView.typeface = robotoMedium
            } else if (tag.equals(ROBOTO_MEDIUM_ITALICS, ignoreCase = true)) {
                if (robotoMediumItalic == null) {
                    robotoMediumItalic = ResourcesCompat.getFont(context, R.font.latoregular)
                }
                myView.typeface = robotoMediumItalic
            } else if (tag.equals(ROBOTO_BOLD, ignoreCase = true)) {
                if (robotoBold == null) {
                    robotoBold = ResourcesCompat.getFont(context, R.font.latobold)
                }
                myView.typeface = robotoBold
            } else if (tag.equals(ROBOTO_BOLD_ITALICS, ignoreCase = true)) {
                if (robotoBoldItalic == null) {
                    robotoBoldItalic = ResourcesCompat.getFont(context, R.font.latobold)
                }
                myView.typeface = robotoBoldItalic
            } else if (tag.equals(ROBOTO_EXTRA_BOLD, ignoreCase = true)) {
                if (robotoExtraBold == null) {
                    robotoExtraBold = ResourcesCompat.getFont(context, R.font.latobold)
                }
                myView.typeface = robotoExtraBold
            }
        }
    }

    fun getCustomTypeface(tag: String, context: Context): Typeface? {
        return when (tag) {
            ROBOTO_LIGHT -> {
                if (robotoLight == null) robotoLight = ResourcesCompat.getFont(context, R.font.latoregular)
                robotoLight
            }

            ROBOTO_LIGHT_ITALICS -> {
                if (robotoLightItalic == null) robotoLightItalic = ResourcesCompat.getFont(context, R.font.latoregular)
                robotoLightItalic
            }

            ROBOTO_REGULAR_ITALICS -> {
                if (robotoRegularItalic == null) robotoRegularItalic = ResourcesCompat.getFont(context, R.font.latoregular)
                robotoRegularItalic
            }

            ROBOTO_MEDIUM -> {
                if (robotoMedium == null) robotoMedium = ResourcesCompat.getFont(context, R.font.latoregular)
                robotoMedium
            }

            ROBOTO_MEDIUM_ITALICS -> {
                if (robotoMediumItalic == null) robotoMediumItalic = ResourcesCompat.getFont(context, R.font.latoregular)
                robotoMediumItalic
            }

            ROBOTO_BOLD -> {
                if (robotoBold == null) robotoBold = ResourcesCompat.getFont(context, R.font.latobold)
                robotoBold
            }

            ROBOTO_BOLD_ITALICS -> {
                if (robotoBoldItalic == null) robotoBoldItalic = ResourcesCompat.getFont(context, R.font.latobold)
                robotoBoldItalic
            }

            ROBOTO_EXTRA_BOLD -> {
                if (robotoExtraBold == null) robotoExtraBold = ResourcesCompat.getFont(context, R.font.latobold)
                robotoExtraBold
            }

            else -> {
                if (robotoRegular == null) robotoRegular = ResourcesCompat.getFont(context, R.font.latoregular)
                robotoRegular
            }
        }
    }
}