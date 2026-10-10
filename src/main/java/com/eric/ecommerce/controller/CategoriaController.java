package com.eric.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.ecommerce.dto.CategoriaDTO;
import com.eric.ecommerce.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
@RestController
@RequestMapping("/api/categories")
public class CategoriaController {

	private final CategoriaService categoriaService;

	public CategoriaController(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	@Operation(summary = "Listar categorias", description = "Listar todas as categorias cadastradas")
	@ApiResponse(responseCode = "200", description = "Categorias encontradas com sucesso")
	@GetMapping
	public List<CategoriaDTO> findAll() {

		return categoriaService.findAll();
	}

	@Operation(summary = "Buscar categorias por ID", description = "Busca uma categoria pelo seu identificador")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada") })
	@GetMapping("/{id}")
	public CategoriaDTO findById(@PathVariable Integer id) {

		return categoriaService.findDTOById(id);

	}

	@Operation(summary = "Cadastrar categoria", description = "Cadastra uma nova categoria")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados da categoria inválidos") })
	@PostMapping
	public ResponseEntity<CategoriaDTO> save(@RequestBody @Valid CategoriaDTO dto) {

		CategoriaDTO saved = categoriaService.save(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(saved);

	}

	@Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados com categoria inválidos"),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada") })
	@PutMapping("/{id}")
	public CategoriaDTO update(@PathVariable Integer id, @RequestBody @Valid CategoriaDTO dto) {

		return categoriaService.update(id, dto);

	}

	@Operation(summary = "Excluir categoria", description = "Remove uma categoria existente pelo seu identificador")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

		categoriaService.deleteById(id);

		return ResponseEntity.noContent().build();

	}

}
