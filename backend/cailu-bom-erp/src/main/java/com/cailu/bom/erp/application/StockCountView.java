package com.cailu.bom.erp.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record StockCountView(
        Long id,
        String billNo,
        Long warehouseId,
        LocalDate billDate,
        String status,
        String remark,
        List<Item> items
) {
    public record Item(
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
}
