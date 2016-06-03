package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.net.KoreRestRequest;
import kore.botssdk.net.KoreRestResponse;
import kore.botssdk.utils.Contants;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.utils.KoreBotSharedPreferences;
import kore.botssdk.websocket.KorePresenceListener;
import kore.botssdk.websocket.KorePresenceWrapper;
import retrofit.client.Response;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotHomeActivity extends BaseSpiceActivity {

    Button launchBotBtn;
    Button logOutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_home_activity_layout);

        findViews();
        setListeners();
    }

    private void findViews() {
        launchBotBtn = (Button) findViewById(R.id.launchBotBtn);
        logOutBtn = (Button) findViewById(R.id.logOutBtn);
    }

    private void setListeners() {
        launchBotBtn.setOnClickListener(launchBotBtnOnClickListener);
    }

    /**
     * START of : Listeners
     */

    View.OnClickListener launchBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
            startActivity(intent);
//            connectToWebSocket(KoreBotSharedPreferences.getAccessTokenFromPreferences(BotHomeActivity.this));
        }
    };

    /**
     * END of : Listeners
     */

    private void connectToWebSocket(final String accessToken){
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

                Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
                startActivity(intent);

            }
        });
    }
}
