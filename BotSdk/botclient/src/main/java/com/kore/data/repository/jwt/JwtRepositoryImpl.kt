package com.kore.data.repository.jwt

import com.kore.common.Result
import com.kore.network.api.ApiClient
import com.kore.network.api.response.BotAuthorizationResponse
import com.kore.network.api.response.JwtTokenResponse
import com.kore.network.api.response.RtmUrlResponse
import com.kore.network.api.service.JwtApi
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date
import javax.crypto.SecretKey

class JwtRepositoryImpl : JwtRepository {
    companion object {
        private const val BEARER = "bearer "
        private const val KEY_TYP = "typ"
        private const val KEY_AUD = "aud"
        private const val KEY_USER_IDENTITY = "userIdentity"
        private const val KEY_APP_ID = "appId"
        private const val KEY_IS_ANONYMOUS = "isAnonymous"
        private const val VALUE_TYP = "JWT"
        private const val VALUE_AUD = "https://idproxy.kore.com/authorize"
        private const val EXPIRY_DURATION = 86400000
    }

    override fun generateJwt(identity: String, clientSecret: String, clientId: String): String {
        val curTime = System.currentTimeMillis()
        val expTime = curTime + EXPIRY_DURATION

        return Jwts.builder()
            .issuer(clientId)
            .issuedAt(Date(curTime))
            .expiration(Date(expTime))
            .audience().add(VALUE_AUD).and()
            .subject(identity)
            .claim(KEY_IS_ANONYMOUS, false)
            .signWith(generateKey(clientSecret), Jwts.SIG.HS256)
            .compact()
    }

    private fun generateKey(key: String): SecretKey? {
        val keyBytes: ByteArray = key.toByteArray()
        return Keys.hmacShaKeyFor(keyBytes)
    }

    override fun generateJwtForAPI(identity: String, clientSecret: String, clientId: String): String {
        val curTime = System.currentTimeMillis()
        val expTime = curTime + EXPIRY_DURATION

        return Jwts.builder()
            .header().add(KEY_TYP, VALUE_TYP).and()
            .issuer(clientId)
            .subject(identity)
            .audience().add(VALUE_AUD).and()
            .issuedAt(Date(curTime))
            .expiration(Date(expTime))
            .claim(KEY_IS_ANONYMOUS, false)
            .claim(KEY_USER_IDENTITY, identity)
            .claim(KEY_APP_ID, clientId)
            .signWith(generateKey(clientSecret), Jwts.SIG.HS256)
            .compact()
    }

    override suspend fun getJwtToken(url: String, clientId: String, clientSecret: String, identity: String): Result<JwtTokenResponse?> {
        return try {
            val body: HashMap<String, Any> = HashMap()
            body["clientId"] = clientId
            body["clientSecret"] = clientSecret
            body["identity"] = identity
            body[KEY_AUD] = "https://idproxy.kore.com/authorize"
            body[KEY_IS_ANONYMOUS] = false
            val jwtApi = ApiClient.getInstance().createService(JwtApi::class.java)
            val response = jwtApi.getJwtToken(url + "users/sts", body)
            if (response.isSuccessful) {
                Result.Success(response.body())
            } else {
                Result.Error(Exception(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Error(Exception(ex.message.toString()))
        }
    }

    override suspend fun getJwtGrant(jwtToken: HashMap<String, Any>): Result<BotAuthorizationResponse?> {
        return try {
            val jwtApi = ApiClient.getInstance().createService(JwtApi::class.java)
            val response = jwtApi.jwtGrant(jwtToken)
            if (response.isSuccessful) {
                Result.Success(response.body())
            } else {
                Result.Error(Exception(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Error(Exception(ex.message.toString()))
        }
    }

    override suspend fun getRtmUrl(token: String, optParameterBotInfo: HashMap<String, Any>): Result<RtmUrlResponse?> {
        return try {
            val jwtApi = ApiClient.getInstance().createService(JwtApi::class.java)
            val response = jwtApi.getRtmUrl(BEARER + token, optParameterBotInfo)
            if (response.isSuccessful) {
                Result.Success(response.body())
            } else {
                Result.Error(Exception(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Error(Exception(ex.message.toString()))
        }
    }

    override suspend fun getRtmUrl(token: String, botInfo: HashMap<String, Any>, isReconnect: Boolean): Result<RtmUrlResponse?> {
        return try {
            val jwtApi = ApiClient.getInstance().createService(JwtApi::class.java)
            val response = jwtApi.getRtmUrl(BEARER + token, botInfo, isReconnect)
            if (response.isSuccessful) {
                Result.Success(response.body())
            } else {
                Result.Error(Exception(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Error(Exception(ex.message.toString()))
        }
    }
}