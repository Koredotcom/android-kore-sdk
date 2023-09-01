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
import kore.botssdk.databinding.ActionItemLayoutBinding;
import kore.botssdk.models.BotButtonModel;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<BotButtonModel> botButtonModels;
    private final Drawable drawable;
    private final ActionHelper actionHelper;

    public ActionsAdapter(Context context, ArrayList<BotButtonModel> botButtonModels, Drawable drawable,ActionHelper actionHelper) {
        this.context = context;
        this.botButtonModels = botButtonModels;
        this.drawable = drawable;
        this.actionHelper = actionHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActionItemLayoutBinding actionItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.action_item_layout, parent, false);
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
        final ActionItemLayoutBinding actionItemLayoutBinding;

        public ViewHolder(@NonNull ActionItemLayoutBinding actionItemLayoutBinding) {
            super(actionItemLayoutBinding.getRoot());
            this.actionItemLayoutBinding = actionItemLayoutBinding;
        }
    }

    public interface ActionHelper{
        void actionItemClicked(Object botButtonModel);
    }
}
