package com.kore.common.markdown

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.text.Html
import android.util.Base64
import android.widget.TextView
import androidx.core.text.HtmlCompat
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL

class MarkdownImageTagHandler(
    context: Context,
    htmlTextViewRemote: TextView,
    htmlStringRemote: String
) : Html.ImageGetter {

    val context: Context? = null
    private val htmlTextViewRemote: TextView? = null
    private val htmlStringRemote: String? = null

    override fun getDrawable(source: String?): Drawable? {
        val httpGetDrawableTask = HttpGetDrawableTask(context, htmlTextViewRemote, htmlStringRemote)
        httpGetDrawableTask.execute(source)
        return null
    }
}


internal class HttpGetDrawableTask(
    val context: Context?,
    val taskTextView: TextView?,
    private val taskHtmlString: String?
) :
    AsyncTask<String?, Void?, Drawable?>() {
    private val dp1: Int = Resources.getSystem().displayMetrics.density.toInt()

    override fun doInBackground(vararg params: String?): Drawable? {
        var drawable: Drawable? = null
        val sourceURL: URL
        val base64: String
        var bufferedInputStream: BufferedInputStream? = null
        var inputStream: InputStream? = null
        try {
            if (params[0]?.contains("base64,") == true) {
                base64 = params[0]?.substring(params[0]?.indexOf(",")?.plus(1) ?: 0).toString()
                val decodedString = Base64.decode(base64.toByteArray(), Base64.DEFAULT)
                val bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                // convert Bitmap to Drawable
                drawable = BitmapDrawable(context!!.resources, bm)
                drawable.setBounds(0, 0, bm.width, bm.height)
            } else {
                sourceURL = URL(params[0])
                val urlConnection = sourceURL.openConnection()
                urlConnection.connect()
                inputStream = urlConnection.getInputStream()
                bufferedInputStream = BufferedInputStream(inputStream)
                val bm = BitmapFactory.decodeStream(bufferedInputStream)

                // convert Bitmap to Drawable
                drawable = BitmapDrawable(context!!.resources, bm)
                drawable.setBounds(0, 0, bm.width, bm.height)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                bufferedInputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return drawable!!
    }

    override fun onPostExecute(result: Drawable?) {
        if (result != null) {
            taskTextView!!.text = Html.fromHtml(
                taskHtmlString,
                HtmlCompat.FROM_HTML_MODE_LEGACY,
                { result }, null
            )
        }
    }
}
