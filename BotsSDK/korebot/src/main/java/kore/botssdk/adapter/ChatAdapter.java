package kore.botssdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.Utils;
import kore.botssdk.view.AttendeeSlotSelectionView;
import kore.botssdk.view.KaBaseBubbleContainer;
import kore.botssdk.view.KaBaseBubbleLayout;
import kore.botssdk.view.KaReceivedBubbleContainer;
import kore.botssdk.view.KaReceivedBubbleLayout;
import kore.botssdk.view.KaSendBubbleLayout;
import kore.botssdk.view.viewUtils.BubbleViewUtil;

/**
 * Created by Shiva Krishna on 11/17/2017.
 */
public class ChatAdapter extends BaseAdapter {

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
    private ListView listView;
    int viewWidth;
    int dp1;
    int position;
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
      //  baseBotMessageArrayList.add(Utils.buildBotMessage("Hi, I'm your Virtual Assistant. I can help you with payments, FAQs and much more.\n \n To get started, please enter your query. You may ask questions like:\n \"What is my balance?\" \n \"I'd like to make a payment.\"",null));
        AttendeeSlotSelectionView.dataMap = new HashMap<>();
    }

    @Override
    public int getViewTypeCount() {
        return BUBBLE_ADAPTER_DIFFERENT_VIEW_COUNTS;
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

    @Override
    public int getCount() {
        return baseBotMessageArrayList.size();
    }

    @Override
    public BaseBotMessage getItem(int position) {
        if (baseBotMessageArrayList != null && position <= baseBotMessageArrayList.size() - 1 && position != -1) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        this.position = position;
        boolean isRight = false;

        int itemViewType = getItemViewType(position);
        if (itemViewType == BUBBLE_RIGHT_LAYOUT) {
            // Inflation for Right side
            if (convertView == null || convertView.getId() != R.id.send_bubble_layout_container)
                convertView = ownLayoutInflater.inflate(R.layout.ka_bubble_layout_right, null);
            isRight = true;
        } else {
            // Inflation for Left side
            if (convertView == null || convertView.getId() != R.id.received_bubble_layout_container)
                convertView = ownLayoutInflater.inflate(R.layout.ka_bubble_layout_left, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView, position);
        }
        if (headersMap.get(DateUtils.formattedSentDateV6(getItem(position).getCreatedInMillis())) == null) {
            headersMap.put(DateUtils.formattedSentDateV6(getItem(position).getCreatedInMillis()), position);
        }

        if (listView == null) {
            listView = (ListView) parent;
        }
        if (convertView.getTag() != null) {
            // Stuffs
            ViewHolder holder = (ViewHolder) convertView.getTag();

            //Bubble Population logic
            holder.baseBubbleContainer.setDimensions(BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);
            holder.baseBubbleLayout.setContinuousMessage(false);
            holder.baseBubbleLayout.setGroupMessage(false);
            holder.baseBubbleLayout.setComposeFooterInterface(composeFooterInterface);
            holder.baseBubbleLayout.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            holder.baseBubbleLayout.setActivityContext(activityContext);
            holder.baseBubbleLayout.fillBubbleLayout(position, position == getCount() - 1, getItem(position), true, BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);
            holder.textView.setText(DateUtils.formattedSentDateV6(getItem(position).getCreatedInMillis()));
            holder.headerView.setVisibility(position != 0 && headersMap.get(DateUtils.formattedSentDateV6(getItem(position).getCreatedInMillis())) == position ? View.VISIBLE :View.GONE);

        }

        return convertView;
    }

    /**
     * View Holder Initialization
     */
    private View initializeViewHolder(View view, int position) {

        ViewHolder holder = new ViewHolder();

        if (getItemViewType(position) == BUBBLE_RIGHT_LAYOUT) {
            // Right Side
            holder.baseBubbleLayout = (KaSendBubbleLayout) view.findViewById(R.id.sendBubbleLayout);
            holder.baseBubbleContainer = (KaBaseBubbleContainer) view.findViewById(R.id.send_bubble_layout_container);
        } else {
            // Left Side
            holder.baseBubbleLayout = (KaReceivedBubbleLayout) view.findViewById(R.id.receivedBubbleLayout);
            holder.baseBubbleContainer = (KaReceivedBubbleContainer) view.findViewById(R.id.received_bubble_layout_container);
        }
        holder.headerView = view.findViewById(R.id.headerLayout);
        holder.textView = view.findViewById(R.id.filesSectionHeader);

        view.setTag(holder);

        return view;

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

    private static class ViewHolder {
        KaBaseBubbleContainer baseBubbleContainer;
        KaBaseBubbleLayout baseBubbleLayout;
        RelativeLayout bubbleLayoutContainer;
        View headerView;
        TextView textView;
    }

    public void addBaseBotMessage(BaseBotMessage baseBotMessage) {
        baseBotMessageArrayList.add(baseBotMessage);
        notifyDataSetChanged();
    }

    public void addBaseBotMessages(ArrayList<BaseBotMessage> list) {
        baseBotMessageArrayList.addAll(0, list);
        notifyDataSetChanged();
    }

    public void setShallShowProfilePic(boolean shallShowProfilePic) {
        this.shallShowProfilePic = shallShowProfilePic;
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }
}
