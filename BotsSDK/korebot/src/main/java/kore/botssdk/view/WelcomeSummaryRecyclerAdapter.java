package kore.botssdk.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.BR;
import kore.botssdk.R;
import kore.botssdk.databinding.WelcomeSummaryListItemBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.WelcomeChatSummaryModel;
import kore.botssdk.utils.StringUtils;

public class WelcomeSummaryRecyclerAdapter extends RecyclerView.Adapter<WelcomeSummaryRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<WelcomeChatSummaryModel> summaryList;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public WelcomeSummaryRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WelcomeSummaryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WelcomeSummaryListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.welcome_summary_list_item, parent, false);
        return new WelcomeSummaryRecyclerAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WelcomeSummaryRecyclerAdapter.ViewHolder holder, int position) {
        WelcomeChatSummaryModel model = summaryList.get(position);
        holder.bind(model);

        if(!StringUtils.isNullOrEmpty(model.getIconId()))
            setImage(model, holder);
        else{
            holder.itemRowBinding.widgetSummaryTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tasks_vector,0,0,0);
        }

        holder.itemRowBinding.summaryRootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                verticalListViewActionHelper.welcomeSummaryItemClick(model);
            }
        });

        /*if(model.getIconId().equalsIgnoreCase("meeting")){
            holder.itemRowBinding.widgetSummaryTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon, 0, 0, 0);
        }*/

        /*if(!StringUtils.isNullOrEmpty(model.getSummary())) {
            holder.itemRowBinding.widgetSummaryTv.setText(summary);//setTypeface(ResourcesCompat.getFont(context, R.font.icomoon));
        }*/
    }

    private void setImage(WelcomeChatSummaryModel mdl, ViewHolder holder){
        switch(mdl.getIconId()){
            case "meeting":
                holder.itemRowBinding.widgetSummaryTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.widget_calender,0,0,0);//R.drawable.widget_calender;
                break;
            case "form":
                holder.itemRowBinding.widgetSummaryTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_notification_active,0,0,0);
                break;
            case "overdue":
                holder.itemRowBinding.widgetSummaryTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_overdue,0,0,0);
                break;
            case "email":
                holder.itemRowBinding.widgetSummaryTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_summary,0,0,0);
                break;
            default:
                holder.itemRowBinding.widgetSummaryTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tasks_vector,0,0,0);
        }
    }

    @Override
    public int getItemCount() {
        return summaryList != null && summaryList.size() > 0 ? summaryList.size() : 0;
    }

    @Override
    public ArrayList getData() {
        return null;
    }

    @Override
    public void setData(ArrayList data) {
        this.summaryList= new ArrayList<WelcomeChatSummaryModel>(data);
        notifyDataSetChanged();
    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
            this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public WelcomeSummaryListItemBinding itemRowBinding;

        public ViewHolder(WelcomeSummaryListItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.welcomeSummaryList, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
