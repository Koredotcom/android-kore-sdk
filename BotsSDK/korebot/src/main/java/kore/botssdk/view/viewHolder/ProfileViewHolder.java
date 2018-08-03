package kore.botssdk.view.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.view.ProfileTextView;

public class ProfileViewHolder extends RecyclerView.ViewHolder{
    ProfileTextView profileView;

    public TextView getMoreView() {
        return moreView;
    }

    public void setMoreView(TextView moreView) {
        this.moreView = moreView;
    }

    TextView moreView;

    public ProfileViewHolder(View view) {
        super(view);
        profileView = (ProfileTextView) view.findViewById(R.id.profile_view);
        moreView = (TextView) view.findViewById(R.id.more_view);
    }

    public ProfileTextView getProfileView() {
        return profileView;
    }

}
