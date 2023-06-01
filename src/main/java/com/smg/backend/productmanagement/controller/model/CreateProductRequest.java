package com.smg.backend.productmanagement.controller.model;

import com.smg.backend.productmanagement.enums.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank
        String name,

        @NotBlank
        String productOwner,

        @NotNull
        ProductType type,

        @NotNull
        @DecimalMin(value = "0.00", inclusive = false)
        @Digits(integer = 10, fraction = 2)
        BigDecimal price
) {
}
