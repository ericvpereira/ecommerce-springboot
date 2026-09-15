package com.eric.ecommerce.exceptions;

public class CategoriaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoriaNotFoundException(String mensagem) {
		super(mensagem);
	}

}
