package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.databinding.ActionItemLayoutBinding;
import kore.botssdk.databinding.WactionItemLayoutBinding;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.MultiAction;

public class WActionsAdapter extends RecyclerView.Adapter<WActionsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MultiAction> botButtonModels;
    private Drawable drawable;
    private ActionsAdapter.ActionHelper actionHelper;

    public WActionsAdapter(Context context, ArrayList<MultiAction> botButtonModels, Drawable drawable, ActionsAdapter.ActionHelper actionHelper) {
        this.context = context;
        this.botButtonModels = botButtonModels;
        this.drawable = drawable;
        this.actionHelper = actionHelper;
    }

    @NonNull
    @Override
    public WActionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WactionItemLayoutBinding actionItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.waction_item_layout, parent, false);
        actionItemLayoutBinding.getRoot().setBackground(drawable);
        return new ViewHolder(actionItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WActionsAdapter.ViewHolder holder, int position) {
        holder.actionItemLayoutBinding.setAction(botButtonModels.get(position));
        holder.actionItemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionHelper.actionItemClicked(botButtonModels.get(position));
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
