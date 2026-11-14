package com.cailu.bom.erp.domain.model;

import java.math.BigDecimal;

public record StockCountItem(
        Long id,
        Long productId,
        String colorCode,
        String batchNo,
        String grade,
        String binName,
        BigDecimal bookQty,
        BigDecimal actualQty,
        BigDecimal differenceQty
) {
}
