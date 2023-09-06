package kore.botssdk.common.manager

import android.content.Context
import kore.botssdk.common.listener.SocketChatListener
import kore.botssdk.common.listener.TTSUpdate
import kore.botssdk.models.JWTTokenResponse
import kore.botssdk.models.TokenResponseModel
import kore.botssdk.models.UserNameModel
import kore.botssdk.net.RestResponse.BotCustomData
import kore.botssdk.websocket.SocketConnectionListener

abstract class BaseSocketConnectionManager : SocketConnectionListener, TTSUpdate {
    @JvmField
    protected var context: Context? = null

    @JvmField
    var tokenResponseModel: TokenResponseModel? = null

    //    public SocketUpdateListener socketUpdateListener;
    //    public boolean isWithAuth;
    @JvmField
    var jwtKeyResponse: JWTTokenResponse? = null

    @JvmField
    protected var isSubscribed = false

    enum class EVENT_TYPE {
        TYPE_CONNECTED, TYPE_TEXT_MESSAGE, TYPE_DISCONNECTED, TYPE_MESSAGE_UPDATE
    }

    enum class CONNECTION_STATE {
        CONNECTING, CONNECTED, DISCONNECTED, CONNECTED_BUT_DISCONNECTED
    }

    abstract fun startAndInitiateConnectionWithAuthToken(
        context: Context,
        userId: String,
        accessToken: String,
        botCustomData: BotCustomData
    )

    abstract fun shutDownConnection()
    abstract fun subscribe()
    abstract fun subscribe(listener: SocketChatListener)
    abstract fun unSubscribe()
    abstract fun startAndInitiateConnectionWithConfig(context: Context, botCustomData: BotCustomData?)
    abstract fun startAndInitiateConnection(
        context: Context,
        userId: String,
        accessToken: String,
        userNameModel: UserNameModel,
        orgId: String
    )
}