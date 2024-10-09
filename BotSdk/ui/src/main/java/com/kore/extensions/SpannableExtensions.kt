package com.kore.extensions

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.View
import com.kore.common.constants.RegexConstants.Companion.REGEX_PHONE_NUMBER
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import java.util.regex.Pattern

fun String.spannable(actionEvent: (event: UserActionEvent) -> Unit): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(this)
    Linkify.addLinks(spannableStringBuilder, Linkify.WEB_URLS)
    val urls = spannableStringBuilder.getSpans(0, spannableStringBuilder.length, URLSpan::class.java)
    urls.map { makeLinkClickable(spannableStringBuilder, it, actionEvent) }
    makeClickablePhoneNumbers(spannableStringBuilder, actionEvent)
    return spannableStringBuilder
}

private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan, actionEvent: (event: UserActionEvent) -> Unit) {
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

private fun makeClickablePhoneNumbers(inputText: SpannableStringBuilder, actionEvent: (event: UserActionEvent) -> Unit) {
    val pattern = Pattern.compile(REGEX_PHONE_NUMBER)
    val matcher = pattern.matcher(inputText)
    while (matcher.find()) {
        val phoneNumber = inputText.subSequence(matcher.start(), matcher.end()).toString()
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                actionEvent(BotChatEvent.PhoneNumberClick(phoneNumber))
            }
        }
        inputText.setSpan(clickableSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}