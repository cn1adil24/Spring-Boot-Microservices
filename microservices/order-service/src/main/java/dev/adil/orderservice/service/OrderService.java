package dev.adil.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import dev.adil.orderservice.dto.InventoryResponse;
import dev.adil.orderservice.dto.OrderLineItemsDto;
import dev.adil.orderservice.dto.OrderRequest;
import dev.adil.orderservice.dto.OrderResponse;
import dev.adil.orderservice.exception.OutOfStockException;
import dev.adil.orderservice.model.Order;
import dev.adil.orderservice.model.OrderLineItems;
import dev.adil.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private WebClient webClient;
	private final String inventoryServiceUri = "http://localhost:8082/api/v1/inventory";
	
	public void addOrder(OrderRequest orderRequest) {		
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsList()
				.stream()
				.map(this::mapToOrderLineItems)
				.toList();
		
		Order order = Order.builder()
			.orderNumber(UUID.randomUUID().toString())
			.orderLineItemsList(orderLineItems)
			.build();
		
		List<String> skuCodeList = order.getOrderLineItemsList()
										.stream()
										.map(OrderLineItems::getSkuCode)	//equivalent to 'orderLineItem -> orderLineItem.getSkuCode()'
										.toList();
		
		InventoryResponse[] inventoryResponseArray = webClient.get()
				.uri(
						inventoryServiceUri,
						uriBuilder -> uriBuilder.queryParam("sku_code", skuCodeList).build()
					)
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();
		
		List<String> missingProducts = Arrays.stream(inventoryResponseArray)
											.filter(inventoryResponse -> !inventoryResponse.getIsInStock())
											.map(inventoryResponse -> inventoryResponse.getSkuCode())
											.toList();
		
		if (missingProducts.isEmpty()) {
			orderRepository.save(order);
			log.info("Order {} is saved.", order.getId());
		}
		else {
			throw new OutOfStockException("The following products are not in stock: " + String.join(", ", missingProducts));
		}
	}

	private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
		return OrderLineItems.builder()
				.skuCode(orderLineItemsDto.getSkuCode())
				.price(orderLineItemsDto.getPrice())
				.quantity(orderLineItemsDto.getQuantity())
				.build();
	}

	public List<OrderResponse> getAllOrders() {
		List<Order> orderList = orderRepository.findAll();
		
		return orderList.stream()
				.map(this::OrderResponse)
				.toList();
	}

	private OrderResponse OrderResponse(Order order) {
		return OrderResponse.builder()
				.id(order.getId())
				.orderNumber(order.getOrderNumber())
				.orderLineItemsList(
						order.getOrderLineItemsList()
							 .stream()
							 .map(this::mapToDto)
							 .toList()
						)
				.build();
	}
	
	private OrderLineItemsDto mapToDto(OrderLineItems orderLineItems) {
		return OrderLineItemsDto.builder()
				.Id(orderLineItems.getId())
				.skuCode(orderLineItems.getSkuCode())
				.price(orderLineItems.getPrice())
				.quantity(orderLineItems.getQuantity())
				.build();
	}
}
