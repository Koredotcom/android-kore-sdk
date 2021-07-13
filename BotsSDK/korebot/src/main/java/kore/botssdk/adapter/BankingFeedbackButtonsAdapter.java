package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.socket.client.On;
import kore.botssdk.R;
import kore.botssdk.listener.FeedbackExperienceUpdateListner;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.Utility;

public class BankingFeedbackButtonsAdapter extends RecyclerView.Adapter<BankingFeedbackButtonsAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<BotButtonModel> botButtonModels;
    private float dp1;
    private FeedbackExperienceUpdateListner feedbackExperienceUpdateListner;
    private boolean isEnabled;

    public BankingFeedbackButtonsAdapter(Context context, ArrayList<BotButtonModel> botButtonModels, FeedbackExperienceUpdateListner feedbackExperienceUpdateListner, boolean isEnabled) {
        this.context = context;
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
        dp1 = Utility.convertDpToPixel(context, 1);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
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
        TextView tvButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvButton = (TextView) itemView.findViewById(R.id.tvButton);
        }
    }
}
