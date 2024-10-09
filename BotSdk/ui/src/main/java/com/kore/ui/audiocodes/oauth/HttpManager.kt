package com.kore.ui.audiocodes.oauth

import android.annotation.SuppressLint
import android.util.Log
import com.kore.common.utils.LogUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.KeyManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@SuppressLint("UnknownNullness")
object HttpManager {
    private const val TAG = "HttpManager"

    @JvmStatic
    @Throws(IOException::class)
    fun getConnection(url: String): HttpURLConnection {
        val urlConnection = URL(url).openConnection() as HttpURLConnection
        if (urlConnection is HttpsURLConnection) {
            try {
                val context = SSLContext.getInstance("SSL")
                context.init(null as Array<KeyManager?>?, arrayOf<TrustManager>(AcceptAllTrustManager()), null)
                urlConnection.sslSocketFactory = context.socketFactory
                urlConnection.hostnameVerifier = HostnameVerifier { s, sslSession -> true }
                LogUtils.d(TAG, "cs_trust_all_certificates")
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return urlConnection
    }

    @JvmStatic
    @Throws(IOException::class)
    fun setBody(urlConnection: HttpURLConnection, body: String?) {
        if (body != null) {
            urlConnection.setRequestProperty("Content-length", body.toByteArray(StandardCharsets.UTF_8).size.toString())
            urlConnection.doInput = true
            urlConnection.doOutput = true
            urlConnection.useCaches = false
            val outputStream = urlConnection.outputStream
            outputStream.write(body.toByteArray(StandardCharsets.UTF_8))
            outputStream.close()
        }
    }

    @JvmStatic
    @Throws(IOException::class, JSONException::class)
    fun make(urlConnection: HttpURLConnection): JSONObject {
        urlConnection.connect()
        val response = handleResponse(urlConnection)
        return JSONObject(response)
    }

    @Throws(IOException::class)
    private fun handleResponse(urlConnection: HttpURLConnection): String {
        val responseCode = urlConnection.responseCode
        LogUtils.d(TAG, "responseCode: $responseCode")
        LogUtils.d(TAG, "responseMessage: " + urlConnection.responseMessage)
        var inputStream: InputStream? = null
        inputStream = try {
            urlConnection.inputStream
        } catch (exception: IOException) {
            urlConnection.errorStream
        }
        val lastResponse = readStream(inputStream)
        if (responseCode < 400) {
            printLongMsg("response: $lastResponse")
        } else {
            printLongMsg("response: $lastResponse")
        }
        return lastResponse
    }

    private fun readStream(`in`: InputStream?): String {
        var reader: BufferedReader? = null
        val response = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return response.toString()
    }

    private fun printLongMsg(msg: String) {
        if (msg.length > 4000) {
            Log.d(TAG, msg.substring(0, 4000))
            printLongMsg(msg.substring(4000))
        } else {
            Log.d(TAG, msg)
        }
    }

    private class AcceptAllTrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, arg1: String) {
            if (!debugMode) {
                try {
                    x509Certificates[0].checkValidity()
                } catch (var4: Exception) {
                    throw CertificateException("Certificate not valid or trusted.")
                }
            }
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, arg1: String) {
            if (!debugMode) {
                try {
                    x509Certificates[0].checkValidity()
                } catch (var4: Exception) {
                    throw CertificateException("Certificate not valid or trusted.")
                }
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }

        companion object {
            var debugMode = true
        }
    }
}