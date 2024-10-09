package com.kore.network.api.response

data class BotUserInfoResponse(
    val userId: String,
    val enrollType: String,
    val roles: List<Any>,
    val pwdChangeRequire: PwdChangeRequireResponse,
    val makeConsentPolicyAccept: Boolean,
    val makeBTConsentPolicyAccept: Boolean,
    val sourceType: String,
    val img: String,
    val orgID: String,
    val emailId: String,
    val profImage: String,
    val profColour: String,
    val isFirstTimeLogin: Boolean
)