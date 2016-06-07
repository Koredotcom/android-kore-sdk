package kore.botssdk.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import java.util.HashMap;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import kore.botssdk.R;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.models.AnonymousAssertionModel;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.net.RestRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.utils.BotRequestController;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.Contants;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.utils.BotSharedPreferences;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotChatActivity extends BaseSpiceActivity {

    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;

    FragmentTransaction fragmentTransaction;

    String chatBot, taskBotId;
    String loginMode = Contants.NORMAL_FLOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);
        findViews();
        getBundleInfo();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        BotContentFragment botContentFragment = new BotContentFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ComposeFooterFragment composeFooterFragment = new ComposeFooterFragment();
        composeFooterFragment.setArguments(getIntent().getExtras());
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();

        if (loginMode.equalsIgnoreCase(Contants.NORMAL_FLOW)) {
            connectToWebSocket();
        } else {
            connectToWebSocketAnonymous();
        }

        updateTitleBar();
    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            chatBot = bundle.getString(BundleUtils.CHATBOT, "");
            taskBotId = bundle.getString(BundleUtils.TASKBOTID, "");
            loginMode = bundle.getString(BundleUtils.LOGIN_MODE, Contants.NORMAL_FLOW);
        }
    }

    private void findViews() {
        chatLayoutFooterContainer = (FrameLayout) findViewById(R.id.chatLayoutFooterContainer);
        chatLayoutContentContainer = (FrameLayout) findViewById(R.id.chatLayoutContentContainer);
    }

    private void updateTitleBar() {
        String botName = (chatBot != null && !chatBot.isEmpty()) ? chatBot : ((loginMode.equalsIgnoreCase(Contants.NORMAL_FLOW)) ? "Kore Bot" : "Kore Bot - anonymous");
        getSupportActionBar().setSubtitle(botName);
    }

    private void connectToWebSocket(){

        String accessToken = BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext());

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class,null, accessToken) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                RestResponse.JWTTokenResponse jwtToken = getService().getJWTToken(accessTokenHeader());
                HashMap<String,Object> hsh = new HashMap<>(1);
                hsh.put(Contants.KEY_ASSERTION,jwtToken.getJwt());
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);

                HashMap<String, Object> optParameterBotInfo = new HashMap<>();
                if (chatBot != null && !chatBot.isEmpty() && taskBotId != null && !taskBotId.isEmpty()) {
                    BotInfoModel botInfoModel = new BotInfoModel(chatBot, taskBotId);
                    optParameterBotInfo.put("botInfo", botInfoModel);
                }

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), optParameterBotInfo);
                return rtmUrl;
            }
        } ;

        getSpiceManager().execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                CustomToast.showToast(getApplicationContext(), "onRequestFailure !!");
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                CustomToast.showToast(getApplicationContext(), "onRequestSuccess !!");
                BotRequestController.setUrl(response.getUrl());
                SocketWrapper.getInstance().connect(BotRequestController.getUrl());
            }
        });
    }

    private void connectToWebSocketAnonymous() {
        String accessToken = BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext());

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class,null, accessToken) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                HashMap<String,Object> hsh = new HashMap<>(2);
                hsh.put(Contants.KEY_ASSERTION, new AnonymousAssertionModel());
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrantAnonymous(hsh);

                HashMap<String, Object> optParameterBotInfo = new HashMap<>();
                if (chatBot != null && !chatBot.isEmpty() && taskBotId != null && !taskBotId.isEmpty()) {
                    BotInfoModel botInfoModel = new BotInfoModel(chatBot, taskBotId);
                    optParameterBotInfo.put("botInfo", botInfoModel);
                }


                String userId = jwtGrant.getUserInfo().getId();
                String authToken = jwtGrant.getAuthorization().getAccessToken();

                boolean successfullySaved = BotSharedPreferences.saveCredsToPreferences(BotChatActivity.this, userId, authToken);

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), optParameterBotInfo);
                return rtmUrl;
            }
        } ;

        getSpiceManager().execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                CustomToast.showToast(getApplicationContext(), "onRequestFailure !! Anonymous");
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                CustomToast.showToast(getApplicationContext(), "onRequestSuccess !! Anonymous");

                BotRequestController.setUrl(response.getUrl());
                SocketWrapper.getInstance().connect(BotRequestController.getUrl());
            }
        });
    }

}
