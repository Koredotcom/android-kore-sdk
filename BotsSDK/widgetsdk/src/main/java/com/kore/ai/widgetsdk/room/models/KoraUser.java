package com.kore.ai.widgetsdk.room.models;

import com.google.gson.annotations.SerializedName;

public class KoraUser {

	@SerializedName("userInfo")
	private UserData userData;

	@SerializedName("authorization")
	private AuthData authData;

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public AuthData getAuthData() {
		return authData;
	}
	public void setAuthData(AuthData authData){
		this.authData = authData;
	}

}
