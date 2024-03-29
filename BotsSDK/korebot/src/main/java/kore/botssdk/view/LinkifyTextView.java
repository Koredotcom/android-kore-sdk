package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

@SuppressLint("UnknownNullness")
public class LinkifyTextView extends AppCompatTextView {

    public LinkifyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LinkifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinkifyTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setAutoLinkMask(Linkify.ALL);
    }

    /**
     * @Linkify applies to a movementMethod to the textView @LinkMovementMethod. That movement method thought it
     * implements a scrolling vertically method it overrides any other scrolling method the parent has.
     * Although touchEvent can be dispached to the parent, the specific parent ScrollView needed the whole sequence
     * ACTION_DOWN , ACTION_MOVE, ACTION_UP to perform (sweep detection). So the solution to this problem is after
     * applying @Linkify we need to remove the textView's scrolling method and handle the @LinkMovementMethod link
     * detection action in onTouchEvent of the textView.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final TextView widget = this;
        final Object text = widget.getText();
        if (text instanceof Spannable) {
            final Spannable buffer = (Spannable)text;
            final int action = event.getAction();

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                int x = (int)event.getX();
                int y = (int)event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                final Layout layout = widget.getLayout();
                final int line = layout.getLineForVertical(y);
                final int off = layout.getOffsetForHorizontal(line, x);

                final ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else {
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        this.setMovementMethod(null);
    }
}