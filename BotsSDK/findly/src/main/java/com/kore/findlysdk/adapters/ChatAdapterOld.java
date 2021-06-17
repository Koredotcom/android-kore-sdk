package com.kore.findlysdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.SearchModel;
import com.kore.findlysdk.view.AutoExpandListView;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;

public class ChatAdapterOld extends RecyclerView.Adapter<ChatAdapterOld.ViewHolder>
{
    private ArrayList<SearchModel> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ArrayList<LiveSearchResultsModel> arrTempResults;

    public ChatAdapterOld(Context context, ArrayList<SearchModel> model, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.chat_list_findly_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        final SearchModel liveSearchResultsModel = model.get(position);

        if(liveSearchResultsModel != null)
        {
            if(liveSearchResultsModel.getLeft())
            {
                holder.tvUserMessage.setVisibility(View.VISIBLE);
                holder.tvBotMessage.setVisibility(View.GONE);
                holder.llLiveSearch.setVisibility(View.GONE);
                holder.tvUserMessage.setText(liveSearchResultsModel.getTitle());
            }
            else
            {
                holder.tvBotMessage.setVisibility(View.GONE);
                holder.tvUserMessage.setVisibility(View.GONE);
                holder.llLiveSearch.setVisibility(View.VISIBLE);

                arrTempResults = new ArrayList<>();

                if(liveSearchResultsModel.getTemplate().getResults().getFaq() != null &&
                        liveSearchResultsModel.getTemplate().getResults().getFaq().size() > 0)
                    arrTempResults.addAll(liveSearchResultsModel.getTemplate().getResults().getFaq());

                if(liveSearchResultsModel.getTemplate().getResults().getWeb() != null &&
                        liveSearchResultsModel.getTemplate().getResults().getWeb().size() > 0)
                    arrTempResults.addAll(liveSearchResultsModel.getTemplate().getResults().getWeb());

                if(arrTempResults != null && arrTempResults.size() > 0)
                    holder.lvLiveSearch.setAdapter( new LiveSearchAdaper(context, arrTempResults));
                else
                    holder.llLiveSearch.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserMessage, tvBotMessage;
        RelativeLayout llLiveSearch;
        AutoExpandListView lvLiveSearch;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvUserMessage = (TextView) itemView.findViewById(R.id.tvUserMessage);
            this.tvBotMessage = (TextView) itemView.findViewById(R.id.tvBotMessage);
            this.llLiveSearch = (RelativeLayout) itemView.findViewById(R.id.llLiveSearch);
            this.lvLiveSearch = (AutoExpandListView) itemView.findViewById(R.id.lvLiveSearch);
        }
    }

    public void refresh(ArrayList<SearchModel> arrLiveSearchResultsModels)
    {
        this.model.clear();
        this.model.addAll(arrLiveSearchResultsModels);
//        notifyDataSetChanged();
    }

//    private ArrayList<SearchResultsModel> getTopFourList(ArrayList<LiveSearchResultsModel> arrResults)
//    {
//        arrTempResults = new ArrayList<>();
//        for (int i = 0; i < arrResults.size(); i++)
//        {
//            if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.FAQ))
//            {
//                arrTempResults.add(arrResults.get(i));
//                if(arrTempResults.size() == 2)
//                    break;
//            }
//        }
//
//        if(arrTempResults.size() >= 1)
//        {
//            int suntoAdd = arrTempResults.size()+2;
//            for (int i = 0; i < arrResults.size(); i++)
//            {
//                if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
//                {
//                    arrTempResults.add(arrResults.get(i));
//                    if(arrTempResults.size() == suntoAdd)
//                        break;
//                }
//            }
//        }
//        else
//        {
//            for (int i = 0; i < arrResults.size(); i++)
//            {
//                if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
//                {
//                    arrTempResults.add(arrResults.get(i));
//                    if(arrTempResults.size() == 2)
//                        break;
//                }
//            }
//        }
//
//        return arrTempResults;
//    }
}
