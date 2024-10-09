package com.kore.widgets.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Panel(
    @SerializedName("_id")
    val id: String,

    @SerializedName("skillId")
    val skillId: String?,

    @SerializedName("theme")
    @Expose
    val theme: String? = "#e5f5f6",

    val isItemClicked: Boolean = false,

    @SerializedName("type")
    val type: String?,

    @SerializedName("teamId")
    var teamId: String?,

    @SerializedName("name")
    @Expose
    val name: String?,

    @SerializedName("icon")
    @Expose
    val icon: String?,

    @SerializedName("iconId")
    @Expose
    val iconId: String?,

    @SerializedName("widgets")
    @Expose
    val widgets: List<WidgetsModel>,

    @SerializedName("trigger")
    @Expose
    val trigger: String?,

    @SerializedName("autoRefresh")
    val autoRefresh: AutoRefresh,

    @SerializedName("callbackURL")
    @Expose
    val callbackURL: String?
): Serializable{
    fun getThemeValue(): String = if (theme.isNullOrEmpty()) "#e5f5f6" else theme
}