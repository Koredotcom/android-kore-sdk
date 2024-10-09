package com.kore.ui.audiocodes.oauth

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import com.kore.common.utils.LogUtils
import com.kore.ui.audiocodes.oauth.HttpManager.getConnection
import com.kore.ui.audiocodes.oauth.HttpManager.make
import com.kore.ui.audiocodes.oauth.HttpManager.setBody
import org.json.JSONObject
import java.util.Locale

class OAuthManager private constructor() {
    private var active = false
    private var eventsCallback: EventsCallback? = null
    private lateinit var context: Context
    val isEnabled: Boolean
        get() = getValue(ACCESS_TOKEN_KEY) != null

    fun initialize(context: Context, eventsCallback: EventsCallback?) {
        this.context = context
        LogUtils.d(TAG, "initialize")
        this.eventsCallback = eventsCallback
        sharedPreferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).apply { editorSP = edit() }
        checkToken()
    }

    fun setUrl(url: String?) {
        putValue(URL_KEY, url)
    }

    fun authorize(context: Context, userName: String?, password: String?, loginCallback: LoginCallback?) {
        this.context = context
        if (userName != null && password != null) {
            putValue(REFRESH_TOKEN_KEY, null)
            LogUtils.d(TAG, "authorize")
            AuthorizeTask(userName, password, loginCallback).execute()
        } else {
            LogUtils.d(TAG, "userName || password null")
        }
    }

    private inner class AuthorizeTask(
        private val userName: String,
        private val password: String,
        private val loginCallback: LoginCallback?
    ) : AsyncTask<Void?, Void?, Void?>() {
        private var jsonObject: JSONObject? = null
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            handleResults(jsonObject, loginCallback)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val connection = getConnection(makeURL())
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                val body = StringBuilder()
                body.append("grant_type=").append("password")
                    .append("&username=").append(userName)
                    .append("&password=").append(password)
                    .append("&client_id=").append(getValue(CLIENT_ID_KEY))
                setBody(connection, body.toString())
                jsonObject = make(connection)
            } catch (e: Throwable) {
                LogUtils.e(TAG, "AuthorizeTask$e")
            }
            return null
        }
    }

    private inner class RefreshTokenTask : AsyncTask<Void?, Void?, Void?>() {
        private var jsonObject: JSONObject? = null
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            handleResults(jsonObject, null)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val connection = getConnection(makeURL())
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                val body = StringBuilder()
                body.append("grant_type=").append("refresh_token")
                    .append("&refresh_token=").append(getValue(REFRESH_TOKEN_KEY))
                    .append("&client_id=").append(getValue(CLIENT_ID_KEY))
                setBody(connection, body.toString())
                jsonObject = make(connection)
            } catch (e: Throwable) {
                LogUtils.e(TAG, "RefreshTokenTask $e")
            }
            return null
        }
    }

    private fun makeURL(): String {
        val server = getValue(URL_KEY)
        val realm = getValue(REALM_KEY)
        val url = StringBuilder()
        url.append(server).append("realms/").append(realm).append("/protocol/openid-connect/token")
        return url.toString()
    }

    private fun handleResults(jsonObject: JSONObject?, loginCallback: LoginCallback?) {
        try {
            if (jsonObject != null && jsonObject.has("access_token")) {
                LogUtils.d(TAG, "success!")
                val accessToken = jsonObject.getString("access_token")
                val expiresIn = jsonObject.getString("expires_in")
                val refreshToken = jsonObject.getString("refresh_token")
                putValue(ACCESS_TOKEN_KEY, accessToken)
                putValue(REFRESH_TOKEN_KEY, refreshToken)
                val tokenExpires = expiresIn.toLong()
                setRefreshTokenAlarm(context, tokenExpires)
                active = true
                eventsCallback?.onUpdateToken(accessToken)
                loginCallback?.onAuthorize(true)
            } else {
                LogUtils.d(TAG, "fail!")
                if (loginCallback != null) {
                    loginCallback.onAuthorize(false)
                } else {
                    putValue(REFRESH_TOKEN_KEY, null)
                    eventsCallback?.onReLogin()
                }
            }
        } catch (e: Throwable) {
            LogUtils.e(TAG, "Error $e")
            loginCallback?.onAuthorize(false)
        }
    }

    fun refreshToken() {
        LogUtils.d(TAG, "refreshToke")
        RefreshTokenTask().execute()
    }

    private fun checkToken() {
        if (isEnabled) {
            LogUtils.d(TAG, "token available")
            if (!active) refreshToken()
        } else {
            LogUtils.d(TAG, "no token available")
        }
    }

    private fun setRefreshTokenAlarm(context: Context, refreshTokenExpries: Long) {
        val time = (refreshTokenExpries * 0.9).toLong()
        OAuthReceiver.setRefreshTokenAlarm(context, time)
    }

    private fun setAccessTokenAlarm(context: Context, accessTokenExpries: Long) {
        val time = (accessTokenExpries * 0.9).toLong()
        OAuthReceiver.setRefreshTokenAlarm(context, time)
    }

    private fun getValue(key: String): String? {
        var newKey = key
        newKey = newKey.lowercase(Locale.getDefault())
        return sharedPreferences?.getString(newKey, null)
    }

    private fun putValue(key: String, value: String?) {
        var newKey = key
        newKey = newKey.lowercase(Locale.getDefault())
        editorSP?.putString(newKey, value)
        editorSP?.commit()
    }

    interface LoginCallback {
        fun onAuthorize(success: Boolean)
    }

    interface EventsCallback {
        fun onReLogin()
        fun onUpdateToken(token: String?)
    }

    fun setURL(url: String?) {
        putValue(URL_KEY, url)
    }

    fun getURL(): String? {
        return getValue(URL_KEY)
    }

    fun getRealm(): String? {
        return getValue(REALM_KEY)
    }

    fun setRealm(realm: String?) {
        putValue(REALM_KEY, realm)
    }

    fun setClientId(clientId: String?) {
        putValue(CLIENT_ID_KEY, clientId)
    }

    fun getClientId(): String? {
        return getValue(CLIENT_ID_KEY)
    }

    companion object {
        const val TAG = "OAUTH"
        private var sharedPreferences: SharedPreferences? = null
        private var editorSP: SharedPreferences.Editor? = null
        private const val PREFS_KEY = "oauth_prefs"
        private const val REFRESH_TOKEN_KEY = "refreshToken"
        private const val ACCESS_TOKEN_KEY = "accessToken"
        private const val URL_KEY = "url"
        private const val REALM_KEY = "realm"
        private const val CLIENT_ID_KEY = "clientId"
        @SuppressLint("StaticFieldLeak")
        private var instance: OAuthManager? = null

        @Synchronized
        fun getInstance(): OAuthManager {
            if (instance == null) instance = OAuthManager()
            return instance!!
        }

    }
}