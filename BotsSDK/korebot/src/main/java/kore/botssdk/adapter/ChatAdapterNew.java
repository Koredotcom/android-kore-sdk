package kore.botssdk.adapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewholders.AdvancedListTemplateHolder;
import kore.botssdk.viewholders.AgentTransferTemplateHolder;
import kore.botssdk.viewholders.BankingFeedbackTemplateHolder;
import kore.botssdk.viewholders.BarChartTemplateHolder;
import kore.botssdk.viewholders.BaseViewHolder;
import kore.botssdk.viewholders.BeneficiaryTemplateHolder;
import kore.botssdk.viewholders.ButtonLinkTemplateHolder;
import kore.botssdk.viewholders.ButtonTemplateHolder;
import kore.botssdk.viewholders.CardTemplateHolder;
import kore.botssdk.viewholders.CarouselStackedTemplateHolder;
import kore.botssdk.viewholders.CarouselTemplateHolder;
import kore.botssdk.viewholders.ClockTemplateHolder;
import kore.botssdk.viewholders.ContactCardTemplateHolder;
import kore.botssdk.viewholders.DropDownTemplateHolder;
import kore.botssdk.viewholders.FeedbackTemplateHolder;
import kore.botssdk.viewholders.FormTemplateHolder;
import kore.botssdk.viewholders.LineChartTemplateHolder;
import kore.botssdk.viewholders.ListTemplateHolder;
import kore.botssdk.viewholders.ListViewTemplateHolder;
import kore.botssdk.viewholders.ListWidgetTemplateHolder;
import kore.botssdk.viewholders.MediaTemplateHolder;
import kore.botssdk.viewholders.MiniTableTemplateHolder;
import kore.botssdk.viewholders.MultiSelectTemplateHolder;
import kore.botssdk.viewholders.PdfTemplateHolder;
import kore.botssdk.viewholders.PieChartTemplateHolder;
import kore.botssdk.viewholders.RadioOptionsTemplateHolder;
import kore.botssdk.viewholders.RequestTextTemplateHolder;
import kore.botssdk.viewholders.ResponseTextTemplateHolder;
import kore.botssdk.viewholders.TableListTemplateHolder;
import kore.botssdk.viewholders.TableResponsiveTemplateHolder;
import kore.botssdk.viewholders.TableTemplateHolder;
import kore.botssdk.viewholders.WelcomeQuickRepliesTemplateHolder;

public class ChatAdapterNew extends RecyclerView.Adapter<BaseViewHolder> implements ChatContentStateListener {

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

    public static final int TEMPLATE_CUSTOM_TEMPLATES = 100;
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
    public static final int TEMPLATE_MEDIA = 21;
    public static final int TEMPLATE_FEEDBACK = 22;
    public static final int TEMPLATE_RADIO_OPTIONS = 23;
    public static final int TEMPLATE_WELCOME_QUICK_REPLIES = 24;
    public static final int TEMPLATE_NOTIFICATIONS = 25;
    public static final int TEMPLATE_CONTACT_CARD = 26;
    public static final int TEMPLATE_BANKING_FEEDBACK = 27;
    public static final int TEMPLATE_PDF_DOWNLOAD = 28;
    public static final int TEMPLATE_BENEFICIARY = 29;
    public static final int TEMPLATE_MULTI_SELECT = 30;

    private final HashMap<Integer, String> customTemplates = new HashMap<>();

    public ChatAdapterNew() {
        super();
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
                int customTemplateType = getCustomTemplateType(payInner.getTemplate_type());
                if (customTemplateType != -1) return customTemplateType;

                switch (payInner.getTemplate_type()) {
                    case BotResponse.TEMPLATE_TYPE_BUTTON:
                        return TEMPLATE_BUTTON;
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
                        } else {
                            return TEMPLATE_TABLE;
                        }
                    case BotResponse.CUSTOM_TABLE_TEMPLATE:
                        break;
                    case BotResponse.TEMPLATE_TYPE_CLOCK:
                        return TEMPLATE_CLOCK;
                    case BotResponse.TEMPLATE_TYPE_MINITABLE:
                        return TEMPLATE_MINI_TABLE;
                    case BotResponse.TEMPLATE_TYPE_MULTI_SELECT:
                        return TEMPLATE_MULTI_SELECT;
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
                        return TEMPLATE_FEEDBACK;
                    case BotResponse.TEMPLATE_TYPE_LIST_WIDGET_2:
                        return TEMPLATE_LIST_WIDGET_2;
                    case BotResponse.TEMPLATE_DROPDOWN:
                        return TEMPLATE_DROP_DOWN;
                    case BotResponse.CARD_TEMPLATE:
                        return TEMPLATE_CARD;
                    case BotResponse.COMPONENT_TYPE_IMAGE:
                        return TEMPLATE_MEDIA;
                    case BotResponse.TEMPLATE_TYPE_RADIO_OPTIONS:
                        return TEMPLATE_RADIO_OPTIONS;
                    case BotResponse.TEMPLATE_TYPE_WELCOME_QUICK_REPLIES:
                        return TEMPLATE_WELCOME_QUICK_REPLIES;
                    case BotResponse.TEMPLATE_TYPE_NOTIFICATIONS:
                        return TEMPLATE_NOTIFICATIONS;
                    case BotResponse.CONTACT_CARD_TEMPLATE:
                        return TEMPLATE_CONTACT_CARD;
                    case BotResponse.TEMPLATE_BANKING_FEEDBACK:
                        return TEMPLATE_BANKING_FEEDBACK;
                    case BotResponse.TEMPLATE_PDF_DOWNLOAD:
                        return TEMPLATE_PDF_DOWNLOAD;
                    case BotResponse.TEMPLATE_BENEFICIARY:
                        return TEMPLATE_BENEFICIARY;
                    default:
                        return TEMPLATE_BUBBLE_RESPONSE;
                }
            } else if (BotResponse.COMPONENT_TYPE_MESSAGE.equalsIgnoreCase(payOuter.getType()) && payInner != null) {
                if (!StringUtils.isNullOrEmpty(payInner.getVideoUrl())) {
                    payOuter.setType(BundleConstants.MEDIA_TYPE_VIDEO);
                    return TEMPLATE_MEDIA;
                } else if (!StringUtils.isNullOrEmpty(payInner.getAudioUrl())) {
                    payOuter.setType(BundleConstants.MEDIA_TYPE_AUDIO);
                    return TEMPLATE_MEDIA;
                }
            } else if (!StringUtils.isNullOrEmpty(payOuter.getType())) {
                switch (payOuter.getType()) {
                    case BotResponse.COMPONENT_TYPE_IMAGE:
                    case BotResponse.COMPONENT_TYPE_AUDIO:
                    case BotResponse.COMPONENT_TYPE_VIDEO:
                        return TEMPLATE_MEDIA;
                    default:
                        break;
                }
                int customTemplateType = getCustomTemplateType(payOuter.getType());
                if (customTemplateType != -1) return customTemplateType;
            }
            return TEMPLATE_BUBBLE_RESPONSE;
        }
    }

    private int getCustomTemplateType(String templateType) {
        Class<?> clazz = SDKConfiguration.getCustomTemplateViewHolder(templateType);
        if (clazz != null) {
            if (clazz.getSuperclass() != null && clazz.getSuperclass().isAssignableFrom(BaseViewHolder.class)) {
                List<String> templateTypes = new ArrayList<>(customTemplates.values());
                if (!templateTypes.contains(templateType)) {
                    int type = TEMPLATE_CUSTOM_TEMPLATES + customTemplates.size();
                    customTemplates.put(type, templateType);
                    return type;
                } else {
                    for (Integer key : customTemplates.keySet()) {
                        if (customTemplates.get(key) != null && customTemplates.get(key).equals(templateType)) {
                            return key;
                        }
                    }
                }
            }
            Log.e("Error", "Custom template view holder should inherit from " + BaseViewHolder.class.getSimpleName());
        }
        return -1;
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
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (customTemplates.containsKey(viewType)) {
            return getCustomTemplate(parent, SDKConfiguration.getCustomTemplateViewHolder(customTemplates.get(viewType)));
        }
        switch (viewType) {
            case TEMPLATE_BUBBLE_REQUEST:
                return RequestTextTemplateHolder.getInstance(parent);
            case TEMPLATE_BUTTON:
                return ButtonTemplateHolder.getInstance(parent);
            case TEMPLATE_BUTTON_LINK:
                return ButtonLinkTemplateHolder.getInstance(parent);
            case TEMPLATE_LIST_VIEW:
                return ListViewTemplateHolder.getInstance(parent);
            case TEMPLATE_LIST:
                return ListTemplateHolder.getInstance(parent);
            case TEMPLATE_CAROUSEL:
                return CarouselTemplateHolder.getInstance(parent);
            case TEMPLATE_CAROUSEL_STACKED:
                return CarouselStackedTemplateHolder.getInstance(parent);
            case TEMPLATE_PIE_CHART:
                return PieChartTemplateHolder.getInstance(parent);
            case TEMPLATE_ADVANCED_LIST_TEMPLATE:
                return AdvancedListTemplateHolder.getInstance(parent);
            case TEMPLATE_LINE_CHART:
                return LineChartTemplateHolder.getInstance(parent);
            case TEMPLATE_FORM:
                return FormTemplateHolder.getInstance(parent);
            case TEMPLATE_TABLE_LIST:
                return TableListTemplateHolder.getInstance(parent);
            case TEMPLATE_LIST_WIDGET_2:
                return ListWidgetTemplateHolder.getInstance(parent);
            case TEMPLATE_CARD:
                return CardTemplateHolder.getInstance(parent);
            case TEMPLATE_BAR_CHART:
                return BarChartTemplateHolder.getInstance(parent);
            case TEMPLATE_MINI_TABLE:
                return MiniTableTemplateHolder.getInstance(parent);
            case TEMPLATE_TABLE_RESPONSIVE:
                return TableResponsiveTemplateHolder.getInstance(parent);
            case TEMPLATE_TABLE:
                return TableTemplateHolder.getInstance(parent);
            case TEMPLATE_CLOCK:
                return ClockTemplateHolder.getInstance(parent);
            case TEMPLATE_DROP_DOWN:
                return DropDownTemplateHolder.getInstance(parent);
            case TEMPLATE_MEDIA:
                return MediaTemplateHolder.getInstance(parent);
            case TEMPLATE_FEEDBACK:
                return FeedbackTemplateHolder.getInstance(parent);
            case TEMPLATE_RADIO_OPTIONS:
                return RadioOptionsTemplateHolder.getInstance(parent);
            case TEMPLATE_WELCOME_QUICK_REPLIES:
                return WelcomeQuickRepliesTemplateHolder.getInstance(parent);
            case TEMPLATE_NOTIFICATIONS:
                return AgentTransferTemplateHolder.getInstance(parent);
            case TEMPLATE_CONTACT_CARD:
                return ContactCardTemplateHolder.getInstance(parent);
            case TEMPLATE_BANKING_FEEDBACK:
                return BankingFeedbackTemplateHolder.getInstance(parent);
            case TEMPLATE_PDF_DOWNLOAD:
                return PdfTemplateHolder.getInstance(parent);
            case TEMPLATE_BENEFICIARY:
                return BeneficiaryTemplateHolder.getInstance(parent);
            case TEMPLATE_MULTI_SELECT:
                return MultiSelectTemplateHolder.getInstance(parent);
            default:
                return ResponseTextTemplateHolder.getInstance(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, final int position) {
        boolean isLastItem = position == baseBotMessageArrayList.size() - 1;
        BaseBotMessage baseBotMessage = baseBotMessageArrayList.get(position);
        holder.setIsLastItem(isLastItem);
        holder.setMsgTime(baseBotMessage.getTimeStamp(), baseBotMessage instanceof BotRequest);
        if (baseBotMessage instanceof BotResponse) {
            holder.setBotIcon(((BotResponse) baseBotMessage).getIcon());
        }
        Integer headerPosition = headersMap.get(baseBotMessage.getFormattedDate());
        holder.setTimeStamp(headerPosition != null && headerPosition == position ? baseBotMessage.getFormattedDate() : null);
        holder.setContentStateListener(this);
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

    private BaseViewHolder getCustomTemplate(ViewGroup parent, Class<?> clazzType) {
        Class<?> clazz = null;
        Method method = null;
        BaseViewHolder holder = null;
        try {
            clazz = Class.forName(clazzType.getName());
            // Get the method by name and parameter types
            method = clazz.getDeclaredMethod("getInstance", ViewGroup.class);
            // Invoke the static method with the parameter and get the result
            holder = (BaseViewHolder) method.invoke(null, parent);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return holder;
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
        if (baseBotMessageArrayList.size() == 1) {
            notifyItemInserted(0);
        } else {
            notifyItemRangeChanged(baseBotMessageArrayList.size() - 2, baseBotMessageArrayList.size() - 1);
        }
    }

    public void addBaseBotMessages(ArrayList<BaseBotMessage> list) {
        baseBotMessageArrayList.addAll(0, list);
        prepareHeaderMap();
        notifyDataSetChanged();
    }

    public void addMissedBaseBotMessages(ArrayList<BaseBotMessage> list) {
        baseBotMessageArrayList.addAll(list);
        prepareHeaderMap();
        notifyItemRangeInserted((baseBotMessageArrayList.size() - 1) - (list.size() - 1), list.size() - 1);
    }

    private void prepareHeaderMap() {
        int i;
        headersMap.clear();
        for (i = 0; i < baseBotMessageArrayList.size(); i++) {
            BaseBotMessage baseBotMessage = baseBotMessageArrayList.get(i);
            if (headersMap.get(baseBotMessage.getFormattedDate()) == null) {
                headersMap.put(baseBotMessage.getFormattedDate(), i);
            }
        }
    }

    private ComponentModel getComponentModel(BaseBotMessage baseBotMessage) {
        ComponentModel compModel = null;
        if (baseBotMessage instanceof BotResponse && ((BotResponse) baseBotMessage).getMessage() != null && !((BotResponse) baseBotMessage).getMessage().isEmpty()) {
            compModel = ((BotResponse) baseBotMessage).getMessage().get(0).getComponent();
        }
        return compModel;
    }

    @Override
    public void onSelect(String id, Object value, String key) {
        BotResponse response = null;
        int index = 0;
        int foundIndex = -1;
        for (BaseBotMessage item : baseBotMessageArrayList) {
            if (item instanceof BotResponse) {
                response = (BotResponse) item;
                if (response.getMessageId().equals(id)) {
                    foundIndex = index;
                    Map<String, Object> map = new HashMap<>();
                    map.put(key, value);
                    response.setContentState(map);
                }
            }
            index++;
        }
        if (foundIndex != -1) {
            baseBotMessageArrayList.remove(foundIndex);
            baseBotMessageArrayList.add(foundIndex, response);
            notifyItemChanged(foundIndex);
        }
    }
}
