package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.models.MeetingConfirmationModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewHolder.ProfileViewHolder;

public class ProfileIndicationAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
    private ArrayList<MeetingConfirmationModel.UserDetailModel> userDetailModels;
    Context context;
    LayoutInflater layoutInflater;
    RecyclerView parentRecyclerView;

    ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;

    public ProfileIndicationAdapter(Context context, RecyclerView parentRecyclerView) {
        this.context = context;
        this.parentRecyclerView = parentRecyclerView;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.profile_circle_layout, null);
        KaFontUtils.applyCustomFont(context,convertView);
        return new ProfileViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        if(position == 3 && userDetailModels.size() > 4) {
            holder.getMoreView().setText(String.format("+%d", userDetailModels.size() - 3));
            holder.getProfileView().setVisibility(View.GONE);
            holder.getMoreView().setVisibility(View.VISIBLE);

        }else{
            final MeetingConfirmationModel.UserDetailModel userDetailModel = userDetailModels.get(position);
            holder.getProfileView().setText(userDetailModel.getInitials());
            holder.getProfileView().setCircle(true);
            holder.getProfileView().setVisibility(View.VISIBLE);
            holder.getMoreView().setVisibility(View.GONE);
            holder.getProfileView().setColor(Color.parseColor(userDetailModel.getColor()));
        }

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        if (userDetailModels == null) {
            return 0;
        } else {
            return userDetailModels.size() <= 4 ? userDetailModels.size() : 4;
        }
    }

    public ArrayList<MeetingConfirmationModel.UserDetailModel> getUserDetailModels() {
        return userDetailModels;
    }

    public void setUserDetailModels(ArrayList<MeetingConfirmationModel.UserDetailModel> userDetailModels) {
        this.userDetailModels = userDetailModels;
    }
}