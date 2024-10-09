package com.kore.common.markdown

class MarkdownConstant {

    companion object {
        const val STAR = '*'
        const val TILDE = '~'
        const val NEW_LINE = '\n'

        const val BOLD_HTML_START = "<b>"
        const val BOLD_HTML_END = "</b>"

        const val ITALIC_HTML_START = "<i>"
        const val ITALIC_HTML_END = "</i>"

        const val LINK_FORMAT = "<a href=%s>%s</a>"
        const val LINK_P1 = '['
        const val LINK_P2 = ']'
        const val LINK_P3 = '('
        const val LINK_P4 = ')'

        const val IMAGE_FORMAT = "<img src=%s alttext=%s/>"
        const val IMAGE_P1 = '!'
        const val IMAGE_P2 = '['
        const val IMAGE_P3 = ']'
        const val IMAGE_P4 = '('
        const val IMAGE_P5 = ')'

        const val UN_ORDER_LIST_CONTAINER_FORMAT = "<ul>%s</ul>"
        const val UN_ORDER_LIST_ITEM_FORMAT = "<li>%s</li>"
        const val UN_ORDER_LIST_P1 = '*'

        const val ORDER_LIST_CONTAINER_FORMAT = "<ol>%s</ol>"
        const val ORDER_LIST_ITEM_FORMAT = "<li>%s</li>"
        const val ORDER_LIST_P1 = '#'

        const val HEADING_P = "#h"
    }

}