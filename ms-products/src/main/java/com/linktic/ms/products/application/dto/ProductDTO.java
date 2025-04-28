package com.linktic.ms.products.application.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    @NotEmpty(message = "The product name must not be empty.")
    @NotBlank(message = "The product name must not be blank.")
    @Size(max = 255, message = "The product name must not exceed 255 characters.")
    private String name;

    @NotBlank(message = "The description must not be blank.")
    @NotEmpty(message = "The description must not be empty.")
    @Size(max = 1000, message = "The description must not exceed 1000 characters.")
    private String description;

    @NotNull(message = "The price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "The price must be greater than 0.")
    private BigDecimal price;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a cero")
    private Integer quantity;
}
