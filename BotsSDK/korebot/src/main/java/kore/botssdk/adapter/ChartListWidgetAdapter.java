package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build.VERSION_CODES;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.LoginModel;
import kore.botssdk.models.MultiAction;
import kore.botssdk.models.Widget;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.utils.WidgetViewMoreEnum;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class ChartListWidgetAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {
    private boolean isExpanded = false;
    VerticalListViewActionHelper verticalListViewActionHelper;

    ArrayList<Widget.Element> eventList = new ArrayList<>();
    private LayoutInflater inflater = null;
    private final Context mContext;

    private final int DATA_FOUND = 1;
    private final int EMPTY_CARD = 0;
    private final int MESSAGE = 2;
    private final int REPORTS = 3;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private ComposeFooterInterface composeFooterInterface;

    private List<MultiAction> multiActions;
    private int previewLength;
    private String msg;
    private Drawable errorIcon;
    private WidgetViewMoreEnum widgetViewMoreEnum;

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    private LoginModel loginModel;

    private boolean isLoginNeeded;

    public ChartListWidgetAdapter(Context mContext, String type, boolean isEnabled, boolean isFromFullView) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.type = type;
        notifyDataSetChanged();
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
    public void setViewMoreEnum(WidgetViewMoreEnum widgetViewMoreEnum) {
        this.widgetViewMoreEnum=widgetViewMoreEnum;
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
            return new ViewHolder(inflater.inflate(R.layout.chart_list_item, parent, false));
    }


    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderData, int position) {
        if(holderData.getItemViewType() ==  REPORTS){
//            final Element model = eventList.get(position);
            ReportsViewHolder holder = (ReportsViewHolder) holderData;

            //holder.txt.setText(model.getText());
            holder.loginBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mContext instanceof Activity) {
                        Intent intent = new Intent(mContext, GenericWebViewActivity.class);
                        intent.putExtra("url", loginModel.getUrl());
                        intent.putExtra("header", mContext.getResources().getString(R.string.app_name));
                        ((Activity)mContext).startActivityForResult(intent, BundleConstants.REQ_CODE_REFRESH_CURRENT_PANEL);
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


        }

        else {

            ViewHolder holder = (ViewHolder) holderData;
            final Widget.Element model = eventList.get(position);

            int dp80 = (int) dp1 * 80;

            String title = model.getTitle();
            String text = model.getText();
            String color = model.getTheme();
            String type = model.getType();

            if (!StringUtils.isNullOrEmpty(title)) {
                holder.title.setText(title);
            }
            int shape = 1;
            if (!StringUtils.isNullOrEmpty(type) && type.equalsIgnoreCase("circle")) {
                shape = GradientDrawable.OVAL;
            } else if (!StringUtils.isNullOrEmpty(type) && type.equalsIgnoreCase("rectange")) {
                shape = GradientDrawable.RECTANGLE;
            } else if (!StringUtils.isNullOrEmpty(type) && type.equalsIgnoreCase("ring")) {
                shape = GradientDrawable.RING;
            }

            if (!StringUtils.isNullOrEmpty(text)) {
                holder.text.setText(text);

                Drawable dr = mContext.getDrawable(R.drawable.selected_video);

                if (!StringUtils.isNullOrEmpty(color)) {
                    GradientDrawable drawable = (GradientDrawable) dr;
                    drawable.setColor(Color.parseColor(color));
                    drawable.setStroke(10, Utility.getDarkerColor(Color.parseColor(color), 0.7f));
                    drawable.setShape(shape);
                    holder.text.setBackground(drawable);
                }
            }
        }
    }

    public void setCalData(List<Widget.Element> data) {
        this.eventList = (ArrayList<Widget.Element>) data;
        notifyDataSetChanged();
    }
    public ArrayList getData(){
        return (ArrayList) this.eventList;
    }

    @Override
    public void setData(ArrayList data) {

    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.txtId);
            title = itemView.findViewById(R.id.titleTV);
        }
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
