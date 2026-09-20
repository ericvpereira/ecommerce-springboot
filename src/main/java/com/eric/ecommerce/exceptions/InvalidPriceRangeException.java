package com.eric.ecommerce.exceptions;

public class InvalidPriceRangeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidPriceRangeException(String mensagem) {
		super(mensagem);
	}

}
