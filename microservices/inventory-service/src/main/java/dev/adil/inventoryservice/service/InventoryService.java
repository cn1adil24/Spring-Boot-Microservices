package dev.adil.inventoryservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.adil.inventoryservice.dto.InventoryResponse;
import dev.adil.inventoryservice.model.Inventory;
import dev.adil.inventoryservice.repository.InventoryRepository;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Transactional(readOnly = true)
	public boolean isInStock(String skuCode) {
		return inventoryRepository.findBySkuCode(skuCode).isPresent();
	}
	
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCodeList) {
		return inventoryRepository.findBySkuCodeIn(skuCodeList)
								.stream()
								.map(this::mapToInventoryResponse)
								.toList();
	}
	
	private InventoryResponse mapToInventoryResponse(Inventory inventory) {
		return InventoryResponse.builder()
						.skuCode(inventory.getSkuCode())
						.isInStock(inventory.getQuantity() > 0)
						.build();
	}
}
