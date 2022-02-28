package com.kore.findlysdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.listners.TabFacetClickListner;
import com.kore.findlysdk.models.TabFacetsModel;

import java.util.ArrayList;

public class TabFacetsAdapter extends RecyclerView.Adapter<TabFacetsAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<TabFacetsModel.TabsModel> model;
    private TabFacetClickListner tabFacetClickListner;

    public TabFacetsAdapter(Context context, ArrayList<TabFacetsModel.TabsModel> model, TabFacetClickListner tabFacetClickListner) {
        this.model = model;
        this.context = context;
        this.tabFacetClickListner = tabFacetClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.tab_facet_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TabFacetsModel.TabsModel liveSearchResultsModel = model.get(position);

        if (liveSearchResultsModel != null)
        {
            holder.tvTabFacetName.setText(liveSearchResultsModel.getBucketName()+" ("+liveSearchResultsModel.getBucketCount()+")");
        }

        holder.tvTabFacetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabFacetClickListner.tabFacetClicked(liveSearchResultsModel.getFieldValue());
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTabFacetName;
        ImageView ivTabFacet;
        LinearLayout llTabFacet;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvTabFacetName = (TextView) itemView.findViewById(R.id.tvTabFacetName);
            this.ivTabFacet = (ImageView) itemView.findViewById(R.id.ivTabFacet);
            this.llTabFacet = (LinearLayout) itemView.findViewById(R.id.llTabFacet);
        }
    }
}
