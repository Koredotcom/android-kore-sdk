package kore.botssdk.audiocodes.webrtcclient.General;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.audiocodes.mv.webrtcsdk.im.InstanceMessageStatus;
import com.audiocodes.mv.webrtcsdk.log.LogLevel;
import com.audiocodes.mv.webrtcsdk.session.ACCallStatistics;
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSession;
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSessionEventListener;
import com.audiocodes.mv.webrtcsdk.session.CallState;
import com.audiocodes.mv.webrtcsdk.session.DTMFOptions;
import com.audiocodes.mv.webrtcsdk.session.InfoAlert;
import com.audiocodes.mv.webrtcsdk.session.NotifyEvent;
import com.audiocodes.mv.webrtcsdk.session.RemoteContact;
import com.audiocodes.mv.webrtcsdk.session.TerminationInfo;
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport;
import com.audiocodes.mv.webrtcsdk.useragent.ACConfiguration;
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesEventListener;
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA;
import com.audiocodes.mv.webrtcsdk.useragent.WebRTCException;

import org.webrtc.PeerConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Activities.CallActivity;
import kore.botssdk.audiocodes.webrtcclient.Activities.IncomingCallActivity;
import kore.botssdk.audiocodes.webrtcclient.Callbacks.CallBackHandler;
import kore.botssdk.audiocodes.webrtcclient.Structure.CallEntry;
import kore.botssdk.audiocodes.webrtcclient.Structure.SipAccount;

public class ACManager implements AudioCodesEventListener {

    private static final String TAG = "ACManager";

    private static ACManager instance;
    private boolean registerState;

    public static synchronized ACManager getInstance() {
        if (instance == null) {
            instance = new ACManager();
        }
        return instance;
    }

    public void startLogin() {
        startLogin(Prefs.getAutoLogin(), Prefs.getDisconnectBrokenConnection());
    }

    public void startLogin(boolean autologin, boolean disconnectCall) {
        Log.d(TAG, "startLogin");
        boolean loginit = false;
        AudioCodesUA.getInstance().disconnectOnBrokenConnection(disconnectCall);
        try {
            initACUA();
            loginit = true;
        } catch (Exception e) {
            Log.d(TAG, "can't set log level");
        }
        initWebRTC(Prefs.getSipAccount());
        Prefs.setAutoLogin(autologin);
        Prefs.setDisconnectBrokenConnection(disconnectCall);
        try {
            AudioCodesUA.getInstance().login(BotApplication.getGlobalContext().getApplicationContext(), autologin);
            if (!loginit) {
                initACUA();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error in login: " + e);
            Toast.makeText(BotApplication.getGlobalContext(), "Error in login", Toast.LENGTH_SHORT).show();
        }
    }

    public void initACUA() {
        AudioCodesUA.getInstance().setLogger(new LogI());
        AudioCodesUA.getInstance().setLogLevel(LogLevel.VERBOSE);
    }

    public void startLogout(){
        Log.d(TAG, "startLogout");
        try {
            AudioCodesUA.getInstance().logout();
        } catch (Exception e) {
            Log.d(TAG, "Error in logout");
        }
    }

    public void initWebRTC(SipAccount sipAccount) {
        String proxy=sipAccount.getProxy();
        int port = sipAccount.getPort();
        String domain = sipAccount.getDomain();
        Transport transport = sipAccount.getTransport();
        String username = sipAccount.getUsername();
        String password = sipAccount.getPassword();
        String displayName =  sipAccount.getDisplayName();

        Log.d(TAG,"sipAccount: "+sipAccount.toString());

        //   AudioCodesUA.getInstance().setContactRewrite(true);

        AudioCodesUA.getInstance().setServerConfig(proxy,port,domain,transport,new ArrayList<PeerConnection.IceServer>());
       // AudioCodesUA.getInstance().setServerConfig(proxy,port,domain,transport, null);
        AudioCodesUA.getInstance().setAllowHeader(null);

        Log.d(TAG, "setVerifyServer: "+ true);
        AudioCodesUA.getInstance().setVerifyServer(true);

        boolean useCustomPemFile = false;
        if (useCustomPemFile) {
            // make sure context is not null at this point
            AudioCodesUA.getInstance().setCaCertFilePath(copyPemfile(BotApplication.getGlobalContext()));
        }

        AudioCodesUA.getInstance().setAccount(username,password,displayName);

        AudioCodesUA.getInstance().setListener(this);
        
        updateWebRTCConfig();

    }

    public void updateWebRTCConfig()
    {
        //set dtmf settings
        DTMFOptions.DTMFMethod dtmfMethod = Prefs.getDTMFType();
        DTMFOptions dtmfOptions = new DTMFOptions();
        dtmfOptions.dtmfMethod = dtmfMethod;
        Log.d(TAG,"use dtmfMethod: "+dtmfMethod);
        ACConfiguration.getConfiguration().setDtmfOptions(dtmfOptions);

        Log.d(TAG,"use isAutoRedirect: "+ Prefs.isAutoRedirect());
        ACConfiguration.getConfiguration().setAutomaticCallOnRedirect(Prefs.isAutoRedirect());
        AudioCodesUA.getInstance().setVideoCodecHardwareAceleration(Prefs.isVideoHardware());
        RemoteContact remoteContact = new RemoteContact();
        remoteContact.setScheme(null);
        remoteContact.setDisplayName(Prefs.getRedirectCallUser());
        remoteContact.setUserName(Prefs.getRedirectCallUser());
        remoteContact.setDomain(Prefs.getSipAccount().getDomain());

        Log.d(TAG,"use isRedirectCall: "+ Prefs.isRedirectCall()+" with RedirectCallUser: "+ Prefs.getRedirectCallUser());
        ACConfiguration.getConfiguration().setRedirect(Prefs.isRedirectCall(), remoteContact);
    }

    private String copyPemfile(Context context)
    {
        // this file should be in assets and provided by the user of the SDK
        String filePath = "cacert.pem";

        File cacheDir = new File(context.getCacheDir(), filePath);
        try {
            InputStream in = context.getAssets().open(filePath);

            FileOutputStream out =new FileOutputStream(cacheDir.getPath());

            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.close();
            in.close();

        } catch (Exception e) {
           Log.d(TAG ,"oops: "+ e.getMessage());
        }
        return cacheDir.getPath();
    }


    @Override
    public void loginStateChanged(boolean isLogin, int code, String cause) {
        Log.e(TAG,"loginStateChanged isLogin: "+isLogin+" cause: "+cause);
        Log.e(TAG,"loginStateChanged currentState: "+ isLogin+" prevState: "+registerState);
        if(registerState != isLogin || !Prefs.getAutoLogin()) {
            registerState = isLogin;
            CallBackHandler.loginStateChange(registerState);
            NotificationUtils.createAppNotification();

        }
    }

    public AudioCodesSession getActiveSession()
    {
        AudioCodesSession audioCodesSession=null;
        ArrayList<AudioCodesSession> audioCodesSessionArrayList = AudioCodesUA.getInstance().getSessionList();
        for (AudioCodesSession session: audioCodesSessionArrayList) {
            if (session.getCallState()!=null)
            {
                audioCodesSession = session;
                break;
            }
        }
        //AudioCodesSession audioCodesSession = AudioCodesUA.getInstance().getSession(0);
        //return audioCodesSession;
        return audioCodesSession;
    }

    public ArrayList<AudioCodesSession> getSessionList()
    {
        return AudioCodesUA.getInstance().getSessionList();
    }

    public boolean isAllredyInActiveCall()
    {
        //return getActiveSession()!=null;
        return getActiveSession()!=null && getActiveSession().getCallState()!= CallState.NULL;
//        switch (getActiveSession().getCallState())
//        {
//            case NULL:
//            case CONNECTING:
//            case CALLING:
//            case CONNECTED:
//            case RINGING:
//            case HOLD:
//            case CONFERENCE_CONNECTING:
//            case CONFERENCE_CONNECTED:
//        }
    }

    public void callNumber(String callNumber)
    {
        callNumber(callNumber, false);
    }
    public void callNumber(String callNumber, boolean videoCall)
    {
        try {
            AppUtils.saveVolumeSettings(true);
            AppUtils.setLastCallVolumeSettings();
            AppUtils.setSpeaker(true);
            Log.d(TAG, "start callNumber: " +callNumber+ " isVideoCall: "+videoCall);
            RemoteContact contact= new RemoteContact();
            contact.setUserName(callNumber);
            contact.setDisplayName(callNumber);
            AudioCodesSession session = AudioCodesUA.getInstance().call(contact,videoCall, null );
            Intent callIntent = new Intent(BotApplication.getGlobalContext(), CallActivity.class);

            session.addSessionEventListener(audioCodesSessionEventListener);

            Log.d(TAG, "callNumber startActivity" );
            callIntent.putExtra(CallActivity.SESSION_ID, session.getSessionID() );
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BotApplication.getGlobalContext().startActivity(callIntent);
        } catch (WebRTCException e) {
            Log.d(TAG, "oops: " + e.getMessage());
        }
    }



    @Override
    public void incomingCall(AudioCodesSession call, InfoAlert infoAlert) {
        boolean curCallConnected = false;
        try {
            Log.d(TAG, "Incoming call");
            Log.d(TAG, "Remote user: " + call.getRemoteNumber().getUserName());
            Log.d(TAG, "current  getActiveSession().getCallState(): " +  getActiveSession().getCallState());
            curCallConnected = getActiveSession().getCallState()== CallState.CONNECTED;
            Log.d(TAG, "current is connected: " + curCallConnected);

            AppUtils.saveVolumeSettings(true);
            AppUtils.setLastCallVolumeSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (curCallConnected && call.getRemoteNumber().getUserName().equals(getActiveSession().getRemoteNumber().getUserName()) && call.getRemoteNumber().getDisplayName().equals(getActiveSession().getRemoteNumber().getDisplayName()))
        {
            Log.d(TAG, "this is the second instance of the same call, reject it");
            call.reject(null);
            return;
        }
        if (infoAlert!=null && infoAlert.autoAnswer)
        {
            android.util.Log.d(TAG, "InfoAlert autoAnswer: " + infoAlert.autoAnswer);
            if (infoAlert.delay>0)
            {
                //you can add a different auto answer protocol here if a long delay is needed
            }
            else
            {

                //answer call immediately, without showing incoming call GUI
                Intent callIntent = new Intent(BotApplication.getGlobalContext(), CallActivity.class);

                callIntent.putExtra(CallActivity.SESSION_ID, call.getSessionID() );
                BotApplication.getGlobalContext().startActivity(callIntent);

                call.answer(null, false);
                return;
            }
        }
        call.addSessionEventListener(audioCodesSessionEventListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !curCallConnected) {

            boolean video = AudioCodesUA.getInstance().getSession(call.getSessionID()).hasVideo();
            NotificationUtils.createCallNotification(call.getRemoteNumber().getDisplayName(), call.getSessionID(), video);
            AudioCodesSession session = AudioCodesUA.getInstance().getSession(call.getSessionID());
            session.addSessionEventListener(new AudioCodesSessionEventListener() {
                @Override
                public void callTerminated(AudioCodesSession audioCodesSession, TerminationInfo info) {
                    NotificationUtils.removeCallNotification();
                }
                @Override
                public void callProgress(AudioCodesSession audioCodesSession) {}
                @Override
                public void cameraSwitched(boolean b) {}
                @Override
                public void reinviteWithVideoCallback(AudioCodesSession audioCodesSession) { }
                @Override
                public void mediaFailed(AudioCodesSession session) {
                    Log.d(TAG, "media failed3");
                }
                @Override
                public void incomingNotify(NotifyEvent notifyEvent, String s) {
                    android.util.Log.d(TAG, "incomingNotify: "+notifyEvent);

                    switch (notifyEvent) {
                        case TALK:
                            android.util.Log.d(TAG, "Notify answer call");

                            NotificationUtils.removeCallNotification();
                            Intent incomingCallIntent = new Intent(BotApplication.getGlobalContext(), IncomingCallActivity.class);
                            incomingCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            incomingCallIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            incomingCallIntent.putExtra(IncomingCallActivity.SESSION_ID, call.getSessionID());
                            incomingCallIntent.putExtra(IncomingCallActivity.INTENT_ANSWER_TAG, 1);
                            BotApplication.getGlobalContext().startActivity(incomingCallIntent);
                            session.removeSessionEventListener(this);
                            break;
                        case HOLD:
                            break;
                    }

                }
            });
        }
        else {
            Intent incomingCallIntent = new Intent(BotApplication.getGlobalContext(), IncomingCallActivity.class);
            incomingCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            incomingCallIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            incomingCallIntent.putExtra(IncomingCallActivity.SESSION_ID, call.getSessionID());
            BotApplication.getGlobalContext().startActivity(incomingCallIntent);
        }
    }

    @Override
    public void onIncomingInstantMessage(RemoteContact remoteContact, String message) {
        Log.d(TAG, "onInstantMessage");
        Log.d(TAG, remoteContact.getUserName() + ", " + message);
        CallBackHandler.onNewMessage(remoteContact.getUserName(), message);
    }

    @Override
    public void onInstantMessageStatus(InstanceMessageStatus instanceMessageStatus, long ID) {
        Log.d(TAG, "onInstantMessageStatus " + ID + ": " + instanceMessageStatus);
        CallBackHandler.onMessageStatus(instanceMessageStatus, ID);
    }

    public boolean isRegisterState() {
        return registerState;
    }

    AudioCodesSessionEventListener audioCodesSessionEventListener = new AudioCodesSessionEventListener() {
        @Override
        public void callTerminated(AudioCodesSession session, TerminationInfo info) {

            Log.d(TAG, "callTerminated name: "+session.getRemoteNumber().getDisplayName()+" userName: "+session.getRemoteNumber().getUserName());

            //Save current level volume
            AppUtils.saveVolumeSettings(true);
            //restore previous volume settings
            AppUtils.restorePrevVolumeSettings();

            //save call to recents
            saveCallHistory(session);
            //save statistics
            if (session!=null) {
                ACCallStatistics acCallStatistics = session.getStats();
                Prefs.setCallStats(acCallStatistics);
                Log.d(TAG, "ACCallStatistics: " + acCallStatistics);
            }

            //CallBackHandler.callStateChanged(CallState.NULL);
            //session.getCallState()
            //BotApplication.getDataBase().addEntry(new CallEntry("Avi", 1512997724646L, CallEntry.CallType.NOT_ANSWERED));
        }

        private void saveCallHistory(AudioCodesSession session)
        {
            CallEntry callEntry = new CallEntry();
            callEntry.setContactName(session.getRemoteNumber().getDisplayName());
            callEntry.setContactNumber(session.getRemoteNumber().getUserName());

            long callStartTime = session.getCallStartTime();
            if(callStartTime==0) {
                callStartTime= new Date().getTime();
            }
            callEntry.setStartTime(callStartTime);

            long callDuration = session.duration();
            if(callDuration>0) {
                callDuration = callDuration*1000;//from sec to milisec
            }

            CallEntry.CallType callType = CallEntry.CallType.OUTGOING;
            if(!session.isOutgoing())
            {
                if(callDuration>0) {
                    callType = CallEntry.CallType.INCOMING;
                }
                else
                {
                    callType = CallEntry.CallType.MISSED;
                }
            }
            callEntry.setCallType(callType);

            callEntry.setDuration( callDuration);
            BotApplication.getDataBase().addEntry(callEntry);
        }

        @Override
        public void callProgress(AudioCodesSession session) {
            Log.d(TAG, "callProgress name: "+session.getRemoteNumber().getDisplayName()+" userName: "+session.getRemoteNumber().getUserName());
        }

        @Override
        public void cameraSwitched(boolean frontCamera) {
            Log.d(TAG, "cameraSwitched isfrontCamera: "+frontCamera);

        }

        @Override
        public void mediaFailed(AudioCodesSession session) {
            Log.d(TAG, "media failed4");
        }

        @Override
        public void incomingNotify(NotifyEvent notifyEvent, String s) {

        }

        @Override
        public void reinviteWithVideoCallback(AudioCodesSession audioCodesSession) {
            Log.d(TAG, "reinviteWithVideoCallback name: "+audioCodesSession.getRemoteNumber().getDisplayName()+" userName: "+audioCodesSession.getRemoteNumber().getUserName());
            //audioCodesSession.answer(null, true);
        }

    };

    public void sendInstantMessage(String user, String message)
    {
        Log.d(TAG, "sendInstantMessage: "+ user);
        RemoteContact remoteContact = new RemoteContact();
        remoteContact.setUserName(user);
        remoteContact.setDisplayName(user);
        long messageID = AudioCodesUA.getInstance().sendInstantMessage(remoteContact, message);
        Log.d(TAG, "message ID " + messageID);
    }

}
