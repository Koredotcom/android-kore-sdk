package com.kore.ai.widgetsdk.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;

public class SkillWidgetViewHolder extends RecyclerView.ViewHolder {

public  TextView item_text;
    public SkillWidgetViewHolder(@NonNull View itemView) {
        super(itemView);
        item_text=itemView.findViewById(R.id.item_text);
    }
}
