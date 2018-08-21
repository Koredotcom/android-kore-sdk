package kore.botssdk.view.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import kore.botssdk.R;
import kore.botssdk.view.ProfileTextView;

public class ProfileViewHolder extends RecyclerView.ViewHolder{
    ProfileTextView profileView;


    public ProfileViewHolder(View view) {
        super(view);
        profileView = (ProfileTextView) view.findViewById(R.id.profile_view);
    }

    public ProfileTextView getProfileView() {
        return profileView;
    }

}
