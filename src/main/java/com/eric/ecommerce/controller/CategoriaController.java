package com.eric.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.ecommerce.dto.CategoriaDTO;
import com.eric.ecommerce.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoriaController {

	private final CategoriaService categoriaService;

	public CategoriaController(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	@GetMapping
	public List<CategoriaDTO> findAll() {

		return categoriaService.findAll();
	}

	@GetMapping("/{id}")
	public CategoriaDTO findById(@PathVariable Integer id) {

		return categoriaService.findDTOById(id);

	}

	@PostMapping
	public ResponseEntity<CategoriaDTO> save(@RequestBody @Valid CategoriaDTO dto) {

		CategoriaDTO saved = categoriaService.save(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(saved);

	}

	@PutMapping("/{id}")
	public CategoriaDTO update(@PathVariable Integer id, @RequestBody @Valid CategoriaDTO dto) {

		return categoriaService.update(id, dto);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

		categoriaService.deleteById(id);

		return ResponseEntity.noContent().build();

	}

}
