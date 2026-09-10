package com.eric.ecommerce.exceptions;

import java.util.Map;

public class ApiError {

	private Integer status;
	private String message;

	private Map<String, String> errors;

	public ApiError() {

	}

	public ApiError(Integer status, String message, Map<String, String> errors) {
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
