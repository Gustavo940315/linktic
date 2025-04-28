package com.linktic.ms.inventory.application.port.out;

import com.linktic.ms.inventory.model.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductId(Long productId);

    void deleteByProductId(Long productId);


}
