package com.eric.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.ecommerce.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
