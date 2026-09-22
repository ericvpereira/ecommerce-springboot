package com.eric.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.eric.ecommerce.dto.ProductDTO;
import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.model.Product;
import com.eric.ecommerce.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoriaService categoriaService;

	@InjectMocks
	private ProductService productService;

	@Test
	void deveBuscarProdutosSemFiltros() {

		Categoria categoria = new Categoria();
		categoria.setId(1);
		categoria.setNome("Informatica");

		Product product = new Product();
		product.setNome("Notebook Gamer");
		product.setPreco(new BigDecimal("4500"));
		product.setCategoria(categoria);

		Page<Product> page = new PageImpl<>(List.of(product));

		when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

		Page<ProductDTO> result = productService.findWithFilters(null, null, null, null, Pageable.unpaged());

		assertEquals(1, result.getTotalElements());

		ProductDTO dto = result.getContent().get(0);

		assertEquals("Notebook Gamer", dto.getNome());
		assertEquals(new BigDecimal("4500"), dto.getPreco());
		assertEquals(1, dto.getCategoriaId());

		verify(productRepository).findAll(any(Specification.class), any(Pageable.class));

	}

}
