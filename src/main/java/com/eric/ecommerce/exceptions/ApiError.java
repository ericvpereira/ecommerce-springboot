package com.eric.ecommerce.exceptions;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta padrão de erro da API")
public class ApiError {

	@Schema(description = "Código HTTP do erro", example = "404")
	private Integer status;

	@Schema(description = "Mensagem principal do erro", example = "Produto não encontrado")
	private String message;

	@Schema(description = "Detalhes dos erros por campos")
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
