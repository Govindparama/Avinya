package com.Avinya.App.dto;

public class JwtRes {
    private String _id , name, email, token;
    private Boolean isAdmin;

    public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public JwtRes(String _id, String name, String email, String token, Boolean isAdmin) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.token = token;
        this.isAdmin = isAdmin;
    }
}
