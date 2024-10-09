package com.kore.data.repository.branding

import com.kore.common.Result
import com.kore.network.api.ApiClient
import com.kore.network.api.responsemodels.branding.BotActiveThemeModel
import com.kore.network.api.service.BrandingApi

class BrandingRepositoryImpl : BrandingRepository {
    override suspend fun getBranding(
        botId: String,
        token: String,
        state: String,
        version: String,
        language: String
    ): Result<BotActiveThemeModel?> {
        return try {
            val brandingApi = ApiClient.getInstance().createService(BrandingApi::class.java)
            val response = brandingApi.getBrandingNewDetails(botId, "bearer $token", state, version, language, botId)
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