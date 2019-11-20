package kore.botssdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.dialogs.WidgetActionSheetFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.CalEventsTemplateModel.Duration;
import kore.botssdk.models.MultiAction;
import kore.botssdk.models.Widget.Element;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;
import kore.botssdk.view.viewUtils.CircleTransform;

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

    public ArrayList<Element> getEventList() {
        return eventList;
    }

    private Duration _cursor;

    ArrayList<Element> eventList = new ArrayList<>();
    private LayoutInflater inflater = null;
    private CalendarEventsAdapter.EventSelectionListener eventSelectionListener;
    private Context mContext;

    String skillName;


    private int DATA_FOUND = 1;
    private int EMPTY_CARD = 0;
    private int MESSAGE = 2;

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
    int preview_length;
    String msg;
    Drawable errorIcon;

    public DefaultWidgetAdapter(Context mContext, String type, boolean isEnabled, boolean isFromFullView) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.type = type;
        notifyDataSetChanged();
        selectedIds = new ArrayList<>();

    }

    public void clearSelectedItems() {
        selectedIds.clear();
    }

    public Element getItem(int position) {
        if (position < eventList.size())
            return eventList.get(position);
        else return null;
    }


    @Override
    public int getItemViewType(int position) {
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
        if (viewType == EMPTY_CARD || viewType == MESSAGE) {
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderData, int position) {
        if (holderData.getItemViewType() == EMPTY_CARD || holderData.getItemViewType() == MESSAGE) {
            EmptyWidgetViewHolder emptyHolder = (EmptyWidgetViewHolder) holderData;

            emptyHolder.tv_disrcription.setText(msg != null ? msg : "No data");
            emptyHolder.img_icon.setImageDrawable(holderData.getItemViewType() == EMPTY_CARD ? ContextCompat.getDrawable(mContext, R.drawable.no_meeting) : errorIcon);


        } else {

            DefaultWidgetAdapter.ViewHolder holder = (DefaultWidgetAdapter.ViewHolder) holderData;

            final Element model = eventList.get(position);

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
                Picasso.get().load(model.getIcon()).transform(new CircleTransform()).into(holder.imageIcon);
            } else {
                holder.imageIcon.setVisibility(GONE);
            }

            if (model.getActions() != null && model.getActions().size() > 0) {
                holder.icon_down.setVisibility(VISIBLE);
                holder.icon_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        {
                            WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                            bottomSheetDialog.setisFromFullView(false);
                            bottomSheetDialog.setData(model);
                            bottomSheetDialog.setVerticalListViewActionHelper(verticalListViewActionHelper);
                            bottomSheetDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "add_tags");
                        }
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

                ButtonListAdapter buttonRecyclerAdapter = new ButtonListAdapter(mContext, model.getButton());
                buttonRecyclerAdapter.setSkillName(skillName);
                holder.recyclerView.setAdapter(buttonRecyclerAdapter);
                buttonRecyclerAdapter.notifyDataSetChanged();
            }

            holder.innerlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getDefaultAction() != null && model.getDefaultAction().getType() != null && model.getDefaultAction().getType().equals("url")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getDefaultAction().getUrl()));
                        mContext.startActivity(browserIntent);
                    }
                }
            });
            if (position == eventList.size() - 1 && eventList.size() < 3) {
                holder.divider.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
      //  return eventList != null && eventList.size() > 0 ? eventList.size() : 1;
        if(Utility.isIsSingleItemInList())
        {
            return eventList != null && eventList.size() > 0 ? eventList.size() : 1;
        }
        return eventList != null && eventList.size() > 0 ? (!isExpanded && eventList.size() > preview_length ? preview_length : eventList.size()) : 1;
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

    public void setCalData(List<Element> data) {
        this.eventList = (ArrayList<Element>) data;
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

    public void setPreviewLength(int preview_length) {
        this.preview_length = preview_length;
    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg = msg;
        this.errorIcon = errorIcon;
    }


    public interface EventSelectionListener {
        void onEventSelected(String url);
    }

    public void setMoreSelectionListener(CalendarEventsAdapter.EventSelectionListener eventSelectionListener) {
        this.eventSelectionListener = eventSelectionListener;
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
}
