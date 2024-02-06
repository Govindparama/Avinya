package com.Avinya.App.dto;

public class LoginResponse {
    private String userType;
    private String jwtToken;

    public LoginResponse(String userType, String jwtToken) {
        this.setUserType(userType);
        this.setJwtToken(jwtToken);
    }

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

    // Getters and setters
}
