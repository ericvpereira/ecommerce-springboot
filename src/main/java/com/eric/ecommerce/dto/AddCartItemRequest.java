package com.eric.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddCartItemRequest {

	@NotNull
	@Schema(description = "Identificador do produto", example = "1")
	private Integer productId;

	@NotNull
	@Min(1)
	@Schema(description = "Quantidade do produto", example = "2")
	private Integer quantidade;

	public AddCartItemRequest() {
	}

	public AddCartItemRequest(Integer productId, Integer quantidade) {
		this.productId = productId;
		this.quantidade = quantidade;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

}
