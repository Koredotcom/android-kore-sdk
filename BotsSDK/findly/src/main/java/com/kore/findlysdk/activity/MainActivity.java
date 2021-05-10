package com.kore.findlysdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kore.findlysdk.R;
import com.kore.findlysdk.adapters.ChatAdapterOld;
import com.kore.findlysdk.adapters.ChatAutoListviewAdapter;
import com.kore.findlysdk.adapters.LiveSearchCyclerAdapter;
import com.kore.findlysdk.adapters.PopularSearchAdapter;
import com.kore.findlysdk.adapters.RecentlySearchAdapter;
import com.kore.findlysdk.events.KoreEventCenter;
import com.kore.findlysdk.events.SocketDataTransferModel;
import com.kore.findlysdk.fragments.BotContentFindlyFragment;
import com.kore.findlysdk.listners.BaseSocketConnectionManager;
import com.kore.findlysdk.listners.BotContentFragmentUpdate;
import com.kore.findlysdk.listners.BotSocketConnectionManager;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.listners.SocketChatListener;
import com.kore.findlysdk.models.BotButtonModel;
import com.kore.findlysdk.models.BotInfoModel;
import com.kore.findlysdk.models.BotRequest;
import com.kore.findlysdk.models.BotResponse;
import com.kore.findlysdk.models.BotResponseMessage;
import com.kore.findlysdk.models.CalEventsTemplateModel;
import com.kore.findlysdk.models.ComponentModel;
import com.kore.findlysdk.models.FormActionTemplate;
import com.kore.findlysdk.models.KnowledgeCollectionModel;
import com.kore.findlysdk.models.LiveSearchModel;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.LiveSearchResultsOuterModel;
import com.kore.findlysdk.models.PayloadInner;
import com.kore.findlysdk.models.PayloadOuter;
import com.kore.findlysdk.models.PopularSearchModel;
import com.kore.findlysdk.models.RestResponse;
import com.kore.findlysdk.models.ResultsViewAppearance;
import com.kore.findlysdk.models.ResultsViewModel;
import com.kore.findlysdk.models.ResultsViewSetting;
import com.kore.findlysdk.models.SearchModel;
import com.kore.findlysdk.net.BotRestBuilder;
import com.kore.findlysdk.net.SDKConfiguration;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.DateUtils;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.view.AutoExpandListView;
import com.kore.findlysdk.websocket.SocketWrapper;
import com.kore.findlysdk.widgetviews.CustomBottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MainActivity extends BotAppCompactActivity implements View.OnTouchListener, InvokeGenericWebViewInterface, ComposeFooterInterface
{
    private LinearLayout perssiatentPanel, llComposeBar, panel_root, llPlrSearch, llRecentlyAsked, llLogin;
    private RelativeLayout llLiveSearch;
    private LinearLayout llPopularSearch, llAllresults;
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
    private AutoExpandListView lvPopularSearch, lvRecentlyAsked;
    private RecyclerView lvLiveSearch;
    private EditText edtTxtMessage;
    private LiveSearchModel liveSearchModel;
    private ResultsViewModel resultsViewModel;
    private SearchModel searchModel;
    private ChatAdapterOld chatAdapterOld;
    private ChatAutoListviewAdapter chatAutoListviewAdapter;
    ArrayList<SearchModel> arrLiveSearchModels = new ArrayList<>();
    private WebView wvLoadUrl;
    private ArrayList<PopularSearchModel> arrPopularSearchModels;
    private String chatIcon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAbCAYAAABiFp9rAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAW0SURBVHgBrVY7bxxVFD73MbOzu36skzgRDVoLISUFitPQQkoqNl3KICQKGuAXJP4FpESisJEQosNUlCy/gICQCAXyFkgkIDvr7HrncV9858zaROSBkJjR1c7MnTnf+b7znXtX0b8cBx8+GmgXb0VqrpJS22RoYIymGOIkEU1Dnb42ROOtT16avCiOet7Ezzd/HxpFu42fvxlcRREnpUhJedLaku11qeivkjEZ+QVmXdqLgXaufPlswGcC3R89+MAFd7epKgqxIc9A/Bs9qZQoUcBQZHRO1ha0fmmTut0uuYWjPLN3tr7Y3Hkh0MHoYBBpdfdkVo6cc9RglG5GIdSQqkZ4MGJmpHEqKGlJKw3Agnpgd+Glc6SjBnPa18G8s7W/MT2NbZ8EMn6wW5XlqJo1VNYlNdFRHUqw8eRTQyHFNjsFkAQwpGl0B79Iqn5MzczRhQvnqNfrjpROA7x6/SlGv7z+8Ha2Zu8cHR3TvCmpChXVkKuJbX1YQmaTkqIkwikAWMoUAylGx3VOPdOh86sDWl3pU57buy9/s/HRGdD3lw+GxVr3IKRAhydTKhG8Dg1VqUbgBDYQDYNP3AoQCRTAYEOrNUDs8l7RuXydNjrrtLayQtqa61vjjbFIZ6zdtdHQ4WIKJg6jJpc82LTBA+L6xBYgAEaplBdTtMw6KaOeSlTAHE1yNId5MpyFKqhj9W18NlbMJs86Bx5h/qyPqEQtHrNsAHIylozYawLCVgCgZA8xMZ/B7myPFZ2JYdZ1ly5ma3TerlMBKXOdX7PaZCOmP/czSBXomBmBQRMTsiP50KVWrIDgHmD61ElsCrgOpm8lZbMkfk4Si991UAXtPbIdnb/dgMUMDpvi4RwvLiLWAbzEUnHx+VW/dJzHPcvn5Wlr88IouXIxSu005h8j1iUkwiqkFN6wVunhoWeQREcYFV4QIPySvAg20N/jmV5+6ERC5tdWqQqoE+asMq2VIxfV0TBPhByY5NAih+EfCPooMAuFGhmqkO8iJXGaGDO1NYkxSmCWkpRuq4Rrh0Qa9FpHG5FVpTYhLgFDd0kNrcPDYwSYYaLERzw5B3BFrd7sNa5Da2sGYatzVyQwlIfiymbpSGYVpTYksbipV7meRtnpSUyDCh/XCBQQFK+L4+Kpt1KShgtLi6fTrCX/tOyoJPVywp5kaSo5CdwMjJraQptJrrPt2pcinZcAFuIZsTSJoxK13QP5lsFp2bIkdeElqTUPioOaGupilZhhmbL4di3Gexbl/G5gettJleI0z82ILmdmtWisBCBRuyIwHCun0pIJKyDXWlLQkmCO5x0wklQ4zg+68rSfIYMCKzB2GsjX9gUho4CPGMxh3glLK2zF7qq9r5OW7yQ5BlEZ9qsCbuvA4knkc1Ht6ys/rY7XTGesEZhfCFg6OChnFakjQaRRkXkAIEvqBRR9w614ep94Hous7sMQBVhndAiToSyTt+5vjqXJN222s6KZbg4mWEbOBkvITDMxCCeRZJlBYIA23ENSFdQOSeZ6RQbHmQHEyeaoZRMUoNfudceX8/7YChAkw2iWMjmuFT5okDlL6NFnYSklJxORTAKIxvqmVA/PDS0Qs+RtQxd7792/uHcGxMelrH/jWmdj0jfdVj7FIJmwaXDNmZ/ALHOYYQHdK7aygPUwupCqkPouMF8DLFe9SRHynac2Pj6+ulwOHxJ9+2MzH07CHEVsELSW36XRKZ7uRKrdfTTYWcWbAvyG6x5M0Edd8pSu703+/qPy1J+TOwDTSX38m29GD7CNzwUI2wVOn/xZeypZgqjd+hQzyGAfTX1lxvDajf3J1vTJuM/9u/X+q/Nbx0HdnpIfltyMGA3AgmyG0k2yEvAKwADndT65qMPOp7+2Nfnn8Vyg0+PdV05GD2IawRBXsZFvu+WqAP+BRZp0yYxXYvrs88nqmP7P49awHN7EoP94/AUUr0KuNdomKAAAAABJRU5ErkJggg==";
    private String welconicon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAJmSURBVHgBtVJLS5RhFH7O+10cZ2jmM5sgUfg0BAkq3VqhRrhx0biQ2ql7w7atnF2rsD8Q02XZQluGhhVBdIHGS4Q7JSOi0hk/57t/7+l1umCouzqr9+Wc5znP83CA/10rKyvmzcfexcFbbttBfdr78V7adiqFs+r5DHpjCazfm88sLTx4sd2ba9TFBaTnRvLt6VrgT2uJX2wcqqyLvQSGoXWzSM/CtCYhMjbM/MyluK/QdST7+sqZ9POeEVCU0Qp6Sh9L0FDYxYi925UeizTzNoy2IvT8GkR2DfqJuzdOD02c7yKnkyjQTbqspXTWGrTuvwh2twsjVQI0C5BPYeQLysYayNwCZYu8OjhV96wJW+gCrInufRnw8qkijJYpkFGGblnQsrYiK3Pw2SYEOSTeIyTfCzIMOHCiarr/Y9MfAn57chSGfh3G0UXox0chjAq0TC5xqyRMDYh3QOyoQY9l4FNY3Wa4sr1ugZlFJJPFenAk+iCrs+DISpwNEoYaSDzI0FV61T49T2Q0MyeC/DDsEfMfuPn+E6dpYvXNF8T+ACQssCxId2NLaDFIOpDepiKpMWSkWgTJJslEpcXIiaXlDXduHeeqntn68Nj71ST8Oo54Uw37yl9NAatKlKfk7yj8jhLmIXbVW+WQEK3XM7h6p9bS20HiWn/jJyJiftc5xoJKP0/NrEvn0EfkBsqvjsjxkYR+2Rre7CEcUtGrjqIimyLzKJga1PYAcVW5dF2WsVxUEgeahiuVQwl2y11onSQ2pmHmiJXn2PmGJHLL+AXedwcH1daMZZNmlCDJrt8Ex+O/wf+kfgAhFxenJ2BlUQAAAABJRU5ErkJggg==";
    private FragmentTransaction fragmentTransaction;
    private BotContentFindlyFragment botContentFragment;
    private BotContentFragmentUpdate botContentFragmentUpdate;
    private Gson gson = new Gson();
    final Handler handler = new Handler();
    private ArrayList<LiveSearchResultsModel> arrTempAllResults;
    private ArrayList<String> arrRecentSearches;
    private EditText edtPassword, edtUserId;
    private TextView tvUserLogin,tvErrorLogin;
    private LinearLayout llWelcomeMessage;
    private ImageView ivWelcomeImage;
    private boolean isEditTouched = false;
    private String displayMsg = "Hello! How can i help you?";
    private boolean lockLiveSearch = false;
    private SharedPreferences sharedPreferences;
    private ResultsViewSetting liveSearchSetting;
    private ResultsViewSetting searchSetting;
    private ResultsViewAppearance faq;
    private ResultsViewAppearance page;
    private ResultsViewAppearance document;

//    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findly_layout);

        screenHeight = (int) AppControl.getInstance(this).getDimensionUtil().screenHeight;

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        perssiatentPanel = findViewById(R.id.persistentPanel);
        img_skill = findViewById(R.id.img_skill);
        txtTitle = findViewById(R.id.txtTitle);
        editButton = (TextView)  findViewById(R.id.editButton);
        edtTxtMessage = (EditText) findViewById(R.id.edtTxtMessage);
        editButton.setTypeface(getTypeFaceObj(this));
        cordinate_layout = findViewById(R.id.cordinate_layout);
        recyclerView_panel =  findViewById(R.id.recyclerView_panel);
        closeBtnPanel = (TextView)  findViewById(R.id.closeBtnPanel);
        closeBtnPanel.setTypeface(getTypeFaceObj(this));
        panelDrag =  findViewById(R.id.panel_drag);
        lvPopularSearch = (AutoExpandListView) findViewById(R.id.lvPopularSearch);
        lvRecentlyAsked = (AutoExpandListView) findViewById(R.id.lvRecentlyAsked);
        lvLiveSearch = (RecyclerView) findViewById(R.id.lvLiveSearch);
        llRecentlyAsked = (LinearLayout) findViewById(R.id.llRecentlyAsked);
//        recyclerView_panel.setLayoutManager(new LinearLayoutManager(this));
        mBottomSheetBehavior = CustomBottomSheetBehavior.from(perssiatentPanel);
        llComposeBar =  (LinearLayout) findViewById(R.id.llComposeBar);
        llPopularSearch = (LinearLayout) findViewById(R.id.llPopularSearch);
        llPlrSearch = (LinearLayout) findViewById(R.id.llPlrSearch);
        llLiveSearch = (RelativeLayout) findViewById(R.id.llLiveSearch);
        llAllresults = (LinearLayout) findViewById(R.id.llAllresults);
        llLogin = (LinearLayout) findViewById(R.id.llLogin);
        panel_root = (LinearLayout) findViewById(R.id.panel_root);
        tvNoResult = (TextView) findViewById(R.id.tvNoResult);
        tvSeeAllResults = (TextView) findViewById(R.id.tvSeeAllResults);
        sendTv = (TextView) findViewById(R.id.sendTv);
        wvLoadUrl = (WebView)findViewById(R.id.wvLoadUrl);
        ivChatIcon = (ImageView) findViewById(R.id.ivChatIcon);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUserId = (EditText) findViewById(R.id.edtUserId);
        tvUserLogin = (TextView) findViewById(R.id.tvUserLogin);
        tvErrorLogin = (TextView) findViewById(R.id.tvErrorLogin);
        llWelcomeMessage = (LinearLayout) findViewById(R.id.llWelcomeMessage);
        ivWelcomeImage = (ImageView) findViewById(R.id.ivWelcomeImage);

        wvLoadUrl.getSettings().setJavaScriptEnabled(true);
//        wvLoadUrl.loadUrl("https://www.caremark.com/");

        edtTxtMessage.setOnTouchListener(this);

        KoreEventCenter.register(this);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        botContentFragment = new BotContentFindlyFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();
        setBotContentFragmentUpdate(botContentFragment);

        lvLiveSearch.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

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

        try
        {
            chatIcon = chatIcon.substring(chatIcon.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(chatIcon.getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivChatIcon.setImageBitmap(decodedByte);
        } catch (Exception e) {
        }

        try
        {
            welconicon = welconicon.substring(welconicon.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(welconicon.getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivWelcomeImage.setImageBitmap(decodedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        ViewGroup.LayoutParams params = llPopularSearch.getLayoutParams();
//        params.height = (int) ((AppControl.getInstance(this).getDimensionUtil().screenHeight - 20 * dp1));
//        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        llPopularSearch.setLayoutParams(params);

        mBottomSheetBehavior.setPeekHeight((int) ((AppControl.getInstance(this).getDimensionUtil().screenHeight)));
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

        edtTxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!StringUtils.isNullOrEmpty(s.toString()))
                {
                    if(!lockLiveSearch)
                        getLiveSearch(s.toString());
                }
                else
                {
                    llPlrSearch.setVisibility(View.VISIBLE);
                    llRecentlyAsked.setVisibility(View.VISIBLE);
                    llLiveSearch.setVisibility(GONE);
                    tvNoResult.setVisibility(GONE);
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
                    Intent intent = new Intent(MainActivity.this, FullResultsActivity.class);
                    intent.putExtra("originalQuery", liveSearchModel.getTemplate().getOriginalQuery());
                    intent.putExtra("searchSetting", searchSetting);
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
                        if(getLogin(MainActivity.this, BotResponse.USER_LOGIN))
                        {
                            llPopularSearch.setVisibility(GONE);
                            perssiatentPanel.setVisibility(View.VISIBLE);
                            saveStringPreferences(edtTxtMessage.getText().toString());
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
                        saveStringPreferences(edtTxtMessage.getText().toString());
//                    sendMessage(edtTxtMessage.getText().toString());
                        sendMessageText(edtTxtMessage.getText().toString());
                        edtTxtMessage.setText("");
                    }
                }
            }
        });

        displayMessage(displayMsg);
        getPopularSearch();
        getRecentlyAsked();
        setLogin(MainActivity.this, BotResponse.USER_LOGIN, false);
        BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithConfig(MainActivity.this,null);
        BotSocketConnectionManager.getInstance().setChatListener(sListener);

        lvPopularSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(arrPopularSearchModels != null && arrPopularSearchModels.size() > 0)
            {
                edtTxtMessage.setText(arrPopularSearchModels.get(i).get_id());
                edtTxtMessage.setSelection(edtTxtMessage.getText().toString().length());
            }
            }
        });

        lvRecentlyAsked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(arrRecentSearches != null && arrRecentSearches.size() > 0)
                {
                    edtTxtMessage.setText(arrRecentSearches.get(i));
                    edtTxtMessage.setSelection(edtTxtMessage.getText().toString().length());
                }
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
                        setLogin(MainActivity.this, BotResponse.USER_LOGIN, true);

//                        if(searchModel != null && searchModel.getTemplate() != null && searchModel.getTemplate().getResults() != null
//                            && searchModel.getTemplate().getResults().getTask() != null && searchModel.getTemplate().getResults().getTask().size() > 0)
//                        {
//                            getSearch(searchModel.getTemplate().getResults().getTask().get(0).getTaskName(), 1);
//                            lockLiveSearch = true;
//                        }
                        saveStringPreferences(edtTxtMessage.getText().toString());
                        sendMessageText(edtTxtMessage.getText().toString());
                        edtTxtMessage.setText("");
                    }
                    else
                        tvErrorLogin.setVisibility(View.VISIBLE);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isEditTouched)
                {
                    llWelcomeMessage.setVisibility(View.VISIBLE);
                }
            }
        }, 3000);
    }

    SocketChatListener sListener = new SocketChatListener()
    {
        @Override
        public void onMessage(BotResponse botResponse) {
            processPayload("", botResponse);
        }

        @Override
        public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
            if(state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED){
                getResultViewSettings();
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

    private void saveStringPreferences(String toString)
    {
        ArrayList<String> arrRecentSearchDos = getStringArrayPref(MainActivity.this, BotResponse.RECENT_SEARCH);

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
            setStringArrayPref(MainActivity.this, BotResponse.RECENT_SEARCH, arrRecentSearchDos);

        getRecentlyAsked();
    }

    @Override
    public void onBackPressed()
    {
        if(perssiatentPanel.getVisibility() == View.VISIBLE)
        {
            perssiatentPanel.setVisibility(GONE);
            llPopularSearch.setVisibility(GONE);
            edtTxtMessage.setText("");

            if(botContentFragment != null)
                botContentFragment.removeAllChatMessages();

            displayMessage(displayMsg);
        }
        else
            super.onBackPressed();
    }

    private void setObject(SearchModel searchModel)
    {
        if(searchModel != null)
        {
            if(searchModel.getTemplate() != null && searchModel.getTemplate().getResults() != null
                && searchModel.getTemplate().getResults().getTask() != null
                && searchModel.getTemplate().getResults().getTask().size() > 0)
            {
                getSearch(searchModel.getTemplate().getResults().getTask().get(0).getTaskName(), 1);
            }
            else
            {
                arrLiveSearchModels.add(searchModel);

                if(chatAutoListviewAdapter == null)
                {
                    chatAutoListviewAdapter = new ChatAutoListviewAdapter(MainActivity.this, arrLiveSearchModels, MainActivity.this);
                    recyclerView_panel.setAdapter(chatAutoListviewAdapter);
                }
                else
                {
                    chatAutoListviewAdapter.refresh(arrLiveSearchModels);
                }
            }
        }
    }

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
        Call<ArrayList<PopularSearchModel>> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getPopularSearch(SDKConfiguration.getSDIX());
        getJWTTokenService.enqueue(new Callback<ArrayList<PopularSearchModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PopularSearchModel>> call, Response<ArrayList<PopularSearchModel>> response) {
                if (response.isSuccessful())
                {
                    arrPopularSearchModels = response.body();

                    if(arrPopularSearchModels != null && arrPopularSearchModels.size() > 0)
                    {
                        llPlrSearch.setVisibility(View.VISIBLE);
                        llLiveSearch.setVisibility(GONE);
                        popularSearchAdapter = new PopularSearchAdapter(MainActivity.this, arrPopularSearchModels);
                        lvPopularSearch.setAdapter(popularSearchAdapter);
                    }
                }
                else
                    llPlrSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ArrayList<PopularSearchModel>> call, Throwable t) {
                Log.e("Popular Search Data", t.toString());
                llPlrSearch.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getRecentlyAsked()
    {
        arrRecentSearches = getStringArrayPref(MainActivity.this, BotResponse.RECENT_SEARCH);

        if(arrRecentSearches != null && arrRecentSearches.size() > 0)
        {
            llRecentlyAsked.setVisibility(View.VISIBLE);
            llLiveSearch.setVisibility(GONE);
            recentlySearchAdapter = new RecentlySearchAdapter(MainActivity.this, arrRecentSearches);
            lvRecentlyAsked.setAdapter(recentlySearchAdapter);
        }
    }

    public void getLiveSearch(String query)
    {
        JsonObject jsonObject = getJsonBody(query, false, 0);
        Call<LiveSearchModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getLiveSearch(SDKConfiguration.getSDIX(),"bearer "+SocketWrapper.getInstance(MainActivity.this).getAccessToken(),"published" ,jsonObject);
        getJWTTokenService.enqueue(new Callback<LiveSearchModel>() {
            @Override
            public void onResponse(Call<LiveSearchModel> call, Response<LiveSearchModel> response) {
                if (response.isSuccessful())
                {
                    liveSearchModel = response.body();

                    if(liveSearchModel != null && liveSearchModel.getTemplate() != null && liveSearchModel.getTemplate().getResults() != null)
                    {
                        cordinate_layout.setVisibility(View.VISIBLE);
                        perssiatentPanel.setVisibility(View.VISIBLE);
                        llPopularSearch.setVisibility(View.VISIBLE);

                        tvNoResult.setVisibility(GONE);
                        llPlrSearch.setVisibility(GONE);
                        llRecentlyAsked.setVisibility(GONE);
                        llLiveSearch.setVisibility(View.VISIBLE);
                        llAllresults.removeAllViews();
//                        lvLiveSearch.setAdapter( new LiveSearchCyclerAdapter(MainActivity.this, getTopFourList(liveSearchModel.getTemplate().getResults()), 0, MainActivity.this, MainActivity.this));

                        if(liveSearchModel.getTemplate().getResults().getFaq() != null && liveSearchModel.getTemplate().getResults().getFaq().size() > 0)
                        {
                            LinearLayout llFaq = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.search_template_new_cell, null);
                            final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
                            final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
                            final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
                            TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);

                            if(faq != null && faq.getTemplate() != null && faq.getTemplate().getType() != null)
                            {
                                if(faq.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                                    alResults.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                                else
                                    alResults.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            }

                            alResults.setAdapter(new LiveSearchCyclerAdapter(MainActivity.this, getTopList(liveSearchModel.getTemplate().getResults().getFaq(), 5, BundleConstants.FAQ), 0, MainActivity.this, MainActivity.this));
                            tvPageTitle.setText("FAQS");

                            ibResults.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(alResults.getVisibility() == View.GONE)
                                    {
                                        alResults.setVisibility(View.VISIBLE);
                                        ibResults.setVisibility(GONE);
                                        ibResults2.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            ibResults2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(alResults.getVisibility() == View.VISIBLE)
                                    {
                                        alResults.setVisibility(GONE);
                                        ibResults2.setVisibility(GONE);
                                        ibResults.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            llAllresults.addView(llFaq);
                        }

                        if(liveSearchModel.getTemplate().getResults().getPage() != null && liveSearchModel.getTemplate().getResults().getPage().size() > 0)
                        {
                            LinearLayout llFaq = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.search_template_new_cell, null);
                            final RecyclerView alResults = (RecyclerView) llFaq.findViewById(R.id.alResults);
                            final ImageButton ibResults = (ImageButton) llFaq.findViewById(R.id.ibResults);
                            final ImageButton ibResults2 = (ImageButton) llFaq.findViewById(R.id.ibResults2);
                            TextView tvPageTitle = (TextView) llFaq.findViewById(R.id.tvPageTitle);

                            if(page != null && page.getTemplate() != null && page.getTemplate().getType() != null)
                            {
                                if(page.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                                    alResults.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                                else
                                    alResults.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            }

                            alResults.setAdapter(new LiveSearchCyclerAdapter(MainActivity.this, getTopList(liveSearchModel.getTemplate().getResults().getPage(), 5, BundleConstants.PAGE), 0, MainActivity.this, MainActivity.this));
                            tvPageTitle.setText("PAGES");

                            ibResults.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(alResults.getVisibility() == View.GONE)
                                    {
                                        alResults.setVisibility(View.VISIBLE);
                                        ibResults.setVisibility(GONE);
                                        ibResults2.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            ibResults2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(alResults.getVisibility() == View.VISIBLE)
                                    {
                                        alResults.setVisibility(GONE);
                                        ibResults2.setVisibility(GONE);
                                        ibResults.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            llAllresults.addView(llFaq);
                        }
                    }
                    else
                    {
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

    public void getSearch(String query, int from)
    {
        JsonObject jsonObject = getJsonBody(query, true, from);
        Call<SearchModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getSearch(SDKConfiguration.getSDIX(), SDKConfiguration.TOKEN, jsonObject);
        getJWTTokenService.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response)
            {
                if (response.isSuccessful())
                {
                    searchModel = response.body();

                    if(searchModel != null && searchModel.getTemplate() != null)
                    {
                        if(searchModel.getTemplate().getResults() != null && searchModel.getTemplate().getResults().getTask() != null
                                && searchModel.getTemplate().getResults().getTask().size() > 0)
                        {
                            if(getLogin(MainActivity.this, BotResponse.USER_LOGIN))
                            {
                                getSearch(searchModel.getTemplate().getResults().getTask().get(0).getTaskName(), 1);
                                lockLiveSearch = true;
                            }
                            else
                            {
                                llPopularSearch.setVisibility(GONE);
                                llLogin.setVisibility(View.VISIBLE);
                            }
                        }
                        else if(searchModel.getTemplate().getWebhookPayload() != null)
                        {
                            if(searchModel.getTemplate().getWebhookPayload().getText() instanceof String)
                            {
                                try
                                {
                                    Type carouselType = new TypeToken<PayloadOuter>() {
                                    }.getType();
                                    PayloadOuter carouselElements = gson.fromJson((String) searchModel.getTemplate().getWebhookPayload().getText(), carouselType);

                                    ComponentModel componentModel = new ComponentModel();
                                    componentModel.setType("template");
                                    componentModel.setPayload(carouselElements);

                                    BotResponseMessage botResponseMessage = new BotResponseMessage();
                                    botResponseMessage.setType("template");
                                    botResponseMessage.setComponent(componentModel);

                                    ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                                    arrBotResponseMessages.add(botResponseMessage);

                                    BotResponse botResponse = new BotResponse();
                                    botResponse.setType("template");
                                    botResponse.setMessage(arrBotResponseMessages);

                                    processPayload("", botResponse);
                                }
                                catch (JsonSyntaxException e)
                                {
                                    displayMessage((String)searchModel.getTemplate().getWebhookPayload().getText());
                                }
                            }
                            else
                            {
                                try
                                {
                                    ComponentModel componentModel = new ComponentModel();
                                    componentModel.setType("template");

                                    Type carouselType = new TypeToken<PayloadOuter>() {
                                    }.getType();
                                    PayloadOuter carouselElements = gson.fromJson(((ArrayList<String>)(searchModel.getTemplate().getWebhookPayload().getText())).get(0), carouselType);

                                    componentModel.setPayload(carouselElements);

                                    BotResponseMessage botResponseMessage = new BotResponseMessage();
                                    botResponseMessage.setType("template");
                                    botResponseMessage.setComponent(componentModel);

                                    ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                                    arrBotResponseMessages.add(botResponseMessage);

                                    BotResponse botResponse = new BotResponse();
                                    botResponse.setType("template");
                                    botResponse.setMessage(arrBotResponseMessages);

                                    processPayload("", botResponse);
                                }
                                catch (JsonSyntaxException e)
                                {
                                    displayMessage(((ArrayList<String>)(searchModel.getTemplate().getWebhookPayload().getText())).get(0));
                                }
                            }

                            if(searchModel.getTemplate().getWebhookPayload().getEndOfTask())
                                lockLiveSearch = !searchModel.getTemplate().getWebhookPayload().getEndOfTask();
                        }
                        else
                        {

                            if(searchModel.getTemplate().getResults() != null)
                            {
                                arrTempAllResults = new ArrayList<>();

                                PayloadInner payloadInner = new PayloadInner();
                                payloadInner.setTemplate_type(searchModel.getTemplateType());

                                if(searchModel.getTemplate().getResults().getFaq() != null &&
                                        searchModel.getTemplate().getResults().getFaq().size() > 0)
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getFaq());

                                if(searchModel.getTemplate().getResults().getPage() != null &&
                                        searchModel.getTemplate().getResults().getPage().size() > 0)
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getPage());

                                if(searchModel.getTemplate().getResults().getTask() != null &&
                                        searchModel.getTemplate().getResults().getTask().size() > 0)
                                    arrTempAllResults.addAll(searchModel.getTemplate().getResults().getTask());

                                payloadInner.setElements(arrTempAllResults);
                                payloadInner.setTitle(searchModel.getTitle());
                                payloadInner.setText("Sure, please find the matched results below");

                                PayloadOuter payloadOuter = new PayloadOuter();
                                payloadOuter.setText(searchModel.getTitle());
                                payloadOuter.setType("template");
                                payloadOuter.setPayload(payloadInner);

                                ComponentModel componentModel = new ComponentModel();
                                componentModel.setType("template");
                                componentModel.setPayload(payloadOuter);

                                BotResponseMessage botResponseMessage = new BotResponseMessage();
                                botResponseMessage.setType("template");
                                botResponseMessage.setComponent(componentModel);

                                ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                                arrBotResponseMessages.add(botResponseMessage);

                                BotResponse botResponse = new BotResponse();
                                botResponse.setType("template");
                                botResponse.setMessage(arrBotResponseMessages);

                                processPayload("", botResponse);

                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Log.e("Search data", t.toString());
            }
        });
    }

    public void getResultViewSettings()
    {
        Call<ResultsViewModel> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getResultViewSettings(SDKConfiguration.getSDIX(),"bearer "+SocketWrapper.getInstance(MainActivity.this).getAccessToken());
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
                            if(resultsViewModel.getSettings().get(i).getInterfaces().equalsIgnoreCase(BundleConstants.LIVE_SEARCH))
                            {
                                liveSearchSetting = resultsViewModel.getSettings().get(i);

                                if(liveSearchSetting != null && liveSearchSetting.getAppearance() != null
                                    && liveSearchSetting.getAppearance().size() > 0)
                                {
                                    for(int k = 0; k < liveSearchSetting.getAppearance().size(); k++)
                                    {
                                        if(liveSearchSetting.getAppearance().get(k).getType().equalsIgnoreCase(BundleConstants.FAQ))
                                            faq = liveSearchSetting.getAppearance().get(k);
                                        else if(liveSearchSetting.getAppearance().get(k).getType().equalsIgnoreCase(BundleConstants.PAGE))
                                            page = liveSearchSetting.getAppearance().get(k);
                                        else if(liveSearchSetting.getAppearance().get(k).getType().equalsIgnoreCase(BundleConstants.DOCUMENT))
                                            document = liveSearchSetting.getAppearance().get(k);
                                    }
                                }
                            }
                            else if(resultsViewModel.getSettings().get(i).getInterfaces().equalsIgnoreCase(BundleConstants.SEARCH))
                            {
                                searchSetting = resultsViewModel.getSettings().get(i);
                            }
                        }
                    }
                    else
                    {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResultsViewModel> call, Throwable t) {
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
            if(results.getFaq() != null && results.getFaq().size() > 0)
            {
                for (int i = 0; i < results.getFaq().size(); i++)
                {
                    results.getFaq().get(i).setAppearance(faq);
                    arrTempResults.add(results.getFaq().get(i));
                    if(arrTempResults.size() == 2)
                        break;
                }
            }

            if(results.getPage() != null && results.getPage().size() > 0)
            {
                if(arrTempResults.size() >= 1)
                {
                    int suntoAdd = arrTempResults.size()+2;
                    for (int i = 0; i < results.getPage().size(); i++)
                    {
                        results.getPage().get(i).setAppearance(page);
                        arrTempResults.add(results.getPage().get(i));
                        if(arrTempResults.size() == suntoAdd)
                            break;
                    }
                }
                else
                {
                    for (int i = 0; i < results.getPage().size(); i++)
                    {
                        results.getPage().get(i).setAppearance(page);
                        arrTempResults.add(results.getPage().get(i));
                        if(arrTempResults.size() == 2)
                            break;
                    }
                }
            }
        }

        if(results.getDocument() != null && results.getDocument().size() > 0)
        {
            if(arrTempResults.size() >= 1)
            {
                int suntoAdd = arrTempResults.size()+2;
                for (int i = 0; i < results.getDocument().size(); i++)
                {
                    results.getDocument().get(i).setAppearance(page);
                    arrTempResults.add(results.getDocument().get(i));
                    if(arrTempResults.size() == suntoAdd)
                        break;
                }
            }
            else
            {
                for (int i = 0; i < results.getDocument().size(); i++)
                {
                    results.getDocument().get(i).setAppearance(page);
                    arrTempResults.add(results.getDocument().get(i));
                    if(arrTempResults.size() == 2)
                        break;
                }
            }
        }

        return arrTempResults;
    }

    private ArrayList<LiveSearchResultsModel> getTopList(ArrayList<LiveSearchResultsModel> results, int count, String type)
    {
        ArrayList<LiveSearchResultsModel> arrTempResults = new ArrayList<>();
        int maxValue = 0;

        if(results != null && results.size() > 0)
        {
            if(results.size() > count)
                maxValue = count;
            else
                maxValue = results.size();

            for (int i = 0; i < maxValue; i++)
            {
                if (type.equalsIgnoreCase(BundleConstants.FAQ))
                    results.get(i).setAppearance(faq);
                else if(type.equalsIgnoreCase(BundleConstants.PAGE))
                    results.get(i).setAppearance(page);
                else if(type.equalsIgnoreCase(BundleConstants.DOCUMENT))
                    results.get(i).setAppearance(document);

                arrTempResults.add(results.get(i));
            }
        }

        return arrTempResults;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view instanceof EditText && !lockLiveSearch)
        {
            cordinate_layout.setVisibility(View.VISIBLE);
            perssiatentPanel.setVisibility(View.VISIBLE);
            llPopularSearch.setVisibility(View.VISIBLE);// User touched edittext
            llPlrSearch.setVisibility(GONE);

            if(arrPopularSearchModels != null && arrPopularSearchModels.size() > 0)
                llPlrSearch.setVisibility(View.VISIBLE);

            isEditTouched = true;
            llWelcomeMessage.setVisibility(GONE);
        }
        return false;
    }

    private JsonObject getJsonBody(String query, boolean smallTalk, int from)
    {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        if(from == 1)
            jsonObject.addProperty("isBotAction", true);

//        jsonObject.add("filters", jsonArray);
        jsonObject.addProperty("query", query.toLowerCase());
        jsonObject.addProperty("maxNumOfResults", 9);
        jsonObject.addProperty("userId", SocketWrapper.getInstance(MainActivity.this).getBotUserId());
        jsonObject.addProperty("streamId", "st-a4a4fabe-11d3-56cc-801d-894ddcd26c51");
        jsonObject.addProperty("lang", "en");

        if(smallTalk)
            jsonObject.addProperty("smallTalk", true);

        Log.e("JsonObject", jsonObject.toString());
        return jsonObject;
    }

    @Override
    public void invokeGenericWebView(String url)
    {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), GenericWebViewActivity.class);
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
        if(payload != null){
            BotSocketConnectionManager.getInstance().sendPayload(message, payload);
        }else{
            BotSocketConnectionManager.getInstance().sendMessage(message, payload);
        }
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

    public void sendMessage(String message) {
        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel("botName", "streamId", null);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
//        persistBotMessage(null,true,botRequest);

        onMessage(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest,false));

        getSearch(message, 0);
    }

    private void sendMessageText(String message) {
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

            if (payloadInner != null)
            {
                payloadInner.convertElementToAppropriate();

                if(payloadInner.getTemplate_type().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_RESULTS_LIST))
                {
                    payloadInner.setResultsViewSetting(searchSetting);
                    ArrayList<LiveSearchResultsModel> arrResults = (ArrayList<LiveSearchResultsModel>)(payloadInner.getElements());
                    for (int i = 0; i < arrResults.size(); i++)
                    {
                        if(arrResults.get(i).get__contentType() != null)
                        {
                            if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.FAQ))
                                arrResults.get(i).setAppearance(faq);
                            else if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
                                arrResults.get(i).setAppearance(page);
                            else if(arrResults.get(i).get__contentType().equalsIgnoreCase(BundleConstants.DOCUMENT))
                                arrResults.get(i).setAppearance(document);
                        }
                        else if(arrResults.get(i).getContentType() != null)
                        {
                            if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.FAQ))
                                arrResults.get(i).setAppearance(faq);
                            else if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.PAGE))
                                arrResults.get(i).setAppearance(page);
                            else if(arrResults.get(i).getContentType().equalsIgnoreCase(BundleConstants.DOCUMENT))
                                arrResults.get(i).setAppearance(document);
                        }
                    }
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
                        payloadOuter.getTemplate().getResults().getFaq().size() > 0)
                    arrTempAllResults.addAll(payloadOuter.getTemplate().getResults().getFaq());

                if(payloadOuter.getTemplate().getResults().getPage() != null &&
                        payloadOuter.getTemplate().getResults().getPage().size() > 0)
                    arrTempAllResults.addAll(payloadOuter.getTemplate().getResults().getPage());

                if(payloadOuter.getTemplate().getResults().getTask() != null &&
                        payloadOuter.getTemplate().getResults().getTask().size() > 0)
                    arrTempAllResults.addAll(payloadOuter.getTemplate().getResults().getTask());

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

}
