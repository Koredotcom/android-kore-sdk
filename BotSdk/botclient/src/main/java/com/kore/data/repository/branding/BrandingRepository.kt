package com.kore.data.repository.branding

import com.kore.common.Result
import com.kore.network.api.responsemodels.branding.BotActiveThemeModel

interface BrandingRepository {
    suspend fun getBranding(
        botId: String,
        token: String,
        state: String = "published",
        version: String = "1",
        language: String = "en_US"
    ): Result<BotActiveThemeModel?>
}