package com.eric.ecommerce.exceptions;

public class ProductNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String mensagem) {
		super(mensagem);
	}

}
