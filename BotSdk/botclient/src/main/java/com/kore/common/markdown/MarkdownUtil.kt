package com.kore.common.markdown

import java.lang.reflect.Array

object MarkdownUtil {

    fun processMarkDown(textStr: String): String {
        var text = textStr
        text = markDownBoldAndItalic(
            text, MarkdownConstant.STAR,
            MarkdownConstant.BOLD_HTML_START, MarkdownConstant.BOLD_HTML_END
        )
        text = markDownBoldAndItalic(
            text, MarkdownConstant.TILDE,
            MarkdownConstant.ITALIC_HTML_START, MarkdownConstant.ITALIC_HTML_END
        )
        text = markDownImage(text)
        text = markDownLink(text)
        text = markDownList(
            text,
            MarkdownConstant.UN_ORDER_LIST_P1,
            MarkdownConstant.UN_ORDER_LIST_ITEM_FORMAT,
            MarkdownConstant.UN_ORDER_LIST_CONTAINER_FORMAT
        )
        text = markDownList(
            text, MarkdownConstant.ORDER_LIST_P1,
            MarkdownConstant.ORDER_LIST_ITEM_FORMAT, MarkdownConstant.ORDER_LIST_CONTAINER_FORMAT
        )
        text = markDownHeading(text)
        return text
    }

    /**
     * Convert *text* (no spaces accepted btw * and text) to **Bold Text**
     * Convert ~italic~ (no spaces accepted btw ~ and text) to *Italic Text*
     */
    private fun markDownBoldAndItalic(
        textStr: String,
        delimiter: Char,
        startTag: String,
        endTag: String
    ): String {
        var text = textStr
        var index = -1
        var nIndex = -1
        do {
            index = text.indexOf(delimiter, max(intArrayOf(index, nIndex)) + 1)
            if (index == -1) {
                break
            }

            //get next char
            /*     char c = text.charAt(index + 1);
          if (c == ' ') {
                continue;
            }*/nIndex = text.indexOf(delimiter, index + 1)
            if (nIndex == -1) {
                break
            }

            //get previous char and ignore SPACE and consecutive STAR
            val c = text[nIndex - 1]
            if (c == delimiter) {
                continue
            }
            val builder = StringBuilder(text)

            //replace * at index with opening braces <br>
            builder.deleteCharAt(index)
            builder.insert(index, startTag)

            //update nIndex since <b> or <i> inserted in place of * i.e 3-1
            nIndex += 2

            //replace * at nIndex with closing braces </b> or </i>
            builder.deleteCharAt(nIndex)
            builder.insert(nIndex, endTag)
            text = builder.toString()
        } while (true)
        return text
    }

    /**
     * Convert [linktext](http://example.com) to [Google!](http://google.com)
     */
    private fun markDownLink(textStr: String): String {
        var text = textStr
        var indexP1 = -1
        var indexP2 = -1
        var indexP3 = -1
        var indexP4 = -1
        do {
            indexP1 = text.indexOf(
                MarkdownConstant.LINK_P1,
                max(intArrayOf(indexP1, indexP2, indexP3, indexP4))
            )
            if (indexP1 == -1) {
                break
            }
            indexP2 = text.indexOf(MarkdownConstant.LINK_P2, indexP1 + 1)
            if (indexP2 == -1) {
                break
            }
            var tmpIndexP1: Int = text.indexOf(MarkdownConstant.LINK_P1, indexP1 + 1)
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP2) {
                indexP2 = -1
                continue
            }
            val hyper = text.substring(indexP1 + 1, indexP2)
            if (hyper.isEmpty()) {
                continue
            }
            indexP3 = text.indexOf(MarkdownConstant.LINK_P3, indexP2)
            if (indexP3 == -1) {
                break
            }
            tmpIndexP1 = text.indexOf(MarkdownConstant.LINK_P1, indexP1 + 1)
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP3) {
                indexP3 = -1
                continue
            }
            var tmpIndexP2: Int = text.indexOf(MarkdownConstant.LINK_P2, indexP2 + 1)
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP3) {
                indexP3 = -1
                continue
            }

            //check for ]( i.e no character in between ](
            if (indexP2 + 1 != indexP3) {
                indexP2 = text.indexOf(MarkdownConstant.LINK_P2, indexP3 + 1)
                continue
            }
            indexP4 = text.indexOf(MarkdownConstant.LINK_P4, indexP3)
            if (indexP4 == -1) {
                break
            }
            tmpIndexP1 = text.indexOf(MarkdownConstant.LINK_P1, indexP1 + 1)
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP4) {
                indexP4 = -1
                continue
            }
            tmpIndexP2 = text.indexOf(MarkdownConstant.LINK_P2, indexP2 + 1)
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP4) {
                indexP4 = -1
                continue
            }
            val tmpIndexP3: Int = text.indexOf(MarkdownConstant.LINK_P3, indexP3 + 1)
            if (tmpIndexP3 != -1 && tmpIndexP3 < indexP4) {
                indexP4 = -1
                continue
            }
            val link = text.substring(indexP3 + 1, indexP4)
            if (link.isEmpty()) {
                continue
            }

            //<a href="http://google.com">Google!</a>
            //Go to [Google](http://google.com) and lookup

            /* if (!link.startsWith("www.") && !link.startsWith("http://")) {
                link = "www." + link;
            }

            if (!link.startsWith("http://")) {
                link = "http://" + link;
            }*/
            val builder = StringBuilder(text)
            builder.delete(indexP1, indexP4 + 1)
            val hyperLink: String =
                java.lang.String.format(MarkdownConstant.LINK_FORMAT, link, hyper)
            builder.insert(indexP1, hyperLink)
            text = builder.toString()
            //            text = text.replace("!<", "<");
        } while (true)
        return text
    }

    /**
     * Convert ![linktext](http://example.com) to <img src="IMAGE_URL" alttext="linktext"></img>
     */
    private fun markDownImage(textStr: String): String {
        var text = textStr
        var indexP1 = -1
        var indexP2 = -1
        var indexP3 = -1
        var indexP4 = -1
        var indexP5 = -1
        do {
            indexP1 = text.indexOf(
                MarkdownConstant.IMAGE_P1,
                max(intArrayOf(indexP1, indexP2, indexP3, indexP4, indexP5))
            )
            if (indexP1 == -1) {
                break
            }
            indexP2 = text.indexOf(MarkdownConstant.IMAGE_P2, indexP1 + 1)
            if (indexP2 == -1) {
                break
            }
            var tmpIndexP1: Int = text.indexOf(MarkdownConstant.IMAGE_P1, indexP1 + 1)
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP2) {
                indexP2 = -1
                continue
            }

            //check for ![ i.e no character in between ![
            if (indexP1 + 1 != indexP2) {
                indexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP2 + 1)
                continue
            }
            indexP3 = text.indexOf(MarkdownConstant.IMAGE_P3, indexP2 + 1)
            if (indexP3 == -1) {
                break
            }
            tmpIndexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP1 + 1)
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP3) {
                indexP3 = -1
                continue
            }
            var tmpIndexP2: Int = text.indexOf(MarkdownConstant.IMAGE_P2, indexP2 + 1)
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP3) {
                indexP3 = -1
                continue
            }
            val hyper = text.substring(indexP2 + 1, indexP3)
            if (hyper.isEmpty()) {
                continue
            }
            indexP4 = text.indexOf(MarkdownConstant.IMAGE_P4, indexP3)
            if (indexP4 == -1) {
                break
            }
            tmpIndexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP1 + 1)
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP4) {
                indexP4 = -1
                continue
            }
            tmpIndexP2 = text.indexOf(MarkdownConstant.IMAGE_P2, indexP2 + 1)
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP4) {
                indexP4 = -1
                continue
            }
            var tmpIndexP3: Int = text.indexOf(MarkdownConstant.IMAGE_P3, indexP3 + 1)
            if (tmpIndexP3 != -1 && tmpIndexP3 < indexP4) {
                indexP4 = -1
                continue
            }

            //check for ]( i.e no character in between ](
            if (indexP3 + 1 != indexP4) {
                indexP3 = text.indexOf(MarkdownConstant.IMAGE_P3, indexP4 + 1)
                continue
            }
            indexP5 = text.indexOf(MarkdownConstant.IMAGE_P5, indexP4)
            if (indexP5 == -1) {
                break
            }
            tmpIndexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP1 + 1)
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP5) {
                indexP5 = -1
                continue
            }
            tmpIndexP2 = text.indexOf(MarkdownConstant.IMAGE_P2, indexP2 + 1)
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP5) {
                indexP5 = -1
                continue
            }
            tmpIndexP3 = text.indexOf(MarkdownConstant.IMAGE_P3, indexP3 + 1)
            if (tmpIndexP3 != -1 && tmpIndexP3 < indexP5) {
                indexP5 = -1
                continue
            }
            val tmpIndexP4: Int = text.indexOf(MarkdownConstant.IMAGE_P4, indexP4 + 1)
            if (tmpIndexP4 != -1 && tmpIndexP4 < indexP5) {
                indexP5 = -1
                continue
            }
            var link = text.substring(indexP4 + 1, indexP5)
            if (link.isEmpty()) {
                continue
            }

            //<a href="http://google.com">Google!</a>
            //Go to [Google](http://google.com) and lookup
            if (!link.startsWith("www.") && !link.startsWith("http://") && !link.startsWith("https://")) {
                link = "www.$link"
            }
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://$link"
            }
            val builder = StringBuilder(text)
            builder.delete(indexP1, indexP5 + 1)
            val hyperLink: String =
                java.lang.String.format(MarkdownConstant.IMAGE_FORMAT, link, hyper)
            builder.insert(indexP1, hyperLink)
            text = builder.toString()
        } while (true)
        return text
    }

    /**
     * Convert * space text to  * ...
     * Convert # space text to  1. ...
     */
    private fun markDownList(
        textStr: String,
        delimiter: Char,
        itemFormat: String,
        itemContainerFormat: String
    ): String {
        var text = textStr
        var indexP1 = -1
        var indexP2 = -1
        var exit: Boolean
        val itemBuilder = StringBuilder()
        do {
            var itemStartOffset = -1
            var itemEndOffset = -1
            while (true) {
                indexP1 = text.indexOf(delimiter, max(intArrayOf(indexP1, indexP2)) + 1)
                if (indexP1 == -1) {
                    exit = true
                    break
                }
                if (text[indexP1 + 1] != ' ') {
                    continue
                }
                indexP2 = text.indexOf(MarkdownConstant.NEW_LINE, indexP1 + 1)
                if (indexP2 == -1) {
                    exit = true
                    break
                }
                itemEndOffset = indexP2
                val item = text.substring(indexP1 + 2, indexP2)
                if (item.isEmpty()) {
                    continue
                }
                if (itemBuilder.isEmpty()) {
                    itemStartOffset = indexP1
                }
                itemBuilder.append(String.format(itemFormat, item))
            }
            if (itemStartOffset == -1) {
                continue
            }
            val builder = StringBuilder(text)
            builder.delete(itemStartOffset, itemEndOffset)
            val itemList = String.format(itemContainerFormat, itemBuilder.toString())
            builder.insert(itemStartOffset, itemList)
            text = builder.toString()
        } while (!exit)
        return text
    }

    /**
     * Convert #h1 to <h1> and so on till 6
    </h1> */
    private fun markDownHeading(textStr: String): String {
        var text = textStr
        var index: Int
        do {
            index = text.indexOf(MarkdownConstant.HEADING_P)
            if (index == -1) {
                break
            }
            val builder = StringBuilder(text)
            val c = text[index + 2]
            text = if (Character.isDigit(c) && (c.toString() + "").toInt() <= 6) {
                builder.delete(index, index + 3)
                when (text[index + 2]) {
                    '1' -> builder.insert(index, "<h1>")
                    '2' -> builder.insert(index, "<h2>")
                    '3' -> builder.insert(index, "<h3>")
                    '4' -> builder.insert(index, "<h4>")
                    '5' -> builder.insert(index, "<h5>")
                    '6' -> builder.insert(index, "<h6>")
                }
                builder.toString()
            } else {
                break
            }
        } while (true)
        return text
    }

    private fun max(array: IntArray): Int {
        validateArray(array)
        var max = array[0]
        for (j in 1 until array.size) {
            if (array[j] > max) {
                max = array[j]
            }
        }
        return max
    }

    private fun validateArray(array: Any?) {
        requireNotNull(array) { "The Array must not be null" }
        require(Array.getLength(array) != 0) { "Array cannot be empty." }
    }
}