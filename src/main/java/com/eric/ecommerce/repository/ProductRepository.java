package com.eric.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.ecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Page<Product> findByCategoria_Id(Integer categoriaId, Pageable pageable);

	Page<Product> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

	Page<Product> findByCategoria_IdAndNomeContainingIgnoreCase(Integer categoriaId, String nome, Pageable pageable);

}
