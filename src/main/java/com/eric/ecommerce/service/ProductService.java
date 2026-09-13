package com.eric.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eric.ecommerce.dto.ProductDTO;
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

	public List<ProductDTO> findAll() {

		List<Product> products = productRepository.findAll();

		List<ProductDTO> dtos = new ArrayList<>();

		for (Product product : products) {

			dtos.add(toDTO(product));

		}

		return dtos;

	}

	public ProductDTO findById(Integer id) {

		Product product = findEntityById(id);

		return toDTO(product);

	}

	public void deleteById(Integer id) {

		Product product = findEntityById(id);

		productRepository.delete(product);
	}

	public ProductDTO toDTO(Product product) {

		ProductDTO productDTO = new ProductDTO();

		productDTO.setId(product.getId());
		productDTO.setNome(product.getNome());
		productDTO.setPreco(product.getPreco());
		productDTO.setCategoriaId(product.getCategoria().getId());

		return productDTO;

	}

	public Product findEntityById(Integer id) {

		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Produto com ID [" + id + "] não encontrado"));

	}

}
