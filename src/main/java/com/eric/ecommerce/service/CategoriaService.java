package com.eric.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.repository.CategoriaRepository;

@Service
public class CategoriaService {

	CategoriaRepository categoriaRepository;

	public CategoriaService(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	public Categoria save(Categoria categoria) {

		return categoriaRepository.save(categoria);

	}

	public List<Categoria> findAll() {

		return categoriaRepository.findAll();

	}

}
