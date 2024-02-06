package com.Avinya.App.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;

public class UpdateReq {

    private String _id;
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    private Boolean isAdmin;

	public UpdateReq(String _id, @NotBlank String name, @NotBlank @Email String email, Boolean isAdmin) {
		super();
		this._id = _id;
		this.name = name;
		this.email = email;
		this.isAdmin = isAdmin;
	}

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

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
