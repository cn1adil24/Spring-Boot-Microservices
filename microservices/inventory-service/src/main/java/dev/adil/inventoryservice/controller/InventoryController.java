package dev.adil.inventoryservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.adil.inventoryservice.dto.InventoryResponse;
import dev.adil.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping("/{sku_code}")
	@ResponseStatus(HttpStatus.OK)
	public boolean isInStock(@PathVariable("sku_code") String skuCode) {
		return inventoryService.isInStock(skuCode);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInStock(@RequestParam("sku_code") List<String> skuCode) {
		return inventoryService.isInStock(skuCode);
	}
}
