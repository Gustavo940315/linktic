package com.linktic.ms.inventory.infrastructure.rest.router.doc;

import com.linktic.ms.inventory.application.dto.InventoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventory", description = "Inventory API")
@RequestMapping("/api/v1/inventory")
public interface InventoryDoc {

    @Operation(summary = "Get inventory by product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    ResponseEntity<?> getByProductId(@PathVariable("id") Long id);

    @Operation(summary = "Update inventory stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update-stock")
    ResponseEntity<?> updateInventory(@Valid @RequestBody InventoryDTO inventoryDTO);
}