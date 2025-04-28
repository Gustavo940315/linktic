package com.linktic.ms.inventory.infrastructure.rest.router.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.ms.inventory.application.event.ProductCreatedEvent;
import com.linktic.ms.inventory.application.port.out.InventoryRepository;
import com.linktic.ms.inventory.config.exception.MyHandleException;
import com.linktic.ms.inventory.model.InventoryEntity;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Payload;

@Log4j2
@EnableScheduling
@Service
@RequiredArgsConstructor
public class SqsInventoryListener {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SqsListener("${aws.sqs.product-created-queue-name}")
    public void receiveCreateMessage(@Payload String message) {
        log.info("Received message: {}", message);
        try {
            ProductCreatedEvent event = objectMapper.readValue(message, ProductCreatedEvent.class);
            InventoryEntity inventory = mapToInventoryEntity(event);
            inventoryRepository.save(inventory);
            log.info("Inventory created for productId: {}", event.getProductId());
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing create message from SQS", e);
        }
    }

    @Transactional
    @SqsListener("${aws.sqs.product-deleted-queue-name}")
    public void receiveDeleteMessage(@Payload String message) {
        log.info("Received delete message: {}", message);
        try {
            long productId = Long.parseLong(message);
            inventoryRepository.deleteByProductId(productId);
            log.info("Inventory deleted for productId: {}", productId);
        } catch (Exception e) {
            log.error("Error processing delete message: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing delete message from SQS", e);
        }
    }

    private InventoryEntity mapToInventoryEntity(ProductCreatedEvent event) {
        return InventoryEntity.builder()
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .build();
    }
}