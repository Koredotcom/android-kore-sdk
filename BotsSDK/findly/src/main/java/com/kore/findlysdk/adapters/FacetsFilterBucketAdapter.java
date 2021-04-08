package com.kore.findlysdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.FilterResultsListner;
import com.kore.findlysdk.models.MultiSelectBase;
import com.kore.findlysdk.models.SearchFacetsBucketsModel;
import com.kore.findlysdk.models.SearchFacetsModel;
import com.kore.findlysdk.view.AutoExpandListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FacetsFilterBucketAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<SearchFacetsBucketsModel> searchFacets;
    private LayoutInflater ownLayoutInflater = null;
    private AutoExpandListView alvFilters;
    private FilterResultsListner filterResultsListner;
    private ArrayList<SearchFacetsBucketsModel> checkedItems = new ArrayList<>();
    private String fieldName, facetType;
    private ArrayList<SearchFacetsBucketsModel> appliedFilters;

    public FacetsFilterBucketAdapter(Context context, ArrayList<SearchFacetsBucketsModel> searchFacets, FilterResultsListner filterResultsListner, String fieldName, String facetType, ArrayList<SearchFacetsBucketsModel> appliedFilters)
    {
        this.mContext = context;
        this.searchFacets = searchFacets;
        this.filterResultsListner = filterResultsListner;
        this.fieldName = fieldName;
        this.facetType = facetType;
        ownLayoutInflater = LayoutInflater.from(context);
        this.appliedFilters = appliedFilters;

        if(appliedFilters != null && appliedFilters.size() > 0)
        {
            checkedItems = new ArrayList<>();

            for (int i = 0; i <appliedFilters.size(); i++)
            {
                if(appliedFilters.get(i).getFieldName().equalsIgnoreCase(fieldName))
                {
                    checkedItems.add(appliedFilters.get(i));
                }

            }
        }
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
            convertView = ownLayoutInflater.inflate(R.layout.facets_filter_bucket_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        populateView(holder, position);

        return convertView;
    }

    public static class ViewHolder {
        TextView tvFacetsFilterName;
        CheckBox checkBox;
    }

    private void populateView(ViewHolder holder, int position)
    {
        SearchFacetsBucketsModel buttonTemplate = (SearchFacetsBucketsModel)getItem(position);

        holder.tvFacetsFilterName.setText(buttonTemplate.getKey()+" ("+buttonTemplate.getDoc_count()+")");

        holder.checkBox.setChecked(buttonTemplate.getChecked());
        holder.checkBox.setTag(buttonTemplate);
        holder.checkBox.setOnClickListener(itemSelectionListener);
    }

    private View.OnClickListener itemSelectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            SearchFacetsBucketsModel item = (SearchFacetsBucketsModel) v.getTag();
            if (!checkedItems.contains(item))
            {
                item.setChecked(true);
                checkedItems.add(item);
            }
            else
            {
                item.setChecked(false);
                checkedItems.remove(item);
                ((CheckBox) v).setChecked(false);
            }

            item.setFieldName(fieldName);
            filterResultsListner.onFilterSelected(checkedItems, fieldName, facetType);
        }
    };

    private void initializeViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvFacetsFilterName = (TextView) view.findViewById(R.id.tvFacetsFilterName);
        viewHolder.checkBox = (CheckBox) view.findViewById(R.id.check_multi_item);
        view.setTag(viewHolder);
    }
}
