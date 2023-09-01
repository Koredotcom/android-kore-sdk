package com.kore.ai.widgetsdk.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.KaShareMeetingActivity;
import com.kore.ai.widgetsdk.models.CalEventsTemplateModel;
import com.kore.ai.widgetsdk.net.KaRestResponse;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.view.ProfileTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareMeetingAdapter extends RecyclerView.Adapter<ShareMeetingAdapter.ShareViewHolder> {

    final KaShareMeetingActivity activity;
    List<CalEventsTemplateModel.Attendee> attendeesList;

    final boolean isCheckBoxRequired;
//    boolean isAllSwitchSelected;

    private final HashMap<String, CalEventsTemplateModel.Attendee> initialSharedAttendeesList = new HashMap<>();
    private final HashMap<String, CalEventsTemplateModel.Attendee> initialUnSharedAttendeesList = new HashMap<>();

    private final HashMap<String, CalEventsTemplateModel.Attendee> newSharedAttendeesList = new HashMap<>();
    private final HashMap<String, CalEventsTemplateModel.Attendee> newUnSharedAttendeesList = new HashMap<>();


    public ShareMeetingAdapter(KaShareMeetingActivity activity, List<CalEventsTemplateModel.Attendee> attendeesList, boolean isCheckBoxRequired) {
        this.activity = activity;

        this.attendeesList = attendeesList;
        this.isCheckBoxRequired = isCheckBoxRequired;
    }


    public void setAllSelected(boolean selectAll) {
        //isAllSwitchSelected = selectAll;
        for (int i = 0; i < attendeesList.size(); i++) {
            attendeesList.get(i).setCheckState(selectAll);
            updateNewSharedOrUnShared(attendeesList.get(i),selectAll);
        }
        doDoneAction();
        notifyDataSetChanged();
    }

    private boolean isInSharedList(ArrayList<KaRestResponse.SharedList> resp, String email){
        for(KaRestResponse.SharedList list:resp){
            if(list != null && list.getEmailId().equals(email)){
                return true;
            }
        }
        return false;
    }

    private void doAllInviteeAction(){
        int count = 0;
        for(int i = 0; i <attendeesList.size(); i++){
                if(attendeesList.get(i).isCheckState()){
                    count++;
                }
        }

        activity.doAttendeeSwitchAction(count == attendeesList.size());

        doDoneAction();
    }

    public void setSelectedUsers(ArrayList<KaRestResponse.SharedList> resp) {
        if(resp != null && resp.size() >0) {
            for (int i = 0; i < attendeesList.size(); i++) {
                if(isInSharedList(resp,attendeesList.get(i).getEmail())) {
                    attendeesList.get(i).setCheckState(true);
                    initialSharedAttendeesList.put(attendeesList.get(i).getEmail(), attendeesList.get(i));
                }else{
                    initialUnSharedAttendeesList.put(attendeesList.get(i).getEmail(),attendeesList.get(i));
                }
            }
        }else{
            for (int i = 0; i < attendeesList.size(); i++) {
                initialUnSharedAttendeesList.put(attendeesList.get(i).getEmail(),attendeesList.get(i));
            }
        }
        doAllInviteeAction();
        notifyDataSetChanged();
    }

    private void doDoneAction(){
        activity.menuDoneVisibility(newSharedAttendeesList != null && newSharedAttendeesList.values().size() > 0 || newUnSharedAttendeesList != null && newUnSharedAttendeesList.values().size() > 0);
    }

    public HashMap<String, ArrayList> getAllInvitees() {
        HashMap<String,ArrayList> map = new HashMap<String,ArrayList>();

        ArrayList<KaRestResponse.SharedList> selectedUserIds = new ArrayList<>();
        ArrayList<KaRestResponse.SharedList> nonSelectedUserIds = new ArrayList<>();

        for(CalEventsTemplateModel.Attendee attendee : newSharedAttendeesList.values()){
            KaRestResponse.SharedList sharedList = new KaRestResponse.SharedList();
            sharedList.setPrivilege(0);
            sharedList.setType("person");
            sharedList.setEmailId(attendee.getEmail());
            sharedList.setId(attendee.getId());
            selectedUserIds.add(sharedList);
        }
        for(CalEventsTemplateModel.Attendee attendee : newUnSharedAttendeesList.values()){
            KaRestResponse.SharedList sharedList = new KaRestResponse.SharedList();
            sharedList.setPrivilege(-1);
            sharedList.setType("person");
            sharedList.setEmailId(attendee.getEmail());
            sharedList.setId(attendee.getId());
            nonSelectedUserIds.add(sharedList);
        }
        map.put("selected",selectedUserIds);
        map.put("unselected",nonSelectedUserIds);
        return map;
    }

    public void setAttendeesList(List<CalEventsTemplateModel.Attendee> attendeesList) {
        this.attendeesList = attendeesList;
        activity.menuDoneVisibility(false);
    }

    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.meeting_share_adapter, parent, false);
        return new ShareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShareViewHolder holder, int position) {

        String name = attendeesList.get(position).getName();
        String email = attendeesList.get(position).getEmail();
        holder.user_name.setText(name != null && !TextUtils.isEmpty(name) ? name : email);
        holder.profile_name.setText(StringUtils.getInitials(holder.user_name.getText().toString()));
        holder.profile_name.setColor(activity.getResources().getColor(R.color.splash_background_color));
        holder.check_user.setVisibility(isCheckBoxRequired ? View.VISIBLE : View.GONE);
        if (isCheckBoxRequired) {

            /*if(isAllSwitchSelected){
                holder.check_user.setClickable(false);
            }*/

            holder.check_user.setChecked(attendeesList.get(position).isCheckState());
            /*if(!isAllSwitchSelected) {*/
                holder.check_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isChecked = holder.check_user.isChecked();
                        if (isChecked) {
                            attendeesList.get(holder.getBindingAdapterPosition()).setCheckState(true);
                            updateNewSharedOrUnShared(attendeesList.get(holder.getBindingAdapterPosition()), true);

                        } else {
                            attendeesList.get(holder.getBindingAdapterPosition()).setCheckState(false);
                            updateNewSharedOrUnShared(attendeesList.get(holder.getBindingAdapterPosition()), false);
                        }
                        doAllInviteeAction();
                    }
                });
            /*}*/
        }
    }

    private void updateNewSharedOrUnShared(CalEventsTemplateModel.Attendee attendee, boolean newShared){

        if(newShared){
            if(newUnSharedAttendeesList.get(attendee.getEmail())!=null ){
                newUnSharedAttendeesList.remove(attendee.getEmail());
            }
            if(initialSharedAttendeesList.get(attendee.getEmail())==null )
                newSharedAttendeesList.put(attendee.getEmail(), attendee);
        }else{
            if(newSharedAttendeesList.get(attendee.getEmail())!=null ){
                newSharedAttendeesList.remove(attendee.getEmail());
            }
            if(initialUnSharedAttendeesList.get(attendee.getEmail()) == null)
                newUnSharedAttendeesList.put(attendee.getEmail(), attendee);
        }
    }


    @Override
    public int getItemCount() {
        if (attendeesList != null) {
            return attendeesList.size();
        }
        return 0;
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {

        final TextView user_name;
        final CheckBox check_user;
        final ProfileTextView profile_name;

        public ShareViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_name = itemView.findViewById(R.id.profile_name);
            user_name = itemView.findViewById(R.id.user_name);
            check_user = itemView.findViewById(R.id.check_user);
        }
    }
}
