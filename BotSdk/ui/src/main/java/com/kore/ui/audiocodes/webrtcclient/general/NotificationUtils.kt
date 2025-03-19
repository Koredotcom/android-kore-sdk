package com.kore.ui.audiocodes.webrtcclient.general

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.graphics.toColorInt
import com.audiocodes.mv.webrtcsdk.session.CallState
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.activities.CallActivity
import com.kore.ui.audiocodes.webrtcclient.activities.IncomingCallActivity
import com.kore.ui.audiocodes.webrtcclient.activities.SplashActivity
import com.kore.ui.audiocodes.webrtcclient.general.ImageUtils.getCroppedRoundBitmap
import com.kore.ui.audiocodes.webrtcclient.general.Log.d

object NotificationUtils {
    private const val TAG = "NotificationUtils"
    private const val NOTIFICATION_ID = 271
    const val NOTIFICATION_SERVICE_ID = 272
    private const val NOTIFICATION_CALL_ID = 273
    private var CHANNEL_NAME: String? = null
    private var CHANNEL_ID: String? = null
    private var CHANNEL_ID_CALL: String? = null
    private var CHANNEL_NAME_CALL: String? = null
    private var notificationManager: NotificationManager? = null
    private fun getNotificationManager(context: Context): NotificationManager {
        if (notificationManager == null) {
            val name = context.getString(R.string.app_name)
            CHANNEL_ID = name + "_id"
            CHANNEL_NAME = name + "_name"
            CHANNEL_ID_CALL = name + "_id_call"
            CHANNEL_NAME_CALL = name + "_name_call"
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_LOW
            var mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationManager?.createNotificationChannel(mChannel)
            mChannel = NotificationChannel(CHANNEL_ID_CALL, CHANNEL_NAME_CALL, NotificationManager.IMPORTANCE_HIGH)
            val aa = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            //mChannel.enableVibration(true);
            mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), aa)
            notificationManager?.createNotificationChannel(mChannel)
        }
        return notificationManager!!
    }

    fun createAppNotification(context: Context, acManager: ACManager) {
        val title = context.getString(R.string.app_name)
        var text = context.getString(R.string.notification_text_prefix)
        val image: Int
        if (acManager.isRegisterState) {
            text += " " + context.getString(R.string.notification_text_connected)
            image = R.mipmap.indicator_green_l
        } else {
            text += " " + context.getString(R.string.notification_text_disconnected)
            image = R.mipmap.indicator_red_l
        }
        var cls: Class<*> = SplashActivity::class.java
        if (acManager.activeSession != null) {
            if (acManager.activeSession!!.callState == CallState.RINGING) {
                cls = IncomingCallActivity::class.java
            } else if (acManager.activeSession!!.callState != CallState.NULL) {
                cls = CallActivity::class.java
            }
        }
        d(TAG, "use activity: $cls")
        createNotification(context, title, text, image, cls)
    }

    private fun createNotification(context: Context, title: String?, text: String?, image: Int, cls: Class<*>?) {
        getNotificationManager(context)
        val notificationCompat = NotificationCompat.Builder(context, CHANNEL_ID!!)
        notificationCompat.setContentTitle(title)
        notificationCompat.setContentText(text)
        notificationCompat.setAutoCancel(true)
        notificationCompat.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        notificationCompat.setOngoing(true)
        notificationCompat.setAutoCancel(false)
        notificationCompat.setOnlyAlertOnce(false)
        notificationCompat.priority = Notification.PRIORITY_HIGH
        notificationCompat.setSmallIcon(image)
        val intent = Intent(context, cls)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        notificationCompat.setContentIntent(pendingIntent)
        val notification = notificationCompat.build()
        getNotificationManager(context).notify(NOTIFICATION_ID, notification)
    }

    @JvmStatic
    fun removeServiceNotification(context: Context) {
        getNotificationManager(context).cancel(NOTIFICATION_SERVICE_ID)
    }

    @JvmStatic
    fun addServiceNotification(context: Context): Notification {
        val notificationCompat = NotificationCompat.Builder(context, CHANNEL_ID!!)
        notificationCompat.setAutoCancel(true)
        notificationCompat.setSmallIcon(R.mipmap.desktop_icon)
        val notifyIntent = Intent(context, CallActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        val contentIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_SERVICE_ID,
            notifyIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationCompat.setContentIntent(contentIntent)
        notificationCompat.setContentText(context.getString(R.string.notification_text_in_call))
        val notification = notificationCompat.build()
        getNotificationManager(context).notify(NOTIFICATION_SERVICE_ID, notification)
        return notification
    }

    @JvmStatic
    fun removeAllNotifications(context: Context) {
        getNotificationManager(context).cancel(NOTIFICATION_ID)
        removeServiceNotification(context)
    }

    fun removeCallNotification(context: Context) {
        d(TAG, "removeCallNotification")
        getNotificationManager(context).cancel(NOTIFICATION_CALL_ID)
    }

    fun createCallNotification(context: Context, callerId: String, sessionId: Int, video: Boolean) {
        d(TAG, "pushCallNotification $sessionId")
        val c: Class<*> = IncomingCallActivity::class.java
        val intent = getNotificationIntent(context, c, sessionId, 5)
        var videoIntent: Intent? = null
        if (video) {
            videoIntent = getNotificationIntent(context, c, sessionId, 2)
        }
        pushCallNotificationBase(
            context,
            callerId, intent,
            getNotificationIntent(context, c, sessionId, 1),
            getNotificationIntent(context, c, sessionId, 0),
            videoIntent
        )
        val timerThread = Thread(Runnable {
            val ringingTimeOutMilliSec = IncomingCallActivity.INCOMING_CALL_RINGING_TIMEOUT_SEC * 1000
            d(TAG, "Start ringing timout timer for: $ringingTimeOutMilliSec")
            try {
                Thread.sleep(ringingTimeOutMilliSec.toLong())
                val session = AudioCodesUA.getInstance().getSession(sessionId)
                if (session != null && session.callState == CallState.RINGING) {
                    session.reject(null)
                }
            } catch (e: Exception) {
                d(TAG, "startRingingTimer interapted")
                return@Runnable
            }
        })
        timerThread.start()
    }

    private fun getNotificationIntent(context: Context, activity: Class<*>, sessionId: Int, extra: Int): Intent {
        val intent = Intent(context, activity)
        intent.putExtra(IncomingCallActivity.INTENT_ANSWER_TAG, extra)
        intent.putExtra(IncomingCallActivity.SESSION_ID, sessionId)
        return intent
    }

    private fun pushCallNotificationBase(
        context: Context,
        callerId: String,
        fullScreenIntent: Intent,
        answerIntent: Intent,
        declineIntent: Intent,
        videoIntent: Intent?
    ) {
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pendingAnswerIntent = PendingIntent.getActivity(
            context, 1,
            answerIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pendingDeclineIntent = PendingIntent.getActivity(
            context, 2,
            declineIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        var pendingVideoIntent: PendingIntent? = null
        if (videoIntent != null) {
            pendingVideoIntent = PendingIntent.getActivity(
                context, 3,
                videoIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        getNotificationManager(context)
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_view)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_CALL!!)
            .setSmallIcon(R.mipmap.indicator_green)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setTimeoutAfter((IncomingCallActivity.INCOMING_CALL_RINGING_TIMEOUT_SEC * 1000).toLong())
            .setColorized(true)
            .setCustomContentView(remoteViews)
        val buttonTextColor = "#ffffff".toColorInt()
        val titleTextColor = "#000000".toColorInt()
        remoteViews.setOnClickPendingIntent(R.id.accept, pendingAnswerIntent)
        remoteViews.setTextColor(R.id.accept, buttonTextColor)
        remoteViews.setTextViewText(R.id.accept, context.resources.getString(R.string.incoming_call_button_answer_text))
        remoteViews.setOnClickPendingIntent(R.id.decline, pendingDeclineIntent)
        remoteViews.setTextColor(R.id.decline, buttonTextColor)
        remoteViews.setTextViewText(R.id.decline, context.resources.getString(R.string.incoming_call_button_decline_text))
        remoteViews.setTextViewText(R.id.title, context.getString(R.string.incoming_call_textview_call_state))
        remoteViews.setTextColor(R.id.title, titleTextColor)
        remoteViews.setTextViewText(R.id.user, callerId)
        remoteViews.setTextColor(R.id.user, titleTextColor)
        var defBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.default_contact_list_picture)
        defBitmap = getCroppedRoundBitmap(defBitmap, 40)
        remoteViews.setImageViewBitmap(R.id.contact_picture_iv, defBitmap)
        if (pendingVideoIntent != null) {
            remoteViews.setOnClickPendingIntent(R.id.accept_video, pendingVideoIntent)
            remoteViews.setViewVisibility(R.id.accept_video, View.VISIBLE)
            remoteViews.setTextViewText(
                R.id.accept_video,
                context.resources.getString(R.string.incoming_call_button_answer_video_notification_text)
            )
            remoteViews.setTextColor(R.id.accept_video, buttonTextColor)
        } else {
            remoteViews.setViewVisibility(R.id.accept_video, View.GONE)
        }
        val incomingCallNotification = notificationBuilder.build()
        getNotificationManager(context).notify(NOTIFICATION_CALL_ID, incomingCallNotification)
    }
}
