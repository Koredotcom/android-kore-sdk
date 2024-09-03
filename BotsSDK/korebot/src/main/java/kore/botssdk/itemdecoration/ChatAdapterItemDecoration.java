package kore.botssdk.itemdecoration;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.adapter.ChatAdapter;

public class ChatAdapterItemDecoration extends RecyclerView.ItemDecoration {

    public static final int firstItemMarginTop = (int) (12 * dp1);
    public static final int commonVerticalMargin = (int) (6 * dp1);
    public static final int messageMargin = (int) (40 * dp1);
    public static final int requestMsgMarginRight = (int) (4 * dp1);
    public static final int responseMarginLeft = (int) (4 * dp1);

    public ChatAdapterItemDecoration() {
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        ChatAdapter adapter = (ChatAdapter) parent.getAdapter();
        if (adapter == null) return;
        if (position < 0 || adapter.getItemCount() <= position) return;

        outRect.top = position == 0 ? firstItemMarginTop : commonVerticalMargin;
        outRect.bottom = commonVerticalMargin;

        if (adapter.getItemType(position) == ChatAdapter.TEMPLATE_BUBBLE_REQUEST) {
            outRect.left = messageMargin;
            outRect.right = requestMsgMarginRight;
        } else {
            outRect.right = messageMargin;
            outRect.left = responseMarginLeft;
        }
    }
}