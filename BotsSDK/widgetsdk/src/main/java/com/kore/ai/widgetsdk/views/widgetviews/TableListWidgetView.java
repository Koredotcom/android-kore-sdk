//package com.kore.ai.widgetsdk.views.widgetviews;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.kora.ai.widgetsdk.R;
//import com.kore.ai.widgetsdk.adapters.BotTableListTemlateAdapter;
//import com.kore.ai.widgetsdk.adapters.ListWidgetAdapter;
//import com.kore.ai.widgetsdk.listeners.ComposeFooterInterface;
//import com.kore.ai.widgetsdk.listeners.InvokeGenericWebViewInterface;
//import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
//import com.kore.ai.widgetsdk.managers.UserDataManager;
//import com.kore.ai.widgetsdk.models.BotResponse;
//import com.kore.ai.widgetsdk.models.BotTableListModel;
//import com.kore.ai.widgetsdk.models.WidgetListDataModel;
//import com.kore.ai.widgetsdk.models.WidgetsModel;
//import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
//import com.kore.ai.widgetsdk.room.models.AuthData;
//import com.kore.ai.widgetsdk.room.models.UserData;
//import com.kore.ai.widgetsdk.utils.KaUtility;
//import com.kore.ai.widgetsdk.utils.NetworkUtility;
//import com.kore.ai.widgetsdk.utils.Utility;
//import com.kore.ai.widgetsdk.utils.Utils;
//import com.kore.ai.widgetsdk.utils.WidgetDataLoader;
//import com.kore.ai.widgetsdk.view.AutoExpandListView;
//
//import java.util.ArrayList;
//import java.util.Map;
//
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//
//import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;
//
//public class TableListWidgetView extends LinearLayout implements VerticalListViewActionHelper
//{
//    String LOG_TAG = BotTableListTemplateView.class.getSimpleName();
//
//    float dp1, layoutItemHeight = 0;
//    AutoExpandListView autoExpandListView;
//    TextView botCustomListViewButton;
//    TextView workBenchListViewButton;
//    LinearLayout botCustomListRoot;
//    float restrictedMaxWidth, restrictedMaxHeight;
//    ComposeFooterInterface composeFooterInterface;
//    VerticalListViewActionHelper verticalListViewActionHelper;
//    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
//    private WidgetsModel mWidget;
//    private PanelLevelData panelData;
//    private String trigger;
//    private String jwtToken;
//    public TextView  widget_header;
//    private TextView pin_view,panel_name_view;
//    private String name;
//    private AuthData authData;
//    private UserData userData;
//    public ProgressBar progress;
//    private View rootView;
//    public ImageView menu_btn,icon_image_load;
//    public View view_more;
//    BotTableListTemlateAdapter botListTemplateAdapter;
//    public ImageView imgMenu;
//    public TextView tvText;
//    public TextView tvUrl;
//    public TextView tvButton;
//    public LinearLayout tvButtonParent;
//    Context context;
//
//    public void setWidget(WidgetsModel mWidget, PanelLevelData panelData, String trigger, String jwtToken) {
//        this.mWidget = mWidget;
//        this.panelData=panelData;
//        this.trigger = trigger;
//        this.jwtToken = jwtToken;
//        widget_header.setText(mWidget.getTitle());
////        pin_view.setText(mWidget.isPinned() ? context.getResources().getString(R.string.icon_31) :context.getResources().getString(R.string.icon_32));
//        panel_name_view.setText(KaUtility.getPanelFormatedName(mWidget.getName()));
//        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(mWidget.getName(),name));
//
//        loadData(false);
//    }
//
//    private void getUserData() {
//        authData = UserDataManager.getAuthData();
//        userData = UserDataManager.getUserData();
//    }
//
//    private void init() {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.tablelist_widget_layout, this, true);
//        rootView = view.findViewById(R.id.meeting_root_view);
//        pin_view = view.findViewById(R.id.pin_view);
//        panel_name_view=view.findViewById(R.id.panel_name_view);
//        icon_image_load=view.findViewById(R.id.icon_image_load);
//        view_more = view.findViewById(R.id.view_more);
//        menu_btn = view.findViewById(R.id.menu_meeting_btn);
//        menu_btn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                showPopUpMenu();
//            }
//        });
//        // KoreEventCenter.register(this);
//        autoExpandListView = view.findViewById(R.id.botCustomListView);
//        widget_header = view.findViewById(R.id.meeting_header);
//        progress = view.findViewById(R.id.meeting_progress);
//        dp1 = (int) Utility.convertDpToPixel(context, 1);
//
//        botListTemplateAdapter = new BotTableListTemlateAdapter(getContext(), 4);
////        botListTemplateAdapter.setSkillName(name);
////        botListTemplateAdapter.setFromWidget(true);
////        botListTemplateAdapter.setViewMoreEnum(widgetViewMoreEnum);
////        botListTemplateAdapter.setVerticalListViewActionHelper(this);
//
//        imgMenu = view.findViewById(R.id.icon_image);
//        tvButton = view.findViewById(R.id.tv_button);
//        tvText = view.findViewById(R.id.tv_text);
//        tvUrl = view.findViewById(R.id.tv_url);
//        tvButtonParent = view.findViewById(R.id.tv_values_layout);
//        getUserData();
//    }
//
//    private void loadData(boolean shouldRefresh) {
//
//        /*if(PanelDataLRUCache.getInstance().getEntry(name) !=null && !shouldRefresh){
//            afterDataLoad((Widget) PanelDataLRUCache.getInstance().getEntry(name));
//            return;
//        }*/
//        if(mWidget.getCallbackURL()==null) {
//            return;
//        }
//        progress.setVisibility(View.VISIBLE);
//
//        Map<String,Object> result = getMapObject(mWidget.getParam());
//        WidgetDataLoader.loadListWidgetServiceData(mWidget.getCallbackURL(), Utils.ah(jwtToken),result)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<WidgetListDataModel>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(Observer<WidgetListDataModel> model) {
////                        PanelDataLRUCache.getInstance().putEntry(name,model);
//                        afterDataLoad(model);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        progress.setVisibility(View.GONE);
//                        if (e.getMessage().equalsIgnoreCase("410") || e.getMessage().equalsIgnoreCase("401"))
//                        {
//                            //KoreEventCenter.post(new OnTokenExpired());
//                        }
//
//                        String msg;
//                        Drawable drawable=null;
//                        if (!NetworkUtility.isNetworkConnectionAvailable(TableListWidgetView.this.getContext())) {
//                            //No Internet Connect
//                            msg=getResources().getString(R.string.no_internet_connection);
//                            drawable=getResources().getDrawable(R.drawable.no_internet);
//                        } else {
//                            //Oops some thing went wrong
//                            msg=getResources().getString(R.string.oops);
//                            drawable=getResources().getDrawable(R.drawable.oops_icon);
//                        }
//                        botListTemplateAdapter.setWidgetData(null);
//                        botListTemplateAdapter.setMessage(msg,drawable);
//                        view_more.setVisibility(GONE);
//                        autoExpandListView.setAdapter(botListTemplateAdapter);
//                        botListTemplateAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        progress.setVisibility(View.GONE);
//                    }
//                });
//    }
//
//    public void populateListTemplateView(ArrayList<BotTableListModel> botListModelArrayList)
//    {
//        if (botListModelArrayList != null && botListModelArrayList.size() > 0) {
//            BotTableListTemlateAdapter botListTemplateAdapter;
//            if (autoExpandListView.getAdapter() == null) {
//                botListTemplateAdapter = new BotTableListTemlateAdapter(getContext(), autoExpandListView, 4);
//                autoExpandListView.setAdapter(botListTemplateAdapter);
//                botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
//                botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
//            } else {
//                botListTemplateAdapter = (BotTableListTemlateAdapter) autoExpandListView.getAdapter();
//            }
//            botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
//            botListTemplateAdapter.notifyDataSetChanged();
//            botCustomListRoot.setVisibility(VISIBLE);
//        }
//        else
//        {
//            botCustomListRoot.setVisibility(GONE);
//            botCustomListViewButton.setVisibility(GONE);
//        }
//    }
//}
