package com.eric.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.eric.ecommerce.dto.ProductDTO;
import com.eric.ecommerce.exceptions.ProductNotFoundException;
import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.repository.CategoriaRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ ProductService.class, CategoriaService.class

})
public class ProductServiceIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Test
	void deveBuscarProdutosComFiltrosUsandoBancoReal() {

		Categoria informatica = new Categoria();
		informatica.setNome("Informatica");

		Categoria informaticaSalva = categoriaRepository.save(informatica);

		ProductDTO notebook = new ProductDTO(null, "Notebook Gamer", new BigDecimal("4500"), informaticaSalva.getId());

		productService.save(notebook);

		ProductDTO mouse = new ProductDTO(null, "Mouse Gamer", new BigDecimal("300"), informaticaSalva.getId());

		productService.save(mouse);

		Page<ProductDTO> resultado = productService.findWithFilters(informaticaSalva.getId(), "note",
				new BigDecimal("3000"), new BigDecimal("5000"), Pageable.unpaged());

		assertEquals(1, resultado.getTotalElements());

		assertEquals("Notebook Gamer", resultado.getContent().get(0).getNome());

		assertEquals(new BigDecimal("4500"), resultado.getContent().get(0).getPreco());

	}

	@Test
	void deveAtualizarProdutoUsandoBancoReal() {

		Categoria informatica = new Categoria();
		informatica.setNome("Informatica");

		Categoria informaticaSalva = categoriaRepository.save(informatica);

		ProductDTO notebook = new ProductDTO(null, "Notebook", new BigDecimal("3500"), informaticaSalva.getId());

		ProductDTO produtoSalvo = productService.save(notebook);

		assertNotNull(produtoSalvo.getId());

		ProductDTO dadosAtualizados = new ProductDTO(null, "Notebook Gamer", new BigDecimal("4500"),
				informaticaSalva.getId());

		ProductDTO produtoAtualizado = productService.update(produtoSalvo.getId(), dadosAtualizados);

		assertEquals(produtoSalvo.getId(), produtoAtualizado.getId());
		assertEquals("Notebook Gamer", produtoAtualizado.getNome());
		assertEquals(new BigDecimal("4500"), produtoAtualizado.getPreco());
		assertEquals(informaticaSalva.getId(), produtoAtualizado.getCategoriaId());

		entityManager.flush();
		entityManager.clear();

		ProductDTO produtoBuscado = productService.findById(produtoSalvo.getId());

		assertEquals("Notebook Gamer", produtoBuscado.getNome());
		assertEquals(0, new BigDecimal("4500").compareTo(produtoBuscado.getPreco()));
		assertEquals(informaticaSalva.getId(), produtoSalvo.getCategoriaId());

	}

	@Test
	void deveExcluirProdutoUsandoBancoReal() {

		Categoria informatica = new Categoria();
		informatica.setNome("Informatica");

		Categoria informaticaSalva = categoriaRepository.save(informatica);

		ProductDTO notebook = new ProductDTO(null, "Notebook Gamer", new BigDecimal("4500"), informaticaSalva.getId());

		ProductDTO produtoSalvo = productService.save(notebook);

		assertNotNull(produtoSalvo.getId());

		productService.deleteById(produtoSalvo.getId());

		entityManager.flush();
		entityManager.clear();

		assertThrows(ProductNotFoundException.class, () -> productService.findById(produtoSalvo.getId()));

	}

}
