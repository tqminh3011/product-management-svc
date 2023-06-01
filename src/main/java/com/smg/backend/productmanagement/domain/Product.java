package com.smg.backend.productmanagement.domain;

import com.google.common.hash.Hashing;
import com.smg.backend.productmanagement.enums.ProductType;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public record Product(
        String id,
        String name,
        String productOwner,
        ProductType type,
        BigDecimal price,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {
    public String generateContentHash() {
        final String originalString = id + name + productOwner + type + price + createdDate;
        return Hashing.sha256().hashString(originalString, StandardCharsets.UTF_8).toString();
    }
}
