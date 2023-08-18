package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.databinding.WactionItemLayoutBinding;
import kore.botssdk.models.MultiAction;

public class WActionsAdapter extends RecyclerView.Adapter<WActionsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<MultiAction> botButtonModels;
    private final Drawable drawable;
    private final ActionsAdapter.ActionHelper actionHelper;

    public WActionsAdapter(Context context, ArrayList<MultiAction> botButtonModels, Drawable drawable, ActionsAdapter.ActionHelper actionHelper) {
        this.context = context;
        this.botButtonModels = botButtonModels;
        this.drawable = drawable;
        this.actionHelper = actionHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WactionItemLayoutBinding actionItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.waction_item_layout, parent, false);
        actionItemLayoutBinding.getRoot().setBackground(drawable);
        return new ViewHolder(actionItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.actionItemLayoutBinding.setAction(botButtonModels.get(position));
        holder.actionItemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionHelper.actionItemClicked(botButtonModels.get(holder.getBindingAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return botButtonModels == null ? 0 : botButtonModels.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        WactionItemLayoutBinding actionItemLayoutBinding;

        public ViewHolder(@NonNull WactionItemLayoutBinding actionItemLayoutBinding) {
            super(actionItemLayoutBinding.getRoot());
            this.actionItemLayoutBinding = actionItemLayoutBinding;
        }
    }


}
