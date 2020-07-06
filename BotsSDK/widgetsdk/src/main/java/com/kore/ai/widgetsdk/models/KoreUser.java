package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.SerializedName;

public class KoreUser {

	private UserInfo userInfo;

	@SerializedName("authorization")
	private AuthInfo authInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public AuthInfo getAuthInfo() {
		return authInfo;
	}

}
