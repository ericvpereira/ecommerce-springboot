package com.eric.ecommerce.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.eric.ecommerce.model.Product;

public class ProductSpecifications {

	public static Specification<Product> hasCategoria(Integer categoriaId) {

		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId);

	}

	public static Specification<Product> nomeContains(String nome) {

		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")),
				"%" + nome.toLowerCase() + "%");

	}

	public static Specification<Product> precoBetween(BigDecimal precoMin, BigDecimal precoMax) {

		return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("preco"), precoMin, precoMax);

	}

}
