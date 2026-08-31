package com.eric.ecommerce.model;

import java.math.BigDecimal;

public class ProductTest {

	public static void main(String[] args) {

		Categoria categoria = new Categoria(null, "Informatica");
		Product product = new Product(null, categoria, "Notebook", new BigDecimal("3500"));

		System.out.println(product.getCategoria().getNome());

	}

}
