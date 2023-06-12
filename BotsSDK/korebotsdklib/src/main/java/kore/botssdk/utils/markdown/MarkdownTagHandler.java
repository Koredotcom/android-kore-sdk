package kore.botssdk.utils.markdown;

import android.text.Editable;
import android.text.Html;

import org.xml.sax.XMLReader;

/**
 * Created by AmitYadav on 1/12/2017.
 */

public class MarkdownTagHandler implements Html.TagHandler {

    private boolean first = true;
    private String parent = null;
    private int index = 1;

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        if (tag.equals("ul")) {
            parent = "ul";

        } else if (tag.equals("ol")) {
            parent = "ol";
        }

        if (tag.equals("li")) {
            if (parent.equals("ul")) {
                if (first) {
                    output.append(MarkdownConstant.NEW_LINE + "\t• ");
                    first = false;
                } else {
                    first = true;
                }
            } else {
                if (first) {
                    output.append(MarkdownConstant.NEW_LINE + "\t" + index + ". ");
                    first = false;
                    index++;
                } else {
                    first = true;
                }
            }
        }
    }
}