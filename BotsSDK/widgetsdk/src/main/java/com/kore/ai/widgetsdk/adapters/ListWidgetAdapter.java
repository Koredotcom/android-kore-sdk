package com.kore.ai.widgetsdk.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.kore.ai.widgetsdk.listeners.RecyclerViewDataAccessor;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.listeners.WidgetComposeFooterInterface;
import com.kore.ai.widgetsdk.models.LoginModel;
import com.kore.ai.widgetsdk.models.MultiAction;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.WidgetListElementModel;
import com.kore.ai.widgetsdk.utils.BundleConstants;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.viewholder.EmptyWidgetViewHolder;
import com.kore.ai.widgetsdk.views.viewutils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class ListWidgetAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {
    private boolean isExpanded = false;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public ArrayList<String> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(ArrayList<String> selectedIds) {
        this.selectedIds = selectedIds;
    }

    ArrayList<String> selectedIds = null;

    public ArrayList<WidgetListElementModel> getEventList() {
        return items;
    }

//    private Duration _cursor;

    private ArrayList<WidgetListElementModel> items = new ArrayList<>();
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
    private WidgetComposeFooterInterface widgetComposeFooterInterface;

    public ListWidgetAdapter(Context mContext, String type, String trigger) {
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

    public WidgetListElementModel getItem(int position) {
        if (position < items.size())
            return items.get(position);
        else return null;
    }


    @Override
    public int getItemViewType(int position) {
        if(isLoginNeeded()){
            return REPORTS;
        }
        if (items != null && items.size() > 0) {
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
            return new ListWidgetAdapter.ViewHolder(inflater.inflate(R.layout.listwidget_view, parent, false));
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
                        if(loginModel != null) {
                            Intent intent = new Intent(mContext, GenericWebViewActivity.class);
                            intent.putExtra("url", loginModel.getUrl());
                            intent.putExtra("header", mContext.getResources().getString(R.string.app_name));
                            ((Activity) mContext).startActivityForResult(intent, BundleConstants.REQ_CODE_REFRESH_CURRENT_PANEL);
                        }
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

            final ListWidgetAdapter.ViewHolder holder = (ListWidgetAdapter.ViewHolder) holderData;

            final WidgetListElementModel model = items.get(position);

            if (StringUtils.isNullOrEmpty(model.getTitle())) {
                holder.txtTitle.setVisibility(GONE);
            } else {
                holder.txtTitle.setText(model.getTitle().trim());
            }

            if (StringUtils.isNullOrEmpty(model.getSubtitle())) {
                holder.txtSubTitle.setVisibility(GONE);
            } else {
                holder.txtSubTitle.setText(model.getSubtitle().trim());
            }


            holder.img_up_down.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean expanded = holder.buttonLayout.isExpanded();
                    if(!expanded)
                        holder.img_up_down.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_up_24px));
                    else
                        holder.img_up_down.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_down_24px));
                    holder.buttonLayout.setExpanded(!expanded);
                }
            });


           /* if (StringUtils.isNullOrEmpty(model.getText())) {
                holder.txtText.setVisibility(GONE);
            } else {
                holder.txtText.setText(model.getText().trim());
            }*/

            if (model.getImage()!=null && !StringUtils.isNullOrEmpty(model.getImage().getImage_src()) && Patterns.WEB_URL.matcher(model.getImage().getImage_src()).matches()) {
                String url = model.getImage().getImage_src().trim();
                url = url.replace("http://","https://");
                Picasso.get().load(url).transform(new RoundedCornersTransform()).into(holder.imageIcon);
            } else {
                holder.imageIcon.setVisibility(GONE);
            }

            /*if (model.getActions() != null && model.getActions().size() > 0) {
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
            }*/


            if(model.getValue() != null && model.getValue().getType() != null) {
                switch (model.getValue().getType()){
                    case "button":
                        holder.icon_image_load.setVisibility(GONE);
                        holder.imgMenu.setVisibility(GONE);
                        holder.tvText.setVisibility(GONE);
                        holder.tvUrl.setVisibility(GONE);
                        holder.tvButtonParent.setVisibility(VISIBLE);

                        holder.tvButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                        (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))) {
                                    buttonAction(model.getValue().getButton(), true);
                                } else {
                                    buttonAction(model.getValue().getButton(), false);
                                }
                            }
                        });
                        String btnTitle = "";
                        if(model.getValue().getButton() != null && model.getValue().getButton().getTitle() != null)
                            btnTitle = model.getValue().getButton().getTitle();
                        else
                            btnTitle = model.getValue().getText();
                        if(!StringUtils.isNullOrEmpty(btnTitle))
                            holder.tvButton.setText(btnTitle);
                        else
                            holder.tvButtonParent.setVisibility(GONE);

                        break;
                    case "menu":
                        holder.icon_image_load.setVisibility(GONE);
                        holder.imgMenu.setVisibility(VISIBLE);
                        holder.imgMenu.bringToFront();
                        holder.imgMenu.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ( model.getValue()!= null &&  model.getValue().getMenu()!= null && model.getValue().getMenu().size() > 0) {
                                    //holder.icon_down.setVisibility(VISIBLE);

                                    WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                                    bottomSheetDialog.setisFromFullView(false);
                                    bottomSheetDialog.setSkillName(skillName,trigger);
                                    bottomSheetDialog.setData(model,true);
                                    bottomSheetDialog.setVerticalListViewActionHelper(verticalListViewActionHelper);
                                    bottomSheetDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "add_tags");

                                }
                            }
                        });
                        holder.tvText.setVisibility(GONE);
                        holder.tvButtonParent.setVisibility(GONE);
                        holder.tvButton.setVisibility(GONE);
                        holder.tvUrl.setVisibility(GONE);
                        break;
                    case "text":
                        holder.icon_image_load.setVisibility(GONE);
                        holder.imgMenu.setVisibility(GONE);
                        holder.tvText.setVisibility(VISIBLE);
                        holder.tvText.setText(model.getValue().getText());
                        holder.tvButtonParent.setVisibility(GONE);
                        holder.tvUrl.setVisibility(GONE);
                        break;
                    case "url":
                        holder.icon_image_load.setVisibility(GONE);
                        holder.imgMenu.setVisibility(GONE);
                        holder.tvText.setVisibility(GONE);
                        SpannableString content = new SpannableString(model.getValue().getUrl().getTitle()!=null?model.getValue().getUrl().getTitle():model.getValue().getUrl().getLink());
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        holder.tvUrl.setText(content);
                        holder.tvButtonParent.setVisibility(GONE);
                        holder.tvUrl.setVisibility(VISIBLE);
                        holder.tvUrl.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(model.getValue().getUrl().getLink() != null) {
                                    Intent intent = new Intent(mContext, GenericWebViewActivity.class);
                                    intent.putExtra("url", model.getValue().getUrl().getLink());
                                    intent.putExtra("header", mContext.getResources().getString(R.string.app_name));
                                    mContext.startActivity(intent);
                                }
                            }
                        });

                        break;
                    case "image":
                        holder.icon_image_load.setVisibility(VISIBLE);
                        holder.imgMenu.setVisibility(GONE);
                        holder.tvText.setVisibility(GONE);
                        holder.tvButtonParent.setVisibility(GONE);
                        holder.tvUrl.setVisibility(GONE);
                        if(model.getValue().getImage()!=null && !StringUtils.isNullOrEmpty(model.getValue().getImage().getImage_src()))
                        {
                            Picasso.get().load(model.getValue().getImage().getImage_src()).into(holder.icon_image_load);
                            holder.icon_image_load.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                 //   defaultAction(model.getValue().getImage().getUtterance()!=null?model.getValue().getImage().getUtterance():model.getValue().getImage().getPayload()!=null?model.getValue().getImage().getPayload():"",true);

                                    if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                            (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))) {
                                        defaultAction(model.getValue().getImage().getUtterance()!=null?model.getValue().getImage().getUtterance():model.getValue().getImage().getPayload()!=null?model.getValue().getImage().getPayload():"",true);


                                    } else {
                                        defaultAction(model.getValue().getImage().getUtterance()!=null?model.getValue().getImage().getUtterance():model.getValue().getImage().getPayload()!=null?model.getValue().getImage().getPayload():"",false);

                                    }
                                }
                            });
                        }
                        break;
                }

            }

     /*       holder.imgMenu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                     if ( model.getButtons()!= null &&  model.getButtons().size() > 0) {
                //holder.icon_down.setVisibility(VISIBLE);

                            WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                            bottomSheetDialog.setisFromFullView(false);
                            bottomSheetDialog.setSkillName(skillName,trigger);
                            bottomSheetDialog.setData(model);
                            bottomSheetDialog.setVerticalListViewActionHelper(verticalListViewActionHelper);
                            bottomSheetDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "add_tags");

            } else {
               // holder.icon_down.setVisibility(GONE);
            }
                }
            });*/

            if (model.getButtons() != null  && model.getButtons().size() > 0) {
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

                ListWidgetButtonAdapter buttonRecyclerAdapter = new ListWidgetButtonAdapter(mContext, model.getButtons(),trigger);
                buttonRecyclerAdapter.setSkillName(skillName);
                buttonRecyclerAdapter.setIsFromFullView(isFullView);
                holder.recyclerView.setAdapter(buttonRecyclerAdapter);
                buttonRecyclerAdapter.notifyDataSetChanged();
            }else{
                holder.img_up_down.setVisibility(GONE);
            }

            holder.alDetails.setVisibility(GONE);
            if(model.getDetails() != null && model.getDetails().size() > 0)
            {
                holder.alDetails.setVisibility(VISIBLE);
                ListWidgetDetailsAdapter listWidgetDetailsAdapter = new ListWidgetDetailsAdapter(mContext, model.getDetails());
                holder.alDetails.setAdapter(listWidgetDetailsAdapter);
            }

            holder.innerlayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getDefault_action() != null && model.getDefault_action().getType() != null && model.getDefault_action().getType().equals("url")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getDefault_action().getUrl()));
                        try {
                            mContext.startActivity(browserIntent);
                        }catch (ActivityNotFoundException ex){
                            ex.printStackTrace();
                        }
                    }else if(model.getDefault_action() != null && model.getDefault_action().getType() != null && model.getDefault_action().getType().equals("postback")){
                        if(Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME)|| TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))){
                            defaultAction(model.getDefault_action().getPayload(),true);
                        }else{
                            defaultAction(model.getDefault_action().getPayload(),false);
                        }
                    }
                }
            });
            if (position == items.size() - 1 && items.size() < 3) {
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

    public void buttonAction(Widget.Button button, boolean appendUtterance){
        String utterance = null;
        if(button != null){
            utterance = button.getUtterance();
        }
        if(utterance == null)return;
        if(utterance !=null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))){
            if(utterance.startsWith("tel:")){
                launchDialer(mContext,utterance);
            }else if(utterance.startsWith("mailto:")){
                showEmailIntent((Activity) mContext,utterance.split(":")[1]);
            }
            return;
        }
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

        try {


            if (isFullView) {
                ((Activity) mContext).finish();
            }
        } catch (Exception e) {

        }
    }

    public static void showEmailIntent(Activity activity, String recepientEmail) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + recepientEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");

        try {
            activity.startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "Error while launching email intent!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public static void launchDialer(Context context, String url) {
        try {
            Intent intent = new Intent(hasPermission(context, Manifest.permission.CALL_PHONE) ? Intent.ACTION_CALL : Intent.ACTION_DIAL);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Invalid url!", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean hasPermission(Context context,String... permission) {
        boolean shouldShowRequestPermissionRationale = true;
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionLength = permission.length;
            for (int i=0;i<permissionLength;i++) {
                shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale &&
                        ActivityCompat.checkSelfPermission(context, permission[i]) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return shouldShowRequestPermissionRationale;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
      //  return items != null && items.size() > 0 ? items.size() : 1;
        if(widgetViewMoreEnum!=null&&widgetViewMoreEnum==WidgetViewMoreEnum.EXPAND_VIEW)
        {


            return items != null && items.size() > 0 ? items.size() : 1;
        }
        if(isLoginNeeded()){
            return 1;
        }
        return items != null && items.size() > 0 ? (!isExpanded && items.size() > previewLength ? previewLength : items.size()) : 1;
    }


    public WidgetComposeFooterInterface getPanelComposeFooterInterface() {
        return widgetComposeFooterInterface;
    }

    public void setPanelComposeFooterInterface(WidgetComposeFooterInterface composeFooterInterface) {
        this.widgetComposeFooterInterface = composeFooterInterface;
    }

    @Override
    public ArrayList getData() {
        return (ArrayList) items;
    }

    @Override
    public void setData(ArrayList data) {

    }

    public void setWidgetData(ArrayList<WidgetListElementModel> data) {
        this.items =  data;
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
        public TextView tvborder, tv_users;
        public ImageView imageIcon,img_up_down;
        public ExpandableLayout buttonLayout;
        public View divider;
        public RecyclerView recyclerView;
        public ImageView imgMenu,icon_image_load;
        public TextView tvText;
        public TextView tvUrl;
        public TextView tvButton;
        public LinearLayout tvButtonParent;
        public ListView alDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            imageIcon = (ImageView) itemView.findViewById(R.id.imageIcon);
            buttonLayout = itemView.findViewById(R.id.button_layout);
            img_up_down = itemView.findViewById(R.id.img_up_down);
            innerlayout = itemView.findViewById(R.id.innerlayout);
            recyclerView = itemView.findViewById(R.id.buttonsList);
            divider = itemView.findViewById(R.id.divider);
            imgMenu = itemView.findViewById(R.id.icon_image);
            tvButton = itemView.findViewById(R.id.tv_button);
            tvText = itemView.findViewById(R.id.tv_text);
            tvUrl = itemView.findViewById(R.id.tv_url);
            icon_image_load=itemView.findViewById(R.id.icon_image_load);
            tvButtonParent = itemView.findViewById(R.id.tv_values_layout);
            alDetails = itemView.findViewById(R.id.alDetails);
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
