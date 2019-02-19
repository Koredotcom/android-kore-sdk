package kore.botssdk.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import io.jsonwebtoken.lang.Collections;
import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.utils.SelectionUtils;
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
    private final ClipboardManager clipboardManager;

    Context context;
    private Activity activityContext;
    private LayoutInflater ownLayoutInflater;
    private HashMap<String, Integer> headersMap = new HashMap<>();

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private int BUBBLE_CONTENT_LAYOUT_WIDTH;
    private int BUBBLE_CONTENT_LAYOUT_HEIGHT;


    private ArrayList<BaseBotMessage> baseBotMessageArrayList;

    private static final int BUBBLE_LEFT_LAYOUT = 0;
    private static final int BUBBLE_RIGHT_LAYOUT = BUBBLE_LEFT_LAYOUT + 1;

    public ChatAdapter(Context context) {
        this.context = context;
        ownLayoutInflater = LayoutInflater.from(context);
        clipboardManager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        BUBBLE_CONTENT_LAYOUT_WIDTH = BubbleViewUtil.getBubbleContentWidth();
        BUBBLE_CONTENT_LAYOUT_HEIGHT = BubbleViewUtil.getBubbleContentHeight();
        baseBotMessageArrayList = new ArrayList<>();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(ownLayoutInflater.inflate( i == BUBBLE_RIGHT_LAYOUT ?  R.layout.ka_bubble_layout_right : R.layout.ka_bubble_layout_left, null),i);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.baseBubbleContainer.setDimensions(BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);
        holder.baseBubbleLayout.setContinuousMessage(position == 0 || checkIsContinuous(position));
        holder.baseBubbleLayout.setGroupMessage(false);
        holder.baseBubbleLayout.setComposeFooterInterface(composeFooterInterface);
        holder.baseBubbleLayout.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        holder.baseBubbleLayout.setActivityContext(activityContext);
        holder.baseBubbleLayout.fillBubbleLayout(position, position == getItemCount() - 1, getItem(position), true, BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);
        holder.textView.setText(getItem(position).getFormattedDate());
        if(Collections.isEmpty(headersMap)) {
            prepareHeaderMap();
        }
        //TODO Need to re visit : Handled crash in a bad way(if you change time zone and come back app crashing)
        boolean fDate = false;
        try {
            fDate = headersMap.get(getItem(position).getFormattedDate()) == position;
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.headerView.setVisibility(getItem(position) != null && fDate ? View.VISIBLE : View.GONE);
        if(getItemViewType(position) == BUBBLE_RIGHT_LAYOUT) {
            holder.baseBubbleLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BotRequest botRequest = (BotRequest) getItem(position);
                    ClipData clip = ClipData.newPlainText("message", botRequest.getMessage().getBody());
                    clipboardManager.setPrimaryClip(clip);
                    Toast.makeText(context,"Message copied to clipboard",Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            holder.baseBubbleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BotRequest botRequest = (BotRequest) getItem(position);
                    composeFooterInterface.copyMessageToComposer(botRequest.getMessage().getBody());
                }
            });
        }
    }

    private boolean checkIsContinuous(int position) {
        if(getItem(position).isSend() && getItem(position-1).isSend()){
            return true;
        }else return !getItem(position).isSend() && !getItem(position - 1).isSend();
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
        if (headersMap.get(baseBotMessage.getFormattedDate()) == null) {
            headersMap.put(baseBotMessage.getFormattedDate(), baseBotMessageArrayList.size() -1);
        }
        SelectionUtils.resetSelectionTasks();
        SelectionUtils.resetSelectionSlots();
        notifyDataSetChanged();
    }


    public void addBaseBotMessages(ArrayList<BaseBotMessage> list) {
        baseBotMessageArrayList.addAll(0, list);
        prepareHeaderMap();
        notifyItemRangeInserted(0, list.size() - 1);
    }



    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }

    private void prepareHeaderMap() {
        int i = 0;
        headersMap.clear();
        for (i = 0; i < baseBotMessageArrayList.size(); i++) {
            BaseBotMessage baseBotMessage = baseBotMessageArrayList.get(i);
            if (headersMap.get(baseBotMessage.getFormattedDate()) == null) {
                headersMap.put(baseBotMessage.getFormattedDate(), i);
            }
        }

    }
}
