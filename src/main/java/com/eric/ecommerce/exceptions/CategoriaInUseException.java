package com.eric.ecommerce.exceptions;

public class CategoriaInUseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoriaInUseException(String mensagem) {
		super(mensagem);
	}

}
