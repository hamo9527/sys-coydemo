package com.cailu.bom.erp.interfaces;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        String category,
        String unit,
        String spec,
        @NotNull BigDecimal purchasePrice,
        @NotNull BigDecimal salePrice
) {
}
