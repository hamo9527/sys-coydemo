package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record StockView(
        Long id,
        Long warehouseId,
        Long productId,
        BigDecimal quantity,
        BigDecimal lockedQty
) {
}
