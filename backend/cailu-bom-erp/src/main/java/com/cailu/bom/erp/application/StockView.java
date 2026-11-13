package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record StockView(
        Long id,
        Long warehouseId,
        Long productId,
        String colorCode,
        String batchNo,
        String grade,
        String binName,
        BigDecimal quantity,
        BigDecimal lockedQty
) {
}
