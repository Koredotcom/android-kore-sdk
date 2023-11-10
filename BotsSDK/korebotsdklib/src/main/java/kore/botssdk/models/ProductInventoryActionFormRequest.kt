package kore.botssdk.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductInventoryActionFormRequest(
    @SerializedName("userName")
    val userName: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("itemId")
    val id: String,

    @SerializedName("actionName")
    val action: String
) : Serializable