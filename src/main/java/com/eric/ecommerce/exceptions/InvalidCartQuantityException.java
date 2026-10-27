package com.eric.ecommerce.exceptions;

public class InvalidCartQuantityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidCartQuantityException(String mensagem) {
		super(mensagem);
	}

}
