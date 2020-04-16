package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.MeetingConfirmationModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewHolder.ProfileViewHolder;

public class ProfileIndicationAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
    private ArrayList<MeetingConfirmationModel.UserDetailModel> userDetailModels;
    Context context;
    LayoutInflater layoutInflater;
    RecyclerView parentRecyclerView;
    int whiteColor,splashColor;
    ComposeFooterInterface composeFooterInterface;

    public ProfileIndicationAdapter(Context context, RecyclerView parentRecyclerView) {
        this.context = context;
        this.parentRecyclerView = parentRecyclerView;
        layoutInflater = LayoutInflater.from(context);
        whiteColor = context.getResources().getColor(R.color.white);
        splashColor = context.getResources().getColor(R.color.splash_color);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.profile_circle_layout, null);
        KaFontUtils.applyCustomFont(context,convertView);
        return new ProfileViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        holder.getProfileView().setCircle(true);
        if(position == 3 && userDetailModels.size() > 4) {
            holder.getProfileView().setText(String.format("+%d", userDetailModels.size() - 3));
            holder.getProfileView().setColor(Color.parseColor("#efeffc"));
            holder.getProfileView().setTextColor(splashColor);
        }else{
            final MeetingConfirmationModel.UserDetailModel userDetailModel = userDetailModels.get(position);
            holder.getProfileView().setText(userDetailModel.getInitials());
            holder.getProfileView().setTextColor(whiteColor);
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