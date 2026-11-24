package com.cailu.bom.erp.domain.model;

import java.math.BigDecimal;

public record StockCountItem(
        Long id,
        Long productId,
        String colorCode,
        String batchNo,
        String grade,
        String binName,
        BigDecimal costPrice,
        BigDecimal bookQty,
        BigDecimal actualQty,
        BigDecimal differenceQty
) {
    public StockCountItem {
        costPrice = costPrice == null ? BigDecimal.ZERO : costPrice;
        bookQty = bookQty == null ? BigDecimal.ZERO : bookQty;
    }

    public BigDecimal differenceAmount() {
        return differenceQty == null ? null : differenceQty.multiply(costPrice);
    }
}
