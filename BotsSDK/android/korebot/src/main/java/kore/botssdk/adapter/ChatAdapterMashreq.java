package kore.botssdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import io.jsonwebtoken.lang.Collections;
import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.KaBaseBubbleContainer;
import kore.botssdk.view.KaBaseBubbleLayout;
import kore.botssdk.view.KaReceivedBubbleContainer;
import kore.botssdk.view.KaReceivedBubbleLayout;
import kore.botssdk.view.KaSendBubbleContainer;
import kore.botssdk.view.KaSendBubbleLayout;
import kore.botssdk.view.viewUtils.BubbleViewUtil;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class ChatAdapterMashreq extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //    private static String LOG_TAG = ChatAdapter.class.getSimpleName();
    Context context;
    private Activity activityContext;
    private LayoutInflater ownLayoutInflater;
    private HashMap<String, Integer> headersMap = new HashMap<>();
    private boolean isAlpha = false;
    private int selectedItem = -1;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 3;

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


    public ArrayList<BaseBotMessage> getBaseBotMessageArrayList() {
        return baseBotMessageArrayList;
    }

    private ArrayList<BaseBotMessage> baseBotMessageArrayList;

    private static final int BUBBLE_LEFT_LAYOUT = 0;
    private static final int BUBBLE_RIGHT_LAYOUT = BUBBLE_LEFT_LAYOUT + 1;

    public ChatAdapterMashreq(Context context) {
        this.context = context;
        ownLayoutInflater = LayoutInflater.from(context);
        BUBBLE_CONTENT_LAYOUT_WIDTH = BubbleViewUtil.getBubbleContentWidth();
        BUBBLE_CONTENT_LAYOUT_HEIGHT = BubbleViewUtil.getBubbleContentHeight();
        baseBotMessageArrayList = new ArrayList<>();
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            return new VHHeader(ownLayoutInflater.inflate(R.layout.ka_bubble_layout_header, null));
        }
        else
            return new ChatAdapterMashreq.ViewHolder(ownLayoutInflater.inflate( viewType == BUBBLE_RIGHT_LAYOUT ?  R.layout.ka_bubble_layout_right : R.layout.ka_bubble_layout_left, null),viewType);

    }

    private boolean isClickable(int position, BaseBotMessage message){
        boolean clickable = false;
        if(!message.isSend()){
            BotResponse resp = (BotResponse) message;
            ComponentModel model = resp.getMessage().get(0).getComponent();
            if (model != null && model.getPayload() != null && model.getPayload().getPayload() != null) {
                PayloadOuter outer = model.getPayload();
                PayloadInner inner = outer.getPayload();
                if(!StringUtils.isNullOrEmpty(inner.getTemplate_type()) && inner.getTemplate_type().equals(BotResponse.TEMPLATE_TYPE_HIDDEN_DIALOG)){
                    clickable = (position == getItemCount() -2) ;
                }else{
                    clickable = (position == getItemCount() -1) ;
                }
            }
        }
        return clickable;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position)
    {
        if (holder instanceof ChatAdapterMashreq.ViewHolder && position != 0)
        {
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleContainer.setAlpha(isAlpha && position != getItemCount() -1 ? 0.4f : 1.0f);
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleContainer.setViewActive(!isAlpha || position == getItemCount()-1);
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleContainer.setDimensions(BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setContinuousMessage(position == 1 || checkIsContinuous(position));
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setGroupMessage(false);
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setComposeFooterInterface(composeFooterInterface);
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setActivityContext(activityContext);
            ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.fillBubbleLayout(position,position == getItemCount() -1 , getItem(position));
//            ((ChatAdapterMashreq.ViewHolder) holder).textView.setText(getItem(position).getFormattedDate());

            if(Collections.isEmpty(headersMap)) {
                prepareHeaderMap();
            }
            //TODO Need to re visit : Handled crash in a bad way(if you change time zone and come back app crashing)
            boolean fDate = false;
//            try {
//                fDate = headersMap.get(getItem(position).getFormattedDate()) == position;
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            ((ChatAdapterMashreq.ViewHolder) holder).headerView.setVisibility(getItem(position) != null && fDate ? View.VISIBLE : View.GONE);
            if(selectedItem == position){
                ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setTimeStampVisible();
            }
            if(getItemViewType(position) == BUBBLE_RIGHT_LAYOUT) {
                ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(selectedItem != -1){
                            notifyItemChanged(selectedItem);
                        }
                        selectedItem = position;
                        ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setTimeStampVisible();
                        return true;
                    }
                });
                ((ChatAdapterMashreq.ViewHolder) holder).baseBubbleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BotRequest botRequest = (BotRequest) getItem(position);
                        if(composeFooterInterface != null)  composeFooterInterface.copyMessageToComposer(botRequest.getMessage().getBody(), false);
                    }
                });
            }
        }
        else
        {

        }
    }

    private boolean checkIsContinuous(int position) {
        if(getItem(position).isSend() && getItem(position-1).isSend()){
            return true;
        }else return !getItem(position).isSend() && !getItem(position - 1).isSend();
    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else
        {
            BaseBotMessage baseBotMessage = getItem(position);

            if(baseBotMessage != null)
            {
                if (baseBotMessage.isSend()) {
                    return BUBBLE_RIGHT_LAYOUT;
                } else {
                    return BUBBLE_LEFT_LAYOUT;
                }
            }

            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public BaseBotMessage getItem(int position) {
        if (baseBotMessageArrayList != null && position <= baseBotMessageArrayList.size() && position != -1 && position != 0) {
            return baseBotMessageArrayList.get(position - 1);
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
        return baseBotMessageArrayList.size() + 1;
    }

    public boolean isAlpha() {
        return isAlpha;
    }

    public void setAlpha(boolean alpha) {
        isAlpha = alpha;
    }


    class VHHeader extends RecyclerView.ViewHolder
    {
        LinearLayout llListHeader;

        public VHHeader(View itemView)
        {
            super(itemView);
            llListHeader = (LinearLayout) itemView.findViewById(R.id.llListHeader);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, (int)(15 * DimensionUtil.dp1));
            llListHeader.setLayoutParams(lp);
        }
    }

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
        isAlpha = false;
        notifyDataSetChanged();
    }



    public void addBaseBotMessages(ArrayList<BaseBotMessage> list) {
        baseBotMessageArrayList.addAll(0, list);
        prepareHeaderMap();
        if(selectedItem != -1) {
            selectedItem = selectedItem + list.size()-1;
        }
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
