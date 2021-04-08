package com.kore.findlysdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;

public class FilterOptionsAdapter extends RecyclerView.Adapter<FilterOptionsAdapter.ViewHoldeer>
{
    private Context mContext;
    private ImageView img_skill;
    private TextView txtTitle;

    public FilterOptionsAdapter (Context context)
    {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHoldeer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.panel_adapter_new, parent, false);
        return new ViewHoldeer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoldeer holder, int position)
    {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHoldeer extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView img_skill;

        public ViewHoldeer(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            img_skill = itemView.findViewById(R.id.img_skill);
        }
    }
}
