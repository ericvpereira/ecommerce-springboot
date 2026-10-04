package com.eric.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.repository.CategoriaRepository;

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

}
