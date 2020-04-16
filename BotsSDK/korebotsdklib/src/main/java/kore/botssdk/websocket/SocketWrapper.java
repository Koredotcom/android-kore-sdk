package kore.botssdk.websocket;

import android.content.Context;
import android.net.Network;
import android.os.Handler;
import android.util.Log;


import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.event.RTMConnectionEvent;
import kore.botssdk.io.crossbar.autobahn.websocket.WebSocketConnection;
import kore.botssdk.io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import kore.botssdk.io.crossbar.autobahn.websocket.exceptions.WebSocketException;
import kore.botssdk.io.crossbar.autobahn.websocket.interfaces.IWebSocket;
import kore.botssdk.io.crossbar.autobahn.websocket.types.WebSocketOptions;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotSocketOptions;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.RestResponse.BotAuthorization;
import kore.botssdk.net.RestResponse.JWTTokenResponse;
import kore.botssdk.net.RestResponse.RTMUrl;
import kore.botssdk.net.BotRestBuilder;
import kore.botssdk.net.RestAPI;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.Utils;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public final class SocketWrapper{

    private final String LOG_TAG = "SocketWrapper";//

    public static SocketWrapper pKorePresenceInstance;
    private SocketConnectionListener socketConnectionListener = null;

//    private final WebSocketConnection mConnection = new WebSocketConnection();
    private final IWebSocket mConnection = new WebSocketConnection();
    private static Timer timer = new Timer();

    public boolean ismIsReconnectionAttemptNeeded() {
        return mIsReconnectionAttemptNeeded;
    }

    public void shouldAttemptToReconnect(boolean mIsReconnectionAttemptNeeded) {
        this.mIsReconnectionAttemptNeeded = mIsReconnectionAttemptNeeded;
    }

    private boolean mIsReconnectionAttemptNeeded = true;
    private boolean isConnecting = false;

//    private String url;
//    private URI uri;
//    private boolean mTLSEnabled = false;

    private HashMap<String, Object> optParameterBotInfo;
    private String accessToken;
    private String userAccessToken = null;

    private String anonymousUserAccessToken = null;
    private String JWTToken;
    private String auth;
    private String botUserId;

    public BotInfoModel getBotInfoModel() {
        return botInfoModel;
    }

    public void setBotInfoModel(BotInfoModel botInfoModel) {
        this.botInfoModel = botInfoModel;
    }

    private BotInfoModel botInfoModel;
    private BotSocketOptions options;

    private Context mContext;
    /**
     * initial reconnection delay 1 Sec
     */
    private int mReconnectDelay = 1000;

    /**
     * initial reconnection count
     */
    private int mReconnectionCount = 0;

    /**
     * Restricting outside object creation
     */
    private SocketWrapper(Context mContext) {

//        start(mContext);
        this.mContext = mContext;
//        KoreEventCenter.register(this);
    }

    public String getAccessToken(){
        return auth;
    }

    /**
     * The global default SocketWrapper instance
     */
    public static SocketWrapper getInstance(Context mContext) {
        if (pKorePresenceInstance == null) {
//            synchronized (SocketWrapper.class) {
//                if(pKorePresenceInstance == null) {
                    pKorePresenceInstance = new SocketWrapper(mContext);
//                }
//            }
        }
        return pKorePresenceInstance;
    }

    /**
     * To prevent cloning
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException("Clone not supported");
    }

//    final RestAPI restAPI = BotRestBuilder.getBotRestService();
    private Observable<RestResponse.RTMUrl> getRtmUrl(String accessToken,final BotInfoModel botInfoModel){

        return Observable.create(new ObservableOnSubscribe<RestResponse.RTMUrl>() {
            @Override
            public void subscribe(ObservableEmitter<RestResponse.RTMUrl> observableEmitter) throws Exception {
                try {

                    Call<RestResponse.JWTTokenResponse> jwtTokenResponseCall = BotRestBuilder.getBotRestService().getJWTToken("bearer " + accessToken);
                    Response<RestResponse.JWTTokenResponse> jwtTokenResponseResponse = jwtTokenResponseCall.execute();

                    HashMap<String, Object> hsh = new HashMap<>();
                    hsh.put(Constants.KEY_ASSERTION, jwtTokenResponseResponse.body().getJwt());
                    hsh.put(Constants.BOT_INFO, botInfoModel);



                    Call<RestResponse.BotAuthorization> botAuthorizationCall = BotRestBuilder.getBotRestService().jwtGrant(hsh);
                    Response<RestResponse.BotAuthorization> botAuthorizationResponse = botAuthorizationCall.execute();

                    auth = botAuthorizationResponse.body().getAuthorization().getAccessToken();
                    botUserId = botAuthorizationResponse.body().getUserInfo().getUserId();

                    Call<RestResponse.RTMUrl> rtmUrlCall = BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorizationResponse.body().getAuthorization().getAccessToken(), optParameterBotInfo);
                    Response<RestResponse.RTMUrl> rtmUrlResponse = rtmUrlCall.execute();

                    observableEmitter.onNext(rtmUrlResponse.body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });

        /*return BotRestBuilder.getBotRestService().getJWTToken("bearer " + accessToken).flatMap(new Function<RestResponse.JWTTokenResponse,
                ObservableSource<RestResponse.BotAuthorization>>() {
            @Override
            public ObservableSource<RestResponse.BotAuthorization> apply(RestResponse.JWTTokenResponse jwtTokenResponse) throws Exception {
                HashMap<String, Object> hsh = new HashMap<>();
                hsh.put(Constants.KEY_ASSERTION, jwtTokenResponse.getJwt());
                hsh.put(Constants.BOT_INFO, botInfoModel);
                return BotRestBuilder.getBotRestService().jwtGrant(hsh);
            }
        }).flatMap(new Function<RestResponse.BotAuthorization, ObservableSource<RestResponse.RTMUrl>>() {
            @Override
            public ObservableSource<RestResponse.RTMUrl> apply(RestResponse.BotAuthorization botAuthorization) throws Exception {
                auth = botAuthorization.getAuthorization().getAccessToken();
                botUserId = botAuthorization.getUserInfo().getUserId();
                return BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorization.getAuthorization().getAccessToken(), optParameterBotInfo);
            }
        });*/

    }

    //TODO For speed connection
    private Observable<RestResponse.RTMUrl> getRtmUrlForConnectAnonymous(final String sJwtGrant, final BotInfoModel botInfoModel){

        return Observable.create(new ObservableOnSubscribe<RestResponse.RTMUrl>() {
            @Override
            public void subscribe(ObservableEmitter<RestResponse.RTMUrl> observableEmitter) throws Exception {
                try {
                    HashMap<String, Object> hsh = new HashMap<>();
                    hsh.put(Constants.KEY_ASSERTION, sJwtGrant);
                    hsh.put(Constants.BOT_INFO, botInfoModel);

                    Call<RestResponse.BotAuthorization> botAuthorizationCall = BotRestBuilder.getBotRestService().jwtGrant(hsh);
                    Response<RestResponse.BotAuthorization> botAuthorizationResponse = botAuthorizationCall.execute();

                    HashMap<String, Object> hsh1 = new HashMap<>();
                    hsh1.put(Constants.BOT_INFO, botInfoModel);

                    botUserId = botAuthorizationResponse.body().getUserInfo().getUserId();
                    auth = botAuthorizationResponse.body().getAuthorization().getAccessToken();

                    Call<RestResponse.RTMUrl> rtmUrlCall = BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorizationResponse.body().getAuthorization().getAccessToken(), hsh1);
                    Response<RestResponse.RTMUrl> rtmUrlResponse = rtmUrlCall.execute();

                    observableEmitter.onNext(rtmUrlResponse.body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });

        /*return BotRestBuilder.getBotRestService().jwtGrant(hsh).flatMap(new Function<RestResponse.BotAuthorization, ObservableSource<RestResponse.RTMUrl>>() {
            @Override
            public ObservableSource<RestResponse.RTMUrl> apply(RestResponse.BotAuthorization botAuthorization) throws Exception {
                HashMap<String, Object> hsh1 = new HashMap<>();
                hsh1.put(Constants.BOT_INFO, botInfoModel);

                botUserId = botAuthorization.getUserInfo().getUserId();
                auth = botAuthorization.getAuthorization().getAccessToken();
                return BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorization.getAuthorization().getAccessToken(), hsh1);
            }
        });*/

    }


    /**
     * Method to invoke connection for authenticated user
     *
     * @param accessToken   : AccessToken of the loged user.
     */
    public void connect(String accessToken,final BotInfoModel botInfoModel,SocketConnectionListener socketConnectionListener) {
        this.botInfoModel = botInfoModel;
        this.socketConnectionListener = socketConnectionListener;
        this.accessToken = accessToken;
        optParameterBotInfo = new HashMap<>();
        optParameterBotInfo.put(Constants.BOT_INFO, botInfoModel);

        //If spiceManager is not started then start it
        /*if (!isConnected()) {
            start(mContext);
        }*/

        getRtmUrl(accessToken,botInfoModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestResponse.RTMUrl>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {}

                    @Override
                    public void onNext(RestResponse.RTMUrl rtmUrl) {try {
                            connectToSocket(rtmUrl.getUrl(), false);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {}

                    @Override
                    public void onComplete() {}
                });


    }

    public void ConnectAnonymousForKora(final String userAccessToken, final String sJwtGrant, BotInfoModel botInfoModel, SocketConnectionListener socketConnectionListener,
                                        final String url, String botUserId, String auth){
        this.userAccessToken = userAccessToken;
        this.botInfoModel = botInfoModel;
        this.socketConnectionListener = socketConnectionListener;
        this.accessToken = null;
        this.JWTToken = sJwtGrant;
        this.botInfoModel = botInfoModel;
        this.botUserId = botUserId;
        this.auth = auth;
//        Log.d("IKIDO","Hey Initiating Socket");
        try {
            connectToSocket(url,false);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to invoke connection for anonymous
     *
     * These keys are generated from bot admin console
     */
    public void connectAnonymous(final String sJwtGrant, final BotInfoModel botInfoModel,
                                 final SocketConnectionListener socketConnectionListener, BotSocketOptions options) {
        this.socketConnectionListener = socketConnectionListener;
        this.accessToken = null;
        this.JWTToken = sJwtGrant;
        this.botInfoModel = botInfoModel;
        this.options = options;
        //If spiceManager is not started then start it
        /*if (!isConnected()) {
            start(mContext);
        }*/

        getRtmUrlForConnectAnonymous(sJwtGrant,botInfoModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestResponse.RTMUrl>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        Log.d("HI","on Subscribe");
                    }
                    @Override
                    public void onNext(RestResponse.RTMUrl rtmUrl) {
                        try {
                            connectToSocket(rtmUrl.getUrl(),false);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        Log.d("HI","on error");
                        mIsReconnectionAttemptNeeded = true;
                        reconnectAttempt();
                    }
                    @Override
                    public void onComplete() {
//                        Log.d("IKIDO","on complete called man");
                    }
                });


    }

    /**
     * To connect through socket
     *
     * @param url : to connect the socket to
     * @throws URISyntaxException
     */
    private void connectToSocket(String url, final boolean isReconnectionAttaempt) throws URISyntaxException {
        if((isConnecting || isConnected())) return;
        isConnecting = true;
        if (url != null) {
            if(options != null){
                url = options.replaceOptions(url,options);
            }
//            this.url = url;
//            this.uri = new URI(url);
            WebSocketOptions connectOptions = new WebSocketOptions();
            connectOptions.setReconnectInterval(0);
            try {
                mConnection.connect(url, new  WebSocketConnectionHandler() {
                    @Override
                    public void onOpen() {
//                        Log.d(LOG_TAG, "Connection Open.");
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onOpen(isReconnectionAttaempt);
                        }else{
                            Log.d("IKIDO","Hey listener is null");
                        }
                        isConnecting = false;
                        startSendingPong();
                        mReconnectionCount = 1;
                        mReconnectDelay = 1000;
                        KoreEventCenter.post(new RTMConnectionEvent(true));
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        Log.d(LOG_TAG, "Connection Lost.");
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onClose(code, reason);
                        }else{
                            Log.d("IKIDO","Hey listener is null");
                        }
                        if(timer != null){
                            timer.cancel();
                            timer = null;
                        }
                        isConnecting = false;

                        /*if (isConnected()) {
                            stop();
                        }*/
//                        if(Utils.isNetworkAvailable(mContext))
                            reconnectAttempt();
                    }

                    @Override
                    public void onMessage(String payload) {
//                        Log.d(LOG_TAG, "onTextMessage payload :" + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onTextMessage(payload);
                        }else{
                            Log.d("IKIDO","Hey listener is null");
                        }
                    }

                    /*@Override
                    public void onRawTextMessage(byte[] payload) {
//                        Log.d(LOG_TAG, "onRawTextMessage payload:" + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onRawTextMessage(payload);
                        }
                    }

                    @Override
                    public void onBinaryMessage(byte[] payload) {
//                        Log.d(LOG_TAG, "onBinaryMessage payload: " + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onBinaryMessage(payload);
                        }
                    }*/
                });
            } catch (WebSocketException e) {
                isConnecting = false;
                if(e.getMessage() != null && e.getMessage().equals("already connected")){
                    if (socketConnectionListener != null) {
                        socketConnectionListener.onOpen(isReconnectionAttaempt);
                    }
                    startSendingPong();
                    mReconnectionCount = 1;
                    mReconnectDelay = 1000;
                }
                e.printStackTrace();
            }
        }
    }
    private void startSendingPong(){
        TimerTask tTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if ( mConnection.isConnected()) {
                        mConnection.sendPing("pong from the client".getBytes());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

       try {
           if(timer == null)timer = new Timer();
           timer.scheduleAtFixedRate(tTask, 1000L, 30000L);
       }catch(Exception e){
           e.printStackTrace();
       }
    }

    /**
     *  Reconnect to socket
     */
    private void reconnect() {
        if (accessToken != null) {
            //Reconnection for valid credential
            reconnectForAuthenticUser();
        } else {
            //Reconnection for anonymous
            reconnectForAnonymousUser();
        }
    }

    private Observable<RestResponse.RTMUrl> getRtmUrlReconnectForAuthenticUser(String accessToken){


        return Observable.create(new ObservableOnSubscribe<RestResponse.RTMUrl>() {
            @Override
            public void subscribe(ObservableEmitter<RestResponse.RTMUrl> observableEmitter) throws Exception {
                try {
                    Call<RestResponse.JWTTokenResponse> jwtTokenResponseCall = BotRestBuilder.getBotRestService().getJWTToken("bearer " + accessToken);
                    Response<RestResponse.JWTTokenResponse> jwtTokenResponseResponse = jwtTokenResponseCall.execute();
                    HashMap<String, Object> hsh = new HashMap<>(1);
                    hsh.put(Constants.KEY_ASSERTION, jwtTokenResponseResponse.body().getJwt());

                    Call<RestResponse.BotAuthorization> botAuthorizationCall = BotRestBuilder.getBotRestService().jwtGrant(hsh);
                    Response<RestResponse.BotAuthorization> botAuthorizationResponse = botAuthorizationCall.execute();

                    Call<RestResponse.RTMUrl> rtmUrlCall = BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorizationResponse.body().getAuthorization().getAccessToken(), optParameterBotInfo,true);
                    Response<RestResponse.RTMUrl> rtmUrlResponse = rtmUrlCall.execute();

                    /*Call<RestResponse.BotAuthorization> botAuthorizationCall = BotRestBuilder.getBotRestService().jwtGrant(hsh);
                    Response<RestResponse.BotAuthorization> botAuthorizationResponse = botAuthorizationCall.execute();
                    HashMap<String, Object> hsh1 = new HashMap<>();
                    hsh1.put(Constants.BOT_INFO, botInfoModel);

                    auth = botAuthorizationResponse.body().getAuthorization().getAccessToken();
                    botUserId = botAuthorizationResponse.body().getUserInfo().getUserId();

                    Call<RestResponse.RTMUrl> rtmUrlCall = BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorizationResponse.body().getAuthorization().getAccessToken(), hsh1,true);
                    Response<RestResponse.RTMUrl> rtmUrlResponse = rtmUrlCall.execute();*/



                    observableEmitter.onNext(rtmUrlResponse.body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });

        /*return BotRestBuilder.getBotRestService().getJWTToken("bearer " + accessToken).flatMap(new Function<RestResponse.JWTTokenResponse, ObservableSource<RestResponse.BotAuthorization>>() {
            @Override
            public ObservableSource<RestResponse.BotAuthorization> apply(RestResponse.JWTTokenResponse jwtTokenResponse) throws Exception {
                HashMap<String, Object> hsh = new HashMap<>(1);
                hsh.put(Constants.KEY_ASSERTION, jwtTokenResponse.getJwt());
                return BotRestBuilder.getBotRestService().jwtGrant(hsh);
            }
        }).flatMap(new Function<RestResponse.BotAuthorization, ObservableSource<RestResponse.RTMUrl>>() {
            @Override
            public ObservableSource<RestResponse.RTMUrl> apply(RestResponse.BotAuthorization botAuthorization) throws Exception {
                return BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorization.getAuthorization().getAccessToken(), optParameterBotInfo,true);
            }
        });*/
    }
    /**
     * Reconnection for authentic user
     */
    private void reconnectForAuthenticUser() {
        Log.i(LOG_TAG, "Connection lost. Reconnecting....");


        getRtmUrlReconnectForAuthenticUser(accessToken).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestResponse.RTMUrl>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(RestResponse.RTMUrl rtmUrl) {
                        try {
                            connectToSocket(rtmUrl.getUrl().concat("&isReconnect=true"), true);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    private Observable<RestResponse.RTMUrl> getRtmUrlReconnectForAnonymousUser(){

        return Observable.create(new ObservableOnSubscribe<RestResponse.RTMUrl>() {
            @Override
            public void subscribe(ObservableEmitter<RestResponse.RTMUrl> observableEmitter) throws Exception {
                try {
                    HashMap<String, Object> hsh = new HashMap<>();
                    hsh.put(Constants.KEY_ASSERTION, JWTToken);
                    hsh.put(Constants.BOT_INFO, botInfoModel);

                    Call<RestResponse.BotAuthorization> botAuthorizationCall = BotRestBuilder.getBotRestService().jwtGrant(hsh);
                    Response<RestResponse.BotAuthorization> botAuthorizationResponse = botAuthorizationCall.execute();
                    HashMap<String, Object> hsh1 = new HashMap<>();
                    hsh1.put(Constants.BOT_INFO, botInfoModel);

                    auth = botAuthorizationResponse.body().getAuthorization().getAccessToken();
                    botUserId = botAuthorizationResponse.body().getUserInfo().getUserId();

                    Call<RestResponse.RTMUrl> rtmUrlCall = BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorizationResponse.body().getAuthorization().getAccessToken(), hsh1,true);
                    Response<RestResponse.RTMUrl> rtmUrlResponse = rtmUrlCall.execute();

                    /*Call<JWTTokenResponse> jwtTokenResponseCall = KaRestBuilder.getKaRestAPI().getJWTToken(Utils.ah(accessToken),new HashMap<String, Object>());
                    Response<JWTTokenResponse> jwtTokenResponse = jwtTokenResponseCall.execute();

                    SDKConfiguration.Server.setKoreBotServerUrl(jwtTokenResponse.body().getBotsUrl());
                    KoraSocketConnectionManager.this.jwtKeyResponse = jwtTokenResponse.body();
                    botName = jwtKeyResponse.getBotName();
                    streamId = jwtKeyResponse.getStreamId();


                    HashMap<String, Object> hsh = new HashMap<>();
                    botInfoModel = new BotInfoModel(jwtTokenResponse.body().getBotName(),jwtTokenResponse.body().getStreamId(),botClient.getCustomData());
                    hsh.put(Constants.KEY_ASSERTION, jwtTokenResponse.body().getJwt());
                    hsh.put(Constants.BOT_INFO, botInfoModel);

                    Call<RestResponse.BotAuthorization> botAuthorizationCall = BotRestBuilder.getBotRestService().jwtGrant(hsh);
                    Response<RestResponse.BotAuthorization> botAuthorizationResponse = botAuthorizationCall.execute();

                    HashMap<String, Object> optParameterBotInfo = new HashMap<>();
                    optParameterBotInfo.put(Constants.BOT_INFO, botInfoModel);
                    botUserId = botAuthorizationResponse.body().getUserInfo().getUserId();
                    botAccessToken = botAuthorizationResponse.body().getAuthorization().getAccessToken();

                    Call<RestResponse.RTMUrl> rtmUrlCall = BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorizationResponse.body().getAuthorization().getAccessToken(), optParameterBotInfo);
                    Response<RestResponse.RTMUrl> rtmUrlResponse = rtmUrlCall.execute();*/

                    observableEmitter.onNext(rtmUrlResponse.body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }

        });

            /*return BotRestBuilder.getBotRestService().jwtGrant(hsh).flatMap(new Function<RestResponse.BotAuthorization, ObservableSource<RestResponse.RTMUrl>>() {
                @Override
                public ObservableSource<RestResponse.RTMUrl> apply(RestResponse.BotAuthorization botAuthorization) throws Exception {
                    HashMap<String, Object> hsh1 = new HashMap<>();
                    hsh1.put(Constants.BOT_INFO, botInfoModel);

                    auth = botAuthorization.getAuthorization().getAccessToken();
                    botUserId = botAuthorization.getUserInfo().getUserId();

                    return BotRestBuilder.getBotRestService().getRtmUrl("bearer " + botAuthorization.getAuthorization().getAccessToken(), hsh1,true);
                }
            });*/

    }
    /**
     * Reconnection for anonymous user
     */
    private void reconnectForAnonymousUser() {

        Log.i(LOG_TAG, "Connection lost. Reconnecting....");

        getRtmUrlReconnectForAnonymousUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestResponse.RTMUrl>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(RestResponse.RTMUrl rtmUrl) {
                        try {
                            connectToSocket(rtmUrl.getUrl().concat("&isReconnect=true"),true);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public void onTokenRefresh(String token){
        JWTToken = token;
        reconnect();
    }

    /**
     * @param msg : The message object
     * @return Was it able to successfully send the message.
     */
    public boolean sendMessage(String msg) {
        if (mConnection != null && mConnection.isConnected()) {
            mConnection.sendMessage(msg);
            return true;
        } else {
            if(userAccessToken != null && socketConnectionListener != null){
                socketConnectionListener.refreshJwtToken();
            }else {
                reconnect();
            }
            Log.e(LOG_TAG, "Connection is not present. Reconnecting...");
            return false;
        }
    }

   /*
     * Method to Reconnection attempt based on incremental delay
     *
     * @reurn
     */
    private void reconnectAttempt() {
//        mIsImmediateFetchActionNeeded = true;
        mReconnectDelay = getReconnectDelay();
        try {
            final Handler _handler = new Handler();
            Runnable r = new Runnable() {

                @Override
                public void run() {

                    Log.d(LOG_TAG, "Entered into reconnection post delayed " + mReconnectDelay);
                    if (mIsReconnectionAttemptNeeded && !isConnected()) {
                        reconnect();
//                        Toast.makeText(mContext,"SocketDisConnected",Toast.LENGTH_SHORT).show();
                        mReconnectDelay = getReconnectDelay();
                        _handler.postDelayed(this, mReconnectDelay);
                        Log.d(LOG_TAG, "#### trying to reconnect");
                    }

                }
            };
                _handler.postDelayed(r, mReconnectDelay);
        } catch (Exception e) {
            Log.d(LOG_TAG, ":: The Exception is " + e.toString());
        }
    }

    /**
     * The reconnection attempt delay(incremental delay)
     *
     * @return
     */
    private int getReconnectDelay() {
        mReconnectionCount++;
        Log.d(LOG_TAG, "Reconnection count " + mReconnectionCount);
        if (mReconnectionCount > 6) mReconnectionCount = 1;
        Random rint = new Random();
        return (rint.nextInt(5) + 1) * mReconnectionCount * 1000;
    }
    /**
     * For disconnecting user's presence
     * Call this method when the user logged out
     */
    public void disConnect() {
        mIsReconnectionAttemptNeeded = false;
        if (mConnection != null && mConnection.isConnected()) {
            try {
                mConnection.sendClose();
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception while disconnection");
            }
            Log.d(LOG_TAG, "DisConnected successfully");
        } else {
            Log.d(LOG_TAG, "Cannot disconnect.._client is null");
        }


        //The bot URL may change
        BotRestBuilder.clearInstance();
        auth = null;
        botUserId = null;
        /*if (isConnected()) {
            stop();
        }*/
    }

    /**
     * Determine the TLS enability of the url.
     * @param url url to connect to (is generally either ws:// or wss://)
     */
   /* private void determineTLSEnability(String url) {
        mTLSEnabled = url.startsWith(Constants.SECURE_WEBSOCKET_PREFIX);
    }*/

    /**
     * To determine wither socket is connected or not
     * @return boolean indicating the connection presence.
     */
    public boolean isConnected() {
        if (mConnection != null && mConnection.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }
}