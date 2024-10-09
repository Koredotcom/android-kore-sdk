package com.kore.network.api.response

data class AuthorizationResponse (
    val identity: String,
    val resourceOwnerID: String,
    val datasource: List<String>,
    val orgID: String,
    val clientID: String,
    val sesId: String,
    val accountId: String,
    val managedBy: String,
    val scope: List<String>,
    val platDevType: String,
    val lastActivity: String,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresDate: String,
    val refreshExpiresDate: String,
    val issuedDate: String
)