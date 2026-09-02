package com.eric.ecommerce.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;

@Entity
public class Product {

	private Integer id;
	private Categoria categoria;
	private String nome;
	private BigDecimal preco;

	public Product(Integer id, Categoria categoria, String nome, BigDecimal preco) {
		this.id = id;
		this.categoria = categoria;
		this.nome = nome;
		this.preco = preco;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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

	public Integer getId() {
		return id;
	}

}
