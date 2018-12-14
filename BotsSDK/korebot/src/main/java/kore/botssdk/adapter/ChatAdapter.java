package kore.botssdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.view.AttendeeSlotSelectionView;
import kore.botssdk.view.KaBaseBubbleContainer;
import kore.botssdk.view.KaBaseBubbleLayout;
import kore.botssdk.view.KaReceivedBubbleContainer;
import kore.botssdk.view.KaReceivedBubbleLayout;
import kore.botssdk.view.KaSendBubbleContainer;
import kore.botssdk.view.KaSendBubbleLayout;
import kore.botssdk.view.viewUtils.BubbleViewUtil;

/**
 * Created by Shiva Krishna on 11/17/2017.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>  {

    public static String LOG_TAG = ChatAdapter.class.getSimpleName();

    Context context;
    Activity activityContext;
    private LayoutInflater ownLayoutInflater;
    private HashMap<String, Integer> headersMap = new HashMap<>();

    public ComposeFooterFragment.ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private int BUBBLE_CONTENT_LAYOUT_WIDTH;
    private int BUBBLE_CONTENT_LAYOUT_HEIGHT;
    int viewWidth;
    int dp1;
    boolean shallShowProfilePic;

    private ArrayList<BaseBotMessage> baseBotMessageArrayList;

    public static final int BUBBLE_ADAPTER_DIFFERENT_VIEW_COUNTS = 3;
    public static final int BUBBLE_LEFT_LAYOUT = 0;
    public static final int BUBBLE_RIGHT_LAYOUT = BUBBLE_LEFT_LAYOUT + 1;

    public ChatAdapter(Context context) {
        this.context = context;
        ownLayoutInflater = LayoutInflater.from(context);
        dp1 = (int) AppControl.getInstance(context).getDimensionUtil().dp1;

        BUBBLE_CONTENT_LAYOUT_WIDTH = BubbleViewUtil.getBubbleContentWidth();
        BUBBLE_CONTENT_LAYOUT_HEIGHT = BubbleViewUtil.getBubbleContentHeight();
        viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        baseBotMessageArrayList = new ArrayList<>();
        AttendeeSlotSelectionView.dataMap = new HashMap<>();
    }

/*
    @Override
    public int getViewTypeCount() {
        return BUBBLE_ADAPTER_DIFFERENT_VIEW_COUNTS;
    }
*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(ownLayoutInflater.inflate( i == BUBBLE_RIGHT_LAYOUT ?  R.layout.ka_bubble_layout_right : R.layout.ka_bubble_layout_left, null),i);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (headersMap.get(DateUtils.formattedSentDateV6(getItem(position).getCreatedInMillis())) == null) {
            headersMap.put(DateUtils.formattedSentDateV6(getItem(position).getCreatedInMillis()), position);
        }
        holder.baseBubbleContainer.setDimensions(BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);
        holder.baseBubbleLayout.setContinuousMessage(false);
        holder.baseBubbleLayout.setGroupMessage(false);
        holder.baseBubbleLayout.setComposeFooterInterface(composeFooterInterface);
        holder.baseBubbleLayout.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        holder.baseBubbleLayout.setActivityContext(activityContext);
        holder.baseBubbleLayout.fillBubbleLayout(position, position == getItemCount() - 1, getItem(position), true, BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);
        holder.textView.setText(DateUtils.formattedSentDateV6(getItem(position).getCreatedInMillis()));
        holder.headerView.setVisibility(position != 0 && headersMap.get(DateUtils.formattedSentDateV6(getItem(position).getCreatedInMillis())) == position ? View.VISIBLE : View.GONE);
    }


    @Override
    public int getItemViewType(int position) {

        BaseBotMessage baseBotMessage = getItem(position);

        if (baseBotMessage.isSend()) {
            return BUBBLE_RIGHT_LAYOUT;
        } else {
            return BUBBLE_LEFT_LAYOUT;
        }
    }

    public BaseBotMessage getItem(int position) {
        if (position <= baseBotMessageArrayList.size() - 1 && position != -1) {
            return baseBotMessageArrayList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return baseBotMessageArrayList.size();
    }



/*    private class HeaderViewHolder {
        TextView txtViewHeader;
    }*/

/*    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = ownLayoutInflater.inflate(R.layout.kora_timestamps_header, parent, false);
            holder.txtViewHeader = (TextView) convertView.findViewById(R.id.filesSectionHeader);
            KaFontUtils.applyCustomFont(context, holder.txtViewHeader);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }


        BaseBotMessage baseBotMessage = ((BaseBotMessage) getItem(position));
        if (baseBotMessage != null) {
            holder.txtViewHeader.setText(DateUtils.formattedSentDateV6(baseBotMessage.getCreatedInMillis()));
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        try {
            BaseBotMessage baseBotMessage = ((BaseBotMessage) getItem(position));
            if (baseBotMessage != null)
                return headersMap.get(DateUtils.formattedSentDateV6(baseBotMessage.getCreatedInMillis()));
            else return 0;
        } catch (Exception e) {
//            e.printStackTrace();
            return 0;
        }
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder{
        KaBaseBubbleContainer baseBubbleContainer;
        KaBaseBubbleLayout baseBubbleLayout;
        RelativeLayout bubbleLayoutContainer;
        View headerView;
        TextView textView;
        public ViewHolder(View view,int viewType){
            super(view);
            if ( viewType== BUBBLE_RIGHT_LAYOUT) {
                // Right Side
                baseBubbleLayout = (KaSendBubbleLayout) view.findViewById(R.id.sendBubbleLayout);
                baseBubbleContainer = (KaSendBubbleContainer) view.findViewById(R.id.send_bubble_layout_container);
            } else {
                // Left Side
                baseBubbleLayout = (KaReceivedBubbleLayout) view.findViewById(R.id.receivedBubbleLayout);
                baseBubbleContainer = (KaReceivedBubbleContainer) view.findViewById(R.id.received_bubble_layout_container);
            }
            headerView = view.findViewById(R.id.headerLayout);
            textView = view.findViewById(R.id.filesSectionHeader);

        }
    }

    public void addBaseBotMessage(BaseBotMessage baseBotMessage) {
        baseBotMessageArrayList.add(baseBotMessage);
        notifyDataSetChanged();
    }

    public void addBaseBotMessages(ArrayList<BaseBotMessage> list) {
        baseBotMessageArrayList.addAll(0, list);
        notifyItemRangeInserted(0, list.size() - 1);
        //notifyItemRangeInserted(getItemCount(),list.size());
    }

    public void setShallShowProfilePic(boolean shallShowProfilePic) {
        this.shallShowProfilePic = shallShowProfilePic;
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }
}
