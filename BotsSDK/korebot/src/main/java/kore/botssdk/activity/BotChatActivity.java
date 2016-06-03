package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import java.util.HashMap;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import kore.botssdk.R;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.net.KoreRestRequest;
import kore.botssdk.net.KoreRestResponse;
import kore.botssdk.utils.Contants;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.utils.KoreBotSharedPreferences;
import kore.botssdk.websocket.KorePresenceWrapper;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotChatActivity extends BaseSpiceActivity {

    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);
        findViews();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        BotContentFragment botContentFragment = new BotContentFragment();
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ComposeFooterFragment composeFooterFragment = new ComposeFooterFragment();
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();

        connectToWebSocket();
    }

    private void findViews() {
        chatLayoutFooterContainer = (FrameLayout) findViewById(R.id.chatLayoutFooterContainer);
        chatLayoutContentContainer = (FrameLayout) findViewById(R.id.chatLayoutContentContainer);
    }


    private void connectToWebSocket(){

        String accessToken = KoreBotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext());

        KoreRestRequest<KoreRestResponse.RTMUrl> request = new KoreRestRequest<KoreRestResponse.RTMUrl>(KoreRestResponse.RTMUrl.class,null, accessToken) {
            @Override
            public KoreRestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                KoreRestResponse.JWTTokenResponse jwtToken = getService().getJWTToken(accessTokenHeader());
                HashMap<String,Object> hsh = new HashMap<>(1);
                hsh.put(Contants.KEY_ASSERTION,jwtToken.getJwt());
                KoreRestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);
                KoreRestResponse.RTMUrl rtmUrl = getService().getRtmUrl(accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()));
                return rtmUrl;
            }
        } ;

        getSpiceManager().execute(request, new RequestListener<KoreRestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                CustomToast.showToast(getApplicationContext(), "onRequestFailure !!");
            }

            @Override
            public void onRequestSuccess(KoreRestResponse.RTMUrl response) {
                CustomToast.showToast(getApplicationContext(), "onRequestSuccess !!");
                KorePresenceWrapper.getInstance().connect(response.getUrl());
            }
        });
    }
}
