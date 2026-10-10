package com.eric.ecommerce.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eric.ecommerce.dto.ProductDTO;
import com.eric.ecommerce.exceptions.ApiError;
import com.eric.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
@RestController
@RequestMapping("/api/products")
public class ProductController {

	ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@Operation(summary = "Listar produtos", description = "Lista os produtos com suporte a filtros e paginação")
	@ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso")
	@GetMapping
	public Page<ProductDTO> findAll(@RequestParam(required = false) BigDecimal precoMin,
			@RequestParam(required = false) BigDecimal precoMax, @RequestParam(required = false) Integer categoriaId,
			@RequestParam(required = false) String nome,
			@PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

		return productService.findWithFilters(categoriaId, nome, precoMin, precoMax, pageable);

	}

	@Operation(summary = "Buscar produto por ID", description = "Busca um produto pelo seu identificador")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Produto encontrado"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("/{id}")
	public ProductDTO findById(@PathVariable Integer id) {

		return productService.findById(id);

	}

	@Operation(summary = "Cadastrar produto", description = "Cadastra um novo produto vinculado a uma categoria existente")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados do produto inválidos"),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada") })
	@PostMapping
	public ResponseEntity<ProductDTO> createdProduct(@RequestBody @Valid ProductDTO dto) {

		ProductDTO save = productService.save(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(save);

	}

	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados do produto inválidos"),
			@ApiResponse(responseCode = "404", description = "Produto ou categoria não encontrado") })
	@PutMapping("/{id}")
	public ProductDTO update(@PathVariable Integer id, @RequestBody @Valid ProductDTO product) {

		return productService.update(id, product);

	}

	@Operation(summary = "Excluir produto", description = "Remove um produto existente pelo seu identificador")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {

		productService.deleteById(id);

		return ResponseEntity.noContent().build();

	}

}
