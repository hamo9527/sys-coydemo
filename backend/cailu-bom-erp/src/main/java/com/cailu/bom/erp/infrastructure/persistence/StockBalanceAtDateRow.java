package com.cailu.bom.erp.infrastructure.persistence;

import java.math.BigDecimal;

public record StockBalanceAtDateRow(
        Long warehouseId,
        Long productId,
        String colorCode,
        String batchNo,
        String grade,
        String binName,
        BigDecimal quantity
) {
}
