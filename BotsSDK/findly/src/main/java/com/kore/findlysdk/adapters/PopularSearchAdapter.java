package com.kore.findlysdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.models.PopularSearchModel;

import java.util.ArrayList;

public class PopularSearchAdapter extends RecyclerView.Adapter<PopularSearchAdapter.PopularItemViewHolder>
{
    private Context mContext;
    private ArrayList<PopularSearchModel> arrPopularSearchModels;
    private ComposeFooterInterface composeFooterInterface;

    public PopularSearchAdapter(Context context, ArrayList<PopularSearchModel> arrPopularSearchModels)
    {
        this.mContext = context;
        this.arrPopularSearchModels = arrPopularSearchModels;
    }

    @NonNull
    @Override
    public PopularItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popular_search_findly_item, parent, false);
        return new PopularSearchAdapter.PopularItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularItemViewHolder holder, int position) {
        final PopularSearchModel popularSearchModel = arrPopularSearchModels.get(position);

        if(popularSearchModel != null)
        {
            holder.tvPopulatItem.setText(popularSearchModel.getQuery());

            holder.llPopularSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    composeFooterInterface.onSendClick(popularSearchModel.getQuery(), popularSearchModel.getQuery(),false);
                }
            });
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrPopularSearchModels.size();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface)
    {
        this.composeFooterInterface = composeFooterInterface;
    }

    class PopularItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvPopulatItem;
        LinearLayout llPopularSearch;

        public PopularItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPopulatItem = itemView.findViewById(R.id.tvPopularItem);
            llPopularSearch = itemView.findViewById(R.id.llPopularSearch);
        }
    }
}
