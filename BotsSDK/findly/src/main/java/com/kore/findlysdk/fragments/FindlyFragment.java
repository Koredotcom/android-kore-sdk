package com.kore.findlysdk.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kore.findlysdk.R;
import com.kore.findlysdk.activity.FullResultsActivity;
import com.kore.findlysdk.activity.GenericWebViewActivity;
import com.kore.findlysdk.adapters.AutoSuggestionsAdapter;
import com.kore.findlysdk.adapters.LiveSearchAdaper;
import com.kore.findlysdk.adapters.LiveSearchCyclerAdapter;
import com.kore.findlysdk.adapters.LiveSearchDynamicAdapter;
import com.kore.findlysdk.adapters.LiveSearchListAdapter;
import com.kore.findlysdk.adapters.PopularSearchAdapter;
import com.kore.findlysdk.adapters.RecentlySearchAdapter;
import com.kore.findlysdk.adapters.SearchAssistCarouselAdapter;
import com.kore.findlysdk.adapters.SearchResultsCarouselAdapter;
import com.kore.findlysdk.events.KoreEventCenter;
import com.kore.findlysdk.events.SocketDataTransferModel;
import com.kore.findlysdk.listners.BaseSocketConnectionManager;
import com.kore.findlysdk.listners.BotContentFragmentUpdate;
import com.kore.findlysdk.listners.BotSocketConnectionManager;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.listners.SocketChatListener;
import com.kore.findlysdk.models.AutoSuggestionsModel;
import com.kore.findlysdk.models.BotButtonModel;
import com.kore.findlysdk.models.BotRequest;
import com.kore.findlysdk.models.BotResponse;
import com.kore.findlysdk.models.BotResponseMessage;
import com.kore.findlysdk.models.CalEventsTemplateModel;
import com.kore.findlysdk.models.ComponentModel;
import com.kore.findlysdk.models.FormActionTemplate;
import com.kore.findlysdk.models.InteractionsConfigModel;
import com.kore.findlysdk.models.KnowledgeCollectionModel;
import com.kore.findlysdk.models.LiveSearchModel;
import com.kore.findlysdk.models.LiveSearchResultsDataModel;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.LiveSearchResultsOuterModel;
import com.kore.findlysdk.models.PayloadInner;
import com.kore.findlysdk.models.PayloadOuter;
import com.kore.findlysdk.models.PopularSearchModel;
import com.kore.findlysdk.models.PopularSearchResultsModel;
import com.kore.findlysdk.models.RecentSearchesModel;
import com.kore.findlysdk.models.RestResponse;
import com.kore.findlysdk.models.ResultsViewAppearance;
import com.kore.findlysdk.models.ResultsViewMapping;
import com.kore.findlysdk.models.ResultsViewModel;
import com.kore.findlysdk.models.ResultsViewSetting;
import com.kore.findlysdk.models.ResultsViewTemplate;
import com.kore.findlysdk.models.ResultsViewlayout;
import com.kore.findlysdk.models.SearchInterfaceModel;
import com.kore.findlysdk.models.SearchModel;
import com.kore.findlysdk.models.TabFacetsModel;
import com.kore.findlysdk.models.WidgetConfigModel;
import com.kore.findlysdk.net.BotRestBuilder;
import com.kore.findlysdk.net.SDKConfiguration;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.DateUtils;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.view.AgentTransferTemplateView;
import com.kore.findlysdk.view.AutoExpandListView;
import com.kore.findlysdk.view.HeightAdjustableViewPager;
import com.kore.findlysdk.view.RoundedCornersTransform;
import com.kore.findlysdk.websocket.SocketWrapper;
import com.kore.findlysdk.widgetviews.CustomBottomSheetBehavior;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FindlyFragment extends KaBaseFragment implements InvokeGenericWebViewInterface, ComposeFooterInterface, View.OnTouchListener
{
    private View view;
    private LinearLayout perssiatentPanel, llComposeBar, panel_root, llPlrSearch, llRecentlyAsked, llLogin;
    private RelativeLayout llLiveSearch;
    private LinearLayout llPopularSearch, llAllresults, llAutoSuggestions;
    private ImageView img_skill, ivChatIcon;
    private TextView closeBtnPanel, editButton, tvNoResult,tvSeeAllResults;
    private TextView  txtTitle, sendTv;
    private ListView recyclerView_panel;
    private Button panelDrag;
    private CustomBottomSheetBehavior mBottomSheetBehavior;
    private GestureDetector gestureScanner;
    private float screenHeight;
    private CoordinatorLayout cordinate_layout;
    private ArrayList<LiveSearchResultsModel> arrTempResults;
    private PopularSearchAdapter popularSearchAdapter;
    private RecentlySearchAdapter recentlySearchAdapter;
    private AutoExpandListView lvRecentlyAsked;
    private RecyclerView lvLiveSearch;
    private EditText edtTxtMessage;
    private LiveSearchModel liveSearchModel;
    private ResultsViewModel resultsViewModel;
    private SearchInterfaceModel searchInterfaceModel;
    private AutoSuggestionsModel autoSuggestionsModel;
    private TabFacetsModel tabFacetsModel;
    ArrayList<SearchModel> arrLiveSearchModels = new ArrayList<>();
    private WebView wvLoadUrl;
    private PopularSearchResultsModel popularSearchResultsModel;
    private FragmentTransaction fragmentTransaction;
    private BotContentFindlyFragment botContentFragment;
    private BotContentFragmentUpdate botContentFragmentUpdate;
    private Gson gson = new Gson();
    final Handler handler = new Handler();
    private ArrayList<LiveSearchResultsModel> arrTempAllResults;
    private ArrayList<String> arrRecentSearches;
    private EditText edtPassword, edtUserId;
    private TextView tvUserLogin,tvErrorLogin, tvWelcomeMsg, sendTvOutside;
    private LinearLayout llWelcomeMessage;
    private ImageView ivWelcomeImage;
    private boolean isEditTouched = false;
    private String displayMsg = "Hello! How can i help you?";
    private boolean lockLiveSearch = false;
    private SharedPreferences sharedPreferences;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;
    private AutoExpandListView lvAutoSuggestions;
    private RoundedCornersTransform roundedCornersTransform;
    private ImageView rec_audio_img;
    private RecentSearchesModel recentSearchesModel;
    private ResultsViewSetting liveSearchSetting;
    private ResultsViewSetting searchSetting;
    private ResultsViewSetting fullSearchSetting;
    private ResultsViewAppearance faq;
    private ResultsViewAppearance page;
    private ResultsViewAppearance document;
    private ResultsViewAppearance task;
    private ResultsViewAppearance data;
    private ResultsViewAppearance default_group;
    private float dp1;
    private String speakerIcon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAETSURBVHgBxVPbSsNAEN2d3XRVSvqQBwNNQwmpgn3xC/x7/0ETEpG2oGAeTKVJ3HXH3Qch1KQiKfTAsJc5c/YyM4QMBO1zhGEYAYwWdg6gn/I8T7p4rGtzPr/xOWcLKat7rdUzIXw5Hl+W2+1btc+FLgGA2kWUa4PKGmO4FuLT6+SSgTi9AG8voii6OkT+8bczctwnNA3stGYXTTMqzOVmQRCcW1OKzBDx3VBcANj1Cvj+5BWAepR+GBKsOD+7s0apk0spSzO6dV0X7ZhflTidxoEQcG2q79Gc/mL30jSdMCZuHYcmWZatDgpYxHHsSkmWjFFPa5SIpFSKJ5vNQ0H+A/vrf2WG9gWaNtkL/Er6GmoQvgHqBWZkE0i8BAAAAABJRU5ErkJggg==";
    private Context context;
    private RecyclerView lvPopularSearch;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ResultsViewTemplate defaultTemplate;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.findly_layout, container, false);
        screenHeight = (int) AppControl.getInstance(getContext()).getDimensionUtil().screenHeight;
        dp1 = (int) AppControl.getInstance(getActivity()).getDimensionUtil().density;
        perssiatentPanel = view.findViewById(R.id.persistentPanel);
        img_skill = view.findViewById(R.id.img_skill);
        txtTitle = view.findViewById(R.id.txtTitle);
        editButton = (TextView)  view.findViewById(R.id.editButton);
        edtTxtMessage = (EditText) view.findViewById(R.id.edtTxtMessage);
        cordinate_layout = view.findViewById(R.id.cordinate_layout);
        recyclerView_panel =  view.findViewById(R.id.recyclerView_panel);
        closeBtnPanel = (TextView)  view.findViewById(R.id.closeBtnPanel);
        closeBtnPanel.setTypeface(getTypeFaceObj(getActivity()));
        panelDrag =  view.findViewById(R.id.panel_drag);
        lvPopularSearch = (RecyclerView) view.findViewById(R.id.lvPopularSearch);
        lvRecentlyAsked = (AutoExpandListView) view.findViewById(R.id.lvRecentlyAsked);
        lvLiveSearch = (RecyclerView) view.findViewById(R.id.lvLiveSearch);
        llRecentlyAsked = (LinearLayout) view.findViewById(R.id.llRecentlyAsked);
        mBottomSheetBehavior = CustomBottomSheetBehavior.from(perssiatentPanel);
        llComposeBar =  (LinearLayout) view.findViewById(R.id.llComposeBar);
        llPopularSearch = (LinearLayout) view.findViewById(R.id.llPopularSearch);
        llPlrSearch = (LinearLayout) view.findViewById(R.id.llPlrSearch);
        llLiveSearch = (RelativeLayout) view.findViewById(R.id.llLiveSearch);
        llLogin = (LinearLayout) view.findViewById(R.id.llLogin);
        panel_root = (LinearLayout) view.findViewById(R.id.panel_root);
        tvNoResult = (TextView) view.findViewById(R.id.tvNoResult);
        tvSeeAllResults = (TextView) view.findViewById(R.id.tvSeeAllResults);
        sendTv = (TextView) view.findViewById(R.id.sendTv);
        wvLoadUrl = (WebView)view.findViewById(R.id.wvLoadUrl);
        ivChatIcon = (ImageView) view.findViewById(R.id.ivChatIcon);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtUserId = (EditText) view.findViewById(R.id.edtUserId);
        tvUserLogin = (TextView) view.findViewById(R.id.tvUserLogin);
        tvErrorLogin = (TextView) view.findViewById(R.id.tvErrorLogin);
        llWelcomeMessage = (LinearLayout) view.findViewById(R.id.llWelcomeMessage);
        ivWelcomeImage = (ImageView) view.findViewById(R.id.ivWelcomeImage);

        llAllresults = (LinearLayout) view.findViewById(R.id.llAllresults);
        tvWelcomeMsg = (TextView) view.findViewById(R.id.tvWelcomeMsg);
        llAutoSuggestions = (LinearLayout) view.findViewById(R.id.llAutoSuggestions);
        lvAutoSuggestions = (AutoExpandListView) view.findViewById(R.id.lvAutoSuggestions);
        roundedCornersTransform = new RoundedCornersTransform();
        rec_audio_img = (ImageView) view.findViewById(R.id.rec_audio_img);
        sendTvOutside = (TextView) view.findViewById(R.id.sendTvOutside);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        lvPopularSearch.setLayoutManager(linearLayoutManager);
        wvLoadUrl.getSettings().setJavaScriptEnabled(true);
//        wvLoadUrl.loadUrl("https://www.caremark.com/");

        edtTxtMessage.setOnTouchListener(this);

        KoreEventCenter.register(this);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        botContentFragment = new BotContentFindlyFragment();
        botContentFragment.setArguments(getActivity().getIntent().getExtras());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();
        setBotContentFragmentUpdate(botContentFragment);

        BotSocketConnectionManager.getInstance().jwtCallWithConfig(getJwtBody(SDKConfiguration.Client.client_id, SDKConfiguration.Client.client_secret, SDKConfiguration.Client.identity, false), false);
        lvLiveSearch.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        closeBtnPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perssiatentPanel.setVisibility(GONE);
                llPopularSearch.setVisibility(GONE);
                edtTxtMessage.setText("");

                if(botContentFragment != null)
                    botContentFragment.removeAllChatMessages();

                displayMessage(displayMsg);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDataDialog(getActivity());
            }
        });

        mBottomSheetBehavior.setPeekHeight((int) ((AppControl.getInstance(getActivity()).getDimensionUtil().screenHeight)));
        mBottomSheetBehavior.setBottomSheetCallback(new CustomBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mBottomSheetBehavior.setLocked(false);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mBottomSheetBehavior.setLocked(false);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mBottomSheetBehavior.setLocked(false);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mBottomSheetBehavior.setLocked(false);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        perssiatentPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                llPopularSearch.setVisibility(View.GONE);
                edtTxtMessage.clearFocus();

//                SearchModel liveSearchResultsModel = new SearchModel();
//                liveSearchResultsModel.setLeft(true);
//                liveSearchResultsModel.setTitle("Hello! How can i help you?");
//
//                setObject(liveSearchResultsModel);
                return false;

            }
        });

        llPopularSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(llAutoSuggestions.getVisibility() == VISIBLE)
                    llAutoSuggestions.setVisibility(GONE);
                else
                {
                    llPopularSearch.setVisibility(View.GONE);
                    edtTxtMessage.clearFocus();
                }

//                SearchModel liveSearchResultsModel = new SearchModel();
//                liveSearchResultsModel.setLeft(true);
//                liveSearchResultsModel.setTitle("Hello! How can i help you?");
//
//                setObject(liveSearchResultsModel);
                return false;

            }
        });

        edtTxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!StringUtils.isNullOrEmpty(s.toString()))
                {
                    if(!lockLiveSearch)
                    {
                        getAutoSuggestions(s.toString());
                        getLiveSearch(s.toString());

                        sendTv.setTextColor(getActivity().getResources().getColor(R.color.white));
                        sendTvOutside.setTextColor(getActivity().getResources().getColor(R.color.white));

                        ((GradientDrawable) sendTv.getBackground()).setColor(getActivity().getResources().getColor(R.color.primary));
                        ((GradientDrawable) sendTvOutside.getBackground()).setColor(getActivity().getResources().getColor(R.color.primary));
                    }
                }
                else
                {
                    llPlrSearch.setVisibility(View.VISIBLE);
                    llRecentlyAsked.setVisibility(View.VISIBLE);
                    llLiveSearch.setVisibility(GONE);
                    tvNoResult.setVisibility(GONE);

                    if(searchInterfaceModel != null && searchInterfaceModel.getWidgetConfig() != null)
                    {
                        if(!StringUtils.isNullOrEmpty(searchInterfaceModel.getWidgetConfig().getButtonTextColor()))
                        {
                            sendTv.setTextColor(Color.parseColor(searchInterfaceModel.getWidgetConfig().getButtonTextColor()));
                            sendTvOutside.setTextColor(Color.parseColor(searchInterfaceModel.getWidgetConfig().getButtonTextColor()));
                        }

                        if(!StringUtils.isNullOrEmpty(searchInterfaceModel.getWidgetConfig().getButtonFillColor()))
                        {
                            ((GradientDrawable) sendTv.getBackground()).setColor(Color.parseColor(searchInterfaceModel.getWidgetConfig().getButtonFillColor()));
                            ((GradientDrawable) sendTvOutside.getBackground()).setColor(Color.parseColor(searchInterfaceModel.getWidgetConfig().getButtonFillColor()));
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}

            @Override
            public void afterTextChanged(Editable s)
            {}
        });

        tvSeeAllResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(liveSearchModel != null && liveSearchModel.getTemplate() != null && liveSearchModel.getTemplate().getResults() != null)
                {
                    Intent intent = new Intent(context, FullResultsActivity.class);
                    intent.putExtra("originalQuery", liveSearchModel.getTemplate().getOriginalQuery());

                    if(sharedPreferences != null)
                        intent.putExtra("messagePayload", sharedPreferences.getString("Payload", ""));

                    startActivity(intent);
                }
            }
        });

        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!StringUtils.isNullOrEmpty(edtTxtMessage.getText().toString()))
                {
                    if(edtTxtMessage.getText().toString().equalsIgnoreCase("Y"))
                    {
                        lockLiveSearch = false;
                    }

                    if(edtTxtMessage.getText().toString().equalsIgnoreCase("Pay bill")
                            || edtTxtMessage.getText().toString().equalsIgnoreCase("Show Balance"))
                    {
                        if(getLogin(context, BotResponse.USER_LOGIN))
                        {
                            llPopularSearch.setVisibility(GONE);
                            perssiatentPanel.setVisibility(View.VISIBLE);
//                            saveStringPreferences(edtTxtMessage.getText().toString());
//                    sendMessage(edtTxtMessage.getText().toString());
                            sendMessageText(edtTxtMessage.getText().toString());
                            edtTxtMessage.setText("");
                            lockLiveSearch = true;
                        }
                        else
                        {
                            llPopularSearch.setVisibility(GONE);
                            llLogin.setVisibility(View.VISIBLE);
                            lockLiveSearch = true;
                        }
                    }
                    else
                    {
                        llPopularSearch.setVisibility(GONE);
                        perssiatentPanel.setVisibility(View.VISIBLE);
                        llAutoSuggestions.setVisibility(GONE);
//                        saveStringPreferences(edtTxtMessage.getText().toString());
                        sendMessageText(edtTxtMessage.getText().toString());
                        edtTxtMessage.setText("");
                    }
                }
            }
        });

        sendTvOutside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!StringUtils.isNullOrEmpty(edtTxtMessage.getText().toString()))
                {
                    if(edtTxtMessage.getText().toString().equalsIgnoreCase("Y"))
                    {
                        lockLiveSearch = false;
                    }

                    if(edtTxtMessage.getText().toString().equalsIgnoreCase("Pay bill")
                            || edtTxtMessage.getText().toString().equalsIgnoreCase("Show Balance"))
                    {
                        if(getLogin(context, BotResponse.USER_LOGIN))
                        {
                            llPopularSearch.setVisibility(GONE);
                            perssiatentPanel.setVisibility(View.VISIBLE);
                            sendMessageText(edtTxtMessage.getText().toString());
                            edtTxtMessage.setText("");
                            lockLiveSearch = true;
                        }
                        else
                        {
                            llPopularSearch.setVisibility(GONE);
                            llLogin.setVisibility(View.VISIBLE);
                            lockLiveSearch = true;
                        }
                    }
                    else
                    {
                        llPopularSearch.setVisibility(GONE);
                        perssiatentPanel.setVisibility(View.VISIBLE);
                        llAutoSuggestions.setVisibility(GONE);
                        sendMessageText(edtTxtMessage.getText().toString());
                        edtTxtMessage.setText("");
                    }
                }
            }
        });

        setLogin(context, BotResponse.USER_LOGIN, false);


        lvRecentlyAsked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                edtTxtMessage.setText(recentSearchesModel.getRecentSearches().get(i));
                edtTxtMessage.setSelection(edtTxtMessage.getText().toString().length());
            }
        });

//        lvPopularSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if(popularSearchResultsModel != null && popularSearchResultsModel.getResult() != null && popularSearchResultsModel.getResult().size() > 0)
//                {
//                    ArrayList<PopularSearchModel> arrPopularSearchModels = popularSearchResultsModel.getResult();
//                    edtTxtMessage.setText(arrPopularSearchModels.get(i).get_id());
//                    edtTxtMessage.setSelection(edtTxtMessage.getText().toString().length());
//                }
//            }
//        });

        lvRecentlyAsked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                edtTxtMessage.setText(recentSearchesModel.getRecentSearches().get(i));
                edtTxtMessage.setSelection(edtTxtMessage.getText().toString().length());
            }
        });

        tvUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!StringUtils.isNullOrEmpty(edtUserId.getText().toString()) && !StringUtils.isNullOrEmpty(edtPassword.getText().toString()))
                {
                    if(validate())
                    {
                        llLogin.setVisibility(GONE);
                        llPopularSearch.setVisibility(GONE);
                        tvErrorLogin.setVisibility(GONE);
                        setLogin(context, BotResponse.USER_LOGIN, true);

                        saveStringPreferences(edtTxtMessage.getText().toString());
                        sendMessageText(edtTxtMessage.getText().toString());
                        edtTxtMessage.setText("");
                    }
                    else
                        tvErrorLogin.setVisibility(View.VISIBLE);
                }
            }
        });

        try
        {
            speakerIcon = speakerIcon.substring(speakerIcon.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(speakerIcon.getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            rec_audio_img.setImageBitmap(decodedByte);
        } catch (Exception e) {
        }

        return view;
    }

    SocketChatListener sListener = new SocketChatListener()
    {
        @Override
        public void onMessage(BotResponse botResponse) {
            processPayload("", botResponse);
        }

        @Override
        public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
            if(state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED)
            {
                getPopularSearch();
                getTabFacets();

                if(resultsViewModel == null)
                    getResultViewSettings();

                doUnlock();
            }
        }

        @Override
        public void onMessage(SocketDataTransferModel data) {
            if (data == null) return;
            if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE))
            {
                processPayload(data.getPayLoad(), null);

            } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE)) {
                if (botContentFragment != null) {
                    botContentFragment.updateContentListOnSend(data.getBotRequest());
                }
            }
        }
    };

    @Override
    public void onDestroy()
    {
        BotSocketConnectionManager.getInstance().shutDownConnection();
        super.onDestroy();
    }

    private void saveStringPreferences(String toString)
    {
        ArrayList<String> arrRecentSearchDos = getStringArrayPref(context, BotResponse.RECENT_SEARCH);

        if(arrRecentSearchDos != null && arrRecentSearchDos.size() > 0)
        {
            boolean isThere = arrRecentSearchDos.contains(toString);
            if(!isThere)
            {
                if(arrRecentSearchDos.size() > 4)
                {
                    arrRecentSearchDos.remove(4);
                    arrRecentSearchDos.add(0, toString);
                }
                else
                {
                    arrRecentSearchDos.add(0, toString);
                }
            }
        }
        else
            arrRecentSearchDos.add(toString);

        if(arrRecentSearchDos != null)
            setStringArrayPref(context, BotResponse.RECENT_SEARCH, arrRecentSearchDos);

        getRecentlyAsked();
    }

    public void setActivityContext(Context activityContext)
    {
        this.context= activityContext;
    }

    //    @Override
//    public void onBackPressed()
//    {
//        if(perssiatentPanel.getVisibility() == View.VISIBLE)
//        {
//            perssiatentPanel.setVisibility(GONE);
//            llPopularSearch.setVisibility(GONE);
//            edtTxtMessage.setText("");
//
//            if(botContentFragment != null)
//                botContentFragment.removeAllChatMessages();
//
//            displayMessage(displayMsg);
//        }
//        else
//            super.onBackPressed();
//    }

    public void displayMessage(String text)
    {
        PayloadInner payloadInner = new PayloadInner();
        payloadInner.setTemplate_type("text");

        PayloadOuter payloadOuter = new PayloadOuter();
        payloadOuter.setText(text);
        payloadOuter.setType("text");
        payloadOuter.setPayload(payloadInner);

        ComponentModel componentModel = new ComponentModel();
        componentModel.setType("text");
        componentModel.setPayload(payloadOuter);

        BotResponseMessage botResponseMessage = new BotResponseMessage();
        botResponseMessage.setType("text");
        botResponseMessage.setComponent(componentModel);

        ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
        arrBotResponseMessages.add(botResponseMessage);

        BotResponse botResponse = new BotResponse();
        botResponse.setType("text");
        botResponse.setMessage(arrBotResponseMessages);

        processPayload("", botResponse);
    }

    public static Typeface getTypeFaceObj(Context context) {
        return ResourcesCompat.getFont(context, R.font.icomoon);
    }

    public void getPopularSearch()
    {
        Call<PopularSearchResultsModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getPopularSearch(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(getActivity()).getAccessToken(), SocketWrapper.getInstance(getActivity()).getJWTToken());
        getJWTTokenService.enqueue(new Callback<PopularSearchResultsModel>() {
            @Override
            public void onResponse(Call<PopularSearchResultsModel> call, Response<PopularSearchResultsModel> response) {
                if (response.isSuccessful())
                {
                    popularSearchResultsModel = response.body();

                    if(popularSearchResultsModel != null && popularSearchResultsModel.getResult() != null && popularSearchResultsModel.getResult().size() > 0)
                    {
                        llPlrSearch.setVisibility(View.VISIBLE);
                        llLiveSearch.setVisibility(GONE);
                        popularSearchAdapter = new PopularSearchAdapter(context, popularSearchResultsModel.getResult());
                        popularSearchAdapter.setComposeFooterInterface(FindlyFragment.this);
                        lvPopularSearch.setAdapter(popularSearchAdapter);
                    }
                }
                else
                    llPlrSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<PopularSearchResultsModel> call, Throwable t) {
                Log.e("Popular Search Data", t.toString());
                llPlrSearch.setVisibility(View.VISIBLE);
            }
        });
    }

    public void doUnlock()
    {
        if(sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BundleConstants.CUSTOM_DATA, "");
        editor.commit();

        Call<ArrayList<PopularSearchModel>> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().doUnlock(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(getActivity()).getAccessToken(), SocketWrapper.getInstance(getActivity()).getJWTToken(), getUnlockBody());
        getJWTTokenService.enqueue(new Callback<ArrayList<PopularSearchModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PopularSearchModel>> call, Response<ArrayList<PopularSearchModel>> response) {
                if (response.isSuccessful())
                {
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PopularSearchModel>> call, Throwable t) {
                Log.e("Popular Search Data", t.toString());
            }
        });
    }

    private JsonObject getJwtBody(String clientId, String clientSecret, String identity, boolean isAnonymous)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("clientId", clientId);
        jsonObject.addProperty("clientSecret", clientSecret);
        jsonObject.addProperty("identity:", identity);
        jsonObject.addProperty("aud", "");
        jsonObject.addProperty("isAnonymous", isAnonymous);

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    public void getRecentSearches()
    {
        Call<RecentSearchesModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getRecentSearches(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(getActivity()).getAccessToken(), SocketWrapper.getInstance(getActivity()).getJWTToken());
        getJWTTokenService.enqueue(new Callback<RecentSearchesModel>() {
            @Override
            public void onResponse(Call<RecentSearchesModel> call, Response<RecentSearchesModel> response) {
                if (response.isSuccessful())
                {
                    recentSearchesModel = response.body();

                    if(recentSearchesModel != null && recentSearchesModel.getRecentSearches() != null && recentSearchesModel.getRecentSearches().size() > 0)
                    {
                        lvRecentlyAsked.setVisibility(View.VISIBLE);
                        llRecentlyAsked.setVisibility(View.VISIBLE);
                        llLiveSearch.setVisibility(GONE);
                        recentlySearchAdapter = new RecentlySearchAdapter(context, recentSearchesModel.getRecentSearches());
                        lvRecentlyAsked.setAdapter(recentlySearchAdapter);
                    }
                    else
                    {
                        lvRecentlyAsked.setVisibility(GONE);
                        llRecentlyAsked.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    lvRecentlyAsked.setVisibility(GONE);
                    llRecentlyAsked.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RecentSearchesModel> call, Throwable t) {
                Log.e("Popular Search Data", t.toString());
                llRecentlyAsked.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getRecentlyAsked()
    {
        arrRecentSearches = getStringArrayPref(context, BotResponse.RECENT_SEARCH);

        if(arrRecentSearches != null && arrRecentSearches.size() > 0)
        {
            llRecentlyAsked.setVisibility(View.VISIBLE);
            llLiveSearch.setVisibility(GONE);
            recentlySearchAdapter = new RecentlySearchAdapter(context, arrRecentSearches);
            lvRecentlyAsked.setAdapter(recentlySearchAdapter);
        }
    }

    public void getLiveSearch(String query)
    {
        try
        {
            JsonObject jsonObject = getJsonBody(query, false, 0, 9);
            Call<LiveSearchModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getLiveSearch(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(),"bearer "+SocketWrapper.getInstance(getActivity()).getAccessToken(),"published" ,jsonObject, SocketWrapper.getInstance(getActivity()).getJWTToken());
            getJWTTokenService.enqueue(new Callback<LiveSearchModel>() {
                @Override
                public void onResponse(Call<LiveSearchModel> call, Response<LiveSearchModel> response) {
                    if (response.isSuccessful())
                    {
                        liveSearchModel = response.body();

                        if(liveSearchModel != null && liveSearchModel.getTemplate() != null && liveSearchModel.getTemplate().getResults() != null)
                        {
                            lockLiveSearch = false;
                            cordinate_layout.setVisibility(View.VISIBLE);
                            perssiatentPanel.setVisibility(View.VISIBLE);
                            llPopularSearch.setVisibility(View.VISIBLE);

                            tvNoResult.setVisibility(GONE);
                            llPlrSearch.setVisibility(GONE);
                            llRecentlyAsked.setVisibility(GONE);
                            llLiveSearch.setVisibility(View.VISIBLE);
                            llAllresults.removeAllViews();

                            if(liveSearchModel.getTemplate().getResults().getFaq() != null)
                            {
                                addLayoutView(liveSearchModel.getTemplate().getResults().getFaq(), faq,1, BundleConstants.FAQ);
                            }

                            if(liveSearchModel.getTemplate().getResults().getDefault_group() != null)
                            {
                                addLayoutView(liveSearchModel.getTemplate().getResults().getDefault_group(), default_group, 6, "");
                            }

                            if(liveSearchModel.getTemplate().getResults().getWeb() != null)
                            {
                                addLayoutView(liveSearchModel.getTemplate().getResults().getWeb(), page, 2, BundleConstants.WEB);
                            }

                            if(liveSearchModel.getTemplate().getResults().getTask() != null)
                            {
                                if(task == null)
                                    task = getResultsViewAppearance(BundleConstants.TASK);

                                addLayoutView(liveSearchModel.getTemplate().getResults().getTask(), task, 3, BundleConstants.TASK);
                            }

                            if(liveSearchModel.getTemplate().getResults().getFile() != null)
                            {
                                addLayoutView(liveSearchModel.getTemplate().getResults().getFile(), document, 4, BundleConstants.FILE);
                            }

//                            if(liveSearchModel.getTemplate().getResults().getData() != null)
//                            {
//                                addLayoutView(liveSearchModel.getTemplate().getResults().getData(), data, 5, BundleConstants.DATA);
//                            }
                        }
                        else
                        {
                            lockLiveSearch = true;
                            llPlrSearch.setVisibility(GONE);
                            llLiveSearch.setVisibility(GONE);
                            llRecentlyAsked.setVisibility(GONE);
                            llPopularSearch.setVisibility(GONE);
                            perssiatentPanel.setVisibility(View.VISIBLE);
                            tvNoResult.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<LiveSearchModel> call, Throwable t) {
                    Log.e("Live Search Data", t.toString());
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addLayoutView(LiveSearchResultsDataModel liveSearchResultsDataModel, final ResultsViewAppearance appearance, int from, String constant)
    {
        if(liveSearchResultsDataModel.getData() != null && liveSearchResultsDataModel.getData().size() > 0)
        {
            LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.search_template_new_cell, null);
            final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
            final RelativeLayout rlTitle = (RelativeLayout) llFaq.findViewById(R.id.rlTitle);
            final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
            final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
            final RelativeLayout rlArrows = (RelativeLayout) llFaq.findViewById(R.id.rlArrows);
            TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
            final HeightAdjustableViewPager heightAdjustableViewPager = (HeightAdjustableViewPager) llFaq.findViewById(R.id.carouselViewpager);
            int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
            final AutoExpandListView lvResults = (AutoExpandListView) llFaq.findViewById(R.id.lvResults);
            alResults.addItemDecoration(new VerticalSpaceItemDecoration((int)(10 * dp1)));
//            ViewGroup.LayoutParams params= heightAdjustableViewPager.getLayoutParams();
//            params.height = (int)(170 * dp1);    //500px
//            heightAdjustableViewPager.setLayoutParams(params);

            heightAdjustableViewPager.setPageMargin(pageMargin);
            if(appearance != null && appearance.getTemplate() != null && appearance.getTemplate().getType() != null)
            {
                heightAdjustableViewPager.setVisibility(GONE);
                alResults.setVisibility(GONE);
                lvResults.setVisibility(GONE);

                if(appearance.getTemplate().getLayout() != null)
                {
                    if(appearance.getTemplate().getLayout().getRenderTitle())
                        rlTitle.setVisibility(VISIBLE);
                    else
                        rlTitle.setVisibility(GONE);
                }

                if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                {
                    alResults.setVisibility(VISIBLE);
                    alResults.setLayoutManager(new GridLayoutManager(context, 2));
                    alResults.setAdapter(new LiveSearchCyclerAdapter(context, getTopList(liveSearchResultsDataModel.getData(),  constant), 0, FindlyFragment.this, FindlyFragment.this));
                }
                else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                {
                    heightAdjustableViewPager.setVisibility(View.VISIBLE);
                    alResults.setVisibility(GONE);

//                    if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
//                    {
//                        params.height = (int)(300 * dp1);    //500px
//                        heightAdjustableViewPager.setLayoutParams(params);
//                    }
//                    else if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_HEADER))
//                    {
//                        params.height = (int)(110 * dp1);    //500px
//                        heightAdjustableViewPager.setLayoutParams(params);
//                    }
//                    else
//                    {
//                        params.height = (int)(110 * dp1);    //500px
//                        heightAdjustableViewPager.setLayoutParams(params);
//                    }

                    heightAdjustableViewPager.setAdapter(new SearchAssistCarouselAdapter(context, getTopList(liveSearchResultsDataModel.getData(),  constant), 0, FindlyFragment.this, FindlyFragment.this));
                }
                else
                {
                    if(appearance.getTemplate().getLayout() != null && !StringUtils.isNullOrEmpty(appearance.getTemplate().getLayout().getListType()) && !appearance.getTemplate().getLayout().getListType().equalsIgnoreCase(BundleConstants.PLAIN))
                    {
                        lvResults.setDivider(getResources().getDrawable(android.R.color.transparent));
                        lvResults.setDividerHeight((int)(10 * dp1));
                    }

                    lvResults.setVisibility(VISIBLE);
                    lvResults.setAdapter(new LiveSearchAdaper(context, getTopList(liveSearchResultsDataModel.getData(),  constant), 0, FindlyFragment.this, FindlyFragment.this));
                }
            }

            switch (from)
            {
                case 1:
                {
                    tvPageTitle.setVisibility(VISIBLE);
                    tvPageTitle.setText("FAQS");
                    break;
                }
                case 2:
                {
                    tvPageTitle.setVisibility(VISIBLE);
                    tvPageTitle.setText("Web");
                    break;
                }
                case 3:
                {
                    tvPageTitle.setVisibility(VISIBLE);
                    tvPageTitle.setText("Task");
                    break;
                }
                case 4:
                {
                    tvPageTitle.setVisibility(VISIBLE);
                    tvPageTitle.setText("File");
                    break;
                }
                case 5:
                {
                    tvPageTitle.setVisibility(VISIBLE);
                    tvPageTitle.setText("Data");
                    break;
                }
                default:
                {
                    rlArrows.setVisibility(GONE);
                    tvPageTitle.setVisibility(GONE);
                }
            }


            ibResults.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alResults.getVisibility() == View.GONE || heightAdjustableViewPager.getVisibility() == GONE
                            || lvResults.getVisibility() == View.GONE)
                    {
                        if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                            heightAdjustableViewPager.setVisibility(View.VISIBLE);
                        else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                            alResults.setVisibility(View.VISIBLE);
                        else
                            lvResults.setVisibility(View.VISIBLE);

                        ibResults.setVisibility(GONE);
                        ibResults2.setVisibility(View.VISIBLE);
                    }
                }
            });

            ibResults2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alResults.getVisibility() == View.VISIBLE || heightAdjustableViewPager.getVisibility() == View.VISIBLE
                            || lvResults.getVisibility() == View.VISIBLE)
                    {
                        if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                            heightAdjustableViewPager.setVisibility(GONE);
                        else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                            alResults.setVisibility(GONE);
                        else
                            lvResults.setVisibility(GONE);

                        ibResults2.setVisibility(GONE);
                        ibResults.setVisibility(View.VISIBLE);
                    }
                }
            });

            llFaq.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    llAutoSuggestions.setVisibility(GONE);
                    return false;
                }
            });

            llAllresults.addView(llFaq);
        }
    }

    private void addResultsView(ArrayList<HashMap<String, Object>> arrSearchResults, final ResultsViewAppearance appearance, int from, String constant)
    {
        LinearLayout llFaq = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.search_template_new_cell, null);
        final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
        final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
        final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
        TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);
        final HeightAdjustableViewPager heightAdjustableViewPager = (HeightAdjustableViewPager) llFaq.findViewById(R.id.carouselViewpager);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
        final AutoExpandListView lvResults = (AutoExpandListView) llFaq.findViewById(R.id.lvResults);

        ViewGroup.LayoutParams params= heightAdjustableViewPager.getLayoutParams();
        params.height = (int)(170 * dp1);    //500px
        heightAdjustableViewPager.setLayoutParams(params);

        heightAdjustableViewPager.setPageMargin(pageMargin);
        if(appearance != null && appearance.getTemplate() != null && appearance.getTemplate().getType() != null)
        {
            heightAdjustableViewPager.setVisibility(GONE);
            alResults.setVisibility(GONE);
            lvResults.setVisibility(GONE);

            if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
            {
                alResults.setVisibility(VISIBLE);
                alResults.setLayoutManager(new GridLayoutManager(context, 2));
                alResults.setAdapter(new LiveSearchDynamicAdapter(context, getResultsTopList(arrSearchResults,  constant), 0, FindlyFragment.this, FindlyFragment.this));
            }
            else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
            {
                heightAdjustableViewPager.setVisibility(View.VISIBLE);

                if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
                {
                    params.height = (int)(300 * dp1);    //500px
                    heightAdjustableViewPager.setLayoutParams(params);
                }
                else if(appearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_HEADER))
                {
                    params.height = (int)(100 * dp1);    //500px
                    heightAdjustableViewPager.setLayoutParams(params);
                }

                heightAdjustableViewPager.setAdapter(new SearchResultsCarouselAdapter(context, getResultsTopList(arrSearchResults,  constant), 0, FindlyFragment.this, FindlyFragment.this));
            }
            else
            {
                lvResults.setVisibility(VISIBLE);
                lvResults.setAdapter(new LiveSearchListAdapter(context, getResultsTopList(arrSearchResults,  constant), 0, FindlyFragment.this, FindlyFragment.this));
            }
        }

        switch (from)
        {
            case 1:
            {
                tvPageTitle.setText("FAQS");
                break;
            }
            case 2:
            {
                tvPageTitle.setText("Web");
                break;
            }
            case 3:
            {
                tvPageTitle.setText("Task");
                break;
            }
            case 4:
            {
                tvPageTitle.setText("File");
                break;
            }
            case 5:
            {
                tvPageTitle.setText("Data");
                break;
            }
        }


        ibResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alResults.getVisibility() == View.GONE || heightAdjustableViewPager.getVisibility() == GONE
                        || lvResults.getVisibility() == View.GONE)
                {
                    if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                        heightAdjustableViewPager.setVisibility(View.VISIBLE);
                    else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                        alResults.setVisibility(View.VISIBLE);
                    else
                        lvResults.setVisibility(View.VISIBLE);

                    ibResults.setVisibility(GONE);
                    ibResults2.setVisibility(View.VISIBLE);
                }
            }
        });

        ibResults2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alResults.getVisibility() == View.VISIBLE || heightAdjustableViewPager.getVisibility() == View.VISIBLE
                        || lvResults.getVisibility() == View.VISIBLE)
                {
                    if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_CAROUSEL))
                        heightAdjustableViewPager.setVisibility(GONE);
                    else if(appearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                        alResults.setVisibility(GONE);
                    else
                        lvResults.setVisibility(GONE);

                    ibResults2.setVisibility(GONE);
                    ibResults.setVisibility(View.VISIBLE);
                }
            }
        });

        llFaq.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                llAutoSuggestions.setVisibility(GONE);
                return false;
            }
        });

        llAllresults.addView(llFaq);
    }

    public void onEvent(RestResponse.JWTTokenResponse jwtTokenResponse)
    {
        if(searchInterfaceModel == null)
        {
            getSearchInterface(jwtTokenResponse.getJwt());
        }
    }

    public void getResultViewSettings()
    {
        Call<ResultsViewModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getResultViewSettings(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(),"bearer "+SocketWrapper.getInstance(getActivity()).getAccessToken(), SocketWrapper.getInstance(getActivity()).getJWTToken());
        getJWTTokenService.enqueue(new Callback<ResultsViewModel>() {
            @Override
            public void onResponse(Call<ResultsViewModel> call, Response<ResultsViewModel> response) {
                if (response.isSuccessful())
                {
                    resultsViewModel = response.body();

                    if(resultsViewModel != null && resultsViewModel.getSettings() != null)
                    {
                        for(int i = 0; i < resultsViewModel.getSettings().size(); i++)
                        {
                            if(resultsViewModel.getSettings().get(i).getMyInterface().equalsIgnoreCase(BundleConstants.LIVE_SEARCH))
                            {
                                liveSearchSetting = resultsViewModel.getSettings().get(i);

                                if(liveSearchSetting != null && liveSearchSetting.getGroupSetting() != null
                                        && liveSearchSetting.getGroupSetting().getConditions() != null
                                        && liveSearchSetting.getGroupSetting().getConditions().size() > 0)
                                {
                                    for(int k = 0; k < liveSearchSetting.getGroupSetting().getConditions().size(); k++)
                                    {
                                        if(liveSearchSetting.getGroupSetting().getConditions().get(k).getFieldValue().equalsIgnoreCase(BundleConstants.FAQ))
                                            faq = liveSearchSetting.getGroupSetting().getConditions().get(k);
                                        else if(liveSearchSetting.getGroupSetting().getConditions().get(k).getFieldValue().equalsIgnoreCase(BundleConstants.PAGE))
                                            page = liveSearchSetting.getGroupSetting().getConditions().get(k);
                                        else if(liveSearchSetting.getGroupSetting().getConditions().get(k).getFieldValue().equalsIgnoreCase(BundleConstants.FILE))
                                            document = liveSearchSetting.getGroupSetting().getConditions().get(k);
                                        else if(liveSearchSetting.getGroupSetting().getConditions().get(k).getFieldValue().equalsIgnoreCase(BundleConstants.TASK))
                                        {
                                            task = liveSearchSetting.getGroupSetting().getConditions().get(k);
                                        }
                                        else if(liveSearchSetting.getGroupSetting().getConditions().get(k).getFieldValue().equalsIgnoreCase(BundleConstants.DATA))
                                            data = liveSearchSetting.getGroupSetting().getConditions().get(k);
                                    }

                                    if(liveSearchSetting.getDefaultTemplate() != null)
                                        defaultTemplate = liveSearchSetting.getDefaultTemplate();

                                    if(faq == null)
                                        faq = getDefaultApperence(BundleConstants.FAQ);

                                    if(page == null)
                                        page = getDefaultApperence(BundleConstants.WEB);

                                    if(document == null)
                                        document = getDefaultApperence(BundleConstants.FILE);

                                    if(data == null)
                                        data = getDefaultApperence(BundleConstants.DATA);

                                    if(task == null)
                                        task = getDefaultApperence(BundleConstants.TASK);

                                    if(default_group == null)
                                        default_group = getDefaultApperence(BundleConstants.FAQ);
                                }
                            }
                            else if(resultsViewModel.getSettings().get(i).getMyInterface().equalsIgnoreCase(BundleConstants.CONVERSATIONAL_SEARCH))
                            {
                                searchSetting = resultsViewModel.getSettings().get(i);
                            }
                            else if(resultsViewModel.getSettings().get(i).getMyInterface().equalsIgnoreCase(BundleConstants.FULL_SEARCH))
                            {
                                fullSearchSetting = resultsViewModel.getSettings().get(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultsViewModel> call, Throwable t) {
                Log.e("Live Search Data", t.toString());
            }
        });
    }

    public void getAutoSuggestions(final String query)
    {
        JsonObject jsonObject = getJsonBody(query, false, 2, 3);
        Call<AutoSuggestionsModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getAutoSuggestions(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(getActivity()).getAccessToken(), jsonObject, SocketWrapper.getInstance(getActivity()).getJWTToken());
        getJWTTokenService.enqueue(new Callback<AutoSuggestionsModel>() {
            @Override
            public void onResponse(Call<AutoSuggestionsModel> call, Response<AutoSuggestionsModel> response) {
                if (response.isSuccessful())
                {
                    autoSuggestionsModel = response.body();

                    if(autoSuggestionsModel != null)
                    {
//                        getLiveSearch(query);

                        if(autoSuggestionsModel.getAutoComplete() != null && autoSuggestionsModel.getAutoComplete().getQuerySuggestions() != null &&
                                autoSuggestionsModel.getAutoComplete().getQuerySuggestions().size() > 0 )
                        {
                            llAutoSuggestions.setVisibility(View.VISIBLE);
                            lvAutoSuggestions.setAdapter(new AutoSuggestionsAdapter(context, autoSuggestionsModel.getAutoComplete().getQuerySuggestions()));


                            lvAutoSuggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    llPopularSearch.setVisibility(GONE);
                                    perssiatentPanel.setVisibility(View.VISIBLE);
                                    edtTxtMessage.setText("");
                                    llAutoSuggestions.setVisibility(GONE);
                                    sendMessageText(autoSuggestionsModel.getAutoComplete().getQuerySuggestions().get(i));
                                }
                            });
                        }
                        else
                            llAutoSuggestions.setVisibility(GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AutoSuggestionsModel> call, Throwable t) {
                Log.e("Live Search Data", t.toString());
            }
        });
    }

    public void getSearchInterface(String jwt)
    {
        Call<SearchInterfaceModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getSearchInterface(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(),jwt);
        getJWTTokenService.enqueue(new Callback<SearchInterfaceModel>()
        {
            @Override
            public void onResponse(Call<SearchInterfaceModel> call, Response<SearchInterfaceModel> response) {
                if (response.isSuccessful())
                {
                    searchInterfaceModel = response.body();

                    if(searchInterfaceModel != null)
                    {
                        applySearchBoxStyles(searchInterfaceModel);
                        saveSearchInterfaceInPref(context, searchInterfaceModel);

                        BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithConfig(context,null);
                        BotSocketConnectionManager.getInstance().setChatListener(sListener);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchInterfaceModel> call, Throwable t) {
                Log.e("Live Search Data", t.toString());
            }
        });
    }

    public void getTabFacets()
    {
        Call<TabFacetsModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getTabFacets(SDKConfiguration.Client.bot_id, SDKConfiguration.getSDIX(), "bearer "+SocketWrapper.getInstance(getActivity()).getAccessToken(), SocketWrapper.getInstance(getActivity()).getJWTToken());
        getJWTTokenService.enqueue(new Callback<TabFacetsModel>() {
            @Override
            public void onResponse(Call<TabFacetsModel> call, Response<TabFacetsModel> response) {
                if (response.isSuccessful())
                {
                    tabFacetsModel = response.body();

                    if(tabFacetsModel != null)
                    {
                        sharedPreferences.edit().putString(BundleConstants.TAB_FACETS, gson.toJson(tabFacetsModel)).commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<TabFacetsModel> call, Throwable t) {
                Log.e("Live Search Data", t.toString());
            }
        });
    }

    public boolean validate()
    {
        if(edtUserId.getText().toString().equalsIgnoreCase("John") && edtPassword.getText().toString().equalsIgnoreCase("1111"))
        {
            return true;
        }
        else
            return false;
    }

    private ArrayList<LiveSearchResultsModel> getTopFourList(LiveSearchResultsOuterModel results)
    {
        arrTempResults = new ArrayList<>();
        if(results != null)
        {
            if(results.getFaq() != null && results.getFaq().getData() != null
                    && results.getFaq().getData().size() > 0)
            {
                for (int i = 0; i < results.getFaq().getData().size(); i++)
                {
                    arrTempResults.add(results.getFaq().getData().get(i));
                    if(arrTempResults.size() == 2)
                        break;
                }
            }

            if(results.getWeb() != null && results.getWeb().getData() != null
                    && results.getWeb().getData().size() > 0)
            {
                if(arrTempResults.size() >= 1)
                {
                    int suntoAdd = arrTempResults.size()+2;
                    for (int i = 0; i < results.getWeb().getData().size(); i++)
                    {
                        arrTempResults.add(results.getWeb().getData().get(i));
                        if(arrTempResults.size() == suntoAdd)
                            break;
                    }
                }
                else
                {
                    for (int i = 0; i < results.getWeb().getData().size(); i++)
                    {
                        arrTempResults.add(results.getWeb().getData().get(i));
                        if(arrTempResults.size() == 2)
                            break;
                    }
                }
            }
        }

        if(results.getFile() != null && results.getFile().getData() != null
                && results.getFile().getData().size() > 0)
        {
            if(arrTempResults.size() >= 1)
            {
                int suntoAdd = arrTempResults.size()+2;
                for (int i = 0; i < results.getFile().getData().size(); i++)
                {
                    arrTempResults.add(results.getFile().getData().get(i));
                    if(arrTempResults.size() == suntoAdd)
                        break;
                }
            }
            else
            {
                for (int i = 0; i < results.getFile().getData().size(); i++)
                {
                    arrTempResults.add(results.getFile().getData().get(i));
                    if(arrTempResults.size() == 2)
                        break;
                }
            }
        }

        return arrTempResults;
    }

    private ResultsViewAppearance getDefaultApperence(String type)
    {
        ResultsViewAppearance resultsViewAppearance = new ResultsViewAppearance();
        resultsViewAppearance.setOp("equals");
        resultsViewAppearance.setFieldValue(type);

        if(defaultTemplate != null)
        {
            resultsViewAppearance.setTemplateId(defaultTemplate.get_id());
            resultsViewAppearance.setTemplate(defaultTemplate);
            return resultsViewAppearance;
        }

        return null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view instanceof EditText && !lockLiveSearch)
        {
            edtTxtMessage.setCursorVisible(true);
            cordinate_layout.setVisibility(View.VISIBLE);
            perssiatentPanel.setVisibility(View.VISIBLE);
            llPopularSearch.setVisibility(View.VISIBLE);// User touched edittext
            llPlrSearch.setVisibility(GONE);
            llAutoSuggestions.setVisibility(GONE);
            getRecentSearches();

//            if(popularSearchResultsModel != null && popularSearchResultsModel.getResult() != null && popularSearchResultsModel.getResult().size() > 0)
//                llPlrSearch.setVisibility(View.VISIBLE);

            isEditTouched = true;
            llWelcomeMessage.setVisibility(GONE);
        }
        return false;
    }

    private JsonObject getJsonBody(String query, boolean smallTalk, int from, int maxResults)
    {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        if(from == 1)
            jsonObject.addProperty("isBotAction", true);

        jsonObject.addProperty("query", query.toLowerCase());
        jsonObject.addProperty("maxNumOfResults", maxResults);
        jsonObject.addProperty("userId", SocketWrapper.getInstance(getActivity()).getBotUserId());
        jsonObject.addProperty("streamId", "st-a4a4fabe-11d3-56cc-801d-894ddcd26c51");
        jsonObject.addProperty("lang", "en");

        if(smallTalk)
            jsonObject.addProperty("smallTalk", true);

        if(from == 2)
            jsonObject.addProperty("isDev", false);

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    private JsonObject getUnlockBody()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("client", "sdk");
        jsonObject.addProperty("userId", SocketWrapper.getInstance(getActivity()).getBotUserId());
        jsonObject.addProperty("streamId", SDKConfiguration.getSDIX());
        jsonObject.addProperty("lang", "en");

        JsonObject botInfo = new JsonObject();
        botInfo.addProperty("chatBot", SDKConfiguration.Client.bot_name);
        botInfo.addProperty("taskBotId", SDKConfiguration.Client.bot_id);
        jsonObject.add("botInfo", botInfo);

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    @Override
    public void invokeGenericWebView(String url)
    {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(context, GenericWebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("header", getResources().getString(R.string.app_name));
            startActivity(intent);
        }
    }

    @Override
    public void handleUserActions(String payload, HashMap<String, Object> type) {

    }

    public void setBotContentFragmentUpdate(BotContentFragmentUpdate botContentFragmentUpdate) {
        this.botContentFragmentUpdate = botContentFragmentUpdate;
    }

    @Override
    public void onSendClick(String message, boolean isFromUtterance) {
//        sendMessage(message);
        BotSocketConnectionManager.getInstance().sendMessage("", message);
    }

    @Override
    public void onSendClick(String message, String payload, boolean isFromUtterance) {
        if(payload != null)
        {
            BotSocketConnectionManager.getInstance().sendPayload(message, payload);
        }
        else
        {
            BotSocketConnectionManager.getInstance().sendMessage(message, payload);
        }

        cordinate_layout.setVisibility(View.VISIBLE);
        llPlrSearch.setVisibility(GONE);
        llWelcomeMessage.setVisibility(GONE);
        llPopularSearch.setVisibility(GONE);
        perssiatentPanel.setVisibility(View.VISIBLE);
        llAutoSuggestions.setVisibility(GONE);
        edtTxtMessage.setText("");
    }

    @Override
    public void onFormActionButtonClicked(FormActionTemplate fTemplate) {

    }

    @Override
    public void launchActivityWithBundle(String type, Bundle payload) {

    }

    @Override
    public void sendWithSomeDelay(String message, String payload, long time, boolean isScrollUpNeeded) {

    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {

    }

    @Override
    public void showMentionNarratorContainer(boolean show, String natxt, String cotext, String handFocus, boolean isEnd, boolean showOverlay, String templateType) {

    }

    @Override
    public void openFullView(String templateType, String data, CalEventsTemplateModel.Duration duration, int position) {

    }

    @Override
    public void updateActionbar(boolean selected, String templateType, ArrayList<BotButtonModel> buttonModels) {

    }

    @Override
    public void lauchMeetingNotesAction(Context context, String mid, String eid) {

    }

    @Override
    public void showAfterOnboard(boolean isDiscardClicked) {

    }

    @Override
    public void onPanelClicked(Object pModel, boolean isFirstLaunch) {

    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }

    private ArrayList<LiveSearchResultsModel> getTopList(ArrayList<LiveSearchResultsModel> results, String type)
    {
        ArrayList<LiveSearchResultsModel> arrTempResults = new ArrayList<>();

        if(sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int maxValue = 0;

        if(results != null && results.size() > 0)
        {
            if(results.size() > sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2))
                maxValue = sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2);
            else
                maxValue = results.size();

            for (int i = 0; i < maxValue; i++)
            {
                if (type.equalsIgnoreCase(BundleConstants.FAQ))
                    results.get(i).setAppearance(faq);
                else if(type.equalsIgnoreCase(BundleConstants.WEB))
                    results.get(i).setAppearance(page);
                else if(type.equalsIgnoreCase(BundleConstants.TASK))
                    results.get(i).setAppearance(task);
                else if(type.equalsIgnoreCase(BundleConstants.FILE))
                    results.get(i).setAppearance(document);
                else if(type.equalsIgnoreCase(BundleConstants.DATA))
                    results.get(i).setAppearance(data);
                else
                    results.get(i).setAppearance(default_group);

                arrTempResults.add(results.get(i));
            }
        }

        return arrTempResults;
    }

    private ArrayList<HashMap<String, Object>> getResultsTopList(ArrayList<HashMap<String, Object>> results, String type)
    {
        ArrayList<HashMap<String, Object>> arrTempResults = new ArrayList<>();

        if(sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int maxValue = 0;

        if(results != null && results.size() > 0)
        {
            if(results.size() > sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2))
                maxValue = sharedPreferences.getInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, 2);
            else
                maxValue = results.size();

            for (int i = 0; i < maxValue; i++)
            {
                if (type.equalsIgnoreCase(BundleConstants.FAQ))
                    results.get(i).put("appearance", faq);
                else if(type.equalsIgnoreCase(BundleConstants.WEB))
                    results.get(i).put("appearance",page);
                else if(type.equalsIgnoreCase(BundleConstants.TASK))
                {
                    if(task == null)
                        task = getResultsViewAppearance(BundleConstants.TASK);

                    results.get(i).put("appearance",task);
                }
                else if(type.equalsIgnoreCase(BundleConstants.FILE))
                    results.get(i).put("appearance",document);
                else if(type.equalsIgnoreCase(BundleConstants.DATA))
                    results.get(i).put("appearance",data);

                arrTempResults.add(results.get(i));
            }
        }

        return arrTempResults;
    }


    private void sendMessageText(String message)
    {
        BotSocketConnectionManager.getInstance().sendMessage(message, null);
    }

    public void onMessage(SocketDataTransferModel data) {
        if (data == null) return;
        if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE)) {
            processPayload(data.getPayLoad(), null);

        } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE)) {
            if (botContentFragment != null) {
                botContentFragment.updateContentListOnSend(data.getBotRequest());
            }
        }
    }

    private void processPayload(String payload, BotResponse botLocalResponse) {
        try {
            final BotResponse botResponse = botLocalResponse != null ? botLocalResponse : gson.fromJson(payload, BotResponse.class);
            if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                return;
            }

            Log.d(LOG_TAG, payload);
            boolean resolved = true;
            PayloadOuter payOuter = null;
//            PayloadInner payInner = null;
            if (!botResponse.getMessage().isEmpty()) {
                ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
                if (compModel != null) {
                    payOuter = compModel.getPayload();
                    if (payOuter != null) {
                        if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                            Gson gson = new Gson();
                            payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                        }
                    }
                }
            }
            payOuter.setType("template");
            final PayloadInner payloadInner = payOuter == null ? null : getPayLoadInner(payOuter);
            botContentFragment.showTypingStatus(botResponse);
            if (payloadInner != null)
            {
                payloadInner.convertElementToAppropriate();

                if(payloadInner.getTemplate_type().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_RESULTS_LIST))
                {
                    payloadInner.setResultsViewSetting(searchSetting);
                    payloadInner.setFullSearchresultsViewSetting(fullSearchSetting);
                }
            }

            if (resolved) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        botContentFragment.addMessageToBotChatAdapter(botResponse);
                        botContentFragment.setQuickRepliesIntoFooter(botResponse);
                        botContentFragment.showCalendarIntoFooter(botResponse);
                    }
                }, BundleConstants.TYPING_STATUS_TIME);
            }
        } catch (Exception e) {
            /*Toast.makeText(getApplicationContext(), "Invalid JSON", Toast.LENGTH_SHORT).show();*/
            e.printStackTrace();
            if (e instanceof JsonSyntaxException) {
                try {
                    //This is the case Bot returning user sent message from another channel
                    if (botContentFragment != null) {
                        BotRequest botRequest = gson.fromJson(payload, BotRequest.class);
                        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
                        botContentFragment.updateContentListOnSend(botRequest);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public ArrayList<String> getStringArrayPref(Context context, String key) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private void applySearchBoxStyles(SearchInterfaceModel searchInterfaceModel)
    {
        WidgetConfigModel widgetConfigModel = searchInterfaceModel.getWidgetConfig();
        final InteractionsConfigModel interactionsConfigModel = searchInterfaceModel.getInteractionsConfig();

        if(widgetConfigModel != null)
        {
            ((GradientDrawable) llComposeBar.getBackground()).setColor(Color.parseColor(widgetConfigModel.getSearchBarFillColor().substring(0, 7)));
            ((GradientDrawable) llComposeBar.getBackground()).setStroke((int)(2 * dp1) , Color.parseColor(widgetConfigModel.getSearchBarBorderColor().substring(0, 7)));

            if(!StringUtils.isNullOrEmpty(widgetConfigModel.getSearchBarIcon()))
            {
                Picasso.get().load(widgetConfigModel.getSearchBarIcon()).transform(roundedCornersTransform).into(ivChatIcon);
                Picasso.get().load(widgetConfigModel.getSearchBarIcon()).transform(roundedCornersTransform).into(ivWelcomeImage);
            }


            edtTxtMessage.setHint(widgetConfigModel.getSearchBarPlaceholderText());
            edtTxtMessage.setHintTextColor(Color.parseColor(widgetConfigModel.getSearchBarPlaceholderTextColor()));

            if(!StringUtils.isNullOrEmpty(widgetConfigModel.getButtonText()))
                sendTv.setText(widgetConfigModel.getButtonText());

            if(!StringUtils.isNullOrEmpty(widgetConfigModel.getButtonTextColor()))
                sendTv.setTextColor(Color.parseColor(widgetConfigModel.getButtonTextColor()));

            if(!StringUtils.isNullOrEmpty(widgetConfigModel.getButtonBorderColor()) &&
                    !StringUtils.isNullOrEmpty(widgetConfigModel.getButtonFillColor()))
            {
                ((GradientDrawable) sendTv.getBackground()).setColor(Color.parseColor(widgetConfigModel.getButtonFillColor()));
                ((GradientDrawable) sendTv.getBackground()).setStroke((int)(2 * dp1) , Color.parseColor(widgetConfigModel.getButtonBorderColor()));
            }

            if(!StringUtils.isNullOrEmpty(widgetConfigModel.getButtonText()))
                sendTvOutside.setText(widgetConfigModel.getButtonText());

            if(!StringUtils.isNullOrEmpty(widgetConfigModel.getButtonTextColor()))
                sendTvOutside.setTextColor(Color.parseColor(widgetConfigModel.getButtonTextColor()));

            if(!StringUtils.isNullOrEmpty(widgetConfigModel.getButtonBorderColor()) &&
                    !StringUtils.isNullOrEmpty(widgetConfigModel.getButtonFillColor()))
            {
                ((GradientDrawable) sendTvOutside.getBackground()).setColor(Color.parseColor(widgetConfigModel.getButtonFillColor()));
                ((GradientDrawable) sendTvOutside.getBackground()).setStroke((int)(2 * dp1) , Color.parseColor(widgetConfigModel.getButtonBorderColor()));
            }

            sendTv.setVisibility(View.VISIBLE);

            if(!StringUtils.isNullOrEmpty(widgetConfigModel.getButtonPlacementPosition()))
            {
                if(widgetConfigModel.getButtonPlacementPosition().equalsIgnoreCase(BundleConstants.POSITION_OUTSIDE))
                {
                    sendTvOutside.setVisibility(View.VISIBLE);
                    sendTv.setVisibility(GONE);
                }
            }

        }

        if(interactionsConfigModel != null)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!isEditTouched)
                    {
                        tvWelcomeMsg.setText(interactionsConfigModel.getWelcomeMsg());
                        tvWelcomeMsg.setTextColor(Color.parseColor(interactionsConfigModel.getWelcomeMsgColor()));

//                        if(!StringUtils.isNullOrEmpty(interactionsConfigModel.getWelcomeMsgEmoji()))
//                            Picasso.get().load(interactionsConfigModel.getWelcomeMsgEmoji()).transform(roundedCornersTransform).into(ivWelcomeImage);

                        llWelcomeMessage.setVisibility(View.VISIBLE);
                    }
                }
            }, 2000);
        }

    }

    public void saveSearchInterfaceInPref(Context context, SearchInterfaceModel searchInterfaceModel)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(searchInterfaceModel);
        editor.putString(BundleConstants.SEARCH_INTERFACE, json);
        editor.putInt(BundleConstants.LIVE_SEARCH_RESULTS_LIMIT, searchInterfaceModel.getInteractionsConfig().getLiveSearchResultsLimit());
        editor.commit();
    }

    public boolean getLogin(Context context, String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isLogin = prefs.getBoolean(key, false);

        return isLogin;
    }

    public void setLogin(Context context, String key, boolean isLogin)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, isLogin);
        editor.commit();
    }

    public void onEvent(SocketDataTransferModel data) {
        if (data == null) return;
        if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE)) {
            processPayload(data.getPayLoad(), null);
        } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE)) {
            if (botContentFragment != null) {
                botContentFragment.updateContentListOnSend(data.getBotRequest());
            }
        }
    }

    private PayloadInner getPayLoadInner(PayloadOuter payloadOuter)
    {
        if(payloadOuter != null && payloadOuter.getTemplate() != null)
        {
            if(payloadOuter.getTemplate().getResults() != null)
            {
                arrTempAllResults = new ArrayList<>();

                if(payloadOuter.getTemplate().getResults().getFaq() != null &&
                        payloadOuter.getTemplate().getResults().getFaq().getData() != null &&
                        payloadOuter.getTemplate().getResults().getFaq().getData().size() > 0)
                    arrTempAllResults.addAll(payloadOuter.getTemplate().getResults().getFaq().getData());

                if(payloadOuter.getTemplate().getResults().getWeb() != null &&
                        payloadOuter.getTemplate().getResults().getWeb().getData() != null &&
                        payloadOuter.getTemplate().getResults().getWeb().getData().size() > 0)
                    arrTempAllResults.addAll(payloadOuter.getTemplate().getResults().getWeb().getData());

                if(payloadOuter.getTemplate().getResults().getTask() != null &&
                        payloadOuter.getTemplate().getResults().getTask().getData() != null &&
                        payloadOuter.getTemplate().getResults().getTask().getData().size() > 0)
                    arrTempAllResults.addAll(payloadOuter.getTemplate().getResults().getTask().getData());

                if(payloadOuter.getTemplate().getResults().getFile() != null &&
                        payloadOuter.getTemplate().getResults().getFile().getData() != null &&
                        payloadOuter.getTemplate().getResults().getFile().getData().size() > 0)
                    arrTempAllResults.addAll(payloadOuter.getTemplate().getResults().getFile().getData());

                if(payloadOuter.getTemplate().getResults().getData() != null &&
                        payloadOuter.getTemplate().getResults().getData().size() > 0)
                    arrTempAllResults.addAll(payloadOuter.getTemplate().getResults().getData());

                payloadOuter.setPayload(new PayloadInner());
                payloadOuter.getPayload().setTemplate_type(payloadOuter.getTemplateType());
                payloadOuter.getPayload().setElements(arrTempAllResults);
                payloadOuter.getPayload().setTitle(payloadOuter.getPayload().getTitle());
                payloadOuter.getPayload().setText("Sure, please find the matched results below");
                payloadOuter.getPayload().setTemplate(payloadOuter.getTemplate());
            }
        }

        return payloadOuter.getPayload();
    }

    public void onEvent(BaseSocketConnectionManager.CONNECTION_STATE states) {
    }

    public void onEvent(BotResponse botResponse) {
        processPayload("", botResponse);
    }

    private ResultsViewAppearance getResultsViewAppearance(String type)
    {
        ResultsViewAppearance resultsViewAppearance = new ResultsViewAppearance();
//        resultsViewAppearance.setType(type);
        resultsViewAppearance.setTemplateId("fsrt-c86f6f10-76b1-5161-88c3-b1d03b1c0de7");

        ResultsViewTemplate template = new ResultsViewTemplate();
        template.set_id("fsrt-c86f6f10-76b1-5161-88c3-b1d03b1c0de7");
//        template.setAppearanceType(type);
        template.setCreatedOn("2021-05-31T07:25:44.364Z");
        template.setStreamId("st-12466d46-dc91-5855-a301-b6ecd7a95d82");
        template.setSearchIndexId("sidx-4c33c7cf-9561-58f2-b547-4745ce12b513");
        template.setIndexPipelineId("fip-f3cef85c-efd0-5e1e-bdb0-4c5b52b288e7");
        template.setlModifiedOn("2021-05-31T07:25:44.364Z");
        template.setType("listTemplate1");

        ResultsViewlayout resultsViewlayout = new ResultsViewlayout();
        resultsViewlayout.setLayoutType("tileWithHeader");
        resultsViewlayout.setIsClickable(true);
        resultsViewlayout.setBehaviour("webpage");
        resultsViewlayout.setTextAlignment("left");
        template.setLayout(resultsViewlayout);

        ResultsViewMapping resultsViewMapping = new ResultsViewMapping();
        resultsViewMapping.setDescription("filePreview");
        resultsViewMapping.setHeading("fileTitle");
        resultsViewMapping.setUrl("fileUrl");
        template.setMapping(resultsViewMapping);

        resultsViewAppearance.setTemplate(template);

        return resultsViewAppearance;
    }

    public void showCustomDataDialog(final Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_data);

        final EditText edtCustomData = (EditText) dialog.findViewById(R.id.edtCustomData);
        TextView tvClear = (TextView) dialog.findViewById(R.id.tvClear);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);

        if(sharedPreferences != null)
        {
            edtCustomData.setText(sharedPreferences.getString(BundleConstants.CUSTOM_DATA, ""));
        }

        tvClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if(sharedPreferences == null)
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(BundleConstants.CUSTOM_DATA, "");
                    editor.commit();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sharedPreferences == null)
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                try
                {
                    JSONObject jsonObject = new JSONObject(edtCustomData.getText().toString());
                    editor.putString(BundleConstants.CUSTOM_DATA, jsonObject.toString());
                    editor.commit();
                }
                catch (Exception e)
                {
                    editor.putString(BundleConstants.CUSTOM_DATA, "");
                    editor.commit();

                    e.printStackTrace();
                }


                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
            outRect.right = verticalSpaceHeight;
        }
    }
}
