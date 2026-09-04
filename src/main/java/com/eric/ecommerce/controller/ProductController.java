package com.eric.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.ecommerce.model.Product;
import com.eric.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<Product> findAll() {

		return productService.findAll();

	}

	@GetMapping("/{id}")
	public Product findById(@PathVariable Integer id) {

		return productService.findById(id);

	}

	@PostMapping
	public ResponseEntity<Product> save(@RequestBody Product product) {

		Product save = productService.save(product);

		return ResponseEntity.status(HttpStatus.CREATED).body(save);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {

		productService.deleteById(id);

		return ResponseEntity.noContent().build();

	}

}
