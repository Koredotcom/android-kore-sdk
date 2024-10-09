package com.kore.uploadfile.ssl

import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HttpsUrlConnectionBuilder(uri: String) {
    lateinit var httpsURLConnection: HttpsURLConnection

    init {
        try {
            val url = URL(uri)
            httpsURLConnection = url.openConnection() as HttpsURLConnection
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}