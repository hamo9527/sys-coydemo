package com.cailu.bom.erp.domain.model;

import java.time.LocalDate;
import java.util.List;

public record StockCount(
        Long id,
        String billNo,
        Long warehouseId,
        LocalDate billDate,
        String status,
        String remark,
        List<StockCountItem> items
) {
    public StockCount {
        if (billNo == null || billNo.isBlank()) {
            throw new IllegalArgumentException("billNo required");
        }
        if (warehouseId == null || billDate == null) {
            throw new IllegalArgumentException("warehouse and bill date required");
        }
        items = items == null ? List.of() : List.copyOf(items);
    }
}
