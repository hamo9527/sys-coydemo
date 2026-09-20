package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record PurchaseItemView(
        Long id,
        Long productId,
        BigDecimal qty,
        BigDecimal price,
        BigDecimal amount
) {
}
