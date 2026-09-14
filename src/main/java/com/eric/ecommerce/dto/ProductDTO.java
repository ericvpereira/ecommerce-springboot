package com.eric.ecommerce.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {

	private Integer id;

	@NotBlank
	private String nome;

	@DecimalMin("0")
	private BigDecimal preco;

	@NotNull
	private Integer categoriaId;

	public ProductDTO() {

	}

	public ProductDTO(Integer id, String nome, BigDecimal preco, Integer categoriaId) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.categoriaId = categoriaId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

}
