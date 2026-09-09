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
	public ResponseEntity<String> handleProductNotFound(ProductNotFoundException ex) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleArgumentNotValidException(MethodArgumentNotValidException ex) {

		Map<String, String> map = new HashMap<>();
		List<FieldError> erros = ex.getBindingResult().getFieldErrors();

		for (FieldError erro : erros) {
			map.put(erro.getField(), erro.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);

	}

}
