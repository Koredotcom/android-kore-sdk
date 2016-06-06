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
import kore.botssdk.net.RestRequest;
import kore.botssdk.net.RestResponse;
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

        String accessToken = BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext());

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class,null, accessToken) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                RestResponse.JWTTokenResponse jwtToken = getService().getJWTToken(accessTokenHeader());
                HashMap<String,Object> hsh = new HashMap<>(1);
                hsh.put(Contants.KEY_ASSERTION,jwtToken.getJwt());
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);
                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()));
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
                SocketWrapper.getInstance().connect(response.getUrl());
            }
        });
    }
}
