package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import kore.botssdk.R;
import kore.botssdk.databinding.AnnouncementCardLayoutBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.AnnoucementResModel;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;

public class AnnouncementAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {

    private static final int DATA_FOUND = 1;
    private static final int NO_DATA = 0;
    Context context;
    List<AnnoucementResModel> data;
    boolean isViewMore;

    public AnnouncementAdapter(Context context) {
        this.context = context;
    }


    public AnnouncementAdapter(Context activity, List<AnnoucementResModel> data) {

        context = activity;
        this.data = data;
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

            ((AnnouncementViewHolder) holder).binding.tvTime.setText(DateUtils.getFormattedSendDateInTimeFormatCoreFunctionality2(context,annoucementResModel.getLastMod()));
            ((AnnouncementViewHolder) holder).binding.userProfileName.setCircle(true);
            ((AnnouncementViewHolder) holder).binding.userProfileName.setText(StringUtils.getInitials(annoucementResModel.getOwner().getFullName()));
            try {
                ((AnnouncementViewHolder) holder).binding.userProfileName.setColor(Color.parseColor(annoucementResModel.getOwner().getColor()));
            } catch (Exception e) {

                ((AnnouncementViewHolder) holder).binding.userProfileName.setColor(context.getResources().getColor(R.color.splash_background_color));
            }}
            else
            {
                holder=(EmptyAnnocementViewHolder)holder;
                ((EmptyAnnocementViewHolder) holder).tv_message.setText("No Announcements");

            }



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
        return null;
    }

    @Override
    public void setData(ArrayList data) {

    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {

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

        public EmptyAnnocementViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }
}
