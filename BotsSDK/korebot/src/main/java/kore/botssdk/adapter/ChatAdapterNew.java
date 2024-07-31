package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.tableview.TableResponsiveView;
import kore.botssdk.viewholders.AdvancedListTemplateHolder;
import kore.botssdk.viewholders.AttachmentTemplateHolder;
import kore.botssdk.viewholders.BarChartTemplateHolder;
import kore.botssdk.viewholders.BaseViewHolderNew;
import kore.botssdk.viewholders.ButtonLinkTemplateHolder;
import kore.botssdk.viewholders.ButtonTemplateHolder;
import kore.botssdk.viewholders.CardTemplateHolder;
import kore.botssdk.viewholders.CarouselStackedTemplateHolder;
import kore.botssdk.viewholders.CarouselTemplateHolder;
import kore.botssdk.viewholders.ClockTemplateHolder;
import kore.botssdk.viewholders.DropDownTemplateHolder;
import kore.botssdk.viewholders.FormTemplateHolder;
import kore.botssdk.viewholders.LineChartTemplateHolder;
import kore.botssdk.viewholders.ListTemplateHolder;
import kore.botssdk.viewholders.ListViewTemplateHolder;
import kore.botssdk.viewholders.ListWidgetTemplateHolder;
import kore.botssdk.viewholders.MiniTableTemplateHolder;
import kore.botssdk.viewholders.PieChartTemplateHolder;
import kore.botssdk.viewholders.RequestTextTemplateHolderNew;
import kore.botssdk.viewholders.ResponseTextTemplateHolderNew;
import kore.botssdk.viewholders.TableListTemplateHolder;
import kore.botssdk.viewholders.TableResponsiveTemplateHolder;

public class ChatAdapterNew extends RecyclerView.Adapter<BaseViewHolderNew> {

    //    private static String LOG_TAG = ChatAdapter.class.getSimpleName();
//    final Context context;
//    private Activity activityContext;
    private final LayoutInflater ownLayoutInflater;
    private final HashMap<String, Integer> headersMap = new HashMap<>();
    private boolean isAlpha = false;


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

    public ArrayList<BaseBotMessage> getBaseBotMessageArrayList() {
        return baseBotMessageArrayList;
    }

    private final ArrayList<BaseBotMessage> baseBotMessageArrayList;

    public static final int TEMPLATE_BUBBLE_RESPONSE = 0;
    public static final int TEMPLATE_BUBBLE_REQUEST = 1;
    public static final int TEMPLATE_BUTTON = 2;
    public static final int TEMPLATE_BUTTON_LINK = 3;
    public static final int TEMPLATE_LIST_VIEW = 4;
    public static final int TEMPLATE_CAROUSEL = 5;
    public static final int TEMPLATE_CAROUSEL_STACKED = 6;
    public static final int TEMPLATE_LIST = 7;
    public static final int TEMPLATE_PIE_CHART = 8;
    public static final int TEMPLATE_ADVANCED_LIST_TEMPLATE = 9;
    public static final int TEMPLATE_LINE_CHART = 10;
    public static final int TEMPLATE_FORM = 11;
    public static final int TEMPLATE_TABLE_LIST = 12;
    public static final int TEMPLATE_LIST_WIDGET_2 = 13;
    public static final int TEMPLATE_CARD = 14;
    public static final int TEMPLATE_BAR_CHART = 15;
    public static final int TEMPLATE_MINI_TABLE = 16;
    public static final int TEMPLATE_TABLE_RESPONSIVE = 17;
    public static final int TEMPLATE_TABLE = 18;
    public static final int TEMPLATE_CLOCK = 19;
    public static final int TEMPLATE_DROP_DOWN = 20;
    public static final int TEMPLATE_IMAGE = 21;

    public ChatAdapterNew(Context context) {
        super();
//        this.context = context;
        ownLayoutInflater = LayoutInflater.from(context);
        baseBotMessageArrayList = new ArrayList<>();
    }

    public BaseBotMessage getItem(int position) {
        if (baseBotMessageArrayList != null && position <= baseBotMessageArrayList.size() - 1 && position != -1) {
            return baseBotMessageArrayList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        BaseBotMessage baseBotMessage = getItem(position);
        if (baseBotMessage instanceof BotRequest) {
            return TEMPLATE_BUBBLE_REQUEST;
        } else {
            ComponentModel componentModel = getComponentModel(baseBotMessage);
            PayloadOuter payOuter = componentModel.getPayload();
            PayloadInner payInner;
            payInner = payOuter.getPayload();
            if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(payOuter.getType()) && payInner != null) {
                switch (payInner.getTemplate_type()) {
                    case BotResponse.TEMPLATE_TYPE_BUTTON:
                        return TEMPLATE_BUTTON;
                    case BotResponse.TEMPLATE_TYPE_QUICK_REPLIES:
                        return TEMPLATE_BUBBLE_RESPONSE;
                    case BotResponse.TEMPLATE_TYPE_CAROUSEL:
                        if (payInner.getCarousel_type() != null && payInner.getCarousel_type().equals(BotResponse.STACKED)) {
                            return TEMPLATE_CAROUSEL_STACKED;
                        }
                        return TEMPLATE_CAROUSEL;
                    case BotResponse.TEMPLATE_TYPE_LIST:
                        return TEMPLATE_LIST;
                    case BotResponse.TEMPLATE_TYPE_PIECHART:
                        return TEMPLATE_PIE_CHART;
                    case BotResponse.TEMPLATE_TYPE_TABLE:
                        if (payInner.getTableDesign().equals(BotResponse.TABLE_VIEW_RESPONSIVE)) {
                            return TEMPLATE_TABLE_RESPONSIVE;
                        }
                        break;
                    case BotResponse.CUSTOM_TABLE_TEMPLATE:
                        break;
                    case BotResponse.TEMPLATE_TYPE_CLOCK:
                        return TEMPLATE_CLOCK;
                    case BotResponse.TEMPLATE_TYPE_MINITABLE:
                        return TEMPLATE_MINI_TABLE;
                    case BotResponse.TEMPLATE_TYPE_MULTI_SELECT:
                        break;
                    case BotResponse.ADVANCED_LIST_TEMPLATE:
                        return TEMPLATE_ADVANCED_LIST_TEMPLATE;
                    case BotResponse.TEMPLATE_TYPE_LINECHART:
                        return TEMPLATE_LINE_CHART;
                    case BotResponse.TEMPLATE_TYPE_BARCHART:
                        return TEMPLATE_BAR_CHART;
                    case BotResponse.TEMPLATE_TYPE_FORM:
                        return TEMPLATE_FORM;
                    case BotResponse.TEMPLATE_TYPE_LIST_VIEW:
                        return TEMPLATE_LIST_VIEW;
                    case BotResponse.TEMPLATE_TYPE_TABLE_LIST:
                        return TEMPLATE_TABLE_LIST;
                    case BotResponse.TEMPLATE_TYPE_FEEDBACK:
                        break;
                    case BotResponse.TEMPLATE_TYPE_LIST_WIDGET:
                        break;
                    case BotResponse.TEMPLATE_TYPE_LIST_WIDGET_2:
                        return TEMPLATE_LIST_WIDGET_2;
                    case BotResponse.TEMPLATE_DROPDOWN:
                        return TEMPLATE_DROP_DOWN;
                    case BotResponse.CARD_TEMPLATE:
                        return TEMPLATE_CARD;
                    case BotResponse.COMPONENT_TYPE_IMAGE:
                        return TEMPLATE_IMAGE;
                    default:
                        if (!StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
                            if (!BotResponse.TEMPLATE_TYPE_DATE.equalsIgnoreCase(payInner.getTemplate_type())) {
//                                message = payInner.getText();
                            } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getText_message())) {
//                                message = payInner.getText_message();
                            }
                        } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.getTemplate_type())) {
//                            message = payInner.getTemplate_type();
                        } else if (StringUtils.isNullOrEmptyWithTrim(payOuter.getText())) {
//                            timeStampsTextView.setText("");
//                            message = "default";
                        }
                        break;
                }
            } else if (BotResponse.COMPONENT_TYPE_MESSAGE.equalsIgnoreCase(payOuter.getType()) && payInner != null) {

                if (!StringUtils.isNullOrEmpty(payInner.getVideoUrl())) {
                    payOuter.setType(BundleConstants.MEDIA_TYPE_VIDEO);
                    return TEMPLATE_IMAGE;
                } else if (!StringUtils.isNullOrEmpty(payInner.getAudioUrl())) {
                    payOuter.setType(BundleConstants.MEDIA_TYPE_AUDIO);
                    return TEMPLATE_IMAGE;
                }
            } else if (!StringUtils.isNullOrEmpty(payOuter.getType())) {
                switch (payOuter.getType()) {
                    case BotResponse.COMPONENT_TYPE_IMAGE:
                    case BotResponse.COMPONENT_TYPE_AUDIO:
                    case BotResponse.COMPONENT_TYPE_VIDEO:
                        return TEMPLATE_IMAGE;
                    default:
                        break;
                }
            }
            return TEMPLATE_BUBBLE_RESPONSE;
        }
    }

    public int getItemType(int position) {
        BaseBotMessage baseBotMessage = getItem(position);

        if (baseBotMessage instanceof BotRequest) {
            return TEMPLATE_BUBBLE_REQUEST;
        } else {
            return TEMPLATE_BUBBLE_RESPONSE;
        }
    }

    @NonNull
    @Override
    public BaseViewHolderNew onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        switch (i) {
            case TEMPLATE_BUBBLE_REQUEST:
                return new RequestTextTemplateHolderNew(ownLayoutInflater.inflate(R.layout.template_bubble_text, parent, false));
            case TEMPLATE_BUTTON:
                return new ButtonTemplateHolder(ownLayoutInflater.inflate(R.layout.template_button, parent, false));
            case TEMPLATE_BUTTON_LINK:
                return new ButtonLinkTemplateHolder(ownLayoutInflater.inflate(R.layout.template_button_link, parent, false));
            case TEMPLATE_LIST_VIEW:
                return new ListViewTemplateHolder(ownLayoutInflater.inflate(R.layout.template_list_view, parent, false));
            case TEMPLATE_LIST:
                return new ListTemplateHolder(ownLayoutInflater.inflate(R.layout.template_list, parent, false));
            case TEMPLATE_CAROUSEL:
                return new CarouselTemplateHolder(ownLayoutInflater.inflate(R.layout.template_carousel, parent, false));
            case TEMPLATE_CAROUSEL_STACKED:
                return new CarouselStackedTemplateHolder(ownLayoutInflater.inflate(R.layout.template_carousel_stacked, parent, false));
            case TEMPLATE_PIE_CHART:
                return new PieChartTemplateHolder(ownLayoutInflater.inflate(R.layout.template_pie_chart, parent, false));
            case TEMPLATE_ADVANCED_LIST_TEMPLATE:
                return new AdvancedListTemplateHolder(ownLayoutInflater.inflate(R.layout.template_advanced_list_template, parent, false));
            case TEMPLATE_LINE_CHART:
                return new LineChartTemplateHolder(ownLayoutInflater.inflate(R.layout.template_line_chart, parent, false));
            case TEMPLATE_FORM:
                return new FormTemplateHolder(ownLayoutInflater.inflate(R.layout.template_form, parent, false));
            case TEMPLATE_TABLE_LIST:
                return new TableListTemplateHolder(ownLayoutInflater.inflate(R.layout.template_table_list, parent, false));
            case TEMPLATE_LIST_WIDGET_2:
                return new ListWidgetTemplateHolder(ownLayoutInflater.inflate(R.layout.template_list_widget, parent, false));
            case TEMPLATE_CARD:
                return new CardTemplateHolder(ownLayoutInflater.inflate(R.layout.template_card, parent, false));
            case TEMPLATE_BAR_CHART:
                return new BarChartTemplateHolder(ownLayoutInflater.inflate(R.layout.template_bar_chart, parent, false));
            case TEMPLATE_MINI_TABLE:
                return new MiniTableTemplateHolder(ownLayoutInflater.inflate(R.layout.template_mini_table, parent, false));
            case TEMPLATE_TABLE_RESPONSIVE:
                return new TableResponsiveTemplateHolder(ownLayoutInflater.inflate(R.layout.template_table_responsive, parent, false));
            case TEMPLATE_CLOCK:
                return new ClockTemplateHolder(ownLayoutInflater.inflate(R.layout.template_clock, parent, false));
            case TEMPLATE_DROP_DOWN:
                return new DropDownTemplateHolder(ownLayoutInflater.inflate(R.layout.template_dropdown, parent, false));
            case TEMPLATE_IMAGE:
                return new AttachmentTemplateHolder(ownLayoutInflater.inflate(R.layout.image_template, parent, false));
            default:
                return new ResponseTextTemplateHolderNew(ownLayoutInflater.inflate(R.layout.template_bubble_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolderNew holder, final int position) {
        boolean isLastItem = position == baseBotMessageArrayList.size() - 1;
        holder.setIsLastItem(isLastItem);
        holder.setComposeFooterInterface(composeFooterInterface);
        holder.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return baseBotMessageArrayList.size();
    }

    public boolean isAlpha() {
        return isAlpha;
    }

    public void setAlpha(boolean alpha) {
        isAlpha = alpha;
    }

    public void addBaseBotMessage(BaseBotMessage baseBotMessage) {
        if (!baseBotMessageArrayList.contains(baseBotMessage)) {
            baseBotMessageArrayList.add(baseBotMessage);
        }

        if (headersMap.get(baseBotMessage.getFormattedDate()) == null) {
            headersMap.put(baseBotMessage.getFormattedDate(), baseBotMessageArrayList.size() - 1);
        }
        SelectionUtils.resetSelectionTasks();
        SelectionUtils.resetSelectionSlots();
        isAlpha = false;
//        submitList(baseBotMessageArrayList);
        if (baseBotMessageArrayList.size() == 1) {
            notifyItemInserted(0);
        } else {
            notifyItemRangeChanged(baseBotMessageArrayList.size() - 2, baseBotMessageArrayList.size() - 1);
        }
    }

    public void addBaseBotMessages(ArrayList<BaseBotMessage> list) {
        baseBotMessageArrayList.addAll(0, list);
//        prepareHeaderMap();
//        if (selectedItem != -1) {
//            selectedItem = selectedItem + list.size() - 1;
//        }
        notifyItemRangeInserted(0, list.size() - 1);
//        submitList(baseBotMessageArrayList);
    }

    public void addMissedBaseBotMessages(ArrayList<BaseBotMessage> list) {
        baseBotMessageArrayList.addAll(list);
//        submitList(baseBotMessageArrayList);
        notifyItemRangeInserted((baseBotMessageArrayList.size() - 1) - (list.size() - 1), list.size() - 1);
    }

    private ComponentModel getComponentModel(BaseBotMessage baseBotMessage) {
        ComponentModel compModel = null;
        if (baseBotMessage instanceof BotResponse && ((BotResponse) baseBotMessage).getMessage() != null && !((BotResponse) baseBotMessage).getMessage().isEmpty()) {
            compModel = ((BotResponse) baseBotMessage).getMessage().get(0).getComponent();
        }
        return compModel;
    }
}
