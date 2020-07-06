package com.kore.ai.widgetsdk.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.kore.ai.widgetsdk.events.EntityEditEvent;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.fragments.WidgetActionSheetFragment;
import com.kore.ai.widgetsdk.listeners.ComposeFooterInterface;
import com.kore.ai.widgetsdk.listeners.RecyclerViewDataAccessor;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.models.LoginModel;
import com.kore.ai.widgetsdk.models.MultiAction;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.utils.BundleConstants;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.view.CircleTransform;
import com.kore.ai.widgetsdk.viewholder.EmptyWidgetViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class DefaultWidgetAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {
    private boolean isExpanded = false;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public ArrayList<String> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(ArrayList<String> selectedIds) {
        this.selectedIds = selectedIds;
    }

    ArrayList<String> selectedIds = null;

    public ArrayList<Widget.Element> getEventList() {
        return eventList;
    }

//    private Duration _cursor;

    ArrayList<Widget.Element> eventList = new ArrayList<>();
    private LayoutInflater inflater = null;
    private Context mContext;

    private String skillName;

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    private LoginModel loginModel;


    private int DATA_FOUND = 1;
    private int EMPTY_CARD = 0;
    private int MESSAGE = 2;
    private int REPORTS = 3;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private ComposeFooterInterface composeFooterInterface;
    private boolean isFromWidget;


    public void setFromWidget(boolean fromWidget) {
        isFromWidget = fromWidget;
    }

    List<MultiAction> multiActions;
    int previewLength;
    String msg;
    Drawable errorIcon;
    String trigger;
    private boolean isLoginNeeded;

    public DefaultWidgetAdapter(Context mContext, String type, String trigger) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.type = type;
        this.trigger = trigger;
        notifyDataSetChanged();
        selectedIds = new ArrayList<>();

    }

    public void clearSelectedItems() {
        selectedIds.clear();
    }

    public Widget.Element getItem(int position) {
        if (position < eventList.size())
            return eventList.get(position);
        else return null;
    }


    @Override
    public int getItemViewType(int position) {
        if(isLoginNeeded()){
            return REPORTS;
        }
        if (eventList != null && eventList.size() > 0) {
            return DATA_FOUND;
        }

        if (msg != null && !msg.equalsIgnoreCase("")) {
            return MESSAGE;
        }
        return EMPTY_CARD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == REPORTS ){
            View view = inflater.inflate(R.layout.need_login_widget_layout, parent, false);
            return new ReportsViewHolder(view);
        }
        else if (viewType == EMPTY_CARD || viewType == MESSAGE) {
            View view = inflater.inflate(R.layout.card_empty_widget_layout, parent, false);
            return new EmptyWidgetViewHolder(view);
        }else
            return new DefaultWidgetAdapter.ViewHolder(inflater.inflate(R.layout.default_list_item, parent, false));
    }


    public String checkStringNull(String value) {

        if (value != null && !value.trim().equalsIgnoreCase("")) {
            return value.trim();
        }
        return "";


    }
    WidgetViewMoreEnum widgetViewMoreEnum;
    public void setViewMoreEnum(WidgetViewMoreEnum widgetViewMoreEnum) {
        this.widgetViewMoreEnum=widgetViewMoreEnum;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderData, int position) {
        if(holderData.getItemViewType() ==  REPORTS){
//            final Element model = eventList.get(position);
            ReportsViewHolder holder = (ReportsViewHolder) holderData;

//            holder.txt.setText(model.getText());
            holder.loginBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mContext instanceof Activity) {
                        Intent intent = new Intent(mContext, GenericWebViewActivity.class);
                        intent.putExtra("url", loginModel.getUrl());
                        intent.putExtra("header", mContext.getResources().getString(R.string.app_name));
                        ((Activity) mContext).startActivityForResult(intent, BundleConstants.REQ_CODE_REFRESH_CURRENT_PANEL);
                    }else{
                        Toast.makeText(mContext,"Instance not activity",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if (holderData.getItemViewType() == EMPTY_CARD || holderData.getItemViewType() == MESSAGE) {
            EmptyWidgetViewHolder emptyHolder = (EmptyWidgetViewHolder) holderData;

            emptyHolder.tv_disrcription.setText(msg != null ? msg : "No data");
            emptyHolder.img_icon.setImageDrawable(holderData.getItemViewType() == EMPTY_CARD ? ContextCompat.getDrawable(mContext, R.drawable.no_meeting) : errorIcon);


        } else {

            DefaultWidgetAdapter.ViewHolder holder = (DefaultWidgetAdapter.ViewHolder) holderData;

            final Widget.Element model = eventList.get(position);

            if (StringUtils.isNullOrEmpty(model.getTitle())) {
                holder.txtTitle.setVisibility(GONE);
            } else {
                holder.txtTitle.setText(model.getTitle().trim());
            }

            if (StringUtils.isNullOrEmpty(model.getSub_title())) {
                holder.txtSubTitle.setVisibility(GONE);
            } else {
                holder.txtSubTitle.setText(model.getSub_title().trim());
            }

            if (StringUtils.isNullOrEmpty(model.getModifiedTime())) {
                holder.txtTextModif.setVisibility(GONE);
            } else {
                holder.txtTextModif.setText(model.getModifiedTime().trim());
            }


            if (StringUtils.isNullOrEmpty(model.getText())) {
                holder.txtText.setVisibility(GONE);
            } else {
                holder.txtText.setText(model.getText().trim());
            }

            if (!StringUtils.isNullOrEmpty(model.getIcon())) {
                Picasso.get().load(model.getIcon().trim()).transform(new CircleTransform()).into(holder.imageIcon);
            } else {
                holder.imageIcon.setVisibility(GONE);
            }

            if (model.getActions() != null && model.getActions().size() > 0) {
                holder.icon_down.setVisibility(VISIBLE);
                holder.icon_down.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                            bottomSheetDialog.setisFromFullView(false);
                            bottomSheetDialog.setSkillName(skillName,trigger);
                            bottomSheetDialog.setData(model);
                            bottomSheetDialog.setVerticalListViewActionHelper(verticalListViewActionHelper);
                            bottomSheetDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "add_tags");
                    }
                });
            } else {
                holder.icon_down.setVisibility(GONE);
            }

            if (model.getButton() != null && model.getButton().size() > 0) {
            /*FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);*/

                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

                ButtonListAdapter buttonRecyclerAdapter = new ButtonListAdapter(mContext, model.getButton(),trigger);
                buttonRecyclerAdapter.setSkillName(skillName);
                buttonRecyclerAdapter.setIsFromFullView(isFullView);
                holder.recyclerView.setAdapter(buttonRecyclerAdapter);
                buttonRecyclerAdapter.notifyDataSetChanged();
            }

            holder.innerlayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getDefaultAction() != null && model.getDefaultAction().getType() != null && model.getDefaultAction().getType().equals("url")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getDefaultAction().getUrl()));
                        try {
                            mContext.startActivity(browserIntent);
                        }catch (ActivityNotFoundException ex){
                            ex.printStackTrace();
                        }
                    }else if(model.getDefaultAction() != null && model.getDefaultAction().getType() != null && model.getDefaultAction().getType().equals("postback")){
                        if(Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME)|| TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))){
                            defaultAction(model.getDefaultAction().getPayload(),true);
                        }else{
                            defaultAction(model.getDefaultAction().getPayload(),false);
                        }
                    }
                }
            });
            if (position == eventList.size() - 1 && eventList.size() < 3) {
                holder.divider.setVisibility(View.GONE);
            }
        }
    }

    public void defaultAction(String utterance, boolean appendUtterance){
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if(appendUtterance && trigger!= null)
            msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);
        if(isFullView)
        {
            ((Activity)mContext).finish();
        }


    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
      //  return eventList != null && eventList.size() > 0 ? eventList.size() : 1;
        if(widgetViewMoreEnum!=null&&widgetViewMoreEnum==WidgetViewMoreEnum.EXPAND_VIEW)
        {


            return eventList != null && eventList.size() > 0 ? eventList.size() : 1;
        }
        if(isLoginNeeded()){
            return 1;
        }
        return eventList != null && eventList.size() > 0 ? (!isExpanded && eventList.size() > previewLength ? previewLength : eventList.size()) : 1;
    }


    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @Override
    public ArrayList getData() {
        return (ArrayList) eventList;
    }

    @Override
    public void setData(ArrayList data) {

    }

    public void setWidgetData(List<Widget.Element> data) {
        this.eventList = (ArrayList<Widget.Element>) data;
        notifyDataSetChanged();
    }



    @Override
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;

    }

    public void setMultiActions(List<MultiAction> multiActions) {
        this.multiActions = multiActions;
    }

    public List<MultiAction> getMultiActions() {
        return multiActions;
    }

    public void setPreviewLength(int previewLength) {
        this.previewLength = previewLength;
    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg = msg;
        this.errorIcon = errorIcon;
    }
    boolean isFullView;
    public void setFromFullView(boolean isFullView) {
        this.isFullView=isFullView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutDetails, innerlayout;
        public TextView txtTitle;
        public TextView txtSubTitle;
        public TextView txtText, txtTextModif;
        public TextView tvborder, tv_users;
        public ImageView imageIcon;
        public View divider;
        public ImageView icon_down;
        public RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            txtText = (TextView) itemView.findViewById(R.id.txtText);
            txtTextModif = (TextView) itemView.findViewById(R.id.txtTextModif);


            imageIcon = (ImageView) itemView.findViewById(R.id.imageIcon);
            innerlayout = itemView.findViewById(R.id.innerlayout);

            recyclerView = itemView.findViewById(R.id.buttonsList);

            icon_down = itemView.findViewById(R.id.icon_down);
            divider = itemView.findViewById(R.id.divider);

        }
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public boolean isLoginNeeded() {
        return this.isLoginNeeded;
    }

    public void setLoginNeeded(boolean loginNeeded) {
        this.isLoginNeeded = loginNeeded;
    }

    class ReportsViewHolder extends RecyclerView.ViewHolder{
        Button loginBtn;
        TextView txt;
        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            loginBtn = itemView.findViewById(R.id.login_button);
            txt = itemView.findViewById(R.id.tv_message);
        }
    }
}
