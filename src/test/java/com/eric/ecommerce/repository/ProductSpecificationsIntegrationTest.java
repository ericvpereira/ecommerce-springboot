package com.eric.ecommerce.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.model.Product;
import com.eric.ecommerce.specification.ProductSpecifications;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductSpecificationsIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Test
	void deveBuscarProdutoPorNome() {

		Categoria categoria = new Categoria();

		categoria.setNome("Informatica");

		Categoria categoriaSalva = categoriaRepository.save(categoria);

		Product product = new Product();

		product.setNome("Notebook Gamer");
		product.setPreco(new BigDecimal("4500"));

		product.setCategoria(categoriaSalva);

		Product produtoSalvo = productRepository.save(product);

		List<Product> resultado = productRepository.findAll(ProductSpecifications.nomeContains("note"));

		assertEquals(1, resultado.size());

		assertEquals("Notebook Gamer", resultado.get(0).getNome());
		
		assertNotNull(produtoSalvo.getId());

	}

}
