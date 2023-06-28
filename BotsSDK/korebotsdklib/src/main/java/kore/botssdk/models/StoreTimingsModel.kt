package kore.botssdk.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreTimingsModel(
    @SerializedName("day_of_week")
    val dayOfWeek: String?,

    @SerializedName("store_timing")
    val storeTiming: String?,
) : Serializable