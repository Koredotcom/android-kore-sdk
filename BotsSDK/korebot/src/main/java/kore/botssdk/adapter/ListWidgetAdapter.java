package kore.botssdk.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.dialogs.WidgetActionSheetFragment;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.LoginModel;
import kore.botssdk.models.MultiAction;
import kore.botssdk.models.Widget.Element;
import kore.botssdk.models.WidgetListElementModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.WidgetViewMoreEnum;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;
import kore.botssdk.view.viewUtils.CircleTransform;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

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
            return new ListWidgetAdapter.ViewHolder(inflater.inflate(R.layout.list_widget_item, parent, false));
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

            ListWidgetAdapter.ViewHolder holder = (ListWidgetAdapter.ViewHolder) holderData;

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
                        holder.imgMenu.setVisibility(GONE);
                        holder.tvText.setVisibility(GONE);
                        holder.tvUrl.setVisibility(GONE);
                        holder.tvButtonParent.setVisibility(VISIBLE);
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
                        holder.imgMenu.setVisibility(VISIBLE);
                        holder.tvText.setVisibility(GONE);
                        holder.tvButtonParent.setVisibility(GONE);
                        holder.tvUrl.setVisibility(GONE);
                        break;
                    case "text":
                        holder.imgMenu.setVisibility(GONE);
                        holder.tvText.setVisibility(VISIBLE);
                        holder.tvText.setText(model.getValue().getText());
                        holder.tvButtonParent.setVisibility(GONE);
                        holder.tvUrl.setVisibility(GONE);
                        break;
                    case "url":
                        holder.imgMenu.setVisibility(GONE);
                        holder.tvText.setVisibility(GONE);
                        holder.tvUrl.setText(model.getValue().getUrl().getTitle()!=null?model.getValue().getUrl().getTitle():model.getValue().getUrl().getLink());
                        holder.tvButtonParent.setVisibility(GONE);
                        holder.tvUrl.setVisibility(VISIBLE);
                        break;


                }

            }

            holder.imgMenu.setOnClickListener(new OnClickListener() {
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
            });

            if (model.getButtons() != null  && model.getButtons().size() > 0) {
            /*FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);*/

                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

                ListWidgetButtonAdapter buttonRecyclerAdapter = new ListWidgetButtonAdapter(mContext, model.getButtons(),trigger);
                buttonRecyclerAdapter.setSkillName(skillName);
                buttonRecyclerAdapter.setIsFromFullView(isFullView);
                holder.recyclerView.setAdapter(buttonRecyclerAdapter);
                buttonRecyclerAdapter.notifyDataSetChanged();
            }else{
                holder.img_up_down.setVisibility(GONE);
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


    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
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
        public ImageView imgMenu;
        public TextView tvText;
        public TextView tvUrl;
        public TextView tvButton;
        public LinearLayout tvButtonParent;

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
            tvButtonParent = itemView.findViewById(R.id.tv_values_layout);

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
