package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.databinding.EmailLookupViewBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.EmailModel;

/**
 * Created by Shiva Krishna Kongara on 06-feb-19.
 */

public class KoraEmailRecyclerAdapter extends RecyclerView.Adapter<KoraEmailRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {

    private final Context context;
    private ArrayList<EmailModel> emailModels;
    private boolean isExpanded;
    private VerticalListViewActionHelper verticalListViewActionHelper;

    public KoraEmailRecyclerAdapter(ArrayList<EmailModel> emailModels, Context context) {
        this.emailModels = emailModels;
        this.context = context;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.email_lookup_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.emailLookupViewBinding.setEmailModel(emailModels.get(position));
        if(position == emailModels.size()-1 && emailModels.size()<3)
            holder.emailLookupViewBinding.divider.setVisibility(View.GONE);
        holder.emailLookupViewBinding.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailModel emailModel = emailModels.get(holder.getBindingAdapterPosition());
                if (emailModel.getButtons() == null || emailModel.getButtons().size() == 0) return;
                BotCaourselButtonModel botCaourselButtonModel = emailModels.get(holder.getBindingAdapterPosition()).getButtons().get(0);
                verticalListViewActionHelper.emailItemClicked(botCaourselButtonModel.getAction(),botCaourselButtonModel.getCustomData());
            }
        });
    }

    @Override
    public int getItemCount() {
        return emailModels != null ? (!isExpanded && emailModels.size() > 3 ? 3 : emailModels.size()) : 0;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    @Override
    public ArrayList getData() {
        return emailModels;
    }

    @Override
    public void setData(ArrayList data) {
        emailModels = data;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final EmailLookupViewBinding emailLookupViewBinding;

        public ViewHolder(@NonNull EmailLookupViewBinding itemView) {
            super(itemView.getRoot());
            this.emailLookupViewBinding = itemView;
        }
    }

}
