package com.Avinya.App.Request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    
    
    private String email;
    private String mobNo;

    @NotBlank
    private String password;
    
    
    
    public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}