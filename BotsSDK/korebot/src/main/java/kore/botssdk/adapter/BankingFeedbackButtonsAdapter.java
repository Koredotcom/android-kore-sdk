package kore.botssdk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.FeedbackExperienceUpdateListner;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.utils.BundleConstants;

public class BankingFeedbackButtonsAdapter extends RecyclerView.Adapter<BankingFeedbackButtonsAdapter.ViewHolder>
{
    private final ArrayList<BotButtonModel> botButtonModels;
    final FeedbackExperienceUpdateListner feedbackExperienceUpdateListner;
    private final boolean isEnabled;

    public BankingFeedbackButtonsAdapter(@NonNull ArrayList<BotButtonModel> botButtonModels, @NonNull FeedbackExperienceUpdateListner feedbackExperienceUpdateListner, boolean isEnabled) {
        this.botButtonModels = botButtonModels;
        this.feedbackExperienceUpdateListner = feedbackExperienceUpdateListner;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public BankingFeedbackButtonsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.banking_feedback_button_cell, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull BankingFeedbackButtonsAdapter.ViewHolder holder, int position) {
        BotButtonModel botListModel = botButtonModels.get(position);
        holder.tvButton.setText(botListModel.getLabel());

        if(holder.tvButton.getText().toString().equalsIgnoreCase(BundleConstants.BUTTON_TYPE_CONFIRM) && isEnabled)
        {
            holder.tvButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedbackExperienceUpdateListner.sendFeedback();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return botButtonModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvButton = itemView.findViewById(R.id.tvButton);
        }
    }
}
