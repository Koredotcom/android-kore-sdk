package kore.botssdk.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NearByStockAvailableStoreModel(
    @SerializedName("id")
    val id: String?,

    @SerializedName("next_closest_in_stock")
    val nextClosestInStock: String?,

    @SerializedName("location")
    val location: String?,

    @SerializedName("distance_to_store")
    val distanceToStore: String?,

    @SerializedName("items_left")
    val itemsLeft: Int?,

    @SerializedName("open_status")
    val openStatus: String?,

    @SerializedName("action")
    val action: String?,

    @SerializedName("lat")
    val latitude: Double?,

    @SerializedName("long")
    val longitude: Double?,

    @SerializedName("store_address")
    val storeAddress: String?,

    @SerializedName("store_timings")
    val storeTimings: List<StoreTimingsModel>?
) : Serializable