package com.eric.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.ecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
