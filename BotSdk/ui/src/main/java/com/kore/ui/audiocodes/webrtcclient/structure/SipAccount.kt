package com.kore.ui.audiocodes.webrtcclient.structure

import android.content.Context
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.general.BasePrefs.Companion.getString

class SipAccount(context: Context) {
    var proxy: String?
    var port: Int
    var domain: String?
    var transport: Transport
    var username: String?
    var password: String?
    var displayName: String?

    init {
        proxy = getString(context, "SipAccountProxy")
        domain = getString(context, "SipAccountDomain")
        username = getString(context, "SipAccountUsername")
        displayName = getString(context, "SipAccountDisplayName")
        password = context.getString(R.string.sip_account_password_default)
        val portStr = context.getString(R.string.sip_account_port_default)
        port = portStr.toInt()
        val transportStr = context.getString(R.string.sip_account_transport_default)
        transport = Transport.valueOf(transportStr)
    }

    override fun toString(): String {
        return "SipAccount{" +
                "proxy='" + proxy + '\'' +
                ", port=" + port +
                ", domain='" + domain + '\'' +
                ", transport=" + transport +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                '}'
    }
}
