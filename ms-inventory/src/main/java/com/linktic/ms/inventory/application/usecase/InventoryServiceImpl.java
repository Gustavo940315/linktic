package com.linktic.ms.inventory.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.ms.inventory.application.dto.InventoryDTO;
import com.linktic.ms.inventory.application.dto.ProductDTO;
import com.linktic.ms.inventory.application.dto.ProductInformationDTO;
import com.linktic.ms.inventory.application.port.in.IInventoryService;
import com.linktic.ms.inventory.application.port.in.IProductServiceFeign;
import com.linktic.ms.inventory.application.port.out.InventoryRepository;
import com.linktic.ms.inventory.config.exception.MyHandleException;
import com.linktic.ms.inventory.model.InventoryEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class InventoryServiceImpl implements IInventoryService {

    private final InventoryRepository inventoryRepository;
    private final IProductServiceFeign productServiceFeign;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<?> getProductById(Long productId) {
        log.info("Starting search for productId: {}", productId);

        var productResponse = this.productServiceFeign.getById(productId);

        log.info("Converting product response to ProductDTO");
        ProductDTO product = objectMapper.convertValue(productResponse.getBody(), ProductDTO.class);

        log.info("Fetching inventory for productId: {}", productId);
        var inventoryEntity = this.inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    log.error("[Get Product By ID] No inventory found for productId: {}", productId);
                    return new MyHandleException("Product not found");
                });

        log.info("Mapping data to ProductInformationDTO");
        ProductInformationDTO productInformationDTO = mapToProductInformationDTO(product, inventoryEntity);

        log.info("Successfully retrieved product information for productId: {}", productId);
        return ResponseEntity.ok(productInformationDTO);
    }

    @Override
    public ResponseEntity<?> updateInventoryByProductId(InventoryDTO inventoryDTO) {
        log.info("Starting update for productId: {}", inventoryDTO.getProductId());

        InventoryEntity inventory = this.inventoryRepository.findByProductId(inventoryDTO.getProductId())
                .orElseThrow(() -> {
                    log.error("No inventory found for productId: {}", inventoryDTO.getProductId());
                    return new MyHandleException("Product not found");
                });

        log.info("Setting new quantity: {}", inventoryDTO.getQuantity());
        inventory.setQuantity(inventoryDTO.getQuantity());

        InventoryEntity updatedInventory = this.inventoryRepository.save(inventory);

        log.info("Successfully updated inventory for productId: {}", inventoryDTO.getProductId());
        return ResponseEntity.ok(updatedInventory);
    }

    private ProductInformationDTO mapToProductInformationDTO(ProductDTO product, InventoryEntity inventoryEntity) {
        return ProductInformationDTO.builder()
                .id(inventoryEntity.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .quantity(inventoryEntity.getQuantity())
                .build();
    }

}