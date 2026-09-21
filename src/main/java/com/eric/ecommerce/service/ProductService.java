package com.eric.ecommerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.eric.ecommerce.dto.ProductDTO;
import com.eric.ecommerce.exceptions.InvalidPriceRangeException;
import com.eric.ecommerce.exceptions.ProductNotFoundException;
import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.model.Product;
import com.eric.ecommerce.repository.ProductRepository;
import com.eric.ecommerce.specification.ProductSpecifications;

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

	public Page<ProductDTO> findWithFilters(Integer categoriaId, String nome, BigDecimal precoMin, BigDecimal precoMax,
			Pageable pageable) {

		Specification<Product> spec = Specification.unrestricted();

		if (categoriaId != null) {

			categoriaService.findById(categoriaId);

			spec = spec.and(ProductSpecifications.hasCategoria(categoriaId));

		}

		if (nome != null && !nome.isBlank()) {

			spec = spec.and(ProductSpecifications.nomeContains(nome));
		}

		if ((precoMin != null && precoMax == null) || (precoMin == null && precoMax != null)) {

			throw new InvalidPriceRangeException("Informe o preço mínimo e o preço máximo");

		}

		if (precoMin != null && precoMax != null) {

			if (precoMin.compareTo(precoMax) > 0) {

				throw new InvalidPriceRangeException("Preço mínimo não pode ser maior que o preço máximo");

			}

			spec = spec.and(ProductSpecifications.precoBetween(precoMin, precoMax));

		}

		Page<Product> products = productRepository.findAll(spec, pageable);

		return products.map(product -> toDTO(product));

	}

}
