package com.eric.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eric.ecommerce.exceptions.ProductNotFoundException;
import com.eric.ecommerce.model.Product;
import com.eric.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

	ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product save(Product product) {

		return productRepository.save(product);

	}

	public List<Product> findAll() {

		return productRepository.findAll();

	}

	public Product findById(Integer id) {

		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Produto com ID [" + id + "] não encontrado"));

	}

	public void deleteById(Integer id) {

		Product product = findById(id);

		productRepository.delete(product);
	}
}
