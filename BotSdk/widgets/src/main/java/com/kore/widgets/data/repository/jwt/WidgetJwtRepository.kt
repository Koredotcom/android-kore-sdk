package com.kore.widgets.data.repository.jwt

import com.kore.widgets.model.WidgetConfigModel

interface WidgetJwtRepository {

    suspend fun getJwtToken(
        configModel: WidgetConfigModel,
        headers: HashMap<String, Any>? = null,
        body: HashMap<String, Any>? = null
    ): String
//    suspend fun getJwtToken(url: String, headers: HashMap<String, Any>?, body: HashMap<String, Any>): Result<JwtTokenResponse?>
}