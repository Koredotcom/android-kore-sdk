package com.kore.findlysdk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.FilterResultsListner;
import com.kore.findlysdk.models.SearchFacetsBucketsModel;
import com.kore.findlysdk.models.SearchFacetsModel;
import com.kore.findlysdk.view.AutoExpandListView;

import java.util.ArrayList;

public class FacetsFilterAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<SearchFacetsModel> searchFacets;
    private LayoutInflater ownLayoutInflater = null;
    private AutoExpandListView alvFilters;
    private FilterResultsListner filterResultsListner;
    private ArrayList<SearchFacetsBucketsModel> appliedFilters = new ArrayList<>();

    public FacetsFilterAdapter(Context context, ArrayList<SearchFacetsModel> searchFacets, FilterResultsListner filterResultsListner, ArrayList<SearchFacetsBucketsModel> appliedFilters)
    {
        this.mContext = context;
        this.searchFacets = searchFacets;
        this.filterResultsListner = filterResultsListner;
        this.appliedFilters = appliedFilters;
        ownLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return searchFacets.size();
    }

    @Override
    public Object getItem(int i) {
        return searchFacets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        if (convertView == null) {
            convertView = ownLayoutInflater.inflate(R.layout.facets_filter_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        populateView(holder, position);

        return convertView;
    }

    public void refresh(ArrayList<SearchFacetsBucketsModel> appliedFilters)
    {
        this.appliedFilters = appliedFilters;

//        for (int i = 0; i < appliedFilters.size(); i++)
//        {
//            appliedFilters.get(i).setFieldName("");
//        }

        this.notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView tvFacetsFilterName;
        AutoExpandListView alvFilters;
    }

    private void populateView(ViewHolder holder, int position) {
        SearchFacetsModel buttonTemplate = (SearchFacetsModel)getItem(position);

        holder.tvFacetsFilterName.setText(buttonTemplate.getName());
        holder.alvFilters.setAdapter(new FacetsFilterBucketAdapter(mContext, buttonTemplate.getBuckets(), filterResultsListner, buttonTemplate.getFieldName(), buttonTemplate.getName(), appliedFilters));
        holder.tvFacetsFilterName.setTypeface(null, Typeface.BOLD);
    }

    private void initializeViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvFacetsFilterName = (TextView) view.findViewById(R.id.tvFacetsFilterName);
        viewHolder.alvFilters = (AutoExpandListView) view.findViewById(R.id.alvFilters);
        view.setTag(viewHolder);
    }
}
