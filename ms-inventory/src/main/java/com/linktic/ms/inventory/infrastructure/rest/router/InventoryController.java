package com.linktic.ms.inventory.infrastructure.rest.router;

import com.linktic.ms.inventory.application.dto.InventoryDTO;
import com.linktic.ms.inventory.application.port.in.IInventoryService;
import com.linktic.ms.inventory.infrastructure.rest.router.doc.InventoryDoc;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class InventoryController implements InventoryDoc {

    private final IInventoryService inventaryService;

    @Override
    public ResponseEntity<?> getByProductId(Long id) {
        return inventaryService.getProductById(id);
    }

    @Override
    public ResponseEntity<?> updateInventory(InventoryDTO inventoryDTO) {
        return inventaryService.updateInventoryByProductId(inventoryDTO);
    }
}