package kore.botssdk.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;

/**
 * Created by Pradeep Mahato on 28/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class QuickReplyViewHolder extends RecyclerView.ViewHolder{
    private final TextView quickReplyTitle;
    private final ImageView quickReplyImage;
    private final LinearLayout quickReplyRoot;

    public QuickReplyViewHolder(View view) {
        super(view);
        quickReplyImage = view.findViewById(R.id.quick_reply_item_image);
        quickReplyTitle = view.findViewById(R.id.quick_reply_item_text);
        quickReplyRoot = view.findViewById(R.id.quick_reply_item_root);
    }

    public TextView getQuickReplyTitle() {
        return quickReplyTitle;
    }

    public ImageView getQuickReplyImage() {
        return quickReplyImage;
    }

    public LinearLayout getQuickReplyRoot() {
        return quickReplyRoot;
    }
}
