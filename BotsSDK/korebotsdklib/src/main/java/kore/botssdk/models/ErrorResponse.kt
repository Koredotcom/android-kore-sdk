package kore.botssdk.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("error")
    val error: String? = null,

    @SerializedName("errorCode")
    val errorCode: String? = null,

    @SerializedName("errorSummary")
    val errorSummary: String?,

    @SerializedName("error_description")
    val errorDescription: String?,
)