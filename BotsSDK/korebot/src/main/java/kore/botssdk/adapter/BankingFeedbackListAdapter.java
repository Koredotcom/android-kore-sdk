package kore.botssdk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.FeedbackExperienceUpdateListner;
import kore.botssdk.models.FeedbackListModel;
import kore.botssdk.utils.KaFontUtils;

@SuppressLint("UnknownNullness")
public class BankingFeedbackListAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<FeedbackListModel> feedbackListModels;
    final ArrayList<FeedbackListModel> checkedItems = new ArrayList<>();
    final FeedbackExperienceUpdateListner feedbackExperienceUpdateListner;
    final boolean isEnabled;

    public BankingFeedbackListAdapter(Context context, ArrayList<FeedbackListModel> feedbackListModels, FeedbackExperienceUpdateListner feedbackExperienceUpdateListner, boolean isEnabled) {
        this.context = context;
        this.feedbackListModels = feedbackListModels;
        this.feedbackExperienceUpdateListner = feedbackExperienceUpdateListner;
        this.isEnabled = isEnabled;
    }

    @Override
    public int getCount() {
        return feedbackListModels.size();
    }

    @Override
    public Object getItem(int position) {
        return feedbackListModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExperienceItemViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate( context, R.layout.multi_select_item, null);
            KaFontUtils.applyCustomFont(context, convertView);
            holder = new ExperienceItemViewHolder();
            holder.text_view = convertView.findViewById(R.id.text_view);
            holder.check_multi_item = convertView.findViewById(R.id.check_multi_item);
            holder.vDivider = convertView.findViewById(R.id.vDivider);
            convertView.setTag(holder);
        } else {
            holder = (ExperienceItemViewHolder) convertView.getTag();
        }

        holder.vDivider.setVisibility(View.GONE);
        FeedbackListModel dataObj = (FeedbackListModel) getItem(position);
        holder.text_view.setText(dataObj.getValue());
        holder.check_multi_item.setTag(dataObj);
        holder.check_multi_item.setClickable(false);
        holder.check_multi_item.setEnabled(false);

        if (isEnabled) {
            holder.check_multi_item.setClickable(true);
            holder.check_multi_item.setEnabled(true);

            holder.check_multi_item.setOnClickListener(itemSelectionListener);
        }

        holder.check_multi_item.setChecked(dataObj.getChecked());

        return convertView;
    }

    private final View.OnClickListener itemSelectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isEnabled) {
                FeedbackListModel item = (FeedbackListModel) v.getTag();
                if (((CompoundButton) v).isChecked()) {
                    item.setChecked(true);
                    checkedItems.add(item);
                } else {
                    item.setChecked(false);
                    checkedItems.remove(item);
                }
                feedbackExperienceUpdateListner.updateFeedbackList(checkedItems);
            }
        }
    };

    static class ExperienceItemViewHolder {
        TextView text_view;
        AppCompatCheckBox check_multi_item;
        View vDivider;
    }
}
