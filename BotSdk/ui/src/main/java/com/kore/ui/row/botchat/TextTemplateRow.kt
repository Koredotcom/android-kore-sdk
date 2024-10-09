package com.kore.ui.row.botchat

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.extensions.formatEmojis
import com.kore.common.markdown.MarkdownImageTagHandler
import com.kore.common.markdown.MarkdownTagHandler
import com.kore.common.markdown.MarkdownUtil
import com.kore.common.row.SimpleListRow
import com.kore.common.utils.DimensionUtils.Companion.dp1
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R
import com.kore.ui.databinding.RowTextTemplateBinding

class TextTemplateRow(
    override val type: SimpleListRowType,
    private val context: Context,
    private val id: String,
    private val botMessage: String?,
    private val isBotRequest: Boolean,
    private val iconUrl: String?,
    private val isLastItem: Boolean = false,
    private val actionEvent: (event: UserActionEvent) -> Unit,
    private val timeStamp: String,
    private val errorTextColor: String = ""
) : SimpleListRow() {

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TextTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TextTemplateRow) return false
        return otherRow.botMessage == botMessage && otherRow.timeStamp == timeStamp
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, !isBotRequest, false)
        val childBinding = RowTextTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val isTimeStampRequired = timeStamp.isNotEmpty() && PreferenceRepositoryImpl()
                .getSharedPreference(context, BotResponseConstants.THEME_NAME)
                .getBoolean(BotResponseConstants.IS_TIME_STAMP_REQUIRED, false)
            if (isTimeStampRequired) {
                msgTimeStamp.setTextColor(
                    Color.parseColor(
                        PreferenceRepositoryImpl().getSharedPreference(context, BotResponseConstants.THEME_NAME)
                            .getString(BotResponseConstants.TIME_STAMP_TXT_COLOR, "#B0B0B0")
                    )
                )
                if (!PreferenceRepositoryImpl().getSharedPreference(context, BotResponseConstants.THEME_NAME)
                        .getBoolean(BotResponseConstants.TIME_STAMP_IS_BOTTOM, false)
                ) {
                    root.removeView(msgTimeStamp)
                    root.addView(msgTimeStamp, 0)
                }
            }
            val stringBuilder = MarkdownUtil.processMarkDown(botMessage?.formatEmojis() ?: "")
            val sequence = stringBuilder.replace("\n", "<br />").let {
                HtmlCompat.fromHtml(
                    it, HtmlCompat.FROM_HTML_MODE_LEGACY, MarkdownImageTagHandler(context, message, stringBuilder), MarkdownTagHandler()
                )
            }

            val strBuilder = SpannableStringBuilder(sequence)
            val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)

            for (span in urls) {
                makeLinkClickable(strBuilder, span)
            }

            //1st & 2nd - topLeft, 3rd & 4th - topRight, 5th & 6th - bottomRight 7th & 8th - bottomLeft
            val roundedRadii = floatArrayOf(16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1, 16 * dp1)
            val recRightRadii = floatArrayOf(8 * dp1, 8 * dp1, 2 * dp1, 2 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1)
            val recLeftRadii = floatArrayOf(2 * dp1, 2 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1)
            val balRightRadii = floatArrayOf(8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 2 * dp1, 2 * dp1, 8 * dp1, 8 * dp1)
            val balLeftRadii = floatArrayOf(8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 8 * dp1, 2 * dp1, 2 * dp1)

            val sharedPrefs = PreferenceRepositoryImpl()
            val bubbleStyle = sharedPrefs.getStringValue(context, BotResponseConstants.THEME_NAME, BotResponseConstants.BUBBLE_STYLE, "EDGE")
            message.text = strBuilder
            val textColor = if (!isBotRequest) {
                errorTextColor.ifEmpty {
                    sharedPrefs.getStringValue(context, BotResponseConstants.THEME_NAME, BotResponseConstants.BUBBLE_LEFT_TEXT_COLOR, "#000000")
                }
            } else
                sharedPrefs.getStringValue(context, BotResponseConstants.THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF")
            message.setTextColor(ColorStateList.valueOf(Color.parseColor(textColor)))
            message.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    root.context.resources,
                    if (isBotRequest) R.drawable.theme1_right_bubble_bg else R.drawable.theme1_left_bubble_bg,
                    root.context.theme
                )
            )

            val gradientDrawable = message.background as GradientDrawable
            if (!isBotRequest) {
                if (bubbleStyle.equals(BotResponseConstants.ROUNDED, ignoreCase = true)) gradientDrawable.cornerRadii = roundedRadii
                else if (bubbleStyle.equals(BotResponseConstants.RECTANGLE, ignoreCase = true)) gradientDrawable.cornerRadii = recLeftRadii
                else gradientDrawable.cornerRadii = balLeftRadii

                gradientDrawable.setColor(
                    Color.parseColor(
                        sharedPrefs.getStringValue(
                            context,
                            BotResponseConstants.THEME_NAME,
                            BotResponseConstants.BUBBLE_LEFT_BG_COLOR,
                            "#efeffc"
                        )
                    )
                )
                gradientDrawable.setStroke(
                    (1 * dp1).toInt(), Color.parseColor(
                        sharedPrefs.getStringValue(context, BotResponseConstants.THEME_NAME, BotResponseConstants.BUBBLE_LEFT_BG_COLOR, "#efeffc")
                    )
                )

            } else {
                if (bubbleStyle.equals(BotResponseConstants.ROUNDED, ignoreCase = true)) gradientDrawable.cornerRadii = roundedRadii
                else if (bubbleStyle.equals(BotResponseConstants.RECTANGLE, ignoreCase = true)) gradientDrawable.cornerRadii = recRightRadii
                else gradientDrawable.cornerRadii = balRightRadii

                gradientDrawable.setColor(
                    Color.parseColor(
                        sharedPrefs.getStringValue(context, BotResponseConstants.THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5")
                    )
                )
                gradientDrawable.setStroke(
                    (1 * dp1).toInt(), Color.parseColor(
                        sharedPrefs.getStringValue(context, BotResponseConstants.THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5")
                    )
                )
            }


            message.movementMethod = LinkMovementMethod.getInstance()
            root.gravity = if (isBotRequest) Gravity.END else Gravity.START
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowTextTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowTextTemplateBinding.commonBind() {
        val isTimeStampRequired = timeStamp.isNotEmpty() && PreferenceRepositoryImpl()
            .getSharedPreference(context, BotResponseConstants.THEME_NAME)
            .getBoolean(BotResponseConstants.IS_TIME_STAMP_REQUIRED, false)
        msgTimeStamp.isVisible = isTimeStampRequired
        if (isTimeStampRequired) {
            msgTimeStamp.text = HtmlCompat.fromHtml(timeStamp, HtmlCompat.FROM_HTML_MODE_COMPACT)
            msgTimeStamp.setTextColor(
                Color.parseColor(
                    PreferenceRepositoryImpl().getSharedPreference(context, BotResponseConstants.THEME_NAME)
                        .getString(BotResponseConstants.TIME_STAMP_TXT_COLOR, "#B0B0B0")
                )
            )
        }
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                actionEvent(BotChatEvent.UrlClick(span.url))
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }
}