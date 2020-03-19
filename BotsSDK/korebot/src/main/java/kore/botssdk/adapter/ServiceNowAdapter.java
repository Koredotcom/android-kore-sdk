package kore.botssdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.UniversalSearchSkillModel;
import kore.botssdk.view.viewHolder.KnowledgeCollectionViewHolder;
import kore.botssdk.view.viewHolder.ServiceNowViewHolder;

public class ServiceNowAdapter extends RecyclerView.Adapter<ServiceNowViewHolder> implements RecyclerViewDataAccessor {

    Context context;
    ArrayList<UniversalSearchSkillModel> modelData;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public ServiceNowAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ServiceNowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.us_servicenow_layout, parent, false);
        return new ServiceNowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceNowViewHolder holder, int position) {
        UniversalSearchSkillModel model = modelData.get(position);
        String descrip=model.getDesc();
        String skillTitle=model.getTitle();
        holder.header.setVisibility(View.GONE);
        holder.title.setText(skillTitle);
        holder.body.setText(descrip);
        holder.service_now_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getDefault_action() != null&&model.getDefault_action().getUrl()!=null) {
                    Intent intent = new Intent(context, GenericWebViewActivity.class);
                    intent.putExtra("url", model.getDefault_action().getUrl());
                    intent.putExtra("header", context.getResources().getString(kore.botssdk.R.string.app_name));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelData!=null?modelData.size():0;
    }


    @Override
    public ArrayList getData() {
        return modelData;
    }

    @Override
    public void setData(ArrayList modelData) {
        this.modelData = modelData;
        notifyDataSetChanged();
    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }
}