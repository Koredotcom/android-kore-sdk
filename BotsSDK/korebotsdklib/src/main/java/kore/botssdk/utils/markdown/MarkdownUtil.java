package kore.botssdk.utils.markdown;


import org.apache.commons.lang3.math.NumberUtils;

import kore.botssdk.utils.Utils;


/**
 * Created by AmitYadav on 2/28/2017.
 */

public class MarkdownUtil {

    public static String processMarkDown(String text) {
//        text = text.replaceAll("" + MarkdownConstant.NEW_LINE, "<br/>");
//        text = text.replaceAll("\\s", "&nbsp;");
        text = markDownBoldAndItalic(text, MarkdownConstant.STAR,
                MarkdownConstant.BOLD_HTML_START, MarkdownConstant.BOLD_HTML_END);
        text = markDownBoldAndItalic(text, MarkdownConstant.TILDE,
                MarkdownConstant.ITALIC_HTML_START, MarkdownConstant.ITALIC_HTML_END);
        text = markDownImage(text);
        text = markDownLink(text);
        text = markDownList(text, MarkdownConstant.UNORDER_LIST_P1,
                MarkdownConstant.UNORDER_LIST_ITEM_FORMAT, MarkdownConstant.UNORDER_LIST_CONTAINER_FORMAT);
        text = markDownList(text, MarkdownConstant.ORDER_LIST_P1,
                MarkdownConstant.ORDER_LIST_ITEM_FORMAT, MarkdownConstant.ORDER_LIST_CONTAINER_FORMAT);
        text = markDownHeading(text);

        return text;
    }

    /**
     * Convert *text* (no spaces accepted btw * and text) to <b>Bold Text</b>
     * Convert ~italic~ (no spaces accepted btw ~ and text) to <i>Italic Text</i>
     */
    private static String markDownBoldAndItalic(String text, char delimiter, String startTag, String endTag) {

        int index = -1;
        int nIndex = -1;

        do {

            index = text.indexOf(delimiter, NumberUtils.max(new int[]{index, nIndex}) + 1);
            if (index == -1) {
                break;
            }

            //get next char
         /*     char c = text.charAt(index + 1);
          if (c == ' ') {
                continue;
            }*/

            nIndex = text.indexOf(delimiter, index + 1);
            if (nIndex == -1) {
                break;
            }

            //get previous char and ignore SPACE and consecutive STAR
            char c = text.charAt(nIndex - 1);
            if (c == delimiter) {
                continue;
            }

            StringBuilder builder = new StringBuilder(text);

            //replace * at index with opening braces <br>
            builder.deleteCharAt(index);
            builder.insert(index, startTag);

            //update nIndex since <b> or <i> inserted in place of * i.e 3-1
            nIndex += 2;

            //replace * at nIndex with closing braces </b> or </i>
            builder.deleteCharAt(nIndex);
            builder.insert(nIndex, endTag);
            text = builder.toString();

        } while (true);

        return text;
    }

    /**
     * Convert [linktext](http://example.com) to <a href="http://google.com">Google!</a>
     */
    private static String markDownLink(String text) {

        int indexP1 = -1;
        int indexP2 = -1;
        int indexP3 = -1;
        int indexP4 = -1;

        do {
            indexP1 = text.indexOf(MarkdownConstant.LINK_P1, NumberUtils.max(new int[]{indexP1, indexP2, indexP3, indexP4}) + 1);
            if (indexP1 == -1) {
                break;
            }

            indexP2 = text.indexOf(MarkdownConstant.LINK_P2, indexP1 + 1);
            if (indexP2 == -1) {
                break;
            }

            int tmpIndexP1 = text.indexOf(MarkdownConstant.LINK_P1, indexP1 + 1);
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP2) {
                indexP2 = -1;
                continue;
            }

            String hyper = text.substring(indexP1 + 1, indexP2);
            if (Utils.isNullOrEmpty(hyper)) {
                continue;
            }

            indexP3 = text.indexOf(MarkdownConstant.LINK_P3, indexP2);
            if (indexP3 == -1) {
                break;
            }

            tmpIndexP1 = text.indexOf(MarkdownConstant.LINK_P1, indexP1 + 1);
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP3) {
                indexP3 = -1;
                continue;
            }

            int tmpIndexP2 = text.indexOf(MarkdownConstant.LINK_P2, indexP2 + 1);
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP3) {
                indexP3 = -1;
                continue;
            }

            //check for ]( i.e no character in between ](
            if (indexP2 + 1 != indexP3) {
                indexP2 = text.indexOf(MarkdownConstant.LINK_P2, indexP3 + 1);
                continue;
            }

            indexP4 = text.indexOf(MarkdownConstant.LINK_P4, indexP3);
            if (indexP4 == -1) {
                break;
            }

            tmpIndexP1 = text.indexOf(MarkdownConstant.LINK_P1, indexP1 + 1);
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP4) {
                indexP4 = -1;
                continue;
            }

            tmpIndexP2 = text.indexOf(MarkdownConstant.LINK_P2, indexP2 + 1);
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP4) {
                indexP4 = -1;
                continue;
            }

            int tmpIndexP3 = text.indexOf(MarkdownConstant.LINK_P3, indexP3 + 1);
            if (tmpIndexP3 != -1 && tmpIndexP3 < indexP4) {
                indexP4 = -1;
                continue;
            }

            String link = text.substring(indexP3 + 1, indexP4);
            if (Utils.isNullOrEmpty(link)) {
                continue;
            }

            //<a href="http://google.com">Google!</a>
            //Go to [Google](http://google.com) and lookup

           /* if (!link.startsWith("www.") && !link.startsWith("http://")) {
                link = "www." + link;
            }

            if (!link.startsWith("http://")) {
                link = "http://" + link;
            }*/

            StringBuilder builder = new StringBuilder(text);
            builder.delete(indexP1, indexP4 + 1);
            String hyperLink = String.format(MarkdownConstant.LINK_FORMAT, link, hyper);
            builder.insert(indexP1, hyperLink);
            text = builder.toString();

        } while (true);

        return text;
    }

    /**
     * Convert ![linktext](http://example.com) to <img src="IMAGE_URL" alttext="linktext"/>
     */
    private static String markDownImage(String text) {

        int indexP1 = -1;
        int indexP2 = -1;
        int indexP3 = -1;
        int indexP4 = -1;
        int indexP5 = -1;

        do {

            indexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, NumberUtils.max(new int[]{indexP1, indexP2, indexP3, indexP4, indexP5}) + 1);
            if (indexP1 == -1) {
                break;
            }

            indexP2 = text.indexOf(MarkdownConstant.IMAGE_P2, indexP1 + 1);
            if (indexP2 == -1) {
                break;
            }

            int tmpIndexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP1 + 1);
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP2) {
                indexP2 = -1;
                continue;
            }

            //check for ![ i.e no character in between ![
            if (indexP1 + 1 != indexP2) {
                indexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP2 + 1);
                continue;
            }

            indexP3 = text.indexOf(MarkdownConstant.IMAGE_P3, indexP2 + 1);
            if (indexP3 == -1) {
                break;
            }

            tmpIndexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP1 + 1);
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP3) {
                indexP3 = -1;
                continue;
            }

            int tmpIndexP2 = text.indexOf(MarkdownConstant.IMAGE_P2, indexP2 + 1);
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP3) {
                indexP3 = -1;
                continue;
            }

            String hyper = text.substring(indexP2 + 1, indexP3);
            if (Utils.isNullOrEmpty(hyper)) {
                continue;
            }

            indexP4 = text.indexOf(MarkdownConstant.IMAGE_P4, indexP3);
            if (indexP4 == -1) {
                break;
            }

            tmpIndexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP1 + 1);
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP4) {
                indexP4 = -1;
                continue;
            }

            tmpIndexP2 = text.indexOf(MarkdownConstant.IMAGE_P2, indexP2 + 1);
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP4) {
                indexP4 = -1;
                continue;
            }

            int tmpIndexP3 = text.indexOf(MarkdownConstant.IMAGE_P3, indexP3 + 1);
            if (tmpIndexP3 != -1 && tmpIndexP3 < indexP4) {
                indexP4 = -1;
                continue;
            }

            //check for ]( i.e no character in between ](
            if (indexP3 + 1 != indexP4) {
                indexP3 = text.indexOf(MarkdownConstant.IMAGE_P3, indexP4 + 1);
                continue;
            }

            indexP5 = text.indexOf(MarkdownConstant.IMAGE_P5, indexP4);
            if (indexP5 == -1) {
                break;
            }

            tmpIndexP1 = text.indexOf(MarkdownConstant.IMAGE_P1, indexP1 + 1);
            if (tmpIndexP1 != -1 && tmpIndexP1 < indexP5) {
                indexP5 = -1;
                continue;
            }

            tmpIndexP2 = text.indexOf(MarkdownConstant.IMAGE_P2, indexP2 + 1);
            if (tmpIndexP2 != -1 && tmpIndexP2 < indexP5) {
                indexP5 = -1;
                continue;
            }

            tmpIndexP3 = text.indexOf(MarkdownConstant.IMAGE_P3, indexP3 + 1);
            if (tmpIndexP3 != -1 && tmpIndexP3 < indexP5) {
                indexP5 = -1;
                continue;
            }

            int tmpIndexP4 = text.indexOf(MarkdownConstant.IMAGE_P4, indexP4 + 1);
            if (tmpIndexP4 != -1 && tmpIndexP4 < indexP5) {
                indexP5 = -1;
                continue;
            }

            String link = text.substring(indexP4 + 1, indexP5);
            if (Utils.isNullOrEmpty(link)) {
                continue;
            }

            //<a href="http://google.com">Google!</a>
            //Go to [Google](http://google.com) and lookup

            if (!link.startsWith("www.") && !link.startsWith("http://")) {
                link = "www." + link;
            }

            if (!link.startsWith("http://")) {
                link = "http://" + link;
            }

            StringBuilder builder = new StringBuilder(text);
            builder.delete(indexP1, indexP5 + 1);
            String hyperLink = String.format(MarkdownConstant.IMAGE_FORMAT, link, hyper);
            builder.insert(indexP1, hyperLink);
            text = builder.toString();

        } while (true);

        return text;
    }

    /**
     * Convert * space text to <ul><li>...</li></ul>
     * Convert # space text to <ol><li>...</li></ol>
     */
    private static String markDownList(String text, char delimiter, String itemFormat, String itemContainerFormat) {

        int indexP1 = -1;
        int indexP2 = -1;

        boolean exit;

        StringBuilder itemBuilder = new StringBuilder();

        do {

            int itemStartOffset = -1;
            int itemEndOffset = -1;

            while (true) {
                indexP1 = text.indexOf(delimiter, NumberUtils.max(new int[]{indexP1, indexP2}) + 1);

                if (indexP1 == -1) {
                    exit = true;
                    break;
                }

                if (text.charAt(indexP1 + 1) != ' ') {
                    continue;
                }

                indexP2 = text.indexOf(MarkdownConstant.NEW_LINE, indexP1 + 1);
                if (indexP2 == -1) {
                    exit = true;
                    break;
                }

                itemEndOffset = indexP2;
                String item = text.substring(indexP1 + 2, indexP2);
                if (Utils.isNullOrEmpty(item)) {
                    continue;
                }

                if (itemBuilder.length() == 0) {
                    itemStartOffset = indexP1;
                }

                itemBuilder.append(String.format(itemFormat, item));
            }

            if (itemStartOffset == -1) {
                continue;
            }

            StringBuilder builder = new StringBuilder(text);
            builder.delete(itemStartOffset, itemEndOffset);
            String itemList = String.format(itemContainerFormat, itemBuilder);
            builder.insert(itemStartOffset, itemList);
            text = builder.toString();

        } while (!exit);

        return text;
    }

    /**
     * Convert #h1 to <h1> and so on till 6
     */
    private static String markDownHeading(String text) {

        int index;

        do {
            index = text.indexOf(MarkdownConstant.HEADING_P);
            if (index == -1) {
                break;
            }

            StringBuilder builder = new StringBuilder(text);
            char c = text.charAt(index+2);
            if (Character.isDigit(c) && Integer.parseInt(c + "") <= 6) {


                builder.delete(index, index + 3);

                switch (text.charAt(index + 2)) {
                    case '1':
                        builder.insert(index, "<h1>");
                        break;

                    case '2':
                        builder.insert(index, "<h2>");
                        break;

                    case '3':
                        builder.insert(index, "<h3>");
                        break;

                    case '4':
                        builder.insert(index, "<h4>");
                        break;

                    case '5':
                        builder.insert(index, "<h5>");
                        break;

                    case '6':
                        builder.insert(index, "<h6>");
                        break;

                }

                text = builder.toString();
            }else{
                break;
            }
        } while (true);

        return text;
    }
}
