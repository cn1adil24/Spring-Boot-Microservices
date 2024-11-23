package dev.adil.orderservice.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
import dev.adil.orderservice.exception.OutOfStockException;
import dev.adil.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod ")
	public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
		try {
			orderService.addOrder(orderRequest);
			return CompletableFuture.supplyAsync(() -> "Order placed successfully");
		} catch(OutOfStockException ex) {
			return CompletableFuture.supplyAsync(() -> ex.getMessage());
		}
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<OrderResponse> getAllOrders(){
		return orderService.getAllOrders();
	}
	
	public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException ex) {
		return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please try again after some time.");
	}
}
