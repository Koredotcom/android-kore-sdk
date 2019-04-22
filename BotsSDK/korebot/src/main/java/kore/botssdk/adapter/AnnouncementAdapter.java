package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.databinding.AnnouncementCardLayoutBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.AnnoucementResModel;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;

public class AnnouncementAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {

    private static final int DATA_FOUND = 1;
    private static final int NO_DATA = 0;
    Context context;

//    public void setData(ArrayList<AnnoucementResModel> data) {
//        this.data = data;
//    }

    ArrayList<AnnoucementResModel> data;
    boolean isViewMore;
    private VerticalListViewActionHelper verticalListViewActionHelper;

    public AnnouncementAdapter(Context context) {
        this.context = context;
    }


    public AnnouncementAdapter(Context activity, ArrayList<AnnoucementResModel> data) {

        context = activity;
        this.data = data;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DATA_FOUND)
            return new AnnouncementAdapter.AnnouncementViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.announcement_card_layout, parent, false));
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.no_hashtag_layout, parent, false);
            return new AnnouncementAdapter.EmptyAnnocementViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data != null && data.size() > 0) {
            return DATA_FOUND;
        }
        return NO_DATA;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AnnouncementViewHolder) {
            holder = (AnnouncementViewHolder) holder;
            AnnoucementResModel annoucementResModel = data.get(position);
            ((AnnouncementViewHolder) holder).binding.setAnnoucement(annoucementResModel);

            ((AnnouncementViewHolder) holder).binding.tvTime.setText(DateUtils.getFormattedSendDateInTimeFormatCoreFunctionality2(context, annoucementResModel.getCreatedOn()));
            ((AnnouncementViewHolder) holder).binding.userProfileName.setCircle(true);
            if (annoucementResModel.getOwner() != null && annoucementResModel.getOwner().getFullName() != null) {
                ((AnnouncementViewHolder) holder).binding.userProfileName.setText(StringUtils.getInitials(annoucementResModel.getOwner().getFullName()));
            }
            try {
                ((AnnouncementViewHolder) holder).binding.userProfileName.setColor(Color.parseColor(annoucementResModel.getOwner().getColor()));
            } catch (Exception e) {

                ((AnnouncementViewHolder) holder).binding.userProfileName.setColor(context.getResources().getColor(R.color.splash_background_color));
            }


            ((AnnouncementViewHolder) holder).binding.viewAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle extras = new Bundle();
                    extras.putString(BundleConstants.KNOWLEDGE_ID, annoucementResModel.getId());
                    verticalListViewActionHelper.knowledgeItemClicked(extras,false);
                }
            });

        } else {
            holder = (EmptyAnnocementViewHolder) holder;
            ((EmptyAnnocementViewHolder) holder).tv_message.setText("No Announcements");

        }


    }

    @Override
    public long getItemId(int position) {
        if(data != null && data.size()>0) {
            AnnoucementResModel model = data.get(position);
            return model.getCreatedOn() + position;
        }else return position;
    }

    @Override
    public int getItemCount() {

        return data != null && data.size() > 0 ? (!isViewMore && data.size() > 3 ? 3 : data.size()) : 1;

    }

    public void setFullView(boolean isViewMore) {
        this.isViewMore = isViewMore;
    }

    @Override
    public ArrayList getData() {
        return data;
    }

    @Override
    public void setData(ArrayList data) {
        this.data = data;
    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        AnnouncementCardLayoutBinding binding;

        public AnnouncementViewHolder(@NonNull AnnouncementCardLayoutBinding binding) {
            super((View) binding.getRoot());
            this.binding = binding;

        }
    }

    class EmptyAnnocementViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message;
        RelativeLayout view_action;
        public EmptyAnnocementViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            view_action = itemView.findViewById(R.id.view_action);
        }
    }
}
