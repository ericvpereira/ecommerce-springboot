package com.eric.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eric.ecommerce.dto.ProductDTO;
import com.eric.ecommerce.exceptions.ProductNotFoundException;
import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.model.Product;
import com.eric.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository productRepository;
	private CategoriaService categoriaService;

	public ProductService(ProductRepository productRepository, CategoriaService categoriaService) {
		this.productRepository = productRepository;
		this.categoriaService = categoriaService;
	}

	public ProductDTO save(ProductDTO dto) {

		Product product = toEntity(dto);

		Product savedProduct = productRepository.save(product);

		return toDTO(savedProduct);

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

	public Product toEntity(ProductDTO dto) {

		Product product = new Product();
		Categoria categoria = categoriaService.findById(dto.getCategoriaId());

		product.setNome(dto.getNome());
		product.setPreco(dto.getPreco());
		product.setCategoria(categoria);

		return product;

	}

	public Product findEntityById(Integer id) {

		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Produto com ID [" + id + "] não encontrado"));

	}

	public ProductDTO update(Integer id, ProductDTO dto) {

		Product product = findEntityById(id);

		product.setNome(dto.getNome());
		product.setPreco(dto.getPreco());
		Categoria categoria = categoriaService.findById(dto.getCategoriaId());
		product.setCategoria(categoria);

		Product savedProduct = productRepository.save(product);

		return toDTO(savedProduct);

	}

	public Page<ProductDTO> findAll(Pageable pageable) {

		Page<Product> products = productRepository.findAll(pageable);

		return products.map(product -> toDTO(product));

	}

	public Page<ProductDTO> findByCategoria(Integer categoriaId, Pageable pageable) {
		
		categoriaService.findById(categoriaId);
		
		Page<Product> products = productRepository.findByCategoria_Id(categoriaId, pageable);

		return products.map(product -> toDTO(product));

	}

}
