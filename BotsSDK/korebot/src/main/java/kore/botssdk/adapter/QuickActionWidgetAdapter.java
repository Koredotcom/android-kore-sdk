package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.utils.Constants;

public class QuickActionWidgetAdapter extends RecyclerView.Adapter<QuickActionWidgetAdapter.QuickActionViewHolder> {

    Context context;
    List<String> quickReplyTemplateList;

    public QuickActionWidgetAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public QuickActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.action_action_adapter,parent,false);
        return new QuickActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickActionViewHolder holder, int position) {

        holder.tv_info.setText(quickReplyTemplateList.get(position));

        holder.tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return quickReplyTemplateList != null ? quickReplyTemplateList.size() : 0;
    }

    public void setData(List<String> quickReplyTemplateList) {
        this.quickReplyTemplateList = quickReplyTemplateList;
        notifyDataSetChanged();
    }

    class QuickActionViewHolder extends RecyclerView.ViewHolder {

        TextView tv_info;
        public QuickActionViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_info=itemView.findViewById(R.id.tv_info);
        }
    }
}
