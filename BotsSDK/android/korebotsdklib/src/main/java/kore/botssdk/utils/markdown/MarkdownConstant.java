package kore.botssdk.utils.markdown;

/**
 * Created by AmitYadav on 1/12/2017.
 */

public interface MarkdownConstant {

    char STAR = '*';
    char TILDE = '~';
    char NEW_LINE = '\n';

    String BOLD_HTML_START = "<b>";
    String BOLD_HTML_END = "</b>";

    String ITALIC_HTML_START = "<i>";
    String ITALIC_HTML_END = "</i>";

    String LINK_FORMAT = "<a href=%s>%s</a>";
    char LINK_P1 = '[';
    char LINK_P2 = ']';
    char LINK_P3 = '(';
    char LINK_P4 = ')';

    String IMAGE_FORMAT = "<img src=%s alttext=%s/>";
    char IMAGE_P1 = '!';
    char IMAGE_P2 = '[';
    char IMAGE_P3 = ']';
    char IMAGE_P4 = '(';
    char IMAGE_P5 = ')';

    String UNORDER_LIST_CONTAINER_FORMAT = "<ul>%s</ul>";
    String UNORDER_LIST_ITEM_FORMAT = "<li>%s</li>";
    char UNORDER_LIST_P1 = '*';

    String ORDER_LIST_CONTAINER_FORMAT = "<ol>%s</ol>";
    String ORDER_LIST_ITEM_FORMAT = "<li>%s</li>";
    char ORDER_LIST_P1 = '#';

    String HEADING_P = "#h";

}
