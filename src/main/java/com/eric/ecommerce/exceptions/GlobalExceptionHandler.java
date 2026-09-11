package com.eric.ecommerce.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiError> handleProductNotFound(ProductNotFoundException ex) {

		Map<String, String> map = new HashMap<>();
		map.put("product", ex.getMessage());

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), "Produto não encontrado", map);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleArgumentNotValidException(MethodArgumentNotValidException ex) {

		Map<String, String> map = new HashMap<>();
		List<FieldError> erros = ex.getBindingResult().getFieldErrors();

		for (FieldError erro : erros) {
			map.put(erro.getField(), erro.getDefaultMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Erro de validação", map);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);

	}

}
