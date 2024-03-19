package dev.adil.inventoryservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.adil.inventoryservice.repository.InventoryRepository;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Transactional(readOnly = true)
	public boolean isInStock(String skuCode) {
		return inventoryRepository.findBySkuCode(skuCode).isPresent();
	}
}
