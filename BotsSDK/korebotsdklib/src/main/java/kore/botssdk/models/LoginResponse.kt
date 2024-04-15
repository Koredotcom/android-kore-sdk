package kore.botssdk.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("expires_in")
    val expires_in: Int = 0,

    @SerializedName("token_type")
    val tokenType: String?,

    @SerializedName("access_token")
    val accessToken: String?,
)