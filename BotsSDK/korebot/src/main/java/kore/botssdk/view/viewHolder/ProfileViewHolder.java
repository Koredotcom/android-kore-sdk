package kore.botssdk.view.viewHolder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.view.ProfileTextView;

public class ProfileViewHolder extends RecyclerView.ViewHolder{
    final ProfileTextView profileView;


    public ProfileViewHolder(View view) {
        super(view);
        profileView = view.findViewById(R.id.profile_view);
    }

    public ProfileTextView getProfileView() {
        return profileView;
    }

}
