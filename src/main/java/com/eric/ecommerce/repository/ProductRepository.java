package com.eric.ecommerce.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.eric.ecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product>{

	Page<Product> findByCategoria_Id(Integer categoriaId, Pageable pageable);

	Page<Product> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

	Page<Product> findByCategoria_IdAndNomeContainingIgnoreCase(Integer categoriaId, String nome, Pageable pageable);

	Page<Product> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax, Pageable pageable);

}
