package kore.botssdk.extensions

import android.content.Context
import android.content.Intent
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.View
import kore.botssdk.R
import kore.botssdk.activity.GenericWebViewActivity
import kore.botssdk.activity.GenericWebViewActivity.EXTRA_HEADER
import kore.botssdk.activity.GenericWebViewActivity.EXTRA_URL

fun String.spannable(context: Context): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(this)
    Linkify.addLinks(spannableStringBuilder, Linkify.WEB_URLS)
    val urls = spannableStringBuilder.getSpans(0, spannableStringBuilder.length, URLSpan::class.java)
    urls.map { makeLinkClickable(context, spannableStringBuilder, it) }
    return spannableStringBuilder
}

private fun makeLinkClickable(context: Context, strBuilder: SpannableStringBuilder, span: URLSpan) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            val intent = Intent(context, GenericWebViewActivity::class.java)
            intent.putExtra(EXTRA_URL, span.url)
            intent.putExtra(EXTRA_HEADER, context.resources.getString(R.string.app_name))
            context.startActivity(intent)
        }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
}