package com.kore.ai.widgetsdk.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.adapters.KaWidgetBaseAdapterNew;
import com.kore.ai.widgetsdk.adapters.PannelAdapter;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.events.EntityEditEvent;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.interfaces.PanelInterface;
import com.kore.ai.widgetsdk.listeners.ComposeFooterInterface;
import com.kore.ai.widgetsdk.listeners.UpdateRefreshItem;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.listeners.WidgetComposeFooterInterface;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.JWTTokenResponse;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.PanelBaseModel;
import com.kore.ai.widgetsdk.models.PanelResponseData;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.BotJWTRestBuilder;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.net.SDKConfiguration;
import com.kore.ai.widgetsdk.utils.AppUtils;
import com.kore.ai.widgetsdk.utils.KaFontUtils;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.SharedPreferenceUtils;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetConstants;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.views.widgetviews.ArticlesWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.BarChartWidgetView;
//import com.kore.ai.widgetsdk.views.widgetviews.BotTableListTemplateView;
import com.kore.ai.widgetsdk.views.widgetviews.ChartListWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.CustomBottomSheetBehavior;
import com.kore.ai.widgetsdk.views.widgetviews.DefaultWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.GenericWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.LineChartWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.ListWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.MeetingWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.PieChartWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.SkillWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.TableListWidgetView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.client_id;
import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.client_secret;
import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.identity;
import static com.kore.ai.widgetsdk.utils.DimensionUtil.dp1;

public class BottomPanelFragment extends KaBaseFragment implements PanelInterface,
        VerticalListViewActionHelper, UpdateRefreshItem, GestureDetector.OnGestureListener {
    private View view;
    //For Bottom Panel
    private ProgressBar progressBarPanel;
    private RecyclerView pannel_recycler;
    private PannelAdapter pannelAdapter;
    private String jwtToken;
    private TextView emptyPanelView;
    private JWTTokenResponse jwtKeyResponse;
    private SharedPreferenceUtils sharedPreferenceUtils;
    private String packageName = "com.kore.koreapp";
    private String appName = "Kore";
    private CustomBottomSheetBehavior mBottomSheetBehavior;
    private boolean keyBoardShowing = false;
    private PanelBaseModel pModels;
    private LinearLayout perssiatentPanel, persistentSubLayout;
    private ImageView img_skill;
    private TextView closeBtnPanel, editButton;
    private RecyclerView recyclerView_panel;
    private LinearLayout single_item_container;
    private KaWidgetBaseAdapterNew widgetBaseAdapter;
    private TextView img_icon, txtTitle;
    private int maxHeight;
    final Handler handler = new Handler();
    private WidgetComposeFooterInterface widgetComposeFooterInterface;
    private Button panelDrag;
    private GestureDetector gestureScanner;
    private CoordinatorLayout cordinate_layout;
    private float screenHeight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.panel_layout, container, false);
        //Restoring state if any
        findPanelView(view);
        KoreEventCenter.register(this);
        KaFontUtils.applyCustomFont(getActivity(), view);
        dp1 = (int) AppControl.getInstance(getContext()).getDimensionUtil().dp1;
        screenHeight = (int) AppControl.getInstance(getContext()).getDimensionUtil().screenHeight;
        maxHeight = (int) ((3 * 64 * dp1) + 28 * dp1);

        closeBtnPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perssiatentPanel.setVisibility(GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        panelDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureScanner.onTouchEvent(event);

            }
        });

        getJWToken();
//        getPanelData("home",false);
        return view;
    }

    private void findPanelView(View view) {
        pannel_recycler = (RecyclerView) view.findViewById(R.id.pannel_recycler);
        progressBarPanel = (ProgressBar) view.findViewById(R.id.progressBarPanel);
        emptyPanelView = (TextView) view.findViewById(R.id.emptyView);

        perssiatentPanel = view.findViewById(R.id.persistentPanel);
        persistentSubLayout = view.findViewById(R.id.panel_sub_layout);
        img_skill = view.findViewById(R.id.img_skill);
        txtTitle = view.findViewById(R.id.txtTitle);
        editButton = (TextView) view.findViewById(R.id.editButton);
        editButton.setTypeface(KaUtility.getTypeFaceObj(getActivity()));

        recyclerView_panel = view.findViewById(R.id.recyclerView_panel);
        closeBtnPanel = (TextView) view.findViewById(R.id.closeBtnPanel);
        closeBtnPanel.setTypeface(KaUtility.getTypeFaceObj(getActivity()));
        single_item_container = view.findViewById(R.id.single_item_container);
        panelDrag = view.findViewById(R.id.panel_drag);

        recyclerView_panel.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBottomSheetBehavior = CustomBottomSheetBehavior.from(perssiatentPanel);
        cordinate_layout = view.findViewById(R.id.cordinate_layout);

        gestureScanner = new GestureDetector(this);

        mBottomSheetBehavior.setBottomSheetCallback(new CustomBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                        }
//                        mBottomSheetBehavior.setLocked(false);
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//
//                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mBottomSheetBehavior.setLocked(true);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        perssiatentPanel.setVisibility(GONE);
                        mBottomSheetBehavior.setLocked(false);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        perssiatentPanel.setVisibility(GONE);
                        mBottomSheetBehavior.setLocked(false);

                        break;
//                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
//                        mBottomSheetBehavior.setLocked(false);
//                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        pannel_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    /*
    @PanelName : Launch default Panel based on name
     */
    public void getPanelData(final String panelName, final boolean isFromPresence) {
        Call<List<PanelResponseData.Panel>> baseWidgetData = KaRestBuilder.getWidgetKaRestAPI().
                getWidgetPannelData(com.kore.ai.widgetsdk.net.SDKConfiguration.Client.bot_id, Utils.ah(jwtToken), com.kore.ai.widgetsdk.net.SDKConfiguration.Client.identity);
        KaRestAPIHelper.enqueueWithRetry(baseWidgetData, new Callback<List<PanelResponseData.Panel>>() {
            @Override
            public void onResponse(Call<List<PanelResponseData.Panel>> call, Response<List<PanelResponseData.Panel>> response) {
                if (response != null && response.isSuccessful()) {
                    progressBarPanel.setVisibility(View.GONE);
                    pannel_recycler.setVisibility(View.VISIBLE);
                    Log.e("Panel Response", response.body() + "");
                    PanelResponseData panelResponseData = new PanelResponseData();
                    panelResponseData.setPanels(response.body());
                    updatePannelData(panelResponseData, panelName, isFromPresence);
                } else {
//                    ((KoraMainActivity) getActivity()).dismissMainLoader();
                    progressBarPanel.setVisibility(View.GONE);
                    if (pannelAdapter == null || pannelAdapter.getItemCount() <= 0) {
                        emptyPanelView.setText(getString(com.kora.ai.widgetsdk.R.string.oops));
                        emptyPanelView.setTypeface(KaFontUtils.getCustomTypeface("regular", getActivity()));
                        emptyPanelView.setVisibility(View.VISIBLE);
                    } else {
                        emptyPanelView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PanelResponseData.Panel>> call, Throwable t) {
                progressBarPanel.setVisibility(View.GONE);
                if (pannelAdapter == null || pannelAdapter.getItemCount() <= 0) {
                    emptyPanelView.setText(getString(com.kora.ai.widgetsdk.R.string.oops));
                    emptyPanelView.setTypeface(KaFontUtils.getCustomTypeface("regular", getActivity()));
                    emptyPanelView.setVisibility(View.VISIBLE);
                } else {
                    emptyPanelView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updatePannelData(PanelResponseData panelResponseData, String panelName, boolean isFromPresence) {

        if (panelResponseData != null && panelResponseData.getPanels() != null && panelResponseData.getPanels().size() > 0) {
            emptyPanelView.setVisibility(View.GONE);
            pannelAdapter = new PannelAdapter(getActivity(), panelResponseData, this);
            pannel_recycler.setAdapter(pannelAdapter);
            PanelBaseModel model = getHomeModelData(panelResponseData, panelName);

            if (model != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(getActivity());
                        if (sharedPreferenceUtils.getKeyValue(com.kore.ai.widgetsdk.utils.BundleConstants.IS_ON_BOARDED, false)) {
                        }
                    }
                }, 700);
            }

        } else {
            emptyPanelView.setTypeface(KaFontUtils.getCustomTypeface("regular", getActivity()));
            emptyPanelView.setVisibility(View.VISIBLE);
        }
    }

    private void getJWToken() {
        HashMap<String, Object> hsh = new HashMap<>();
        hsh.put("clientId", client_id);
        hsh.put("clientSecret", client_secret);
        hsh.put("identity", identity);
        hsh.put("aud", "https://idproxy.kore.com/authorize");
        hsh.put("isAnonymous", 1);

        Call<JWTTokenResponse> getJWTTokenService = BotJWTRestBuilder.getBotJWTRestAPI().getJWTToken(hsh);
        getJWTTokenService.enqueue(new Callback<JWTTokenResponse>() {
            @Override
            public void onResponse(Call<JWTTokenResponse> call, Response<JWTTokenResponse> response) {
                if (response.isSuccessful()) {
                    jwtKeyResponse = response.body();
                    jwtToken = jwtKeyResponse.getJwt();

                    if (sharedPreferenceUtils != null) {
                        sharedPreferenceUtils.putKeyValue("JWToken", jwtToken);
                    }

                    getPanelData("home", false);
                }
            }

            @Override
            public void onFailure(Call<JWTTokenResponse> call, Throwable t) {
                Log.e("Skill Panel Data", t.toString());
            }
        });
    }

    private PanelBaseModel getHomeModelData(PanelResponseData panelResponseData, String panelName) {
        PanelBaseModel model = null;
        if (panelResponseData != null && panelResponseData.getPanels() != null && panelResponseData.getPanels().size() > 0) {
            for (PanelResponseData.Panel panel : panelResponseData.getPanels()) {
                if (panel != null && panel.getName() != null && panel.getName().equalsIgnoreCase(panelName)) {
                    model = new PanelBaseModel();
                    panel.setItemClicked(true);
                    model.setData(panel);
                    return model;
                }
            }

        }

        return model;
    }

    @Override
    public void onPanelClicked(PanelBaseModel pModel) {
        AppUtils.showHideVirtualKeyboard(getActivity(), recyclerView_panel, false);

        if (pModel != null && ((PanelBaseModel) pModel).getData().get_id().equalsIgnoreCase(com.kore.ai.widgetsdk.utils.StringUtils.kora_thread)) {
            try {
                if (isAppInstalled(getActivity().getApplicationContext(), packageName)) {
                    Intent _intent = new Intent(Intent.ACTION_VIEW, Uri.parse("koretest://messages"));
                    _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(_intent);
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));

                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }
            } catch (Exception e) {
                if (isAppEnabled(getActivity().getApplicationContext(), packageName)) {
                    Intent intent = getActivity().getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().getApplicationContext().startActivity(intent);
                } else
                    Toast.makeText(getActivity().getApplicationContext(), appName + " app is not enabled.", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (pModel != null && ((PanelBaseModel) pModel).getData().get_id().equalsIgnoreCase(com.kore.ai.widgetsdk.utils.StringUtils.kora_team)) {
            try {
                if (isAppInstalled(getActivity().getApplicationContext(), packageName)) {
                    Intent _intent = new Intent(Intent.ACTION_VIEW, Uri.parse("koretest://teams"/*"http://threads.com/thread"*/));
                    _intent.putExtra("KoreHomeState", "KoreHomeState");
                    _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(_intent);
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }
            } catch (Exception e) {
                if (isAppEnabled(getActivity().getApplicationContext(), packageName)) {
                    Intent intent = getActivity().getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);
                    intent.putExtra("KoreHomeState", "KoreHomeState");
                    getActivity().getApplicationContext().startActivity(intent);
                } else
                    Toast.makeText(getActivity().getApplicationContext(), appName + " app is not enabled.", Toast.LENGTH_SHORT).show();
            }
            return;
        }


        if (mBottomSheetBehavior != null) {
            if (keyBoardShowing)
                AppUtils.showHideVirtualKeyboard(getActivity(), null, false);
            pModels = removeSystemHealth((PanelBaseModel) pModel);
            persistentSubLayout.setVisibility(View.VISIBLE);
            perssiatentPanel.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updatePanelData((PanelBaseModel) pModel, false);
                }
            }, 500);

            perssiatentPanel.post(new Runnable() {
                @Override
                public void run() {
//                    if(!isFromPresence) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    }
                }
            });
        }
    }

    private static boolean isAppEnabled(Context context, String packageName) {
        boolean appStatus = false;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (ai != null) {
                appStatus = ai.enabled;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appStatus;
    }

    private static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    private PanelBaseModel removeSystemHealth(PanelBaseModel model) {
        if (model != null && model.getData() != null && model.getData().getWidgets() != null) {
            boolean isHomePanel = model.getData().getName().equalsIgnoreCase("home");
            ArrayList<WidgetsModel> newWidgets = new ArrayList<>();
            for (int index = 0; index < model.getData().getWidgets().size(); index++) {
                if (model.getData().getWidgets().get(index).getTemplateType() != null &&
                        model.getData().getWidgets().get(index).getTemplateType().equalsIgnoreCase(WidgetConstants.SYSTEM_HEALTH_TEMPLATE)) {
//                    model.getData().getWidgets().remove(index);
                    continue;
                }
                if (isHomePanel && model.getData().getWidgets().get(index).getTemplateType() != null &&
                        model.getData().getWidgets().get(index).getTemplateType().equalsIgnoreCase(WidgetConstants.MEETINGS_TEMPLATE_SERVER)) {
//                    model.getData().getWidgets().remove(index);
                    continue;
                }
                newWidgets.add(model.getData().getWidgets().get(index));
            }
            model.getData().setWidgets(newWidgets);
        }
        return model;
    }

    private void updatePanelData(PanelBaseModel pModels, boolean isFirstLaunch) {
        int size = pModels.getData().getWidgets().size();

        if (size == 0) {
            showToast("No data to show");
            return;
        }

        LinearLayout img_background = (LinearLayout) view.findViewById(R.id.panel_title_icon);
        img_background.setBackgroundResource(0);

        try {
            img_skill.setVisibility(VISIBLE);
            String imageData;
            imageData = pModels.getData().getIcon();
            if (imageData.contains(",")) {
                imageData = imageData.substring(imageData.indexOf(",") + 1);
                byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img_skill.setImageBitmap(decodedByte);
            } else {
                Picasso.get().load(imageData).into(img_skill);
//                selectedItem.setColor(getResources().getColor(android.R.color.transparent));
            }
        } catch (Exception e) {
            img_skill.setVisibility(GONE);
        }


        if (size > 1) {
            List<WidgetsModel> tempModel = sortData(pModels);
//            if (tempModel != null && tempModel.size() > 1) {
//                editButton.setVisibility(View.VISIBLE);
//            }
//            else {
//                editButton.setVisibility(View.GONE);
//            }

            recyclerView_panel.setVisibility(View.VISIBLE);
            recyclerView_panel.setMinimumHeight((int) (screenHeight));

            single_item_container.removeAllViews();
            single_item_container.setVisibility(View.GONE);
            if (widgetBaseAdapter == null)
                widgetBaseAdapter = new KaWidgetBaseAdapterNew(getActivity(), size > 1 ? WidgetViewMoreEnum.COLLAPSE_VIEW : WidgetViewMoreEnum.EXPAND_VIEW, isFirstLaunch, (""));
            widgetBaseAdapter.setFirstLaunch(isFirstLaunch);
            recyclerView_panel.setAdapter(widgetBaseAdapter);
            widgetBaseAdapter.setWidget(pModels, jwtToken);
            widgetBaseAdapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utility.setRecyclerViewTempForOnboard(recyclerView_panel, pModels.getData().get_id());
                }
            }, 500);
        } else {
            editButton.setVisibility(View.GONE);
            single_item_container.setVisibility(View.VISIBLE);
            single_item_container.setMinimumHeight((int) (screenHeight));
            recyclerView_panel.setVisibility(View.GONE);
            if (widgetBaseAdapter != null) {
                recyclerView_panel.setAdapter(null);
                widgetBaseAdapter.notifyDataSetChanged();
            }
            single_item_container.removeAllViews();
            single_item_container.addView(getWidgetObject(pModels));
        }

        txtTitle.setText(pModels.getData().getName());
    }

    public int getItemViewType(WidgetsModel widget) {

//        if(widget.getName().equalsIgnoreCase("Table List"))
//            widget.setTemplateType("tableList");

        switch (widget.getTemplateType().toLowerCase()) {
            case WidgetConstants.MEETINGS_TEMPLATE_SERVER:
                return WidgetConstants.MEETINGS_TEMPLATE;
            case WidgetConstants.CHART_LIST:
                return WidgetConstants.CHART_LIST_TEMPLATE;

            case WidgetConstants.TASK_LIST:
                if (widget.getTemplateType().toLowerCase().equals("list")) {
                    return WidgetConstants.TASKS_SINGLE_TEMPLATE;
                } else {
                    return WidgetConstants.TASK_LIST_TEMPLATE;
                }

            case WidgetConstants.FILES_TEMPLATE_SERVER:
                if (widget.getTemplateType().toLowerCase().equals("list")) {
                    return WidgetConstants.FILES_SINGLE_TEMPLATE;
                } else {
                    return WidgetConstants.FILES_TEMPLATE;
                }

            case WidgetConstants.HASH_TAG_TEMPLATE_SERVER:
                return WidgetConstants.HASH_TAG_TEMPLATE;

            case WidgetConstants.ARTICLES_TEMPLATE_SERVER:
                return WidgetConstants.ARTICLES_TEMPLATE;

            case WidgetConstants.ANNOUNCEMENTS_TEMPLATE_SERVER:
                return WidgetConstants.ANNOUNCEMENTS_TEMPLATE;

            case WidgetConstants.SKILL_TEMPLATE_SERVER:
                return WidgetConstants.SKILL_TEMPLATE;

            case WidgetConstants.CLOUD_TEMPLATE_SERVER:
                return WidgetConstants.CLOUD_TEMPLATE;

            case WidgetConstants.HEADLINE_TEMPLATE_SERVER:
                return WidgetConstants.HEADLINE_TEMPLATE;
            case WidgetConstants.PIE_CHART:
                return WidgetConstants.PIE_CHART_TEMPLATE;
            case WidgetConstants.BAR_CHART:
                return WidgetConstants.BAR_CHART_TEMPLATE;
            case WidgetConstants.LINE_CHART:
                return WidgetConstants.LINE_CHART_TEMPLATE;
            case WidgetConstants.LIST_WIDGET:
                return WidgetConstants.LIST_WIDGET_TEMPLATE;
            case WidgetConstants.STANDARD:
                return WidgetConstants.TABLE_LIST_TEMPLATE;
            case WidgetConstants.FORM:
                return WidgetConstants.FORM_TEMPLATE;
            default:
                return WidgetConstants.DEFAULT_TEMPLATE;

        }
    }

    public View getWidgetObject(PanelBaseModel pModels) {
        View view = null;
        PanelLevelData panelData = new PanelLevelData();
        panelData.set_id("Id");
        panelData.setSkillId("Skill Id");
        panelData.setName(pModels.getData().getName() != null ? pModels.getData().getName() : "");

        switch (getItemViewType(pModels.getData().getWidgets().get(0))) {
            case WidgetConstants.MEETINGS_TEMPLATE:
                MeetingWidgetView mView = new MeetingWidgetView(getActivity(), this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                mView.setWidget(pModels.getData().getWidgets().get(0), panelData);
                view = mView;
                break;
            case WidgetConstants.CHART_LIST_TEMPLATE:
                ChartListWidgetView chartListWidgetView = new ChartListWidgetView(getActivity(), this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                chartListWidgetView.setWidget(pModels.getData().getWidgets().get(0), panelData);
                view = chartListWidgetView;
                break;
            case WidgetConstants.ARTICLES_TEMPLATE:
                ArticlesWidgetView articlesWidgetView = new ArticlesWidgetView(getActivity(), WidgetViewMoreEnum.EXPAND_VIEW);
                articlesWidgetView.setWidget(pModels.getData().getName(), pModels.getData().getWidgets().get(0), 0, true, panelData);
                view = articlesWidgetView;
                break;
//            case WidgetConstants.HASH_TAG_TEMPLATE:
//                TrendingHashTagView hashTagView = new TrendingHashTagView(this, this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
//                view = hashTagView;
//                break;

            case WidgetConstants.SKILL_TEMPLATE:
                SkillWidgetView skillWidgetView = new SkillWidgetView(getActivity(), pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
//                skillWidgetView.setWidget(pModels.getData().getWidgets().get(0), panelData);
                view = skillWidgetView;
                break;

            case WidgetConstants.FILES_SINGLE_TEMPLATE:
            case WidgetConstants.TASKS_SINGLE_TEMPLATE:
                GenericWidgetView gwv = new GenericWidgetView(getActivity(), 0, this, null,
                        pModels.getData().getName(), "180", false, WidgetViewMoreEnum.EXPAND_VIEW);
                gwv.setWidget(pModels.getData().getName(), pModels.getData().getWidgets().get(0), true, panelData);
                view = gwv;
                break;
            case WidgetConstants.PIE_CHART_TEMPLATE:
                PieChartWidgetView pieChartWidgetView = new PieChartWidgetView(getActivity(), pModels.getData().getName());
                pieChartWidgetView.setWidget(pModels.getData().getName(), pModels.getData().getWidgets().get(0), panelData, jwtToken);
                view = pieChartWidgetView;
                break;
            case WidgetConstants.BAR_CHART_TEMPLATE:
                BarChartWidgetView barChartWidgetView = new BarChartWidgetView(getActivity());
                barChartWidgetView.setWidget(pModels.getData().getName(), pModels.getData().getWidgets().get(0), panelData, jwtToken);
                view = barChartWidgetView;
                break;

            case WidgetConstants.LINE_CHART_TEMPLATE:
                LineChartWidgetView lineChartWidgetView = new LineChartWidgetView(getActivity());
                lineChartWidgetView.setWidget(pModels.getData().getName(), pModels.getData().getWidgets().get(0), panelData, jwtToken);
                view = lineChartWidgetView;
                break;

            case WidgetConstants.LIST_WIDGET_TEMPLATE:
                ListWidgetView listWidgetView = new ListWidgetView(getActivity(), pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                listWidgetView.setWidget(pModels.getData().getWidgets().get(0), panelData, "Ask MyIT", jwtToken);
                view = listWidgetView;
                break;

            case WidgetConstants.TABLE_LIST_TEMPLATE:
                TableListWidgetView tableListWidgetView = new TableListWidgetView(getActivity(), pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                tableListWidgetView.setWidget(pModels.getData().getWidgets().get(0), panelData,  "Ask MyIT", jwtToken);
                view = tableListWidgetView;
                break;

//            case WidgetConstants.TABLE_LIST_TEMPLATE:
//                BotTableListTemplateView botTableListTemplateView = new BotTableListTemplateView(getActivity());
//                BotTableListTemplateView.setWidget(pModels.getData().getName(),pModels.getData().getWidgets().get(0), panelData, jwtToken);
//                view = botTableListTemplateView;
//                break;


            case WidgetConstants.DEFAULT_TEMPLATE:
                DefaultWidgetView dView = new DefaultWidgetView(getActivity(), pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                dView.setWidget(pModels.getData().getWidgets().get(0), panelData, jwtToken);
                view = dView;
                break;
        }

        return view;

    }

    public List<WidgetsModel> sortData(PanelBaseModel pModels) {
        List<WidgetsModel> widgetsList = new ArrayList<>();
        if (pModels != null && pModels.getData() != null) {

            for (WidgetsModel widget : pModels.getData().getWidgets()) {
                if (!StringUtils.isNullOrEmptyWithTrim(widget.getName())) {
                    widgetsList.add(widget);
                }
            }
        }
        return widgetsList;
    }

    public WidgetComposeFooterInterface getPanelComposeFooterInterface() {
        return widgetComposeFooterInterface;
    }

    public void setPanelComposeFooterInterface(WidgetComposeFooterInterface composeFooterInterface, String identity) {
        this.widgetComposeFooterInterface = composeFooterInterface;
        SDKConfiguration.Client.identity = identity;
    }

//    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
//        return invokeGenericWebViewInterface;
//    }
//
//    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
//        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
//    }

    @Override
    public void updateItemToRefresh(int pos) {

    }

    @Override
    public void updateWeatherWidgetSummery(int type, String summary) {

    }

    @Override
    public void onWidgetMenuButtonClicked() {

    }

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {

    }

    @Override
    public void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel) {

    }

    @Override
    public void emailItemClicked(String action, HashMap customData) {

    }

    @Override
    public void calendarItemClicked(String action, BaseCalenderTemplateModel model) {

    }

    @Override
    public void tasksSelectedOrDeselected(boolean selecetd) {

    }

    @Override
    public void widgetItemSelected(boolean isSelected, int count) {

    }

    @Override
    public void navigationToDialAndJoin(String actiontype, String actionLink) {

    }

    @Override
    public void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel) {

    }

    @Override
    public void meetingNotesNavigation(Context context, String mId, String eId) {

    }

    @Override
    public void meetingWidgetViewMoreVisibility(boolean visible) {

    }

    @Override
    public void calendarContactItemClick(ContactViewListModel model) {

    }

    @Override
    public void welcomeSummaryItemClick(WelcomeChatSummaryModel model) {

    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        mBottomSheetBehavior.setLocked(false);
        try {
            if (mBottomSheetBehavior != null && mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior.onInterceptTouchEvent(cordinate_layout, perssiatentPanel, motionEvent);
            }
        } catch (Exception ex) {

        }

        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public void onEvent(EntityEditEvent entityEditEvent)
    {
        perssiatentPanel.setVisibility(GONE);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        widgetComposeFooterInterface.onPanelSendClick(entityEditEvent.getMessage(), true);
    }

    private void unregisterEventBus() {
        KoreEventCenter.unregister(this);
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }

}
