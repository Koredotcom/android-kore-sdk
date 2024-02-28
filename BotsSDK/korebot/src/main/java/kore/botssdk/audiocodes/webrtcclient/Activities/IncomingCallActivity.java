package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.audiocodes.mv.webrtcsdk.session.AudioCodesSession;
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSessionEventListener;
import com.audiocodes.mv.webrtcsdk.session.NotifyEvent;
import com.audiocodes.mv.webrtcsdk.session.TerminationInfo;
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA;
import com.audiocodes.mv.webrtcsdk.useragent.WebRTCException;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.ImageUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.NotificationUtils;
import kore.botssdk.audiocodes.webrtcclient.Login.LogoutManager;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBManager;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBObject;

public class IncomingCallActivity extends BaseAppCompatActivity implements AudioCodesSessionEventListener {

    private static final String TAG = "IncomingCallActivity" ;
    public static final String SESSION_ID = "sessionID";
    public static final int INCOMING_CALL_RINGING_TIMEOUT_SEC = 30;

    public static final String INTENT_ANSWER_TAG = "ANSWER_TAG";

    private static Thread timerThread = null;
    private PowerManager.WakeLock wakeLock;
    //private Handler handler;

    //private TextView callStateTV;

    private AudioCodesSession session;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NotificationUtils.removeCallNotification();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming_call_activity);

        Log.d(TAG,"onCreate");
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
        Log.d(TAG, "sessionIndex: " + sessionIndex);
        session = AudioCodesUA.getInstance().getSession(sessionIndex);
        int answer = getIntent().getIntExtra(INTENT_ANSWER_TAG, -1);
        Log.d("aaa", "ANSWER: " + answer);
        if(answer == 0){
            decline();
        }
        else if(answer == 1){
            answer();
        }
        else if(answer == 2){
            answerWithVideo();
        }
        else{
            unlockScreen();
            initGui();
            startRingingTimer();
            playRingtone();
        }
    }

    private void unlockScreen()
    {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //unloack screen
//        Log.d(TAG, "unlockScreen");
//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
//        wakeLock.acquire(INCOMING_CALL_RINGING_TIMEOUT_SEC);
    }

    private void lockScreen()
    {
        //unloack screen
//        if(wakeLock!=null && wakeLock.isHeld()) {
//            Log.d(TAG, "lockScreen");
//            wakeLock.release();
//        }
    }

    private void initGui()
    {
        if (session==null)
        {
            startNextActivity(MainActivity.class);
            finish();
            return;
        }
        // TextView callStateTextView = (TextView) findViewById(R.id.incoming_call_textview_call_state);
        TextView contactNameTextView = (TextView) findViewById(R.id.incoming_call_textview_contact_name);
        TextView contactPhoneNumberTextView = (TextView) findViewById(R.id.incoming_call_textview_contact_number);
        ImageView contactImageView = (ImageView) findViewById(R.id.incoming_call_imageView_contact_image);


        //List<NativeContactObject> nativeContactObjectList = NativeContactUtils.getContactListByPhoneNumber(session.getRemoteNumber().getUserName());
        List<NativeDBObject> nativeDBObjectList = NativeDBManager.getContactList(NativeDBManager.QueryType.BY_PHONE_AND_SIP, session.getRemoteNumber().getUserName());//  .getContactListByPhoneNumber(session.getRemoteNumber().getUserName());

        String displayName=session.getRemoteNumber().getDisplayName();
        String userName=session.getRemoteNumber().getUserName();
        Log.d(TAG, "check user name: "+userName+" displayName:"+displayName);
        if (nativeDBObjectList!=null && nativeDBObjectList.size()>0)
        {
            Log.d(TAG, "check user nativeDBObjectList: " +nativeDBObjectList.size());
            NativeDBObject nativeDBObject = nativeDBObjectList.get(0);
            if (nativeDBObject.getPhotoURI() != null) {
                Bitmap photoBitmap = ImageUtils.getContactBitmapFromURI(this, Uri.parse(nativeDBObject.getPhotoURI()));
                if (photoBitmap != null) {

                    Bitmap roundPhotoBitmap = ImageUtils.getCroppedRoundBitmap(photoBitmap, contactImageView.getDrawable().getIntrinsicHeight());
                    //Bitmap photoBitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                    contactImageView.setImageBitmap(roundPhotoBitmap);
                }
            }
            displayName=nativeDBObject.getDisplayName();
        }

        contactNameTextView.setText(displayName);
        contactPhoneNumberTextView.setText(userName);


        View answerButton = findViewById(R.id.incoming_call_button_answer);
        View answerVideoButton = findViewById(R.id.incoming_call_button_answer_video);
        View declineButton = findViewById(R.id.incoming_call_button_decline);



        answerVideoButton.setVisibility(session.hasVideo()? View.VISIBLE: View.GONE);


        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer();
            }
        });

        answerVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerWithVideo();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline();
            }
        });

        session.addSessionEventListener(this);

        View videoCallLayout = findViewById(R.id.incoming_call_layout_ac_webrtc_video);
        View audioCallLayout = findViewById(R.id.incoming_call_layout_audio_call);

        if (session.hasVideo()) {
            videoCallLayout.setVisibility(View.VISIBLE);
            contactImageView.setVisibility(View.GONE);
            try {
                session.showVideo(this);
            } catch (WebRTCException e) {
                Log.d(TAG, "oops: " + e.getMessage());
            }
        }
        else
        {
            videoCallLayout.setVisibility(View.GONE);
            audioCallLayout.setVisibility(View.VISIBLE);
        }
    }

    public void decline(){
        Log.d(TAG, "decline");
        stopRingtone();
        stopRingingTimer();
        session.reject(null);
        finish();
    }

    public void answer(){
        stopRingtone();
        stopRingingTimer();
        if (session==null)
        {
            startNextActivity(MainActivity.class);
            finish();
            return;
        }
        Intent callIntent = new Intent(IncomingCallActivity.this, CallActivity.class);
        callIntent.putExtra(CallActivity.SESSION_ID, session.getSessionID());
        startActivity(callIntent);
        session.answer(null,false);
        finish();
    }

    public void answerWithVideo(){
        stopRingtone();
        stopRingingTimer();
        if (session==null)
        {
            startNextActivity(MainActivity.class);
            finish();
            return;
        }
        Intent callIntent = new Intent(IncomingCallActivity.this, CallActivity.class);
        callIntent.putExtra(CallActivity.SESSION_ID, session.getSessionID() );
        startActivity(callIntent);
        session.answer(null,true);
        finish();
    }

    private void playRingtone()
    {
        Log.d(TAG, "playRingtone");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(BotApplication.getGlobalContext(), uri);
        ringtone.play();
    }

    private void stopRingtone()
    {
        if(ringtone!=null && ringtone.isPlaying())
        {
            ringtone.stop();
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        lockScreen();
        stopRingtone();
    }

    @Override
    public void callTerminated(AudioCodesSession call, TerminationInfo terminationInfo) {
        handleCallTermination(call,terminationInfo);
    }

    @Override
    public void callProgress(final AudioCodesSession call) {
    }

    @Override
    public void cameraSwitched(boolean frontCamera) {

    }

    @Override
    public void reinviteWithVideoCallback(AudioCodesSession audioCodesSession) {

    }

    @Override
    public void mediaFailed(AudioCodesSession session) {
        Log.d(TAG, "media failed2");
    }

    @Override
    public void incomingNotify(NotifyEvent notifyEvent, String dtmfValue) {

        android.util.Log.d(TAG, "incomingNotify: "+notifyEvent);
        switch (notifyEvent) {
            case TALK:

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        android.util.Log.d(TAG, "Notify answer call");
                        answer();
                    }
                });
                break;
            case HOLD:
                break;
        }

    }


    private void handleCallTermination(final AudioCodesSession session, final TerminationInfo terminationInfo) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Checking for termination");
                stopRingtone();
                stopRingingTimer();
                if(session!=null) {
                    Toast.makeText(IncomingCallActivity.this, "Call terminated: " + session.getCallState(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });


    }

    private void startRingingTimer() {

        Log.d(TAG,"Start ringing timer");
        if (timerThread==null) {
            timerThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    int ringingTimeOutMiliSec= INCOMING_CALL_RINGING_TIMEOUT_SEC*1000;
                    Log.d(TAG,"Start ringing timout timer for: "+ringingTimeOutMiliSec);
                    try {
                        Thread.sleep(ringingTimeOutMiliSec);
                    } catch (InterruptedException e) {
                        Log.d(TAG,"startRingingTimer interapted");
                        return;
                    }
                    session.reject(null);
                    handleCallTermination(null, null);
                    //decline();
                }
            });
            timerThread.start();
        }
    }
    private void stopRingingTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread=null;
        }
    }
}
