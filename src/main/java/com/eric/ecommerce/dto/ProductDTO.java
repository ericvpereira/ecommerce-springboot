package com.eric.ecommerce.dto;

import java.math.BigDecimal;

public class ProductDTO {

	private Integer id;
	private String nome;
	private BigDecimal preco;
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
