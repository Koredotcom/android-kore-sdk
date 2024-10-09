package com.kore.common.markdown

import android.text.Editable
import android.text.Html
import org.xml.sax.XMLReader

/**
 * Created by AmitYadav on 1/12/2017.
 */
class MarkdownTagHandler : Html.TagHandler {
    private var first = true
    private var parent: String? = null
    private var index = 1
    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (tag == "ul") {
            parent = "ul"
        } else if (tag == "ol") {
            parent = "ol"
        }
        if (tag == "li") {
            if (parent == "ul") {
                first = if (first) {
                    output.append(MarkdownConstant.NEW_LINE.toString() + "\t• ")
                    false
                } else {
                    true
                }
            } else {
                if (first) {
                    output.append(MarkdownConstant.NEW_LINE.toString() + "\t" + index + ". ")
                    first = false
                    index++
                } else {
                    first = true
                }
            }
        }
    }
}