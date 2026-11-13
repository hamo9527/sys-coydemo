package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record PurchaseItemView(
        Long id,
        Long productId,
        String colorCode,
        String batchNo,
        String grade,
        String binName,
        BigDecimal qty,
        BigDecimal price,
        BigDecimal amount
) {
}
