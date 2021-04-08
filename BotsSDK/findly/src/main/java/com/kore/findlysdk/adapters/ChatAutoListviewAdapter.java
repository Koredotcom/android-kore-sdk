package com.kore.findlysdk.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.activity.FullResultsActivity;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.SearchModel;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;

public class ChatAutoListviewAdapter extends BaseAdapter
{
    private ArrayList<SearchModel> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ArrayList<LiveSearchResultsModel> arrTempResults;
    private ArrayList<LiveSearchResultsModel> arrTempAllResults;

    public ChatAutoListviewAdapter (Context context, ArrayList<SearchModel> model, InvokeGenericWebViewInterface invokeGenericWebViewInterface)
    {
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int i) {
        return model.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        ChatAdapterViewHolder chatAdapterViewHolder;

        if (convertView == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.chat_list_findly_item, null);
            chatAdapterViewHolder = new ChatAdapterViewHolder();
            chatAdapterViewHolder.tvUserMessage = (TextView) convertView.findViewById(R.id.tvUserMessage);
            chatAdapterViewHolder.tvBotMessage = (TextView) convertView.findViewById(R.id.tvBotMessage);
            chatAdapterViewHolder.llLiveSearch = (RelativeLayout) convertView.findViewById(R.id.llLiveSearch);
            chatAdapterViewHolder.lvLiveSearch = (RecyclerView) convertView.findViewById(R.id.lvLiveSearch);
            chatAdapterViewHolder.tvSeeAllResults = (TextView) convertView.findViewById(R.id.tvSeeAllResults);
            chatAdapterViewHolder.lvLiveSearch.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            convertView.setTag(chatAdapterViewHolder);
        } else {
            chatAdapterViewHolder = (ChatAdapterViewHolder) convertView.getTag();
        }

        final SearchModel liveSearchResultsModel = model.get(position);

        if(liveSearchResultsModel != null)
        {
            if(liveSearchResultsModel.getLeft())
            {
                chatAdapterViewHolder.tvUserMessage.setVisibility(View.VISIBLE);
                chatAdapterViewHolder.tvBotMessage.setVisibility(View.GONE);
                chatAdapterViewHolder.llLiveSearch.setVisibility(View.GONE);
                chatAdapterViewHolder.tvUserMessage.setText(liveSearchResultsModel.getTitle());
            }
            else
            {
                chatAdapterViewHolder.tvBotMessage.setVisibility(View.GONE);
                chatAdapterViewHolder.tvUserMessage.setVisibility(View.GONE);
                chatAdapterViewHolder.llLiveSearch.setVisibility(View.VISIBLE);

                arrTempAllResults = new ArrayList<>();

                if(liveSearchResultsModel.getTemplate().getResults().getFaq() != null &&
                        liveSearchResultsModel.getTemplate().getResults().getFaq().size() > 0)
                    arrTempAllResults.addAll(liveSearchResultsModel.getTemplate().getResults().getFaq());

                if(liveSearchResultsModel.getTemplate().getResults().getPage() != null &&
                        liveSearchResultsModel.getTemplate().getResults().getPage().size() > 0)
                    arrTempAllResults.addAll(liveSearchResultsModel.getTemplate().getResults().getPage());

                if(liveSearchResultsModel.getTemplate().getResults().getPage() != null &&
                        liveSearchResultsModel.getTemplate().getResults().getPage().size() > 0)
                    arrTempAllResults.addAll(liveSearchResultsModel.getTemplate().getResults().getTask());

                if(arrTempAllResults != null && arrTempAllResults.size() > 0)
                    chatAdapterViewHolder.lvLiveSearch.setAdapter( new LiveSearchCyclerAdapter(context, getTopFourList(arrTempAllResults), 1, invokeGenericWebViewInterface, null));
                else
                    chatAdapterViewHolder.llLiveSearch.setVisibility(View.GONE);

                chatAdapterViewHolder.tvSeeAllResults.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(arrTempAllResults != null && arrTempAllResults.size() > 0)
                        {
                            Intent intent = new Intent(context, FullResultsActivity.class);
                            intent.putExtra("Results", arrTempAllResults);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }

        return convertView;
    }

    public void refresh(ArrayList<SearchModel> arrLiveSearchResultsModels)
    {
        this.model = arrLiveSearchResultsModels;
        notifyDataSetChanged();
    }

    private class ChatAdapterViewHolder {
        TextView tvUserMessage, tvBotMessage, tvSeeAllResults;
        RelativeLayout llLiveSearch;
        RecyclerView lvLiveSearch;
    }

    private ArrayList<LiveSearchResultsModel> getTopFourList(ArrayList<LiveSearchResultsModel> arrResults)
    {
        arrTempResults = new ArrayList<>();
        for (int i = 0; i < arrResults.size(); i++)
        {
            if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.FAQ))
            {
                arrTempResults.add(arrResults.get(i));
                if(arrTempResults.size() == 2)
                    break;
            }
        }

        if(arrTempResults.size() >= 1)
        {
            int suntoAdd = arrTempResults.size()+2;
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                {
                    arrTempResults.add(arrResults.get(i));
                    if(arrTempResults.size() == suntoAdd)
                        break;
                }
            }
        }
        else
        {
            for (int i = 0; i < arrResults.size(); i++)
            {
                if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                {
                    arrTempResults.add(arrResults.get(i));
                    if(arrTempResults.size() == 2)
                        break;
                }
            }
        }

        return arrTempResults;
    }
}
