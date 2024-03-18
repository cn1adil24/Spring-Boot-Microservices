package dev.adil.orderservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
	private Long id;
	private String orderNumber;
	private List<OrderLineItemsDto> orderLineItemsList;
}
