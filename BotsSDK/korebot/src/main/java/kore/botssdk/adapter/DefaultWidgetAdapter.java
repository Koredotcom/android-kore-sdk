package kore.botssdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.CalEventsTemplateModel.Duration;
import kore.botssdk.models.MultiAction;
import kore.botssdk.models.WCalEventsTemplateModel;
import kore.botssdk.models.Widget.Element;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;
import kore.botssdk.view.viewUtils.KaRoundedCornersTransform;

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


   /* public void setEventList(ArrayList<WCalEventsTemplateModel> eventList) {
        if (eventList != null) {
            this.eventList.clear();
            this.eventList.addAll(eventList);
            this.title = "SHOW MORE";

        }
    }*/

    ArrayList<Element> eventList = new ArrayList<>();
    private LayoutInflater inflater = null;
    private int EVENTS_LIST_LIMIT = 3;
    private String title = "SHOW MORE";
    private CalendarEventsAdapter.EventSelectionListener eventSelectionListener;
    private Context mContext;


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
    private boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private Gson gson = new Gson();
    private boolean isFromWidget;

    public boolean isFromWidget() {
        return isFromWidget;
    }

    private Drawable selectedCheck;
    private Drawable unSelectedCheck;

    private Drawable insetDivider, normalDivider;

    public void setFromWidget(boolean fromWidget) {
        isFromWidget = fromWidget;
    }

    List<MultiAction> multiActions;
    int preview_length;
    private boolean isFromFullView;
    String msg;
    Drawable errorIcon;

    public DefaultWidgetAdapter(Context mContext, String type, boolean isEnabled, boolean isFromFullView) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.type = type;
        this.isEnabled = isEnabled;
        this.isFromFullView = isFromFullView;
        notifyDataSetChanged();
        selectedIds = new ArrayList<>();
        selectedCheck = mContext.getResources().getDrawable(R.mipmap.checkbox_on);
        unSelectedCheck = mContext.getResources().getDrawable(R.mipmap.checkbox_off);

        insetDivider = mContext.getResources().getDrawable(R.drawable.inset_65_divider);
        normalDivider = mContext.getResources().getDrawable(R.drawable.inset_divider_meetings);
//        EVENTS_LIST_LIMIT = 3;
//        title = "SHOW MORE";txtDateAndTime
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
        /*if (eventList != null && eventList.size() > 0) {
            return DATA_FOUND;
        }

        if (msg != null && !msg.equalsIgnoreCase("")) {
            return MESSAGE;
        }*/
        return DATA_FOUND;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

  /*      if (viewType == EMPTY_CARD || viewType == MESSAGE) {


            View view = inflater.inflate(R.layout.card_empty_widget_layout, parent, false);
            return new EmptyWidgetViewHolder(view);
        } else*/
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

            emptyHolder.tv_disrcription.setText(msg != null ? msg : "No Upcoming Meetings");
            emptyHolder.img_icon.setImageDrawable(holderData.getItemViewType() == EMPTY_CARD ? ContextCompat.getDrawable(mContext, R.drawable.no_meeting) : errorIcon);


        } else {

            DefaultWidgetAdapter.ViewHolder holder = (DefaultWidgetAdapter.ViewHolder) holderData;

          /*  holder.icon_notes.setTypeface(ResourcesCompat.getFont(mContext, R.font.icomoon));
            holder.icon_dial.setTypeface(ResourcesCompat.getFont(mContext, R.font.icomoon));
            holder.icon_join.setTypeface(ResourcesCompat.getFont(mContext, R.font.icomoon));*/


            final Element model = (Element) eventList.get(position);

            holder.txtTitle.setText(model.getTitle());
            holder.txtSubTitle.setText(StringUtils.isNullOrEmpty(model.getSub_title())?"No title":model.getSub_title());
            holder.txtText.setText("No description"/*model.getText()*/);

            if(model.getIcon()!=null)
            Picasso.get().load(model.getIcon()).transform(new KaRoundedCornersTransform()).into(holder.imageIcon);

            /*holder.icon_down.setTypeface(ResourcesCompat.getFont(mContext, R.font.icomoon));

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.round_shape_common);
            try {
                ((GradientDrawable) drawable).setColor(mContext.getResources().getColor(R.color.color_d8d8d8));

            } catch (Exception e) {

            }
            holder.icon_down.setBackground(drawable);*/

           /* if (selectedIds.size() > 0) {
                holder.checkbox.setVisibility(VISIBLE);
                holder.checkbox.setImageDrawable(selectedIds.contains(model.getData().getEventId()) ? selectedCheck : unSelectedCheck);
            } else {
                holder.checkbox.setVisibility(GONE);
            }

            holder.icon_down.setVisibility(selectedIds.size() > 0 ? GONE : VISIBLE);


            holder.txtTitle.setText(model.getTitle());
            // holder.txtPlace.setText(model.getWhere());
            if (!StringUtils.isNullOrEmptyWithTrim(model.getLocation())) {
                holder.txtPlace.setText(model.getLocation());
                holder.txtPlace.setVisibility(VISIBLE);

            } else {
                holder.txtPlace.setVisibility(GONE);

            }
            // holder.tv_time.setText(DateUtils.calendar_list_format_2.format(model.getData().getDuration().getStart()) + "\n" + DateUtils.calendar_list_format_2.format(model.getData().getDuration().getEnd()));

            if (!StringUtils.isNullOrEmpty(model.getData().getReqTextToDisp()))
                holder.tv_time.setText(model.getData().getReqTextToDisp());
            else
                holder.tv_time.setText(DateUtils.calendar_list_format_2.format(model.getData().getDuration().getStart()) + "\n" + DateUtils.calendar_list_format_2.format(model.getData().getDuration().getEnd()));

            holder.tv_users.setText(getFormatedAttendiesFromList(model.getData().getAttendees()));
            if (position == 0 || model.isShowDate()) {
                holder.tvborder.setVisibility(VISIBLE);

                holder.txtDateTime.setVisibility(VISIBLE);
            } else {
                holder.tvborder.setVisibility(GONE);
                holder.txtDateTime.setVisibility(GONE);
            }

            if (model.getData() != null && model.getData().getColor() != null)
                holder.sideBar.setBackgroundColor(Color.parseColor(model.getData().getColor()));

            if (position < getItemCount() - 1) {
                holder.divider.setBackground(getItem(position + 1).isShowDate() ? insetDivider : normalDivider);
            }*/

            holder.innerlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("IKIDO","HI");
                    if(model.getDefaultAction().getType().equals("url")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getDefaultAction().getUrl()));
                        mContext.startActivity(browserIntent);
                    }
                }
            });
            if (position == eventList.size() - 1 && eventList.size() < 3)
                holder.divider.setVisibility(View.GONE);


           /* holder.icon_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                        bottomSheetDialog.setisFromFullView(isFromFullView);
                        bottomSheetDialog.setData(model);
                        bottomSheetDialog.setVerticalListViewActionHelper(verticalListViewActionHelper);
                        bottomSheetDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "add_tags");

                    }

                }
            });
            holder.innerlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET.equalsIgnoreCase(type) || isFromWidget()) {
                        //from left widget click

                        if (selectedIds != null && selectedIds.size() > 0 && verticalListViewActionHelper != null) {
                            // multiple item can be selected after long press and single click on other items
                            if (selectedIds.contains(model.getData().getEventId())) {
                                selectedIds.remove(model.getData().getEventId());
                                holder.innerlayout.setSelected(false);
                            } else {
                                selectedIds.add(model.getData().getEventId());
                                holder.innerlayout.setSelected(true);
                            }
                            verticalListViewActionHelper.widgetItemSelected(true, selectedIds.size());
                            notifyDataSetChanged();

                        }
                        else {
                            if(verticalListViewActionHelper!=null) {
                                verticalListViewActionHelper.calendarItemClicked(BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, (WCalEventsTemplateModel) model);
                            }
                        }
                    } else if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS.equalsIgnoreCase(type)) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (AppPermissionsHelper.hasPermission(mContext, Manifest.permission.READ_CALENDAR)) {
                                    launchNativeView(model.getTitle(), (long) model.getData().getDuration().getStart());
                                } else {
                                    gModel = model;
                                    AppPermissionsHelper.requestForPermission((Activity) mContext, Manifest.permission.READ_CALENDAR, CAL_PERMISSION_REQUEST);
                                }
                            } else {

                                launchNativeView(model.getTitle(), (long) model.getData().getDuration().getStart());
                            }

                        } catch (Exception e) {

                            launchWebView(model.getData().getHtmlLink());
                        }

                    } else if (isEnabled) {

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("meetingId", model.getData().getEventId());
                        String message = "Cancel \"" + model.getTitle() + "\" " + getDateinDayFormat((long) model.getData().getDuration().getStart()) + ", " + getTimeInAmPm((long) model.getData().getDuration().getStart()) + " - " + getTimeInAmPm((long) model.getData().getDuration().getEnd());
                        if (composeFooterInterface != null) {
                            composeFooterInterface.sendWithSomeDelay(message, gson.toJson(hashMap), 0, true);
                        } else {
                            KoreEventCenter.post(new CancelEvent(message, gson.toJson(hashMap), 0, true));
                            ((Activity) mContext).finish();
                        }
                    }
                }
            });*/
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
        /*if (eventList != null) {
            try {
                this.eventList = expandEventList(eventList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.eventList = sortEventList(eventList);

            if (eventList.size() > 3) {
                if (verticalListViewActionHelper != null)
                    verticalListViewActionHelper.meetingWidgetViewMoreVisibility(true);
            }
        }*/
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

    public Duration get_cursor() {
        return _cursor;
    }

    public void set_cursor(Duration _cursor) {
        this._cursor = _cursor;
    }

    public interface EventSelectionListener {
        void onEventSelected(String url);
    }

    public void setMoreSelectionListener(CalendarEventsAdapter.EventSelectionListener eventSelectionListener) {
        this.eventSelectionListener = eventSelectionListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView icon_down;
        LinearLayout layoutDetails, innerlayout;
        public View sideBar;
        public TextView txtTitle;
        public TextView txtSubTitle;
        public TextView txtText;
        public TextView tvborder, tv_users;
        public ImageView imageIcon;
        public View divider;
        TextView icon_dial, icon_join, icon_notes, time_tostart_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            txtText = (TextView) itemView.findViewById(R.id.txtText);
            imageIcon = (ImageView) itemView.findViewById(R.id.imageIcon);
            innerlayout = itemView.findViewById(R.id.innerlayout);

        }
    }


    private WCalEventsTemplateModel gModel;
    private final int CAL_PERMISSION_REQUEST = 3221;

    private static final int EVENT_INDEX_ID = 0;
    private static final int EVENT_INDEX_START = 1;
    private static final int EVENT_INDEX_END = 2;
    private static final int EVENT_INDEX_DURATION = 3;

    private static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,      // 0
            CalendarContract.Events.DTSTART,  // 1
            CalendarContract.Events.DTEND,    // 2
            CalendarContract.Events.DURATION, // 3
    };
}
