package dev.adil.orderservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.adil.orderservice.dto.OrderRequest;
import dev.adil.orderservice.dto.OrderResponse;
import dev.adil.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String placeOrder(@RequestBody OrderRequest orderRequest) {
		orderService.addOrder(orderRequest);
		return "Order placed successfully";
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<OrderResponse> getAllOrders(){
		return orderService.getAllOrders();
	}
}
