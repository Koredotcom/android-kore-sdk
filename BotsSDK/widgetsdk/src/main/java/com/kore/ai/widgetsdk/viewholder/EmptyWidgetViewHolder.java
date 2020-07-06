package com.kore.ai.widgetsdk.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;

public class EmptyWidgetViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_icon;
    public TextView tv_disrcription;
    public View rAccViewRootLayout;

    public EmptyWidgetViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_disrcription = itemView.findViewById(R.id.tv_message);
        img_icon = itemView.findViewById(R.id.img_icon);
        rAccViewRootLayout=itemView.findViewById(R.id.rAccViewRootLayout);
    }
}
