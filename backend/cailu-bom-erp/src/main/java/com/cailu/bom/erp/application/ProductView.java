package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record ProductView(
        Long id,
        String sku,
        String name,
        String unit,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Integer status
) {
}
