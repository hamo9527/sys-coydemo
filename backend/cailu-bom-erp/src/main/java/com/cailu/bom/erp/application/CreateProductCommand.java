package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record CreateProductCommand(
        String sku,
        String name,
        String category,
        String unit,
        String spec,
        BigDecimal purchasePrice,
        BigDecimal salePrice
) {
    public CreateProductCommand {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
    }
}
