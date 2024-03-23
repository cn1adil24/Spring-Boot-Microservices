package dev.adil.orderservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemsDto {
	private Long Id;
	private String skuCode;
	private BigDecimal price;
	private Integer quantity;
}
