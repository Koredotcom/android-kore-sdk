package kore.botssdk.viewholders;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.MiniTablePagerAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;

public class MiniTableTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;
    private final MiniTablePagerAdapter adapter;

    public static MiniTableTemplateHolder getInstance(ViewGroup parent) {
        return new MiniTableTemplateHolder(createView(R.layout.template_mini_table, parent));
    }

    private MiniTableTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        recyclerView = itemView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new MiniTablePagerAdapter(itemView.getContext());
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10 * (int) dp1));
        adapter.populateData(payloadInner.getMiniTableDataModels(), isLastItem());
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = space;
            }

            int position = parent.getChildAdapterPosition(view);
            if (position != RecyclerView.NO_POSITION) {
                outRect.right = space; // Add spacing between items
            }
        }
    }
}
