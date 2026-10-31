package com.eric.ecommerce.exceptions;

public class CartItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CartItemNotFoundException(String mensagem) {
		super(mensagem);
	}

}
