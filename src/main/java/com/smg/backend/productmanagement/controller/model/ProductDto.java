package com.smg.backend.productmanagement.controller.model;

import java.math.BigDecimal;

public record ProductDto(
        String productOwnerId,
        String name,
        String type,
        BigDecimal price
) { }
