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

import com.eric.ecommerce.dto.ProductDTO;
import com.eric.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<ProductDTO> findAll() {

		return productService.findAll();

	}

	@GetMapping("/{id}")
	public ProductDTO findById(@PathVariable Integer id) {

		return productService.findById(id);

	}

	@PostMapping
	public ResponseEntity<ProductDTO> createdProduct(@RequestBody @Valid ProductDTO dto) {

		ProductDTO save = productService.save(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(save);

	}

	@PutMapping("/{id}")
	public ProductDTO update(@PathVariable Integer id, @RequestBody @Valid ProductDTO product) {

		return productService.update(id, product);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {

		productService.deleteById(id);

		return ResponseEntity.noContent().build();

	}

}
