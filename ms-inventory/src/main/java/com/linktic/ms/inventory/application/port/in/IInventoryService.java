package com.linktic.ms.inventory.application.port.in;


import com.linktic.ms.inventory.application.dto.InventoryDTO;
import org.springframework.http.ResponseEntity;

public interface IInventoryService {

    ResponseEntity<?> getProductById(Long productId);

    ResponseEntity<?> updateInventoryByProductId(InventoryDTO inventoryDTO);
}

