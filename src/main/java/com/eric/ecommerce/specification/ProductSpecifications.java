package com.eric.ecommerce.specification;

import org.springframework.data.jpa.domain.Specification;

import com.eric.ecommerce.model.Product;

public class ProductSpecifications {

	public static Specification<Product> hasCategoria(Integer categoriaId) {

		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId);

	}

}
