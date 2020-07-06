package com.kore.ai.widgetsdk.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.view.ProfileTextView;

/**
 * Created by Shiva Krishna on 2/22/2018.
 */

public class SelectedAudienceViewHolder extends RecyclerView.ViewHolder{
    ProfileTextView initials;

    public TextView getPrivilegeView() {
        return privilegeView;
    }

    public void setPrivilegeView(TextView privilegeView) {
        this.privilegeView = privilegeView;
    }

    TextView privilegeView;

    public ImageView getCancelAction() {
        return cancelAction;
    }

    public void setCancelAction(ImageView cancelAction) {
        this.cancelAction = cancelAction;
    }

    ImageView cancelAction;

    public ProfileTextView getInitials() {
        return initials;
    }

    public void setInitials(ProfileTextView initials) {
        this.initials = initials;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    TextView name;




    public SelectedAudienceViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.name_view);
        initials = view.findViewById(R.id.initials_view);
        cancelAction = view.findViewById(R.id.cancel_action);
        privilegeView = view.findViewById(R.id.user_privilege);
    }

}