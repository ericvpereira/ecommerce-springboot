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

		Product mouse = new Product();
		mouse.setNome("Mouse sem fio");
		mouse.setPreco(new BigDecimal("150"));
		mouse.setCategoria(categoriaSalva);

		productRepository.save(mouse);

		List<Product> resultado = productRepository.findAll(ProductSpecifications.nomeContains("note"));

		assertEquals(1, resultado.size());

		assertEquals("Notebook Gamer", resultado.get(0).getNome());

		assertNotNull(produtoSalvo.getId());

	}

	@Test
	void deveBuscarProdutosPorCategoria() {

		Categoria informatica = new Categoria();
		informatica.setNome("Informatica");

		Categoria informaticaSalva = categoriaRepository.save(informatica);

		Categoria esportes = new Categoria();
		esportes.setNome("Esportes");

		Categoria esportesSalva = categoriaRepository.save(esportes);

		Product notebook = new Product();
		notebook.setNome("Notebook Gamer");
		notebook.setPreco(new BigDecimal("4500"));
		notebook.setCategoria(informaticaSalva);

		productRepository.save(notebook);

		Product bola = new Product();
		bola.setNome("Bola de futebol");
		bola.setPreco(new BigDecimal("150"));
		bola.setCategoria(esportesSalva);

		productRepository.save(bola);

		List<Product> resultado = productRepository
				.findAll(ProductSpecifications.hasCategoria(informaticaSalva.getId()));

		assertEquals(1, resultado.size());

		assertEquals("Notebook Gamer", resultado.get(0).getNome());

	}

	@Test
	void deveBuscarProdutosPorFaixaDePreco() {

		Categoria informatica = new Categoria();
		informatica.setNome("Informatica");

		Categoria informaticaSalva = categoriaRepository.save(informatica);

		Product mouse = new Product();
		mouse.setNome("Mouse");
		mouse.setPreco(new BigDecimal("150"));
		mouse.setCategoria(informaticaSalva);

		productRepository.save(mouse);

		Product monitor = new Product();
		monitor.setNome("Monitor");
		monitor.setPreco(new BigDecimal("1200"));
		monitor.setCategoria(informaticaSalva);

		productRepository.save(monitor);

		Product notebook = new Product();
		notebook.setNome("Notebook Gamer");
		notebook.setPreco(new BigDecimal("4500"));
		notebook.setCategoria(informaticaSalva);

		productRepository.save(notebook);

		List<Product> resultado = productRepository
				.findAll(ProductSpecifications.precoBetween(new BigDecimal("1000"), new BigDecimal("2000")));

		assertEquals(1, resultado.size());

		assertEquals("Monitor", resultado.get(0).getNome());

		assertEquals(new BigDecimal("1200"), resultado.get(0).getPreco());

	}

}
