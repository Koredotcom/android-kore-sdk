package com.kore.ai.widgetsdk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.models.ContactsTeamsHolder;
import com.kore.ai.widgetsdk.utils.KaFontUtils;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.view.ProfileTextView;

import java.util.ArrayList;

/**
 * Created by Shiva Krishna on 6/25/2018.
 */

public class ListedMembersAdapter extends BaseAdapter {

    private Context mContext;
    private String userId;
    private ArrayList<ContactsTeamsHolder> contactsTeamsHolders;
    // private HashMap<String, KoreContact> contactHashMap;
    LayoutInflater layoutInflater;

    public ListedMembersAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }




    @Override
    public int getCount() {
        return contactsTeamsHolders != null ? contactsTeamsHolders.size() : 0;
    }

    @Override
    public ContactsTeamsHolder getItem(int position) {
        if (contactsTeamsHolders != null && contactsTeamsHolders.size() > position)
            return contactsTeamsHolders.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        SharesViewHolder holder;

        if (view == null) {

            view = layoutInflater.inflate(R.layout.listed_member_item, null);
            KaFontUtils.applyCustomFont(mContext, view);
            holder = new SharesViewHolder();
            holder.profileTextView = view.findViewById(R.id.initials_view);
            holder.userName = view.findViewById(R.id.name_view);
            holder.role = view.findViewById(R.id.role_view);
            view.setTag(holder);
        } else {
            holder = (SharesViewHolder) view.getTag();
        }

        populateData(holder, position);

        return view;
    }

    private void populateData(SharesViewHolder holder, int position) {
        ContactsTeamsHolder dataObj = (ContactsTeamsHolder) getItem(position);
        holder.userName.setText(dataObj.getDisplayName());
        holder.profileTextView.setCircle(true);
        holder.profileTextView.setText(dataObj.getDisplayNameInitials());
        try {
            holder.profileTextView.setColor(Color.parseColor(dataObj.getPfColor()));
        } catch (Exception e) {
            holder.profileTextView.setColor(mContext.getResources().getColor(R.color.splash_background_color));
        }
        String emailId = dataObj.getEmailId();
        holder.role.setText(emailId);
        holder.role.setVisibility(!StringUtils.isNullOrEmptyWithTrim(emailId) ? View.VISIBLE : View.GONE);
    }

    public void setContactsTeamsHolders(ArrayList<ContactsTeamsHolder> contactsTeamsHolders) {
        this.contactsTeamsHolders = contactsTeamsHolders;
    }


    private class SharesViewHolder {
        private ProfileTextView profileTextView;
        private TextView userName;
        private TextView role;

    }
}

