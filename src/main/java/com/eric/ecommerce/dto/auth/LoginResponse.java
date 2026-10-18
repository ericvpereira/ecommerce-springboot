package com.eric.ecommerce.dto.auth;

public class LoginResponse {

	private String token;

	public LoginResponse() {

	}

	public LoginResponse(String token) {
		this.token = token;
	}

	public String getMessage() {
		return token;
	}

	public void setMessage(String token) {
		this.token = token;
	}

}
