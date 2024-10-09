package com.kore.widgets.data.repository.jwt

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kore.widgets.model.WidgetBotInfoModel
import com.kore.widgets.model.WidgetConfigModel
import com.kore.widgets.network.api.ApiClient
import com.kore.widgets.network.api.service.WidgetJwtApi
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.crypto.SecretKey

class WidgetJwtRepositoryImpl : WidgetJwtRepository {
    companion object {
        private const val KEY_ISS = "iss"
        private const val KEY_IAT = "iat"
        private const val KEY_EXP = "exp"
        private const val KEY_AUD = "aud"
        private const val KEY_SUB = "sub"
        private const val KEY_IS_ANONYMOUS = "isAnonymous"
        private const val VALUE_AUD = "https://idproxy.kore.com/authorize"
        private const val EXPIRY_DURATION = 86400000
    }

    private fun generateJwt(identity: String, clientSecret: String, clientId: String): String {
        val curTime = System.currentTimeMillis()
        val expTime = curTime + EXPIRY_DURATION
        return Jwts.builder()
            .claim(KEY_ISS, clientId)
            .claim(KEY_IAT, curTime)
            .claim(KEY_EXP, expTime)
            .claim(KEY_AUD, VALUE_AUD)
            .claim(KEY_SUB, identity)
            .claim(KEY_IS_ANONYMOUS, false)
            .signWith(generateKey(clientSecret), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun generateKey(key: String): SecretKey? {
        val keyBytes: ByteArray = key.toByteArray()
        return Keys.hmacShaKeyFor(keyBytes)
    }

    override suspend fun getJwtToken(configModel: WidgetConfigModel, headers: HashMap<String, Any>?, body: HashMap<String, Any>?): String {
        return withContext(Dispatchers.IO) {
            try {
                val botInfoModel = WidgetBotInfoModel(configModel.botName, configModel.botId, emptyMap())
                val gson = Gson()
                val botInfoMap: HashMap<String, Any> =
                    gson.fromJson(gson.toJson(botInfoModel), object : TypeToken<HashMap<String, Any>>() {}.type)

                if (headers == null) {
                    generateJwt(configModel.identity, configModel.clientSecret, configModel.clientId)
                } else {
                    val jwtApi: WidgetJwtApi = ApiClient.getInstance().createService(WidgetJwtApi::class.java)
                    val response = jwtApi.getJwtToken(
                        configModel.jwtServerUrl,
                        headers,
                        if (body.isNullOrEmpty()) botInfoMap else body
                    )
                    if (response.isSuccessful) {
                        response.body()?.jwt!!
                    } else {
                        ""
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}