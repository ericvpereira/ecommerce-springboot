package com.eric.ecommerce.dto;

import org.antlr.v4.runtime.misc.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public class UpdateCartItemRequest {

	@NotNull
	@Min(1)
	@Schema(description = "Nova quantidade do produto", example = "3")
	private Integer quantidade;

	public UpdateCartItemRequest() {
	}

	public UpdateCartItemRequest(@Min(1) Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

}
