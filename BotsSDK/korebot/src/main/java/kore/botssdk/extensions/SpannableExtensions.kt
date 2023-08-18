package kore.botssdk.extensions

import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.View
import kore.botssdk.R
import kore.botssdk.activity.GenericWebViewActivity

fun String.spannable(context: Context): SpannableStringBuilder {
    val sequence: CharSequence = Html.fromHtml(
        this.replace("\n", "<br />"),
    )
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(
        0, sequence.length,
        URLSpan::class.java
    )

    for (span in urls) {
        Log.e("Called", "makeLinkClickable $span")
        makeLinkClickable(context, strBuilder, span)
    }

    return strBuilder
}


private fun makeLinkClickable(context: Context, strBuilder: SpannableStringBuilder, span: URLSpan) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            val intent = Intent(context, GenericWebViewActivity::class.java)
            intent.putExtra("url", span.url)
            intent.putExtra("header", context.resources.getString(R.string.app_name))
            context.startActivity(intent)
        }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
}