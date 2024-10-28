package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.audiocodes.mv.webrtcsdk.audio.WebRTCAudioManager;
import com.audiocodes.mv.webrtcsdk.audio.WebRTCAudioRouteListener;
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSession;
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSessionEventListener;
import com.audiocodes.mv.webrtcsdk.session.CallState;
import com.audiocodes.mv.webrtcsdk.session.CallTermination;
import com.audiocodes.mv.webrtcsdk.session.CallTransferState;
import com.audiocodes.mv.webrtcsdk.session.DTMF;
import com.audiocodes.mv.webrtcsdk.session.NotifyEvent;
import com.audiocodes.mv.webrtcsdk.session.RemoteContact;
import com.audiocodes.mv.webrtcsdk.session.TerminationInfo;
import com.audiocodes.mv.webrtcsdk.useragent.ACConfiguration;
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA;
import com.audiocodes.mv.webrtcsdk.useragent.WebRTCException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Callbacks.CallBackHandler;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;
import kore.botssdk.audiocodes.webrtcclient.General.ImageUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;
import kore.botssdk.audiocodes.webrtcclient.Login.LogoutManager;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBManager;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBObject;
import kore.botssdk.audiocodes.webrtcclient.services.CallForegroundService;
import kore.botssdk.models.EventModel;
import kore.botssdk.utils.BundleConstants;

@SuppressWarnings("UnknownNullness")
public class CallActivity extends BaseAppCompatActivity implements AudioCodesSessionEventListener {
    private static final String TAG = "CallActivity";
    public static final String SESSION_ID = "sessionID";
    private TextView callStateTextView;
    private TextView transferStateNumberTextView;
    private TextView contactNameTextView;
    private TextView contactPhoneNumberTextView;
    private ImageView contactImageView;
    private AudioCodesSession session;
    private View endCallButton;
    private View holdButton;
    private View muteAudioButton;
    private View switchCallButton;
    private View audioRouteButton;
    private View muteVideoButton;
    private View addVideoButton;
    private boolean dtmfEnabled = false;
    boolean guiInitialized;
    private int callDuration = 0;
    private WebRTCAudioManager ac;
    private WebRTCAudioManager.AudioRoute route = WebRTCAudioManager.AudioRoute.EARPIECE;
    private final Handler timerHandler = new Handler();
    private final ArrayList<Integer> sessionList = new ArrayList<>();
    private Runnable runnable;
    private final MyAudioCodesSessionEventListener myAudioCodesSessionEventListener = new MyAudioCodesSessionEventListener();
    private int lastSessionIndex;

    private final CallBackHandler.LoginStateChanged loginStateChanged = new CallBackHandler.LoginStateChanged() {
        @Override
        public void loginStateChange(boolean state) {
            if (handler != null) {
                handler.post(() -> {
                    endCallButton.setEnabled(false);
                    holdButton.setEnabled(false);
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.call_activity);
        handler = new Handler();
        CallBackHandler.registerLginStateChange(loginStateChanged);
        BotApplication.setCurrentActivity(this);

        ac = WebRTCAudioManager.getInstance();
        ac.setAudioRoute(WebRTCAudioManager.AudioRoute.SPEAKER_PHONE);

        int sessionIndex = this.getIntent().getIntExtra(SESSION_ID, -1);
        if (sessionIndex == -1) {
            Log.d(TAG, "getActiveSession: " + ACManager.getInstance().getActiveSession());
            Log.d(TAG, "getSessionList: " + ACManager.getInstance().getSessionList());

            if (ACManager.getInstance().getActiveSession() != null) {
                sessionIndex = ACManager.getInstance().getActiveSession().getSessionID();
            } else {
                //close application
                LogoutManager.closeApplication();
                return;
            }
        }

        Log.d(TAG, "Session: " + sessionIndex);
        if (!sessionList.contains(sessionIndex)) {
            sessionList.add(sessionIndex);
        }

        session = AudioCodesUA.getInstance().getSession(sessionIndex);
        if (session == null) {
            Log.d(TAG, "Session is null!!!");
            Toast.makeText(BotApplication.getGlobalContext(), getString(R.string.session_null_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        lastSessionIndex = sessionIndex;
        updateUI();
        CallForegroundService.startService();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");

        int sessionIndex = intent.getIntExtra(SESSION_ID, -1);
        if (sessionIndex == -1) {
            sessionIndex = lastSessionIndex;
        } else {
            lastSessionIndex = sessionIndex;
        }
        Log.d(TAG, "Session: " + sessionIndex);
        if (!sessionList.contains(sessionIndex)) {
            sessionList.add(sessionIndex);
        }
        if (session.hasVideo()) {
            session.stopVideo();
        }
        session = AudioCodesUA.getInstance().getSession(sessionIndex);
        switchCallButton.setEnabled(true);
        updateUI();
    }

    void initGUI() {
        callStateTextView = findViewById(R.id.call_textview_call_state);
        transferStateNumberTextView = findViewById(R.id.call_textview_call_transfer_state_number);

        contactNameTextView = findViewById(R.id.call_textview_contact_name);
        contactPhoneNumberTextView = findViewById(R.id.call_textview_contact_number);
        contactImageView = findViewById(R.id.call_imageView_contact_image);

        endCallButton = findViewById(R.id.call_button_end_call);
        View dtmfButton = findViewById(R.id.call_button_dtmf);
        audioRouteButton = findViewById(R.id.call_button_audio_route);
        addVideoButton = findViewById(R.id.call_button_add_video);

        View endVideoCallButton = findViewById(R.id.call_button_end_video_call);
        muteVideoButton = findViewById(R.id.call_button_video_mute);
        View videoRouteButton = findViewById(R.id.call_button_video_route);
        View videoHoldButton = findViewById(R.id.call_button_video_hold);

        View addCallButton = findViewById(R.id.call_button_add_call);
        switchCallButton = findViewById(R.id.call_button_switch_call);
        muteAudioButton = findViewById(R.id.call_button_mute);
        holdButton = findViewById(R.id.call_button_hold);
        View switchCameraButton = findViewById(R.id.call_button_switch_camera);
        View transferCallButton = findViewById(R.id.call_button_transfer);

        // transfer call is always enabled, if only 1 call is present, it will transfer the call to a preconfigured number
        // otherwise it will transfer the active call to the 2nd call.
        transferCallButton.setEnabled(true);

        endCallButton.setOnClickListener(v -> {
            Log.d(TAG, "session getTermination: " + session.getTermination());
            if (session.getTermination() == CallTermination.TERMINATED_NOT_FOUND) {
                handleCallTermination(session, null);
                return;
            }
            if (session.getTermination() == CallTermination.TERMINATED_MEDIA_FAILED) {
                handleCallTermination(session, null);
                return;
            }
            //session.
            Log.d(TAG, "session getCallState: " + session.getCallState());
            Log.d(TAG, "session getTermination: " + session.getTermination());
            session.terminate();
            handleCallTermination(session, null);
        });

        endVideoCallButton.setOnClickListener(v -> {

            Log.d(TAG, "session getTermination: " + session.getTermination());
            if (session.getTermination() == CallTermination.TERMINATED_NOT_FOUND) {
                handleCallTermination(session, null);
                return;
            }
            if (session.getTermination() == CallTermination.TERMINATED_MEDIA_FAILED) {
                handleCallTermination(session, null);
                return;
            }
            //session.
            Log.d(TAG, "session getCallState: " + session.getCallState());
            Log.d(TAG, "session getTermination: " + session.getTermination());
            session.terminate();
            handleCallTermination(session, null);
        });

        switchCameraButton.setOnClickListener(v -> session.switchCamera());

        holdButton.setOnClickListener(v -> {
            boolean isLocalHold = session.isLocalHold();
            setHold(!isLocalHold);
        });

        videoHoldButton.setOnClickListener(v -> {
            boolean isLocalHold = session.isLocalHold();
            setHold(!isLocalHold);
        });

        muteAudioButton.setOnClickListener(v -> {
            boolean isAudioMuted = session.isAudioMuted();
            Log.d(TAG, "isAudioMuted before: " + isAudioMuted);

            isAudioMuted = !isAudioMuted;

            session.muteAudio(isAudioMuted);
            session.muteVideo(isAudioMuted);

            isAudioMuted = session.isAudioMuted();
            Log.d(TAG, "isAudioMuted after: " + isAudioMuted);
            muteAudioButton.setSelected(isAudioMuted);
            ((ImageView) v).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_call_mute_off, getTheme()));

            if (isAudioMuted)
                ((ImageView) v).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_call_mute_on, getTheme()));
        });

        muteVideoButton.setOnClickListener(v -> {
            boolean isAudioMuted = session.isAudioMuted();
            Log.d(TAG, "isAudioMuted before: " + isAudioMuted);

            isAudioMuted = !isAudioMuted;

            session.muteAudio(isAudioMuted);
            session.muteVideo(isAudioMuted);

            isAudioMuted = session.isAudioMuted();
            Log.d(TAG, "isAudioMuted after: " + isAudioMuted);
            muteVideoButton.setSelected(isAudioMuted);
            ((ImageView) v).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_call_mute_off, getTheme()));

            if (isAudioMuted)
                ((ImageView) v).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_call_mute_on, getTheme()));
        });

        switchCallButton.setOnClickListener(v -> {
            for (Integer index : sessionList) {
                Log.d(TAG, "Session ID: " + index);
            }
            for (Integer index : sessionList) {

                if (index != session.getSessionID()) {
                    session.hold(true);
                    session = AudioCodesUA.getInstance().getSession(index);
                    session.hold(false);
                    updateUI();
                    Log.d(TAG, "Session index: " + index);
                    return;
                }
            }

        });

        addCallButton.setOnClickListener(v -> {
            String call = Prefs.getSecondCall();
            if (call == null || call.equals("")) {
                Toast.makeText(BotApplication.getGlobalContext(), getString(R.string.no_remote_contact), Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Log.d(TAG, "Calling to: " + call);
                if (session.hasVideo()) {
                    session.stopVideo();
                }
                RemoteContact contact = new RemoteContact();
                contact.setUserName(call);
                session = AudioCodesUA.getInstance().call(contact, session.hasVideo(), null);
                Log.d(TAG, "Added call_activity: " + session.getSessionID());
                if (!sessionList.contains(session.getSessionID())) {
                    sessionList.add(session.getSessionID());
                }
                switchCallButton.setEnabled(true);
                updateCallGui();
                updateUI();
            } catch (WebRTCException e) {
                Log.d(TAG, "oops: " + e.getMessage());
            }
        });

        transferCallButton.setOnClickListener(v -> {
            if (sessionList.size() > 1) {
                for (Integer index : sessionList) {
                    if (index != session.getSessionID()) {
                        AudioCodesSession transferSession = AudioCodesUA.getInstance().getSession(index);
                        transferSession.transferCall(session);
                        updateUI();
                        return;
                    }
                }
            } else {
                String call = Prefs.getTransferCall();
                if (call == null || call.equals("")) {
                    Toast.makeText(BotApplication.getGlobalContext(), getString(R.string.no_remote_contact), Toast.LENGTH_SHORT).show();
                    return;
                }
                RemoteContact transferContact = new RemoteContact();
                transferContact.setUserName(call);
                session.transferCall(transferContact);
            }
        });

        audioRouteButton.setOnClickListener(v -> {
            if (route == WebRTCAudioManager.AudioRoute.BLUETOOTH) {
                route = WebRTCAudioManager.AudioRoute.SPEAKER_PHONE;
            } else if (route == WebRTCAudioManager.AudioRoute.SPEAKER_PHONE) {
                route = WebRTCAudioManager.AudioRoute.EARPIECE;
            } else {
                route = WebRTCAudioManager.AudioRoute.BLUETOOTH;
            }
            Log.d("WebRTCAudioManager", "new route = " + route);
            ac.setAudioRoute(route);
        });

        videoRouteButton.setOnClickListener(v -> {
            if (route == WebRTCAudioManager.AudioRoute.BLUETOOTH) {
                route = WebRTCAudioManager.AudioRoute.SPEAKER_PHONE;
            } else if (route == WebRTCAudioManager.AudioRoute.SPEAKER_PHONE) {
                route = WebRTCAudioManager.AudioRoute.EARPIECE;
            } else {
                route = WebRTCAudioManager.AudioRoute.BLUETOOTH;
            }
            Log.d("WebRTCAudioManager", "new route = " + route);
            ac.setAudioRoute(route);
        });

        addVideoButton.setOnClickListener(v -> {
            Log.d(TAG, "hasVideo: " + session.hasVideo() + " isVideoMuted: " + session.isVideoMuted());
            if (session.hasVideo()) {
                Log.d(TAG, "new video mute stat: " + !session.isVideoMuted());
                session.muteVideo(!session.isVideoMuted());
            } else {
                Log.d(TAG, "add video to the session");
                session.reinviteWithVideo();
            }
            updateVideoButton();
        });

        dtmfButton.setOnClickListener(v -> {
            dtmfEnabled = !dtmfEnabled;
            updateCallGui();

            ((ImageView) v).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dial_pad_off, getTheme()));

            if (dtmfEnabled)
                ((ImageView) v).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dial_pad_on, getTheme()));
        });
        session.addSessionEventListener(this);

        initDtmf();
        updateVideoButton();
        guiInitialized = true;

        runnable = new Runnable() {
            @Override
            public void run() {
                callDuration++;
                int hours = callDuration / 3600;
                int minutes = (callDuration % 3600) / 60;
                int secs = callDuration % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
                contactNameTextView.setText(time);
                timerHandler.postDelayed(this, 1000);
            }
        };
    }

    void setHold(boolean hold) {
        Log.d(TAG, "setHold: " + hold);
        holdButton.setSelected(hold);
        session.hold(hold);
    }

    private void initDtmf() {
        int[] keypadButtonClickListID = {R.id.call_button_keypad_1, R.id.call_button_keypad_2, R.id.call_button_keypad_3, R.id.call_button_keypad_4, R.id.call_button_keypad_5, R.id.call_button_keypad_6, R.id.call_button_keypad_7, R.id.call_button_keypad_8, R.id.call_button_keypad_9, R.id.call_button_keypad_hash, R.id.call_button_keypad_0, R.id.call_button_keypad_asterisk};

        View.OnClickListener dialPadClickListener = clickedView -> {
            if (clickedView == null) {
                return;
            }
            int id = clickedView.getId();
            if (id == R.id.call_button_keypad_1) {
                session.sendDTMF(DTMF.ONE);
            } else if (id == R.id.call_button_keypad_2) {
                session.sendDTMF(DTMF.TWO);
            } else if (id == R.id.call_button_keypad_3) {
                session.sendDTMF(DTMF.THREE);
            } else if (id == R.id.call_button_keypad_4) {
                session.sendDTMF(DTMF.FOUR);
            } else if (id == R.id.call_button_keypad_5) {
                session.sendDTMF(DTMF.FIVE);
            } else if (id == R.id.call_button_keypad_6) {
                session.sendDTMF(DTMF.SIX);
            } else if (id == R.id.call_button_keypad_7) {
                session.sendDTMF(DTMF.SEVEN);
            } else if (id == R.id.call_button_keypad_8) {
                session.sendDTMF(DTMF.EIGHT);
            } else if (id == R.id.call_button_keypad_9) {
                session.sendDTMF(DTMF.NINE);
            } else if (id == R.id.call_button_keypad_hash) {
                session.sendDTMF(DTMF.POUND);
            } else if (id == R.id.call_button_keypad_0) {
                session.sendDTMF(DTMF.ZERO);
            } else if (id == R.id.call_button_keypad_asterisk) {
                session.sendDTMF(DTMF.STAR);
            }
        };

        for (int keypadButtonClickID : keypadButtonClickListID) {
            View view = findViewById(keypadButtonClickID);
            if (view != null) {
                view.setOnClickListener(dialPadClickListener);
            }
        }
    }

    void updateUI() {
        handler.post(() -> {
            if (!guiInitialized) {
                initGUI();
                WebRTCAudioManager.getInstance().setWebRTcAudioRouteListener(new MyWebRTcAudioRouteListener());
                updateCallGui();
            }

            RemoteContact remote = session.getRemoteNumber();
            if (remote != null) {
                String callState = getString(R.string.call_textview_call_state) + " " + session.getCallState();
                callStateTextView.setText(callState);

                //List<NativeContactObject> nativeContactObjectList = NativeContactUtils.getContactListByPhoneNumber(session.getRemoteNumber().getUserName());
                List<NativeDBObject> nativeDBObjectList = NativeDBManager.getContactList(NativeDBManager.QueryType.BY_PHONE_AND_SIP, remote.getUserName());//  .getContactListByPhoneNumber(session.getRemoteNumber().getUserName());
                String displayName = remote.getDisplayName();
                String userName = remote.getUserName();
                // Log.d(TAG, "check user name: "+userName+" displayName:"+displayName);
                if (nativeDBObjectList != null && !nativeDBObjectList.isEmpty()) {
                    // Log.d(TAG, "check user nativeDBObjectList: " +nativeDBObjectList.size());
                    NativeDBObject nativeDBObject = nativeDBObjectList.get(0);
                    if (nativeDBObject.getPhotoURI() != null) {
                        Bitmap photoBitmap = ImageUtils.getContactBitmapFromURI(BotApplication.getGlobalContext(), Uri.parse(nativeDBObject.getPhotoURI()));
                        if (photoBitmap != null) {

                            Bitmap roundPhotoBitmap = ImageUtils.getCroppedRoundBitmap(photoBitmap, contactImageView.getDrawable().getIntrinsicHeight());
                            //Bitmap photoBitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                            contactImageView.setImageBitmap(roundPhotoBitmap);
                        }
                    }
                }

                contactPhoneNumberTextView.setText(userName);
                session.addSessionEventListener(myAudioCodesSessionEventListener);
            } else {
                finish();
                Log.d(TAG, "RemoteContact null!");
            }
        });

    }

    void updateVideoButton() {
        int hasVideoInt = 0;
        if (session.hasVideo() && !session.isVideoMuted()) {
            hasVideoInt = R.mipmap.call_button_icon_mute_video;
        } else {
            hasVideoInt = R.mipmap.call_button_icon_add_video;
        }
        ((ImageView) addVideoButton).setImageResource(hasVideoInt);
    }

    void updateCallGui() {
        Log.d(TAG, "updateCallGui");
        View videoCallLayout = findViewById(R.id.call_layout_ac_webrtc_video);
        View audioCallLayout = findViewById(R.id.call_layout_audio_call);
        View dtmfCallLayout = findViewById(R.id.call_layout_dtmf_pad);
        View topButtonsLayout = findViewById(R.id.call_layout_top_buttons);
        View llVideo = findViewById(R.id.llVideoButtons);
        View buttonsLayout = findViewById(R.id.call_layout_buttons);

        if (dtmfEnabled) {
            Log.d(TAG, "updateCallGui: DTMF");
            //show dtmf screen
            videoCallLayout.setVisibility(View.GONE);
            llVideo.setVisibility(View.GONE);
            audioCallLayout.setVisibility(View.VISIBLE);
            buttonsLayout.setVisibility(View.VISIBLE);
            dtmfCallLayout.setVisibility(View.VISIBLE);
            topButtonsLayout.setVisibility(View.GONE);
        } else if (session.hasVideo()) {
            Log.d(TAG, "updateCallGui: video");
            //show video screen
            videoCallLayout.setVisibility(View.VISIBLE);
            llVideo.setVisibility(View.VISIBLE);
            audioCallLayout.setVisibility(View.GONE);
            buttonsLayout.setVisibility(View.GONE);
            dtmfCallLayout.setVisibility(View.GONE);
            topButtonsLayout.setVisibility(View.VISIBLE);

            try {
                session.setLocaLRenderPosition(70, 50);
                session.showVideo(CallActivity.this);
            } catch (WebRTCException e) {
                Log.d(TAG, "oops: " + e.getMessage());
            }
        } else {
            Log.d(TAG, "updateCallGui: audio");
            //show call screen
            videoCallLayout.setVisibility(View.GONE);
            llVideo.setVisibility(View.GONE);
            audioCallLayout.setVisibility(View.VISIBLE);
            buttonsLayout.setVisibility(View.VISIBLE);
            dtmfCallLayout.setVisibility(View.GONE);
            topButtonsLayout.setVisibility(View.VISIBLE);
        }
        updateVideoButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session != null && session.getTermination() != null) {
            handleCallTermination(session, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //CallBackHandler.unregisterCallStateChanged(callStateChanged);
        CallBackHandler.unregisterLoginStateChange(loginStateChanged);
        timerHandler.removeCallbacks(runnable);
    }

    @Override
    public void callTerminated(AudioCodesSession session, TerminationInfo terminationInfo) {
        Log.d(TAG, "callTerminated");
        Log.d(TAG, "terminationInfo1: " + terminationInfo.callTermination);
        Log.d(TAG, "terminationInfo2: " + terminationInfo.statusCode);
        Log.d(TAG, "terminationInfo2: " + terminationInfo.pjssipStatusCode);
        Log.d(TAG, "terminationInfo3: " + terminationInfo.reason);
        Log.d(TAG, "terminationInfo4: " + terminationInfo.reasonHeader);
        Log.d(TAG, "terminationInfo5: " + terminationInfo.sipMessage);

        // handleCallTermination(session,terminationInfo);
    }

    @Override
    public void callProgress(AudioCodesSession session) {
        Log.d(TAG, "callProgress CallState: " + session.getCallState());
        if (session.getCallState() == CallState.CONNECTED || session.getCallState() == CallState.HOLD) {
            if (session.getCallState().toString().equalsIgnoreCase(BundleConstants.CONNECTED) && callDuration == 0) {
                timerHandler.postDelayed(runnable, 1000);
            }
            handler.post(() -> {
                endCallButton.setEnabled(true);
                holdButton.setEnabled(true);
            });
        }
    }

    @Override
    public void mediaFailed(AudioCodesSession session) {
        Log.d(TAG, "media failed1");
    }


    @Override
    public void cameraSwitched(boolean frontCamera) {

    }

    @Override
    public void reinviteWithVideoCallback(AudioCodesSession audioCodesSession) {

    }

    @Override
    public void incomingNotify(NotifyEvent notifyEvent, String dtmfValue) {
    }

    public void onNotifyEvent(NotifyEvent notifyEvent, String dtmfValue) {
        Log.d(TAG, "onNotifyEvent: " + notifyEvent);
        switch (notifyEvent) {
            case TALK:
                handler.post(() -> {
                    Log.d(TAG, "Notify resume");
                    if (session.isLocalHold()) {
                        setHold(false);
                    }
                });
                break;
            case HOLD:
                handler.post(() -> {
                    Log.d(TAG, "Notify hold");
                    if (!session.isLocalHold()) {
                        setHold(true);
                    }
                });
                break;
            case DTMF:
                Log.d(TAG, "Notify DTMF");
                Thread thread = new Thread(() -> {
                    DTMF[] dtmfValueArr = getDTMFArrayFromString(dtmfValue);
                    if (dtmfValueArr != null) {
                        for (DTMF dtmf : dtmfValueArr) {
                            if (dtmf != null) {
                                try {
                                    Thread.sleep(ACConfiguration.getConfiguration().getDtmfOptions().duration + 30);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, "incomingNotify dtmfValue: " + dtmf);
                                session.sendDTMF(dtmf);
                            }
                        }
                    }
                });
                thread.start();
                break;
        }
    }

    DTMF[] getDTMFArrayFromString(String dtmfString) {
        if (dtmfString == null) {
            return null;
        }
        String[] dtmfValueArr = dtmfString.split("");
        DTMF[] res = null;
        if (dtmfValueArr.length > 0) {
            res = new DTMF[dtmfValueArr.length - 1];
            int charIndex = 0;
            for (String signalValueArrChar : dtmfValueArr) {
                if (signalValueArrChar == null || signalValueArrChar.equals("")) {
                    continue;
                }
                switch (signalValueArrChar) {
                    case "1":
                        res[charIndex] = DTMF.ONE;
                        break;
                    case "2":
                        res[charIndex] = DTMF.TWO;
                        break;
                    case "3":
                        res[charIndex] = DTMF.THREE;
                        break;
                    case "4":
                        res[charIndex] = DTMF.FOUR;
                        break;
                    case "5":
                        res[charIndex] = DTMF.FIVE;
                        break;
                    case "6":
                        res[charIndex] = DTMF.SIX;
                        break;
                    case "7":
                        res[charIndex] = DTMF.SEVEN;
                        break;
                    case "8":
                        res[charIndex] = DTMF.EIGHT;
                        break;
                    case "9":
                        res[charIndex] = DTMF.NINE;
                        break;
                    case "0":
                        res[charIndex] = DTMF.ZERO;
                        break;
                    case "#":
                        res[charIndex] = DTMF.POUND;
                        break;
                    case "*":
                        res[charIndex] = DTMF.STAR;
                        break;
                }
                charIndex++;
            }
        }
        return res;
    }

    public class MyAudioCodesSessionEventListener implements AudioCodesSessionEventListener {
        @Override
        public void callTerminated(final AudioCodesSession call, TerminationInfo info) {
            CallForegroundService.stopService();
            handler.post(() -> {
                Log.d(TAG, "MyAudioCodesSessionEventListener");
                handleCallTermination(call, info);
            });

        }

        @Override
        public void callProgress(final AudioCodesSession call) {
            Log.d(TAG, "Call state: " + call.getCallState());
            Log.d(TAG, "Call session id: " + call.getSessionID());
            Log.d(TAG, "Call number: " + call.getRemoteNumber().getUserName());
            handler.post(() -> {
                if (call.getCallState() == CallState.RINGING) {
                    Log.d(TAG, "do local ring!");
                    AppUtils.startRingingMP("ringback.wav", true, false, null);
                }
                if (call.getCallState() == CallState.CONNECTED) {
                    AppUtils.stopRingingMP();
                }
                if (call.getSessionID() == session.getSessionID()) {
                    callStateTextView.setText(getString(R.string.call_textview_call_state) + " " + call.getCallState());

                    if (call.getCallState() == CallState.TRANSFER) {
                        if (call.getTransferContact() != null) {
                            transferStateNumberTextView.setText(call.getTransferState() + " - " + call.getTransferContact().getUserName());
                        }
                    } else {
                        transferStateNumberTextView.setText("");
                    }
                    // updateUI();
                }
                updateUI();
            });
        }

        @Override
        public void cameraSwitched(boolean frontCamera) {
            Log.d(TAG, "cameraSwitched: " + frontCamera);
        }

        @Override
        public void reinviteWithVideoCallback(AudioCodesSession audioCodesSession) {
            Log.d(TAG, "reInviteWithVideoCallback name: " + audioCodesSession.getRemoteNumber().getDisplayName() + " userName: " + audioCodesSession.getRemoteNumber().getUserName());
            Log.d(TAG, "hasVideo: " + audioCodesSession.hasVideo());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //audioCodesSession.answer(null, true);
                    try {
                        //audioCodesSession.showVideo(CallActivity.this);
                        updateCallGui();
                    } catch (Exception e) {
                        Log.e(TAG, "error: " + e);
                    }
                }
            });
        }

        @Override
        public void mediaFailed(AudioCodesSession session) {
            Log.d(TAG, "media failed2");
            if (handler != null) {
                handler.post(() -> Toast.makeText(CallActivity.this, "Media failure, trying to reconnect....", Toast.LENGTH_LONG).show());
            }
        }

        @Override
        public void incomingNotify(NotifyEvent notifyEvent, String dtmfValue) {
            Log.d(TAG, "NotifyEvent: " + notifyEvent);
            onNotifyEvent(notifyEvent, dtmfValue);
        }
    }

    void handleCallTermination(AudioCodesSession audioCodesSession, final TerminationInfo terminationInfo) {
        handler.post(() -> {
            AppUtils.stopRingingMP();
            sessionList.remove((Integer) audioCodesSession.getSessionID());
            AudioCodesSession currentSession = session;
            if (sessionList.isEmpty()) {
                WebRTCAudioManager.getInstance().setWebRTcAudioRouteListener(null);
                if (AppUtils.getBotClient() != null && AppUtils.getEventModel() != null) {
                    EventModel eventModel = AppUtils.getEventModel();
                    eventModel.getMessage().setType(BundleConstants.CALL_AGENT_WEBRTC_TERMINATED);
                    AppUtils.getBotClient().sendMessage(eventModel.getMessage());
                }
                AudioCodesUA.getInstance().logout();

                finish();
            } else {
                switchCallButton.setEnabled(false);
                for (Integer session1 : sessionList) {
                    Log.d(TAG, "session1: id " + session1);
                }
                try {
                    session = AudioCodesUA.getInstance().getSession(sessionList.get(0));
                } catch (Exception e) {
                    Log.d(TAG, "oops: " + e.getMessage());
                }
                if (session == null) {
                    session = AudioCodesUA.getInstance().getSession(0);
                }
                if (audioCodesSession.getTermination() != CallTermination.TERMINATED_TRANSFER) {
                    if (currentSession.getSessionID() == audioCodesSession.getSessionID() && currentSession.getTransferState() == CallTransferState.NONE) {
                        session.hold(false);
                    }
                }

                if (AppUtils.getBotClient() != null && AppUtils.getEventModel() != null) {
                    EventModel eventModel = AppUtils.getEventModel();
                    eventModel.getMessage().setType(BundleConstants.CANCEL_AGENT_WEBRTC);
                    AppUtils.getBotClient().sendMessage(eventModel.getMessage());
                }
                updateUI();
            }
        });
    }

    class MyWebRTcAudioRouteListener implements WebRTCAudioRouteListener {

        @Override
        public void audioRoutesChanged(List<WebRTCAudioManager.AudioRoute> audioRouteList) {
            Log.d(TAG, "ROUTS: " + audioRouteList);
        }

        @Override
        public void currentAudioRouteChanged(WebRTCAudioManager.AudioRoute newAudioRoute) {
            Log.d(TAG, "currentAudioRouteChanged: " + newAudioRoute);
            route = newAudioRoute;
            if (audioRouteButton != null) {
                int audioRouteInt = 0;
                if (route == WebRTCAudioManager.AudioRoute.SPEAKER_PHONE) {
                    audioRouteInt = R.drawable.ic_audio_speaker_on;
                } else if (route == WebRTCAudioManager.AudioRoute.EARPIECE) {
                    audioRouteInt = R.drawable.ic_audio_off;
                } else {
                    audioRouteInt = R.mipmap.call_button_icon_bluetooth;
                }

                ((ImageView) audioRouteButton).setImageResource(audioRouteInt);

            }
        }
    }
}
