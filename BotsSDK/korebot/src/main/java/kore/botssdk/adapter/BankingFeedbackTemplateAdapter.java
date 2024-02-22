package kore.botssdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.FeedbackExperienceUpdateListner;
import kore.botssdk.models.FeedbackExperienceContentModel;
import kore.botssdk.utils.KaFontUtils;

public class BankingFeedbackTemplateAdapter extends BaseAdapter
{
    private final Context context;
    ArrayList<FeedbackExperienceContentModel> feedbackExperienceContentModels;
    final FeedbackExperienceUpdateListner feedbackExperienceUpdateListner;
    private final boolean isEnabled;

    public BankingFeedbackTemplateAdapter(@NonNull Context context, @NonNull ArrayList<FeedbackExperienceContentModel> feedbackExperienceContentModels, @NonNull FeedbackExperienceUpdateListner feedbackExperienceUpdateListner, boolean isEnabled)
    {
        this.context = context;
        this.feedbackExperienceContentModels = feedbackExperienceContentModels;
        this.feedbackExperienceUpdateListner = feedbackExperienceUpdateListner;
        this.isEnabled = isEnabled;
    }

    @Override
    public int getCount()
    {
        return feedbackExperienceContentModels.size();
    }

    @Override
    public Object getItem(int position)
    {
        return feedbackExperienceContentModels.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ExperienceItemViewHolder holder;
        if (convertView == null)
        {
            convertView = View.inflate(context, R.layout.banking_feedback_template_cell, null);
            KaFontUtils.applyCustomFont(context, convertView);
            holder = new ExperienceItemViewHolder();
            holder.tvExperience = convertView.findViewById(R.id.tvExperience);
            holder.rbSelect = convertView.findViewById(R.id.rbSelect);
            holder.llExperience = convertView.findViewById(R.id.llExperience);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ExperienceItemViewHolder) convertView.getTag();
        }

        holder.rbSelect.setClickable(false);
        holder.rbSelect.setEnabled(false);

        if(isEnabled)
        {
            holder.rbSelect.setClickable(true);
            holder.rbSelect.setEnabled(true);

            holder.llExperience.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    feedbackExperienceUpdateListner.updateExperienceList(position, feedbackExperienceContentModels);
                }
            });

            holder.rbSelect.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    feedbackExperienceUpdateListner.updateExperienceList(position, feedbackExperienceContentModels);
                }
            });
        }

        FeedbackExperienceContentModel dataObj = feedbackExperienceContentModels.get(position);
        holder.tvExperience.setText(dataObj.getValue());
        holder.rbSelect.setTag(dataObj);

        holder.rbSelect.setChecked(dataObj.getChecked());

        return convertView;
    }

    public void refresh(@NonNull ArrayList<FeedbackExperienceContentModel> feedbackExperienceContentModels)
    {
        this.feedbackExperienceContentModels = feedbackExperienceContentModels;
        notifyDataSetChanged();
    }

    static class ExperienceItemViewHolder {
        TextView tvExperience;
        CheckBox rbSelect;
        LinearLayout llExperience;
    }
}
