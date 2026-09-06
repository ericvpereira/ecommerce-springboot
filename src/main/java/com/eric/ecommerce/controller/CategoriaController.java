package com.eric.ecommerce.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.service.CategoriaService;

@RestController
@RequestMapping("/api/categories")
public class CategoriaController {

	CategoriaService categoriaService;

	public CategoriaController(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	@GetMapping
	public List<Categoria> findAll() {

		return categoriaService.findAll();
	}

	@PostMapping
	public Categoria save(@RequestBody Categoria categoria) {

		return categoriaService.save(categoria);

	}

}
