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
import kore.botssdk.databinding.SummaryHelpListItemBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.WelcomeChatSummaryModel;

public class KoraSummaryHelpRecyclerAdapter extends RecyclerView.Adapter<KoraSummaryHelpRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {

    private final Context context;
    private ArrayList<WelcomeChatSummaryModel> summaryList;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public KoraSummaryHelpRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SummaryHelpListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.summary_help_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WelcomeChatSummaryModel model = summaryList.get(position);
        holder.bind(model);

        holder.itemRowBinding.summaryRootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                verticalListViewActionHelper.welcomeSummaryItemClick(model);
            }
        });
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
        public final SummaryHelpListItemBinding itemRowBinding;

        public ViewHolder(SummaryHelpListItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.summaryList, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
