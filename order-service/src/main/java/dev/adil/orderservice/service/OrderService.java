package dev.adil.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.adil.orderservice.dto.OrderLineItemsDto;
import dev.adil.orderservice.dto.OrderRequest;
import dev.adil.orderservice.dto.OrderResponse;
import dev.adil.orderservice.model.Order;
import dev.adil.orderservice.model.OrderLineItems;
import dev.adil.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	public void addOrder(OrderRequest orderRequest) {		
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsList()
				.stream()
				.map(this::mapToOrderLineItems)
				.toList();
		
		Order order = Order.builder()
			.orderNumber(UUID.randomUUID().toString())
			.orderLineItemsList(orderLineItems)
			.build();
		
		orderRepository.save(order);
		
		log.info("Order {} is saved.", order.getId());
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
